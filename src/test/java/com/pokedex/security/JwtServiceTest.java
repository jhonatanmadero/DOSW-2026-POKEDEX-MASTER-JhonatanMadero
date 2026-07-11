package com.pokedex.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.pokedex.core.model.Role;
import com.pokedex.core.model.User;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        String secret = Base64.getEncoder().encodeToString(
                "test-secret-key-for-jwt-unit-tests-1234567890".getBytes());
        ReflectionTestUtils.setField(jwtService, "secret", secret);
        ReflectionTestUtils.setField(jwtService, "expirationMs", 86_400_000L); // 24h, RN-06
    }

    private User trainer() {
        return User.builder().id(1L).email("ash@pokedex.com").role(Role.TRAINER).build();
    }

    @Test
    @DisplayName("issue: genera un token valido con el email del usuario como subject")
    void issue_generatesValidToken() {
        String token = jwtService.issue(trainer());

        assertThat(token).isNotBlank();
        assertThat(jwtService.extractUsername(token)).isEqualTo("ash@pokedex.com");
    }

    @Test
    @DisplayName("generateToken: crea un token a partir de UserDetails")
    void generateToken_fromUserDetails() {
        CustomUserDetails details = new CustomUserDetails(trainer());

        String token = jwtService.generateToken(details);

        assertThat(jwtService.extractUsername(token)).isEqualTo("ash@pokedex.com");
    }

    @Test
    @DisplayName("isTokenValid: true cuando el token corresponde al usuario y no ha expirado")
    void isTokenValid_trueForMatchingUser() {
        CustomUserDetails details = new CustomUserDetails(trainer());
        String token = jwtService.generateToken(details);

        assertThat(jwtService.isTokenValid(token, details)).isTrue();
    }

    @Test
    @DisplayName("isTokenValid: false cuando el token pertenece a otro usuario")
    void isTokenValid_falseForDifferentUser() {
        CustomUserDetails owner = new CustomUserDetails(trainer());
        String token = jwtService.generateToken(owner);

        CustomUserDetails other = new CustomUserDetails(
                User.builder().id(2L).email("misty@pokedex.com").role(Role.TRAINER).build());

        assertThat(jwtService.isTokenValid(token, other)).isFalse();
    }

    @Test
    @DisplayName("RN-06: un token ya vencido lanza ExpiredJwtException al validarse")
    void isTokenValid_throwsWhenTokenExpired() {
        ReflectionTestUtils.setField(jwtService, "expirationMs", -1000L); // vencido al emitirse
        CustomUserDetails details = new CustomUserDetails(trainer());
        String token = jwtService.generateToken(details);

        assertThrows(ExpiredJwtException.class, () -> jwtService.isTokenValid(token, details));
    }
}
