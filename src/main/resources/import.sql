INSERT INTO kitchen (name) VALUES ( 'Thai');
INSERT INTO kitchen (name) VALUES ( 'Japanese' );
INSERT INTO restaurant (name, delivery_rate, kitchen_id ) VALUES ('The International', 20.30, 1);
INSERT INTO restaurant (name, delivery_rate, kitchen_id) VALUES ('The Deliver', 12.30, 1);
INSERT INTO restaurant (name, delivery_rate, kitchen_id) VALUES ('The TukTuk', 110.30, 2);

insert into  geographical_state (id, name)values (1, 'Minas Gerais');
insert into geographical_state  (id, name)values (2, 'São Paulo');
insert into geographical_state  (id, name) values (3, 'Ceará');

insert into city (id, name, state_id)values (1, 'Uberlândia', 1);
insert into city (id, name, state_id) values (2, 'Belo Horizonte', 1);
insert into city (id, name, state_id) values (3, 'São Paulo', 2);
insert into city (id, name, state_id) values (4, 'Campinas', 2);
insert into city (id, name, state_id) values (5, 'Fortaleza', 3);

insert into payment_type (id, description) values (1, 'Cartão de crédito');
insert into payment_type (id, description) values (2, 'Cartão de débito');
insert into payment_type (id, description) values (3, 'Dinheiro');

insert into permission (id, name, description) values (1, 'CONSULTAR_COZINHAS', 'Permite consultar cozinhas');
insert into permission (id, name, description) values (2, 'EDITAR_COZINHAS', 'Permite editar cozinhas');