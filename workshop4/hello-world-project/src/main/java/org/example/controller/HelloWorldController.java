package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.UserLoginRequest;
import org.example.dto.UserRegisterRequest;
import org.example.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Tag(name = "HelloWorld API", description = "API for user registration, login, and profile")
public class HelloWorldController {

    @Operation(summary = "Hello World message")
    @GetMapping("/hello-world")
    public Map<String, String> helloWorld() {
        return Map.of("message", "helloworld");
    }

    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterRequest request) {
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

    @Operation(summary = "Login and get JWT token")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest request) {
        if ("user@example.com".equals(request.getEmail()) && "yourpassword".equals(request.getPassword())) {
            String token = JwtUtil.generateToken(request.getEmail());
            return ResponseEntity.ok(java.util.Map.of("token", token));
        }
        return ResponseEntity.status(401).body(java.util.Map.of("error", "Invalid credentials"));
    }

    @Operation(summary = "Get current user info from JWT token")
    @GetMapping("/me")
    public ResponseEntity<?> getMe(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(java.util.Map.of("error", "Missing or invalid Authorization header"));
        }
        String token = authHeader.substring(7);
        String email;
        try {
            email = JwtUtil.parseToken(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(java.util.Map.of("error", "Invalid token"));
        }
        return ResponseEntity.ok(java.util.Map.of(
                "email", email,
                "firstname", "John",
                "lastname", "Doe"
        ));
    }
}