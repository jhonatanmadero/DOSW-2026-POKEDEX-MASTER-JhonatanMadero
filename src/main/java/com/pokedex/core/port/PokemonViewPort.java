package com.pokedex.core.port;

import com.pokedex.core.model.PopularityStat;
import java.util.List;

/** Puerto hacia MongoDB para registrar y consultar vistas/consultas de Pokemon (RF-19). */
public interface PokemonViewPort {
    void registerView(Long pokemonId, String pokemonName);
    List<PopularityStat> topByViews(int limit);
}
