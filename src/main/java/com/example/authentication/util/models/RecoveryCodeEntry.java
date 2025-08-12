package com.example.authentication.util.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecoveryCodeEntry {
    private String code;
    private boolean isAuthenticated;

    public RecoveryCodeEntry(String code){
        this.code = code;
    }
}


