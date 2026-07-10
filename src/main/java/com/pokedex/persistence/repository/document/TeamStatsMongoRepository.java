package com.pokedex.persistence.repository.document;

import com.pokedex.persistence.entity.document.TeamStatsDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamStatsMongoRepository extends MongoRepository<TeamStatsDocument, String> {
}
