package com.pokedex.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TypeEffectivenessUtilTest {

    @Test
    @DisplayName("RF-17: retorna 2.0 cuando el ataque es super efectivo")
    void multiplier_returnsSuperEffective() {
        assertThat(TypeEffectivenessUtil.multiplier("Agua", "Electrico")).isEqualTo(2.0);
        assertThat(TypeEffectivenessUtil.multiplier("Planta", "Fuego")).isEqualTo(2.0);
        assertThat(TypeEffectivenessUtil.multiplier("Tierra", "Agua")).isEqualTo(2.0);
    }

    @Test
    @DisplayName("RF-17: retorna 0.5 cuando el ataque es poco efectivo")
    void multiplier_returnsNotVeryEffective() {
        assertThat(TypeEffectivenessUtil.multiplier("Fuego", "Planta")).isEqualTo(0.5);
        assertThat(TypeEffectivenessUtil.multiplier("Agua", "Fuego")).isEqualTo(0.5);
    }

    @Test
    @DisplayName("RF-17: retorna 0.0 cuando el tipo es inmune")
    void multiplier_returnsImmune() {
        assertThat(TypeEffectivenessUtil.multiplier("Tierra", "Electrico")).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Retorna 1.0 (neutral) cuando no hay matchup definido para ese tipo")
    void multiplier_returnsNeutralWhenNoMatchupDefined() {
        assertThat(TypeEffectivenessUtil.multiplier("Dragon", "Fuego")).isEqualTo(1.0);
        assertThat(TypeEffectivenessUtil.multiplier("Normal", "Fantasma")).isEqualTo(1.0);
    }

    @Test
    @DisplayName("Retorna 1.0 (neutral) cuando alguno de los tipos es null")
    void multiplier_returnsNeutralForNullTypes() {
        assertThat(TypeEffectivenessUtil.multiplier(null, "Fuego")).isEqualTo(1.0);
        assertThat(TypeEffectivenessUtil.multiplier("Fuego", null)).isEqualTo(1.0);
        assertThat(TypeEffectivenessUtil.multiplier(null, null)).isEqualTo(1.0);
    }
}
