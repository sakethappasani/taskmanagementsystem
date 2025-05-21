package com.stms.dev.services;

import com.stms.dev.dto.ChangePasswordDTO;
import com.stms.dev.dto.ProfileUpdateDTO;
import com.stms.dev.dto.UserProfileDTO;
import com.stms.dev.models.User;
import com.stms.dev.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserProfileDTO getUserProfile(String username) {
        Optional<User> userInDb = userRepository.findByUsername(username);
        if (userInDb.isPresent()) {
            User user = userInDb.get();
            return UserProfileDTO.builder()
                    .username(user.getUsername())
                    .name(user.getName())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .build();
        } else return null;
    }

    public String updateUserProfile(String username, ProfileUpdateDTO profileUpdateDTO) {
        Optional<User> userInDb = userRepository.findByUsername(username);
        if (userInDb.isEmpty()) {
            return "User Not Found";
        }
        Optional<User> emailOwner = userRepository.findByEmail(profileUpdateDTO.getEmail());
        if (emailOwner.isPresent() && !emailOwner.get().getUsername().equals(username)) {
            return "Email Already Exists";
        }
        User user = userInDb.get();
        user.setName(profileUpdateDTO.getName());
        user.setEmail(profileUpdateDTO.getEmail());
        userRepository.save(user);
        return "Profile Updated Successfully";
    }

    public String updatePassword(String username, ChangePasswordDTO changePasswordDTO) {
        Optional<User> userInDb = userRepository.findByUsername(username);
        if (userInDb.isEmpty()) {
            return "User Not Found";
        }
        User user = userInDb.get();
        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
            return "Incorrect Old Password";
        }
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);
        return "Password Changed Successfully";
    }
}