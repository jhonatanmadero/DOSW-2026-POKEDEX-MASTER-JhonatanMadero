package com.pokedex.core.service.impl;

import com.pokedex.core.exception.ForbiddenOperationException;
import com.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.core.model.Pokemon;
import com.pokedex.core.model.PokemonStats;
import com.pokedex.core.model.Team;
import com.pokedex.core.model.TeamAnalysis;
import com.pokedex.core.port.TeamPersistencePort;
import com.pokedex.core.service.interfaces.TeamService;
import com.pokedex.core.util.TypeEffectivenessUtil;
import com.pokedex.core.validator.TeamValidator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** RF-16, RF-17, RF-18: creacion, gestion y analisis competitivo de equipos Pokemon. */
@Service
@RequiredArgsConstructor
@Slf4j
public class TeamServiceImpl implements TeamService {

    private static final List<String> ALL_TYPES = List.of(
            "Normal", "Fuego", "Agua", "Planta", "Electrico", "Hielo",
            "Volador", "Veneno", "Psiquico", "Roca", "Tierra");

    private final TeamPersistencePort teamPort;
    private final TeamValidator teamValidator;

    @Override
    @Transactional
    public Team create(Long userId, Team team) {
        Team toValidate = team.toBuilder().userId(userId).build();
        teamValidator.validate(toValidate);
        Team toSave = toValidate.toBuilder().createdAt(LocalDateTime.now()).build();
        log.info("Creando equipo '{}' para usuario {}", toSave.getName(), userId);
        return teamPort.save(toSave);
    }

    @Override
    public List<Team> findAllByUser(Long userId) {
        return teamPort.findAllByUserId(userId);
    }

    @Override
    public Team findById(Long userId, Long teamId) {
        Team team = teamPort.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipo", "id", teamId));
        assertOwnership(userId, team);
        return team;
    }

    @Override
    @Transactional
    public Team update(Long userId, Long teamId, Team changes) {
        Team existing = findById(userId, teamId);
        Team updated = existing.toBuilder()
                .name(changes.getName() != null ? changes.getName() : existing.getName())
                .description(changes.getDescription() != null ? changes.getDescription() : existing.getDescription())
                .pokemons(changes.getPokemons() != null ? changes.getPokemons() : existing.getPokemons())
                .build();
        teamValidator.validate(updated);
        return teamPort.save(updated);
    }

    @Override
    @Transactional
    public void delete(Long userId, Long teamId) {
        findById(userId, teamId); // valida existencia y pertenencia
        teamPort.deleteById(teamId);
    }

    @Override
    public TeamAnalysis analyze(Long userId, Long teamId) {
        Team team = findById(userId, teamId);
        List<Pokemon> members = team.getPokemons();

        int hp = 0, atk = 0, def = 0, spa = 0, spd = 0, spe = 0;
        for (Pokemon p : members) {
            PokemonStats s = p.getStats();
            if (s == null) continue;
            hp += nz(s.getHp()); atk += nz(s.getAttack()); def += nz(s.getDefense());
            spa += nz(s.getSpecialAttack()); spd += nz(s.getSpecialDefense()); spe += nz(s.getSpeed());
        }

        Map<String, Double> weaknesses = new LinkedHashMap<>();
        Map<String, Double> resistances = new LinkedHashMap<>();
        for (String attackType : ALL_TYPES) {
            double totalMultiplier = 0;
            int count = 0;
            for (Pokemon p : members) {
                double m1 = TypeEffectivenessUtil.multiplier(p.getTypePrimary(), attackType);
                double m2 = p.getTypeSecondary() != null
                        ? TypeEffectivenessUtil.multiplier(p.getTypeSecondary(), attackType) : 1.0;
                totalMultiplier += (m1 * m2);
                count++;
            }
            double avg = count > 0 ? totalMultiplier / count : 1.0;
            if (avg > 1.0) weaknesses.put(attackType, avg);
            if (avg < 1.0) resistances.put(attackType, avg);
        }

        List<String> coverage = members.stream()
                .map(Pokemon::getTypePrimary)
                .filter(java.util.Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        List<String> recommendations = new ArrayList<>();
        if (members.size() < 6) {
            recommendations.add("Tu equipo tiene " + members.size() + "/6 Pokemon. Considera completar el equipo.");
        }
        if (weaknesses.size() > coverage.size()) {
            recommendations.add("El equipo tiene mas debilidades que tipos de cobertura ofensiva. Evalua diversificar tipos.");
        }
        if (recommendations.isEmpty()) {
            recommendations.add("El equipo tiene un balance razonable de tipos.");
        }

        return TeamAnalysis.builder()
                .teamId(team.getId())
                .teamName(team.getName())
                .totalHp(hp).totalAttack(atk).totalDefense(def)
                .totalSpecialAttack(spa).totalSpecialDefense(spd).totalSpeed(spe)
                .typeWeaknesses(weaknesses)
                .typeResistances(resistances)
                .typeCoverage(coverage)
                .recommendations(recommendations)
                .build();
    }

    private void assertOwnership(Long userId, Team team) {
        // RN-02: un usuario solo puede acceder a sus propios equipos.
        if (!team.getUserId().equals(userId)) {
            throw new ForbiddenOperationException("No tienes acceso a este equipo");
        }
    }

    private int nz(Integer value) {
        return value == null ? 0 : value;
    }
}
