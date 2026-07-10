package com.pokedex.persistence.entity.relational;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pokemon_stats")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PokemonStatsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pokemon_id", nullable = false, unique = true)
    private PokemonEntity pokemon;

    @Column(nullable = false)
    private Integer hp;
    @Column(nullable = false)
    private Integer attack;
    @Column(nullable = false)
    private Integer defense;
    @Column(name = "special_attack", nullable = false)
    private Integer specialAttack;
    @Column(name = "special_defense", nullable = false)
    private Integer specialDefense;
    @Column(nullable = false)
    private Integer speed;
}
