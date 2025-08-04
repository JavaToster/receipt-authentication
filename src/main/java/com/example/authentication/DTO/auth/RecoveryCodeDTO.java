package com.example.authentication.DTO.auth;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RecoveryCodeDTO {
    @Min(value = 1, message = "Id should be from 1")
    private long telegramId;
    @NotBlank(message = "Recovery code should be not empty")
    private String recoveryCode;
}
