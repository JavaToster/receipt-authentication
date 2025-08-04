package com.example.authentication.controllers;

import com.example.authentication.DTO.auth.AuthenticationDataDTO;
import com.example.authentication.DTO.auth.RecoveryCodeDTO;
import com.example.authentication.DTO.auth.RecoveryCodeRequestDTO;
import com.example.authentication.DTO.auth.SuccessAuthenticationDTO;
import com.example.authentication.services.EmailService;
import com.example.authentication.services.RecoveryCodeService;
import com.example.authentication.services.UserService;
import com.example.authentication.validator.AuthValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthValidator authValidator;
    private final UserService userService;
    private final EmailService emailService;
    private final RecoveryCodeService recoveryCodeService;

    @PostMapping("/sing_up")
    public ResponseEntity<SuccessAuthenticationDTO> singUp(@RequestBody @Valid AuthenticationDataDTO authenticationDataDTO, BindingResult errors){
        authValidator.validate(errors);

        String jwt = userService.singUp(authenticationDataDTO);
        return new ResponseEntity<>(new SuccessAuthenticationDTO(jwt), HttpStatus.OK);
    }

    @PostMapping("/sing_in")
    public ResponseEntity<SuccessAuthenticationDTO> singIn(@RequestBody @Valid AuthenticationDataDTO data, BindingResult errors){
        authValidator.validateAllFields(data);

        String jwt = userService.singIn(data);
        return new ResponseEntity<>(new SuccessAuthenticationDTO(jwt), HttpStatus.OK);
    }

    @PostMapping("/forgot_password_send_recovery_code")
    public ResponseEntity<HttpStatus> sendRecoveryCode(@RequestBody @Valid RecoveryCodeRequestDTO data, BindingResult errors){
        authValidator.validate(errors);
        String generatedCode = recoveryCodeService.addNewRecoveryCode(data.getTelegramId());
        emailService.sendRestoreCode(data.getTelegramId(), generatedCode);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/forgot_password_check_recovery_code")
    public ResponseEntity<HttpStatus> checkRecoveryCode(@RequestBody @Valid RecoveryCodeDTO recoveryCodeDTO, BindingResult errors){
        authValidator.validate(errors);

        recoveryCodeService.checkRecoveryCode(recoveryCodeDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/forgot_password_replace_password")
    public ResponseEntity<HttpStatus> replacePassword(@RequestBody @Valid AuthenticationDataDTO data, BindingResult errors){
        authValidator.validate(errors);

        userService.replacePassword(data);
        recoveryCodeService.removeRecoveryCode(data.getTelegramId());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}