package com.pokedex.core.port;

import com.pokedex.core.model.User;
import java.util.List;
import java.util.Optional;

public interface UserPersistencePort {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    User save(User user);
    List<User> findAll();
    void deleteById(Long id);
}
