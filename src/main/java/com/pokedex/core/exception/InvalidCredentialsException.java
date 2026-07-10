package com.pokedex.core.exception;

/** Credenciales invalidas al iniciar sesion (RF-01, RF-02). */
public class InvalidCredentialsException extends BusinessException {
    public InvalidCredentialsException() {
        super("Correo o contrasena incorrectos", "INVALID_CREDENTIALS");
    }
}
