package com.pokedex.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.core.model.Role;
import com.pokedex.core.model.User;
import com.pokedex.core.port.UserPersistencePort;
import com.pokedex.core.service.impl.UserServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock private UserPersistencePort userPort;

    private UserServiceImpl service;
    private User misty;

    @BeforeEach
    void setUp() {
        service = new UserServiceImpl(userPort);
        misty = User.builder().id(2L).fullName("Misty").email("misty@pokedex.com")
                .role(Role.TRAINER).active(true).build();
    }

    @Test
    @DisplayName("findById: debe lanzar 404 si el usuario no existe")
    void findById_whenNotFound_throws() {
        when(userPort.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    @DisplayName("findAll: RF-21 debe listar todos los usuarios")
    void findAll_returnsList() {
        when(userPort.findAll()).thenReturn(List.of(misty));
        List<User> result = service.findAll();
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("updateProfile: RF-21 debe actualizar solo los campos provistos")
    void updateProfile_updatesFields() {
        when(userPort.findById(2L)).thenReturn(Optional.of(misty));
        User changes = User.builder().favoriteRegion("Kanto").build();
        when(userPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        User result = service.updateProfile(2L, changes);

        assertThat(result.getFavoriteRegion()).isEqualTo("Kanto");
        assertThat(result.getFullName()).isEqualTo("Misty"); // se conserva lo no enviado
    }

    @Test
    @DisplayName("changeRole: RF-21 debe cambiar el rol del usuario")
    void changeRole_updatesRole() {
        when(userPort.findById(2L)).thenReturn(Optional.of(misty));
        when(userPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        User result = service.changeRole(2L, Role.ADMIN);

        assertThat(result.getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    @DisplayName("deactivate: RF-21 debe marcar al usuario como inactivo")
    void deactivate_setsActiveFalse() {
        when(userPort.findById(2L)).thenReturn(Optional.of(misty));
        when(userPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        service.deactivate(2L);

        verify(userPort).save(argThat(u -> !u.getActive()));
    }

    @Test
    @DisplayName("delete: RF-21 debe lanzar 404 si el usuario no existe")
    void delete_whenNotFound_throws() {
        when(userPort.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.delete(99L));
        verify(userPort, never()).deleteById(any());
    }
}
