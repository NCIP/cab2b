/*L
  Copyright Georgetown University.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cab2b/LICENSE.txt for details.
L*/

ALTER TABLE DE_FILE_ATTR_RECORD_VALUES MODIFY FILE_CONTENT LONGBLOB;
ALTER TABLE DE_OBJECT_ATTR_RECORD_VALUES MODIFY OBJECT_CONTENT LONGBLOB;