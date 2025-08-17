create table orders(
                       id bigint not null auto_increment,
                       subtotal decimal(10,2) not null,
                       delivery_fee decimal(10,2) not null,
                       total decimal(10,2) not null,

                       restaurant_id bigint not null,
                       user_customer_id bigint not null,
                       payment_method_id bigint not null,

                       address_city_id bigint not null,
                       address_postcode varchar(9) not null,
                       address_district varchar(100) not null,
                       address_number varchar(20) not null,
                       address_street varchar(100) not null,

                       status varchar(10) not null,
                       created_date datetime not null DEFAULT CURRENT_TIMESTAMP,
                       confirmed_date datetime null,
                       cancelled_date datetime null,
                       delivered_date datetime null,

                       primary key (id),

                       constraint fk_order_address_city foreign key (address_city_id) references city (id),
                       constraint fk_order_restaurant foreign key (restaurant_id) references restaurant (id),
                       constraint fk_order_user_customer foreign key (user_customer_id) references users (id),
                       constraint fk_order_payment_method foreign key (payment_method_id) references payment_method (id)
)engine=InnoDB default charset=utf8;

create table order_item(
                           id bigint not null,
                           quantity smallint(6) not null,
                           unit_price decimal(10,2) not null,
                           total decimal(10,2) not null,
                           notes varchar(255) null,
                           order_id bigint not null,
                           product_id bigint not null,

                           primary key (id),
                           unique key uk_order_item_product (order_id, product_id),

                           constraint fk_order_item_order foreign key (order_id) references orders (id),
                           constraint fk_order_item_product foreign key (product_id) references product (id)
)engine=InnoDB default charset=utf8;
