package com.example.authentication.validator;

import com.example.authentication.DTO.auth.AuthenticationDataDTO;
import com.example.authentication.util.services.ErrorMessageCreator;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthValidatorTest {

    @Mock
    private ErrorMessageCreator creator;

    @InjectMocks
    private AuthValidator validator;

    @Test
    void validate_ShouldThrowException_WhenErrorsExist() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(creator.createErrorMessage(bindingResult)).thenReturn("some error");

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> validator.validate(bindingResult)
        );

        assertEquals("some error", ex.getMessage());
    }

    @Test
    void validate_ShouldNotThrowException_WhenNoErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        assertDoesNotThrow(() -> validator.validate(bindingResult));
    }

    @Test
    void validateAllFields_ShouldThrowException_WhenAllFieldsInvalid() {
        AuthenticationDataDTO dto = new AuthenticationDataDTO();
        dto.setTelegramId(0);
        dto.setPassword("   ");
        dto.setRecoveryEmail("invalidEmail");

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> validator.validateAllFields(dto)
        );

        assertTrue(ex.getMessage().contains("Not correct authentication data"));
    }

    @Test
    void validateAllFields_ShouldNotThrow_WhenTelegramIdExists() {
        AuthenticationDataDTO dto = new AuthenticationDataDTO();
        dto.setTelegramId(12345);

        assertDoesNotThrow(() -> validator.validateAllFields(dto));
    }

    @Test
    void validateAllFields_ShouldNotThrow_WhenPasswordExists() {
        AuthenticationDataDTO dto = new AuthenticationDataDTO();
        dto.setPassword("secret");

        assertDoesNotThrow(() -> validator.validateAllFields(dto));
    }

    @Test
    void validateAllFields_ShouldNotThrow_WhenValidEmailExists() {
        AuthenticationDataDTO dto = new AuthenticationDataDTO();
        dto.setRecoveryEmail("test@example.com");

        assertDoesNotThrow(() -> validator.validateAllFields(dto));
    }
}
