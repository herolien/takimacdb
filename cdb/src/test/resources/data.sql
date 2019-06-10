drop table if exists computer;
drop table if exists company;

create table if not exists company (
  id                        bigint not null auto_increment,
  name                      varchar(255),
  constraint pk_company primary key (id))
;

create table if not exists computer (
  id                        bigint not null auto_increment,
  name                      varchar(255),
  introduced                timestamp NULL,
  discontinued              timestamp NULL,
  company_id                bigint default NULL,
  constraint pk_computer primary key (id))
;

create index if not exists ix_computer_company_1 on computer (company_id);

insert into company (id,name) values ( null,'Apple Inc.');
insert into company (id,name) values ( null,'Thinking Machines');
insert into company (id,name) values ( null,'RCA');
insert into company (id,name) values ( null,'Netronics');
insert into company (id,name) values ( null,'Tandy Corporation');
insert into company (id,name) values ( null,'Commodore International');
insert into company (id,name) values ( null,'MOS Technology');
insert into company (id,name) values ( null,'Micro Instrumentation and Telemetry Systems');
insert into company (id,name) values ( null,'IMS Associates, Inc.');
insert into company (id,name) values ( null,'Digital Equipment Corporation');
insert into company (id,name) values ( null,'Lincoln Laboratory');
insert into company (id,name) values ( null,'Moore School of Electrical Engineering');
insert into company (id,name) values ( null,'IBM');
insert into company (id,name) values ( null,'Amiga Corporation');
insert into company (id,name) values ( null,'Canon');
insert into company (id,name) values ( null,'Nokia');
insert into company (id,name) values ( null,'Sony');
insert into company (id,name) values ( null,'OQO');
insert into company (id,name) values ( null,'NeXT');


insert into computer (id,name,introduced,discontinued,company_id) values ( null,'Dragon 32/64',null,null,null);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'MEK6800D2',null,null,null);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'Newbear 77/68',null,null,null);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'Commodore PET',null,null,6);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'Commodore 64','1982-08-01','1994-01-01',6);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'Commodore 64C',null,null,null);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'Commodore SX-64',null,null,6);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'Commodore 128',null,null,6);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'Apple I','1976-04-01','1977-10-01',1);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'KIM-1','1975-01-01',null,7);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'Altair 8800','1974-12-19',null,8);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'IMSAI 8080','1975-08-01',null,9);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'IMSAI Series Two',null,null,null);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'VAX','1977-10-25',null,10);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'VAX 11/780','1977-10-25',null,10);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'VAX 11/750','1980-10-01',null,10);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'TX-2','1958-01-01',null,11);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'TX-0','1956-01-01',null,11);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'Whirlwind','1951-04-20',null,11);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'ENIAC','1946-02-15','1955-10-02',12);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'IBM PC','1981-08-12',null,13);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'Macintosh Classic',null,null,null);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'Macintosh Classic II','1991-01-01',null,1);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'Amiga','1985-01-01',null,14);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'Amiga 1000',null,null,6);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'Amiga 500','1987-01-01',null,6);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'Amiga 500+',null,null,null);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'Amiga 2000','1986-01-01','1990-01-01',6);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'Amiga 3000',null,null,6);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'Amiga 600','1992-03-01',null,6);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'Macintosh 128K','1984-01-01',null,1);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'Macintosh 512K','1984-09-10','1986-04-14',1);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'Macintosh SE','1987-03-02','1989-08-01',1);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'Macintosh SE/30','1989-01-19','1991-10-21',1);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'Canon Cat','1987-01-01',null,15);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'Nokia 770',null,null,16);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'Nokia N800','2007-01-01',null,16);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'Mylo','2006-09-21',null,17);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'OQO 02','2007-01-01',null,18);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'OQO 01+',null,null,null);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'Pinwheel calculator',null,null,null);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'iBook',null,null,1);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'MacBook','2006-05-16',null,1);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'NeXTstation','1990-01-01','1993-01-01',19);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'NeXTcube','1988-01-01','1993-01-01',19);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'NeXTstation Color Turbo',null,null,null);
insert into computer (id,name,introduced,discontinued,company_id) values ( null,'NeXTstation Color',null,null,null);