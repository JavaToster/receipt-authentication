package com.example.authentication.services;

import com.example.authentication.DTO.auth.RecoveryCodeForEmailSenderDTO;
import com.example.authentication.clients.EmailSenderClient;
import com.example.authentication.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailSenderClient emailSenderClient;

    @InjectMocks
    private EmailService emailService;

    private final long TELEGRAM_ID = 1111;

    @Test
    void sendRestoreCodeShouldNoThrow() {
        String code = "1111111";
        String expectedEmail = "user@gmail.com";
        RecoveryCodeForEmailSenderDTO emailCode = new RecoveryCodeForEmailSenderDTO(expectedEmail, code);
        ReflectionTestUtils.setField(emailService, "authKey", "test-key");
        when(userRepository.findEmailByTelegramId(TELEGRAM_ID)).thenReturn(Optional.of(expectedEmail));

        assertDoesNotThrow(() -> emailService.sendRestoreCode(TELEGRAM_ID, code));
        verify(emailSenderClient).sendRecoveryCode(
                new RecoveryCodeForEmailSenderDTO(expectedEmail, code), "test-key"
        );
    }

    @Test
    void sendRestoreCodeShouldThrowEntityNotFoundException(){
        String code = "1111";

        when(userRepository.findEmailByTelegramId(TELEGRAM_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> emailService.sendRestoreCode(TELEGRAM_ID, code));
    }
}