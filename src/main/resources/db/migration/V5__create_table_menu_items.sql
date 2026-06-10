CREATE TABLE menu_items
(
    id                         SERIAL PRIMARY KEY,
    restaurant_id              INTEGER        NOT NULL,
    name                       VARCHAR(255)   NOT NULL,
    description                TEXT           NOT NULL,
    price                      DECIMAL(10, 2) NOT NULL,
    available_only_at_location BOOLEAN        NOT NULL DEFAULT FALSE,
    image_path                 VARCHAR(500),

    created_at                 TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                 TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_menu_item_restaurant
        FOREIGN KEY (restaurant_id)
            REFERENCES restaurants (id)
            ON DELETE CASCADE
);
-- Index to optimize menu listing per restaurant
CREATE INDEX idx_menu_items_restaurant ON menu_items (restaurant_id);