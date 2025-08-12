package com.example.authentication.services;

import com.example.authentication.util.services.RecoveryCodesGenerator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class RecoveryCodeServiceTest {
    @InjectMocks
    private RecoveryCodeService service;
    @Mock
    private RecoveryCodesGenerator generator;
}
