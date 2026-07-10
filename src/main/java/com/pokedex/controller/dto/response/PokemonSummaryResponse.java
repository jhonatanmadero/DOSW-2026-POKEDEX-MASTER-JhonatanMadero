package com.pokedex.controller.dto.response;

/** RF-03/RF-04/RF-05: version resumida para listados y busquedas (evita sobre-fetch). */
public record PokemonSummaryResponse(
        Long id,
        Integer nationalNumber,
        String name,
        String imageUrl,
        String typePrimary,
        String typeSecondary,
        String region,
        Integer generation
) {}
