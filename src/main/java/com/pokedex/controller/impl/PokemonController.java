package com.pokedex.controller.impl;

import com.pokedex.controller.api.PokemonApi;
import com.pokedex.controller.dto.request.PokemonRequest;
import com.pokedex.controller.dto.response.PokemonResponse;
import com.pokedex.controller.dto.response.PokemonSummaryResponse;
import com.pokedex.controller.mapper.PokemonDtoMapper;
import com.pokedex.core.model.Pokemon;
import com.pokedex.core.model.PokemonFilterCriteria;
import com.pokedex.core.service.interfaces.PokemonService;
import com.pokedex.core.service.interfaces.StatsService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
public class PokemonController implements PokemonApi {

    private final PokemonService pokemonService;
    private final StatsService statsService;
    private final PokemonDtoMapper mapper;

    @Override
    public ResponseEntity<Page<PokemonSummaryResponse>> findAll(Pageable pageable) {
        Page<Pokemon> page = pokemonService.findAll(pageable);
        return ResponseEntity.ok(page.map(mapper::toSummary));
    }

    @Override
    public ResponseEntity<List<PokemonSummaryResponse>> searchByName(String term, Pageable pageable) {
        return ResponseEntity.ok(mapper.toSummaryList(pokemonService.searchByName(term, pageable)));
    }

    @Override
    public ResponseEntity<PokemonResponse> findByNationalNumber(Integer number) {
        Pokemon pokemon = pokemonService.findByNationalNumber(number);
        statsService.registerPokemonView(pokemon.getId(), pokemon.getName());
        return ResponseEntity.ok(mapper.toResponse(pokemon));
    }

    @Override
    public ResponseEntity<List<PokemonSummaryResponse>> filter(String region, String typePrimary, String typeSecondary,
            Integer generation, String statName, Integer statMin, Integer statMax, String ability, Boolean hasMega,
            String evolutionStage, Double weightMin, Double weightMax, Pageable pageable) {
        PokemonFilterCriteria criteria = PokemonFilterCriteria.builder()
                .region(region).typePrimary(typePrimary).typeSecondary(typeSecondary)
                .generation(generation).statName(statName).statMin(statMin).statMax(statMax)
                .ability(ability).hasMega(hasMega).evolutionStage(evolutionStage)
                .weightMin(weightMin).weightMax(weightMax)
                .build();
        return ResponseEntity.ok(mapper.toSummaryList(pokemonService.filterByCriteria(criteria, pageable)));
    }

    @Override
    public ResponseEntity<List<String>> listAbilities() {
        return ResponseEntity.ok(pokemonService.listAbilities());
    }

    @Override
    public ResponseEntity<PokemonResponse> findById(Long id) {
        Pokemon pokemon = pokemonService.findById(id);
        statsService.registerPokemonView(pokemon.getId(), pokemon.getName());
        return ResponseEntity.ok(mapper.toResponse(pokemon));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PokemonResponse> create(PokemonRequest request) {
        Pokemon created = pokemonService.create(mapper.toDomain(request));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(mapper.toResponse(created));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PokemonResponse> update(Long id, PokemonRequest request) {
        Pokemon updated = pokemonService.update(id, mapper.toDomain(request));
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(Long id) {
        pokemonService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
