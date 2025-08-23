package com.example.authentication.DTO.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationDataDTO {
    @Min(value = 1, message = "Id should be from 1")
    private long telegramId;
    private String username;
    @NotBlank(message = "Password should be not empty")
    private String password;
    private String recoveryEmail;
}