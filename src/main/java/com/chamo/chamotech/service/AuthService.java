package com.chamo.chamotech.service;

import com.chamo.chamotech.constants.MessageConstants;
import com.chamo.chamotech.dto.auth.AuthResponseDTO;
import com.chamo.chamotech.dto.auth.LoginRequestDTO;
import com.chamo.chamotech.dto.auth.RegisterRequestDTO;
import com.chamo.chamotech.entity.UserEntity;
import com.chamo.chamotech.enums.Role;
import com.chamo.chamotech.exception.InvalidCredentialsException;
import com.chamo.chamotech.exception.ResourceConflictException;
import com.chamo.chamotech.repository.UserRepository;
import com.chamo.chamotech.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponseDTO login(LoginRequestDTO request) {
        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException(MessageConstants.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException(MessageConstants.INVALID_CREDENTIALS);
        }

        String token = jwtService.generateToken(user.getId(), user.getRole().name(), user.getUsername());

        return new AuthResponseDTO(token, user.getUsername(), user.getRole().name());
    }

    public void register(RegisterRequestDTO request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ResourceConflictException(MessageConstants.USERNAME_TAKEN);
        }

        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);
    }
}
