package com.pokedex.core.model;

import lombok.Builder;
import lombok.Value;

/**
 * Criterios de filtrado avanzado sobre el catalogo de Pokemon.
 * Agrupa RF-07 (region), RF-08 (tipo primario), RF-09 (tipo secundario),
 * RF-10 (generacion), RF-11 (rango de estadistica), RF-12 (habilidad),
 * RF-13 (mega evolucion) y RF-22 (peso).
 */
@Value
@Builder
public class PokemonFilterCriteria {
    String region;
    String typePrimary;
    String typeSecondary;
    Integer generation;
    String statName;      // hp, attack, defense, specialAttack, specialDefense, speed
    Integer statMin;
    Integer statMax;
    String ability;
    Boolean hasMega;
    String evolutionStage;
    Double weightMin;
    Double weightMax;
    String nameContains;  // RF-04
    Integer nationalNumber; // RF-05
}
