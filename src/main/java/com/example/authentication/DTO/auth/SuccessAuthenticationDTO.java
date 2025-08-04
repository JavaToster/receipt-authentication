package com.example.authentication.DTO.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessAuthenticationDTO {
    private String jwt;
}
