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
