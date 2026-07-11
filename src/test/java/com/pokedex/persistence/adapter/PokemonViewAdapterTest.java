package com.pokedex.persistence.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pokedex.core.model.PopularityStat;
import com.pokedex.persistence.entity.document.PokemonViewDocument;
import com.pokedex.persistence.repository.document.PokemonViewMongoRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PokemonViewAdapterTest {

    @Mock private PokemonViewMongoRepository repository;
    private PokemonViewAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new PokemonViewAdapter(repository);
    }

    @Test
    @DisplayName("RF-19: registerView incrementa el contador cuando ya existe el documento")
    void registerView_incrementsExistingCounter() {
        PokemonViewDocument existing = PokemonViewDocument.builder()
                .pokemonId(25L).pokemonName("Pikachu").viewCount(5L).build();
        when(repository.findByPokemonId(25L)).thenReturn(Optional.of(existing));

        adapter.registerView(25L, "Pikachu");

        verify(repository).save(existing);
        assertThat(existing.getViewCount()).isEqualTo(6L);
    }

    @Test
    @DisplayName("RF-19: registerView crea el documento en la primera vista")
    void registerView_createsDocumentWhenMissing() {
        when(repository.findByPokemonId(25L)).thenReturn(Optional.empty());

        adapter.registerView(25L, "Pikachu");

        org.mockito.ArgumentCaptor<PokemonViewDocument> captor =
                org.mockito.ArgumentCaptor.forClass(PokemonViewDocument.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getViewCount()).isEqualTo(1L);
        assertThat(captor.getValue().getPokemonName()).isEqualTo("Pikachu");
    }

    @Test
    @DisplayName("RF-19: topByViews respeta el limite pedido")
    void topByViews_respectsLimit() {
        List<PokemonViewDocument> docs = List.of(
                PokemonViewDocument.builder().pokemonId(1L).pokemonName("Bulbasaur").viewCount(30L).build(),
                PokemonViewDocument.builder().pokemonId(4L).pokemonName("Charmander").viewCount(20L).build(),
                PokemonViewDocument.builder().pokemonId(7L).pokemonName("Squirtle").viewCount(10L).build());
        when(repository.findTop50ByOrderByViewCountDesc()).thenReturn(docs);

        List<PopularityStat> result = adapter.topByViews(2);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getPokemonName()).isEqualTo("Bulbasaur");
    }
}