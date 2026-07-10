package com.pokedex.persistence.repository.relational;

import com.pokedex.persistence.entity.relational.PokemonEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repositorio JPA de Pokemon. Usa @EntityGraph para evitar el problema N+1
 * (Seccion 11 del plan) al cargar stats, region y habilidades en una sola consulta.
 */
public interface PokemonJpaRepository extends JpaRepository<PokemonEntity, Long>,
        JpaSpecificationExecutor<PokemonEntity> {

    @EntityGraph(attributePaths = {"stats", "region", "abilities", "abilities.ability"})
    Optional<PokemonEntity> findWithDetailsById(Long id);

    @EntityGraph(attributePaths = {"stats", "region", "abilities", "abilities.ability"})
    Optional<PokemonEntity> findWithDetailsByNationalNumber(Integer nationalNumber);

    @EntityGraph(attributePaths = {"stats", "region"})
    @Query("SELECT p FROM PokemonEntity p")
    Page<PokemonEntity> findAllWithStatsAndRegion(Pageable pageable);

    boolean existsByNationalNumber(Integer nationalNumber);

    @Query("SELECT DISTINCT a.name FROM PokemonAbilityEntity pa JOIN pa.ability a ORDER BY a.name")
    java.util.List<String> findAllAbilityNames();
}
