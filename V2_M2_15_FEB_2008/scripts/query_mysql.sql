alter table USER_URL_MAPPING drop foreign key FKC64BBF4AAEC86F2D;
alter table USER_URL_MAPPING drop foreign key FKC64BBF4AB2004842;
drop table if exists SERVICE_URL;
drop table if exists USER;
drop table if exists USER_URL_MAPPING;
create table SERVICE_URL (
   URL_ID bigint not null auto_increment,
   ENTITY_GROUP_NAME text not null,
   URL text not null,
   ADMIN_DEFINED bit not null,
   primary key (URL_ID)
);
create table USER (
   USER_ID bigint not null auto_increment,
   NAME varchar(30) not null,
   PASSWD varchar(30) not null,
   IS_ADMIN bit not null,
   primary key (USER_ID)
);
create table USER_URL_MAPPING (
   SERVICE_URL_ID bigint not null,
   USER_ID bigint not null,
   primary key (USER_ID, SERVICE_URL_ID)
);
alter table USER_URL_MAPPING add index FKC64BBF4AAEC86F2D (USER_ID), add constraint FKC64BBF4AAEC86F2D foreign key (USER_ID) references USER (USER_ID);
alter table USER_URL_MAPPING add index FKC64BBF4AB2004842 (SERVICE_URL_ID), add constraint FKC64BBF4AB2004842 foreign key (SERVICE_URL_ID) references SERVICE_URL (URL_ID);
