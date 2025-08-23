package com.example.authentication.util.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ErrorMessageCreatorTest {

    @InjectMocks
    private ErrorMessageCreator errorMessageCreator;

    @Test
    void createErrorMessage() {
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        ObjectError error1 = new ObjectError("field1", "value 1");
        ObjectError error2 = new ObjectError("field2", "value 2");

        when(bindingResult.getAllErrors()).thenReturn(Arrays.asList(error1, error2));

        String expected = "value 1;value 2;";

        String actual = errorMessageCreator.createErrorMessage(bindingResult);

        assertEquals(expected, actual);
    }
}