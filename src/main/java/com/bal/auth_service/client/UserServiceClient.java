package com.bal.auth_service.client;

import com.bal.auth_service.dto.RegisterRequestDTO;
import com.bal.auth_service.dto.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "USER-SERVICE")
public interface UserServiceClient {

    @GetMapping("/api/users/username/{username}/password-hash")
    String getUserPasswordHash(@PathVariable String username);

    @GetMapping("/api/users/{username}")
    UserResponseDTO getByUsername(@PathVariable String username);

    @PostMapping("/api/users/save")
    UserResponseDTO saveUser(@RequestBody RegisterRequestDTO userDTO);
}