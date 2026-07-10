package com.pokedex.persistence.entity.relational;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

/**
 * Entidad JPA de Pokemon. Vive SOLO en la capa persistence (Seccion 5.4 del plan).
 * Nota de diseno: a diferencia del ManyToMany generico del plan, aqui se modela
 * tipo primario y secundario como columnas explicitas porque los RF-08/RF-09
 * requieren filtrar por cada uno de forma independiente.
 */
@Entity
@Table(name = "pokemon", indexes = {
        @Index(name = "idx_pokemon_number", columnList = "national_number"),
        @Index(name = "idx_pokemon_name", columnList = "name"),
        @Index(name = "idx_pokemon_type_primary", columnList = "type_primary"),
        @Index(name = "idx_pokemon_generation", columnList = "generation")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PokemonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "national_number", nullable = false, unique = true)
    private Integer nationalNumber;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(name = "image_url", length = 300)
    private String imageUrl;

    @Column(name = "type_primary", nullable = false, length = 30)
    private String typePrimary;

    @Column(name = "type_secondary", length = 30)
    private String typeSecondary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private RegionEntity region;

    @Column(nullable = false)
    private Integer generation;

    @Column(name = "height_meters")
    private Double heightMeters;

    @Column(name = "weight_kg")
    private Double weightKg;

    @Column(name = "has_mega")
    private Boolean hasMega;

    @Column(name = "evolution_stage", length = 30)
    private String evolutionStage;

    // Auto-referencia simplificada de cadena evolutiva (evita modelar N tablas extra para la demo).
    @Column(name = "evolves_from_id")
    private Long evolvesFromId;

    @Column(name = "evolves_to_id")
    private Long evolvesToId;

    @Column(name = "evolution_level")
    private Integer evolutionLevel;

    @Column(name = "evolution_method", length = 100)
    private String evolutionMethod;

    @OneToOne(mappedBy = "pokemon", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private PokemonStatsEntity stats;

    @Builder.Default
    @OneToMany(mappedBy = "pokemon", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PokemonAbilityEntity> abilities = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Metodos de negocio en lugar de setters (Seccion 10.2 del plan)
    public void assignStats(PokemonStatsEntity stats) {
        this.stats = stats;
        if (stats != null) {
            stats.setPokemon(this);
        }
    }

    public void replaceAbilities(List<PokemonAbilityEntity> newAbilities) {
        this.abilities.clear();
        if (newAbilities != null) {
            this.abilities.addAll(newAbilities);
        }
    }
}
