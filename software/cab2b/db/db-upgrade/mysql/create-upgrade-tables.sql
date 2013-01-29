/*L
  Copyright Georgetown University, Washington University.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cab2b/LICENSE.txt for details.
L*/

--  DROP TABLES 
DROP TABLE IF EXISTS CAB2B_UPGRADE ;


--  CREATE TABLES 
CREATE TABLE CAB2B_UPGRADE ( 
	ID BIGINT COMMENT 'SYSTEM GENERATED PRIMARY KEY.',
	NAME VARCHAR(200),			
	PRIMARY KEY (ID)
)ENGINE=INNODB 
;
