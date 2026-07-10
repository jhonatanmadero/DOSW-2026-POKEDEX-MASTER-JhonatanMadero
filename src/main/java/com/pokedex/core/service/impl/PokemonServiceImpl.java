package com.pokedex.core.service.impl;

import com.pokedex.core.exception.DuplicateResourceException;
import com.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.core.model.Pokemon;
import com.pokedex.core.model.PokemonFilterCriteria;
import com.pokedex.core.port.PokemonPersistencePort;
import com.pokedex.core.service.interfaces.PokemonService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PokemonServiceImpl implements PokemonService {

    private final PokemonPersistencePort pokemonPort;

    @Override
    public Page<Pokemon> findAll(Pageable pageable) {
        return pokemonPort.findAll(pageable);
    }

    @Override
    public Pokemon findById(Long id) {
        log.debug("Buscando Pokemon con id: {}", id);
        return pokemonPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pokemon", "id", id));
    }

    @Override
    public Pokemon findByNationalNumber(Integer number) {
        return pokemonPort.findByNationalNumber(number)
                .orElseThrow(() -> new ResourceNotFoundException("Pokemon", "nationalNumber", number));
    }

    @Override
    public List<Pokemon> searchByName(String term, Pageable pageable) {
        if (term == null || term.trim().length() < 2) {
            return List.of();
        }
        PokemonFilterCriteria criteria = PokemonFilterCriteria.builder()
                .nameContains(term.trim())
                .build();
        return pokemonPort.search(criteria, pageable);
    }

    @Override
    public List<Pokemon> filterByCriteria(PokemonFilterCriteria criteria, Pageable pageable) {
        return pokemonPort.search(criteria, pageable);
    }

    @Override
    @Transactional
    public Pokemon create(Pokemon pokemon) {
        if (pokemonPort.existsByNationalNumber(pokemon.getNationalNumber())) {
            throw new DuplicateResourceException("Pokemon", "nationalNumber", pokemon.getNationalNumber());
        }
        log.info("Creando Pokemon: {}", pokemon.getName());
        return pokemonPort.save(pokemon);
    }

    @Override
    @Transactional
    public Pokemon update(Long id, Pokemon pokemon) {
        Pokemon existing = findById(id);
        Pokemon updated = pokemon.toBuilder().id(existing.getId()).build();
        log.info("Actualizando Pokemon id={}", id);
        return pokemonPort.save(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        findById(id); // valida existencia -> 404 si no existe
        pokemonPort.deleteById(id);
    }

    @Override
    public List<String> listAbilities() {
        return pokemonPort.findAllAbilityNames();
    }
}
