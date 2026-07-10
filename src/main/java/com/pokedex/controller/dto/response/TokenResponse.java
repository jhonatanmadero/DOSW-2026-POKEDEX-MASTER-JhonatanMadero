package com.pokedex.controller.dto.response;

/** RF-01/RF-02: respuesta de autenticacion exitosa. */
public record TokenResponse(String token, UserResponse user) {}
