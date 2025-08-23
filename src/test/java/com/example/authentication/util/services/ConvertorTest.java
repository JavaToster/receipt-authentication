package com.example.authentication.util.services;

import com.example.authentication.DTO.auth.AuthenticationDataDTO;
import com.example.authentication.DTO.user.UserDTO;
import com.example.authentication.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConvertorTest {

    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private Convertor convertor;

    private final long TELEGRAM_ID = 1111;

    @Test
    void convertToUserShouldReturnConvertedUser() {
        AuthenticationDataDTO authDTO = new AuthenticationDataDTO(TELEGRAM_ID, "Some username", "1111", "recovery@gmail.com");
        User expectedUser = new User();
        expectedUser.setTelegramId(TELEGRAM_ID);
        expectedUser.setUsername("Some username");
        expectedUser.setPassword("1111");
        expectedUser.setRecoveryEmail("recovery@gmail.com");

        when(modelMapper.map(authDTO, User.class)).thenReturn(expectedUser);

        User actualUser = convertor.convertToUser(authDTO);

        assertEquals(expectedUser, actualUser);

    }

    @Test
    void convertToUserDTO() {
        User user = new User();
        user.setTelegramId(TELEGRAM_ID);
        user.setPassword("1111");
        user.setRecoveryEmail("recovery@gmail.com");
        user.setUsername("username");

        UserDTO expectedUserDTO = new UserDTO(TELEGRAM_ID, "username", "recovery@gmail.com");

        when(modelMapper.map(user, UserDTO.class)).thenReturn(expectedUserDTO);

        UserDTO actualUserDTO = convertor.convertToUserDTO(user);

        assertEquals(expectedUserDTO, actualUserDTO);
    }
}