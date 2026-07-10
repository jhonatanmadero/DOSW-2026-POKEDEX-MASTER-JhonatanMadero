package com.pokedex.core.exception;

/** Error de validacion de reglas de negocio (ej. equipo con mas de 6 Pokemon). */
public class ValidationException extends BusinessException {
    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR");
    }
}
