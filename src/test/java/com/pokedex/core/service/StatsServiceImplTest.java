package com.pokedex.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.pokedex.core.model.PopularityStat;
import com.pokedex.core.port.PokemonViewPort;
import com.pokedex.core.port.TeamPersistencePort;
import com.pokedex.core.port.UserPersistencePort;
import com.pokedex.core.service.impl.StatsServiceImpl;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StatsServiceImplTest {

    @Mock private PokemonViewPort pokemonViewPort;
    @Mock private TeamPersistencePort teamPort;
    @Mock private UserPersistencePort userPort;

    private StatsServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new StatsServiceImpl(pokemonViewPort, teamPort, userPort);
    }

    @Test
    @DisplayName("topPopular: RF-19 debe delegar en el puerto de vistas")
    void topPopular_delegatesToPort() {
        PopularityStat stat = PopularityStat.builder().pokemonId(25L).pokemonName("Pikachu").viewCount(10L).build();
        when(pokemonViewPort.topByViews(5)).thenReturn(List.of(stat));

        List<PopularityStat> result = service.topPopular(5);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPokemonName()).isEqualTo("Pikachu");
    }

    @Test
    @DisplayName("topPickRate: RF-19 debe calcular el porcentaje de eleccion sobre el total de equipos")
    void topPickRate_computesPercentage() {
        PopularityStat stat = PopularityStat.builder().pokemonId(25L).pokemonName("Pikachu").viewCount(10L).build();
        when(pokemonViewPort.topByViews(5)).thenReturn(List.of(stat));
        when(teamPort.countAll()).thenReturn(4L);
        when(teamPort.countTeamsContainingPokemon(25L)).thenReturn(2L);

        List<PopularityStat> result = service.topPickRate(5);

        assertThat(result.get(0).getTeamPickCount()).isEqualTo(2L);
        assertThat(result.get(0).getPickRate()).isEqualTo(50.0);
    }

    @Test
    @DisplayName("generalStats: RF-19 debe retornar totales de usuarios y equipos")
    void generalStats_returnsTotals() {
        when(userPort.findAll()).thenReturn(List.of());
        when(teamPort.countAll()).thenReturn(3L);

        Map<String, Object> result = service.generalStats();

        assertThat(result).containsEntry("totalEquipos", 3L);
    }

    @Test
    @DisplayName("registerPokemonView: debe delegar en el puerto de vistas")
    void registerPokemonView_delegatesToPort() {
        service.registerPokemonView(25L, "Pikachu");
        verify(pokemonViewPort).registerView(25L, "Pikachu");
    }
}
