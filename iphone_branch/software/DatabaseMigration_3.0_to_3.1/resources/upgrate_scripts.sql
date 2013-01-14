/*L
  Copyright Georgetown University.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cab2b/LICENSE.txt for details.
L*/

ALTER TABLE DYEXTN_SEMANTIC_PROPERTY MODIFY CONCEPT_DEFINITION VARCHAR(2048);
ALTER TABLE DYEXTN_TAGGED_VALUE MODIFY T_KEY VARCHAR(1024);
ALTER TABLE DYEXTN_TAGGED_VALUE MODIFY T_VALUE VARCHAR(1024);
ALTER TABLE ABSTRACT_CATEGORY ADD SYSTEM_GENERATED boolean default false not null;
ALTER TABLE CAB2B_QUERY ADD COMPOUND_QUERY_ID bigint;
ALTER TABLE CAB2B_MODEL_GROUP MODIFY SECURED BOOLEAN;

create table CAB2B_ABSTRACT_STATUS (
  ID bigint not null auto_increment, 
  STATUS varchar(100) not null, 
  RESULT_COUNT integer, 
  MESSAGE varchar(255), 
  DESCRIPTION varchar(255), 
  primary key (ID)
);

create table CAB2B_QUERY_STATUS (
  ID bigint not null, 
  USER_ID bigint not null, 
  QUERY_ID bigint not null, 
  CONDITIONS varchar(1024) not null, 
  START_TIME datetime, 
  END_TIME datetime, 
  FILENAME varchar(100), 
  PARENT_ID bigint,
  VISIBLE boolean not null default false,
  primary key (ID)
);

create table CAB2B_URL_STATUS (
  ID bigint not null, 
  URL varchar(255) not null, 
  QUERY_STATUS_ID bigint not null default 0, 
  primary key (ID)
);

alter table CAB2B_QUERY_STATUS add index FKE587F8D49D65F450 (ID), add constraint FKE587F8D49D65F450 foreign key (ID) references CAB2B_ABSTRACT_STATUS (ID);
alter table CAB2B_QUERY_STATUS add index FKE587F8D4AEC86F2D (USER_ID), add constraint FKE587F8D4AEC86F2D foreign key (USER_ID) references CAB2B_USER (USER_ID);
alter table CAB2B_QUERY_STATUS add index FKE587F8D4286B8D51 (PARENT_ID), add constraint FKE587F8D4286B8D51 foreign key (PARENT_ID) references CAB2B_QUERY_STATUS (ID);
alter table CAB2B_QUERY_STATUS add index FKE587F8D4CEDB347A (QUERY_ID), add constraint FKE587F8D4CEDB347A foreign key (QUERY_ID) references CAB2B_QUERY (IDENTIFIER);
alter table CAB2B_URL_STATUS add index FK8C4B750D9D65F450 (ID), add constraint FK8C4B750D9D65F450 foreign key (ID) references CAB2B_ABSTRACT_STATUS (ID);
alter table CAB2B_URL_STATUS add index FK8C4B750D7B24DE6C (QUERY_STATUS_ID);

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

create table CAB2B_COMPOUND_QUERY (IDENTIFIER bigint not null, primary key (IDENTIFIER));
create table CAB2B_KEYWORD_QUERY (IDENTIFIER bigint not null, APP_GROUP_ID bigint, primary key (IDENTIFIER));
create table CAB2B_MMC_QUERY (IDENTIFIER bigint not null, primary key (IDENTIFIER));

alter table CAB2B_COMPOUND_QUERY add index FK4DB047BF17568730 (IDENTIFIER), add constraint FK4DB047BF17568730 foreign key (IDENTIFIER) references CAB2B_QUERY (IDENTIFIER);
alter table CAB2B_KEYWORD_QUERY add index FK9A5BAA79A9FD6A2 (APP_GROUP_ID), add constraint FK9A5BAA79A9FD6A2 foreign key (APP_GROUP_ID) references CAB2B_MODEL_GROUP (MODEL_ID);
alter table CAB2B_KEYWORD_QUERY add index FK9A5BAA7BC19CC53 (IDENTIFIER), add constraint FK9A5BAA7BC19CC53 foreign key (IDENTIFIER) references CAB2B_COMPOUND_QUERY (IDENTIFIER);
alter table CAB2B_MMC_QUERY add index FK3CB8F281BC19CC53 (IDENTIFIER), add constraint FK3CB8F281BC19CC53 foreign key (IDENTIFIER) references CAB2B_COMPOUND_QUERY (IDENTIFIER);
alter table CAB2B_QUERY add index FKCC34AD9D45C67BB0 (COMPOUND_QUERY_ID), add constraint FKCC34AD9D45C67BB0 foreign key (COMPOUND_QUERY_ID) references CAB2B_COMPOUND_QUERY (IDENTIFIER);
alter table CAB2B_MMA add index FK2A9170D69EDCC3F8 (MMC_ID), add constraint FK2A9170D69EDCC3F8 foreign key (MMC_ID) references CAB2B_MMC (ID);
alter table CAB2B_MMC add index FK2A9170D89A9FD6A2 (APP_GROUP_ID), add constraint FK2A9170D89A9FD6A2 foreign key (APP_GROUP_ID) references CAB2B_MODEL_GROUP (MODEL_ID);
alter table CAB2B_QUERY  drop foreign key FKCC34AD9DBC7298A9;
alter table CAB2B_QUERY  drop index FKCC34AD9DBC7298A9;
alter table CAB2B_QUERY add index FKCC34AD9D1F030BCB (IDENTIFIER), add constraint FKCC34AD9D1F030BCB foreign key (IDENTIFIER) references QUERY_PARAMETERIZED_QUERY (IDENTIFIER);

insert into cab2b_user(name, is_admin) values('db_migrator',0);
update query_abstract_query set CREATED_BY=(select USER_ID from cab2b_user where NAME='db_migrator') where QUERY_TYPE='ORed';