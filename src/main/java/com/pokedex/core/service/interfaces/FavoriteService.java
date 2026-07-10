package com.pokedex.core.service.interfaces;

import com.pokedex.core.model.Favorite;
import java.util.List;

public interface FavoriteService {
    List<Favorite> findAllByUser(Long userId);          // RF-15
    Favorite add(Long userId, Long pokemonId);           // RF-15
    void remove(Long userId, Long pokemonId);            // RF-15
}
