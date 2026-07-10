package com.pokedex.core.service.interfaces;

import com.pokedex.core.model.Team;
import com.pokedex.core.model.TeamAnalysis;
import java.util.List;

public interface TeamService {
    Team create(Long userId, Team team);                 // RF-16
    List<Team> findAllByUser(Long userId);                // RF-18
    Team findById(Long userId, Long teamId);              // RF-18
    Team update(Long userId, Long teamId, Team changes);  // RF-18
    void delete(Long userId, Long teamId);                // RF-18
    TeamAnalysis analyze(Long userId, Long teamId);       // RF-17
}
