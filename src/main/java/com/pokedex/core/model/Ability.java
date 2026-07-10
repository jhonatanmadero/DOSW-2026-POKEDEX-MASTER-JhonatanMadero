package com.pokedex.core.model;

import lombok.Builder;
import lombok.Value;

/** Habilidad de un Pokemon (RF-12). */
@Value
@Builder
public class Ability {
    Long id;
    String name;
    String description;
    Boolean isHidden;
}
