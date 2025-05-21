package com.stms.dev.controllers;

import com.stms.dev.dto.LoginRequest;
import com.stms.dev.dto.UserDTO;
import com.stms.dev.services.AuthService;
import com.stms.dev.services.JWTService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, JWTService jwtService) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("register")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserDTO newUser) {
        String message = authService.createNewUser(newUser);
        if (message.contains("already exists") || message.contains("Error")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        }
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String role = authService.getUserRole(userDetails.getUsername());
            String token = jwtService.generateToken(userDetails.getUsername(), role);
            return ResponseEntity.status(HttpStatus.OK).body(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username or password");
        }
    }
}
