package com.pokedex.controller.dto.request;

import jakarta.validation.constraints.NotNull;

public record FavoriteRequest(@NotNull Long pokemonId) {}
