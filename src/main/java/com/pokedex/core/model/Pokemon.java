package com.pokedex.core.model;

import java.util.List;
import lombok.Builder;
import lombok.Value;

/**
 * Modelo de negocio de Pokemon. POJO inmutable, sin anotaciones de
 * persistencia (Seccion 6.1 del plan). Cubre RF-03 a RF-14 y RF-22.
 */
@Value
@Builder(toBuilder = true)
public class Pokemon {
    Long id;
    Integer nationalNumber;
    String name;
    String description;
    String imageUrl;
    String typePrimary;
    String typeSecondary;
    String region;
    Integer generation;
    Double heightMeters;
    Double weightKg;
    Boolean hasMega;
    String evolutionStage; // BASICO, PRIMERA, SEGUNDA, SIN_EVOLUCION, LEGENDARIO
    List<Ability> abilities;
    List<EvolutionStep> evolutionChain;
    PokemonStats stats;
}
