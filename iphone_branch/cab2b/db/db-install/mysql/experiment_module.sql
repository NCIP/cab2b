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
