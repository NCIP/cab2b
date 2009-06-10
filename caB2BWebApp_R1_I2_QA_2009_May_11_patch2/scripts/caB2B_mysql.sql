alter table CAB2B_DATA_CATEGORY drop foreign key FKFA70BDE8A2330820;
alter table CAB2B_DATA_CATEGORY drop foreign key FKFA70BDE8DF75106F;
alter table DATA_CATEGORIAL_ATTRIBUTE drop foreign key FK782EFCCB34ED55B7;
alter table DATA_CATEGORIAL_CLASS drop foreign key FK13067327F94A5493;
alter table OUTPUT_CLASS_URLS drop foreign key FKE131CD69A638FEFD;
alter table CAB2B_QUERY drop foreign key FKCC34AD9DBC7298A9;
alter table CAB2B_USER_URL_MAPPING drop foreign key FKC64BBF4AAEC86F2D;
alter table CAB2B_USER_URL_MAPPING drop foreign key FKC64BBF4AB2004842;

drop table if exists CURATED_PATH;
drop table if exists CURATED_PATH_TO_PATH;
drop table if exists PATH;
drop table if exists INTER_MODEL_ASSOCIATION;
drop table if exists INTRA_MODEL_ASSOCIATION;
drop table if exists ASSOCIATION;
drop table if exists CAB2B_ID_TABLE;
drop table if exists CAB2B_DATA_CATEGORY;
drop table if exists DATA_CATEGORIAL_ATTRIBUTE;
drop table if exists DATA_CATEGORIAL_CLASS;
drop table if exists OUTPUT_CLASS_URLS;
drop table if exists CAB2B_MODEL_GROUP;
drop table if exists CAB2B_QUERY;
drop table if exists CAB2B_SERVICE_URL;
drop table if exists CAB2B_USER;
drop table if exists CAB2B_USER_URL_MAPPING;
drop table if exists CAB2B_CATEGORY_POPULARITY;

/*INTERMEDIATE_PATH contains ASSOCIATION(ASSOCIATION_ID) connected by underscore */
create table PATH(
     PATH_ID           bigint         not null auto_increment,
     FIRST_ENTITY_ID   bigint         null,
     INTERMEDIATE_PATH varchar(1000)  null,
     LAST_ENTITY_ID    bigint         null,
     primary key (PATH_ID),
     index INDEX1 (FIRST_ENTITY_ID,LAST_ENTITY_ID)
);
/* Possible values for ASSOCIATION_TYPE are 1 and 2
ASSOCIATION_TYPE = 1 represents INTER_MODEL_ASSOCIATION.
ASSOCIATION_TYPE = 2 represents INTRA_MODEL_ASSOCIATION.
*/
create table ASSOCIATION(
    ASSOCIATION_ID    bigint    not null,
    ASSOCIATION_TYPE  INT(8)    not null ,
    primary key (ASSOCIATION_ID)
);
create table INTER_MODEL_ASSOCIATION(
    ASSOCIATION_ID      bigint  not null references ASSOCIATION(ASSOCIATION_ID),
    LEFT_ENTITY_ID      bigint  not null,
    LEFT_ATTRIBUTE_ID   bigint  not null,
    RIGHT_ENTITY_ID     bigint  not null,
    RIGHT_ATTRIBUTE_ID  bigint  not null,
    primary key (ASSOCIATION_ID)
);
create table INTRA_MODEL_ASSOCIATION(
    ASSOCIATION_ID    bigint    not null references ASSOCIATION(ASSOCIATION_ID),
    DE_ASSOCIATION_ID bigint    not null UNIQUE ,
    primary key (ASSOCIATION_ID)
);
create table CAB2B_ID_TABLE(
    NEXT_ASSOCIATION_ID    bigint    not null,
    primary key (NEXT_ASSOCIATION_ID)
);
create table CURATED_PATH (
	curated_path_Id BIGINT not null auto_increment,
	entity_ids VARCHAR(1000),
	selected boolean,
	primary key (curated_path_Id)
);
/*This is mapping table for many-to-many relationship between tables PATH and CURATED_PATH */
create table CURATED_PATH_TO_PATH (
	curated_path_Id BIGINT references CURATED_PATH (curated_path_Id),
	path_id BIGINT  references PATH (path_id),
	primary key (curated_path_Id,path_id)
);
create table OUTPUT_CLASS_URLS (
   CAB2B_QUERY_ID bigint not null,
   OUTPUT_CLASS_URL varchar(255),
   POSITION integer not null,
   primary key (CAB2B_QUERY_ID, POSITION)
);
create table CAB2B_QUERY (
   IDENTIFIER bigint not null,
   ENTITY_ID bigint not null,
   primary key (IDENTIFIER)
);
create table CAB2B_DATA_CATEGORY (
   ID bigint not null,
   description varchar(255),
   name varchar(255) unique,
   ROOT_CLASS_ID bigint unique,
   primary key (ID)
);
create table DATA_CATEGORIAL_ATTRIBUTE (
   ID bigint not null,
   primary key (ID)
);
create table DATA_CATEGORIAL_CLASS (
   ID bigint not null,
   primary key (ID)
);
create table CAB2B_MODEL_GROUP (
	MODEL_ID bigint not null auto_increment,
	MODEL_GROUP_NAME text null,
	SECURED bit null,
	DESCRIPTION text null,
	ENTITY_GROUP_NAMES text null,
	primary key(MODEL_ID)
);
create table CAB2B_SERVICE_URL (
   URL_ID bigint not null auto_increment,
   DOMAIN_MODEL text not null,
   VERSION text not null,
   URL text not null,
   ADMIN_DEFINED bit not null,
   HOSTING_CENTER varchar(254) null,
   DESCRIPTION text null,
   CONTACT_NAME text null,
   CONTACT_MAIL text null,
   HOSTING_CENTER_SHORT_NAME text null,
   primary key (URL_ID)
);
create table CAB2B_USER (
   USER_ID bigint not null auto_increment,
   NAME varchar(254) not null,
   PASSWORD varchar(30),
   IS_ADMIN bit not null,
   primary key (USER_ID)
);
create table CAB2B_USER_URL_MAPPING (
   SERVICE_URL_ID bigint not null,
   USER_ID bigint not null,
   primary key (USER_ID, SERVICE_URL_ID)
);
CREATE TABLE CAB2B_CATEGORY_POPULARITY (
  IDENTIFIER bigint(30) not null auto_increment,
  ENTITY_ID bigint(30) default null,
  POPULARITY bigint(30) not null default '0',
  UPDATED_DATE timestamp not null,
  primary key  (IDENTIFIER)
);

alter table OUTPUT_CLASS_URLS add index FKE131CD69A638FEFD (CAB2B_QUERY_ID), add constraint FKE131CD69A638FEFD foreign key (CAB2B_QUERY_ID) references CAB2B_QUERY (IDENTIFIER);
alter table CAB2B_QUERY add index FKCC34AD9DBC7298A9 (IDENTIFIER), add constraint FKCC34AD9DBC7298A9 foreign key (IDENTIFIER) references QUERY (IDENTIFIER);
alter table CAB2B_DATA_CATEGORY add index FKFA70BDE8A2330820 (ID), add constraint FKFA70BDE8A2330820 foreign key (ID) references ABSTRACT_CATEGORY (ID);
alter table CAB2B_DATA_CATEGORY add index FKFA70BDE8DF75106F (ROOT_CLASS_ID), add constraint FKFA70BDE8DF75106F foreign key (ROOT_CLASS_ID) references DATA_CATEGORIAL_CLASS (ID);
alter table DATA_CATEGORIAL_ATTRIBUTE add index FK782EFCCB34ED55B7 (ID), add constraint FK782EFCCB34ED55B7 foreign key (ID) references ABSTRACT_CATEGORIAL_ATTRIBUTE (ID);
alter table DATA_CATEGORIAL_CLASS add index FK13067327F94A5493 (ID), add constraint FK13067327F94A5493 foreign key (ID) references ABSTRACT_CATEGORIAL_CLASS (IDENTIFIER);
alter table CAB2B_USER_URL_MAPPING add index FKC64BBF4AAEC86F2D (USER_ID), add constraint FKC64BBF4AAEC86F2D foreign key (USER_ID) references CAB2B_USER (USER_ID);
alter table CAB2B_USER_URL_MAPPING add index FKC64BBF4AB2004842 (SERVICE_URL_ID), add constraint FKC64BBF4AB2004842 foreign key (SERVICE_URL_ID) references CAB2B_SERVICE_URL (URL_ID);

insert into CAB2B_ID_TABLE(NEXT_ASSOCIATION_ID) value(1);
insert into CAB2B_user (USER_ID, NAME, PASSWORD, IS_ADMIN) values (1,'Admin','admin123',1);
insert into CAB2B_user (USER_ID, NAME, IS_ADMIN) values (2,'Anonymous',0);

