--read in at SQL Plus as
--connect name/password (substitute yours);
--start filename.txt;
-- DROP TABLE and CREATE TABLE commands
drop table if exists  authors CASCADE;
create table authors
 (au_id char(11) UNIQUE not null,
 au_lname varchar(40) not null,
 au_fname varchar(20) not null,
 phone char(12) null,
 address varchar(40) null,
 city varchar(20) null,
 state char(2) null,
 zip char(5) null,
 PRIMARY KEY (au_id));
grant select on authors to public;

drop table if exists  publishers;
create table publishers
  (pub_id char(4) UNIQUE not null,
  pub_name varchar(40) null,
  address varchar(40) null,
  city varchar(20) null,
  state char(2) null,
  PRIMARY KEY (pub_id));
grant select on publishers to public;

drop table if exists  titles CASCADE;
create table titles
(title_id char(6) UNIQUE not null,
 title varchar(80) not null,
 type char(12) null,
 pub_id char(4) null,
 price numeric(9,2) null,
 advance numeric(9,2) null,
 ytd_sales int null,
 contract char(1) not null,
 notes varchar(200) null,
 pubdate date null,
 PRIMARY KEY (title_id));
grant select on titles to public;

drop table if exists  roysched CASCADE ;
create table roysched
 (title_id char(6) not null REFERENCES titles(title_id) on DELETE CASCADE,
 lorange numeric null,
 hirange numeric null,
 royalty numeric(5,2) null,
  PRIMARY KEY (title_id, lorange, hirange));
grant select on roysched to public;

drop table if exists  titleauthors CASCADE;
create table titleauthors
 (au_id char(11) not null REFERENCES authors(au_id) on DELETE CASCADE,
 title_id char(6) not null REFERENCES titles(title_id) on DELETE CASCADE,
 au_ord numeric not null,
 royaltyshare numeric (5,2) null,
 PRIMARY KEY (au_id, title_id));
 grant select on titleauthors to public;

drop table if exists  sales CASCADE;
create table sales
  (sonum numeric UNIQUE not null,
  stor_id char(4) not null,
  ponum varchar(20) not null,
  sdate date null,
  PRIMARY KEY (sonum));
grant select on sales to public;

drop table if exists  editors CASCADE;
create table editors
 (ed_id char(11) UNIQUE not null,
 ed_lname varchar(40) not null,
 ed_fname varchar(20) not null,
 ed_pos varchar(12) null,
 phone char(12) null,
 address varchar(40) null,
 city varchar(20) null,
 state char(2) null,
 zip char(5) null,
 ed_boss char(11) null,
 PRIMARY KEY (ed_id));
grant select on editors to public
;
drop table if exists  titleditors;
create table titleditors
 (ed_id char(11) not null REFERENCES editors(ed_id) on DELETE CASCADE,
 title_id char(6) not null REFERENCES titles(title_id) on DELETE CASCADE,
 ed_ord numeric null,
 PRIMARY KEY (ed_id, title_id));
grant select on titleditors to public
;
drop table if exists  salesdetails CASCADE;
create table salesdetails
  (sonum numeric not null REFERENCES sales(sonum) ON DELETE CASCADE,
  qty_ordered numeric not null,
  qty_shipped numeric null,
  title_id char(6) not null REFERENCES titles(title_id) ON DELETE CASCADE,
  date_shipped date null,
  PRIMARY KEY (sonum, title_id));
grant select on salesdetails to public
;
--CREATE INDEX commands
create unique index pubind on publishers(pub_id);
create unique index auidind on authors (au_id);
create index aunmind on authors (au_lname, au_fname);
create unique index titleidind on titles (title_id);
create index titleind on titles (title);
create unique index taind on titleauthors (au_id, title_id);
create unique index edind on editors (ed_id);
create index ednmind on editors (ed_lname, ed_fname);
create unique index teind on titleditors (ed_id, title_id);
create index rstidind on roysched (title_id);
create unique index sdind on salesdetails (sonum, title_id);
create unique index salesind on sales (sonum);
