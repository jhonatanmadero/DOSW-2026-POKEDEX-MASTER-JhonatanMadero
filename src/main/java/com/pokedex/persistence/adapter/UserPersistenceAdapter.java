package com.pokedex.persistence.adapter;

import com.pokedex.core.model.Role;
import com.pokedex.core.model.User;
import com.pokedex.core.port.UserPersistencePort;
import com.pokedex.persistence.entity.relational.RoleEntity;
import com.pokedex.persistence.entity.relational.UserEntity;
import com.pokedex.persistence.mapper.UserPersistenceMapper;
import com.pokedex.persistence.repository.relational.UserJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPersistencePort {

    private final UserJpaRepository repository;
    private final UserPersistenceMapper mapper;

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmailIgnoreCase(email).map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmailIgnoreCase(email);
    }

    @Override
    public User save(User user) {
        UserEntity entity = UserEntity.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .passwordHash(user.getPasswordHash())
                .birthDate(user.getBirthDate())
                .favoriteRegion(user.getFavoriteRegion())
                .role(user.getRole() != null ? RoleEntity.valueOf(user.getRole().name()) : RoleEntity.TRAINER)
                .active(user.getActive() != null ? user.getActive() : true)
                .fromGoogle(user.getFromGoogle() != null ? user.getFromGoogle() : false)
                .avatarUrl(user.getAvatarUrl())
                .build();
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public List<User> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
