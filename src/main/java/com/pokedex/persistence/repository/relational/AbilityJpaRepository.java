package com.pokedex.persistence.repository.relational;

import com.pokedex.persistence.entity.relational.AbilityEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbilityJpaRepository extends JpaRepository<AbilityEntity, Long> {
    Optional<AbilityEntity> findByNameIgnoreCase(String name);
}
