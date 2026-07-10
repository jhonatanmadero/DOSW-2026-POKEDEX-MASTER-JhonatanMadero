package com.pokedex.core.port;

import com.pokedex.core.model.Pokemon;
import com.pokedex.core.model.PokemonFilterCriteria;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PokemonPersistencePort {
    Page<Pokemon> findAll(Pageable pageable);
    Optional<Pokemon> findById(Long id);
    Optional<Pokemon> findByNationalNumber(Integer number);
    List<Pokemon> search(PokemonFilterCriteria criteria, Pageable pageable);
    boolean existsByNationalNumber(Integer number);
    Pokemon save(Pokemon pokemon);
    void deleteById(Long id);
    List<String> findAllAbilityNames();
}
