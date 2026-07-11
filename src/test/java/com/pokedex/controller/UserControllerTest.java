package com.pokedex.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pokedex.controller.dto.response.UserResponse;
import com.pokedex.controller.impl.UserController;
import com.pokedex.controller.mapper.UserDtoMapper;
import com.pokedex.core.model.Role;
import com.pokedex.core.model.User;
import com.pokedex.core.service.interfaces.UserService;
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

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private UserService userService;
    @MockBean private UserDtoMapper mapper;

    @MockBean private JwtAuthFilter jwtAuthFilter;
    @MockBean private JwtService jwtService;
    @MockBean private UserDetailsServiceImpl userDetailsService;

    private Authentication trainerAuth() {
        CustomUserDetails principal = new CustomUserDetails(
                User.builder().id(1L).email("ash@pokedex.com").role(Role.TRAINER).build());
        return new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
    }

    private UserResponse userResponse() {
        return new UserResponse(1L, "Ash Ketchum", "ash@pokedex.com", null, "Kanto", "TRAINER", true, false, null);
    }

    @Test
    void me_returnsOwnProfile() throws Exception {
        User user = User.builder().id(1L).fullName("Ash Ketchum").email("ash@pokedex.com").role(Role.TRAINER).build();
        when(userService.findById(1L)).thenReturn(user);
        when(mapper.toResponse(user)).thenReturn(userResponse());

        mockMvc.perform(get("/v1/users/me").principal(trainerAuth()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("ash@pokedex.com"));
    }

    @Test
    void findAll_returnsAllUsers_forAdmin() throws Exception {
        User user = User.builder().id(1L).fullName("Ash Ketchum").email("ash@pokedex.com").role(Role.TRAINER).build();
        when(userService.findAll()).thenReturn(List.of(user));
        when(mapper.toResponse(user)).thenReturn(userResponse());

        mockMvc.perform(get("/v1/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("ash@pokedex.com"));
    }

    @Test
    void changeRole_updatesUserRole() throws Exception {
        User promoted = User.builder().id(2L).fullName("Misty").email("misty@pokedex.com").role(Role.ADMIN).build();
        UserResponse response = new UserResponse(2L, "Misty", "misty@pokedex.com", null, "Kanto", "ADMIN", true, false, null);
        when(userService.changeRole(eq(2L), any(Role.class))).thenReturn(promoted);
        when(mapper.toResponse(promoted)).thenReturn(response);

        mockMvc.perform(patch("/v1/admin/users/2/role")
                        .contentType("application/json")
                        .content("{\"role\":\"ADMIN\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    void deactivate_returns204() throws Exception {
        mockMvc.perform(patch("/v1/admin/users/2/deactivate"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_returns204() throws Exception {
        mockMvc.perform(delete("/v1/admin/users/2"))
                .andExpect(status().isNoContent());
    }
}
