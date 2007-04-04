USE cab2b_test
;



--  Drop Tables 
DROP TABLE IF EXISTS AbstractDomainObject
;

DROP TABLE IF EXISTS AdditionalMetaData
;

DROP TABLE IF EXISTS ArrayDesign
;

DROP TABLE IF EXISTS Experiment
;

DROP TABLE IF EXISTS ExperimentGroup
;

DROP TABLE IF EXISTS ExpGrpMapping
;

DROP TABLE IF EXISTS MicroarrayChip
;

DROP TABLE IF EXISTS MicroarrayExperiment
;

DROP TABLE IF EXISTS MicroExpChipMap
;


--  Create Tables 
CREATE TABLE AbstractDomainObject
(
	ADO_ID BIGINT auto_increment,
	ADO_ACTIVITY_STATUS VARCHAR(20),
	PRIMARY KEY (ADO_ID)	
) 
;
CREATE TABLE AdditionalMetaData
(
	AMD_ID BIGINT auto_increment,
	AMD_NAME VARCHAR(50),
	AMD_DESCRIPTION VARCHAR(200),
	AMD_CREATED_ON DATE,
	AMD_LAST_UPDATED_ON DATE,
	PRIMARY KEY (AMD_ID),
	KEY (AMD_ID)
) 
;
CREATE TABLE ArrayDesign
(
	ARD_ID BIGINT auto_increment,
	ARD_NAME VARCHAR(50),
	PRIMARY KEY (ARD_ID),
	KEY (ARD_ID)
) 
;
CREATE TABLE Experiment
(
	EXP_ID BIGINT auto_increment,
	PRIMARY KEY (EXP_ID),
	KEY (EXP_ID)
) 
;
CREATE TABLE ExperimentGroup
(
	EXG_ID BIGINT auto_increment,
	PARENT_EXG_ID BIGINT default NULL,
	PRIMARY KEY (EXG_ID),
	KEY (EXG_ID)
) 
;
CREATE TABLE ExpGrpMapping
(
	IDENTIFIER BIGINT auto_increment,
	EXP_ID BIGINT NOT NULL,
	EXG_ID BIGINT NOT NULL,
	PRIMARY KEY (IDENTIFIER),
	KEY (EXP_ID),
	KEY (EXG_ID)
) 
;
CREATE TABLE MicroarrayChip
(
	MIC_ID BIGINT auto_increment,
	MIC_DESCRIPTION VARCHAR(200),
	MIC_LABEL VARCHAR(50),	
	ARD_ID BIGINT NOT NULL,
	PRIMARY KEY (MIC_ID),
	KEY (MIC_ID),
	KEY (ARD_ID)
) 
;
CREATE TABLE MicroarrayExperiment
(
	MEX_ID BIGINT auto_increment,
	PRIMARY KEY (MEX_ID),
	KEY (MEX_ID)
) 
;
CREATE TABLE MicroExpChipMap
(
	IDENTIFIER BIGINT auto_increment,
	MEX_ID BIGINT NOT NULL,
	MIC_ID BIGINT NOT NULL,
	PRIMARY KEY (IDENTIFIER),
	KEY (MIC_ID),
	KEY (MEX_ID)
) 
;


# ----------- Table for saving data list annontation information ------------
CREATE TABLE DataList
(
	DL_ID BIGINT auto_increment,
	ENT_ID BIGINT DEFAULT NULL,
	KEY (DL_ID)
) 
;


# ----------- Table for saving the many-to-many mapping between experiment and datalist -------------
CREATE TABLE DLExpMap
(
	IDENTIFIER BIGINT auto_increment,
	EXP_ID BIGINT NOT NULL,
	DL_ID BIGINT NOT NULL,
	PRIMARY KEY (IDENTIFIER),
	KEY (EXP_ID),
	KEY (DL_ID)
) 
;


--  Create Foreign Key Constraints 
ALTER TABLE AdditionalMetaData ADD CONSTRAINT FK_AdditionalMetaData_AbstractDomainObject 
	FOREIGN KEY (AMD_ID) REFERENCES AbstractDomainObject (ADO_ID)
;

ALTER TABLE Experiment ADD CONSTRAINT FK_Experiment_AdditionalMetaData 
	FOREIGN KEY (EXP_ID) REFERENCES AdditionalMetaData (AMD_ID)
;

ALTER TABLE ExperimentGroup ADD CONSTRAINT FK_ExperimentGroup_AdditionalMetaData 
	FOREIGN KEY (EXG_ID) REFERENCES AdditionalMetaData (AMD_ID)
;

ALTER TABLE ExpGrpMapping ADD CONSTRAINT FK_ExpGrpMapping_Experiment 
	FOREIGN KEY (EXP_ID) REFERENCES Experiment (EXP_ID)
;

ALTER TABLE ExpGrpMapping ADD CONSTRAINT FK_ExpGrpMapping_ExperimentGroup 
	FOREIGN KEY (EXG_ID) REFERENCES ExperimentGroup (EXG_ID)
;

ALTER TABLE MicroarrayExperiment ADD CONSTRAINT FK_MicroarrayExperiment_Experiment 
	FOREIGN KEY (MEX_ID) REFERENCES Experiment (EXP_ID)
;

ALTER TABLE MicroExpChipMap ADD CONSTRAINT FK_MicroExpChipMap_MicroarrayChip 
	FOREIGN KEY (MIC_ID) REFERENCES MicroarrayChip (MIC_ID)
;


# --------- Constraints corresponding to DataList and Experiment assocation ------------

ALTER TABLE DataList ADD CONSTRAINT FK_DataList_AdditionalMetadata
	FOREIGN KEY (DL_ID) REFERENCES AdditionalMetadata(AMD_ID)
;

ALTER TABLE DLExpMap ADD CONSTRAINT FK_DLMap_Experiment
	FOREIGN KEY (EXP_ID) REFERENCES Experiment(EXP_ID)
;

ALTER TABLE DLExpMap ADD CONSTRAINT FK_DLMap_DataList
	FOREIGN KEY (DL_ID) REFERENCES DataList(DL_ID)
;
