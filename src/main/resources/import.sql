-- Kitchens
INSERT INTO kitchen (id, name) VALUES (1, 'Tailandesa');
INSERT INTO kitchen (id, name) VALUES (2, 'Indiana');
INSERT INTO kitchen (id, name) VALUES (3, 'Brasileira');
INSERT INTO kitchen (id, name) VALUES (4, 'Portuguesa');

-- States
INSERT INTO geographical_state (id, name) VALUES (1, 'Minas Gerais');
INSERT INTO geographical_state (id, name) VALUES (2, 'São Paulo');
INSERT INTO geographical_state (id, name) VALUES (3, 'Ceará');

-- Cities
INSERT INTO city (id, name, state_id) VALUES (1, 'Uberlândia', 1);
INSERT INTO city (id, name, state_id) VALUES (2, 'Belo Horizonte', 1);
INSERT INTO city (id, name, state_id) VALUES (3, 'São Paulo', 2);
INSERT INTO city (id, name, state_id) VALUES (4, 'Campinas', 2);
INSERT INTO city (id, name, state_id) VALUES (5, 'Fortaleza', 3);

-- Restaurants
INSERT INTO restaurant (id, name, delivery_rate, kitchen_id, registered_date, last_updated_date, address_city_id, address_postcode, address_street, address_number, address_county) VALUES (1, 'Thai Gourmet', 10, 1, utc_timestamp, utc_timestamp, 1, '38400-999', 'Rua João Pinheiro', '1000', 'Centro');
INSERT INTO restaurant (id, name, delivery_rate, kitchen_id, registered_date, last_updated_date) VALUES (2, 'Thai Delivery', 9.50, 1, utc_timestamp, utc_timestamp);
INSERT INTO restaurant (id, name, delivery_rate, kitchen_id, registered_date, last_updated_date) VALUES (3, 'Tuk Tuk Comida Indiana', 15, 2, utc_timestamp, utc_timestamp);
INSERT INTO restaurant (id, name, delivery_rate, kitchen_id, registered_date, last_updated_date) VALUES (4, 'Java Steakhouse', 12, 3, utc_timestamp, utc_timestamp);
INSERT INTO restaurant (id, name, delivery_rate, kitchen_id, registered_date, last_updated_date) VALUES (5, 'LanchunBox', 30, 4, utc_timestamp, utc_timestamp);
INSERT INTO restaurant (id, name, delivery_rate, kitchen_id, registered_date, last_updated_date) VALUES (6, 'MariaDBAR', 5, 4, utc_timestamp, utc_timestamp);

-- Payment types
INSERT INTO payment_type (id, description) VALUES (1, 'Cartão de crédito');
INSERT INTO payment_type (id, description) VALUES (2, 'Cartão de débito');
INSERT INTO payment_type (id, description) VALUES (3, 'Dinheiro');

-- Permissions
INSERT INTO permission (id, name, description) VALUES (1, 'CONSULTAR_COZINHAS', 'Permite consultar cozinhas');
INSERT INTO permission (id, name, description) VALUES (2, 'EDITAR_COZINHAS', 'Permite editar cozinhas');

-- Restaurant ↔ Payment type mapping
INSERT INTO restaurant_payment_types (restaurant_id, payment_types_id) VALUES (1, 1), (1, 2), (1, 3), (2, 3), (3, 2), (3, 3);
insert into restaurant_payment_types (restaurant_id, payment_types_id) values (1, 1), (1, 2), (1, 3), (2, 3), (3, 2), (3, 3), (4, 1), (4, 2), (5, 1), (5, 2), (6, 3);
-- Products
INSERT INTO product (name, description, price, active, restaurant_id) VALUES ('Sweet and Sour Pork', 'Delicious pork meat in a special sauce', 78.90, 1, 1);
INSERT INTO product (name, description, price, active, restaurant_id) VALUES ('Thai Shrimp', '16 large shrimps in a spicy sauce', 110, 1, 1);
INSERT INTO product (name, description, price, active, restaurant_id) VALUES ('Spicy Salad with Grilled Beef', 'Leaf salad with thin slices of grilled beef and our special red chili sauce', 87.20, 1, 2);
INSERT INTO product (name, description, price, active, restaurant_id) VALUES ('Garlic Naan', 'Traditional Indian bread topped with garlic', 21, 1, 3);
INSERT INTO product (name, description, price, active, restaurant_id) VALUES ('Murg Curry', 'Chicken cubes prepared with curry sauce and spices', 43, 1, 3);
INSERT INTO product (name, description, price, active, restaurant_id) VALUES ('Bife Ancho', 'Tender and juicy cut, two fingers thick, taken from the front part of the ribeye', 79, 1, 4);
INSERT INTO product (name, description, price, active, restaurant_id) VALUES ('T-Bone', 'Very flavorful cut with a T-shaped bone, with ribeye on one side and tenderloin on the other', 89, 1, 4);
INSERT INTO product (name, description, price, active, restaurant_id) VALUES ('X-Tudo Sandwich', 'Big sandwich with lots of cheese, beef hamburger, bacon, egg, salad, and mayonnaise', 19, 1, 5);
INSERT INTO product (name, description, price, active, restaurant_id) VALUES ('Beef hump skewer', 'Served with flour, cassava, and vinaigrette', 8, 1, 6);
