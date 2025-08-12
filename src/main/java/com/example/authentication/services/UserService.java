package com.example.authentication.services;


import com.example.authentication.DTO.auth.AuthenticationDataDTO;
import com.example.authentication.DTO.user.UserDTO;
import com.example.authentication.forExceptions.exceptions.AuthenticationException;
import com.example.authentication.model.User;
import com.example.authentication.repositories.UserRepository;
import com.example.authentication.security.JwtUtil;
import com.example.authentication.util.services.Convertor;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final Convertor convertor;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            throw new Exception("No using this method for search user!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public UserDetails loadUserByTelegramId(long id){
        User user = userRepository.findByTelegramId(id).orElseThrow(() -> new EntityNotFoundException("User not found"));

        return new com.example.authentication.security.UserDetails(user);
    }

    public UserDTO findByTelegramId(long id){
        com.example.authentication.security.UserDetails userDetails = (com.example.authentication.security.UserDetails) loadUserByTelegramId(id);

        return convertor.convertToUserDTO(userDetails.getUser());

    }

    @Transactional
    public String singUp(AuthenticationDataDTO dto) {
        // Проверяем существование пользователя
        if (userRepository.findByTelegramId(dto.getTelegramId()).isPresent()) {
            throw new AuthenticationException("User already exists");
        }

        User newUser = convertor.convertToUser(dto);
        newUser.setId(0);
        newUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(newUser); // Теперь сохранится новый объект

        return jwtUtil.generateToken(dto.getTelegramId());
    }

    public String singIn(AuthenticationDataDTO data){
        UserDetails userDetails;
        try {
            userDetails = loadUserByTelegramId(data.getTelegramId());
        }catch (EntityNotFoundException e){
            throw new AuthenticationException("User with telegram id not found!");
        }

        String passwordOfUser = userDetails.getPassword();
        String receivedPassword = data.getPassword();
        if(!passwordEncoder.matches(receivedPassword, passwordOfUser)){
            throw new AuthenticationException("Wrong password!");
        }
        return jwtUtil.generateToken(data.getTelegramId());
    }

    @Transactional
    public void replacePassword(AuthenticationDataDTO data) {
        User user = userRepository.findByTelegramId(data.getTelegramId()).orElseThrow(EntityNotFoundException::new);

        user.setPassword(passwordEncoder.encode(data.getPassword()));
        user.setTelegramId(data.getTelegramId());
        userRepository.save(user);
    }

    public List<UserDTO> getSeveral(List<Long> ids) {
        List<User> users = userRepository.findByTelegramIdIn(ids);

        return users.stream().map(convertor::convertToUserDTO).toList();
    }
}
