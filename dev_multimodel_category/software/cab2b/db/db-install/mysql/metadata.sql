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
   SYSTEM_GENERATED boolean default false not null;	
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
