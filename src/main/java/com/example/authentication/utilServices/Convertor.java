package com.example.authentication.utilServices;

import com.example.authentication.DTO.auth.AuthenticationDataDTO;
import com.example.authentication.DTO.user.UserDTO;
import com.example.authentication.model.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@RequiredArgsConstructor
public class Convertor {

    private final ModelMapper modelMapper;

    public User convertToUser(AuthenticationDataDTO authenticationDataDTO){
        return modelMapper.map(authenticationDataDTO, User.class);
    }

    public UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}

