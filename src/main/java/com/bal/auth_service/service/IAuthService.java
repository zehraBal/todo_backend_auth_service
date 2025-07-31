package com.bal.auth_service.service;

import com.bal.auth_service.dto.LoginRequestDTO;
import com.bal.auth_service.dto.LoginResponseDTO;
import com.bal.auth_service.dto.SignUpRequestDTO;
import com.bal.auth_service.dto.SignUpResponseDTO;


public interface IAuthService {

    SignUpResponseDTO register(SignUpRequestDTO dto);
    LoginResponseDTO login(LoginRequestDTO dto);
    String getUserIdByToken(String token);
}
