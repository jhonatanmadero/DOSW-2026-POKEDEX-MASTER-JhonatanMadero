package com.pokedex.core.service.impl;

import com.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.core.model.Favorite;
import com.pokedex.core.model.Pokemon;
import com.pokedex.core.port.FavoritePersistencePort;
import com.pokedex.core.service.interfaces.FavoriteService;
import com.pokedex.core.service.interfaces.PokemonService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** RF-15: guardar y quitar Pokemon favoritos. Requiere usuario autenticado (RN-05). */
@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoritePersistencePort favoritePort;
    private final PokemonService pokemonService;

    @Override
    public List<Favorite> findAllByUser(Long userId) {
        return favoritePort.findAllByUserId(userId);
    }

    @Override
    @Transactional
    public Favorite add(Long userId, Long pokemonId) {
        Pokemon pokemon = pokemonService.findById(pokemonId); // 404 si no existe
        return favoritePort.findByUserIdAndPokemonId(userId, pokemonId)
                .orElseGet(() -> favoritePort.save(Favorite.builder()
                        .userId(userId)
                        .pokemonId(pokemonId)
                        .pokemonName(pokemon.getName())
                        .pokemonImageUrl(pokemon.getImageUrl())
                        .addedAt(LocalDateTime.now())
                        .build()));
    }

    @Override
    @Transactional
    public void remove(Long userId, Long pokemonId) {
        Favorite favorite = favoritePort.findByUserIdAndPokemonId(userId, pokemonId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorito", "pokemonId", pokemonId));
        favoritePort.delete(favorite);
    }
}
