create data;

create table stuff
(stuff_id varchar(40) not null primary key
,stuff_name varchar(100)
,stuff_count integer
);

create index stuff_name on stuff (stuff_name);