/*L
  Copyright Georgetown University.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cab2b/LICENSE.txt for details.
L*/

create table cab2b_service_group (
   id bigint not null auto_increment,
   NAME text not null,
   QUERY_IDENTIFIER bigint not null,
   primary key (id)
);
create table cab2b_service_group_item (
   ID bigint not null auto_increment,
   TARGET_OBJECT text not null,
   SERVICE_URL_ID bigint not null,
   SERVICE_GROUP_ID bigint not null,
   primary key (ID)
);

alter table CAB2B_SERVICE_GROUP_ITEM add index FK76343E084DC3D83 (SERVICE_GROUP_ID), add constraint FK76343E084DC3D83 foreign key (SERVICE_GROUP_ID) references CAB2B_SERVICE_GROUP (id);
alter table CAB2B_SERVICE_GROUP_ITEM add index FK76343E08B2004842 (SERVICE_URL_ID), add constraint FK76343E08B2004842 foreign key (SERVICE_URL_ID) references CAB2B_SERVICE_URL (URL_ID);
