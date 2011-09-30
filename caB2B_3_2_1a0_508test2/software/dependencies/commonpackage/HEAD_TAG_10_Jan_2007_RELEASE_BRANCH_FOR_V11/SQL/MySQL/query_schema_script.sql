alter table ABSTRACT_CATEGORIAL_ATTRIBUTE drop foreign key FK85397F53A5F024A0;
alter table ABSTRACT_CATEGORIAL_ATTRIBUTE drop foreign key FK85397F5379C8ACDF;
alter table ABSTRACT_CATEGORIAL_CLASS drop foreign key FK6C8779AF8168C18B;
alter table ABSTRACT_CATEGORIAL_CLASS drop foreign key FK6C8779AF1E10D264;
alter table ABSTRACT_CATEGORY drop foreign key FK92BB047B70295EEC;
alter table CATEGORIAL_ATTRIBUTE drop foreign key FK31F77B5634ED55B7;
alter table CATEGORIAL_CLASS drop foreign key FK9651EF32F94A5493;
alter table CATEGORY drop foreign key FK31A8ACFEA2330820;
alter table CATEGORY drop foreign key FK31A8ACFEC88316F9;
alter table CATISSUE_AUDIT_EVENT_DETAILS drop foreign key FK5C07745DC62F96A4;
alter table CATISSUE_AUDIT_EVENT_LOG drop foreign key FK8BB672DFE2182003;
alter table CATISSUE_INTERFACE_COLUMN_DATA drop foreign key FK9C900851F98C179C;
alter table CATISSUE_PERMISSIBLE_VALUE drop foreign key FK57DDCE11F55618;
alter table CATISSUE_PERMISSIBLE_VALUE drop foreign key FK57DDCE1C6D9C7E1;
alter table QUERY drop foreign key FK49D20A84B0F861E;
alter table QUERY_CONDITION drop foreign key FKACCE6242DCE1896;
alter table QUERY_CONDITION_VALUES drop foreign key FK9997379D4D1598FE;
alter table QUERY_CONSTRAINTS drop foreign key FKE364FCFF1C7EBF3B;
alter table QUERY_EXPRESSION drop foreign key FK1B473A8F526D4561;
alter table QUERY_EXPRESSION drop foreign key FK1B473A8FCF83E189;
alter table QUERY_EXPRESSION drop foreign key FK1B473A8F3FB1E956;
alter table QUERY_EXPRESSIONID drop foreign key FK6662DBEA62E3EDC7;
alter table QUERY_EXPRESSION_OPERAND drop foreign key FKA3B976F9180A6E16;
alter table QUERY_GRAPH_ENTRY drop foreign key FKF055E4EAB2B42E5B;
alter table QUERY_GRAPH_ENTRY drop foreign key FKF055E4EA346832A9;
alter table QUERY_GRAPH_ENTRY drop foreign key FKF055E4EA1C7EBF3B;
alter table QUERY_GRAPH_ENTRY drop foreign key FKF055E4EAC070901F;
alter table QUERY_INTER_MODEL_ASSOCIATION drop foreign key FKD70658D15F5AB67E;
alter table QUERY_INTRA_MODEL_ASSOCIATION drop foreign key FKF1EDBDD35F5AB67E;
alter table QUERY_LOGICAL_CONNECTOR drop foreign key FKCF304780180A6E16;
alter table QUERY_OUTPUT_ATTRIBUTE drop foreign key FK22C9DB75509C0ACD;
alter table QUERY_OUTPUT_ATTRIBUTE drop foreign key FK22C9DB75604D4BDA;
alter table QUERY_PARAMETERIZED_CONDITION drop foreign key FK9BE75A3E4B9044D1;
alter table QUERY_PARAMETERIZED_QUERY drop foreign key FKA272176B76177EFE;
alter table QUERY_RULE drop foreign key FK14A65033180A6E16;
alter table QUERY_RULE drop foreign key FK14A6503362E3EDC7;
drop table if exists ABSTRACT_CATEGORIAL_ATTRIBUTE;
drop table if exists ABSTRACT_CATEGORIAL_CLASS;
drop table if exists ABSTRACT_CATEGORY;
drop table if exists CATEGORIAL_ATTRIBUTE;
drop table if exists CATEGORIAL_CLASS;
drop table if exists CATEGORY;
drop table if exists CATISSUE_AUDIT_EVENT;
drop table if exists CATISSUE_AUDIT_EVENT_DETAILS;
drop table if exists CATISSUE_AUDIT_EVENT_LOG;
drop table if exists CATISSUE_CDE;
drop table if exists CATISSUE_INTERFACE_COLUMN_DATA;
drop table if exists CATISSUE_PERMISSIBLE_VALUE;
drop table if exists CATISSUE_QUERY_TABLE_DATA;
drop table if exists QUERY;
drop table if exists QUERY_CONDITION;
drop table if exists QUERY_CONDITION_VALUES;
drop table if exists QUERY_CONSTRAINTS;
drop table if exists QUERY_EXPRESSION;
drop table if exists QUERY_EXPRESSIONID;
drop table if exists QUERY_EXPRESSION_OPERAND;
drop table if exists QUERY_GRAPH_ENTRY;
drop table if exists QUERY_INTER_MODEL_ASSOCIATION;
drop table if exists QUERY_INTRA_MODEL_ASSOCIATION;
drop table if exists QUERY_JOIN_GRAPH;
drop table if exists QUERY_LOGICAL_CONNECTOR;
drop table if exists QUERY_MODEL_ASSOCIATION;
drop table if exists QUERY_OUTPUT_ATTRIBUTE;
drop table if exists QUERY_PARAMETERIZED_CONDITION;
drop table if exists QUERY_PARAMETERIZED_QUERY;
drop table if exists QUERY_QUERY_ENTITY;
drop table if exists QUERY_RULE;
create table ABSTRACT_CATEGORIAL_ATTRIBUTE (
   ID bigint not null auto_increment,
   CATEGORIAL_CLASS_ID bigint,
   DE_SOURCE_CLASS_ATTRIBUTE_ID bigint,
   ABSTRACT_CATEGORIAL_ATTRIBUTE_ID bigint,
   primary key (ID)
);
create table ABSTRACT_CATEGORIAL_CLASS (
   IDENTIFIER bigint not null auto_increment,
   ABSTRACT_CATEGORY_ID bigint,
   PARENT_CATEGORIAL_CLASS_ID bigint,
   PATH_FROM_PARENT_ID bigint,
   DE_ENTITY_ID bigint,
   primary key (IDENTIFIER)
);
create table ABSTRACT_CATEGORY (
   ID bigint not null auto_increment,
   PARENT_CATEGORY_ID bigint unique,
   primary key (ID)
);
create table CATEGORIAL_ATTRIBUTE (
   ID bigint not null,
   DE_CATEGORY_ATTRIBUTE_ID bigint,
   primary key (ID)
);
create table CATEGORIAL_CLASS (
   ID bigint not null,
   primary key (ID)
);
create table CATEGORY (
   ID bigint not null,
   DE_ENTITY_ID bigint,
   ROOT_CLASS_ID bigint unique,
   primary key (ID)
);
create table CATISSUE_AUDIT_EVENT (
   IDENTIFIER bigint not null auto_increment,
   EVENT_TIMESTAMP datetime,
   USER_ID bigint,
   COMMENTS text,
   IP_ADDRESS varchar(20),
   primary key (IDENTIFIER)
);
create table CATISSUE_AUDIT_EVENT_DETAILS (
   IDENTIFIER bigint not null auto_increment,
   ELEMENT_NAME varchar(150),
   PREVIOUS_VALUE varchar(150),
   CURRENT_VALUE text,
   AUDIT_EVENT_LOG_ID bigint,
   primary key (IDENTIFIER)
);
create table CATISSUE_AUDIT_EVENT_LOG (
   IDENTIFIER bigint not null auto_increment,
   OBJECT_IDENTIFIER bigint,
   OBJECT_NAME varchar(50),
   EVENT_TYPE varchar(50),
   AUDIT_EVENT_ID bigint,
   primary key (IDENTIFIER)
);
create table CATISSUE_CDE (
   PUBLIC_ID varchar(30) not null,
   LONG_NAME varchar(200),
   DEFINITION text,
   VERSION varchar(50),
   LAST_UPDATED datetime,
   primary key (PUBLIC_ID)
);
create table CATISSUE_INTERFACE_COLUMN_DATA (
   IDENTIFIER bigint not null auto_increment,
   TABLE_ID bigint,
   COLUMN_NAME varchar(50),
   DISPLAY_NAME varchar(50),
   primary key (IDENTIFIER)
);
create table CATISSUE_PERMISSIBLE_VALUE (
   IDENTIFIER bigint not null auto_increment,
   CONCEPT_CODE varchar(20),
   DEFINITION text,
   PARENT_IDENTIFIER bigint,
   VALUE varchar(100),
   PUBLIC_ID varchar(30),
   primary key (IDENTIFIER)
);
create table CATISSUE_QUERY_TABLE_DATA (
   TABLE_ID bigint not null auto_increment,
   DISPLAY_NAME varchar(50),
   TABLE_NAME varchar(50),
   ALIAS_NAME varchar(50),
   primary key (TABLE_ID)
);
create table QUERY (
   IDENTIFIER bigint not null auto_increment,
   QUERY_CONSTRAINTS_ID bigint unique,
   primary key (IDENTIFIER)
);
create table QUERY_CONDITION (
   IDENTIFIER bigint not null auto_increment,
   ATTRIBUTE_ID bigint not null,
   RELATIONAL_OPERATOR varchar(255),
   QUERY_RULE_ID bigint,
   POSITION integer,
   primary key (IDENTIFIER)
);
create table QUERY_CONDITION_VALUES (
   QUERY_CONDITION_ID bigint not null,
   VALUE_LIST varchar(255),
   POSITION integer not null,
   primary key (QUERY_CONDITION_ID, POSITION)
);
create table QUERY_CONSTRAINTS (
   IDENTIFIER bigint not null auto_increment,
   QUERY_JOIN_GRAPH_ID bigint unique,
   primary key (IDENTIFIER)
);
create table QUERY_EXPRESSION (
   IDENTIFIER bigint not null auto_increment,
   QUERY_EXPRESSIONID_ID bigint unique,
   QUERY_QUERY_ENTITY_ID bigint not null,
   IS_IN_VIEW bit,
   IS_VISIBLE bit,
   QUERY_CONSTRAINT_ID bigint,
   primary key (IDENTIFIER)
);
create table QUERY_EXPRESSIONID (
   IDENTIFIER bigint not null,
   SUB_EXPRESSION_ID integer not null,
   primary key (IDENTIFIER)
);
create table QUERY_EXPRESSION_OPERAND (
   IDENTIFIER bigint not null auto_increment,
   QUERY_EXPRESSION_ID bigint,
   POSITION integer,
   primary key (IDENTIFIER)
);
create table QUERY_GRAPH_ENTRY (
   IDENTIFIER bigint not null auto_increment,
   QUERY_MODEL_ASSOCIATION_ID bigint,
   SOURCE_EXPRESSIONID_ID bigint,
   TARGET_EXPRESSIONID_ID bigint,
   QUERY_JOIN_GRAPH_ID bigint,
   primary key (IDENTIFIER)
);
create table QUERY_INTER_MODEL_ASSOCIATION (
   IDENTIFIER bigint not null,
   SOURCE_SERVICE_URL text not null,
   TARGET_SERVICE_URL text not null,
   SOURCE_ATTRIBUTE_ID bigint not null,
   TARGET_ATTRIBUTE_ID bigint not null,
   primary key (IDENTIFIER)
);
create table QUERY_INTRA_MODEL_ASSOCIATION (
   IDENTIFIER bigint not null,
   DE_ASSOCIATION_ID bigint not null,
   primary key (IDENTIFIER)
);
create table QUERY_JOIN_GRAPH (
   IDENTIFIER bigint not null auto_increment,
   primary key (IDENTIFIER)
);
create table QUERY_LOGICAL_CONNECTOR (
   IDENTIFIER bigint not null auto_increment,
   LOGICAL_OPERATOR varchar(255),
   NESTING_NUMBER integer,
   QUERY_EXPRESSION_ID bigint,
   POSITION integer,
   primary key (IDENTIFIER)
);
create table QUERY_MODEL_ASSOCIATION (
   IDENTIFIER bigint not null auto_increment,
   primary key (IDENTIFIER)
);
create table QUERY_OUTPUT_ATTRIBUTE (
   IDENTIFIER bigint not null auto_increment,
   EXPRESSIONID_ID bigint,
   ATTRIBUTE_ID bigint not null,
   PARAMETERIZED_QUERY_ID bigint,
   POSITION integer,
   primary key (IDENTIFIER)
);
create table QUERY_PARAMETERIZED_CONDITION (
   IDENTIFIER bigint not null,
   CONDITION_INDEX integer,
   CONDITION_NAME varchar(255),
   primary key (IDENTIFIER)
);
create table QUERY_PARAMETERIZED_QUERY (
   IDENTIFIER bigint not null,
   QUERY_NAME varchar(255) unique,
   DESCRIPTION text,
   primary key (IDENTIFIER)
);
create table QUERY_QUERY_ENTITY (
   IDENTIFIER bigint not null auto_increment,
   ENTITY_ID bigint not null,
   primary key (IDENTIFIER)
);
create table QUERY_RULE (
   IDENTIFIER bigint not null,
   QUERY_EXPRESSION_ID bigint not null,
   primary key (IDENTIFIER)
);
alter table ABSTRACT_CATEGORIAL_ATTRIBUTE add index FK85397F53A5F024A0 (CATEGORIAL_CLASS_ID), add constraint FK85397F53A5F024A0 foreign key (CATEGORIAL_CLASS_ID) references ABSTRACT_CATEGORIAL_CLASS (IDENTIFIER);
alter table ABSTRACT_CATEGORIAL_ATTRIBUTE add index FK85397F5379C8ACDF (ABSTRACT_CATEGORIAL_ATTRIBUTE_ID), add constraint FK85397F5379C8ACDF foreign key (ABSTRACT_CATEGORIAL_ATTRIBUTE_ID) references ABSTRACT_CATEGORIAL_CLASS (IDENTIFIER);
alter table ABSTRACT_CATEGORIAL_CLASS add index FK6C8779AF8168C18B (PARENT_CATEGORIAL_CLASS_ID), add constraint FK6C8779AF8168C18B foreign key (PARENT_CATEGORIAL_CLASS_ID) references ABSTRACT_CATEGORIAL_CLASS (IDENTIFIER);
alter table ABSTRACT_CATEGORIAL_CLASS add index FK6C8779AF1E10D264 (ABSTRACT_CATEGORY_ID), add constraint FK6C8779AF1E10D264 foreign key (ABSTRACT_CATEGORY_ID) references ABSTRACT_CATEGORY (ID);
alter table ABSTRACT_CATEGORY add index FK92BB047B70295EEC (PARENT_CATEGORY_ID), add constraint FK92BB047B70295EEC foreign key (PARENT_CATEGORY_ID) references ABSTRACT_CATEGORY (ID);
alter table CATEGORIAL_ATTRIBUTE add index FK31F77B5634ED55B7 (ID), add constraint FK31F77B5634ED55B7 foreign key (ID) references ABSTRACT_CATEGORIAL_ATTRIBUTE (ID);
alter table CATEGORIAL_CLASS add index FK9651EF32F94A5493 (ID), add constraint FK9651EF32F94A5493 foreign key (ID) references ABSTRACT_CATEGORIAL_CLASS (IDENTIFIER);
alter table CATEGORY add index FK31A8ACFEA2330820 (ID), add constraint FK31A8ACFEA2330820 foreign key (ID) references ABSTRACT_CATEGORY (ID);
alter table CATEGORY add index FK31A8ACFEC88316F9 (ROOT_CLASS_ID), add constraint FK31A8ACFEC88316F9 foreign key (ROOT_CLASS_ID) references CATEGORIAL_CLASS (ID);
alter table CATISSUE_AUDIT_EVENT_DETAILS add index FK5C07745DC62F96A4 (AUDIT_EVENT_LOG_ID), add constraint FK5C07745DC62F96A4 foreign key (AUDIT_EVENT_LOG_ID) references CATISSUE_AUDIT_EVENT_LOG (IDENTIFIER);
alter table CATISSUE_AUDIT_EVENT_LOG add index FK8BB672DFE2182003 (AUDIT_EVENT_ID), add constraint FK8BB672DFE2182003 foreign key (AUDIT_EVENT_ID) references CATISSUE_AUDIT_EVENT (IDENTIFIER);
alter table CATISSUE_INTERFACE_COLUMN_DATA add index FK9C900851F98C179C (TABLE_ID), add constraint FK9C900851F98C179C foreign key (TABLE_ID) references CATISSUE_QUERY_TABLE_DATA (TABLE_ID);
alter table CATISSUE_PERMISSIBLE_VALUE add index FK57DDCE11F55618 (PARENT_IDENTIFIER), add constraint FK57DDCE11F55618 foreign key (PARENT_IDENTIFIER) references CATISSUE_PERMISSIBLE_VALUE (IDENTIFIER);
alter table CATISSUE_PERMISSIBLE_VALUE add index FK57DDCE1C6D9C7E1 (PUBLIC_ID), add constraint FK57DDCE1C6D9C7E1 foreign key (PUBLIC_ID) references CATISSUE_CDE (PUBLIC_ID);
alter table QUERY add index FK49D20A84B0F861E (QUERY_CONSTRAINTS_ID), add constraint FK49D20A84B0F861E foreign key (QUERY_CONSTRAINTS_ID) references QUERY_CONSTRAINTS (IDENTIFIER);
alter table QUERY_CONDITION add index FKACCE6242DCE1896 (QUERY_RULE_ID), add constraint FKACCE6242DCE1896 foreign key (QUERY_RULE_ID) references QUERY_RULE (IDENTIFIER);
alter table QUERY_CONDITION_VALUES add index FK9997379D4D1598FE (QUERY_CONDITION_ID), add constraint FK9997379D4D1598FE foreign key (QUERY_CONDITION_ID) references QUERY_CONDITION (IDENTIFIER);
alter table QUERY_CONSTRAINTS add index FKE364FCFF1C7EBF3B (QUERY_JOIN_GRAPH_ID), add constraint FKE364FCFF1C7EBF3B foreign key (QUERY_JOIN_GRAPH_ID) references QUERY_JOIN_GRAPH (IDENTIFIER);
alter table QUERY_EXPRESSION add index FK1B473A8F526D4561 (QUERY_QUERY_ENTITY_ID), add constraint FK1B473A8F526D4561 foreign key (QUERY_QUERY_ENTITY_ID) references QUERY_QUERY_ENTITY (IDENTIFIER);
alter table QUERY_EXPRESSION add index FK1B473A8FCF83E189 (QUERY_CONSTRAINT_ID), add constraint FK1B473A8FCF83E189 foreign key (QUERY_CONSTRAINT_ID) references QUERY_CONSTRAINTS (IDENTIFIER);
alter table QUERY_EXPRESSION add index FK1B473A8F3FB1E956 (QUERY_EXPRESSIONID_ID), add constraint FK1B473A8F3FB1E956 foreign key (QUERY_EXPRESSIONID_ID) references QUERY_EXPRESSIONID (IDENTIFIER);
alter table QUERY_EXPRESSIONID add index FK6662DBEA62E3EDC7 (IDENTIFIER), add constraint FK6662DBEA62E3EDC7 foreign key (IDENTIFIER) references QUERY_EXPRESSION_OPERAND (IDENTIFIER);
alter table QUERY_EXPRESSION_OPERAND add index FKA3B976F9180A6E16 (QUERY_EXPRESSION_ID), add constraint FKA3B976F9180A6E16 foreign key (QUERY_EXPRESSION_ID) references QUERY_EXPRESSION (IDENTIFIER);
alter table QUERY_GRAPH_ENTRY add index FKF055E4EAB2B42E5B (QUERY_MODEL_ASSOCIATION_ID), add constraint FKF055E4EAB2B42E5B foreign key (QUERY_MODEL_ASSOCIATION_ID) references QUERY_MODEL_ASSOCIATION (IDENTIFIER);
alter table QUERY_GRAPH_ENTRY add index FKF055E4EA346832A9 (SOURCE_EXPRESSIONID_ID), add constraint FKF055E4EA346832A9 foreign key (SOURCE_EXPRESSIONID_ID) references QUERY_EXPRESSIONID (IDENTIFIER);
alter table QUERY_GRAPH_ENTRY add index FKF055E4EA1C7EBF3B (QUERY_JOIN_GRAPH_ID), add constraint FKF055E4EA1C7EBF3B foreign key (QUERY_JOIN_GRAPH_ID) references QUERY_JOIN_GRAPH (IDENTIFIER);
alter table QUERY_GRAPH_ENTRY add index FKF055E4EAC070901F (TARGET_EXPRESSIONID_ID), add constraint FKF055E4EAC070901F foreign key (TARGET_EXPRESSIONID_ID) references QUERY_EXPRESSIONID (IDENTIFIER);
alter table QUERY_INTER_MODEL_ASSOCIATION add index FKD70658D15F5AB67E (IDENTIFIER), add constraint FKD70658D15F5AB67E foreign key (IDENTIFIER) references QUERY_MODEL_ASSOCIATION (IDENTIFIER);
alter table QUERY_INTRA_MODEL_ASSOCIATION add index FKF1EDBDD35F5AB67E (IDENTIFIER), add constraint FKF1EDBDD35F5AB67E foreign key (IDENTIFIER) references QUERY_MODEL_ASSOCIATION (IDENTIFIER);
alter table QUERY_LOGICAL_CONNECTOR add index FKCF304780180A6E16 (QUERY_EXPRESSION_ID), add constraint FKCF304780180A6E16 foreign key (QUERY_EXPRESSION_ID) references QUERY_EXPRESSION (IDENTIFIER);
alter table QUERY_OUTPUT_ATTRIBUTE add index FK22C9DB75509C0ACD (EXPRESSIONID_ID), add constraint FK22C9DB75509C0ACD foreign key (EXPRESSIONID_ID) references QUERY_EXPRESSIONID (IDENTIFIER);
alter table QUERY_OUTPUT_ATTRIBUTE add index FK22C9DB75604D4BDA (PARAMETERIZED_QUERY_ID), add constraint FK22C9DB75604D4BDA foreign key (PARAMETERIZED_QUERY_ID) references QUERY_PARAMETERIZED_QUERY (IDENTIFIER);
alter table QUERY_PARAMETERIZED_CONDITION add index FK9BE75A3E4B9044D1 (IDENTIFIER), add constraint FK9BE75A3E4B9044D1 foreign key (IDENTIFIER) references QUERY_CONDITION (IDENTIFIER);
alter table QUERY_PARAMETERIZED_QUERY add index FKA272176B76177EFE (IDENTIFIER), add constraint FKA272176B76177EFE foreign key (IDENTIFIER) references QUERY (IDENTIFIER);
alter table QUERY_RULE add index FK14A65033180A6E16 (QUERY_EXPRESSION_ID), add constraint FK14A65033180A6E16 foreign key (QUERY_EXPRESSION_ID) references QUERY_EXPRESSION (IDENTIFIER);
alter table QUERY_RULE add index FK14A6503362E3EDC7 (IDENTIFIER), add constraint FK14A6503362E3EDC7 foreign key (IDENTIFIER) references QUERY_EXPRESSION_OPERAND (IDENTIFIER);
