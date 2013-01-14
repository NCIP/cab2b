/*L
  Copyright Georgetown University.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cab2b/LICENSE.txt for details.
L*/

alter table CAB2B_USER_URL_MAPPING drop foreign key FKC64BBF4AAEC86F2D;
alter table CAB2B_USER_URL_MAPPING drop foreign key FKC64BBF4AB2004842;
drop table if exists CAB2B_SERVICE_URL;
drop table if exists CAB2B_USER;
drop table if exists CAB2B_USER_URL_MAPPING;
create table CAB2B_SERVICE_URL (
   URL_ID bigint not null auto_increment,
   ENTITY_GROUP_NAME text not null,
   URL text not null,
   ADMIN_DEFINED bit not null,
   primary key (URL_ID)
);
create table CAB2B_USER (
   USER_ID bigint not null auto_increment,
   NAME varchar(30) not null,
   PASSWD varchar(30) not null,
   IS_ADMIN bit not null,
   primary key (USER_ID)
);
create table CAB2B_USER_URL_MAPPING (
   SERVICE_URL_ID bigint not null,
   USER_ID bigint not null,
   primary key (USER_ID, SERVICE_URL_ID)
);
alter table CAB2B_USER_URL_MAPPING add index FKC64BBF4AAEC86F2D (USER_ID), add constraint FKC64BBF4AAEC86F2D foreign key (USER_ID) references CAB2B_USER (USER_ID);
alter table CAB2B_USER_URL_MAPPING add index FKC64BBF4AB2004842 (SERVICE_URL_ID), add constraint FKC64BBF4AB2004842 foreign key (SERVICE_URL_ID) references CAB2B_SERVICE_URL (URL_ID);
insert into CAB2B_user (USER_ID, NAME, PASSWD, IS_ADMIN) values (1,'Admin', 'admin123', 1); 
insert into CAB2B_user (USER_ID, NAME, PASSWD, IS_ADMIN) values (2,'cab2bUser', 'cab2b123', 0); 