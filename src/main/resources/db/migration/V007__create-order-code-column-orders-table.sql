alter table orders add order_code varchar(36) not null after id;
update orders set order_code = uuid() where id > 0;
alter table orders add constraint uk_orders_order_code unique (order_code);