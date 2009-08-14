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
create table CAB2B_MODEL_GROUP (
  MODEL_ID bigint not null auto_increment, 
  MODEL_GROUP_NAME varchar(255) unique not null, 
  SECURED BOOLEAN, DESCRIPTION text, 
  ENTITY_GROUP_NAMES text not null, 
  primary key (MODEL_ID)
);
create table CAB2B_MMA (
  ID bigint not null auto_increment, 
  IS_VISIBLE BOOLEAN,  
  ATTRIBUTE_ID bigint not null,
  CATEGORIAL_ATTRIBUTE_IDS varchar(254) not null,
  MMC_ID bigint not null,
  primary key (ID)
);
create table CAB2B_MMC (
  ID bigint not null auto_increment,
  ENTITY_ID bigint not null,
  APP_GROUP_ID bigint not null, 
  CATEGORY_IDS varchar(254) not null,
  primary key (ID)
);
create table CAB2B_COMPOUND_QUERY (
  IDENTIFIER bigint not null,
  primary key (IDENTIFIER)
);
create table CAB2B_KEYWORD_QUERY (
  IDENTIFIER bigint not null,
  APP_GROUP_ID bigint, 
  primary key (IDENTIFIER)
);
create table CAB2B_MMC_QUERY (
  IDENTIFIER bigint not null,
  primary key (IDENTIFIER)
);
create table CAB2B_QUERY (
  IDENTIFIER bigint not null,
  ENTITY_ID bigint not null,
  COMPOUND_QUERY_ID bigint, 
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
create table CAB2B_SERVICE_URL (
   URL_ID bigint not null auto_increment,
   DOMAIN_MODEL text not null,
   VERSION text not null,
   URL text not null,
   ADMIN_DEFINED tinyint not null,
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
create table CAB2B_CATEGORY_POPULARITY (
  IDENTIFIER bigint(30) not null auto_increment,
  ENTITY_ID bigint(30) default null,
  POPULARITY bigint(30) not null default '0',
  UPDATED_DATE timestamp not null,
  primary key  (IDENTIFIER)
);
create table CAB2B_ABSTRACT_STATUS (
  ID bigint not null, 
  STATUS varchar(100) not null, 
  RESULT_COUNT integer not null, 
  MESSAGE varchar(255) not null, 
  DESCRIPTION varchar(255) not null, 
  primary key (ID)
);

create table CAB2B_QUERY_STATUS (
  ID bigint not null, 
  USER_ID bigint not null, 
  QUERY_ID bigint not null, 
  CONDITIONS varchar(1024) not null, 
  START_TIME datetime not null, 
  END_TIME datetime not null, 
  FILENAME varchar(100) not null, 
  PARENT_ID bigint, 
  primary key (ID)
);

create table CAB2B_URL_STATUS (
  ID bigint not null, 
  URL varchar(255) not null, 
  URL_ID bigint, 
  primary key (ID)
);

alter table CAB2B_MMA add index FK2A9170D69EDCC3F8 (MMC_ID), add constraint FK2A9170D69EDCC3F8 foreign key (MMC_ID) references CAB2B_MMC (ID);
alter table CAB2B_MMC add index FK2A9170D89A9FD6A2 (APP_GROUP_ID), add constraint FK2A9170D89A9FD6A2 foreign key (APP_GROUP_ID) references CAB2B_MODEL_GROUP (MODEL_ID);
alter table CAB2B_COMPOUND_QUERY add index FK4DB047BF17568730 (IDENTIFIER), add constraint FK4DB047BF17568730 foreign key (IDENTIFIER) references CAB2B_QUERY (IDENTIFIER);
alter table CAB2B_KEYWORD_QUERY add index FK9A5BAA79A9FD6A2 (APP_GROUP_ID), add constraint FK9A5BAA79A9FD6A2 foreign key (APP_GROUP_ID) references CAB2B_MODEL_GROUP (MODEL_ID);
alter table CAB2B_KEYWORD_QUERY add index FK9A5BAA7BC19CC53 (IDENTIFIER), add constraint FK9A5BAA7BC19CC53 foreign key (IDENTIFIER) references CAB2B_COMPOUND_QUERY (IDENTIFIER);
alter table CAB2B_MMC_QUERY add index FK3CB8F281BC19CC53 (IDENTIFIER), add constraint FK3CB8F281BC19CC53 foreign key (IDENTIFIER) references CAB2B_COMPOUND_QUERY (IDENTIFIER);
alter table CAB2B_QUERY add index FKCC34AD9D45C67BB0 (COMPOUND_QUERY_ID), add constraint FKCC34AD9D45C67BB0 foreign key (COMPOUND_QUERY_ID) references CAB2B_COMPOUND_QUERY (IDENTIFIER);
alter table CAB2B_QUERY add index FKCC34AD9D1F030BCB (IDENTIFIER), add constraint FKCC34AD9D1F030BCB foreign key (IDENTIFIER) references QUERY_PARAMETERIZED_QUERY (IDENTIFIER);

alter table OUTPUT_CLASS_URLS add index FKE131CD69A638FEFD (CAB2B_QUERY_ID), add constraint FKE131CD69A638FEFD foreign key (CAB2B_QUERY_ID) references CAB2B_QUERY (IDENTIFIER);
alter table CAB2B_DATA_CATEGORY add index FKFA70BDE8A2330820 (ID), add constraint FKFA70BDE8A2330820 foreign key (ID) references ABSTRACT_CATEGORY (ID);
alter table CAB2B_DATA_CATEGORY add index FKFA70BDE8DF75106F (ROOT_CLASS_ID), add constraint FKFA70BDE8DF75106F foreign key (ROOT_CLASS_ID) references DATA_CATEGORIAL_CLASS (ID);
alter table DATA_CATEGORIAL_ATTRIBUTE add index FK782EFCCB34ED55B7 (ID), add constraint FK782EFCCB34ED55B7 foreign key (ID) references ABSTRACT_CATEGORIAL_ATTRIBUTE (ID);
alter table DATA_CATEGORIAL_CLASS add index FK13067327F94A5493 (ID), add constraint FK13067327F94A5493 foreign key (ID) references ABSTRACT_CATEGORIAL_CLASS (IDENTIFIER);
alter table CAB2B_USER_URL_MAPPING add index FKC64BBF4AAEC86F2D (USER_ID), add constraint FKC64BBF4AAEC86F2D foreign key (USER_ID) references CAB2B_USER (USER_ID);
alter table CAB2B_USER_URL_MAPPING add index FKC64BBF4AB2004842 (SERVICE_URL_ID), add constraint FKC64BBF4AB2004842 foreign key (SERVICE_URL_ID) references CAB2B_SERVICE_URL (URL_ID);

alter table CAB2B_QUERY_STATUS add index FKE587F8D49D65F450 (ID), add constraint FKE587F8D49D65F450 foreign key (ID) references CAB2B_ABSTRACT_STATUS (ID);
alter table CAB2B_QUERY_STATUS add index FKE587F8D4AEC86F2D (USER_ID), add constraint FKE587F8D4AEC86F2D foreign key (USER_ID) references CAB2B_USER (USER_ID);
alter table CAB2B_QUERY_STATUS add index FKE587F8D4286B8D51 (PARENT_ID), add constraint FKE587F8D4286B8D51 foreign key (PARENT_ID) references CAB2B_QUERY_STATUS (ID);
alter table CAB2B_QUERY_STATUS add index FKE587F8D4CEDB347A (QUERY_ID), add constraint FKE587F8D4CEDB347A foreign key (QUERY_ID) references CAB2B_QUERY (IDENTIFIER);
alter table CAB2B_URL_STATUS add index FK8C4B750D9D65F450 (ID), add constraint FK8C4B750D9D65F450 foreign key (ID) references CAB2B_ABSTRACT_STATUS (ID);
alter table CAB2B_URL_STATUS add index FK8C4B750D7B24DE6C (URL_ID), add constraint FK8C4B750D7B24DE6C foreign key (URL_ID) references CAB2B_QUERY_STATUS (ID);

insert into CAB2B_ID_TABLE(NEXT_ASSOCIATION_ID) value(1);
insert into CAB2B_user (USER_ID, NAME, PASSWORD, IS_ADMIN) values (1,'Admin','admin123',1);
insert into CAB2B_user (USER_ID, NAME, IS_ADMIN) values (2,'Anonymous',0);