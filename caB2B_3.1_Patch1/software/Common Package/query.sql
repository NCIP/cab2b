alter table CATEGORIAL_CLASS drop constraint FK9651EF32DBFCB7FC;
alter table CATEGORIAL_CLASS drop constraint FK9651EF32D8D56A33;
alter table QUERY_INTRA_MODEL_ASSOCIATION drop constraint FKF1EDBDD3BC7298A9;
alter table QUERY_CONSTRAINTS drop constraint FKE364FCFFD3C625EA;
alter table QUERY_CONDITION drop constraint FKACCE6246104CA7;
alter table QUERY_RULE drop constraint FK14A6503365F8F4CB;
alter table QUERY_RULE drop constraint FK14A65033BC7298A9;
alter table QUERY drop constraint FK49D20A8251EDC5B;
alter table QUERY_EXPRESSION drop constraint FK1B473A8FCA571190;
alter table QUERY_EXPRESSION drop constraint FK1B473A8F9E19EF66;
alter table QUERY_EXPRESSION drop constraint FK1B473A8F1CF7F689;
alter table QUERY_LOGICAL_CONNECTOR drop constraint FKCF30478065F8F4CB;
alter table QUERY_CONDITION_VALUES drop constraint FK9997379DDA532516;
alter table CATEGORIAL_ATTRIBUTE drop constraint FK31F77B56E8CBD948;
alter table QUERY_INTER_MODEL_ASSOCIATION drop constraint FKD70658D1BC7298A9;
alter table QUERY_EXPRESSION_OPERAND drop constraint FKA3B976F965F8F4CB;
alter table QUERY_GRAPH_ENTRY drop constraint FKF055E4EA955C60E6;
alter table QUERY_GRAPH_ENTRY drop constraint FKF055E4EAD3C625EA;
alter table QUERY_GRAPH_ENTRY drop constraint FKF055E4EA7A5E6479;
alter table QUERY_GRAPH_ENTRY drop constraint FKF055E4EAEE560703;
alter table CATEGORY drop constraint FK31A8ACFE2D0F63E7;
alter table CATEGORY drop constraint FK31A8ACFE211D9A6B;
alter table QUERY_EXPRESSIONID drop constraint FK6662DBEABC7298A9;
drop table CATEGORIAL_CLASS cascade constraints;
drop table QUERY_INTRA_MODEL_ASSOCIATION cascade constraints;
drop table QUERY_CONSTRAINTS cascade constraints;
drop table QUERY_QUERY_ENTITY cascade constraints;
drop table QUERY_CONDITION cascade constraints;
drop table QUERY_RULE cascade constraints;
drop table QUERY cascade constraints;
drop table QUERY_EXPRESSION cascade constraints;
drop table QUERY_LOGICAL_CONNECTOR cascade constraints;
drop table QUERY_MODEL_ASSOCIATION cascade constraints;
drop table QUERY_CONDITION_VALUES cascade constraints;
drop table QUERY_JOIN_GRAPH cascade constraints;
drop table CATEGORIAL_ATTRIBUTE cascade constraints;
drop table QUERY_INTER_MODEL_ASSOCIATION cascade constraints;
drop table QUERY_EXPRESSION_OPERAND cascade constraints;
drop table QUERY_GRAPH_ENTRY cascade constraints;
drop table CATEGORY cascade constraints;
drop table QUERY_EXPRESSIONID cascade constraints;
drop sequence EXPRESSION_OPERAND_SEQ;
drop sequence CONSTRAINT_SEQ;
drop sequence GRAPH_ENTRY_SEQ;
drop sequence JOIN_GRAPH_SEQ;
drop sequence CONDITION_SEQ;
drop sequence LOGICAL_CONNECTOR_SEQ;
drop sequence CATEGORIAL_ATTRIBUTE_SEQ;
drop sequence MODEL_ASSOCIATION_SEQ;
drop sequence CATEGORIAL_CLASS_SEQ;
drop sequence EXPRESSION_SEQ;
drop sequence CATEGORY_SEQ;
drop sequence QUERY_ENTITY_SEQ;
create table CATEGORIAL_CLASS (
   ID number(19,0) not null,
   CATEGORY_ID number(19,0),
   DE_ENTITY_ID number(19,0),
   PATH_FROM_PARENT_ID number(19,0),
   PARENT_CATEGORIAL_CLASS_ID number(19,0),
   primary key (ID)
);
create table QUERY_INTRA_MODEL_ASSOCIATION (
   IDENTIFIER number(19,0) not null,
   DE_ASSOCIATION_ID number(19,0) not null,
   primary key (IDENTIFIER)
);
create table QUERY_CONSTRAINTS (
   IDENTIFIER number(19,0) not null,
   QUERY_JOIN_GRAPH_ID number(19,0) unique,
   primary key (IDENTIFIER)
);
create table QUERY_QUERY_ENTITY (
   IDENTIFIER number(19,0) not null,
   ENTITY_ID number(19,0) not null,
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
create table QUERY_RULE (
   IDENTIFIER number(19,0) not null,
   QUERY_EXPRESSION_ID number(19,0) not null,
   primary key (IDENTIFIER)
);
create table QUERY (
   IDENTIFIER number(19,0) not null,
   QUERY_CONSTRAINTS_ID number(19,0) unique,
   QUERY_NAME varchar2(256) unique,
   DESCRIPTION varchar2(1024),
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
create table QUERY_CONDITION_VALUES (
   QUERY_CONDITION_ID number(19,0) not null,
   VALUE_LIST varchar2(255),
   POSITION number(10,0) not null,
   primary key (QUERY_CONDITION_ID, POSITION)
);
create table QUERY_JOIN_GRAPH (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);
create table CATEGORIAL_ATTRIBUTE (
   ID number(19,0) not null,
   CATEGORIAL_CLASS_ID number(19,0),
   DE_CATEGORY_ATTRIBUTE_ID number(19,0),
   DE_SOURCE_CLASS_ATTRIBUTE_ID number(19,0),
   primary key (ID)
);
create table QUERY_INTER_MODEL_ASSOCIATION (
   IDENTIFIER number(19,0) not null,
   SOURCE_SERVICE_URL varchar2(1000) not null,
   TARGET_SERVICE_URL varchar2(1000) not null,
   SOURCE_ATTRIBUTE_ID number(19,0) not null,
   TARGET_ATTRIBUTE_ID number(19,0) not null,
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
create table CATEGORY (
   ID number(19,0) not null,
   DE_ENTITY_ID number(19,0),
   PARENT_CATEGORY_ID number(19,0),
   ROOT_CATEGORIAL_CLASS_ID number(19,0),
   primary key (ID)
);
create table QUERY_EXPRESSIONID (
   IDENTIFIER number(19,0) not null,
   SUB_EXPRESSION_ID number(10,0) not null,
   primary key (IDENTIFIER)
);
alter table CATEGORIAL_CLASS add constraint FK9651EF32DBFCB7FC foreign key (CATEGORY_ID) references CATEGORY;
alter table CATEGORIAL_CLASS add constraint FK9651EF32D8D56A33 foreign key (PARENT_CATEGORIAL_CLASS_ID) references CATEGORIAL_CLASS;
alter table QUERY_INTRA_MODEL_ASSOCIATION add constraint FKF1EDBDD3BC7298A9 foreign key (IDENTIFIER) references QUERY_MODEL_ASSOCIATION;
alter table QUERY_CONSTRAINTS add constraint FKE364FCFFD3C625EA foreign key (QUERY_JOIN_GRAPH_ID) references QUERY_JOIN_GRAPH;
alter table QUERY_CONDITION add constraint FKACCE6246104CA7 foreign key (QUERY_RULE_ID) references QUERY_RULE;
alter table QUERY_RULE add constraint FK14A6503365F8F4CB foreign key (QUERY_EXPRESSION_ID) references QUERY_EXPRESSION;
alter table QUERY_RULE add constraint FK14A65033BC7298A9 foreign key (IDENTIFIER) references QUERY_EXPRESSION_OPERAND;
alter table QUERY add constraint FK49D20A8251EDC5B foreign key (QUERY_CONSTRAINTS_ID) references QUERY_CONSTRAINTS;
alter table QUERY_EXPRESSION add constraint FK1B473A8FCA571190 foreign key (QUERY_EXPRESSIONID_ID) references QUERY_EXPRESSIONID;
alter table QUERY_EXPRESSION add constraint FK1B473A8F9E19EF66 foreign key (QUERY_CONSTRAINT_ID) references QUERY_CONSTRAINTS;
alter table QUERY_EXPRESSION add constraint FK1B473A8F1CF7F689 foreign key (QUERY_QUERY_ENTITY_ID) references QUERY_QUERY_ENTITY;
alter table QUERY_LOGICAL_CONNECTOR add constraint FKCF30478065F8F4CB foreign key (QUERY_EXPRESSION_ID) references QUERY_EXPRESSION;
alter table QUERY_CONDITION_VALUES add constraint FK9997379DDA532516 foreign key (QUERY_CONDITION_ID) references QUERY_CONDITION;
alter table CATEGORIAL_ATTRIBUTE add constraint FK31F77B56E8CBD948 foreign key (CATEGORIAL_CLASS_ID) references CATEGORIAL_CLASS;
alter table QUERY_INTER_MODEL_ASSOCIATION add constraint FKD70658D1BC7298A9 foreign key (IDENTIFIER) references QUERY_MODEL_ASSOCIATION;
alter table QUERY_EXPRESSION_OPERAND add constraint FKA3B976F965F8F4CB foreign key (QUERY_EXPRESSION_ID) references QUERY_EXPRESSION;
alter table QUERY_GRAPH_ENTRY add constraint FKF055E4EA955C60E6 foreign key (QUERY_MODEL_ASSOCIATION_ID) references QUERY_MODEL_ASSOCIATION;
alter table QUERY_GRAPH_ENTRY add constraint FKF055E4EAD3C625EA foreign key (QUERY_JOIN_GRAPH_ID) references QUERY_JOIN_GRAPH;
alter table QUERY_GRAPH_ENTRY add constraint FKF055E4EA7A5E6479 foreign key (TARGET_EXPRESSIONID_ID) references QUERY_EXPRESSIONID;
alter table QUERY_GRAPH_ENTRY add constraint FKF055E4EAEE560703 foreign key (SOURCE_EXPRESSIONID_ID) references QUERY_EXPRESSIONID;
alter table CATEGORY add constraint FK31A8ACFE2D0F63E7 foreign key (PARENT_CATEGORY_ID) references CATEGORY;
alter table CATEGORY add constraint FK31A8ACFE211D9A6B foreign key (ROOT_CATEGORIAL_CLASS_ID) references CATEGORIAL_CLASS;
alter table QUERY_EXPRESSIONID add constraint FK6662DBEABC7298A9 foreign key (IDENTIFIER) references QUERY_EXPRESSION_OPERAND;
create sequence EXPRESSION_OPERAND_SEQ;
create sequence CONSTRAINT_SEQ;
create sequence GRAPH_ENTRY_SEQ;
create sequence JOIN_GRAPH_SEQ;
create sequence CONDITION_SEQ;
create sequence LOGICAL_CONNECTOR_SEQ;
create sequence CATEGORIAL_ATTRIBUTE_SEQ;
create sequence MODEL_ASSOCIATION_SEQ;
create sequence CATEGORIAL_CLASS_SEQ;
create sequence EXPRESSION_SEQ;
create sequence CATEGORY_SEQ;
create sequence QUERY_ENTITY_SEQ;
