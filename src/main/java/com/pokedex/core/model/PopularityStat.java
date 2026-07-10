package com.pokedex.core.model;

import lombok.Builder;
import lombok.Value;

/** Estadistica de popularidad/uso de un Pokemon (RF-19). */
@Value
@Builder
public class PopularityStat {
    Long pokemonId;
    String pokemonName;
    Long viewCount;
    Long teamPickCount;
    Double pickRate; // porcentaje de equipos que lo incluyen
}
