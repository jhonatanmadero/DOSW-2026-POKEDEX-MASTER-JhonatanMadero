package com.pokedex.core.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Tabla simplificada de efectividad de tipos Pokemon, usada para calcular
 * debilidades y resistencias en el analisis competitivo de equipos (RF-17).
 * Multiplicador de dano RECIBIDO por el tipo defensor (key) frente a cada
 * tipo atacante (value dentro del mapa interno).
 */
public final class TypeEffectivenessUtil {

    private TypeEffectivenessUtil() {}

    private static final Map<String, Map<String, Double>> CHART = new HashMap<>();

    static {
        // Solo se modela el subconjunto de tipos presentes en los datos semilla
        // (Fuego, Agua, Planta, Electrico, Normal, Volador, Veneno, Psiquico, Hielo, Roca, Tierra).
        put("Fuego", "Agua", 2.0);
        put("Fuego", "Roca", 2.0);
        put("Fuego", "Tierra", 2.0);
        put("Fuego", "Planta", 0.5);
        put("Fuego", "Hielo", 0.5);
        put("Fuego", "Fuego", 0.5);

        put("Agua", "Electrico", 2.0);
        put("Agua", "Planta", 2.0);
        put("Agua", "Fuego", 0.5);
        put("Agua", "Agua", 0.5);
        put("Agua", "Hielo", 0.5);

        put("Planta", "Fuego", 2.0);
        put("Planta", "Hielo", 2.0);
        put("Planta", "Veneno", 2.0);
        put("Planta", "Volador", 2.0);
        put("Planta", "Agua", 0.5);
        put("Planta", "Electrico", 0.5);
        put("Planta", "Planta", 0.5);

        put("Electrico", "Tierra", 2.0);
        put("Electrico", "Electrico", 0.5);
        put("Electrico", "Volador", 0.5);

        put("Normal", "Roca", 1.0);

        put("Volador", "Electrico", 2.0);
        put("Volador", "Hielo", 2.0);
        put("Volador", "Roca", 2.0);
        put("Volador", "Planta", 0.5);

        put("Veneno", "Tierra", 2.0);
        put("Veneno", "Psiquico", 2.0);
        put("Veneno", "Planta", 0.5);
        put("Veneno", "Veneno", 0.5);

        put("Psiquico", "Psiquico", 0.5);

        put("Hielo", "Fuego", 2.0);
        put("Hielo", "Roca", 2.0);
        put("Hielo", "Hielo", 0.5);

        put("Roca", "Agua", 2.0);
        put("Roca", "Planta", 2.0);
        put("Roca", "Tierra", 2.0);
        put("Roca", "Normal", 0.5);
        put("Roca", "Fuego", 0.5);

        put("Tierra", "Agua", 2.0);
        put("Tierra", "Planta", 2.0);
        put("Tierra", "Hielo", 2.0);
        put("Tierra", "Electrico", 0.0);
    }

    private static void put(String defender, String attacker, double multiplier) {
        CHART.computeIfAbsent(defender, k -> new HashMap<>()).put(attacker, multiplier);
    }

    /** Multiplicador de dano que recibe defenderType al ser atacado por attackerType. */
    public static double multiplier(String defenderType, String attackerType) {
        if (defenderType == null || attackerType == null) return 1.0;
        return CHART.getOrDefault(defenderType, Map.of()).getOrDefault(attackerType, 1.0);
    }
}
