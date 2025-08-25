SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM city;
DELETE FROM kitchen;
DELETE FROM state;
DELETE FROM payment_method;
DELETE FROM user_group;
DELETE FROM user_group_permissions;
DELETE FROM permission;
DELETE FROM product;
DELETE FROM restaurant;
DELETE FROM restaurant_payment_method;
DELETE FROM users;
DELETE FROM user_user_groups;
DELETE FROM restaurant_user_responsible;
delete from orders;
delete from order_item;

SET FOREIGN_KEY_CHECKS = 1;

ALTER TABLE city AUTO_INCREMENT = 1;
ALTER TABLE kitchen AUTO_INCREMENT = 1;
ALTER TABLE state AUTO_INCREMENT = 1;
ALTER TABLE payment_method AUTO_INCREMENT = 1;
ALTER TABLE user_group AUTO_INCREMENT = 1;
ALTER TABLE permission AUTO_INCREMENT = 1;
ALTER TABLE product AUTO_INCREMENT = 1;
ALTER TABLE restaurant AUTO_INCREMENT = 1;
ALTER TABLE users AUTO_INCREMENT = 1;

-- Kitchens
INSERT INTO kitchen (id, name) VALUES (1, 'Thai');
INSERT INTO kitchen (id, name) VALUES (2, 'Indian');
INSERT INTO kitchen (id, name) VALUES (3, 'Argentinian');
INSERT INTO kitchen (id, name) VALUES (4, 'Brazilian');

-- States
INSERT INTO state (id, name) VALUES (1, 'Minas Gerais');
INSERT INTO state (id, name) VALUES (2, 'São Paulo');
INSERT INTO state (id, name) VALUES (3, 'Ceará');

-- Cities
INSERT INTO city (id, name, state_id) VALUES (1, 'Uberlândia', 1);
INSERT INTO city (id, name, state_id) VALUES (2, 'Belo Horizonte', 1);
INSERT INTO city (id, name, state_id) VALUES (3, 'São Paulo', 2);
INSERT INTO city (id, name, state_id) VALUES (4, 'Campinas', 2);
INSERT INTO city (id, name, state_id) VALUES (5, 'Fortaleza', 3);

-- Restaurants
INSERT INTO restaurant (id, name, delivery_fee, kitchen_id, registered_date, last_updated_date, address_city_id, address_postcode, address_street, address_number, address_district, is_active, is_open)
VALUES (1, 'Thai Gourmet', 10, 1, UTC_TIMESTAMP, UTC_TIMESTAMP, 1, '38400-999', 'João Pinheiro Street', '1000', 'Downtown', true, true);

INSERT INTO restaurant (id, name, delivery_fee, kitchen_id, registered_date, last_updated_date, is_active, is_open)
VALUES (2, 'Thai Delivery', 9.50, 1, UTC_TIMESTAMP, UTC_TIMESTAMP, true, true);

INSERT INTO restaurant (id, name, delivery_fee, kitchen_id, registered_date, last_updated_date, is_active, is_open)
VALUES (3, 'Tuk Tuk Indian Food', 15, 2, UTC_TIMESTAMP, UTC_TIMESTAMP, true, true);

INSERT INTO restaurant (id, name, delivery_fee, kitchen_id, registered_date, last_updated_date, is_active, is_open)
VALUES (4, 'Java Steakhouse', 12, 3, UTC_TIMESTAMP, UTC_TIMESTAMP, true, true);

INSERT INTO restaurant (id, name, delivery_fee, kitchen_id, registered_date, last_updated_date, is_active, is_open)
VALUES (5, 'Uncle Sam Snack Bar', 11, 4, UTC_TIMESTAMP, UTC_TIMESTAMP, true, true);

INSERT INTO restaurant (id, name, delivery_fee, kitchen_id, registered_date, last_updated_date, is_active, is_open)
VALUES (6, 'Maria’s Bar', 6, 4, UTC_TIMESTAMP, UTC_TIMESTAMP, true, true);

-- Payment Methods
INSERT INTO payment_method (id, description) VALUES (1, 'Credit card');
INSERT INTO payment_method (id, description) VALUES (2, 'Debit card');
INSERT INTO payment_method (id, description) VALUES (3, 'Cash');

-- Permissions
INSERT INTO permission (id, name, description) VALUES (1, 'CONSULT_KITCHENS', 'Allows consulting kitchens');
INSERT INTO permission (id, name, description) VALUES (2, 'EDIT_KITCHENS', 'Allows editing kitchens');

-- Restaurant ↔ Payment method
INSERT INTO restaurant_payment_method (restaurant_id, payment_method_id) VALUES
                                                                             (1, 1), (1, 2), (1, 3),
                                                                             (2, 3),
                                                                             (3, 2), (3, 3),
                                                                             (4, 1), (4, 2),
                                                                             (5, 1), (5, 2),
                                                                             (6, 3);

-- Products
INSERT INTO product (name, description, price, active, restaurant_id)
VALUES ('Sweet and Sour Pork', 'Delicious pork meat in a special sauce', 78.90, 1, 1);

INSERT INTO product (name, description, price, active, restaurant_id)
VALUES ('Thai Shrimp', '16 large shrimps in a spicy sauce', 110, 1, 1);

INSERT INTO product (name, description, price, active, restaurant_id)
VALUES ('Spicy Salad with Grilled Beef', 'Leaf salad with thin slices of grilled beef and our special red chili sauce', 87.20, 1, 2);

INSERT INTO product (name, description, price, active, restaurant_id)
VALUES ('Garlic Naan', 'Traditional Indian bread topped with garlic', 21, 1, 3);

INSERT INTO product (name, description, price, active, restaurant_id)
VALUES ('Murg Curry', 'Chicken cubes prepared with curry sauce and spices', 43, 1, 3);

INSERT INTO product (name, description, price, active, restaurant_id)
VALUES ('Ribeye (Bife Ancho)', 'Tender and juicy cut, two fingers thick, taken from the front part of the ribeye', 79, 1, 4);

INSERT INTO product (name, description, price, active, restaurant_id)
VALUES ('T-Bone', 'Very flavorful cut with a T-shaped bone, with ribeye on one side and tenderloin on the other', 89, 1, 4);

INSERT INTO product (name, description, price, active, restaurant_id)
VALUES ('X-Tudo Sandwich', 'Big sandwich with lots of cheese, beef hamburger, bacon, egg, salad, and mayonnaise', 19, 1, 5);

INSERT INTO product (name, description, price, active, restaurant_id)
VALUES ('Beef Hump Skewer', 'Served with flour, cassava, and vinaigrette', 8, 1, 6);

insert into user_group (name) values ('Gerente'), ('Vendedor'), ('Secretária'), ('Cadastrador');

insert into users (id, name, email, password, registered_date) values
                                                                (1, 'João da Silva', 'joao.ger@algafood.com', '123', utc_timestamp),
                                                                (2, 'Maria Joaquina', 'maria.vnd@algafood.com', '123', utc_timestamp),
                                                                (3, 'José Souza', 'jose.aux@algafood.com', '123', utc_timestamp),
                                                                (4, 'Sebastião Martins', 'sebastiao.cad@algafood.com', '123', utc_timestamp);


insert into user_group_permissions (user_group_id, permission_id) values (1, 1), (1, 2), (2, 1), (2, 2), (3, 1);

insert into user_user_groups (user_id, user_group_id) values (1, 1), (1, 2), (2, 2);

insert into users (id, name, email, password, registered_date) values
    (5, 'Manoel Lima', 'manoel.loja@gmail.com', '123', utc_timestamp);

insert into restaurant_user_responsible (restaurant_id, users_id) values (1, 5), (3, 5);


insert into orders (id, restaurant_id,user_customer_id, payment_method_id, address_city_id, address_postcode,
                    address_street, address_number, address_district,
                    status, created_date, subtotal, delivery_fee, total)
values (1, 1, 1, 1, 1, '38400-000', 'Rua Floriano Peixoto', '500', 'Apto 801',
        'CREATED', utc_timestamp, 298.90, 10, 308.90);

insert into order_item (id, order_id, product_id, quantity, unit_price, total, notes)
values (1, 1, 1, 1, 78.9, 78.9, null);

insert into order_item (id, order_id, product_id, quantity, unit_price, total, notes)
values (2, 1, 2, 2, 110, 220, 'Menos picante, por favor');


insert into orders (id, restaurant_id,user_customer_id, payment_method_id, address_city_id, address_postcode,
                    address_street, address_number, address_district,
                    status, created_date, subtotal, delivery_fee, total)
values (2, 4, 1, 2, 1, '38400-111', 'Rua Acre', '300', 'Casa 2',
        'CREATED', utc_timestamp, 79, 0, 79);

insert into order_item (id, order_id, product_id, quantity, unit_price, total, notes)
values (3, 2, 6, 1, 79, 79, 'Ao ponto');