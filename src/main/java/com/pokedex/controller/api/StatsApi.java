package com.pokedex.controller.api;

import com.pokedex.controller.dto.response.PopularityStatResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** RF-19: estadisticas de uso y popularidad. */
@Tag(name = "Stats", description = "Estadisticas de popularidad y uso")
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/v1/stats")
public interface StatsApi {

    @Operation(summary = "Ranking de popularidad (consultas)")
    @GetMapping("/popularity")
    ResponseEntity<List<PopularityStatResponse>> popularity(@RequestParam(defaultValue = "10") int top);

    @Operation(summary = "Tasa de eleccion en equipos")
    @GetMapping("/pick-rate")
    ResponseEntity<List<PopularityStatResponse>> pickRate(@RequestParam(defaultValue = "10") int top);

    @Operation(summary = "Estadisticas generales")
    @GetMapping("/general")
    ResponseEntity<Map<String, Object>> general();
}
