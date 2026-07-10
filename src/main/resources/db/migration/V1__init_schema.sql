-- ============================================================
-- V1: Esquema inicial - Pokedex Master (DOSW 2026 Intersemestral)
-- ============================================================

CREATE TABLE region (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,
    sort_order  INTEGER
);

CREATE TABLE ability (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500),
    is_hidden   BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE pokemon (
    id                BIGSERIAL PRIMARY KEY,
    national_number   INTEGER NOT NULL UNIQUE,
    name              VARCHAR(100) NOT NULL,
    description       VARCHAR(1000),
    image_url         VARCHAR(300),
    type_primary      VARCHAR(30) NOT NULL,
    type_secondary    VARCHAR(30),
    region_id         BIGINT NOT NULL REFERENCES region(id),
    generation        INTEGER NOT NULL,
    height_meters     DOUBLE PRECISION,
    weight_kg         DOUBLE PRECISION,
    has_mega          BOOLEAN NOT NULL DEFAULT FALSE,
    evolution_stage   VARCHAR(30),
    evolves_from_id   BIGINT,
    evolves_to_id     BIGINT,
    evolution_level   INTEGER,
    evolution_method  VARCHAR(100),
    created_at        TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_pokemon_number ON pokemon(national_number);
CREATE INDEX idx_pokemon_name ON pokemon(name);
CREATE INDEX idx_pokemon_type_primary ON pokemon(type_primary);
CREATE INDEX idx_pokemon_generation ON pokemon(generation);

CREATE TABLE pokemon_stats (
    id                BIGSERIAL PRIMARY KEY,
    pokemon_id        BIGINT NOT NULL UNIQUE REFERENCES pokemon(id) ON DELETE CASCADE,
    hp                INTEGER NOT NULL,
    attack            INTEGER NOT NULL,
    defense           INTEGER NOT NULL,
    special_attack    INTEGER NOT NULL,
    special_defense   INTEGER NOT NULL,
    speed             INTEGER NOT NULL
);

CREATE TABLE pokemon_ability (
    id          BIGSERIAL PRIMARY KEY,
    pokemon_id  BIGINT NOT NULL REFERENCES pokemon(id) ON DELETE CASCADE,
    ability_id  BIGINT NOT NULL REFERENCES ability(id)
);

CREATE TABLE app_user (
    id              BIGSERIAL PRIMARY KEY,
    full_name       VARCHAR(100) NOT NULL,
    email           VARCHAR(150) NOT NULL UNIQUE,
    password_hash   VARCHAR(200),
    birth_date      DATE,
    favorite_region VARCHAR(50),
    role            VARCHAR(20) NOT NULL DEFAULT 'TRAINER',
    active          BOOLEAN NOT NULL DEFAULT TRUE,
    from_google     BOOLEAN NOT NULL DEFAULT FALSE,
    avatar_url      VARCHAR(300),
    created_at      TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_user_email ON app_user(email);

CREATE TABLE team (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    name        VARCHAR(30) NOT NULL,
    description VARCHAR(200),
    created_at  TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE team_pokemon (
    id          BIGSERIAL PRIMARY KEY,
    team_id     BIGINT NOT NULL REFERENCES team(id) ON DELETE CASCADE,
    pokemon_id  BIGINT NOT NULL REFERENCES pokemon(id),
    slot_order  INTEGER NOT NULL,
    CONSTRAINT chk_team_slot_order CHECK (slot_order BETWEEN 1 AND 6)
);

CREATE TABLE favorite (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    pokemon_id  BIGINT NOT NULL REFERENCES pokemon(id),
    added_at    TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT uq_favorite_user_pokemon UNIQUE (user_id, pokemon_id)
);
