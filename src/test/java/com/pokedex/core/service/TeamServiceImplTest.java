package com.pokedex.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.pokedex.core.exception.ForbiddenOperationException;
import com.pokedex.core.exception.ValidationException;
import com.pokedex.core.model.Pokemon;
import com.pokedex.core.model.PokemonStats;
import com.pokedex.core.model.Team;
import com.pokedex.core.model.TeamAnalysis;
import com.pokedex.core.port.TeamPersistencePort;
import com.pokedex.core.service.impl.TeamServiceImpl;
import com.pokedex.core.validator.TeamValidator;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TeamServiceImplTest {

    @Mock private TeamPersistencePort teamPort;

    private TeamServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new TeamServiceImpl(teamPort, new TeamValidator());
    }

    private Pokemon pokemonWithType(Long id, String type1, String type2) {
        return Pokemon.builder().id(id).name("Poke" + id).typePrimary(type1).typeSecondary(type2)
                .stats(PokemonStats.builder().hp(50).attack(50).defense(50)
                        .specialAttack(50).specialDefense(50).speed(50).build())
                .build();
    }

    @Test
    @DisplayName("create: RN-03 debe rechazar equipos con mas de 6 Pokemon")
    void create_withMoreThanSixPokemon_throws() {
        List<Pokemon> sevenPokemon = List.of(
                pokemonWithType(1L, "Fuego", null), pokemonWithType(2L, "Agua", null),
                pokemonWithType(3L, "Planta", null), pokemonWithType(4L, "Electrico", null),
                pokemonWithType(5L, "Normal", null), pokemonWithType(6L, "Roca", null),
                pokemonWithType(7L, "Hielo", null));
        Team team = Team.builder().name("Equipo Grande").pokemons(sevenPokemon).build();

        assertThrows(ValidationException.class, () -> service.create(1L, team));
        verify(teamPort, never()).save(any());
    }

    @Test
    @DisplayName("create: debe guardar un equipo valido de hasta 6 Pokemon")
    void create_withValidTeam_saves() {
        Team team = Team.builder().name("Iniciales")
                .pokemons(List.of(pokemonWithType(1L, "Fuego", null))).build();
        Team saved = team.toBuilder().id(10L).userId(1L).build();
        when(teamPort.save(any())).thenReturn(saved);

        Team result = service.create(1L, team);

        assertThat(result.getId()).isEqualTo(10L);
    }

    @Test
    @DisplayName("findById: RN-02 debe lanzar ForbiddenOperationException si el equipo no pertenece al usuario")
    void findById_whenNotOwner_throws() {
        Team team = Team.builder().id(5L).userId(99L).name("Otro equipo").pokemons(List.of()).build();
        when(teamPort.findById(5L)).thenReturn(Optional.of(team));

        assertThrows(ForbiddenOperationException.class, () -> service.findById(1L, 5L));
    }

    @Test
    @DisplayName("analyze: RF-17 debe calcular correctamente los stats totales del equipo")
    void analyze_computesTotals() {
        List<Pokemon> members = List.of(pokemonWithType(1L, "Fuego", null), pokemonWithType(2L, "Agua", null));
        Team team = Team.builder().id(5L).userId(1L).name("Duo").pokemons(members).build();
        when(teamPort.findById(5L)).thenReturn(Optional.of(team));

        TeamAnalysis analysis = service.analyze(1L, 5L);

        assertThat(analysis.getTotalHp()).isEqualTo(100);
        assertThat(analysis.getTypeCoverage()).containsExactlyInAnyOrder("Fuego", "Agua");
    }
}
