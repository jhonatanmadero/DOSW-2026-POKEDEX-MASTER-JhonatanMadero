package com.pokedex.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pokedex.controller.dto.response.TokenResponse;
import com.pokedex.controller.dto.response.UserResponse;
import com.pokedex.controller.impl.AuthController;
import com.pokedex.controller.mapper.UserDtoMapper;
import com.pokedex.core.model.Role;
import com.pokedex.core.model.User;
import com.pokedex.core.service.interfaces.AuthService;
import com.pokedex.security.JwtAuthFilter;
import com.pokedex.security.JwtService;
import com.pokedex.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private AuthService authService;
    @MockBean private UserDtoMapper mapper;

    @MockBean private JwtAuthFilter jwtAuthFilter;
    @MockBean private JwtService jwtService;
    @MockBean private UserDetailsServiceImpl userDetailsService;

    private static final String VALID_REGISTER_JSON = """
            {
              "fullName": "Ash Ketchum",
              "email": "ash@pokedex.com",
              "password": "Password1",
              "confirmPassword": "Password1",
              "favoriteRegion": "Kanto"
            }
            """;

    private TokenResponse fakeTokenResponse() {
        UserResponse userResponse = new UserResponse(1L, "Ash Ketchum", "ash@pokedex.com", null, "Kanto", "TRAINER", true, false, null);
        return new TokenResponse("fake-jwt-token", userResponse);
    }

    @Test
    void register_withMismatchedPasswords_returns400() throws Exception {
        String json = """
                {
                  "fullName": "Ash Ketchum",
                  "email": "ash@pokedex.com",
                  "password": "Password1",
                  "confirmPassword": "Password2",
                  "favoriteRegion": "Kanto"
                }
                """;

        mockMvc.perform(post("/v1/auth/register")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_withValidData_returns200WithToken() throws Exception {
        User user = User.builder().id(1L).fullName("Ash Ketchum").email("ash@pokedex.com").role(Role.TRAINER).build();
        AuthService.AuthResult result = new AuthService.AuthResult(user, "fake-jwt-token");

        when(authService.register(any(User.class), eq("Password1"))).thenReturn(result);
        when(mapper.toTokenResponse(result)).thenReturn(fakeTokenResponse());

        mockMvc.perform(post("/v1/auth/register")
                        .contentType("application/json")
                        .content(VALID_REGISTER_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));
    }

    @Test
    void login_withInvalidEmailFormat_returns400() throws Exception {
        String json = "{ \"email\": \"no-es-un-correo\", \"password\": \"Password1\" }";

        mockMvc.perform(post("/v1/auth/login")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_withValidCredentials_returns200WithToken() throws Exception {
        User user = User.builder().id(1L).fullName("Ash Ketchum").email("ash@pokedex.com").role(Role.TRAINER).build();
        AuthService.AuthResult result = new AuthService.AuthResult(user, "fake-jwt-token");

        when(authService.login("ash@pokedex.com", "Password1")).thenReturn(result);
        when(mapper.toTokenResponse(result)).thenReturn(fakeTokenResponse());

        mockMvc.perform(post("/v1/auth/login")
                        .contentType("application/json")
                        .content("{\"email\":\"ash@pokedex.com\",\"password\":\"Password1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.email").value("ash@pokedex.com"));
    }
}
