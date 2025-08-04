package com.example.authentication.DTO.user;

import lombok.Data;

@Data
public class UserDTO {
    private long telegramId;
    private String username;
    private String recoveryEmail;
}
