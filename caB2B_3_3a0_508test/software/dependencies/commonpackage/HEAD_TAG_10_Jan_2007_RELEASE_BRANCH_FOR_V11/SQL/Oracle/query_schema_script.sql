drop table ABSTRACT_CATEGORIAL_ATTRIBUTE cascade constraints;
drop table ABSTRACT_CATEGORIAL_CLASS cascade constraints;
drop table ABSTRACT_CATEGORY cascade constraints;
drop table CATEGORIAL_ATTRIBUTE cascade constraints;
drop table CATEGORIAL_CLASS cascade constraints;
drop table CATEGORY cascade constraints;
drop table CATISSUE_AUDIT_EVENT cascade constraints;
drop table CATISSUE_AUDIT_EVENT_DETAILS cascade constraints;
drop table CATISSUE_AUDIT_EVENT_LOG cascade constraints;
drop table CATISSUE_CDE cascade constraints;
drop table CATISSUE_INTERFACE_COLUMN_DATA cascade constraints;
drop table CATISSUE_PERMISSIBLE_VALUE cascade constraints;
drop table CATISSUE_QUERY_TABLE_DATA cascade constraints;
drop table QUERY cascade constraints;
drop table QUERY_CONDITION cascade constraints;
drop table QUERY_CONDITION_VALUES cascade constraints;
drop table QUERY_CONSTRAINTS cascade constraints;
drop table QUERY_EXPRESSION cascade constraints;
drop table QUERY_EXPRESSIONID cascade constraints;
drop table QUERY_EXPRESSION_OPERAND cascade constraints;
drop table QUERY_GRAPH_ENTRY cascade constraints;
drop table QUERY_INTER_MODEL_ASSOCIATION cascade constraints;
drop table QUERY_INTRA_MODEL_ASSOCIATION cascade constraints;
drop table QUERY_JOIN_GRAPH cascade constraints;
drop table QUERY_LOGICAL_CONNECTOR cascade constraints;
drop table QUERY_MODEL_ASSOCIATION cascade constraints;
drop table QUERY_OUTPUT_ATTRIBUTE cascade constraints;
drop table QUERY_PARAMETERIZED_CONDITION cascade constraints;
drop table QUERY_PARAMETERIZED_QUERY cascade constraints;
drop table QUERY_QUERY_ENTITY cascade constraints;
drop table QUERY_RULE cascade constraints;
drop sequence ABSTRACT_CATEGORIAL_ATTRIBUTE_SEQ;
drop sequence ABSTRACT_CATEGORY_SEQ;
drop sequence CATISSUE_AUDIT_EVENT_DET_SEQ;
drop sequence CATISSUE_AUDIT_EVENT_LOG_SEQ;
drop sequence CATISSUE_AUDIT_EVENT_PARAM_SEQ;
drop sequence CATISSUE_INTF_COLUMN_DATA_SEQ;
drop sequence CATISSUE_PERMISSIBLE_VALUE_SEQ;
drop sequence CATISSUE_QUERY_TABLE_DATA_SEQ;
drop sequence CONDITION_SEQ;
drop sequence CONSTRAINT_SEQ;
drop sequence EXPRESSION_OPERAND_SEQ;
drop sequence EXPRESSION_SEQ;
drop sequence GRAPH_ENTRY_SEQ;
drop sequence JOIN_GRAPH_SEQ;
drop sequence LOGICAL_CONNECTOR_SEQ;
drop sequence MODEL_ASSOCIATION_SEQ;
drop sequence OUTPUT_ATTRIBUTE_SEQ;
drop sequence QUERY_ENTITY_SEQ;
create table ABSTRACT_CATEGORIAL_ATTRIBUTE (
   ID number(19,0) not null,
   CATEGORIAL_CLASS_ID number(19,0),
   DE_SOURCE_CLASS_ATTRIBUTE_ID number(19,0),
   ABSTRACT_CATEGORIAL_ATTRIBUTE_ID number(19,0),
   primary key (ID)
);
create table ABSTRACT_CATEGORIAL_CLASS (
   IDENTIFIER number(19,0) not null,
   ABSTRACT_CATEGORY_ID number(19,0),
   PARENT_CATEGORIAL_CLASS_ID number(19,0),
   PATH_FROM_PARENT_ID number(19,0),
   DE_ENTITY_ID number(19,0),
   primary key (IDENTIFIER)
);
create table ABSTRACT_CATEGORY (
   ID number(19,0) not null,
   PARENT_CATEGORY_ID number(19,0) unique,
   primary key (ID)
);
create table CATEGORIAL_ATTRIBUTE (
   ID number(19,0) not null,
   DE_CATEGORY_ATTRIBUTE_ID number(19,0),
   primary key (ID)
);
create table CATEGORIAL_CLASS (
   ID number(19,0) not null,
   primary key (ID)
);
create table CATEGORY (
   ID number(19,0) not null,
   DE_ENTITY_ID number(19,0),
   ROOT_CLASS_ID number(19,0) unique,
   primary key (ID)
);
create table CATISSUE_AUDIT_EVENT (
   IDENTIFIER number(19,0) not null,
   EVENT_TIMESTAMP timestamp,
   USER_ID number(19,0),
   COMMENTS varchar2(500),
   IP_ADDRESS varchar2(20),
   primary key (IDENTIFIER)
);
create table CATISSUE_AUDIT_EVENT_DETAILS (
   IDENTIFIER number(19,0) not null,
   ELEMENT_NAME varchar2(150),
   PREVIOUS_VALUE varchar2(150),
   CURRENT_VALUE varchar2(500),
   AUDIT_EVENT_LOG_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_AUDIT_EVENT_LOG (
   IDENTIFIER number(19,0) not null,
   OBJECT_IDENTIFIER number(19,0),
   OBJECT_NAME varchar2(50),
   EVENT_TYPE varchar2(50),
   AUDIT_EVENT_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_CDE (
   PUBLIC_ID varchar2(30) not null,
   LONG_NAME varchar2(200),
   DEFINITION varchar2(500),
   VERSION varchar2(50),
   LAST_UPDATED timestamp,
   primary key (PUBLIC_ID)
);
create table CATISSUE_INTERFACE_COLUMN_DATA (
   IDENTIFIER number(19,0) not null,
   TABLE_ID number(19,0),
   COLUMN_NAME varchar2(50),
   DISPLAY_NAME varchar2(50),
   primary key (IDENTIFIER)
);
create table CATISSUE_PERMISSIBLE_VALUE (
   IDENTIFIER number(19,0) not null,
   CONCEPT_CODE varchar2(20),
   DEFINITION varchar2(500),
   PARENT_IDENTIFIER number(19,0),
   VALUE varchar2(100),
   PUBLIC_ID varchar2(30),
   primary key (IDENTIFIER)
);
create table CATISSUE_QUERY_TABLE_DATA (
   TABLE_ID number(19,0) not null,
   DISPLAY_NAME varchar2(50),
   TABLE_NAME varchar2(50),
   ALIAS_NAME varchar2(50),
   primary key (TABLE_ID)
);
create table QUERY (
   IDENTIFIER number(19,0) not null,
   QUERY_CONSTRAINTS_ID number(19,0) unique,
   primary key (IDENTIFIER)
);
create table QUERY_CONDITION (
   IDENTIFIER number(19,0) not null,
   ATTRIBUTE_ID number(19,0) not null,
   RELATIONAL_OPERATOR varchar2(255),
   QUERY_RULE_ID number(19,0),
   POSITION number(10,0),
   primary key (IDENTIFIER)
);
create table QUERY_CONDITION_VALUES (
   QUERY_CONDITION_ID number(19,0) not null,
   VALUE_LIST varchar2(255),
   POSITION number(10,0) not null,
   primary key (QUERY_CONDITION_ID, POSITION)
);
create table QUERY_CONSTRAINTS (
   IDENTIFIER number(19,0) not null,
   QUERY_JOIN_GRAPH_ID number(19,0) unique,
   primary key (IDENTIFIER)
);
create table QUERY_EXPRESSION (
   IDENTIFIER number(19,0) not null,
   QUERY_EXPRESSIONID_ID number(19,0) unique,
   QUERY_QUERY_ENTITY_ID number(19,0) not null,
   IS_IN_VIEW number(1,0),
   IS_VISIBLE number(1,0),
   QUERY_CONSTRAINT_ID number(19,0),
   primary key (IDENTIFIER)
);
create table QUERY_EXPRESSIONID (
   IDENTIFIER number(19,0) not null,
   SUB_EXPRESSION_ID number(10,0) not null,
   primary key (IDENTIFIER)
);
create table QUERY_EXPRESSION_OPERAND (
   IDENTIFIER number(19,0) not null,
   QUERY_EXPRESSION_ID number(19,0),
   POSITION number(10,0),
   primary key (IDENTIFIER)
);
create table QUERY_GRAPH_ENTRY (
   IDENTIFIER number(19,0) not null,
   QUERY_MODEL_ASSOCIATION_ID number(19,0),
   SOURCE_EXPRESSIONID_ID number(19,0),
   TARGET_EXPRESSIONID_ID number(19,0),
   QUERY_JOIN_GRAPH_ID number(19,0),
   primary key (IDENTIFIER)
);
create table QUERY_INTER_MODEL_ASSOCIATION (
   IDENTIFIER number(19,0) not null,
   SOURCE_SERVICE_URL varchar2(1000) not null,
   TARGET_SERVICE_URL varchar2(1000) not null,
   SOURCE_ATTRIBUTE_ID number(19,0) not null,
   TARGET_ATTRIBUTE_ID number(19,0) not null,
   primary key (IDENTIFIER)
);
create table QUERY_INTRA_MODEL_ASSOCIATION (
   IDENTIFIER number(19,0) not null,
   DE_ASSOCIATION_ID number(19,0) not null,
   primary key (IDENTIFIER)
);
create table QUERY_JOIN_GRAPH (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);
create table QUERY_LOGICAL_CONNECTOR (
   IDENTIFIER number(19,0) not null,
   LOGICAL_OPERATOR varchar2(255),
   NESTING_NUMBER number(10,0),
   QUERY_EXPRESSION_ID number(19,0),
   POSITION number(10,0),
   primary key (IDENTIFIER)
);
create table QUERY_MODEL_ASSOCIATION (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);
create table QUERY_OUTPUT_ATTRIBUTE (
   IDENTIFIER number(19,0) not null,
   EXPRESSIONID_ID number(19,0),
   ATTRIBUTE_ID number(19,0) not null,
   PARAMETERIZED_QUERY_ID number(19,0),
   POSITION number(10,0),
   primary key (IDENTIFIER)
);
create table QUERY_PARAMETERIZED_CONDITION (
   IDENTIFIER number(19,0) not null,
   CONDITION_INDEX number(10,0),
   CONDITION_NAME varchar2(255),
   primary key (IDENTIFIER)
);
create table QUERY_PARAMETERIZED_QUERY (
   IDENTIFIER number(19,0) not null,
   QUERY_NAME varchar2(255) unique,
   DESCRIPTION varchar2(1024),
   primary key (IDENTIFIER)
);
create table QUERY_QUERY_ENTITY (
   IDENTIFIER number(19,0) not null,
   ENTITY_ID number(19,0) not null,
   primary key (IDENTIFIER)
);
create table QUERY_RULE (
   IDENTIFIER number(19,0) not null,
   QUERY_EXPRESSION_ID number(19,0) not null,
   primary key (IDENTIFIER)
);
alter table ABSTRACT_CATEGORIAL_ATTRIBUTE add constraint FK85397F53A5F024A0 foreign key (CATEGORIAL_CLASS_ID) references ABSTRACT_CATEGORIAL_CLASS;
alter table ABSTRACT_CATEGORIAL_ATTRIBUTE add constraint FK85397F5379C8ACDF foreign key (ABSTRACT_CATEGORIAL_ATTRIBUTE_ID) references ABSTRACT_CATEGORIAL_CLASS;
alter table ABSTRACT_CATEGORIAL_CLASS add constraint FK6C8779AF8168C18B foreign key (PARENT_CATEGORIAL_CLASS_ID) references ABSTRACT_CATEGORIAL_CLASS;
alter table ABSTRACT_CATEGORIAL_CLASS add constraint FK6C8779AF1E10D264 foreign key (ABSTRACT_CATEGORY_ID) references ABSTRACT_CATEGORY;
alter table ABSTRACT_CATEGORY add constraint FK92BB047B70295EEC foreign key (PARENT_CATEGORY_ID) references ABSTRACT_CATEGORY;
alter table CATEGORIAL_ATTRIBUTE add constraint FK31F77B5634ED55B7 foreign key (ID) references ABSTRACT_CATEGORIAL_ATTRIBUTE;
alter table CATEGORIAL_CLASS add constraint FK9651EF32F94A5493 foreign key (ID) references ABSTRACT_CATEGORIAL_CLASS;
alter table CATEGORY add constraint FK31A8ACFEA2330820 foreign key (ID) references ABSTRACT_CATEGORY;
alter table CATEGORY add constraint FK31A8ACFEC88316F9 foreign key (ROOT_CLASS_ID) references CATEGORIAL_CLASS;
alter table CATISSUE_AUDIT_EVENT_DETAILS add constraint FK5C07745DC62F96A4 foreign key (AUDIT_EVENT_LOG_ID) references CATISSUE_AUDIT_EVENT_LOG;
alter table CATISSUE_AUDIT_EVENT_LOG add constraint FK8BB672DFE2182003 foreign key (AUDIT_EVENT_ID) references CATISSUE_AUDIT_EVENT;
alter table CATISSUE_INTERFACE_COLUMN_DATA add constraint FK9C900851F98C179C foreign key (TABLE_ID) references CATISSUE_QUERY_TABLE_DATA;
alter table CATISSUE_PERMISSIBLE_VALUE add constraint FK57DDCE11F55618 foreign key (PARENT_IDENTIFIER) references CATISSUE_PERMISSIBLE_VALUE;
alter table CATISSUE_PERMISSIBLE_VALUE add constraint FK57DDCE1C6D9C7E1 foreign key (PUBLIC_ID) references CATISSUE_CDE;
alter table QUERY add constraint FK49D20A84B0F861E foreign key (QUERY_CONSTRAINTS_ID) references QUERY_CONSTRAINTS;
alter table QUERY_CONDITION add constraint FKACCE6242DCE1896 foreign key (QUERY_RULE_ID) references QUERY_RULE;
alter table QUERY_CONDITION_VALUES add constraint FK9997379D4D1598FE foreign key (QUERY_CONDITION_ID) references QUERY_CONDITION;
alter table QUERY_CONSTRAINTS add constraint FKE364FCFF1C7EBF3B foreign key (QUERY_JOIN_GRAPH_ID) references QUERY_JOIN_GRAPH;
alter table QUERY_EXPRESSION add constraint FK1B473A8F526D4561 foreign key (QUERY_QUERY_ENTITY_ID) references QUERY_QUERY_ENTITY;
alter table QUERY_EXPRESSION add constraint FK1B473A8FCF83E189 foreign key (QUERY_CONSTRAINT_ID) references QUERY_CONSTRAINTS;
alter table QUERY_EXPRESSION add constraint FK1B473A8F3FB1E956 foreign key (QUERY_EXPRESSIONID_ID) references QUERY_EXPRESSIONID;
alter table QUERY_EXPRESSIONID add constraint FK6662DBEA62E3EDC7 foreign key (IDENTIFIER) references QUERY_EXPRESSION_OPERAND;
alter table QUERY_EXPRESSION_OPERAND add constraint FKA3B976F9180A6E16 foreign key (QUERY_EXPRESSION_ID) references QUERY_EXPRESSION;
alter table QUERY_GRAPH_ENTRY add constraint FKF055E4EAB2B42E5B foreign key (QUERY_MODEL_ASSOCIATION_ID) references QUERY_MODEL_ASSOCIATION;
alter table QUERY_GRAPH_ENTRY add constraint FKF055E4EA346832A9 foreign key (SOURCE_EXPRESSIONID_ID) references QUERY_EXPRESSIONID;
alter table QUERY_GRAPH_ENTRY add constraint FKF055E4EA1C7EBF3B foreign key (QUERY_JOIN_GRAPH_ID) references QUERY_JOIN_GRAPH;
alter table QUERY_GRAPH_ENTRY add constraint FKF055E4EAC070901F foreign key (TARGET_EXPRESSIONID_ID) references QUERY_EXPRESSIONID;
alter table QUERY_INTER_MODEL_ASSOCIATION add constraint FKD70658D15F5AB67E foreign key (IDENTIFIER) references QUERY_MODEL_ASSOCIATION;
alter table QUERY_INTRA_MODEL_ASSOCIATION add constraint FKF1EDBDD35F5AB67E foreign key (IDENTIFIER) references QUERY_MODEL_ASSOCIATION;
alter table QUERY_LOGICAL_CONNECTOR add constraint FKCF304780180A6E16 foreign key (QUERY_EXPRESSION_ID) references QUERY_EXPRESSION;
alter table QUERY_OUTPUT_ATTRIBUTE add constraint FK22C9DB75509C0ACD foreign key (EXPRESSIONID_ID) references QUERY_EXPRESSIONID;
alter table QUERY_OUTPUT_ATTRIBUTE add constraint FK22C9DB75604D4BDA foreign key (PARAMETERIZED_QUERY_ID) references QUERY_PARAMETERIZED_QUERY;
alter table QUERY_PARAMETERIZED_CONDITION add constraint FK9BE75A3E4B9044D1 foreign key (IDENTIFIER) references QUERY_CONDITION;
alter table QUERY_PARAMETERIZED_QUERY add constraint FKA272176B76177EFE foreign key (IDENTIFIER) references QUERY;
alter table QUERY_RULE add constraint FK14A65033180A6E16 foreign key (QUERY_EXPRESSION_ID) references QUERY_EXPRESSION;
alter table QUERY_RULE add constraint FK14A6503362E3EDC7 foreign key (IDENTIFIER) references QUERY_EXPRESSION_OPERAND;
create sequence ABSTRACT_CATEGORIAL_ATTRIBUTE_SEQ;
create sequence ABSTRACT_CATEGORY_SEQ;
create sequence CATISSUE_AUDIT_EVENT_DET_SEQ;
create sequence CATISSUE_AUDIT_EVENT_LOG_SEQ;
create sequence CATISSUE_AUDIT_EVENT_PARAM_SEQ;
create sequence CATISSUE_INTF_COLUMN_DATA_SEQ;
create sequence CATISSUE_PERMISSIBLE_VALUE_SEQ;
create sequence CATISSUE_QUERY_TABLE_DATA_SEQ;
create sequence CONDITION_SEQ;
create sequence CONSTRAINT_SEQ;
create sequence EXPRESSION_OPERAND_SEQ;
create sequence EXPRESSION_SEQ;
create sequence GRAPH_ENTRY_SEQ;
create sequence JOIN_GRAPH_SEQ;
create sequence LOGICAL_CONNECTOR_SEQ;
create sequence MODEL_ASSOCIATION_SEQ;
create sequence OUTPUT_ATTRIBUTE_SEQ;
create sequence QUERY_ENTITY_SEQ;
