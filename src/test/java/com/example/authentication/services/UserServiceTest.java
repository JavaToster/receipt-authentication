package com.example.authentication.services;

import com.example.authentication.DTO.auth.AuthenticationDataDTO;
import com.example.authentication.forExceptions.exceptions.AuthenticationException;
import com.example.authentication.model.User;
import com.example.authentication.repositories.UserRepository;
import com.example.authentication.security.JwtUtil;
import com.example.authentication.utilServices.Convertor;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Convertor convertor;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    private AuthenticationDataDTO dto;
    private User user;

    @BeforeEach
    void setUp() {
        dto = new AuthenticationDataDTO();
        dto.setTelegramId(12345L);
        dto.setPassword("plainPass");

        user = new User();
        user.setTelegramId(dto.getTelegramId());
        user.setPassword("encodedPass");
    }

    @Test
    void testSignUpSuccess() {
        when(userRepository.findByTelegramId(dto.getTelegramId())).thenReturn(Optional.empty());
        when(convertor.convertToUser(dto)).thenReturn(user);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPass");
        when(jwtUtil.generateToken(dto.getTelegramId())).thenReturn("jwtToken");

        String token = userService.singUp(dto);

        assertEquals("jwtToken", token);
        verify(userRepository).save(user);
        verify(passwordEncoder).encode("plainPass");
    }

    @Test
    void testSignUpDuplicate() {
        when(userRepository.findByTelegramId(dto.getTelegramId())).thenReturn(Optional.of(user));
        AuthenticationException ex = assertThrows(AuthenticationException.class, () -> userService.singUp(dto));
        assertEquals("User already exists", ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testSignInSuccess() {
        when(userRepository.findByTelegramId(dto.getTelegramId())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(dto.getTelegramId())).thenReturn("jwtToken");

        String token = userService.singIn(dto);

        assertEquals("jwtToken", token);
        verify(passwordEncoder).matches("plainPass", "encodedPass");
    }

    @Test
    void testSignInUserNotFound() {
        when(userRepository.findByTelegramId(dto.getTelegramId())).thenReturn(Optional.empty());
        AuthenticationException ex = assertThrows(AuthenticationException.class, () -> userService.singIn(dto));
        assertEquals("User with telegram id not found!", ex.getMessage());
    }

    @Test
    void testSignInWrongPassword() {
        when(userRepository.findByTelegramId(dto.getTelegramId())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.getPassword(), user.getPassword())).thenReturn(false);
        AuthenticationException ex = assertThrows(AuthenticationException.class, () -> userService.singIn(dto));
        assertEquals("Wrong password!", ex.getMessage());
    }

    @Test
    void testReplacePasswordSuccess() {
        when(userRepository.findByTelegramId(dto.getTelegramId())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("newEncoded");

        userService.replacePassword(dto);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals("newEncoded", captor.getValue().getPassword());
    }

    @Test
    void testReplacePasswordUserNotFound() {
        when(userRepository.findByTelegramId(dto.getTelegramId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.replacePassword(dto));
    }

    @Test
    void testLoadUserByTelegramIdSuccess() {
        when(userRepository.findByTelegramId(12345L)).thenReturn(Optional.of(user));
        UserDetails details = userService.loadUserByTelegramId(12345L);
        assertNotNull(details);
        assertEquals(user.getPassword(), details.getPassword());
    }

    @Test
    void testLoadUserByTelegramIdNotFound() {
        when(userRepository.findByTelegramId(12345L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.loadUserByTelegramId(12345L));
    }

    @Test
    void testLoadUserByUsernameThrows() {
        assertThrows(RuntimeException.class, () -> userService.loadUserByUsername("any"));
    }
}
