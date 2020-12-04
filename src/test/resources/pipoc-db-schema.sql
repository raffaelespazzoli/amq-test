drop table if exists accounts;

create table accounts
(
	username varchar(50) primary key,
	first_name varchar(50),
	last_name varchar(50),
	age integer
);

