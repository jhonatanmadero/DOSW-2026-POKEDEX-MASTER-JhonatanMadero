package com.pokedex.core.service.impl;

import com.pokedex.core.model.PopularityStat;
import com.pokedex.core.port.PokemonViewPort;
import com.pokedex.core.port.TeamPersistencePort;
import com.pokedex.core.port.UserPersistencePort;
import com.pokedex.core.service.interfaces.StatsService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** RF-19: estadisticas de popularidad, tasa de eleccion y metricas generales. */
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final PokemonViewPort pokemonViewPort;
    private final TeamPersistencePort teamPort;
    private final UserPersistencePort userPort;

    @Override
    public List<PopularityStat> topPopular(int limit) {
        return pokemonViewPort.topByViews(limit);
    }

    @Override
    public List<PopularityStat> topPickRate(int limit) {
        long totalTeams = Math.max(teamPort.countAll(), 1);
        return pokemonViewPort.topByViews(limit).stream()
                .map(stat -> {
                    long picks = teamPort.countTeamsContainingPokemon(stat.getPokemonId());
                    double rate = (picks * 100.0) / totalTeams;
                    return PopularityStat.builder()
                            .pokemonId(stat.getPokemonId())
                            .pokemonName(stat.getPokemonName())
                            .viewCount(stat.getViewCount())
                            .teamPickCount(picks)
                            .pickRate(rate)
                            .build();
                })
                .sorted((a, b) -> Long.compare(b.getTeamPickCount(), a.getTeamPickCount()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> generalStats() {
        return Map.of(
                "totalUsuarios", userPort.findAll().size(),
                "totalEquipos", teamPort.countAll()
        );
    }

    @Override
    public void registerPokemonView(Long pokemonId, String pokemonName) {
        pokemonViewPort.registerView(pokemonId, pokemonName);
    }
}
