package com.pokedex.core.model;

import lombok.Builder;
import lombok.Value;

/** Estadisticas base de un Pokemon (RF-06, RF-11). */
@Value
@Builder
public class PokemonStats {
    Integer hp;
    Integer attack;
    Integer defense;
    Integer specialAttack;
    Integer specialDefense;
    Integer speed;

    public Integer getTotal() {
        return hp + attack + defense + specialAttack + specialDefense + speed;
    }
}
