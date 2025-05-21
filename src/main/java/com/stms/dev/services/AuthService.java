package com.stms.dev.services;

import com.stms.dev.dto.UserDTO;
import com.stms.dev.models.Role;
import com.stms.dev.models.User;
import com.stms.dev.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String createNewUser(UserDTO userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            return "Username already exists";
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            return "Email already exists";
        }
        try {
            User newUser = User.builder()
                    .name(userDto.getName())
                    .username(userDto.getUsername())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .email(userDto.getEmail())
                    .role(Role.DEVELOPER)
                    .build();
            userRepository.save(newUser);
            return "User Registered Successfully";
        } catch (Exception e) {
            return "Error creating user" + e.getMessage();
        }
    }

    public String getUserRole(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(value -> value.getRole().name()).orElse(null);
    }

}
