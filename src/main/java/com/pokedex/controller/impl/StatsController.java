package com.pokedex.controller.impl;

import com.pokedex.controller.api.StatsApi;
import com.pokedex.controller.dto.response.PopularityStatResponse;
import com.pokedex.core.model.PopularityStat;
import com.pokedex.core.service.interfaces.StatsService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StatsController implements StatsApi {

    private final StatsService statsService;

    private PopularityStatResponse toResponse(PopularityStat s) {
        return new PopularityStatResponse(s.getPokemonId(), s.getPokemonName(), s.getViewCount(),
                s.getTeamPickCount(), s.getPickRate());
    }

    @Override
    public ResponseEntity<List<PopularityStatResponse>> popularity(int top) {
        return ResponseEntity.ok(statsService.topPopular(top).stream().map(this::toResponse).toList());
    }

    @Override
    public ResponseEntity<List<PopularityStatResponse>> pickRate(int top) {
        return ResponseEntity.ok(statsService.topPickRate(top).stream().map(this::toResponse).toList());
    }

    @Override
    public ResponseEntity<Map<String, Object>> general() {
        return ResponseEntity.ok(statsService.generalStats());
    }
}
