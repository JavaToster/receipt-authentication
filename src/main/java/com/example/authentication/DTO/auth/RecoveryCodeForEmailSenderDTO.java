package com.example.authentication.DTO.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecoveryCodeForEmailSenderDTO {
    private String email;
    private String code;
}
