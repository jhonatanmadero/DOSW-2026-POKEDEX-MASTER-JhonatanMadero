package com.pokedex.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pokedex.core.model.Role;
import com.pokedex.core.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @Mock private JwtService jwtService;
    @Mock private UserDetailsServiceImpl userDetailsService;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private FilterChain filterChain;

    private JwtAuthFilter filter;

    @BeforeEach
    void setUp() {
        filter = new JwtAuthFilter(jwtService, userDetailsService);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Sin header Authorization: continua la cadena sin autenticar")
    void doFilterInternal_noAuthHeader_continuesChain() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    @DisplayName("Header sin prefijo Bearer: continua la cadena sin autenticar")
    void doFilterInternal_headerWithoutBearerPrefix_continuesChain() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Basic abc123");

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    @DisplayName("RN-06: token valido autentica al usuario en el SecurityContext")
    void doFilterInternal_validToken_authenticatesUser() throws Exception {
        CustomUserDetails userDetails = new CustomUserDetails(
                User.builder().id(1L).email("ash@pokedex.com").role(Role.TRAINER).build());

        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");
        when(jwtService.extractUsername("valid-token")).thenReturn("ash@pokedex.com");
        when(userDetailsService.loadUserByUsername("ash@pokedex.com")).thenReturn(userDetails);
        when(jwtService.isTokenValid("valid-token", userDetails)).thenReturn(true);

        filter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getName()).isEqualTo("ash@pokedex.com");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Token invalido: no autentica pero continua la cadena")
    void doFilterInternal_invalidToken_doesNotAuthenticate() throws Exception {
        CustomUserDetails userDetails = new CustomUserDetails(
                User.builder().id(1L).email("ash@pokedex.com").role(Role.TRAINER).build());

        when(request.getHeader("Authorization")).thenReturn("Bearer bad-token");
        when(jwtService.extractUsername("bad-token")).thenReturn("ash@pokedex.com");
        when(userDetailsService.loadUserByUsername("ash@pokedex.com")).thenReturn(userDetails);
        when(jwtService.isTokenValid("bad-token", userDetails)).thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }
}
