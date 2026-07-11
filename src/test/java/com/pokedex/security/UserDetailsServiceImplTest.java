package com.pokedex.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.pokedex.core.model.Role;
import com.pokedex.core.model.User;
import com.pokedex.core.port.UserPersistencePort;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock private UserPersistencePort userPort;
    private UserDetailsServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new UserDetailsServiceImpl(userPort);
    }

    @Test
    @DisplayName("loadUserByUsername: retorna CustomUserDetails cuando el usuario existe")
    void loadUserByUsername_returnsUserDetails() {
        User user = User.builder().id(1L).email("ash@pokedex.com").role(Role.TRAINER).build();
        when(userPort.findByEmail("ash@pokedex.com")).thenReturn(Optional.of(user));

        UserDetails details = service.loadUserByUsername("ash@pokedex.com");

        assertThat(details.getUsername()).isEqualTo("ash@pokedex.com");
        assertThat(((CustomUserDetails) details).getUserId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("loadUserByUsername: lanza UsernameNotFoundException si no existe (RF-01)")
    void loadUserByUsername_throwsWhenNotFound() {
        when(userPort.findByEmail("nadie@pokedex.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("nadie@pokedex.com"));
    }
}
