package com.pokedex.controller.dto.response;

import java.time.LocalDate;

public record UserResponse(
        Long id,
        String fullName,
        String email,
        LocalDate birthDate,
        String favoriteRegion,
        String role,
        Boolean active,
        Boolean fromGoogle,
        String avatarUrl
) {}
