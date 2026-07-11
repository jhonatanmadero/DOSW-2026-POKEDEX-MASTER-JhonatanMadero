package com.pokedex.core.validator;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.pokedex.core.exception.ValidationException;
import com.pokedex.core.model.Pokemon;
import com.pokedex.core.model.Team;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TeamValidatorTest {

    private final TeamValidator validator = new TeamValidator();

    private Pokemon dummy(long id) {
        return Pokemon.builder().id(id).name("Poke" + id).build();
    }

    @Test
    @DisplayName("RN-03: rechaza equipo sin nombre")
    void rejectsBlankName() {
        Team team = Team.builder().name("").pokemons(List.of(dummy(1))).build();
        assertThrows(ValidationException.class, () -> validator.validate(team));
    }

    @Test
    @DisplayName("rechaza nombre muy corto")
    void rejectsShortName() {
        Team team = Team.builder().name("ab").pokemons(List.of(dummy(1))).build();
        assertThrows(ValidationException.class, () -> validator.validate(team));
    }

    @Test
    @DisplayName("rechaza equipo sin Pokemon")
    void rejectsEmptyTeam() {
        Team team = Team.builder().name("Equipo").pokemons(List.of()).build();
        assertThrows(ValidationException.class, () -> validator.validate(team));
    }

    @Test
    @DisplayName("rechaza descripcion mayor a 200 caracteres")
    void rejectsLongDescription() {
        String longDesc = "a".repeat(201);
        Team team = Team.builder().name("Equipo").description(longDesc).pokemons(List.of(dummy(1))).build();
        assertThrows(ValidationException.class, () -> validator.validate(team));
    }

    @Test
    @DisplayName("acepta un equipo valido")
    void acceptsValidTeam() {
        Team team = Team.builder().name("Iniciales").description("Mi equipo")
                .pokemons(List.of(dummy(1), dummy(2))).build();
        assertThatCode(() -> validator.validate(team)).doesNotThrowAnyException();
    }
}
