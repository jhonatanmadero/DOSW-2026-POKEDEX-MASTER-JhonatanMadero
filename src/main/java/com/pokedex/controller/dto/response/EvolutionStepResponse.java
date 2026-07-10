package com.pokedex.controller.dto.response;

public record EvolutionStepResponse(
        Long pokemonId, String pokemonName, Integer level, String method, String stage
) {}
