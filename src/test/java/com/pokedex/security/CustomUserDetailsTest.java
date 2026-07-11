package com.pokedex.security;

import static org.assertj.core.api.Assertions.assertThat;

import com.pokedex.core.model.Role;
import com.pokedex.core.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CustomUserDetailsTest {

    @Test
    @DisplayName("Expone los datos del usuario segun el contrato de UserDetails")
    void exposesUserDataThroughUserDetailsContract() {
        User user = User.builder()
                .id(7L).email("brock@pokedex.com").passwordHash("hashed")
                .role(Role.ADMIN).active(true).build();

        CustomUserDetails details = new CustomUserDetails(user);

        assertThat(details.getUserId()).isEqualTo(7L);
        assertThat(details.getUsername()).isEqualTo("brock@pokedex.com");
        assertThat(details.getPassword()).isEqualTo("hashed");
        assertThat(details.isEnabled()).isTrue();
        assertThat(details.getAuthorities())
                .extracting(a -> a.getAuthority())
                .containsExactly("ROLE_ADMIN");
        assertThat(details.isAccountNonExpired()).isTrue();
        assertThat(details.isAccountNonLocked()).isTrue();
        assertThat(details.isCredentialsNonExpired()).isTrue();
    }

    @Test
    @DisplayName("isEnabled es falso cuando el usuario esta desactivado (RF-21)")
    void isEnabled_falseWhenUserInactive() {
        User user = User.builder().id(1L).email("x@x.com").role(Role.TRAINER).active(false).build();

        assertThat(new CustomUserDetails(user).isEnabled()).isFalse();
    }

    @Test
    @DisplayName("isEnabled es falso cuando el campo active es null")
    void isEnabled_falseWhenActiveIsNull() {
        User user = User.builder().id(1L).email("x@x.com").role(Role.TRAINER).active(null).build();

        assertThat(new CustomUserDetails(user).isEnabled()).isFalse();
    }
}
