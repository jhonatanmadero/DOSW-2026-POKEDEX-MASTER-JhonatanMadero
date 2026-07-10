package com.pokedex.core.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;

/** Modelo de negocio del usuario/entrenador (RF-01, RF-02, RF-21). */
@Value
@Builder(toBuilder = true)
public class User {
    Long id;
    String fullName;
    String email;
    String passwordHash; // null si el usuario entro solo por OAuth2
    LocalDate birthDate;
    String favoriteRegion;
    Role role;
    Boolean active;
    Boolean fromGoogle;
    String avatarUrl;
    LocalDateTime createdAt;
}
