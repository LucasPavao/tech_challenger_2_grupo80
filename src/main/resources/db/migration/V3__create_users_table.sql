CREATE TABLE users
(
    id              SERIAL PRIMARY KEY,

    name            VARCHAR(255) NOT NULL,
    email           VARCHAR(255) NOT NULL UNIQUE,
    login           VARCHAR(255) NOT NULL UNIQUE,
    password        VARCHAR(255) NOT NULL,

    user_type_id    INTEGER      NOT NULL,
    user_address_id INTEGER      NOT NULL UNIQUE,

    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_user_type
        FOREIGN KEY (user_type_id)
            REFERENCES user_types (id),

    CONSTRAINT fk_user_address
        FOREIGN KEY (user_address_id)
            REFERENCES user_address (id)
);