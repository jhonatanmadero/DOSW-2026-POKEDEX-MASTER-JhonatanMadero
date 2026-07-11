package com.pokedex.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pokedex.controller.dto.response.TeamAnalysisResponse;
import com.pokedex.controller.dto.response.TeamResponse;
import com.pokedex.controller.impl.TeamController;
import com.pokedex.controller.mapper.TeamDtoMapper;
import com.pokedex.core.model.Role;
import com.pokedex.core.model.Team;
import com.pokedex.core.model.TeamAnalysis;
import com.pokedex.core.model.User;
import com.pokedex.core.service.interfaces.TeamService;
import com.pokedex.security.CustomUserDetails;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TeamController.class)
@AutoConfigureMockMvc(addFilters = false)
class TeamControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private TeamService teamService;
    @MockBean private TeamDtoMapper mapper;

    @MockBean private JwtAuthFilter jwtAuthFilter;
    @MockBean private JwtService jwtService;
    @MockBean private UserDetailsServiceImpl userDetailsService;

    private Authentication trainerAuth() {
        CustomUserDetails principal = new CustomUserDetails(
                User.builder().id(1L).email("ash@pokedex.com").role(Role.TRAINER).build());
        return new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
    }

    @Test
    void create_withEmptyPokemonList_returns400() throws Exception {
        mockMvc.perform(post("/v1/teams").principal(trainerAuth())
                        .contentType("application/json")
                        .content("{\"name\":\"Iniciales\",\"pokemonIds\":[]}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_withValidData_returns200() throws Exception {
        Team created = Team.builder().id(1L).name("Iniciales").build();
        TeamResponse response = new TeamResponse(1L, "Iniciales", null, List.of(), null);
        when(teamService.create(eq(1L), any(Team.class))).thenReturn(created);
        when(mapper.toResponse(created)).thenReturn(response);

        mockMvc.perform(post("/v1/teams").principal(trainerAuth())
                        .contentType("application/json")
                        .content("{\"name\":\"Iniciales\",\"pokemonIds\":[25]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Iniciales"));
    }

    @Test
    void findAll_returnsTeamsOfAuthenticatedUser() throws Exception {
        when(teamService.findAllByUser(1L)).thenReturn(List.of(Team.builder().id(1L).name("Iniciales").build()));
        when(mapper.toResponseList(anyList())).thenReturn(List.of(new TeamResponse(1L, "Iniciales", null, List.of(), null)));

        mockMvc.perform(get("/v1/teams").principal(trainerAuth()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Iniciales"));
    }

    @Test
    void analyze_returnsCompetitiveAnalysis() throws Exception {
        TeamAnalysis analysis = TeamAnalysis.builder().teamId(1L).teamName("Iniciales")
                .totalHp(150).totalAttack(120).totalDefense(100)
                .totalSpecialAttack(90).totalSpecialDefense(90).totalSpeed(110)
                .typeWeaknesses(Map.of("Agua", 2.0)).typeResistances(Map.of())
                .typeCoverage(List.of("Fuego")).recommendations(List.of()).build();
        TeamAnalysisResponse response = new TeamAnalysisResponse(1L, "Iniciales", 150, 120, 100, 90, 90, 110,
                Map.of("Agua", 2.0), Map.of(), List.of("Fuego"), List.of());

        when(teamService.analyze(1L, 1L)).thenReturn(analysis);
        when(mapper.toAnalysisResponse(analysis)).thenReturn(response);

        mockMvc.perform(get("/v1/teams/1/analysis").principal(trainerAuth()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teamName").value("Iniciales"));
    }
}
