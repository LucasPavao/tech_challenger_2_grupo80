CREATE TABLE user_address
(
    id         SERIAL PRIMARY KEY,
    street     VARCHAR(255) NOT NULL,
    city       VARCHAR(50)  NOT NULL,
    number     VARCHAR(20)  NOT NULL,
    complement VARCHAR(50),
    state      VARCHAR(255) NOT NULL,
    zip_code   VARCHAR(20)  NOT NULL,
    country    VARCHAR(255) NOT NULL,

    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);