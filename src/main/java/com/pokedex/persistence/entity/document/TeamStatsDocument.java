package com.pokedex.persistence.entity.document;

import java.time.LocalDateTime;
import java.util.List;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/** RF-19: snapshot de composicion de un equipo, usado para calcular tasas de eleccion agregadas. */
@Document(collection = "team_stats")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamStatsDocument {
    @Id
    private String id;

    @Field("team_id")
    private Long teamId;

    @Field("pokemon_ids")
    private List<Long> pokemonIds;

    @Field("recorded_at")
    private LocalDateTime recordedAt;
}
