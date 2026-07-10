package com.pokedex.persistence.entity.relational;

import jakarta.persistence.*;
import lombok.*;

/** Tabla intermedia Pokemon <-> Habilidad (muchos a muchos con datos propios). */
@Entity
@Table(name = "pokemon_ability")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PokemonAbilityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pokemon_id", nullable = false)
    private PokemonEntity pokemon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ability_id", nullable = false)
    private AbilityEntity ability;
}
