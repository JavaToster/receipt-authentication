package com.example.authentication.utilServices;

import com.example.authentication.DTO.auth.AuthenticationDataDTO;
import com.example.authentication.util.services.ErrorMessageCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ErrorMessageDTOCreatorTest {

    @InjectMocks
    private ErrorMessageCreator errorMessageCreator;

    @Test
    void createErrorMessage() {
        AuthenticationDataDTO dataDTO = new AuthenticationDataDTO();

        BindingResult bindingResult = new BeanPropertyBindingResult(dataDTO, "stub");

        bindingResult.rejectValue("telegramId", "errorCode", "Wrong value");

        String exceptedMessage = "Wrong value"+";";

        String actualMessage = errorMessageCreator.createErrorMessage(bindingResult);
        assertEquals(exceptedMessage, actualMessage);
    }
}