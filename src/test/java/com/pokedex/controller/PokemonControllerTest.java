package com.pokedex.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pokedex.controller.dto.response.PokemonResponse;
import com.pokedex.controller.dto.response.PokemonStatsResponse;
import com.pokedex.controller.impl.PokemonController;
import com.pokedex.controller.mapper.PokemonDtoMapper;
import com.pokedex.core.model.Pokemon;
import com.pokedex.core.model.PokemonStats;
import com.pokedex.core.service.interfaces.PokemonService;
import com.pokedex.core.service.interfaces.StatsService;
import com.pokedex.security.JwtAuthFilter;
import com.pokedex.security.JwtService;
import com.pokedex.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PokemonController.class)
@AutoConfigureMockMvc(addFilters = false) // sin filtros de seguridad en pruebas unitarias de controller
class PokemonControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private PokemonService pokemonService;
    @MockBean private StatsService statsService;
    @MockBean private PokemonDtoMapper mapper;

    // @WebMvcTest detecta JwtAuthFilter porque es un bean tipo Filter, aunque
    // addFilters=false evita que se ejecute; igual necesita sus dependencias
    // satisfechas para poder construirse dentro del contexto de la prueba.
    @MockBean private JwtAuthFilter jwtAuthFilter;
    @MockBean private JwtService jwtService;
    @MockBean private UserDetailsServiceImpl userDetailsService;

    @Test
    void findById_returns200WithPokemonData() throws Exception {
        Pokemon pikachu = Pokemon.builder().id(1L).name("Pikachu").nationalNumber(25)
                .typePrimary("Electrico")
                .stats(PokemonStats.builder().hp(35).attack(55).defense(40)
                        .specialAttack(50).specialDefense(50).speed(90).build())
                .build();
        PokemonResponse response = new PokemonResponse(1L, 25, "Pikachu", null, null,
                "Electrico", null, "Kanto", 1, 0.4, 6.0, false, "PRIMERA", null, null,
                new PokemonStatsResponse(35, 55, 40, 50, 50, 90, 320));

        when(pokemonService.findById(1L)).thenReturn(pikachu);
        when(mapper.toResponse(pikachu)).thenReturn(response);

        mockMvc.perform(get("/v1/pokemon/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pikachu"))
                .andExpect(jsonPath("$.typePrimary").value("Electrico"));
    }

    @Test
    void create_withInvalidBody_returns400() throws Exception {
        String invalidJson = "{\"name\": \"\"}"; // faltan campos obligatorios

        mockMvc.perform(post("/v1/pokemon")
                        .contentType("application/json")
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}
