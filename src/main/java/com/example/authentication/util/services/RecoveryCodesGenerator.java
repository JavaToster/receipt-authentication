package com.example.authentication.util.services;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class RecoveryCodesGenerator {
    private static final int MIN_CODE = 100_000;
    private static final int MAX_CODE = 999_999;

    public String generate(){
        int generatedCode = ThreadLocalRandom.current().nextInt(MIN_CODE, MAX_CODE+1);
        return String.valueOf(generatedCode);
    }
}
