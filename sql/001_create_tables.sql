create table users(id bigserial primary key, name varchar(500), date_of_birth date, password varchar(500));

create table account(id bigserial primary key, user_id bigint unique, balance decimal(20, 2));
alter table account add constraint fk_user foreign key(user_id) references users(id);

create table email_data(id bigserial primary key, user_id bigint, email varchar(200) unique);
alter table email_data add constraint fk_user foreign key(user_id) references users(id);

create table phone_data(id bigserial primary key, user_id bigint, phone varchar(13) unique);
alter table phone_data add constraint fk_user foreign key(user_id) references users(id);
