package com.pokedex.controller.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record TeamResponse(
        Long id, String name, String description,
        List<PokemonSummaryResponse> pokemons, LocalDateTime createdAt
) {}
