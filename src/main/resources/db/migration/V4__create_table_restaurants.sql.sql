CREATE TABLE restaurants
(
    id              SERIAL PRIMARY KEY,

    name            VARCHAR(255) NOT NULL,
    address         VARCHAR(255) NOT NULL,
    cuisine_type    VARCHAR(100) NOT NULL,
    operating_hours VARCHAR(150) NOT NULL,
    owner_id        INTEGER      NOT NULL,

    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_restaurant_owner
        FOREIGN KEY (owner_id)
            REFERENCES users (id)
            ON DELETE RESTRICT
);

-- Index to optimize restaurant queries by owner ID
CREATE INDEX idx_restaurants_owner ON restaurants (owner_id);