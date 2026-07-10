package com.pokedex.core.exception;

import lombok.Getter;

/** Excepcion base de negocio. Toda excepcion propia del dominio hereda de esta. */
@Getter
public class BusinessException extends RuntimeException {
    private final String errorCode;

    public BusinessException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
