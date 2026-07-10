package com.pokedex.core.exception;

/** El usuario autenticado no tiene permiso sobre el recurso (RN-02: solo sus equipos/favoritos). */
public class ForbiddenOperationException extends BusinessException {
    public ForbiddenOperationException(String message) {
        super(message, "FORBIDDEN");
    }
}
