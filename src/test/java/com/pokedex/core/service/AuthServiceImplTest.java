package com.pokedex.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.pokedex.core.exception.DuplicateResourceException;
import com.pokedex.core.exception.InvalidCredentialsException;
import com.pokedex.core.model.Role;
import com.pokedex.core.model.User;
import com.pokedex.core.port.UserPersistencePort;
import com.pokedex.core.service.impl.AuthServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock private UserPersistencePort userPort;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthServiceImpl.TokenIssuer tokenIssuer;

    private AuthServiceImpl service;
    private User ash;

    @BeforeEach
    void setUp() {
        service = new AuthServiceImpl(userPort, passwordEncoder, tokenIssuer);
        ash = User.builder().id(1L).fullName("Ash Ketchum").email("ash@pokedex.com")
                .passwordHash("hashed").role(Role.TRAINER).active(true).build();
    }

    @Test
    @DisplayName("register: RF-01 debe lanzar DuplicateResourceException si el correo ya existe")
    void register_whenEmailExists_throws() {
        when(userPort.existsByEmail("ash@pokedex.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> service.register(ash, "Password1"));
        verify(userPort, never()).save(any());
    }

    @Test
    @DisplayName("register: RF-01 debe crear el usuario y retornar token cuando el correo es nuevo")
    void register_whenNewEmail_createsUser() {
        when(userPort.existsByEmail("ash@pokedex.com")).thenReturn(false);
        when(passwordEncoder.encode("Password1")).thenReturn("hashed");
        when(userPort.save(any())).thenReturn(ash);
        when(tokenIssuer.issue(ash)).thenReturn("jwt-token");

        AuthServiceImpl.AuthResult result = service.register(ash, "Password1");

        assertThat(result.token()).isEqualTo("jwt-token");
        assertThat(result.user().getEmail()).isEqualTo("ash@pokedex.com");
    }

    @Test
    @DisplayName("login: debe lanzar InvalidCredentialsException si el correo no existe")
    void login_whenEmailNotFound_throws() {
        when(userPort.findByEmail("noexiste@pokedex.com")).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class,
                () -> service.login("noexiste@pokedex.com", "cualquier"));
    }

    @Test
    @DisplayName("login: debe lanzar InvalidCredentialsException si la contrasena no coincide")
    void login_whenPasswordDoesNotMatch_throws() {
        when(userPort.findByEmail("ash@pokedex.com")).thenReturn(Optional.of(ash));
        when(passwordEncoder.matches("incorrecta", "hashed")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> service.login("ash@pokedex.com", "incorrecta"));
    }

    @Test
    @DisplayName("login: debe retornar token cuando las credenciales son correctas")
    void login_whenValid_returnsToken() {
        when(userPort.findByEmail("ash@pokedex.com")).thenReturn(Optional.of(ash));
        when(passwordEncoder.matches("Password1", "hashed")).thenReturn(true);
        when(tokenIssuer.issue(ash)).thenReturn("jwt-token");

        AuthServiceImpl.AuthResult result = service.login("ash@pokedex.com", "Password1");

        assertThat(result.token()).isEqualTo("jwt-token");
    }
}
