package com.example.authentication.utilServices;

import com.example.authentication.DTO.auth.AuthenticationDataDTO;
import com.example.authentication.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
    @Test
    void convertToUser() {
        AuthenticationDataDTO authDTO = new AuthenticationDataDTO();
        authDTO.setPassword("1111");
        authDTO.setTelegramId(123456789);
        User exceptedUser = new User();
        exceptedUser.setUsername("TosterW");
        exceptedUser.setPassword("1111");
        exceptedUser.setTelegramId(123456789);

        when(modelMapper.map(authDTO, User.class)).thenReturn(exceptedUser);

        User actualUser = convertor.convertToUser(authDTO);
        assertEquals(actualUser, exceptedUser);
    }
}