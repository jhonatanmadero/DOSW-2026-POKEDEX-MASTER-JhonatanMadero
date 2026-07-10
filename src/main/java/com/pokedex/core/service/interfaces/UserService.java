package com.pokedex.core.service.interfaces;

import com.pokedex.core.model.Role;
import com.pokedex.core.model.User;
import java.util.List;

public interface UserService {
    User findById(Long id);
    List<User> findAll();                          // RF-21
    User updateProfile(Long id, User changes);       // RF-21
    User changeRole(Long id, Role newRole);          // RF-21
    void deactivate(Long id);                        // RF-21
    void delete(Long id);                             // RF-21
}
