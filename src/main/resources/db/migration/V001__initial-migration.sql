-- V1__create_schema.sql
-- MySQL 8+, English table/column names
-- Uses utf8mb4 for proper accents if needed

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- Kitchens
CREATE TABLE IF NOT EXISTS kitchen
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_kitchen_name (name)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- States
CREATE TABLE IF NOT EXISTS state
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_state_name (name)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Cities
CREATE TABLE IF NOT EXISTS city
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    name     VARCHAR(150) NOT NULL,
    state_id BIGINT       NOT NULL,
    PRIMARY KEY (id),
    KEY idx_city_state (state_id),
    CONSTRAINT fk_city_state
        FOREIGN KEY (state_id) REFERENCES state (id)
            ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Restaurants
CREATE TABLE IF NOT EXISTS restaurant
(
    id                BIGINT         NOT NULL AUTO_INCREMENT,
    name              VARCHAR(200)   NOT NULL,
    delivery_fee      DECIMAL(10, 2) NOT NULL,
    kitchen_id        BIGINT         NOT NULL,
    registered_date   DATETIME       NOT NULL,
    last_updated_date DATETIME       NOT NULL,
    address_city_id   BIGINT         NULL,
    address_postcode  VARCHAR(20),
    address_street    VARCHAR(255),
    address_number    VARCHAR(20),
    address_district  VARCHAR(100),
    PRIMARY KEY (id),
    KEY idx_restaurant_kitchen (kitchen_id),
    KEY idx_restaurant_city (address_city_id),
    CONSTRAINT fk_restaurant_kitchen
        FOREIGN KEY (kitchen_id) REFERENCES kitchen (id)
            ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_restaurant_city
        FOREIGN KEY (address_city_id) REFERENCES city (id)
            ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Payment Methods
CREATE TABLE IF NOT EXISTS payment_method
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    description VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_payment_method_description (description)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Restaurant ↔ Payment Method mapping
CREATE TABLE IF NOT EXISTS restaurant_payment_method
(
    restaurant_id     BIGINT NOT NULL,
    payment_method_id BIGINT NOT NULL,
    PRIMARY KEY (restaurant_id, payment_method_id),
    KEY idx_rpm_payment_method (payment_method_id),
    CONSTRAINT fk_rpm_restaurant
        FOREIGN KEY (restaurant_id) REFERENCES restaurant (id)
            ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_rpm_payment_method
        FOREIGN KEY (payment_method_id) REFERENCES payment_method (id)
            ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Products
CREATE TABLE IF NOT EXISTS product
(
    id            BIGINT         NOT NULL AUTO_INCREMENT,
    name          VARCHAR(255)   NOT NULL,
    description   TEXT           NULL,
    price         DECIMAL(10, 2) NOT NULL,
    active        TINYINT(1)     NOT NULL,
    restaurant_id BIGINT         NOT NULL,
    PRIMARY KEY (id),
    KEY idx_product_restaurant (restaurant_id),
    UNIQUE KEY uk_product_name_per_restaurant (restaurant_id, name),
    CONSTRAINT fk_product_restaurant
        FOREIGN KEY (restaurant_id) REFERENCES restaurant (id)
            ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Users
CREATE TABLE IF NOT EXISTS users
(
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    email           VARCHAR(255) NOT NULL,
    name            VARCHAR(255) NOT NULL,
    password        VARCHAR(255) NOT NULL,
    registered_date DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_users_email (email)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Groups
CREATE TABLE IF NOT EXISTS user_group
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_group_name (name)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Permissions
CREATE TABLE IF NOT EXISTS permission
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(255) NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_permission_name (name)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- User ↔ Group mapping
CREATE TABLE IF NOT EXISTS user_user_groups
(
    user_id       BIGINT NOT NULL,
    user_group_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, user_group_id),
    KEY idx_uug_group (user_group_id),
    CONSTRAINT fk_uug_user
        FOREIGN KEY (user_id) REFERENCES users (id)
            ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_uug_group
        FOREIGN KEY (user_group_id) REFERENCES user_group (id)
            ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Group ↔ Permission mapping
CREATE TABLE IF NOT EXISTS user_group_permissions
(
    user_group_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (user_group_id, permission_id),
    KEY idx_ugp_permission (permission_id),
    CONSTRAINT fk_ugp_group
        FOREIGN KEY (user_group_id) REFERENCES user_group (id)
            ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_ugp_permission
        FOREIGN KEY (permission_id) REFERENCES permission (id)
            ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

SET FOREIGN_KEY_CHECKS = 1;
