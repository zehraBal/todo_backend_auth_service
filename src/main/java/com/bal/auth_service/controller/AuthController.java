package com.bal.auth_service.controller;

import com.bal.auth_service.dto.LoginRequestDTO;
import com.bal.auth_service.dto.LoginResponseDTO;
import com.bal.auth_service.dto.SignUpRequestDTO;
import com.bal.auth_service.dto.SignUpResponseDTO;
import com.bal.auth_service.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    @GetMapping("/validate-token")
    public Long getUserIdFromToken(@RequestHeader("Authorization") String token) {
        return authService.getUserIdByToken(token);
    }

    @PostMapping("/register")
    public ResponseEntity<SignUpResponseDTO> register(@RequestBody SignUpRequestDTO signUpRequest) {
        SignUpResponseDTO response = authService.register(signUpRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}
