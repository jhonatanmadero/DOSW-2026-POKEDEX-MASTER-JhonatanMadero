package com.pokedex.core.service.interfaces;

import com.pokedex.core.model.PopularityStat;
import java.util.List;
import java.util.Map;

public interface StatsService {
    List<PopularityStat> topPopular(int limit);   // RF-19: ranking de popularidad / consultas
    List<PopularityStat> topPickRate(int limit);  // RF-19: tasa de eleccion en equipos
    Map<String, Object> generalStats();           // RF-19: totales (usuarios, equipos, etc.)
    void registerPokemonView(Long pokemonId, String pokemonName);
}
