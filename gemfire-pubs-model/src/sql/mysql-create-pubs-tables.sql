DROP TABLE if exists titleauthor;
DROP TABLE if exists employee;
DROP TABLE if exists authors;   
DROP TABLE if exists discounts;
DROP TABLE if exists jobs;
DROP TABLE if exists pub_info;
DROP TABLE if exists roysched;
DROP TABLE if exists sales;
DROP TABLE if exists stores;
DROP TABLE if exists titles;
DROP table if exists publishers;

CREATE TABLE authors
(
   au_id          varchar(11)       NOT NULL,
   au_lname       varchar(40)       NOT NULL,
   au_fname       varchar(20)       NOT NULL,
   phone          char(12)          NOT NULL         DEFAULT 'UNKNOWN',
   address        varchar(40)           NULL,
   city           varchar(20)           NULL,
   state          char(2)               NULL,
   zip            char(5)               NULL         CHECK (zip like '[0-9][0-9][0-9][0-9][0-9]'),
   contract       CHAR               NOT NULL,
   PRIMARY KEY (au_id),
   CONSTRAINT UPKCL_audind CHECK (au_id like '[0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9][0-9][0-9]')
);

CREATE TABLE publishers
(
   pub_id         char(4)           NOT NULL,
   pub_name       varchar(40)           NULL,
   city           varchar(20)           NULL,
   state          char(2)               NULL,
   country        varchar(30)           NULL DEFAULT 'USA',
   PRIMARY KEY (pub_id),
   CONSTRAINT UPKCL_pubind CHECK (pub_id in ('1389', '0736', '0877', '1622', '1756') OR pub_id like '99[0-9][0-9]')
);


CREATE TABLE titles
(
   title_id       varchar(6) NOT NULL,
   title          varchar(80)       NOT NULL,
   type           char(12)          NOT NULL            DEFAULT 'UNDECIDED',
   pub_id         char(4)               NULL            REFERENCES publishers(pub_id),
   price          DECIMAL(7,2)          NULL,
   advance        DECIMAL(13,2)          NULL,
   royalty        int                   NULL,
   ytd_sales      int                   NULL,
   notes          varchar(200)          NULL,
   pubdate        DATETIME          NOT NULL,
   PRIMARY KEY (title_id)
);


CREATE TABLE titleauthor
(
   au_id          varchar(11)	  REFERENCES authors(au_id),
   title_id       varchar(6)	  REFERENCES titles(title_id),
   au_ord         SMALLINT               NULL,
   royaltyper     int                   NULL,
   PRIMARY KEY (au_id, title_id)
);


CREATE TABLE stores
(
   stor_id        char(4)           NOT NULL,
   stor_name      varchar(40)           NULL,
   stor_address   varchar(40)           NULL,
   city           varchar(20)           NULL,
   state          char(2)               NULL,
   zip            char(5)               NULL,
   PRIMARY KEY (stor_id)
);


CREATE TABLE sales
(
   stor_id        char(4)           NOT NULL REFERENCES stores(stor_id),
   ord_num        varchar(20)       NOT NULL,
   ord_date       DATE          NOT NULL,
   qty            smallint          NOT NULL,
   payterms       varchar(12)       NOT NULL,
   title_id       varchar(6)	    REFERENCES titles(title_id),
   CONSTRAINT UPKCL_sales PRIMARY KEY  (stor_id, ord_num, title_id)
);


CREATE TABLE roysched
(
   title_id       varchar(6)	REFERENCES titles(title_id),
   lorange        int                   NULL,
   hirange        int                   NULL,
   royalty        int                   NULL
);

CREATE TABLE discounts
(
   discounttype   varchar(40)       NOT NULL,
   stor_id        char(4) NULL	    REFERENCES stores(stor_id),
   lowqty         smallint              NULL,
   highqty        smallint              NULL,
   discount       dec(4,2)          NOT NULL
);


CREATE TABLE jobs
(
   job_id         smallint          NOT NULL      AUTO_INCREMENT ,
   job_desc       varchar(50)       NOT NULL        DEFAULT 'New Position - title not formalized yet',
   min_lvl        SMALLINT           NOT NULL       CHECK (min_lvl >= 10),
   max_lvl        SMALLINT           NOT NULL       CHECK (max_lvl <= 250),
   PRIMARY KEY (job_id) 
);

CREATE TABLE pub_info
(
   pub_id         char(4)           NOT NULL            REFERENCES publishers(pub_id),
   logo           BLOB                 NULL,
   pr_info        LONGTEXT                  NULL,
   PRIMARY KEY (pub_id)
);

CREATE TABLE employee
(
   emp_id         char(9)         NOT NULL,
   fname          varchar(20)       NOT NULL,
   minit          char (1)        NULL,
   lname          varchar(30)       NOT NULL,
   job_id         smallint          NOT NULL    DEFAULT 1        REFERENCES jobs(job_id),
   job_lvl        SMALLINT          NULL        DEFAULT 10,
   pub_id         char(4)           NOT NULL            DEFAULT '9952'   REFERENCES publishers(pub_id),
   hire_date      DATETIME          NOT NULL,
   PRIMARY KEY (emp_id)
);

