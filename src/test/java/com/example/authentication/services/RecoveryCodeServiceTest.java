package com.example.authentication.services;

import com.example.authentication.DTO.auth.RecoveryCodeDTO;
import com.example.authentication.utilModels.RecoveryCodeEntry;
import com.example.authentication.utilServices.RecoveryCodesGenerator;
import com.example.authentication.forExceptions.exceptions.AuthenticationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecoveryCodeServiceTest {
    @InjectMocks
    private RecoveryCodeService service;
    @Mock
    private RecoveryCodesGenerator generator;
}
