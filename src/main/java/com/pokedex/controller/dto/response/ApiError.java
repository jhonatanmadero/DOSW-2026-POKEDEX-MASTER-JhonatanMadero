package com.pokedex.controller.dto.response;

import java.time.LocalDateTime;
import java.util.List;

/** Formato estandar de error de toda la API (Seccion 12.1 del plan). */
public record ApiError(
        int status,
        String errorCode,
        String message,
        String path,
        LocalDateTime timestamp,
        List<FieldError> fieldErrors
) {
    public record FieldError(String field, String message) {}
}
