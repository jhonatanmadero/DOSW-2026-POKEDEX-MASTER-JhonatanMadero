package com.pokedex.controller.mapper;

import com.pokedex.controller.dto.response.TeamAnalysisResponse;
import com.pokedex.controller.dto.response.TeamResponse;
import com.pokedex.core.model.Team;
import com.pokedex.core.model.TeamAnalysis;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = PokemonDtoMapper.class)
public interface TeamDtoMapper {

    TeamResponse toResponse(Team team);

    List<TeamResponse> toResponseList(List<Team> teams);

    TeamAnalysisResponse toAnalysisResponse(TeamAnalysis analysis);
}
