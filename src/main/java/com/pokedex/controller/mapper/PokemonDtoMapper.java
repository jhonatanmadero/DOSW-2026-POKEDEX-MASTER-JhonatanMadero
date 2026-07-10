package com.pokedex.controller.mapper;

import com.pokedex.controller.dto.request.PokemonRequest;
import com.pokedex.controller.dto.response.PokemonResponse;
import com.pokedex.controller.dto.response.PokemonSummaryResponse;
import com.pokedex.core.model.Ability;
import com.pokedex.core.model.Pokemon;
import com.pokedex.core.model.PokemonStats;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PokemonDtoMapper {

    PokemonResponse toResponse(Pokemon pokemon);

    default com.pokedex.controller.dto.response.PokemonStatsResponse toStatsResponse(PokemonStats stats) {
        if (stats == null) return null;
        return new com.pokedex.controller.dto.response.PokemonStatsResponse(
                stats.getHp(), stats.getAttack(), stats.getDefense(),
                stats.getSpecialAttack(), stats.getSpecialDefense(), stats.getSpeed(), stats.getTotal());
    }

    PokemonSummaryResponse toSummary(Pokemon pokemon);

    List<PokemonSummaryResponse> toSummaryList(List<Pokemon> pokemons);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hasMega", expression = "java(request.hasMega() != null ? request.hasMega() : false)")
    @Mapping(target = "abilities", source = "abilities")
    @Mapping(target = "evolutionChain", ignore = true)
    Pokemon toDomain(PokemonRequest request);

    @Named("abilityFromRequest")
    default Ability abilityFromRequest(PokemonRequest.AbilityRequest a) {
        if (a == null) return null;
        return Ability.builder().name(a.name()).description(a.description())
                .isHidden(a.isHidden() != null ? a.isHidden() : false).build();
    }

    default List<Ability> mapAbilities(List<PokemonRequest.AbilityRequest> requests) {
        if (requests == null) return List.of();
        return requests.stream().map(this::abilityFromRequest).toList();
    }

    PokemonStats toStatsDomain(com.pokedex.controller.dto.request.PokemonStatsRequest request);
}
