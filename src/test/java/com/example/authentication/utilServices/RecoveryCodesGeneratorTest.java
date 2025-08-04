package com.example.authentication.utilServices;

import jakarta.inject.Inject;
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
        String code = recoveryCodesGenerator.generate();

        assertNotNull(code);
        assertEquals(6, code.length());
    }
}