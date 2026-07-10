package com.pokedex.controller.impl;

import com.pokedex.controller.api.AuthApi;
import com.pokedex.controller.dto.request.LoginRequest;
import com.pokedex.controller.dto.request.RegisterRequest;
import com.pokedex.controller.dto.response.TokenResponse;
import com.pokedex.controller.mapper.UserDtoMapper;
import com.pokedex.core.exception.ValidationException;
import com.pokedex.core.model.User;
import com.pokedex.core.service.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;
    private final UserDtoMapper mapper;

    @Override
    public ResponseEntity<TokenResponse> register(RegisterRequest request) {
        if (!request.password().equals(request.confirmPassword())) {
            throw new ValidationException("Las contrasenas no coinciden");
        }
        User user = User.builder()
                .fullName(request.fullName())
                .email(request.email())
                .birthDate(request.birthDate())
                .favoriteRegion(request.favoriteRegion())
                .build();
        AuthService.AuthResult result = authService.register(user, request.password());
        return ResponseEntity.ok(mapper.toTokenResponse(result));
    }

    @Override
    public ResponseEntity<TokenResponse> login(LoginRequest request) {
        AuthService.AuthResult result = authService.login(request.email(), request.password());
        return ResponseEntity.ok(mapper.toTokenResponse(result));
    }
}
