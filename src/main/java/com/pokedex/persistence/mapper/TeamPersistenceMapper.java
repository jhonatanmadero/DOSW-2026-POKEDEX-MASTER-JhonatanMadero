package com.pokedex.persistence.mapper;

import com.pokedex.core.model.Pokemon;
import com.pokedex.core.model.Team;
import com.pokedex.persistence.entity.relational.TeamEntity;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Mapea TeamEntity (persistence) <-> Team (core). Se implementa como
 * @Component manual (no @Mapper de MapStruct) porque necesita ordenar los
 * miembros por slot y delegar en PokemonPersistenceMapper de forma inyectada.
 */
@Component
@RequiredArgsConstructor
public class TeamPersistenceMapper {

    private final PokemonPersistenceMapper pokemonMapper;

    public Team toDomain(TeamEntity entity) {
        List<Pokemon> pokemons = entity.getMembers().stream()
                .sorted(Comparator.comparing(m -> m.getSlotOrder()))
                .map(m -> pokemonMapper.toDomain(m.getPokemon()))
                .toList();

        return Team.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .pokemons(pokemons)
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
