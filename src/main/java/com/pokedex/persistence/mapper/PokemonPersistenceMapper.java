package com.pokedex.persistence.mapper;

import com.pokedex.core.model.Ability;
import com.pokedex.core.model.Pokemon;
import com.pokedex.core.model.PokemonStats;
import com.pokedex.persistence.entity.relational.AbilityEntity;
import com.pokedex.persistence.entity.relational.PokemonAbilityEntity;
import com.pokedex.persistence.entity.relational.PokemonEntity;
import com.pokedex.persistence.entity.relational.PokemonStatsEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/** Mapea Entidad JPA (persistence) <-> Modelo de negocio Pokemon (core). */
@Mapper(componentModel = "spring")
public interface PokemonPersistenceMapper {

    @Mapping(source = "region.name", target = "region")
    @Mapping(source = "stats", target = "stats")
    @Mapping(target = "abilities", expression = "java(toAbilityList(entity.getAbilities()))")
    @Mapping(target = "evolutionChain", ignore = true) // se resuelve aparte en el adapter (auto-referencia)
    Pokemon toDomain(PokemonEntity entity);

    PokemonStats toStatsDomain(PokemonStatsEntity entity);

    default List<Ability> toAbilityList(List<PokemonAbilityEntity> abilities) {
        if (abilities == null) return List.of();
        return abilities.stream().map(this::toAbilityDomain).toList();
    }

    default Ability toAbilityDomain(PokemonAbilityEntity pa) {
        AbilityEntity a = pa.getAbility();
        return Ability.builder()
                .id(a.getId())
                .name(a.getName())
                .description(a.getDescription())
                .isHidden(a.getIsHidden())
                .build();
    }
}
