/*L
  Copyright Georgetown University.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cab2b/LICENSE.txt for details.
L*/

insert into CAB2B_ID_TABLE(NEXT_ASSOCIATION_ID) value(1);
insert into CAB2B_user (USER_ID, NAME, PASSWORD, IS_ADMIN) values (1,'Admin','admin123',1);
insert into CAB2B_user (USER_ID, NAME, IS_ADMIN) values (2,'Anonymous',0);
