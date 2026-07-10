package com.pokedex.core.model;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Value;

/** Analisis competitivo de un equipo: stats totales, debilidades y resistencias (RF-17). */
@Value
@Builder
public class TeamAnalysis {
    Long teamId;
    String teamName;
    Integer totalHp;
    Integer totalAttack;
    Integer totalDefense;
    Integer totalSpecialAttack;
    Integer totalSpecialDefense;
    Integer totalSpeed;
    Map<String, Double> typeWeaknesses;   // tipo -> multiplicador de dano recibido
    Map<String, Double> typeResistances;  // tipo -> multiplicador de dano recibido
    List<String> typeCoverage;            // tipos ofensivos cubiertos por el equipo
    List<String> recommendations;
}
