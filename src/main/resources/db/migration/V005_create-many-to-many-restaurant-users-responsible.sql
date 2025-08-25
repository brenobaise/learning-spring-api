CREATE TABLE restaurant_user_responsible
(
    restaurant_id BIGINT NOT NULL,
    users_id      BIGINT NOT NULL,

    PRIMARY KEY (restaurant_id, users_id),

    CONSTRAINT fk_rur_restaurant
        FOREIGN KEY (restaurant_id)
            REFERENCES restaurant (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_rur_users
        FOREIGN KEY (users_id)
            REFERENCES users (id)
            ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
