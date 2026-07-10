package com.pokedex.controller.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

/** RF-16: creacion de equipo. */
public record TeamRequest(
        @NotBlank @Size(min = 3, max = 30) String name,
        @Size(max = 200) String description,
        @NotEmpty(message = "Debes seleccionar al menos un Pokemon")
        @Size(max = 6, message = "Un equipo Pokemon no puede superar 6 integrantes")
        List<Long> pokemonIds
) {}
