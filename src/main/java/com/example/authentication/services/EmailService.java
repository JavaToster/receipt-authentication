package com.example.authentication.services;


import com.example.authentication.DTO.auth.RecoveryCodeForEmailSenderDTO;
import com.example.authentication.clients.EmailSenderClient;
import com.example.authentication.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final UserRepository userRepository;
    private final EmailSenderClient emailSenderClient;
    @Value("${clients.header.key}")
    private String authKey;

    @Async("taskExecutor")
    public void sendRestoreCode(long telegramId, String code){
        String email = userRepository.findEmailByTelegramId(telegramId).orElseThrow(EntityNotFoundException::new);

        emailSenderClient.sendRecoveryCode(new RecoveryCodeForEmailSenderDTO(email, code), authKey);
    }

}

