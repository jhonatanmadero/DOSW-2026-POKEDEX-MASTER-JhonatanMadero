package com.pokedex.persistence.adapter;

import com.pokedex.core.model.PopularityStat;
import com.pokedex.core.port.PokemonViewPort;
import com.pokedex.persistence.entity.document.PokemonViewDocument;
import com.pokedex.persistence.repository.document.PokemonViewMongoRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PokemonViewAdapter implements PokemonViewPort {

    private final PokemonViewMongoRepository repository;

    @Override
    public void registerView(Long pokemonId, String pokemonName) {
        PokemonViewDocument doc = repository.findByPokemonId(pokemonId)
                .orElseGet(() -> PokemonViewDocument.builder()
                        .pokemonId(pokemonId)
                        .pokemonName(pokemonName)
                        .viewCount(0L)
                        .build());
        doc.setViewCount(doc.getViewCount() + 1);
        doc.setLastViewed(LocalDateTime.now());
        repository.save(doc);
    }

    @Override
    public List<PopularityStat> topByViews(int limit) {
        return repository.findTop50ByOrderByViewCountDesc().stream()
                .limit(limit)
                .map(d -> PopularityStat.builder()
                        .pokemonId(d.getPokemonId())
                        .pokemonName(d.getPokemonName())
                        .viewCount(d.getViewCount())
                        .teamPickCount(0L)
                        .pickRate(0.0)
                        .build())
                .toList();
    }
}
