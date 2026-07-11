package com.pokedex.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.core.model.Favorite;
import com.pokedex.core.model.Pokemon;
import com.pokedex.core.port.FavoritePersistencePort;
import com.pokedex.core.service.impl.FavoriteServiceImpl;
import com.pokedex.core.service.interfaces.PokemonService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceImplTest {

    @Mock private FavoritePersistencePort favoritePort;
    @Mock private PokemonService pokemonService;

    private FavoriteServiceImpl service;
    private Pokemon pikachu;

    @BeforeEach
    void setUp() {
        service = new FavoriteServiceImpl(favoritePort, pokemonService);
        pikachu = Pokemon.builder().id(25L).name("Pikachu").imageUrl("pikachu.png").build();
    }

    @Test
    @DisplayName("findAllByUser: RF-15 debe listar los favoritos del usuario")
    void findAllByUser_returnsList() {
        Favorite fav = Favorite.builder().id(1L).userId(1L).pokemonId(25L).pokemonName("Pikachu").build();
        when(favoritePort.findAllByUserId(1L)).thenReturn(List.of(fav));

        List<Favorite> result = service.findAllByUser(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPokemonName()).isEqualTo("Pikachu");
    }

    @Test
    @DisplayName("add: RF-15 debe lanzar 404 si el Pokemon no existe")
    void add_whenPokemonNotFound_throws() {
        when(pokemonService.findById(99L)).thenThrow(new ResourceNotFoundException("Pokemon", "id", 99L));

        assertThrows(ResourceNotFoundException.class, () -> service.add(1L, 99L));
        verify(favoritePort, never()).save(any());
    }

    @Test
    @DisplayName("add: no debe duplicar si el favorito ya existe")
    void add_whenAlreadyFavorite_returnsExisting() {
        when(pokemonService.findById(25L)).thenReturn(pikachu);
        Favorite existing = Favorite.builder().id(5L).userId(1L).pokemonId(25L).build();
        when(favoritePort.findByUserIdAndPokemonId(1L, 25L)).thenReturn(Optional.of(existing));

        Favorite result = service.add(1L, 25L);

        assertThat(result.getId()).isEqualTo(5L);
        verify(favoritePort, never()).save(any());
    }

    @Test
    @DisplayName("add: debe crear el favorito cuando no existe aun")
    void add_whenNew_saves() {
        when(pokemonService.findById(25L)).thenReturn(pikachu);
        when(favoritePort.findByUserIdAndPokemonId(1L, 25L)).thenReturn(Optional.empty());
        Favorite saved = Favorite.builder().id(10L).userId(1L).pokemonId(25L).pokemonName("Pikachu").build();
        when(favoritePort.save(any())).thenReturn(saved);

        Favorite result = service.add(1L, 25L);

        assertThat(result.getId()).isEqualTo(10L);
    }

    @Test
    @DisplayName("remove: debe lanzar 404 si el favorito no existe")
    void remove_whenNotFound_throws() {
        when(favoritePort.findByUserIdAndPokemonId(1L, 99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.remove(1L, 99L));
    }

    @Test
    @DisplayName("remove: debe eliminar el favorito cuando existe")
    void remove_whenExists_deletes() {
        Favorite existing = Favorite.builder().id(5L).userId(1L).pokemonId(25L).build();
        when(favoritePort.findByUserIdAndPokemonId(1L, 25L)).thenReturn(Optional.of(existing));

        service.remove(1L, 25L);

        verify(favoritePort).delete(existing);
    }
}
