package com.example.authentication.validator;

import com.example.authentication.utilServices.ErrorMessageCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import jakarta.validation.ValidationException;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthValidatorTest {

    @InjectMocks
    private AuthValidator authValidator;
    @Mock
    private ErrorMessageCreator creator;

    @Test
    void shouldThrowValidationException() {
        BindingResult errors = Mockito.mock(BindingResult.class);

        when(errors.hasErrors()).thenReturn(true);
        when(creator.createErrorMessage(errors)).thenReturn("Hello world");

        assertThrows(ValidationException.class, () -> authValidator.validate(errors));
    }

    @Test
    void shouldNoThrowValidationException(){
        BindingResult errors = Mockito.mock(BindingResult.class);

        when(errors.hasErrors()).thenReturn(false);

        assertDoesNotThrow(() -> authValidator.validate(errors));
    }
}