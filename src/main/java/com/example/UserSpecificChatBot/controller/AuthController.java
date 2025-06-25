package com.example.UserSpecificChatBot.controller;

import com.example.UserSpecificChatBot.dto.AuthRequest;
import com.example.UserSpecificChatBot.dto.AuthResponse;
import com.example.UserSpecificChatBot.model.User;
import com.example.UserSpecificChatBot.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired private AuthService authService;

    @GetMapping("/health")
    public String health() {
        return "good";
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        authService.register(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @GetMapping("/me")
    public User me(@RequestHeader("Authorization") String authHeader) {
        String token = "";
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }
        return authService.getCurrentUser(token);
    }
}
