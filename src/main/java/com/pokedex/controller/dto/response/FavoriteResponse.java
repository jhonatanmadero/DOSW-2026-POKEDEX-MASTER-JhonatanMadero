package com.pokedex.controller.dto.response;

import java.time.LocalDateTime;

public record FavoriteResponse(
        Long id, Long pokemonId, String pokemonName, String pokemonImageUrl, LocalDateTime addedAt
) {}
