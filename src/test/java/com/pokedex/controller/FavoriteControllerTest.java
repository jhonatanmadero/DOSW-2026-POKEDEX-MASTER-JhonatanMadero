package com.pokedex.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pokedex.controller.impl.FavoriteController;
import com.pokedex.core.model.Favorite;
import com.pokedex.core.model.Role;
import com.pokedex.core.model.User;
import com.pokedex.core.service.interfaces.FavoriteService;
import com.pokedex.security.CustomUserDetails;
import com.pokedex.security.JwtAuthFilter;
import com.pokedex.security.JwtService;
import com.pokedex.security.UserDetailsServiceImpl;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(FavoriteController.class)
@AutoConfigureMockMvc(addFilters = false)
class FavoriteControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private FavoriteService favoriteService;

    @MockBean private JwtAuthFilter jwtAuthFilter;
    @MockBean private JwtService jwtService;
    @MockBean private UserDetailsServiceImpl userDetailsService;

    // RN-05: los endpoints de favoritos requieren un usuario autenticado
    private Authentication trainerAuth() {
        CustomUserDetails principal = new CustomUserDetails(
                User.builder().id(1L).email("ash@pokedex.com").role(Role.TRAINER).build());
        return new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
    }

    @Test
    void findAll_returnsFavoritesOfAuthenticatedUser() throws Exception {
        Favorite fav = Favorite.builder().id(1L).userId(1L).pokemonId(25L).pokemonName("Pikachu").build();
        when(favoriteService.findAllByUser(1L)).thenReturn(List.of(fav));

        mockMvc.perform(get("/v1/favorites").principal(trainerAuth()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pokemonName").value("Pikachu"));
    }

    @Test
    void add_savesFavoriteForAuthenticatedUser() throws Exception {
        Favorite saved = Favorite.builder().id(10L).userId(1L).pokemonId(25L).pokemonName("Pikachu").build();
        when(favoriteService.add(1L, 25L)).thenReturn(saved);

        mockMvc.perform(post("/v1/favorites").principal(trainerAuth())
                        .contentType("application/json")
                        .content("{\"pokemonId\":25}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pokemonId").value(25));
    }

    @Test
    void remove_returns204() throws Exception {
        mockMvc.perform(delete("/v1/favorites/25").principal(trainerAuth()))
                .andExpect(status().isNoContent());
    }
}
