package com.pokedex.controller.dto.request;

import java.time.LocalDate;

/** RF-21: actualizacion de perfil por ADMIN o por el propio usuario. */
public record UpdateUserRequest(
        String fullName,
        String favoriteRegion,
        LocalDate birthDate
) {}
