package com.pokedex.core.model;

import lombok.Builder;
import lombok.Value;

/** Paso de la cadena evolutiva de un Pokemon (RF-06, RF-14). */
@Value
@Builder
public class EvolutionStep {
    Long pokemonId;
    String pokemonName;
    Integer level;
    String method;
    String stage; // BASICO, PRIMERA, SEGUNDA, SIN_EVOLUCION, LEGENDARIO
}
