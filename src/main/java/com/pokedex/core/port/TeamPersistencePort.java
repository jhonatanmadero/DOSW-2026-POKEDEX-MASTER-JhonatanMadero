package com.pokedex.core.port;

import com.pokedex.core.model.Team;
import java.util.List;
import java.util.Optional;

public interface TeamPersistencePort {
    Optional<Team> findById(Long id);
    List<Team> findAllByUserId(Long userId);
    Team save(Team team);
    void deleteById(Long id);
    /** Cuenta cuantos equipos incluyen a un Pokemon dado (RF-19: tasa de eleccion). */
    long countTeamsContainingPokemon(Long pokemonId);
    long countAll();
}
