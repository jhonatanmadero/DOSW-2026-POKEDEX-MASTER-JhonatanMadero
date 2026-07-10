package com.pokedex.controller.dto.response;

import java.util.List;

/** RF-06: ficha completa de detalle de un Pokemon. */
public record PokemonResponse(
        Long id,
        Integer nationalNumber,
        String name,
        String description,
        String imageUrl,
        String typePrimary,
        String typeSecondary,
        String region,
        Integer generation,
        Double heightMeters,
        Double weightKg,
        Boolean hasMega,
        String evolutionStage,
        List<AbilityResponse> abilities,
        List<EvolutionStepResponse> evolutionChain,
        PokemonStatsResponse stats
) {}
