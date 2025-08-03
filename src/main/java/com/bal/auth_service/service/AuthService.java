package com.bal.auth_service.service;


import com.bal.auth_service.client.UserServiceClient;
import com.bal.auth_service.dto.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService{
    private final UserServiceClient userServiceClient;
    private final BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
    private final ModelMapper modelMapper;
    private final JwtService jwtService;

    @Override
    public SignUpResponseDTO register(SignUpRequestDTO dto) {

        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        RegisterRequestDTO userDTO = new RegisterRequestDTO();
        userDTO.setUsername(dto.getUsername());
        userDTO.setEmail(dto.getEmail());
        userDTO.setPassword(encodedPassword);
        userDTO.setRoles(dto.getRoles());

        UserResponseDTO createdUser = userServiceClient.saveUser(userDTO);

        return new SignUpResponseDTO(createdUser.getUsername(), createdUser.getEmail());
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO dto) {
        // 1. Kullanıcıyı username ile bul
        UserResponseDTO user = userServiceClient.getByUsername(dto.getUsername());

        if (user == null) {
            throw new RuntimeException("Invalid username or password");
        }

        // 2. Password doğrulama için UserService'den hash'li password al
        String storedHashedPassword = userServiceClient.getUserPasswordHash(user.getUsername());

        // 3. Password'leri karşılaştır
        if (!passwordEncoder.matches(dto.getPassword(), storedHashedPassword)) {
            throw new RuntimeException("Invalid username or password");
        }

        // 4. JWT token oluştur (username kullanarak)
        String token = jwtService.generateToken(user.getId(), user.getUsername());

        return new LoginResponseDTO(
                token
        );
    }
    @Override
    public Long getUserIdByToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtService.extractUserId(token);
    }

}
