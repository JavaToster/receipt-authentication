package com.example.authentication.services;

import com.example.authentication.DTO.auth.RecoveryCodeDTO;
import com.example.authentication.forExceptions.exceptions.AuthenticationException;
import com.example.authentication.repositories.RedisCacheRepository;
import com.example.authentication.utilModels.RecoveryCodeEntry;
import com.example.authentication.utilServices.RecoveryCodesGenerator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RecoveryCodeService {
    private final RecoveryCodesGenerator recoveryCodesGenerator;
    private final static long LIFE_TIME =  600;
    private final RedisCacheRepository redisCacheRepository;

    public String addNewRecoveryCode(long telegramId) {
        String recoveryCode = recoveryCodesGenerator.generate();

        redisCacheRepository.saveWithLifeTime(telegramId, new RecoveryCodeEntry(recoveryCode), LIFE_TIME);
        return recoveryCode;
    }

    public void removeRecoveryCode(long telegramId) {
        redisCacheRepository.removeRecoveryCodeEntry(telegramId);
    }

    public void checkRecoveryCode(RecoveryCodeDTO recoveryCodeDTO){
        Optional<RecoveryCodeEntry> recoveryCodeEntryOptional = redisCacheRepository.findRecoveryCodeEntryByTelegramId(recoveryCodeDTO.getTelegramId());
        if(recoveryCodeEntryOptional.isPresent()){
            RecoveryCodeEntry entry = recoveryCodeEntryOptional.get();
            if (entry.getCode() != null && !entry.getCode().isBlank() && entry.getCode().equals(recoveryCodeDTO.getRecoveryCode())){
                entry.setAuthenticated(true);
                redisCacheRepository.updateRecoveryCodeEntry(recoveryCodeDTO.getTelegramId(), entry);
                return;
            }
        }
        throw new AuthenticationException("Incorrect authentication code");
    }
}
