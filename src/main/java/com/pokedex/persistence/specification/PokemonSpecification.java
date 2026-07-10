package com.pokedex.persistence.specification;

import com.pokedex.core.model.PokemonFilterCriteria;
import com.pokedex.persistence.entity.relational.PokemonAbilityEntity;
import com.pokedex.persistence.entity.relational.PokemonEntity;
import com.pokedex.persistence.entity.relational.PokemonStatsEntity;
import com.pokedex.persistence.entity.relational.RegionEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

/**
 * Construye consultas dinamicas para el catalogo de Pokemon a partir de
 * PokemonFilterCriteria. Cubre RF-04 (nombre), RF-05 (numero), RF-07 (region),
 * RF-08/RF-09 (tipos), RF-10 (generacion), RF-11 (rango de stat), RF-12 (habilidad),
 * RF-13 (mega evolucion), RF-14 (etapa evolutiva) y RF-22 (peso).
 */
public final class PokemonSpecification {

    private PokemonSpecification() {}

    public static Specification<PokemonEntity> fromCriteria(PokemonFilterCriteria c) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            query.distinct(true);

            if (c.getNameContains() != null && !c.getNameContains().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + c.getNameContains().toLowerCase() + "%"));
            }
            if (c.getNationalNumber() != null) {
                predicates.add(cb.equal(root.get("nationalNumber"), c.getNationalNumber()));
            }
            if (c.getRegion() != null && !c.getRegion().isBlank()) {
                Join<PokemonEntity, RegionEntity> region = root.join("region");
                predicates.add(cb.equal(cb.lower(region.get("name")), c.getRegion().toLowerCase()));
            }
            if (c.getTypePrimary() != null && !c.getTypePrimary().isBlank()) {
                predicates.add(cb.equal(cb.lower(root.get("typePrimary")), c.getTypePrimary().toLowerCase()));
            }
            if (c.getTypeSecondary() != null && !c.getTypeSecondary().isBlank()) {
                predicates.add(cb.equal(cb.lower(root.get("typeSecondary")), c.getTypeSecondary().toLowerCase()));
            }
            if (c.getGeneration() != null) {
                predicates.add(cb.equal(root.get("generation"), c.getGeneration()));
            }
            if (c.getHasMega() != null) {
                predicates.add(cb.equal(root.get("hasMega"), c.getHasMega()));
            }
            if (c.getEvolutionStage() != null && !c.getEvolutionStage().isBlank()) {
                predicates.add(cb.equal(cb.lower(root.get("evolutionStage")), c.getEvolutionStage().toLowerCase()));
            }
            if (c.getWeightMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("weightKg"), c.getWeightMin()));
            }
            if (c.getWeightMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("weightKg"), c.getWeightMax()));
            }
            if (c.getStatName() != null && (c.getStatMin() != null || c.getStatMax() != null)) {
                Join<PokemonEntity, PokemonStatsEntity> stats = root.join("stats");
                String field = mapStatField(c.getStatName());
                if (c.getStatMin() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(stats.get(field), c.getStatMin()));
                }
                if (c.getStatMax() != null) {
                    predicates.add(cb.lessThanOrEqualTo(stats.get(field), c.getStatMax()));
                }
            }
            if (c.getAbility() != null && !c.getAbility().isBlank()) {
                Join<PokemonEntity, PokemonAbilityEntity> pa = root.join("abilities");
                predicates.add(cb.equal(cb.lower(pa.get("ability").get("name")), c.getAbility().toLowerCase()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static String mapStatField(String statName) {
        return switch (statName.toLowerCase()) {
            case "hp" -> "hp";
            case "attack", "atk" -> "attack";
            case "defense", "def" -> "defense";
            case "specialattack", "spa" -> "specialAttack";
            case "specialdefense", "spd" -> "specialDefense";
            case "speed", "spe" -> "speed";
            default -> "hp";
        };
    }
}
