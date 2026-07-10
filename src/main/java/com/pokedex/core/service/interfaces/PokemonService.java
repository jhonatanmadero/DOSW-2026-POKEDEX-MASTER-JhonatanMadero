package com.pokedex.core.service.interfaces;

import com.pokedex.core.model.Pokemon;
import com.pokedex.core.model.PokemonFilterCriteria;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PokemonService {
    Page<Pokemon> findAll(Pageable pageable);           // RF-03
    Pokemon findById(Long id);                            // RF-06
    Pokemon findByNationalNumber(Integer number);          // RF-05
    List<Pokemon> searchByName(String term, Pageable pageable); // RF-04
    List<Pokemon> filterByCriteria(PokemonFilterCriteria criteria, Pageable pageable); // RF-07..RF-14, RF-22
    Pokemon create(Pokemon pokemon);                       // RF-20
    Pokemon update(Long id, Pokemon pokemon);              // RF-20
    void delete(Long id);                                  // RF-20
    List<String> listAbilities();
}
