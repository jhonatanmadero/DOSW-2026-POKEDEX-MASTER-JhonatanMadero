package com.pokedex.controller.dto.response;

import java.util.List;
import java.util.Map;

/** RF-17: analisis competitivo del equipo. */
public record TeamAnalysisResponse(
        Long teamId,
        String teamName,
        Integer totalHp, Integer totalAttack, Integer totalDefense,
        Integer totalSpecialAttack, Integer totalSpecialDefense, Integer totalSpeed,
        Map<String, Double> typeWeaknesses,
        Map<String, Double> typeResistances,
        List<String> typeCoverage,
        List<String> recommendations
) {}
