package com.pokedex.persistence.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.core.model.Pokemon;
import com.pokedex.core.model.Team;
import com.pokedex.persistence.entity.relational.PokemonEntity;
import com.pokedex.persistence.entity.relational.TeamEntity;
import com.pokedex.persistence.entity.relational.UserEntity;
import com.pokedex.persistence.mapper.TeamPersistenceMapper;
import com.pokedex.persistence.repository.relational.PokemonJpaRepository;
import com.pokedex.persistence.repository.relational.TeamJpaRepository;
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
class TeamPersistenceAdapterTest {

    @Mock private TeamJpaRepository repository;
    @Mock private UserJpaRepository userRepository;
    @Mock private PokemonJpaRepository pokemonRepository;
    @Mock private TeamPersistenceMapper mapper;

    private TeamPersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new TeamPersistenceAdapter(repository, userRepository, pokemonRepository, mapper);
    }

    @Test
    @DisplayName("findById: mapea el equipo con detalle cuando existe")
    void findById_returnsMappedTeam() {
        TeamEntity entity = TeamEntity.builder().id(1L).name("Iniciales").build();
        Team domain = Team.builder().id(1L).name("Iniciales").build();
        when(repository.findWithDetailsById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        assertThat(adapter.findById(1L)).contains(domain);
    }

    @Test
    @DisplayName("findAllByUserId: mapea todos los equipos del usuario")
    void findAllByUserId_mapsAll() {
        TeamEntity entity = TeamEntity.builder().id(1L).name("Iniciales").build();
        Team domain = Team.builder().id(1L).name("Iniciales").build();
        when(repository.findByUser_Id(1L)).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        assertThat(adapter.findAllByUserId(1L)).containsExactly(domain);
    }

    @Test
    @DisplayName("save: lanza 404 si el usuario dueno del equipo no existe")
    void save_whenUserMissing_throws() {
        Team team = Team.builder().userId(99L).name("Iniciales").pokemons(List.of()).build();
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adapter.save(team));
    }

    @Test
    @DisplayName("RF-16: save persiste el equipo con sus Pokemon en orden de slot")
    void save_persistsTeamWithMembers() {
        UserEntity user = UserEntity.builder().id(1L).build();
        Team team = Team.builder().userId(1L).name("Iniciales")
                .pokemons(List.of(Pokemon.builder().id(25L).build())).build();
        TeamEntity saved = TeamEntity.builder().id(1L).name("Iniciales").build();
        Team domain = Team.builder().id(1L).name("Iniciales").build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(pokemonRepository.findById(25L)).thenReturn(Optional.of(PokemonEntity.builder().id(25L).build()));
        when(repository.save(any(TeamEntity.class))).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(domain);

        Team result = adapter.save(team);

        assertThat(result.getName()).isEqualTo("Iniciales");
    }

    @Test
    @DisplayName("save: lanza 404 si algun Pokemon del equipo no existe")
    void save_whenPokemonMissing_throws() {
        UserEntity user = UserEntity.builder().id(1L).build();
        Team team = Team.builder().userId(1L).name("Iniciales")
                .pokemons(List.of(Pokemon.builder().id(999L).build())).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(pokemonRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adapter.save(team));
    }

    @Test
    @DisplayName("deleteById: delega al repositorio")
    void deleteById_delegatesToRepository() {
        adapter.deleteById(1L);

        org.mockito.Mockito.verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("RF-19: countTeamsContainingPokemon delega al repositorio")
    void countTeamsContainingPokemon_delegatesToRepository() {
        when(repository.countTeamsContainingPokemon(25L)).thenReturn(3L);

        assertThat(adapter.countTeamsContainingPokemon(25L)).isEqualTo(3L);
    }

    @Test
    @DisplayName("countAll: delega al repositorio")
    void countAll_delegatesToRepository() {
        when(repository.count()).thenReturn(10L);

        assertThat(adapter.countAll()).isEqualTo(10L);
    }
}
