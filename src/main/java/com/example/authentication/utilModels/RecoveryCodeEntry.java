package com.example.authentication.utilModels;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class RecoveryCodeEntry {
    private String code;
    private boolean isAuthenticated;

    public RecoveryCodeEntry(String code){
        this.code = code;
    }
}


