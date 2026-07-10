package com.pokedex.persistence.entity.relational;

import jakarta.persistence.*;
import lombok.*;

/** Relacion equipo <-> Pokemon, con el slot (orden 1-6) dentro del equipo (RN-03). */
@Entity
@Table(name = "team_pokemon")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TeamPokemonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private TeamEntity team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pokemon_id", nullable = false)
    private PokemonEntity pokemon;

    @Column(name = "slot_order", nullable = false)
    private Integer slotOrder;
}
