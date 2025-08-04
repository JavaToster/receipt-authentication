package com.example.authentication.DTO.auth;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class RecoveryCodeRequestDTO {
    @Min(value=1, message = "Id should be from 1")
    private long telegramId;
}


