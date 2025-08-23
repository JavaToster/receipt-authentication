package com.example.authentication.util.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RecoveryCodesGeneratorTest {

    @InjectMocks
    private RecoveryCodesGenerator recoveryCodesGenerator;

    @Test
    void generate() {
        int code = Integer.parseInt(recoveryCodesGenerator.generate());
        assertTrue(100_000 <= code && 999_999 >= code);
    }
}