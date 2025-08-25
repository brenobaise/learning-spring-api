alter table restaurant add is_open tinyint(1) not null;
update restaurant set is_open = true;