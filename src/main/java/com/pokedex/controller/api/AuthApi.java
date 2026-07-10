package com.pokedex.controller.api;

import com.pokedex.controller.dto.request.LoginRequest;
import com.pokedex.controller.dto.request.RegisterRequest;
import com.pokedex.controller.dto.response.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** RF-01 (registro/login) y RF-02 (login con Gmail via /oauth2/authorization/google). */
@Tag(name = "Auth", description = "Registro, login y autenticacion")
@RequestMapping("/v1/auth")
public interface AuthApi {

    @Operation(summary = "Registrar usuario", description = "RF-01")
    @PostMapping("/register")
    ResponseEntity<TokenResponse> register(@Valid @RequestBody RegisterRequest request);

    @Operation(summary = "Iniciar sesion", description = "RF-01: login con correo y contrasena")
    @PostMapping("/login")
    ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request);
}
