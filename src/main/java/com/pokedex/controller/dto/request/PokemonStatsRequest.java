package com.pokedex.controller.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PokemonStatsRequest(
        @NotNull @Min(1) @Max(255) Integer hp,
        @NotNull @Min(1) @Max(255) Integer attack,
        @NotNull @Min(1) @Max(255) Integer defense,
        @NotNull @Min(1) @Max(255) Integer specialAttack,
        @NotNull @Min(1) @Max(255) Integer specialDefense,
        @NotNull @Min(1) @Max(255) Integer speed
) {}
