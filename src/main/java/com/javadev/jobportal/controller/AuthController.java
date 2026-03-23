package com.javadev.jobportal.controller;

import com.javadev.jobportal.dto.LoginRequestDTO;
import com.javadev.jobportal.dto.RegisterRequestDTO;
import com.javadev.jobportal.response.ApiResponse;
import com.javadev.jobportal.response.AuthResponse;
import com.javadev.jobportal.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponse> signIn(@RequestBody @Valid LoginRequestDTO requestDTO){
        AuthResponse res = authService.authenticate(requestDTO);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody @Valid RegisterRequestDTO requestDTO){
        ApiResponse<?> res = authService.register(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}
