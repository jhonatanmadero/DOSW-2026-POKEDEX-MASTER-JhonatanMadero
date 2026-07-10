package com.pokedex.core.port;

import com.pokedex.core.model.Favorite;
import java.util.List;
import java.util.Optional;

public interface FavoritePersistencePort {
    List<Favorite> findAllByUserId(Long userId);
    Optional<Favorite> findByUserIdAndPokemonId(Long userId, Long pokemonId);
    Favorite save(Favorite favorite);
    void delete(Favorite favorite);
}
