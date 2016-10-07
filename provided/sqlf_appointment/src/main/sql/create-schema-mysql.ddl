create database tpmg;

create user 'tpmg'@'%' identified by 'tpmg';
create user 'tpmg'@'localhost' identified by 'tpmg';
create user 'tpmg'@'server1.dev' identified by 'tpmg';

grant all privileges on *.* to 'tpmg'@'%';
grant all privileges on *.* to 'tpmg'@'localhost';
grant all privileges on *.* to 'tpmg'@'server1.dev';
flush privileges;

use tpmg;

create table stuff
(stuff_id varchar(40) not null primary key
,stuff_name varchar(100)
,stuff_count integer
);

create index stuff_name on stuff (stuff_name);

insert into stuff (stuff_id,stuff_name,stuff_count) values ('1','One',1);
insert into stuff (stuff_id,stuff_name,stuff_count) values ('2','Two',2);
insert into stuff (stuff_id,stuff_name,stuff_count) values ('3','Three',3);
insert into stuff (stuff_id,stuff_name,stuff_count) values ('4','Four',4);
insert into stuff (stuff_id,stuff_name,stuff_count) values ('5','Five',5);
insert into stuff (stuff_id,stuff_name,stuff_count) values ('6','Six',6);
insert into stuff (stuff_id,stuff_name,stuff_count) values ('7','Seven',7);
insert into stuff (stuff_id,stuff_name,stuff_count) values ('8','Eight',8);
insert into stuff (stuff_id,stuff_name,stuff_count) values ('9','Nine',9);
