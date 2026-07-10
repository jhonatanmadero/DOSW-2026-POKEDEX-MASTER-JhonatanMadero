package com.pokedex.controller.impl;

import com.pokedex.controller.api.TeamApi;
import com.pokedex.controller.dto.request.TeamRequest;
import com.pokedex.controller.dto.response.TeamAnalysisResponse;
import com.pokedex.controller.dto.response.TeamResponse;
import com.pokedex.controller.mapper.TeamDtoMapper;
import com.pokedex.core.model.Pokemon;
import com.pokedex.core.model.Team;
import com.pokedex.core.service.interfaces.TeamService;
import com.pokedex.security.CustomUserDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TeamController implements TeamApi {

    private final TeamService teamService;
    private final TeamDtoMapper mapper;

    private Long uid(Authentication authentication) {
        return ((CustomUserDetails) authentication.getPrincipal()).getUserId();
    }

    private Team fromRequest(TeamRequest request) {
        List<Pokemon> pokemons = request.pokemonIds().stream()
                .map(pid -> Pokemon.builder().id(pid).build())
                .toList();
        return Team.builder()
                .name(request.name())
                .description(request.description())
                .pokemons(pokemons)
                .build();
    }

    @Override
    public ResponseEntity<TeamResponse> create(Authentication authentication, TeamRequest request) {
        Team created = teamService.create(uid(authentication), fromRequest(request));
        return ResponseEntity.ok(mapper.toResponse(created));
    }

    @Override
    public ResponseEntity<List<TeamResponse>> findAll(Authentication authentication) {
        return ResponseEntity.ok(mapper.toResponseList(teamService.findAllByUser(uid(authentication))));
    }

    @Override
    public ResponseEntity<TeamResponse> findById(Authentication authentication, Long id) {
        return ResponseEntity.ok(mapper.toResponse(teamService.findById(uid(authentication), id)));
    }

    @Override
    public ResponseEntity<TeamResponse> update(Authentication authentication, Long id, TeamRequest request) {
        Team updated = teamService.update(uid(authentication), id, fromRequest(request));
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @Override
    public ResponseEntity<Void> delete(Authentication authentication, Long id) {
        teamService.delete(uid(authentication), id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<TeamAnalysisResponse> analyze(Authentication authentication, Long id) {
        return ResponseEntity.ok(mapper.toAnalysisResponse(teamService.analyze(uid(authentication), id)));
    }
}
