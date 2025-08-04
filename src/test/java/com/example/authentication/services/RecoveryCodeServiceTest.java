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


    @Test
    void testAddNewRecoveryCode() {
        when(generator.generate()).thenReturn("code123");

        String code = service.addNewRecoveryCode(1L);

        assertEquals("code123", code);
        Optional<String> stored = service.getValidCode(1L);
        assertTrue(stored.isPresent());
        assertEquals("code123", stored.get());
    }

    @Test
    void testRemoveRecoveryCode() {
        when(generator.generate()).thenReturn("codeX");
        service.addNewRecoveryCode(2L);
        service.removeRecoveryCode(2L);
        assertFalse(service.getValidCode(2L).isPresent());
    }

    @Test
    void testReplaceRecoveryCode() {
        when(generator.generate()).thenReturn("oldCode");
        service.addNewRecoveryCode(3L);
        service.replace(3L, "newCode");
        Optional<String> code = service.getValidCode(3L);
        assertTrue(code.isPresent());
        assertEquals("newCode", code.get());
    }

    @Test
    void testIsValidCode() {
        when(generator.generate()).thenReturn("valCode");
        service.addNewRecoveryCode(4L);
        assertTrue(service.isValidCode(4L, "valCode"));
        assertFalse(service.isValidCode(4L, "wrong"));
        assertFalse(service.isValidCode(5L, "any"));
    }

    @Test
    void testGetValidCodeExpired() {
        RecoveryCodeEntry expired = new RecoveryCodeEntry(
                "expCode",
                Instant.now().minusSeconds(TimeUnit.MINUTES.toSeconds(1))
        );
        @SuppressWarnings("unchecked")
        ConcurrentHashMap<Long, RecoveryCodeEntry> storage =
                (ConcurrentHashMap<Long, RecoveryCodeEntry>) ReflectionTestUtils.getField(service, "storage");
        storage.put(6L, expired);

        Optional<String> code = service.getValidCode(6L);
        assertFalse(code.isPresent());
    }

    @Test
    void testScheduledCleanup() {
        RecoveryCodeEntry expired = new RecoveryCodeEntry(
                "c1",
                Instant.now().minusSeconds(60)
        );
        RecoveryCodeEntry valid = new RecoveryCodeEntry(
                "c2",
                Instant.now().plusSeconds(60)
        );
        @SuppressWarnings("unchecked")
        ConcurrentHashMap<Long, RecoveryCodeEntry> storage =
                (ConcurrentHashMap<Long, RecoveryCodeEntry>) ReflectionTestUtils.getField(service, "storage");
        storage.put(7L, expired);
        storage.put(8L, valid);

        service.scheduledCleanup();

        assertFalse(storage.containsKey(7L));
        assertTrue(storage.containsKey(8L));
    }

    @Test
    void testCheckRecoveryCodeSuccess() {
        RecoveryCodeEntry entry = new RecoveryCodeEntry(
                "okCode",
                Instant.now().plusSeconds(60)
        );
        @SuppressWarnings("unchecked")
        ConcurrentHashMap<Long, RecoveryCodeEntry> storage =
                (ConcurrentHashMap<Long, RecoveryCodeEntry>) ReflectionTestUtils.getField(service, "storage");
        storage.put(9L, entry);

        RecoveryCodeDTO dto = new RecoveryCodeDTO();
        dto.setTelegramId(9L);
        dto.setRecoveryCode("okCode");

        service.checkRecoveryCode(dto);
        assertTrue(entry.isAuthenticated());
    }

    @Test
    void testCheckRecoveryCodeFailure() {
        RecoveryCodeEntry entry = new RecoveryCodeEntry(
                "right",
                Instant.now().plusSeconds(60)
        );
        @SuppressWarnings("unchecked")
        ConcurrentHashMap<Long, RecoveryCodeEntry> storage =
                (ConcurrentHashMap<Long, RecoveryCodeEntry>) ReflectionTestUtils.getField(service, "storage");
        storage.put(10L, entry);

        RecoveryCodeDTO dto = new RecoveryCodeDTO();
        dto.setTelegramId(10L);
        dto.setRecoveryCode("wrong");

        AuthenticationException ex = assertThrows(AuthenticationException.class,
                () -> service.checkRecoveryCode(dto));
        assertEquals("incorrect authentication code", ex.getMessage());
    }

    @Test
    void testStorageOverflow() {
        @SuppressWarnings("unchecked")
        ConcurrentHashMap<Long, RecoveryCodeEntry> storage =
                (ConcurrentHashMap<Long, RecoveryCodeEntry>) ReflectionTestUtils.getField(service, "storage");
        int maxSize = (int) ReflectionTestUtils.getField(service, "MAX_SIZE");
        for (long i = 0; i < maxSize; i++) {
            storage.put(i, new RecoveryCodeEntry("c", Instant.now().plusSeconds(60)));
        }
        // Принудительно истек интервал очистки
        ReflectionTestUtils.setField(service, "lastCleanup", new AtomicLong(0L));

        assertThrows(IllegalStateException.class, () ->
                service.addNewRecoveryCode(maxSize + 1L)
        );
    }
}
