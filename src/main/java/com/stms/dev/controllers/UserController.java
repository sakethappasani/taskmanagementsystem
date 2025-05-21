package com.stms.dev.controllers;

import com.stms.dev.dto.ChangePasswordDTO;
import com.stms.dev.dto.ProfileUpdateDTO;
import com.stms.dev.dto.UserProfileDTO;
import com.stms.dev.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            UserProfileDTO user = userService.getUserProfile(username);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User No Longer Exists");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

    @PutMapping("profile")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody ProfileUpdateDTO profileUpdateDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            String message = userService.updateUserProfile(username, profileUpdateDTO);

            if (message.equals("Profile Updated Successfully")) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(message);
            } else if (message.equals("Email Already Exists")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

    @PutMapping("change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            String message = userService.updatePassword(username, changePasswordDTO);
            if (message.equals("Password Changed Successfully")) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(message);
            } else if (message.equals("Incorrect Old Password")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

}
