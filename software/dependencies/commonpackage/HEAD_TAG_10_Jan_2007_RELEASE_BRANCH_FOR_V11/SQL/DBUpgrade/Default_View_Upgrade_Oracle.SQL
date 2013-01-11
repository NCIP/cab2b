/*L
  Copyright Georgetown University.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cab2b/LICENSE.txt for details.
L*/

alter table CATISSUE_SEARCH_DISPLAY_DATA ADD COLUMN DEFAULT_VIEW_ATTRIBUTE number(1,0);
update CATISSUE_SEARCH_DISPLAY_DATA set COLUMN DEFAULT_VIEW_ATTRIBUTE=1;