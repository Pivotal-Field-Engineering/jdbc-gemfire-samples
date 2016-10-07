connect 'jdbc:sqlfire://server1.dev:1527';

create schema tpmg;

create diskstore tpmg;

create asynceventlistener prototypeasynclistener 
(listenerclass 'com.kp.ttg.SimpleRowWriter' 
 initparams 'dataSourceName=mysql|name=writer|ignoredErrorCodes=1062') 
 server groups (prototype);

create table tpmg.stuff
(stuff_id varchar(40) not null
,stuff_name varchar(100)
,stuff_count integer
,primary key (stuff_id)) 
 partition by primary key redundancy 1 
 asynceventlistener(prototypeasynclistener);

create index tpmg.stuff_name on tpmg.stuff (stuff_name);

call sys.attach_loader('tpmg','stuff','com.kp.ttg.SimpleRowLoader','dataSourceName=mysql');

call sys.start_async_event_listener ('prototypeasynclistener');

create procedure tpmg.bulkloader (in dsn varchar(100), in schemaName varchar(100), in tableName varchar(100))
language java parameter style java modifies sql data
external name 'com.kp.ttg.BulkLoader.load';


insert into tpmg.stuff (stuff_id,stuff_name,stuff_count) values ('1','One',1);
select * from tpmg.stuff where stuff_id='1';
select * from tpmg.stuff where stuff_id='2';
select * from tpmg.stuff where stuff_id='3';
select * from tpmg.stuff where stuff_id='4';
select * from tpmg.stuff where stuff_id='5';
select * from tpmg.stuff where stuff_id='6';
select * from tpmg.stuff where stuff_id='7';
select * from tpmg.stuff where stuff_id='8';
select * from tpmg.stuff where stuff_id='9';

call tpmg.bulkloader('mysql','TPMG','STUFF');

call sqlj.install_jar('/home/tdalsing/prototype1-0.0.1-SNAPSHOT.jar','prototypejar',0);
call sys.remove_loader('tpmg','stuff');

