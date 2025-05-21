package com.stms.dev.services;

import com.stms.dev.dto.ManageRoleDTO;
import com.stms.dev.dto.UserProfileDTO;
import com.stms.dev.models.Role;
import com.stms.dev.models.User;
import com.stms.dev.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserProfileDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserProfileDTO(user.getName(), user.getUsername(), user.getEmail(), user.getRole()))
                .toList();
    }

    public boolean manageUserRole(ManageRoleDTO manageRoleDTO) {
        Optional<User> userInDb = userRepository.findByUsername(manageRoleDTO.getUsername());
        if (userInDb.isEmpty()) return false;
        User user = userInDb.get();
        Role currentRole = user.getRole();
        if (currentRole == Role.ADMIN) return false;
        if (currentRole == Role.DEVELOPER) {
            user.setRole(Role.MANAGER);
        } else if (currentRole == Role.MANAGER) {
            user.setRole(Role.DEVELOPER);
        } else {
            return false;
        }
        userRepository.save(user);
        return true;
    }
}