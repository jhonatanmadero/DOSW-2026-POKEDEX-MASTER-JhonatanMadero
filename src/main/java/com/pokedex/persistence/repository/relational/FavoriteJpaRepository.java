package com.pokedex.persistence.repository.relational;

import com.pokedex.persistence.entity.relational.FavoriteEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteJpaRepository extends JpaRepository<FavoriteEntity, Long> {
    List<FavoriteEntity> findByUser_Id(Long userId);
    Optional<FavoriteEntity> findByUser_IdAndPokemon_Id(Long userId, Long pokemonId);
}
