package com.pokedex.core.service.interfaces;

import com.pokedex.core.model.User;

public interface AuthService {
    /** RF-01: registra un nuevo usuario y retorna el token JWT generado. */
    record AuthResult(User user, String token) {}

    AuthResult register(User user, String rawPassword);
    AuthResult login(String email, String rawPassword);          // RF-01/flujo login
    AuthResult loginOrRegisterWithGoogle(String email, String fullName, String avatarUrl); // RF-02
}
