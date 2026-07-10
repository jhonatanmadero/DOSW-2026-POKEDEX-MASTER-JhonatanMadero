package com.pokedex.core.model;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Value;

/** Equipo Pokemon de un usuario, hasta 6 integrantes (RF-16, RF-18). */
@Value
@Builder(toBuilder = true)
public class Team {
    Long id;
    Long userId;
    String name;
    String description;
    List<Pokemon> pokemons; // maximo 6 - validado en TeamValidator
    LocalDateTime createdAt;
}
