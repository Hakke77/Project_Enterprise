package com.abdilhakim.enterprise.service;

import com.abdilhakim.enterprise.dto.AuthResponse;
import com.abdilhakim.enterprise.dto.LoginRequest;
import com.abdilhakim.enterprise.dto.RegisterRequest;
import com.abdilhakim.enterprise.entity.Role;
import com.abdilhakim.enterprise.entity.User;
import com.abdilhakim.enterprise.repository.RoleRepository;
import com.abdilhakim.enterprise.repository.UserRepository;
import com.abdilhakim.enterprise.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {

        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .name("USER")
                        .build()));

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .role(userRole)
                .build();

        userRepository.save(user);

        return new AuthResponse(jwtService.generateToken(user.getEmail()));
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        return new AuthResponse(jwtService.generateToken(user.getEmail()));
    }

}
