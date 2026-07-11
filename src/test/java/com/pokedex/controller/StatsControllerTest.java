package com.pokedex.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pokedex.controller.impl.StatsController;
import com.pokedex.core.model.PopularityStat;
import com.pokedex.core.service.interfaces.StatsService;
import com.pokedex.security.JwtAuthFilter;
import com.pokedex.security.JwtService;
import com.pokedex.security.UserDetailsServiceImpl;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(StatsController.class)
@AutoConfigureMockMvc(addFilters = false)
class StatsControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private StatsService statsService;

    @MockBean private JwtAuthFilter jwtAuthFilter;
    @MockBean private JwtService jwtService;
    @MockBean private UserDetailsServiceImpl userDetailsService;

    @Test
    void popularity_returnsTopPokemonByViews() throws Exception {
        PopularityStat stat = PopularityStat.builder().pokemonId(25L).pokemonName("Pikachu")
                .viewCount(120L).teamPickCount(30L).pickRate(15.5).build();
        when(statsService.topPopular(10)).thenReturn(List.of(stat));

        mockMvc.perform(get("/v1/stats/popularity"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pokemonName").value("Pikachu"));
    }

    @Test
    void pickRate_returnsTopPokemonByTeamPicks() throws Exception {
        PopularityStat stat = PopularityStat.builder().pokemonId(6L).pokemonName("Charizard")
                .viewCount(80L).teamPickCount(50L).pickRate(40.0).build();
        when(statsService.topPickRate(5)).thenReturn(List.of(stat));

        mockMvc.perform(get("/v1/stats/pick-rate").param("top", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pokemonName").value("Charizard"));
    }

    @Test
    void general_returnsGeneralStatsMap() throws Exception {
        when(statsService.generalStats()).thenReturn(Map.of("totalPokemon", 33, "totalUsers", 4));

        mockMvc.perform(get("/v1/stats/general"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPokemon").value(33));
    }
}
