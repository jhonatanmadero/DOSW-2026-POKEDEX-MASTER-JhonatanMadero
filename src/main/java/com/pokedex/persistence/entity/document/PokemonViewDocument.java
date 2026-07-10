package com.pokedex.persistence.entity.document;

import java.time.LocalDateTime;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/** RF-19: registra cuantas veces se consulta cada Pokemon (popularidad). */
@Document(collection = "pokemon_views")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PokemonViewDocument {
    @Id
    private String id;

    @Field("pokemon_id")
    private Long pokemonId;

    @Field("pokemon_name")
    private String pokemonName;

    @Field("view_count")
    @Builder.Default
    private Long viewCount = 0L;

    @Field("last_viewed")
    private LocalDateTime lastViewed;
}
