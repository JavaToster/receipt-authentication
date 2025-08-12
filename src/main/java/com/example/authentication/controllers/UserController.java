package com.example.authentication.controllers;


import com.example.authentication.DTO.user.UserDTO;
import com.example.authentication.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/get/{telegramId}")
    public ResponseEntity<UserDTO> get(@PathVariable("telegramId") long telegramId){
        UserDTO userDTO = userService.findByTelegramId(telegramId);

        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/get/several")
    public ResponseEntity<List<UserDTO>> get(@RequestParam List<Long> ids){
        List<UserDTO> users = userService.getSeveral(ids);

        return ResponseEntity.ok(users);
    }
}
