package com.pokedex.controller.dto.request;

import jakarta.validation.constraints.*;
import java.util.List;

/** RF-20: creacion/actualizacion de Pokemon (solo ADMIN). */
public record PokemonRequest(
        @NotNull @Min(1) @Max(1025) Integer nationalNumber,
        @NotBlank @Size(max = 100) String name,
        @Size(max = 1000) String description,
        @NotBlank String imageUrl,
        @NotBlank String typePrimary,
        String typeSecondary,
        @NotBlank String region,
        @NotNull @Min(1) @Max(9) Integer generation,
        @Positive Double heightMeters,
        @Positive Double weightKg,
        Boolean hasMega,
        String evolutionStage,
        List<AbilityRequest> abilities,
        @NotNull PokemonStatsRequest stats
) {
    public record AbilityRequest(@NotBlank String name, String description, Boolean isHidden) {}
}
