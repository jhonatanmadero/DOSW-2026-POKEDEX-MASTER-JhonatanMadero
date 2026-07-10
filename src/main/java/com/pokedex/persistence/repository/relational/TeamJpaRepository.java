package com.pokedex.persistence.repository.relational;

import com.pokedex.persistence.entity.relational.TeamEntity;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamJpaRepository extends JpaRepository<TeamEntity, Long> {

    @EntityGraph(attributePaths = {"members", "members.pokemon", "members.pokemon.stats", "members.pokemon.region"})
    List<TeamEntity> findByUser_Id(Long userId);

    @EntityGraph(attributePaths = {"members", "members.pokemon", "members.pokemon.stats", "members.pokemon.region"})
    java.util.Optional<TeamEntity> findWithDetailsById(Long id);

    @Query("SELECT COUNT(tp) FROM TeamPokemonEntity tp WHERE tp.pokemon.id = :pokemonId")
    long countTeamsContainingPokemon(@Param("pokemonId") Long pokemonId);
}
