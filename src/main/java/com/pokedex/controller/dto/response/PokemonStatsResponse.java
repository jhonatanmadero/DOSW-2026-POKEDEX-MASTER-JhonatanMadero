package com.pokedex.controller.dto.response;

public record PokemonStatsResponse(
        Integer hp, Integer attack, Integer defense,
        Integer specialAttack, Integer specialDefense, Integer speed, Integer total
) {}
