package com.abdilhakim.enterprise.controller;

import com.abdilhakim.enterprise.dto.AuthResponse;
import com.abdilhakim.enterprise.dto.LoginRequest;
import com.abdilhakim.enterprise.dto.RegisterRequest;
import com.abdilhakim.enterprise.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
