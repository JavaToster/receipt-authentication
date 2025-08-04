package com.example.authentication.forExceptions;

import com.example.authentication.DTO.ErrorMessageDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHandlerController {
    @ExceptionHandler
    public ResponseEntity<ErrorMessageDTO> exceptionHandler(EntityNotFoundException exc){
        return new ResponseEntity<>(new ErrorMessageDTO(exc.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
