package com.pokedex.persistence.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.pokedex.core.model.Role;
import com.pokedex.core.model.User;
import com.pokedex.persistence.entity.relational.UserEntity;
import com.pokedex.persistence.mapper.UserPersistenceMapper;
import com.pokedex.persistence.repository.relational.UserJpaRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserPersistenceAdapterTest {

    @Mock private UserJpaRepository repository;
    @Mock private UserPersistenceMapper mapper;

    private UserPersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new UserPersistenceAdapter(repository, mapper);
    }

    @Test
    @DisplayName("findById: mapea la entidad a dominio cuando existe")
    void findById_returnsMappedUser() {
        UserEntity entity = UserEntity.builder().id(1L).email("ash@pokedex.com").build();
        User domain = User.builder().id(1L).email("ash@pokedex.com").build();
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        Optional<User> result = adapter.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("ash@pokedex.com");
    }

    @Test
    @DisplayName("findById: vacio cuando no existe")
    void findById_returnsEmptyWhenMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThat(adapter.findById(99L)).isEmpty();
    }

    @Test
    @DisplayName("findByEmail: busca ignorando mayusculas/minusculas")
    void findByEmail_delegatesToRepository() {
        UserEntity entity = UserEntity.builder().id(1L).email("ash@pokedex.com").build();
        User domain = User.builder().id(1L).email("ash@pokedex.com").build();
        when(repository.findByEmailIgnoreCase("ASH@pokedex.com")).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        assertThat(adapter.findByEmail("ASH@pokedex.com")).contains(domain);
    }

    @Test
    @DisplayName("existsByEmail: delega al repositorio")
    void existsByEmail_delegatesToRepository() {
        when(repository.existsByEmailIgnoreCase("ash@pokedex.com")).thenReturn(true);

        assertThat(adapter.existsByEmail("ash@pokedex.com")).isTrue();
    }

    @Test
    @DisplayName("save: usa TRAINER y active=true por defecto cuando vienen nulos")
    void save_appliesDefaultsWhenNull() {
        User newUser = User.builder().fullName("Ash").email("ash@pokedex.com").build(); // role/active nulos
        UserEntity saved = UserEntity.builder().id(1L).fullName("Ash").email("ash@pokedex.com").build();
        User domain = User.builder().id(1L).fullName("Ash").email("ash@pokedex.com").role(Role.TRAINER).active(true).build();

        when(repository.save(any(UserEntity.class))).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(domain);

        User result = adapter.save(newUser);

        assertThat(result.getRole()).isEqualTo(Role.TRAINER);
        assertThat(result.getActive()).isTrue();
    }

    @Test
    @DisplayName("findAll: mapea todas las entidades a dominio")
    void findAll_mapsAllEntities() {
        UserEntity entity = UserEntity.builder().id(1L).email("ash@pokedex.com").build();
        User domain = User.builder().id(1L).email("ash@pokedex.com").build();
        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        assertThat(adapter.findAll()).containsExactly(domain);
    }

    @Test
    @DisplayName("deleteById: delega al repositorio")
    void deleteById_delegatesToRepository() {
        adapter.deleteById(1L);

        org.mockito.Mockito.verify(repository).deleteById(1L);
    }
}
