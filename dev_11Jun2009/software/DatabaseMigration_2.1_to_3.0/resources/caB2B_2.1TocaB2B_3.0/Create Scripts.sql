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

create table COMMONS_GRAPH (
IDENTIFIER bigint not null auto_increment,
primary key (IDENTIFIER)
);

create table COMMONS_GRAPH_EDGE (
IDENTIFIER bigint not null auto_increment,
SOURCE_VERTEX_CLASS varchar(255),
SOURCE_VERTEX_ID bigint,
TARGET_VERTEX_CLASS varchar(255),
TARGET_VERTEX_ID bigint,
EDGE_CLASS varchar(255),
EDGE_ID bigint,
primary key (IDENTIFIER)
);

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