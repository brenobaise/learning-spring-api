alter table restaurant add is_active tinyint(1) not null;
update restaurant set is_active = true;