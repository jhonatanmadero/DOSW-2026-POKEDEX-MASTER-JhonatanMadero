package com.pokedex.core.validator;

import com.pokedex.core.exception.ValidationException;
import com.pokedex.core.model.Team;
import org.springframework.stereotype.Component;

/**
 * Valida las reglas de negocio de un equipo Pokemon.
 * RN-03: un equipo Pokemon no puede superar 6 integrantes.
 */
@Component
public class TeamValidator {

    private static final int MAX_TEAM_SIZE = 6;

    public void validate(Team team) {
        if (team.getName() == null || team.getName().isBlank()) {
            throw new ValidationException("El nombre del equipo es obligatorio");
        }
        if (team.getName().length() < 3 || team.getName().length() > 30) {
            throw new ValidationException("El nombre del equipo debe tener entre 3 y 30 caracteres");
        }
        if (team.getPokemons() == null || team.getPokemons().isEmpty()) {
            throw new ValidationException("El equipo debe tener al menos un Pokemon");
        }
        if (team.getPokemons().size() > MAX_TEAM_SIZE) {
            throw new ValidationException("Un equipo Pokemon no puede superar " + MAX_TEAM_SIZE + " integrantes");
        }
        if (team.getDescription() != null && team.getDescription().length() > 200) {
            throw new ValidationException("La descripcion no puede exceder 200 caracteres");
        }
    }
}
