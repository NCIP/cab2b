/*L
  Copyright Georgetown University.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cab2b/LICENSE.txt for details.
L*/

create table DE_COLL_ATTR_RECORD_VALUES (
   IDENTIFIER bigint not null auto_increment,
   RECORD_VALUE text,
   COLLECTION_ATTR_RECORD_ID bigint,
   primary key (IDENTIFIER)
);
create table DE_FILE_ATTR_RECORD_VALUES (
   IDENTIFIER bigint not null auto_increment,
   CONTENT_TYPE varchar(255),
   FILE_CONTENT blob,
   FILE_NAME varchar(255),
   RECORD_ID bigint,
   primary key (IDENTIFIER)
);
create table DE_OBJECT_ATTR_RECORD_VALUES (
   IDENTIFIER bigint not null auto_increment,
   CLASS_NAME varchar(255),
   OBJECT_CONTENT blob,
   RECORD_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_ABSTRACT_METADATA (
   IDENTIFIER bigint not null auto_increment,
   CREATED_DATE date,
   DESCRIPTION text,
   LAST_UPDATED date,
   NAME text,
   PUBLIC_ID varchar(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_ASSOCIATION (
   IDENTIFIER bigint not null,
   DIRECTION varchar(255),
   TARGET_ENTITY_ID bigint,
   SOURCE_ROLE_ID bigint,
   TARGET_ROLE_ID bigint,
   IS_SYSTEM_GENERATED bit,
   primary key (IDENTIFIER)
);
create table DYEXTN_ASSO_DISPLAY_ATTR (
   IDENTIFIER bigint not null auto_increment,
   SEQUENCE_NUMBER integer,
   DISPLAY_ATTRIBUTE_ID bigint,
   SELECT_CONTROL_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_ATTRIBUTE (
   IDENTIFIER bigint not null,
   ENTIY_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_ATTRIBUTE_RECORD (
   IDENTIFIER bigint not null auto_increment,
   ENTITY_ID bigint,
   ATTRIBUTE_ID bigint,
   RECORD_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_ATTRIBUTE_TYPE_INFO (
   IDENTIFIER bigint not null auto_increment,
   PRIMITIVE_ATTRIBUTE_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_BARR_CONCEPT_VALUE (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_BOOLEAN_CONCEPT_VALUE (
   IDENTIFIER bigint not null,
   VALUE bit,
   primary key (IDENTIFIER)
);
create table DYEXTN_BOOLEAN_TYPE_INFO (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_BYTE_ARRAY_TYPE_INFO (
   IDENTIFIER bigint not null,
   CONTENT_TYPE varchar(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_CADSRDE (
   IDENTIFIER bigint not null,
   PUBLIC_ID varchar(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_CADSR_VALUE_DOMAIN_INFO (
   IDENTIFIER bigint not null auto_increment,
   DATATYPE varchar(255),
   NAME varchar(255),
   TYPE varchar(255),
   PRIMITIVE_ATTRIBUTE_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_CHECK_BOX (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_COLUMN_PROPERTIES (
   IDENTIFIER bigint not null,
   PRIMITIVE_ATTRIBUTE_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_COMBOBOX (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_CONSTRAINT_PROPERTIES (
   IDENTIFIER bigint not null,
   SOURCE_ENTITY_KEY varchar(255),
   TARGET_ENTITY_KEY varchar(255),
   ASSOCIATION_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_CONTAINER (
   IDENTIFIER bigint not null auto_increment,
   BUTTON_CSS varchar(255),
   CAPTION varchar(255),
   ENTITY_ID bigint,
   MAIN_TABLE_CSS varchar(255),
   REQUIRED_FIELD_INDICATOR varchar(255),
   REQUIRED_FIELD_WARNING_MESSAGE varchar(255),
   TITLE_CSS varchar(255),
   BASE_CONTAINER_ID bigint,
   ENTITY_GROUP_ID bigint,
   VIEW_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_CONTAINMENT_CONTROL (
   IDENTIFIER bigint not null,
   DISPLAY_CONTAINER_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_CONTROL (
   IDENTIFIER bigint not null auto_increment,
   CAPTION varchar(255),
   CSS_CLASS varchar(255),
   HIDDEN bit,
   NAME varchar(255),
   SEQUENCE_NUMBER integer,
   TOOLTIP varchar(255),
   ABSTRACT_ATTRIBUTE_ID bigint,
   CONTAINER_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_DATABASE_PROPERTIES (
   IDENTIFIER bigint not null auto_increment,
   NAME varchar(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_DATA_ELEMENT (
   IDENTIFIER bigint not null auto_increment,
   ATTRIBUTE_TYPE_INFO_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_DATA_GRID (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_DATEPICKER (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_DATE_CONCEPT_VALUE (
   IDENTIFIER bigint not null,
   VALUE datetime,
   primary key (IDENTIFIER)
);
create table DYEXTN_DATE_TYPE_INFO (
   IDENTIFIER bigint not null,
   FORMAT varchar(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_DOUBLE_CONCEPT_VALUE (
   IDENTIFIER bigint not null,
   VALUE double precision,
   primary key (IDENTIFIER)
);
create table DYEXTN_DOUBLE_TYPE_INFO (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_ENTITY (
   IDENTIFIER bigint not null,
   DATA_TABLE_STATE integer,
   IS_ABSTRACT bit,
   PARENT_ENTITY_ID bigint,
   INHERITANCE_STRATEGY integer,
   DISCRIMINATOR_COLUMN_NAME varchar(255),
   DISCRIMINATOR_VALUE varchar(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_ENTITY_GROUP (
   IDENTIFIER bigint not null,
   LONG_NAME varchar(255),
   SHORT_NAME varchar(255),
   VERSION varchar(255),
   IS_SYSTEM_GENERATED bit,
   primary key (IDENTIFIER)
);
create table DYEXTN_ENTITY_GROUP_REL (
   ENTITY_GROUP_ID bigint not null,
   ENTITY_ID bigint not null,
   primary key (ENTITY_ID, ENTITY_GROUP_ID)
);
create table DYEXTN_ENTITY_MAP (
   IDENTIFIER bigint not null auto_increment,
   CONTAINER_ID bigint,
   STATUS varchar(10),
   STATIC_ENTITY_ID bigint,
   CREATED_DATE date,
   CREATED_BY varchar(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_ENTITY_MAP_CONDNS (
   IDENTIFIER bigint not null auto_increment,
   STATIC_RECORD_ID bigint,
   TYPE_ID bigint,
   FORM_CONTEXT_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_ENTITY_MAP_RECORD (
   IDENTIFIER bigint not null auto_increment,
   FORM_CONTEXT_ID bigint,
   STATIC_ENTITY_RECORD_ID bigint,
   STATUS varchar(10),
   DYNAMIC_ENTITY_RECORD_ID bigint,
   MODIFIED_DATE date,
   CREATED_DATE date,
   CREATED_BY varchar(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_FILE_EXTENSIONS (
   IDENTIFIER bigint not null auto_increment,
   FILE_EXTENSION varchar(255),
   ATTRIBUTE_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_FILE_TYPE_INFO (
   IDENTIFIER bigint not null,
   MAX_FILE_SIZE float,
   primary key (IDENTIFIER)
);
create table DYEXTN_FILE_UPLOAD (
   IDENTIFIER bigint not null,
   NO_OF_COLUMNS integer,
   primary key (IDENTIFIER)
);
create table DYEXTN_FLOAT_CONCEPT_VALUE (
   IDENTIFIER bigint not null,
   VALUE float,
   primary key (IDENTIFIER)
);
create table DYEXTN_FLOAT_TYPE_INFO (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_FORM_CONTEXT (
   IDENTIFIER bigint not null auto_increment,
   IS_INFINITE_ENTRY bit,
   ENTITY_MAP_ID bigint,
   STUDY_FORM_LABEL varchar(255),
   NO_OF_ENTRIES integer,
   primary key (IDENTIFIER)
);
create table DYEXTN_INTEGER_CONCEPT_VALUE (
   IDENTIFIER bigint not null,
   VALUE integer,
   primary key (IDENTIFIER)
);
create table DYEXTN_INTEGER_TYPE_INFO (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_LIST_BOX (
   IDENTIFIER bigint not null,
   MULTISELECT bit,
   primary key (IDENTIFIER)
);
create table DYEXTN_LONG_CONCEPT_VALUE (
   IDENTIFIER bigint not null,
   VALUE bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_LONG_TYPE_INFO (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_NUMERIC_TYPE_INFO (
   IDENTIFIER bigint not null,
   MEASUREMENT_UNITS varchar(255),
   DECIMAL_PLACES integer,
   NO_DIGITS integer,
   primary key (IDENTIFIER)
);
create table DYEXTN_OBJECT_TYPE_INFO (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_PERMISSIBLE_VALUE (
   IDENTIFIER bigint not null auto_increment,
   DESCRIPTION varchar(255),
   ATTRIBUTE_TYPE_INFO_ID bigint,
   USER_DEF_DE_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_PRIMITIVE_ATTRIBUTE (
   IDENTIFIER bigint not null,
   IS_COLLECTION bit,
   IS_IDENTIFIED bit,
   IS_PRIMARY_KEY bit,
   IS_NULLABLE bit,
   primary key (IDENTIFIER)
);
create table DYEXTN_RADIOBUTTON (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_ROLE (
   IDENTIFIER bigint not null auto_increment,
   ASSOCIATION_TYPE varchar(255),
   MAX_CARDINALITY integer,
   MIN_CARDINALITY integer,
   NAME varchar(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_RULE (
   IDENTIFIER bigint not null auto_increment,
   NAME varchar(255),
   ATTRIBUTE_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_RULE_PARAMETER (
   IDENTIFIER bigint not null auto_increment,
   NAME varchar(255),
   VALUE varchar(255),
   RULE_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_SELECT_CONTROL (
   IDENTIFIER bigint not null,
   SEPARATOR_STRING varchar(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_SEMANTIC_PROPERTY (
   IDENTIFIER bigint not null auto_increment,
   CONCEPT_CODE varchar(255),
   TERM varchar(255),
   THESAURAS_NAME varchar(255),
   SEQUENCE_NUMBER integer,
   CONCEPT_DEFINITION varchar(1024),
   ABSTRACT_METADATA_ID bigint,
   ABSTRACT_VALUE_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_SHORT_CONCEPT_VALUE (
   IDENTIFIER bigint not null,
   VALUE smallint,
   primary key (IDENTIFIER)
);
create table DYEXTN_SHORT_TYPE_INFO (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_STRING_CONCEPT_VALUE (
   IDENTIFIER bigint not null,
   VALUE varchar(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_STRING_TYPE_INFO (
   IDENTIFIER bigint not null,
   MAX_SIZE integer,
   primary key (IDENTIFIER)
);
create table DYEXTN_TABLE_PROPERTIES (
   IDENTIFIER bigint not null,
   ENTITY_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_TAGGED_VALUE (
   IDENTIFIER bigint not null auto_increment,
   T_KEY varchar(255),
   T_VALUE varchar(255),
   ABSTRACT_METADATA_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_TEXTAREA (
   IDENTIFIER bigint not null,
   TEXTAREA_COLUMNS integer,
   TEXTAREA_ROWS integer,
   primary key (IDENTIFIER)
);
create table DYEXTN_TEXTFIELD (
   IDENTIFIER bigint not null,
   NO_OF_COLUMNS integer,
   IS_PASSWORD bit,
   IS_URL bit,
   primary key (IDENTIFIER)
);
create table DYEXTN_USERDEFINED_DE (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_VIEW (
   IDENTIFIER bigint not null auto_increment,
   NAME varchar(255),
   primary key (IDENTIFIER)
);
alter table DE_COLL_ATTR_RECORD_VALUES add index FK847DA57775255CA5 (COLLECTION_ATTR_RECORD_ID), add constraint FK847DA57775255CA5 foreign key (COLLECTION_ATTR_RECORD_ID) references DYEXTN_ATTRIBUTE_RECORD (IDENTIFIER);
alter table DE_FILE_ATTR_RECORD_VALUES add index FKE68334E74EB991B2 (RECORD_ID), add constraint FKE68334E74EB991B2 foreign key (RECORD_ID) references DYEXTN_ATTRIBUTE_RECORD (IDENTIFIER);
alter table DE_OBJECT_ATTR_RECORD_VALUES add index FK504EADC44EB991B2 (RECORD_ID), add constraint FK504EADC44EB991B2 foreign key (RECORD_ID) references DYEXTN_ATTRIBUTE_RECORD (IDENTIFIER);
alter table DYEXTN_ASSOCIATION add index FK1046842440738A50 (TARGET_ENTITY_ID), add constraint FK1046842440738A50 foreign key (TARGET_ENTITY_ID) references DYEXTN_ENTITY (IDENTIFIER);
alter table DYEXTN_ASSOCIATION add index FK1046842439780F7A (SOURCE_ROLE_ID), add constraint FK1046842439780F7A foreign key (SOURCE_ROLE_ID) references DYEXTN_ROLE (IDENTIFIER);
alter table DYEXTN_ASSOCIATION add index FK104684242BD842F0 (TARGET_ROLE_ID), add constraint FK104684242BD842F0 foreign key (TARGET_ROLE_ID) references DYEXTN_ROLE (IDENTIFIER);
alter table DYEXTN_ASSOCIATION add index FK104684246D19A21F (IDENTIFIER), add constraint FK104684246D19A21F foreign key (IDENTIFIER) references DYEXTN_ATTRIBUTE (IDENTIFIER);
alter table DYEXTN_ASSO_DISPLAY_ATTR add index FKD12FD3827FD29CDD (SELECT_CONTROL_ID), add constraint FKD12FD3827FD29CDD foreign key (SELECT_CONTROL_ID) references DYEXTN_SELECT_CONTROL (IDENTIFIER);
alter table DYEXTN_ASSO_DISPLAY_ATTR add index FKD12FD38235D6E973 (DISPLAY_ATTRIBUTE_ID), add constraint FKD12FD38235D6E973 foreign key (DISPLAY_ATTRIBUTE_ID) references DYEXTN_PRIMITIVE_ATTRIBUTE (IDENTIFIER);
alter table DYEXTN_ATTRIBUTE add index FK37F1E2FFF99EA906 (ENTIY_ID), add constraint FK37F1E2FFF99EA906 foreign key (ENTIY_ID) references DYEXTN_ENTITY (IDENTIFIER);
alter table DYEXTN_ATTRIBUTE add index FK37F1E2FF728B19BE (IDENTIFIER), add constraint FK37F1E2FF728B19BE foreign key (IDENTIFIER) references DYEXTN_ABSTRACT_METADATA (IDENTIFIER);
alter table DYEXTN_ATTRIBUTE_RECORD add index FK9B20ED914AC41F7E (ENTITY_ID), add constraint FK9B20ED914AC41F7E foreign key (ENTITY_ID) references DYEXTN_ENTITY (IDENTIFIER);
alter table DYEXTN_ATTRIBUTE_RECORD add index FK9B20ED914DC2CD16 (ATTRIBUTE_ID), add constraint FK9B20ED914DC2CD16 foreign key (ATTRIBUTE_ID) references DYEXTN_PRIMITIVE_ATTRIBUTE (IDENTIFIER);
alter table DYEXTN_ATTRIBUTE_TYPE_INFO add index FK62596D531333996E (PRIMITIVE_ATTRIBUTE_ID), add constraint FK62596D531333996E foreign key (PRIMITIVE_ATTRIBUTE_ID) references DYEXTN_PRIMITIVE_ATTRIBUTE (IDENTIFIER);
alter table DYEXTN_BARR_CONCEPT_VALUE add index FK89D27DF74641D513 (IDENTIFIER), add constraint FK89D27DF74641D513 foreign key (IDENTIFIER) references DYEXTN_PERMISSIBLE_VALUE (IDENTIFIER);
alter table DYEXTN_BOOLEAN_CONCEPT_VALUE add index FK57B6C4A64641D513 (IDENTIFIER), add constraint FK57B6C4A64641D513 foreign key (IDENTIFIER) references DYEXTN_PERMISSIBLE_VALUE (IDENTIFIER);
alter table DYEXTN_BOOLEAN_TYPE_INFO add index FK28F1809FE5294FA3 (IDENTIFIER), add constraint FK28F1809FE5294FA3 foreign key (IDENTIFIER) references DYEXTN_ATTRIBUTE_TYPE_INFO (IDENTIFIER);
alter table DYEXTN_BYTE_ARRAY_TYPE_INFO add index FK18BDA73E5294FA3 (IDENTIFIER), add constraint FK18BDA73E5294FA3 foreign key (IDENTIFIER) references DYEXTN_ATTRIBUTE_TYPE_INFO (IDENTIFIER);
alter table DYEXTN_CADSRDE add index FK588A250953CC4A77 (IDENTIFIER), add constraint FK588A250953CC4A77 foreign key (IDENTIFIER) references DYEXTN_DATA_ELEMENT (IDENTIFIER);
alter table DYEXTN_CADSR_VALUE_DOMAIN_INFO add index FK1C9AA3641333996E (PRIMITIVE_ATTRIBUTE_ID), add constraint FK1C9AA3641333996E foreign key (PRIMITIVE_ATTRIBUTE_ID) references DYEXTN_PRIMITIVE_ATTRIBUTE (IDENTIFIER);
alter table DYEXTN_CHECK_BOX add index FK4EFF925740F198C2 (IDENTIFIER), add constraint FK4EFF925740F198C2 foreign key (IDENTIFIER) references DYEXTN_CONTROL (IDENTIFIER);
alter table DYEXTN_COLUMN_PROPERTIES add index FK8FCE2B3F1333996E (PRIMITIVE_ATTRIBUTE_ID), add constraint FK8FCE2B3F1333996E foreign key (PRIMITIVE_ATTRIBUTE_ID) references DYEXTN_PRIMITIVE_ATTRIBUTE (IDENTIFIER);
alter table DYEXTN_COLUMN_PROPERTIES add index FK8FCE2B3F3AB6A1D3 (IDENTIFIER), add constraint FK8FCE2B3F3AB6A1D3 foreign key (IDENTIFIER) references DYEXTN_DATABASE_PROPERTIES (IDENTIFIER);
alter table DYEXTN_COMBOBOX add index FKABBC649ABF67AB26 (IDENTIFIER), add constraint FKABBC649ABF67AB26 foreign key (IDENTIFIER) references DYEXTN_SELECT_CONTROL (IDENTIFIER);
alter table DYEXTN_CONSTRAINT_PROPERTIES add index FK82886CD87EE87FF6 (ASSOCIATION_ID), add constraint FK82886CD87EE87FF6 foreign key (ASSOCIATION_ID) references DYEXTN_ASSOCIATION (IDENTIFIER);
alter table DYEXTN_CONSTRAINT_PROPERTIES add index FK82886CD83AB6A1D3 (IDENTIFIER), add constraint FK82886CD83AB6A1D3 foreign key (IDENTIFIER) references DYEXTN_DATABASE_PROPERTIES (IDENTIFIER);
alter table DYEXTN_CONTAINER add index FK1EAB84E44AC41F7E (ENTITY_ID), add constraint FK1EAB84E44AC41F7E foreign key (ENTITY_ID) references DYEXTN_ENTITY (IDENTIFIER);
alter table DYEXTN_CONTAINER add index FK1EAB84E4178865E (VIEW_ID), add constraint FK1EAB84E4178865E foreign key (VIEW_ID) references DYEXTN_VIEW (IDENTIFIER);
alter table DYEXTN_CONTAINER add index FK1EAB84E488C075EF (ENTITY_GROUP_ID), add constraint FK1EAB84E488C075EF foreign key (ENTITY_GROUP_ID) references DYEXTN_ENTITY_GROUP (IDENTIFIER);
alter table DYEXTN_CONTAINER add index FK1EAB84E4BF901C84 (BASE_CONTAINER_ID), add constraint FK1EAB84E4BF901C84 foreign key (BASE_CONTAINER_ID) references DYEXTN_CONTAINER (IDENTIFIER);
alter table DYEXTN_CONTAINMENT_CONTROL add index FK3F9D4AD351A77A33 (DISPLAY_CONTAINER_ID), add constraint FK3F9D4AD351A77A33 foreign key (DISPLAY_CONTAINER_ID) references DYEXTN_CONTAINER (IDENTIFIER);
alter table DYEXTN_CONTAINMENT_CONTROL add index FK3F9D4AD340F198C2 (IDENTIFIER), add constraint FK3F9D4AD340F198C2 foreign key (IDENTIFIER) references DYEXTN_CONTROL (IDENTIFIER);
alter table DYEXTN_CONTROL add index FK70FB5E8069935DD6 (CONTAINER_ID), add constraint FK70FB5E8069935DD6 foreign key (CONTAINER_ID) references DYEXTN_CONTAINER (IDENTIFIER);
alter table DYEXTN_CONTROL add index FK70FB5E807769A811 (ABSTRACT_ATTRIBUTE_ID), add constraint FK70FB5E807769A811 foreign key (ABSTRACT_ATTRIBUTE_ID) references DYEXTN_ATTRIBUTE (IDENTIFIER);
alter table DYEXTN_DATA_ELEMENT add index FKB1153E4AA208204 (ATTRIBUTE_TYPE_INFO_ID), add constraint FKB1153E4AA208204 foreign key (ATTRIBUTE_TYPE_INFO_ID) references DYEXTN_ATTRIBUTE_TYPE_INFO (IDENTIFIER);
alter table DYEXTN_DATA_GRID add index FK233EB73E40F198C2 (IDENTIFIER), add constraint FK233EB73E40F198C2 foreign key (IDENTIFIER) references DYEXTN_CONTROL (IDENTIFIER);
alter table DYEXTN_DATEPICKER add index FKFEADD19940F198C2 (IDENTIFIER), add constraint FKFEADD19940F198C2 foreign key (IDENTIFIER) references DYEXTN_CONTROL (IDENTIFIER);
alter table DYEXTN_DATE_CONCEPT_VALUE add index FK45F598A64641D513 (IDENTIFIER), add constraint FK45F598A64641D513 foreign key (IDENTIFIER) references DYEXTN_PERMISSIBLE_VALUE (IDENTIFIER);
alter table DYEXTN_DATE_TYPE_INFO add index FKFBA549FE5294FA3 (IDENTIFIER), add constraint FKFBA549FE5294FA3 foreign key (IDENTIFIER) references DYEXTN_ATTRIBUTE_TYPE_INFO (IDENTIFIER);
alter table DYEXTN_DOUBLE_CONCEPT_VALUE add index FKB94E64494641D513 (IDENTIFIER), add constraint FKB94E64494641D513 foreign key (IDENTIFIER) references DYEXTN_PERMISSIBLE_VALUE (IDENTIFIER);
alter table DYEXTN_DOUBLE_TYPE_INFO add index FKC83869C2BA4AE008 (IDENTIFIER), add constraint FKC83869C2BA4AE008 foreign key (IDENTIFIER) references DYEXTN_NUMERIC_TYPE_INFO (IDENTIFIER);
alter table DYEXTN_ENTITY add index FK8B2436402264D629 (PARENT_ENTITY_ID), add constraint FK8B2436402264D629 foreign key (PARENT_ENTITY_ID) references DYEXTN_ENTITY (IDENTIFIER);
alter table DYEXTN_ENTITY add index FK8B243640728B19BE (IDENTIFIER), add constraint FK8B243640728B19BE foreign key (IDENTIFIER) references DYEXTN_ABSTRACT_METADATA (IDENTIFIER);
alter table DYEXTN_ENTITY_GROUP add index FK105DE7A0728B19BE (IDENTIFIER), add constraint FK105DE7A0728B19BE foreign key (IDENTIFIER) references DYEXTN_ABSTRACT_METADATA (IDENTIFIER);
alter table DYEXTN_ENTITY_GROUP_REL add index FK5A0D835A4AC41F7E (ENTITY_ID), add constraint FK5A0D835A4AC41F7E foreign key (ENTITY_ID) references DYEXTN_ENTITY (IDENTIFIER);
alter table DYEXTN_ENTITY_GROUP_REL add index FK5A0D835A88C075EF (ENTITY_GROUP_ID), add constraint FK5A0D835A88C075EF foreign key (ENTITY_GROUP_ID) references DYEXTN_ENTITY_GROUP (IDENTIFIER);
alter table DYEXTN_ENTITY_MAP_CONDNS add index FK2A9D6029CFA08B13 (FORM_CONTEXT_ID), add constraint FK2A9D6029CFA08B13 foreign key (FORM_CONTEXT_ID) references DYEXTN_FORM_CONTEXT (IDENTIFIER);
alter table DYEXTN_ENTITY_MAP_RECORD add index FK43A45013CFA08B13 (FORM_CONTEXT_ID), add constraint FK43A45013CFA08B13 foreign key (FORM_CONTEXT_ID) references DYEXTN_FORM_CONTEXT (IDENTIFIER);
alter table DYEXTN_FILE_EXTENSIONS add index FKD49834FA56AF0834 (ATTRIBUTE_ID), add constraint FKD49834FA56AF0834 foreign key (ATTRIBUTE_ID) references DYEXTN_FILE_TYPE_INFO (IDENTIFIER);
alter table DYEXTN_FILE_TYPE_INFO add index FKA00F0EDE5294FA3 (IDENTIFIER), add constraint FKA00F0EDE5294FA3 foreign key (IDENTIFIER) references DYEXTN_ATTRIBUTE_TYPE_INFO (IDENTIFIER);
alter table DYEXTN_FILE_UPLOAD add index FK2FAD41E740F198C2 (IDENTIFIER), add constraint FK2FAD41E740F198C2 foreign key (IDENTIFIER) references DYEXTN_CONTROL (IDENTIFIER);
alter table DYEXTN_FLOAT_CONCEPT_VALUE add index FK6785309A4641D513 (IDENTIFIER), add constraint FK6785309A4641D513 foreign key (IDENTIFIER) references DYEXTN_PERMISSIBLE_VALUE (IDENTIFIER);
alter table DYEXTN_FLOAT_TYPE_INFO add index FK7E1C0693BA4AE008 (IDENTIFIER), add constraint FK7E1C0693BA4AE008 foreign key (IDENTIFIER) references DYEXTN_NUMERIC_TYPE_INFO (IDENTIFIER);
alter table DYEXTN_FORM_CONTEXT add index FKE56CCDB12B784475 (ENTITY_MAP_ID), add constraint FKE56CCDB12B784475 foreign key (ENTITY_MAP_ID) references DYEXTN_ENTITY_MAP (IDENTIFIER);
alter table DYEXTN_INTEGER_CONCEPT_VALUE add index FKFBA33B3C4641D513 (IDENTIFIER), add constraint FKFBA33B3C4641D513 foreign key (IDENTIFIER) references DYEXTN_PERMISSIBLE_VALUE (IDENTIFIER);
alter table DYEXTN_INTEGER_TYPE_INFO add index FK5F9CB235BA4AE008 (IDENTIFIER), add constraint FK5F9CB235BA4AE008 foreign key (IDENTIFIER) references DYEXTN_NUMERIC_TYPE_INFO (IDENTIFIER);
alter table DYEXTN_LIST_BOX add index FK208395A7BF67AB26 (IDENTIFIER), add constraint FK208395A7BF67AB26 foreign key (IDENTIFIER) references DYEXTN_SELECT_CONTROL (IDENTIFIER);
alter table DYEXTN_LONG_CONCEPT_VALUE add index FK3E1A6EF44641D513 (IDENTIFIER), add constraint FK3E1A6EF44641D513 foreign key (IDENTIFIER) references DYEXTN_PERMISSIBLE_VALUE (IDENTIFIER);
alter table DYEXTN_LONG_TYPE_INFO add index FK257281EDBA4AE008 (IDENTIFIER), add constraint FK257281EDBA4AE008 foreign key (IDENTIFIER) references DYEXTN_NUMERIC_TYPE_INFO (IDENTIFIER);
alter table DYEXTN_NUMERIC_TYPE_INFO add index FK4DEC9544E5294FA3 (IDENTIFIER), add constraint FK4DEC9544E5294FA3 foreign key (IDENTIFIER) references DYEXTN_ATTRIBUTE_TYPE_INFO (IDENTIFIER);
alter table DYEXTN_OBJECT_TYPE_INFO add index FK74819FB0E5294FA3 (IDENTIFIER), add constraint FK74819FB0E5294FA3 foreign key (IDENTIFIER) references DYEXTN_ATTRIBUTE_TYPE_INFO (IDENTIFIER);
alter table DYEXTN_PERMISSIBLE_VALUE add index FK136264E0AA208204 (ATTRIBUTE_TYPE_INFO_ID), add constraint FK136264E0AA208204 foreign key (ATTRIBUTE_TYPE_INFO_ID) references DYEXTN_ATTRIBUTE_TYPE_INFO (IDENTIFIER);
alter table DYEXTN_PERMISSIBLE_VALUE add index FK136264E05521B106 (USER_DEF_DE_ID), add constraint FK136264E05521B106 foreign key (USER_DEF_DE_ID) references DYEXTN_USERDEFINED_DE (IDENTIFIER);
alter table DYEXTN_PRIMITIVE_ATTRIBUTE add index FKA9F765C76D19A21F (IDENTIFIER), add constraint FKA9F765C76D19A21F foreign key (IDENTIFIER) references DYEXTN_ATTRIBUTE (IDENTIFIER);
alter table DYEXTN_RADIOBUTTON add index FK16F5BA9040F198C2 (IDENTIFIER), add constraint FK16F5BA9040F198C2 foreign key (IDENTIFIER) references DYEXTN_CONTROL (IDENTIFIER);
alter table DYEXTN_RULE add index FKC27E09990F96714 (ATTRIBUTE_ID), add constraint FKC27E09990F96714 foreign key (ATTRIBUTE_ID) references DYEXTN_ATTRIBUTE (IDENTIFIER);
alter table DYEXTN_RULE_PARAMETER add index FK2256736395D4A5AE (RULE_ID), add constraint FK2256736395D4A5AE foreign key (RULE_ID) references DYEXTN_RULE (IDENTIFIER);
alter table DYEXTN_SELECT_CONTROL add index FKDFEBB65740F198C2 (IDENTIFIER), add constraint FKDFEBB65740F198C2 foreign key (IDENTIFIER) references DYEXTN_CONTROL (IDENTIFIER);
alter table DYEXTN_SEMANTIC_PROPERTY add index FKD2A0B5B15EB60E90 (ABSTRACT_VALUE_ID), add constraint FKD2A0B5B15EB60E90 foreign key (ABSTRACT_VALUE_ID) references DYEXTN_PERMISSIBLE_VALUE (IDENTIFIER);
alter table DYEXTN_SEMANTIC_PROPERTY add index FKD2A0B5B19AEB0CA3 (ABSTRACT_METADATA_ID), add constraint FKD2A0B5B19AEB0CA3 foreign key (ABSTRACT_METADATA_ID) references DYEXTN_ABSTRACT_METADATA (IDENTIFIER);
alter table DYEXTN_SHORT_CONCEPT_VALUE add index FKC1945ABA4641D513 (IDENTIFIER), add constraint FKC1945ABA4641D513 foreign key (IDENTIFIER) references DYEXTN_PERMISSIBLE_VALUE (IDENTIFIER);
alter table DYEXTN_SHORT_TYPE_INFO add index FK99540B3BA4AE008 (IDENTIFIER), add constraint FK99540B3BA4AE008 foreign key (IDENTIFIER) references DYEXTN_NUMERIC_TYPE_INFO (IDENTIFIER);
alter table DYEXTN_STRING_CONCEPT_VALUE add index FKADE7D8894641D513 (IDENTIFIER), add constraint FKADE7D8894641D513 foreign key (IDENTIFIER) references DYEXTN_PERMISSIBLE_VALUE (IDENTIFIER);
alter table DYEXTN_STRING_TYPE_INFO add index FKDA35FE02E5294FA3 (IDENTIFIER), add constraint FKDA35FE02E5294FA3 foreign key (IDENTIFIER) references DYEXTN_ATTRIBUTE_TYPE_INFO (IDENTIFIER);
alter table DYEXTN_TABLE_PROPERTIES add index FKE608E0814AC41F7E (ENTITY_ID), add constraint FKE608E0814AC41F7E foreign key (ENTITY_ID) references DYEXTN_ENTITY (IDENTIFIER);
alter table DYEXTN_TABLE_PROPERTIES add index FKE608E0813AB6A1D3 (IDENTIFIER), add constraint FKE608E0813AB6A1D3 foreign key (IDENTIFIER) references DYEXTN_DATABASE_PROPERTIES (IDENTIFIER);
alter table DYEXTN_TAGGED_VALUE add index FKF79D055B9AEB0CA3 (ABSTRACT_METADATA_ID), add constraint FKF79D055B9AEB0CA3 foreign key (ABSTRACT_METADATA_ID) references DYEXTN_ABSTRACT_METADATA (IDENTIFIER);
alter table DYEXTN_TEXTAREA add index FK946EE25740F198C2 (IDENTIFIER), add constraint FK946EE25740F198C2 foreign key (IDENTIFIER) references DYEXTN_CONTROL (IDENTIFIER);
alter table DYEXTN_TEXTFIELD add index FKF9AFC85040F198C2 (IDENTIFIER), add constraint FKF9AFC85040F198C2 foreign key (IDENTIFIER) references DYEXTN_CONTROL (IDENTIFIER);
alter table DYEXTN_USERDEFINED_DE add index FK630761FF53CC4A77 (IDENTIFIER), add constraint FK630761FF53CC4A77 foreign key (IDENTIFIER) references DYEXTN_DATA_ELEMENT (IDENTIFIER);

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
alter table ABSTRACT_CATEGORIAL_ATTRIBUTE add index FK85397F53A5F024A0 (CATEGORIAL_CLASS_ID), add constraint FK85397F53A5F024A0 foreign key (CATEGORIAL_CLASS_ID) references ABSTRACT_CATEGORIAL_CLASS (IDENTIFIER);
alter table ABSTRACT_CATEGORIAL_ATTRIBUTE add index FK85397F5379C8ACDF (ABSTRACT_CATEGORIAL_ATTRIBUTE_ID), add constraint FK85397F5379C8ACDF foreign key (ABSTRACT_CATEGORIAL_ATTRIBUTE_ID) references ABSTRACT_CATEGORIAL_CLASS (IDENTIFIER);
alter table ABSTRACT_CATEGORIAL_CLASS add index FK6C8779AF8168C18B (PARENT_CATEGORIAL_CLASS_ID), add constraint FK6C8779AF8168C18B foreign key (PARENT_CATEGORIAL_CLASS_ID) references ABSTRACT_CATEGORIAL_CLASS (IDENTIFIER);
alter table ABSTRACT_CATEGORIAL_CLASS add index FK6C8779AF1E10D264 (ABSTRACT_CATEGORY_ID), add constraint FK6C8779AF1E10D264 foreign key (ABSTRACT_CATEGORY_ID) references ABSTRACT_CATEGORY (ID);
alter table ABSTRACT_CATEGORY add index FK92BB047B70295EEC (PARENT_CATEGORY_ID), add constraint FK92BB047B70295EEC foreign key (PARENT_CATEGORY_ID) references ABSTRACT_CATEGORY (ID);
alter table CATEGORIAL_ATTRIBUTE add index FK31F77B5634ED55B7 (ID), add constraint FK31F77B5634ED55B7 foreign key (ID) references ABSTRACT_CATEGORIAL_ATTRIBUTE (ID);
alter table CATEGORIAL_CLASS add index FK9651EF32F94A5493 (ID), add constraint FK9651EF32F94A5493 foreign key (ID) references ABSTRACT_CATEGORIAL_CLASS (IDENTIFIER);
alter table CATEGORY add index FK31A8ACFEA2330820 (ID), add constraint FK31A8ACFEA2330820 foreign key (ID) references ABSTRACT_CATEGORY (ID);
alter table CATEGORY add index FK31A8ACFEC88316F9 (ROOT_CLASS_ID), add constraint FK31A8ACFEC88316F9 foreign key (ROOT_CLASS_ID) references CATEGORIAL_CLASS (ID);

create table COMMONS_GRAPH (IDENTIFIER bigint not null auto_increment, primary key (IDENTIFIER));
create table COMMONS_GRAPH_EDGE (IDENTIFIER bigint not null auto_increment, SOURCE_VERTEX_CLASS varchar(255), SOURCE_VERTEX_ID bigint, TARGET_VERTEX_CLASS varchar(255), TARGET_VERTEX_ID bigint, EDGE_CLASS varchar(255), EDGE_ID bigint, primary key (IDENTIFIER));
create table COMMONS_GRAPH_TO_EDGES (GRAPH_ID bigint not null, EDGE_ID bigint not null unique, primary key (GRAPH_ID, EDGE_ID));
create table COMMONS_GRAPH_TO_VERTICES (GRAPH_ID bigint not null, VERTEX_CLASS varchar(255), VERTEX_ID bigint);
create table QUERY (IDENTIFIER bigint not null, CONSTRAINTS_ID bigint unique, primary key (IDENTIFIER));
create table QUERY_ABSTRACT_QUERY (IDENTIFIER bigint not null auto_increment, QUERY_NAME varchar(255) unique, QUERY_TYPE varchar(30), DESCRIPTION text, CREATED_DATE datetime not null, CREATED_BY bigint not null, primary key (IDENTIFIER));
create table QUERY_ARITHMETIC_OPERAND (IDENTIFIER bigint not null, LITERAL varchar(255), TERM_TYPE varchar(255), DATE_LITERAL date, TIME_INTERVAL varchar(255), DE_ATTRIBUTE_ID bigint, EXPRESSION_ID bigint, primary key (IDENTIFIER));
create table QUERY_BASEEXPR_TO_CONNECTORS (BASE_EXPRESSION_ID bigint not null, CONNECTOR_ID bigint not null, POSITION integer not null, primary key (BASE_EXPRESSION_ID, POSITION));
create table QUERY_BASE_EXPRESSION (IDENTIFIER bigint not null auto_increment, EXPR_TYPE varchar(255) not null, primary key (IDENTIFIER));
create table QUERY_BASE_EXPR_OPND (BASE_EXPRESSION_ID bigint not null, OPERAND_ID bigint not null, POSITION integer not null, primary key (BASE_EXPRESSION_ID, POSITION));
create table QUERY_COMPOSITE_QUERY (IDENTIFIER bigint not null, OPERATION_ID bigint, primary key (IDENTIFIER));
create table QUERY_CONDITION (IDENTIFIER bigint not null auto_increment, ATTRIBUTE_ID bigint not null, RELATIONAL_OPERATOR varchar(255), primary key (IDENTIFIER));
create table QUERY_CONDITION_VALUES (CONDITION_ID bigint not null, VALUE varchar(255), POSITION integer not null, primary key (CONDITION_ID, POSITION));
create table QUERY_CONNECTOR (IDENTIFIER bigint not null auto_increment, OPERATOR varchar(255), NESTING_NUMBER integer, primary key (IDENTIFIER));
create table QUERY_CONSTRAINTS (IDENTIFIER bigint not null auto_increment, QUERY_JOIN_GRAPH_ID bigint unique, primary key (IDENTIFIER));
create table QUERY_CONSTRAINT_TO_EXPR (CONSTRAINT_ID bigint not null, EXPRESSION_ID bigint not null unique, primary key (CONSTRAINT_ID, EXPRESSION_ID));
create table QUERY_COUNT_VIEW (IDENTIFIER bigint not null, COUNT_ENTITY_ID bigint, primary key (IDENTIFIER));
create table QUERY_CUSTOM_FORMULA (IDENTIFIER bigint not null, OPERATOR varchar(255), LHS_TERM_ID bigint, primary key (IDENTIFIER));
create table QUERY_DATA_VIEW (IDENTIFIER bigint not null, primary key (IDENTIFIER));
create table QUERY_EXPRESSION (IDENTIFIER bigint not null, IS_IN_VIEW BOOLEAN, IS_VISIBLE BOOLEAN, UI_EXPR_ID integer, QUERY_ENTITY_ID bigint, primary key (IDENTIFIER));
create table QUERY_FORMULA_RHS (CUSTOM_FORMULA_ID bigint not null, RHS_TERM_ID bigint not null, POSITION integer not null, primary key (CUSTOM_FORMULA_ID, POSITION));
create table QUERY_INTERSECTION (IDENTIFIER bigint not null, primary key (IDENTIFIER));
create table QUERY_INTER_MODEL_ASSOCIATION (IDENTIFIER bigint not null, SOURCE_SERVICE_URL varchar(255) not null, TARGET_SERVICE_URL varchar(255) not null, SOURCE_ATTRIBUTE_ID bigint not null, TARGET_ATTRIBUTE_ID bigint not null, primary key (IDENTIFIER));
create table QUERY_INTRA_MODEL_ASSOCIATION (IDENTIFIER bigint not null, DE_ASSOCIATION_ID bigint not null, primary key (IDENTIFIER));
create table QUERY_JOIN_GRAPH (IDENTIFIER bigint not null auto_increment, COMMONS_GRAPH_ID bigint, primary key (IDENTIFIER));
create table QUERY_MINUS (IDENTIFIER bigint not null, primary key (IDENTIFIER));
create table QUERY_MODEL_ASSOCIATION (IDENTIFIER bigint not null auto_increment, primary key (IDENTIFIER));
create table QUERY_OPERAND (IDENTIFIER bigint not null auto_increment, OPND_TYPE varchar(255) not null, primary key (IDENTIFIER));
create table QUERY_OPERATION (IDENTIFIER bigint not null auto_increment, OPERAND_ONE bigint, OPERAND_TWO bigint, primary key (IDENTIFIER));
create table QUERY_OUTPUT_ATTRIBUTE (IDENTIFIER bigint not null auto_increment, EXPRESSION_ID bigint, ATTRIBUTE_ID bigint not null, PARAMETERIZED_QUERY_ID bigint, POSITION integer, DATA_VIEW_ID bigint, primary key (IDENTIFIER));
create table QUERY_OUTPUT_TERM (IDENTIFIER bigint not null auto_increment, NAME varchar(255), TIME_INTERVAL varchar(255), TERM_ID bigint, primary key (IDENTIFIER));
create table QUERY_PARAMETER (IDENTIFIER bigint not null auto_increment, NAME varchar(255), OBJECT_CLASS varchar(255), OBJECT_ID bigint, primary key (IDENTIFIER));
create table QUERY_PARAMETERIZED_QUERY (IDENTIFIER bigint not null, primary key (IDENTIFIER));
create table QUERY_QUERY_ENTITY (IDENTIFIER bigint not null auto_increment, ENTITY_ID bigint not null, primary key (IDENTIFIER));
create table QUERY_RESULT_VIEW (IDENTIFIER bigint not null auto_increment, primary key (IDENTIFIER));
create table QUERY_RULE_COND (RULE_ID bigint not null, CONDITION_ID bigint not null, POSITION integer not null, primary key (RULE_ID, POSITION));
create table QUERY_SUBEXPR_OPERAND (IDENTIFIER bigint not null, EXPRESSION_ID bigint, primary key (IDENTIFIER));
create table QUERY_TO_OUTPUT_TERMS (QUERY_ID bigint not null, OUTPUT_TERM_ID bigint not null unique, POSITION integer not null, primary key (QUERY_ID, POSITION));
create table QUERY_TO_PARAMETERS (QUERY_ID bigint not null, PARAMETER_ID bigint not null unique, POSITION integer not null, primary key (QUERY_ID, POSITION));
create table QUERY_UNION (IDENTIFIER bigint not null, primary key (IDENTIFIER));
alter table COMMONS_GRAPH_TO_EDGES add index FKA6B0D8BAA0494B1D (GRAPH_ID), add constraint FKA6B0D8BAA0494B1D foreign key (GRAPH_ID) references COMMONS_GRAPH (IDENTIFIER);
alter table COMMONS_GRAPH_TO_EDGES add index FKA6B0D8BAFAEF80D (EDGE_ID), add constraint FKA6B0D8BAFAEF80D foreign key (EDGE_ID) references COMMONS_GRAPH_EDGE (IDENTIFIER);
alter table COMMONS_GRAPH_TO_VERTICES add index FK2C4412F5A0494B1D (GRAPH_ID), add constraint FK2C4412F5A0494B1D foreign key (GRAPH_ID) references COMMONS_GRAPH (IDENTIFIER);
alter table QUERY add index FK49D20A886AD86FC (IDENTIFIER), add constraint FK49D20A886AD86FC foreign key (IDENTIFIER) references QUERY_ABSTRACT_QUERY (IDENTIFIER);
alter table QUERY add index FK49D20A89E2FD9C7 (CONSTRAINTS_ID), add constraint FK49D20A89E2FD9C7 foreign key (CONSTRAINTS_ID) references QUERY_CONSTRAINTS (IDENTIFIER);
alter table QUERY_ARITHMETIC_OPERAND add index FK262AEB0BE92C814D (EXPRESSION_ID), add constraint FK262AEB0BE92C814D foreign key (EXPRESSION_ID) references QUERY_BASE_EXPRESSION (IDENTIFIER);
alter table QUERY_ARITHMETIC_OPERAND add index FK262AEB0BD635BD31 (IDENTIFIER), add constraint FK262AEB0BD635BD31 foreign key (IDENTIFIER) references QUERY_OPERAND (IDENTIFIER);
alter table QUERY_ARITHMETIC_OPERAND add index FK262AEB0B96C7CE5A (IDENTIFIER), add constraint FK262AEB0B96C7CE5A foreign key (IDENTIFIER) references QUERY_OPERAND (IDENTIFIER);
alter table QUERY_ARITHMETIC_OPERAND add index FK262AEB0BD006BE44 (IDENTIFIER), add constraint FK262AEB0BD006BE44 foreign key (IDENTIFIER) references QUERY_OPERAND (IDENTIFIER);
alter table QUERY_ARITHMETIC_OPERAND add index FK262AEB0B7223B197 (IDENTIFIER), add constraint FK262AEB0B7223B197 foreign key (IDENTIFIER) references QUERY_OPERAND (IDENTIFIER);
alter table QUERY_ARITHMETIC_OPERAND add index FK262AEB0B687BE69E (IDENTIFIER), add constraint FK262AEB0B687BE69E foreign key (IDENTIFIER) references QUERY_OPERAND (IDENTIFIER);
alter table QUERY_BASEEXPR_TO_CONNECTORS add index FK3F0043482FCE1DA7 (CONNECTOR_ID), add constraint FK3F0043482FCE1DA7 foreign key (CONNECTOR_ID) references QUERY_CONNECTOR (IDENTIFIER);
alter table QUERY_BASEEXPR_TO_CONNECTORS add index FK3F00434848BA6890 (BASE_EXPRESSION_ID), add constraint FK3F00434848BA6890 foreign key (BASE_EXPRESSION_ID) references QUERY_BASE_EXPRESSION (IDENTIFIER);
alter table QUERY_BASE_EXPR_OPND add index FKAE67EAF0712A4C (OPERAND_ID), add constraint FKAE67EAF0712A4C foreign key (OPERAND_ID) references QUERY_OPERAND (IDENTIFIER);
alter table QUERY_BASE_EXPR_OPND add index FKAE67EA48BA6890 (BASE_EXPRESSION_ID), add constraint FKAE67EA48BA6890 foreign key (BASE_EXPRESSION_ID) references QUERY_BASE_EXPRESSION (IDENTIFIER);
alter table QUERY_COMPOSITE_QUERY add index FKD453833986AD86FC (IDENTIFIER), add constraint FKD453833986AD86FC foreign key (IDENTIFIER) references QUERY_ABSTRACT_QUERY (IDENTIFIER);
alter table QUERY_COMPOSITE_QUERY add index FKD453833932224F67 (OPERATION_ID), add constraint FKD453833932224F67 foreign key (OPERATION_ID) references QUERY_OPERATION (IDENTIFIER);
alter table QUERY_CONDITION_VALUES add index FK9997379D6458C2E7 (CONDITION_ID), add constraint FK9997379D6458C2E7 foreign key (CONDITION_ID) references QUERY_CONDITION (IDENTIFIER);
alter table QUERY_CONSTRAINTS add index FKE364FCFF1C7EBF3B (QUERY_JOIN_GRAPH_ID), add constraint FKE364FCFF1C7EBF3B foreign key (QUERY_JOIN_GRAPH_ID) references QUERY_JOIN_GRAPH (IDENTIFIER);
alter table QUERY_CONSTRAINT_TO_EXPR add index FK2BD705CEE92C814D (EXPRESSION_ID), add constraint FK2BD705CEE92C814D foreign key (EXPRESSION_ID) references QUERY_BASE_EXPRESSION (IDENTIFIER);
alter table QUERY_CONSTRAINT_TO_EXPR add index FK2BD705CEA0A5F4C0 (CONSTRAINT_ID), add constraint FK2BD705CEA0A5F4C0 foreign key (CONSTRAINT_ID) references QUERY_CONSTRAINTS (IDENTIFIER);
alter table QUERY_COUNT_VIEW add index FK4A5C8BECF17325F (COUNT_ENTITY_ID), add constraint FK4A5C8BECF17325F foreign key (COUNT_ENTITY_ID) references QUERY_QUERY_ENTITY (IDENTIFIER);
alter table QUERY_COUNT_VIEW add index FK4A5C8BEC89DB039E (IDENTIFIER), add constraint FK4A5C8BEC89DB039E foreign key (IDENTIFIER) references QUERY_RESULT_VIEW (IDENTIFIER);
alter table QUERY_CUSTOM_FORMULA add index FK5C0EEAEFBE674D45 (LHS_TERM_ID), add constraint FK5C0EEAEFBE674D45 foreign key (LHS_TERM_ID) references QUERY_BASE_EXPRESSION (IDENTIFIER);
alter table QUERY_CUSTOM_FORMULA add index FK5C0EEAEF12D455EB (IDENTIFIER), add constraint FK5C0EEAEF12D455EB foreign key (IDENTIFIER) references QUERY_OPERAND (IDENTIFIER);
alter table QUERY_DATA_VIEW add index FK2A3EA74389DB039E (IDENTIFIER), add constraint FK2A3EA74389DB039E foreign key (IDENTIFIER) references QUERY_RESULT_VIEW (IDENTIFIER);
alter table QUERY_EXPRESSION add index FK1B473A8F635766D8 (QUERY_ENTITY_ID), add constraint FK1B473A8F635766D8 foreign key (QUERY_ENTITY_ID) references QUERY_QUERY_ENTITY (IDENTIFIER);
alter table QUERY_EXPRESSION add index FK1B473A8F40EB75D4 (IDENTIFIER), add constraint FK1B473A8F40EB75D4 foreign key (IDENTIFIER) references QUERY_BASE_EXPRESSION (IDENTIFIER);
alter table QUERY_FORMULA_RHS add index FKAE90F94D9A0B7164 (CUSTOM_FORMULA_ID), add constraint FKAE90F94D9A0B7164 foreign key (CUSTOM_FORMULA_ID) references QUERY_OPERAND (IDENTIFIER);
alter table QUERY_FORMULA_RHS add index FKAE90F94D3BC37DCB (RHS_TERM_ID), add constraint FKAE90F94D3BC37DCB foreign key (RHS_TERM_ID) references QUERY_BASE_EXPRESSION (IDENTIFIER);
alter table QUERY_INTERSECTION add index FK2C1FACC0E201AD1D (IDENTIFIER), add constraint FK2C1FACC0E201AD1D foreign key (IDENTIFIER) references QUERY_OPERATION (IDENTIFIER);
alter table QUERY_INTER_MODEL_ASSOCIATION add index FKD70658D15F5AB67E (IDENTIFIER), add constraint FKD70658D15F5AB67E foreign key (IDENTIFIER) references QUERY_MODEL_ASSOCIATION (IDENTIFIER);
alter table QUERY_INTRA_MODEL_ASSOCIATION add index FKF1EDBDD35F5AB67E (IDENTIFIER), add constraint FKF1EDBDD35F5AB67E foreign key (IDENTIFIER) references QUERY_MODEL_ASSOCIATION (IDENTIFIER);
alter table QUERY_JOIN_GRAPH add index FK2B41B5D09DBC4D94 (COMMONS_GRAPH_ID), add constraint FK2B41B5D09DBC4D94 foreign key (COMMONS_GRAPH_ID) references COMMONS_GRAPH (IDENTIFIER);
alter table QUERY_MINUS add index FK7FD7D5F9E201AD1D (IDENTIFIER), add constraint FK7FD7D5F9E201AD1D foreign key (IDENTIFIER) references QUERY_OPERATION (IDENTIFIER);
alter table QUERY_OPERATION add index FKA13E4E70E4553443 (OPERAND_ONE), add constraint FKA13E4E70E4553443 foreign key (OPERAND_ONE) references QUERY_ABSTRACT_QUERY (IDENTIFIER);
alter table QUERY_OPERATION add index FKA13E4E70E4554829 (OPERAND_TWO), add constraint FKA13E4E70E4554829 foreign key (OPERAND_TWO) references QUERY_ABSTRACT_QUERY (IDENTIFIER);
alter table QUERY_OUTPUT_ATTRIBUTE add index FK22C9DB75E92C814D (EXPRESSION_ID), add constraint FK22C9DB75E92C814D foreign key (EXPRESSION_ID) references QUERY_BASE_EXPRESSION (IDENTIFIER);
alter table QUERY_OUTPUT_ATTRIBUTE add index FK22C9DB75F961BE22 (DATA_VIEW_ID), add constraint FK22C9DB75F961BE22 foreign key (DATA_VIEW_ID) references QUERY_DATA_VIEW (IDENTIFIER);
alter table QUERY_OUTPUT_ATTRIBUTE add index FK22C9DB75604D4BDA (PARAMETERIZED_QUERY_ID), add constraint FK22C9DB75604D4BDA foreign key (PARAMETERIZED_QUERY_ID) references QUERY_PARAMETERIZED_QUERY (IDENTIFIER);
alter table QUERY_OUTPUT_TERM add index FK13C8A3D388C86B0D (TERM_ID), add constraint FK13C8A3D388C86B0D foreign key (TERM_ID) references QUERY_BASE_EXPRESSION (IDENTIFIER);
alter table QUERY_PARAMETERIZED_QUERY add index FKA272176B76177EFE (IDENTIFIER), add constraint FKA272176B76177EFE foreign key (IDENTIFIER) references QUERY (IDENTIFIER);
alter table QUERY_RULE_COND add index FKC32D37AE6458C2E7 (CONDITION_ID), add constraint FKC32D37AE6458C2E7 foreign key (CONDITION_ID) references QUERY_CONDITION (IDENTIFIER);
alter table QUERY_RULE_COND add index FKC32D37AE39F0A10D (RULE_ID), add constraint FKC32D37AE39F0A10D foreign key (RULE_ID) references QUERY_OPERAND (IDENTIFIER);
alter table QUERY_SUBEXPR_OPERAND add index FK2BF760E8E92C814D (EXPRESSION_ID), add constraint FK2BF760E8E92C814D foreign key (EXPRESSION_ID) references QUERY_BASE_EXPRESSION (IDENTIFIER);
alter table QUERY_SUBEXPR_OPERAND add index FK2BF760E832E875C8 (IDENTIFIER), add constraint FK2BF760E832E875C8 foreign key (IDENTIFIER) references QUERY_OPERAND (IDENTIFIER);
alter table QUERY_TO_OUTPUT_TERMS add index FK8A70E25691051647 (QUERY_ID), add constraint FK8A70E25691051647 foreign key (QUERY_ID) references QUERY (IDENTIFIER);
alter table QUERY_TO_OUTPUT_TERMS add index FK8A70E2565E5B9430 (OUTPUT_TERM_ID), add constraint FK8A70E2565E5B9430 foreign key (OUTPUT_TERM_ID) references QUERY_OUTPUT_TERM (IDENTIFIER);
alter table QUERY_TO_PARAMETERS add index FK8060DAD739F0A314 (QUERY_ID), add constraint FK8060DAD739F0A314 foreign key (QUERY_ID) references QUERY_PARAMETERIZED_QUERY (IDENTIFIER);
alter table QUERY_TO_PARAMETERS add index FK8060DAD7F84B9027 (PARAMETER_ID), add constraint FK8060DAD7F84B9027 foreign key (PARAMETER_ID) references QUERY_PARAMETER (IDENTIFIER);
alter table QUERY_UNION add index FK804AC458E201AD1D (IDENTIFIER), add constraint FK804AC458E201AD1D foreign key (IDENTIFIER) references QUERY_OPERATION (IDENTIFIER);

create table CAB2B_ADDITIONAL_META_DATA (
   AMD_ID bigint not null,
   NAME varchar(50),
   DESCRIPTION varchar(255),
   CREATED_ON date,
   LAST_UPDATED_ON date,
   USER_ID bigint not null,
   primary key (AMD_ID)
);
create table CAB2B_ABSTRACT_DOMAIN_OBJECT (
   ADO_ID bigint not null auto_increment,
   ADO_ACTIVITY_STATUS varchar(50),
   primary key (ADO_ID)
);
create table CAB2B_DATA_LIST (
   DL_ID bigint not null,
   CUSTOM_DATA_CATEGORY bit,
   ROOT_ENTITY_ID bigint,
   primary key (DL_ID)
);
create table CAB2B_DATALIST_ENTITY (
   DATALIST_METADATA_ID bigint not null,
   ENTITY_ID bigint not null,
   primary key (DATALIST_METADATA_ID, ENTITY_ID)
);
create table CAB2B_EXPERIMENT (
   EXP_ID bigint not null,
   primary key (EXP_ID)
);
create table CAB2B_EXP_GRP_MAPPING (
   EXG_ID bigint not null,
   EXP_ID bigint not null,
   primary key (EXP_ID, EXG_ID)
);
create table CAB2B_EXPERIMENT_GROUP (
   EXG_ID bigint not null,
   PARENT_EXG_ID bigint,
   primary key (EXG_ID)
);
create table CAB2B_EXP_DLMETADATA_MAPPING (
   EXP_ID bigint not null,
   DL_ID bigint not null,
   primary key (EXP_ID, DL_ID)
);
alter table CAB2B_ADDITIONAL_META_DATA add index FKC025F1F773486922 (AMD_ID), add constraint FKC025F1F773486922 foreign key (AMD_ID) references CAB2B_ABSTRACT_DOMAIN_OBJECT (ADO_ID);
alter table CAB2B_DATA_LIST add index FK789CBE883E23832 (DL_ID), add constraint FK789CBE883E23832 foreign key (DL_ID) references CAB2B_ADDITIONAL_META_DATA (AMD_ID);
alter table CAB2B_DATALIST_ENTITY add index FK96B3076FD1F8DDB4 (DATALIST_METADATA_ID), add constraint FK96B3076FD1F8DDB4 foreign key (DATALIST_METADATA_ID) references CAB2B_DATA_LIST (DL_ID);
alter table CAB2B_EXPERIMENT add index FKFF270C287ABC429D (EXP_ID), add constraint FKFF270C287ABC429D foreign key (EXP_ID) references CAB2B_ADDITIONAL_META_DATA (AMD_ID);
alter table CAB2B_EXP_GRP_MAPPING add index FK1154C2A77AB82B46 (EXG_ID), add constraint FK1154C2A77AB82B46 foreign key (EXG_ID) references CAB2B_EXPERIMENT_GROUP (EXG_ID);
alter table CAB2B_EXP_GRP_MAPPING add index FK1154C2A77ABC429D (EXP_ID), add constraint FK1154C2A77ABC429D foreign key (EXP_ID) references CAB2B_EXPERIMENT (EXP_ID);
alter table CAB2B_EXPERIMENT_GROUP add index FK7AD2AF8864FE787B (PARENT_EXG_ID), add constraint FK7AD2AF8864FE787B foreign key (PARENT_EXG_ID) references CAB2B_EXPERIMENT_GROUP (EXG_ID);
alter table CAB2B_EXPERIMENT_GROUP add index FK7AD2AF887AB82B46 (EXG_ID), add constraint FK7AD2AF887AB82B46 foreign key (EXG_ID) references CAB2B_ADDITIONAL_META_DATA (AMD_ID);
alter table CAB2B_EXP_DLMETADATA_MAPPING add index FK223A61533E23832 (DL_ID), add constraint FK223A61533E23832 foreign key (DL_ID) references CAB2B_DATA_LIST (DL_ID);
alter table CAB2B_EXP_DLMETADATA_MAPPING add index FK223A61537ABC429D (EXP_ID), add constraint FK223A61537ABC429D foreign key (EXP_ID) references CAB2B_EXPERIMENT (EXP_ID);
create table PATH(
     PATH_ID           bigint         not null auto_increment,
     FIRST_ENTITY_ID   bigint         null,
     INTERMEDIATE_PATH varchar(1000)  null,
     LAST_ENTITY_ID    bigint         null,
     primary key (PATH_ID),
     index INDEX1 (FIRST_ENTITY_ID,LAST_ENTITY_ID)
);
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
	MODEL_GROUP_NAME varchar(255) unique not null,
	SECURED bit null,
	DESCRIPTION text null,
	ENTITY_GROUP_NAMES text not null,
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
