package com.pokedex.core.service.impl;

import com.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.core.model.Role;
import com.pokedex.core.model.User;
import com.pokedex.core.port.UserPersistencePort;
import com.pokedex.core.service.interfaces.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** RF-21: administracion de perfiles de usuario por parte del ADMIN. */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserPersistencePort userPort;

    @Override
    public User findById(Long id) {
        return userPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
    }

    @Override
    public List<User> findAll() {
        return userPort.findAll();
    }

    @Override
    @Transactional
    public User updateProfile(Long id, User changes) {
        User existing = findById(id);
        User updated = existing.toBuilder()
                .fullName(changes.getFullName() != null ? changes.getFullName() : existing.getFullName())
                .favoriteRegion(changes.getFavoriteRegion() != null ? changes.getFavoriteRegion() : existing.getFavoriteRegion())
                .birthDate(changes.getBirthDate() != null ? changes.getBirthDate() : existing.getBirthDate())
                .build();
        return userPort.save(updated);
    }

    @Override
    @Transactional
    public User changeRole(Long id, Role newRole) {
        User existing = findById(id);
        log.info("Cambiando rol de usuario {} a {}", id, newRole);
        return userPort.save(existing.toBuilder().role(newRole).build());
    }

    @Override
    @Transactional
    public void deactivate(Long id) {
        User existing = findById(id);
        userPort.save(existing.toBuilder().active(false).build());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        findById(id);
        userPort.deleteById(id);
    }
}
