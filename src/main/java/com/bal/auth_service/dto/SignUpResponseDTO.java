package com.bal.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpResponseDTO {
    private String message;

    public SignUpResponseDTO(String username, String email) {
    }
}
