package com.pokedex.controller.dto.response;

public record PopularityStatResponse(
        Long pokemonId, String pokemonName, Long viewCount, Long teamPickCount, Double pickRate
) {}
