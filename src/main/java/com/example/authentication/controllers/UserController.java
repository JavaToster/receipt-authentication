package com.example.authentication.controllers;


import com.example.authentication.DTO.user.UserDTO;
import com.example.authentication.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/get/{telegramId}")
    public ResponseEntity<UserDTO> get(@PathVariable("telegramId") long telegramId){
        UserDTO userDTO = userService.findByTelegramId(telegramId);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
}
