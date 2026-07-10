package com.pokedex.persistence.adapter;

import com.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.core.model.Favorite;
import com.pokedex.core.port.FavoritePersistencePort;
import com.pokedex.persistence.entity.relational.FavoriteEntity;
import com.pokedex.persistence.entity.relational.PokemonEntity;
import com.pokedex.persistence.entity.relational.UserEntity;
import com.pokedex.persistence.repository.relational.FavoriteJpaRepository;
import com.pokedex.persistence.repository.relational.PokemonJpaRepository;
import com.pokedex.persistence.repository.relational.UserJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FavoritePersistenceAdapter implements FavoritePersistencePort {

    private final FavoriteJpaRepository repository;
    private final UserJpaRepository userRepository;
    private final PokemonJpaRepository pokemonRepository;

    @Override
    public List<Favorite> findAllByUserId(Long userId) {
        return repository.findByUser_Id(userId).stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<Favorite> findByUserIdAndPokemonId(Long userId, Long pokemonId) {
        return repository.findByUser_IdAndPokemon_Id(userId, pokemonId).map(this::toDomain);
    }

    @Override
    public Favorite save(Favorite favorite) {
        UserEntity user = userRepository.findById(favorite.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", favorite.getUserId()));
        PokemonEntity pokemon = pokemonRepository.findById(favorite.getPokemonId())
                .orElseThrow(() -> new ResourceNotFoundException("Pokemon", "id", favorite.getPokemonId()));

        FavoriteEntity entity = FavoriteEntity.builder()
                .user(user)
                .pokemon(pokemon)
                .build();
        return toDomain(repository.save(entity));
    }

    @Override
    public void delete(Favorite favorite) {
        repository.deleteById(favorite.getId());
    }

    private Favorite toDomain(FavoriteEntity e) {
        return Favorite.builder()
                .id(e.getId())
                .userId(e.getUser().getId())
                .pokemonId(e.getPokemon().getId())
                .pokemonName(e.getPokemon().getName())
                .pokemonImageUrl(e.getPokemon().getImageUrl())
                .addedAt(e.getAddedAt())
                .build();
    }
}
