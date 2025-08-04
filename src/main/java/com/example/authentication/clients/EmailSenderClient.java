package com.example.authentication.clients;

import com.example.authentication.DTO.auth.RecoveryCodeForEmailSenderDTO;
import com.example.authentication.config.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "email-sender", url = "${clients.email-sender.url}", configuration = FeignConfiguration.class)
public interface EmailSenderClient {
    @PostMapping("/send_recovery_code")
    void sendRecoveryCode(@RequestBody RecoveryCodeForEmailSenderDTO emailSenderDTO, @RequestHeader("Authorization-key") String authorizationKey);
}
