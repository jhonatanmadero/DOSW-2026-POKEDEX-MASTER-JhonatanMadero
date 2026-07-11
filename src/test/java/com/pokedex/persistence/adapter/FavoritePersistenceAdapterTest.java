package com.pokedex.persistence.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.core.model.Favorite;
import com.pokedex.persistence.entity.relational.FavoriteEntity;
import com.pokedex.persistence.entity.relational.PokemonEntity;
import com.pokedex.persistence.entity.relational.UserEntity;
import com.pokedex.persistence.repository.relational.FavoriteJpaRepository;
import com.pokedex.persistence.repository.relational.PokemonJpaRepository;
import com.pokedex.persistence.repository.relational.UserJpaRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FavoritePersistenceAdapterTest {

    @Mock private FavoriteJpaRepository repository;
    @Mock private UserJpaRepository userRepository;
    @Mock private PokemonJpaRepository pokemonRepository;

    private FavoritePersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new FavoritePersistenceAdapter(repository, userRepository, pokemonRepository);
    }

    private UserEntity userEntity() {
        return UserEntity.builder().id(1L).fullName("Ash").email("ash@pokedex.com").build();
    }

    private PokemonEntity pokemonEntity() {
        return PokemonEntity.builder().id(25L).nationalNumber(25).name("Pikachu").imageUrl("pikachu.png").build();
    }

    @Test
    @DisplayName("findAllByUserId: mapea las entidades a dominio")
    void findAllByUserId_mapsToDomain() {
        FavoriteEntity entity = FavoriteEntity.builder().id(1L).user(userEntity()).pokemon(pokemonEntity()).build();
        when(repository.findByUser_Id(1L)).thenReturn(List.of(entity));

        List<Favorite> result = adapter.findAllByUserId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPokemonName()).isEqualTo("Pikachu");
    }

    @Test
    @DisplayName("save: lanza 404 si el usuario no existe")
    void save_whenUserMissing_throws() {
        Favorite favorite = Favorite.builder().userId(99L).pokemonId(25L).build();
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adapter.save(favorite));
    }

    @Test
    @DisplayName("save: lanza 404 si el pokemon no existe")
    void save_whenPokemonMissing_throws() {
        Favorite favorite = Favorite.builder().userId(1L).pokemonId(999L).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity()));
        when(pokemonRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adapter.save(favorite));
    }

    @Test
    @DisplayName("save: persiste y retorna el favorito mapeado a dominio")
    void save_persistsAndReturnsDomain() {
        Favorite favorite = Favorite.builder().userId(1L).pokemonId(25L).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity()));
        when(pokemonRepository.findById(25L)).thenReturn(Optional.of(pokemonEntity()));
        FavoriteEntity saved = FavoriteEntity.builder().id(10L).user(userEntity()).pokemon(pokemonEntity()).build();
        when(repository.save(org.mockito.ArgumentMatchers.any(FavoriteEntity.class))).thenReturn(saved);

        Favorite result = adapter.save(favorite);

        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getPokemonName()).isEqualTo("Pikachu");
    }

    @Test
    @DisplayName("delete: elimina por id")
    void delete_removesById() {
        Favorite favorite = Favorite.builder().id(5L).build();

        adapter.delete(favorite);

        org.mockito.Mockito.verify(repository).deleteById(5L);
    }
}
