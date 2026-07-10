package com.pokedex.persistence.repository.relational;

import com.pokedex.persistence.entity.relational.RegionEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionJpaRepository extends JpaRepository<RegionEntity, Long> {
    Optional<RegionEntity> findByNameIgnoreCase(String name);
}
