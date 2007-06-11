alter table experiment drop foreign key FKFAE9DBFD7ABC429D;
alter table datalist drop foreign key FK6AADBB483E23832;

alter table dlexpmap drop foreign key FK983F90A73E23832;
alter table dlexpmap drop foreign key FK983F90A77ABC429D;
alter table additionalmetadata drop foreign key FK74035C5673486922;

alter table experimentgroup drop foreign key FKD9A210A264FE787B;
alter table experimentgroup drop foreign key FKD9A210A27AB82B46;


alter table expgrpmapping drop foreign key FKCD7AF6267AB82B46;
alter table expgrpmapping drop foreign key FKCD7AF6267ABC429D;
drop table if exists experiment;
drop table if exists datalist;


drop table if exists dlexpmap;
drop table if exists additionalmetadata;

drop table if exists abstractdomainobject;
drop table if exists experimentgroup;

drop table if exists expgrpmapping;
create table experiment (
   EXP_ID bigint not null,
   primary key (EXP_ID)
);
create table datalist (
   DL_ID bigint not null,
   ENT_ID bigint,
   primary key (DL_ID)
);
create table dlexpmap (
   EXP_ID bigint not null,
   DL_ID bigint not null,
   primary key (EXP_ID, DL_ID)
);
create table additionalmetadata (
   AMD_ID bigint not null,
   AMD_NAME varchar(50),
   AMD_DESCRIPTION varchar(50),
   AMD_CREATED_ON date,
   AMD_LAST_UPDATED_ON date,
   primary key (AMD_ID)
);
create table abstractdomainobject (
   ADO_ID bigint not null auto_increment,
   ADO_ACTIVITY_STATUS varchar(50),
   primary key (ADO_ID)
);
create table experimentgroup (
   EXG_ID bigint not null,
   PARENT_EXG_ID bigint,
   primary key (EXG_ID)
);
create table expgrpmapping (
   EXG_ID bigint not null,
   EXP_ID bigint not null,
   primary key (EXP_ID, EXG_ID)
);
alter table experiment add index FKFAE9DBFD7ABC429D (EXP_ID), add constraint FKFAE9DBFD7ABC429D foreign key (EXP_ID) references additionalmetadata (AMD_ID);
alter table datalist add index FK6AADBB483E23832 (DL_ID), add constraint FK6AADBB483E23832 foreign key (DL_ID) references additionalmetadata (AMD_ID);
alter table dlexpmap add index FK983F90A73E23832 (DL_ID), add constraint FK983F90A73E23832 foreign key (DL_ID) references datalist (DL_ID);
alter table dlexpmap add index FK983F90A77ABC429D (EXP_ID), add constraint FK983F90A77ABC429D foreign key (EXP_ID) references experiment (EXP_ID);
alter table additionalmetadata add index FK74035C5673486922 (AMD_ID), add constraint FK74035C5673486922 foreign key (AMD_ID) references abstractdomainobject (ADO_ID);
alter table experimentgroup add index FKD9A210A264FE787B (PARENT_EXG_ID), add constraint FKD9A210A264FE787B foreign key (PARENT_EXG_ID) references experimentgroup (EXG_ID);
alter table experimentgroup add index FKD9A210A27AB82B46 (EXG_ID), add constraint FKD9A210A27AB82B46 foreign key (EXG_ID) references additionalmetadata (AMD_ID);
alter table expgrpmapping add index FKCD7AF6267AB82B46 (EXG_ID), add constraint FKCD7AF6267AB82B46 foreign key (EXG_ID) references experimentgroup (EXG_ID);
alter table expgrpmapping add index FKCD7AF6267ABC429D (EXP_ID), add constraint FKCD7AF6267ABC429D foreign key (EXP_ID) references experiment (EXP_ID);
