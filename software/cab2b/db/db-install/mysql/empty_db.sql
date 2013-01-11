/*L
  Copyright Georgetown University.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cab2b/LICENSE.txt for details.
L*/

-- MySQL dump 10.11
--
-- Host: localhost    Database: cab2b
-- ------------------------------------------------------
-- Server version	5.0.45-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `CAB2B_SERVICE_GROUP_ITEM`
--

DROP TABLE IF EXISTS `CAB2B_SERVICE_GROUP_ITEM`;
CREATE TABLE `CAB2B_SERVICE_GROUP_ITEM` (
  `ID` bigint(20) NOT NULL auto_increment,
  `TARGET_OBJECT` text NOT NULL,
  `SERVICE_URL_ID` bigint(20) NOT NULL,
  `SERVICE_GROUP_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK76343E084DC3D83` (`SERVICE_GROUP_ID`),
  KEY `FK76343E08B2004842` (`SERVICE_URL_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `CAB2B_SERVICE_GROUP_ITEM`
--

LOCK TABLES `CAB2B_SERVICE_GROUP_ITEM` WRITE;
/*!40000 ALTER TABLE `CAB2B_SERVICE_GROUP_ITEM` DISABLE KEYS */;
/*!40000 ALTER TABLE `CAB2B_SERVICE_GROUP_ITEM` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `abstract_categorial_attribute`
--

DROP TABLE IF EXISTS `abstract_categorial_attribute`;
CREATE TABLE `abstract_categorial_attribute` (
  `ID` bigint(20) NOT NULL auto_increment,
  `CATEGORIAL_CLASS_ID` bigint(20) default NULL,
  `DE_SOURCE_CLASS_ATTRIBUTE_ID` bigint(20) default NULL,
  `ABSTRACT_CATEGORIAL_ATTRIBUTE_ID` bigint(20) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK85397F53A5F024A0` (`CATEGORIAL_CLASS_ID`),
  KEY `FK85397F5379C8ACDF` (`ABSTRACT_CATEGORIAL_ATTRIBUTE_ID`),
  CONSTRAINT `FK85397F5379C8ACDF` FOREIGN KEY (`ABSTRACT_CATEGORIAL_ATTRIBUTE_ID`) REFERENCES `abstract_categorial_class` (`IDENTIFIER`),
  CONSTRAINT `FK85397F53A5F024A0` FOREIGN KEY (`CATEGORIAL_CLASS_ID`) REFERENCES `abstract_categorial_class` (`IDENTIFIER`)
) ENGINE=InnoDB AUTO_INCREMENT=1746 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `abstract_categorial_attribute`
--

LOCK TABLES `abstract_categorial_attribute` WRITE;
/*!40000 ALTER TABLE `abstract_categorial_attribute` DISABLE KEYS */;
/*!40000 ALTER TABLE `abstract_categorial_attribute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `abstract_categorial_class`
--

DROP TABLE IF EXISTS `abstract_categorial_class`;
CREATE TABLE `abstract_categorial_class` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `ABSTRACT_CATEGORY_ID` bigint(20) default NULL,
  `PARENT_CATEGORIAL_CLASS_ID` bigint(20) default NULL,
  `PATH_FROM_PARENT_ID` bigint(20) default NULL,
  `DE_ENTITY_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK6C8779AF8168C18B` (`PARENT_CATEGORIAL_CLASS_ID`),
  KEY `FK6C8779AF1E10D264` (`ABSTRACT_CATEGORY_ID`),
  CONSTRAINT `FK6C8779AF1E10D264` FOREIGN KEY (`ABSTRACT_CATEGORY_ID`) REFERENCES `abstract_category` (`ID`),
  CONSTRAINT `FK6C8779AF8168C18B` FOREIGN KEY (`PARENT_CATEGORIAL_CLASS_ID`) REFERENCES `abstract_categorial_class` (`IDENTIFIER`)
) ENGINE=InnoDB AUTO_INCREMENT=594 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `abstract_categorial_class`
--

LOCK TABLES `abstract_categorial_class` WRITE;
/*!40000 ALTER TABLE `abstract_categorial_class` DISABLE KEYS */;
/*!40000 ALTER TABLE `abstract_categorial_class` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `abstract_category`
--

DROP TABLE IF EXISTS `abstract_category`;
CREATE TABLE `abstract_category` (
  `ID` bigint(20) NOT NULL auto_increment,
  `PARENT_CATEGORY_ID` bigint(20) default NULL,
  `SYSTEM_GENERATED` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `PARENT_CATEGORY_ID` (`PARENT_CATEGORY_ID`),
  KEY `FK92BB047B70295EEC` (`PARENT_CATEGORY_ID`),
  CONSTRAINT `FK92BB047B70295EEC` FOREIGN KEY (`PARENT_CATEGORY_ID`) REFERENCES `abstract_category` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=119 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `abstract_category`
--

LOCK TABLES `abstract_category` WRITE;
/*!40000 ALTER TABLE `abstract_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `abstract_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `association`
--

DROP TABLE IF EXISTS `association`;
CREATE TABLE `association` (
  `ASSOCIATION_ID` bigint(20) NOT NULL,
  `ASSOCIATION_TYPE` int(8) NOT NULL,
  PRIMARY KEY  (`ASSOCIATION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `association`
--

LOCK TABLES `association` WRITE;
/*!40000 ALTER TABLE `association` DISABLE KEYS */;
/*!40000 ALTER TABLE `association` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cab2b_abstract_domain_object`
--

DROP TABLE IF EXISTS `cab2b_abstract_domain_object`;
CREATE TABLE `cab2b_abstract_domain_object` (
  `ADO_ID` bigint(20) NOT NULL auto_increment,
  `ADO_ACTIVITY_STATUS` varchar(50) default NULL,
  PRIMARY KEY  (`ADO_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=94 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_abstract_domain_object`
--

LOCK TABLES `cab2b_abstract_domain_object` WRITE;
/*!40000 ALTER TABLE `cab2b_abstract_domain_object` DISABLE KEYS */;
/*!40000 ALTER TABLE `cab2b_abstract_domain_object` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cab2b_abstract_status`
--

DROP TABLE IF EXISTS `cab2b_abstract_status`;
CREATE TABLE `cab2b_abstract_status` (
  `ID` bigint(20) NOT NULL auto_increment,
  `STATUS` varchar(100) NOT NULL,
  `RESULT_COUNT` int(11) default NULL,
  `MESSAGE` varchar(255) default NULL,
  `DESCRIPTION` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6897 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_abstract_status`
--

LOCK TABLES `cab2b_abstract_status` WRITE;
/*!40000 ALTER TABLE `cab2b_abstract_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `cab2b_abstract_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cab2b_additional_meta_data`
--

DROP TABLE IF EXISTS `cab2b_additional_meta_data`;
CREATE TABLE `cab2b_additional_meta_data` (
  `AMD_ID` bigint(20) NOT NULL,
  `NAME` varchar(50) default NULL,
  `DESCRIPTION` varchar(255) default NULL,
  `CREATED_ON` date default NULL,
  `LAST_UPDATED_ON` date default NULL,
  `USER_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`AMD_ID`),
  KEY `FKC025F1F773486922` (`AMD_ID`),
  CONSTRAINT `FKC025F1F773486922` FOREIGN KEY (`AMD_ID`) REFERENCES `cab2b_abstract_domain_object` (`ADO_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_additional_meta_data`
--

LOCK TABLES `cab2b_additional_meta_data` WRITE;
/*!40000 ALTER TABLE `cab2b_additional_meta_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `cab2b_additional_meta_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cab2b_category_popularity`
--

DROP TABLE IF EXISTS `cab2b_category_popularity`;
CREATE TABLE `cab2b_category_popularity` (
  `IDENTIFIER` bigint(30) NOT NULL auto_increment,
  `ENTITY_ID` bigint(30) default NULL,
  `POPULARITY` bigint(30) NOT NULL default '0',
  `UPDATED_DATE` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=InnoDB AUTO_INCREMENT=208 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_category_popularity`
--

LOCK TABLES `cab2b_category_popularity` WRITE;
/*!40000 ALTER TABLE `cab2b_category_popularity` DISABLE KEYS */;
/*!40000 ALTER TABLE `cab2b_category_popularity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cab2b_compound_query`
--

DROP TABLE IF EXISTS `cab2b_compound_query`;
CREATE TABLE `cab2b_compound_query` (
  `IDENTIFIER` bigint(20) NOT NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK4DB047BF17568730` (`IDENTIFIER`),
  CONSTRAINT `FK4DB047BF17568730` FOREIGN KEY (`IDENTIFIER`) REFERENCES `cab2b_query` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_compound_query`
--

LOCK TABLES `cab2b_compound_query` WRITE;
/*!40000 ALTER TABLE `cab2b_compound_query` DISABLE KEYS */;
/*!40000 ALTER TABLE `cab2b_compound_query` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cab2b_data_category`
--

DROP TABLE IF EXISTS `cab2b_data_category`;
CREATE TABLE `cab2b_data_category` (
  `ID` bigint(20) NOT NULL,
  `description` varchar(255) default NULL,
  `name` varchar(255) default NULL,
  `ROOT_CLASS_ID` bigint(20) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `name` (`name`),
  UNIQUE KEY `ROOT_CLASS_ID` (`ROOT_CLASS_ID`),
  KEY `FKFA70BDE8A2330820` (`ID`),
  KEY `FKFA70BDE8DF75106F` (`ROOT_CLASS_ID`),
  CONSTRAINT `FKFA70BDE8A2330820` FOREIGN KEY (`ID`) REFERENCES `abstract_category` (`ID`),
  CONSTRAINT `FKFA70BDE8DF75106F` FOREIGN KEY (`ROOT_CLASS_ID`) REFERENCES `data_categorial_class` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_data_category`
--

LOCK TABLES `cab2b_data_category` WRITE;
/*!40000 ALTER TABLE `cab2b_data_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `cab2b_data_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cab2b_data_list`
--

DROP TABLE IF EXISTS `cab2b_data_list`;
CREATE TABLE `cab2b_data_list` (
  `DL_ID` bigint(20) NOT NULL,
  `CUSTOM_DATA_CATEGORY` bit(1) default NULL,
  `ROOT_ENTITY_ID` bigint(20) default NULL,
  PRIMARY KEY  (`DL_ID`),
  KEY `FK789CBE883E23832` (`DL_ID`),
  CONSTRAINT `FK789CBE883E23832` FOREIGN KEY (`DL_ID`) REFERENCES `cab2b_additional_meta_data` (`AMD_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_data_list`
--

LOCK TABLES `cab2b_data_list` WRITE;
/*!40000 ALTER TABLE `cab2b_data_list` DISABLE KEYS */;
/*!40000 ALTER TABLE `cab2b_data_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cab2b_datalist_entity`
--

DROP TABLE IF EXISTS `cab2b_datalist_entity`;
CREATE TABLE `cab2b_datalist_entity` (
  `DATALIST_METADATA_ID` bigint(20) NOT NULL,
  `ENTITY_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`DATALIST_METADATA_ID`,`ENTITY_ID`),
  KEY `FK96B3076FD1F8DDB4` (`DATALIST_METADATA_ID`),
  CONSTRAINT `FK96B3076FD1F8DDB4` FOREIGN KEY (`DATALIST_METADATA_ID`) REFERENCES `cab2b_data_list` (`DL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_datalist_entity`
--

LOCK TABLES `cab2b_datalist_entity` WRITE;
/*!40000 ALTER TABLE `cab2b_datalist_entity` DISABLE KEYS */;
/*!40000 ALTER TABLE `cab2b_datalist_entity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cab2b_exp_dlmetadata_mapping`
--

DROP TABLE IF EXISTS `cab2b_exp_dlmetadata_mapping`;
CREATE TABLE `cab2b_exp_dlmetadata_mapping` (
  `EXP_ID` bigint(20) NOT NULL,
  `DL_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`EXP_ID`,`DL_ID`),
  KEY `FK223A61533E23832` (`DL_ID`),
  KEY `FK223A61537ABC429D` (`EXP_ID`),
  CONSTRAINT `FK223A61533E23832` FOREIGN KEY (`DL_ID`) REFERENCES `cab2b_data_list` (`DL_ID`),
  CONSTRAINT `FK223A61537ABC429D` FOREIGN KEY (`EXP_ID`) REFERENCES `cab2b_experiment` (`EXP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_exp_dlmetadata_mapping`
--

LOCK TABLES `cab2b_exp_dlmetadata_mapping` WRITE;
/*!40000 ALTER TABLE `cab2b_exp_dlmetadata_mapping` DISABLE KEYS */;
/*!40000 ALTER TABLE `cab2b_exp_dlmetadata_mapping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cab2b_exp_grp_mapping`
--

DROP TABLE IF EXISTS `cab2b_exp_grp_mapping`;
CREATE TABLE `cab2b_exp_grp_mapping` (
  `EXG_ID` bigint(20) NOT NULL,
  `EXP_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`EXP_ID`,`EXG_ID`),
  KEY `FK1154C2A77AB82B46` (`EXG_ID`),
  KEY `FK1154C2A77ABC429D` (`EXP_ID`),
  CONSTRAINT `FK1154C2A77AB82B46` FOREIGN KEY (`EXG_ID`) REFERENCES `cab2b_experiment_group` (`EXG_ID`),
  CONSTRAINT `FK1154C2A77ABC429D` FOREIGN KEY (`EXP_ID`) REFERENCES `cab2b_experiment` (`EXP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_exp_grp_mapping`
--

LOCK TABLES `cab2b_exp_grp_mapping` WRITE;
/*!40000 ALTER TABLE `cab2b_exp_grp_mapping` DISABLE KEYS */;
/*!40000 ALTER TABLE `cab2b_exp_grp_mapping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cab2b_experiment`
--

DROP TABLE IF EXISTS `cab2b_experiment`;
CREATE TABLE `cab2b_experiment` (
  `EXP_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`EXP_ID`),
  KEY `FKFF270C287ABC429D` (`EXP_ID`),
  CONSTRAINT `FKFF270C287ABC429D` FOREIGN KEY (`EXP_ID`) REFERENCES `cab2b_additional_meta_data` (`AMD_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_experiment`
--

LOCK TABLES `cab2b_experiment` WRITE;
/*!40000 ALTER TABLE `cab2b_experiment` DISABLE KEYS */;
/*!40000 ALTER TABLE `cab2b_experiment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cab2b_experiment_group`
--

DROP TABLE IF EXISTS `cab2b_experiment_group`;
CREATE TABLE `cab2b_experiment_group` (
  `EXG_ID` bigint(20) NOT NULL,
  `PARENT_EXG_ID` bigint(20) default NULL,
  PRIMARY KEY  (`EXG_ID`),
  KEY `FK7AD2AF8864FE787B` (`PARENT_EXG_ID`),
  KEY `FK7AD2AF887AB82B46` (`EXG_ID`),
  CONSTRAINT `FK7AD2AF8864FE787B` FOREIGN KEY (`PARENT_EXG_ID`) REFERENCES `cab2b_experiment_group` (`EXG_ID`),
  CONSTRAINT `FK7AD2AF887AB82B46` FOREIGN KEY (`EXG_ID`) REFERENCES `cab2b_additional_meta_data` (`AMD_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_experiment_group`
--

LOCK TABLES `cab2b_experiment_group` WRITE;
/*!40000 ALTER TABLE `cab2b_experiment_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `cab2b_experiment_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cab2b_id_table`
--

DROP TABLE IF EXISTS `cab2b_id_table`;
CREATE TABLE `cab2b_id_table` (
  `NEXT_ASSOCIATION_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`NEXT_ASSOCIATION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_id_table`
--

LOCK TABLES `cab2b_id_table` WRITE;
/*!40000 ALTER TABLE `cab2b_id_table` DISABLE KEYS */;
/*!40000 ALTER TABLE `cab2b_id_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cab2b_keyword_query`
--

DROP TABLE IF EXISTS `cab2b_keyword_query`;
CREATE TABLE `cab2b_keyword_query` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `APP_GROUP_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK9A5BAA79A9FD6A2` (`APP_GROUP_ID`),
  KEY `FK9A5BAA7BC19CC53` (`IDENTIFIER`),
  CONSTRAINT `FK9A5BAA79A9FD6A2` FOREIGN KEY (`APP_GROUP_ID`) REFERENCES `cab2b_model_group` (`MODEL_ID`),
  CONSTRAINT `FK9A5BAA7BC19CC53` FOREIGN KEY (`IDENTIFIER`) REFERENCES `cab2b_compound_query` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_keyword_query`
--

LOCK TABLES `cab2b_keyword_query` WRITE;
/*!40000 ALTER TABLE `cab2b_keyword_query` DISABLE KEYS */;
/*!40000 ALTER TABLE `cab2b_keyword_query` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cab2b_mma`
--

DROP TABLE IF EXISTS `cab2b_mma`;
CREATE TABLE `cab2b_mma` (
  `ID` bigint(20) NOT NULL auto_increment,
  `IS_VISIBLE` tinyint(1) default NULL,
  `ATTRIBUTE_ID` bigint(20) NOT NULL,
  `CATEGORIAL_ATTRIBUTE_IDS` varchar(254) NOT NULL,
  `MMC_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK2A9170D69EDCC3F8` (`MMC_ID`),
  CONSTRAINT `FK2A9170D69EDCC3F8` FOREIGN KEY (`MMC_ID`) REFERENCES `cab2b_mmc` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=287 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_mma`
--

LOCK TABLES `cab2b_mma` WRITE;
/*!40000 ALTER TABLE `cab2b_mma` DISABLE KEYS */;
/*!40000 ALTER TABLE `cab2b_mma` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cab2b_mmc`
--

DROP TABLE IF EXISTS `cab2b_mmc`;
CREATE TABLE `cab2b_mmc` (
  `ID` bigint(20) NOT NULL auto_increment,
  `ENTITY_ID` bigint(20) NOT NULL,
  `APP_GROUP_ID` bigint(20) NOT NULL,
  `CATEGORY_IDS` varchar(254) NOT NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK2A9170D89A9FD6A2` (`APP_GROUP_ID`),
  CONSTRAINT `FK2A9170D89A9FD6A2` FOREIGN KEY (`APP_GROUP_ID`) REFERENCES `cab2b_model_group` (`MODEL_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_mmc`
--

LOCK TABLES `cab2b_mmc` WRITE;
/*!40000 ALTER TABLE `cab2b_mmc` DISABLE KEYS */;
/*!40000 ALTER TABLE `cab2b_mmc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cab2b_mmc_query`
--

DROP TABLE IF EXISTS `cab2b_mmc_query`;
CREATE TABLE `cab2b_mmc_query` (
  `IDENTIFIER` bigint(20) NOT NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK3CB8F281BC19CC53` (`IDENTIFIER`),
  CONSTRAINT `FK3CB8F281BC19CC53` FOREIGN KEY (`IDENTIFIER`) REFERENCES `cab2b_compound_query` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_mmc_query`
--

LOCK TABLES `cab2b_mmc_query` WRITE;
/*!40000 ALTER TABLE `cab2b_mmc_query` DISABLE KEYS */;
/*!40000 ALTER TABLE `cab2b_mmc_query` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cab2b_model_group`
--

DROP TABLE IF EXISTS `cab2b_model_group`;
CREATE TABLE `cab2b_model_group` (
  `MODEL_ID` bigint(20) NOT NULL auto_increment,
  `MODEL_GROUP_NAME` varchar(255) NOT NULL,
  `SECURED` tinyint(1) default NULL,
  `DESCRIPTION` text,
  `ENTITY_GROUP_NAMES` text NOT NULL,
  PRIMARY KEY  (`MODEL_ID`),
  UNIQUE KEY `MODEL_GROUP_NAME` (`MODEL_GROUP_NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_model_group`
--

LOCK TABLES `cab2b_model_group` WRITE;
/*!40000 ALTER TABLE `cab2b_model_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `cab2b_model_group` ENABLE KEYS */;
UNLOCK TABLES;

INSERT INTO `cab2b_model_group` VALUES (1,'Microarray Data',0,NULL,''),(2,'Imaging Data',0,NULL,''),(3,'Biospecimen Data',1,NULL,''),(4,'Nanoparticle Data',0,'model group for caNanoLab 1.4','');

--
-- Table structure for table `cab2b_query`
--

DROP TABLE IF EXISTS `cab2b_query`;
CREATE TABLE `cab2b_query` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `ENTITY_ID` bigint(20) NOT NULL,
  `COMPOUND_QUERY_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKCC34AD9D45C67BB0` (`COMPOUND_QUERY_ID`),
  KEY `FKCC34AD9D1F030BCB` (`IDENTIFIER`),
  CONSTRAINT `FKCC34AD9D1F030BCB` FOREIGN KEY (`IDENTIFIER`) REFERENCES `query_parameterized_query` (`IDENTIFIER`),
  CONSTRAINT `FKCC34AD9D45C67BB0` FOREIGN KEY (`COMPOUND_QUERY_ID`) REFERENCES `cab2b_compound_query` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_query`
--

LOCK TABLES `cab2b_query` WRITE;
/*!40000 ALTER TABLE `cab2b_query` DISABLE KEYS */;
/*!40000 ALTER TABLE `cab2b_query` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cab2b_query_status`
--

DROP TABLE IF EXISTS `cab2b_query_status`;
CREATE TABLE `cab2b_query_status` (
  `ID` bigint(20) NOT NULL,
  `USER_ID` bigint(20) NOT NULL,
  `QUERY_ID` bigint(20) NOT NULL,
  `CONDITIONS` varchar(1024) NOT NULL,
  `START_TIME` datetime default NULL,
  `END_TIME` datetime default NULL,
  `FILENAME` varchar(100) default NULL,
  `PARENT_ID` bigint(20) default NULL,
  `VISIBLE` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`ID`),
  KEY `FKE587F8D49D65F450` (`ID`),
  KEY `FKE587F8D4AEC86F2D` (`USER_ID`),
  KEY `FKE587F8D4286B8D51` (`PARENT_ID`),
  KEY `FKE587F8D4CEDB347A` (`QUERY_ID`),
  CONSTRAINT `FKE587F8D4286B8D51` FOREIGN KEY (`PARENT_ID`) REFERENCES `cab2b_query_status` (`ID`),
  CONSTRAINT `FKE587F8D49D65F450` FOREIGN KEY (`ID`) REFERENCES `cab2b_abstract_status` (`ID`),
  CONSTRAINT `FKE587F8D4AEC86F2D` FOREIGN KEY (`USER_ID`) REFERENCES `cab2b_user` (`USER_ID`),
  CONSTRAINT `FKE587F8D4CEDB347A` FOREIGN KEY (`QUERY_ID`) REFERENCES `cab2b_query` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_query_status`
--

LOCK TABLES `cab2b_query_status` WRITE;
/*!40000 ALTER TABLE `cab2b_query_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `cab2b_query_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cab2b_service_group`
--

DROP TABLE IF EXISTS `cab2b_service_group`;
CREATE TABLE `cab2b_service_group` (
  `id` bigint(20) NOT NULL auto_increment,
  `NAME` text NOT NULL,
  `QUERY_IDENTIFIER` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_service_group`
--

LOCK TABLES `cab2b_service_group` WRITE;
/*!40000 ALTER TABLE `cab2b_service_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `cab2b_service_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cab2b_service_url`
--

DROP TABLE IF EXISTS `cab2b_service_url`;
CREATE TABLE `cab2b_service_url` (
  `URL_ID` bigint(20) NOT NULL auto_increment,
  `DOMAIN_MODEL` text NOT NULL,
  `VERSION` text NOT NULL,
  `URL` text NOT NULL,
  `ADMIN_DEFINED` tinyint(4) NOT NULL,
  `HOSTING_CENTER` varchar(254) default NULL,
  `DESCRIPTION` text,
  `CONTACT_NAME` text,
  `CONTACT_MAIL` text,
  `HOSTING_CENTER_SHORT_NAME` text,
  PRIMARY KEY  (`URL_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=143 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_service_url`
--

LOCK TABLES `cab2b_service_url` WRITE;
/*!40000 ALTER TABLE `cab2b_service_url` DISABLE KEYS */;
/*!40000 ALTER TABLE `cab2b_service_url` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cab2b_service_url_1`
--

DROP TABLE IF EXISTS `cab2b_service_url_1`;
CREATE TABLE `cab2b_service_url_1` (
  `URL_ID` bigint(20) NOT NULL default '0',
  `DOMAIN_MODEL` text NOT NULL,
  `VERSION` text NOT NULL,
  `URL` text NOT NULL,
  `ADMIN_DEFINED` tinyint(4) NOT NULL,
  `HOSTING_CENTER` varchar(254) default NULL,
  `DESCRIPTION` text,
  `CONTACT_NAME` text,
  `CONTACT_MAIL` text,
  `HOSTING_CENTER_SHORT_NAME` text
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_service_url_1`
--

LOCK TABLES `cab2b_service_url_1` WRITE;
/*!40000 ALTER TABLE `cab2b_service_url_1` DISABLE KEYS */;
/*!40000 ALTER TABLE `cab2b_service_url_1` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cab2b_upgrade`
--

DROP TABLE IF EXISTS `cab2b_upgrade`;
CREATE TABLE `cab2b_upgrade` (
  `ID` bigint(20) NOT NULL default '0' COMMENT 'SYSTEM GENERATED PRIMARY KEY.',
  `NAME` varchar(200) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_upgrade`
--

LOCK TABLES `cab2b_upgrade` WRITE;
/*!40000 ALTER TABLE `cab2b_upgrade` DISABLE KEYS */;
/*!40000 ALTER TABLE `cab2b_upgrade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cab2b_url_status`
--

DROP TABLE IF EXISTS `cab2b_url_status`;
CREATE TABLE `cab2b_url_status` (
  `ID` bigint(20) NOT NULL,
  `URL` varchar(255) NOT NULL,
  `QUERY_STATUS_ID` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`ID`),
  KEY `FK8C4B750D9D65F450` (`ID`),
  KEY `FK8C4B750D7B24DE6C` (`QUERY_STATUS_ID`),
  CONSTRAINT `FK8C4B750D9D65F450` FOREIGN KEY (`ID`) REFERENCES `cab2b_abstract_status` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_url_status`
--

LOCK TABLES `cab2b_url_status` WRITE;
/*!40000 ALTER TABLE `cab2b_url_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `cab2b_url_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cab2b_user`
--

DROP TABLE IF EXISTS `cab2b_user`;
CREATE TABLE `cab2b_user` (
  `USER_ID` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(254) NOT NULL,
  `PASSWORD` varchar(30) default NULL,
  `IS_ADMIN` bit(1) NOT NULL,
  PRIMARY KEY  (`USER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_user`
--

LOCK TABLES `cab2b_user` WRITE;
/*!40000 ALTER TABLE `cab2b_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `cab2b_user` ENABLE KEYS */;
UNLOCK TABLES;


INSERT INTO `cab2b_user` VALUES (1,'Admin','cab2badmin',''),(2,'Anonymous',NULL,'\0'),(3,'/O=caBIG/OU=caGrid/OU=LOA1/OU=Dorian/CN=msharma',NULL,'\0');

--
-- Table structure for table `cab2b_user_url_mapping`
--

DROP TABLE IF EXISTS `cab2b_user_url_mapping`;
CREATE TABLE `cab2b_user_url_mapping` (
  `SERVICE_URL_ID` bigint(20) NOT NULL,
  `USER_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`USER_ID`,`SERVICE_URL_ID`),
  KEY `FKC64BBF4AAEC86F2D` (`USER_ID`),
  KEY `FKC64BBF4AB2004842` (`SERVICE_URL_ID`),
  CONSTRAINT `FKC64BBF4AAEC86F2D` FOREIGN KEY (`USER_ID`) REFERENCES `cab2b_user` (`USER_ID`),
  CONSTRAINT `FKC64BBF4AB2004842` FOREIGN KEY (`SERVICE_URL_ID`) REFERENCES `cab2b_service_url` (`URL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_user_url_mapping`
--

LOCK TABLES `cab2b_user_url_mapping` WRITE;
/*!40000 ALTER TABLE `cab2b_user_url_mapping` DISABLE KEYS */;
/*!40000 ALTER TABLE `cab2b_user_url_mapping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categorial_attribute`
--

DROP TABLE IF EXISTS `categorial_attribute`;
CREATE TABLE `categorial_attribute` (
  `ID` bigint(20) NOT NULL,
  `DE_CATEGORY_ATTRIBUTE_ID` bigint(20) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK31F77B5634ED55B7` (`ID`),
  CONSTRAINT `FK31F77B5634ED55B7` FOREIGN KEY (`ID`) REFERENCES `abstract_categorial_attribute` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `categorial_attribute`
--

LOCK TABLES `categorial_attribute` WRITE;
/*!40000 ALTER TABLE `categorial_attribute` DISABLE KEYS */;
/*!40000 ALTER TABLE `categorial_attribute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categorial_class`
--

DROP TABLE IF EXISTS `categorial_class`;
CREATE TABLE `categorial_class` (
  `ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK9651EF32F94A5493` (`ID`),
  CONSTRAINT `FK9651EF32F94A5493` FOREIGN KEY (`ID`) REFERENCES `abstract_categorial_class` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `categorial_class`
--

LOCK TABLES `categorial_class` WRITE;
/*!40000 ALTER TABLE `categorial_class` DISABLE KEYS */;
/*!40000 ALTER TABLE `categorial_class` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `ID` bigint(20) NOT NULL,
  `DE_ENTITY_ID` bigint(20) default NULL,
  `ROOT_CLASS_ID` bigint(20) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `ROOT_CLASS_ID` (`ROOT_CLASS_ID`),
  KEY `FK31A8ACFEA2330820` (`ID`),
  KEY `FK31A8ACFEC88316F9` (`ROOT_CLASS_ID`),
  CONSTRAINT `FK31A8ACFEA2330820` FOREIGN KEY (`ID`) REFERENCES `abstract_category` (`ID`),
  CONSTRAINT `FK31A8ACFEC88316F9` FOREIGN KEY (`ROOT_CLASS_ID`) REFERENCES `categorial_class` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `commons_graph`
--

DROP TABLE IF EXISTS `commons_graph`;
CREATE TABLE `commons_graph` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=InnoDB AUTO_INCREMENT=184 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `commons_graph`
--

LOCK TABLES `commons_graph` WRITE;
/*!40000 ALTER TABLE `commons_graph` DISABLE KEYS */;
/*!40000 ALTER TABLE `commons_graph` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `commons_graph_edge`
--

DROP TABLE IF EXISTS `commons_graph_edge`;
CREATE TABLE `commons_graph_edge` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `SOURCE_VERTEX_CLASS` varchar(255) default NULL,
  `SOURCE_VERTEX_ID` bigint(20) default NULL,
  `TARGET_VERTEX_CLASS` varchar(255) default NULL,
  `TARGET_VERTEX_ID` bigint(20) default NULL,
  `EDGE_CLASS` varchar(255) default NULL,
  `EDGE_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `commons_graph_edge`
--

LOCK TABLES `commons_graph_edge` WRITE;
/*!40000 ALTER TABLE `commons_graph_edge` DISABLE KEYS */;
/*!40000 ALTER TABLE `commons_graph_edge` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `commons_graph_to_edges`
--

DROP TABLE IF EXISTS `commons_graph_to_edges`;
CREATE TABLE `commons_graph_to_edges` (
  `GRAPH_ID` bigint(20) NOT NULL,
  `EDGE_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`GRAPH_ID`,`EDGE_ID`),
  UNIQUE KEY `EDGE_ID` (`EDGE_ID`),
  KEY `FKA6B0D8BAA0494B1D` (`GRAPH_ID`),
  KEY `FKA6B0D8BAFAEF80D` (`EDGE_ID`),
  CONSTRAINT `FKA6B0D8BAA0494B1D` FOREIGN KEY (`GRAPH_ID`) REFERENCES `commons_graph` (`IDENTIFIER`),
  CONSTRAINT `FKA6B0D8BAFAEF80D` FOREIGN KEY (`EDGE_ID`) REFERENCES `commons_graph_edge` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `commons_graph_to_edges`
--

LOCK TABLES `commons_graph_to_edges` WRITE;
/*!40000 ALTER TABLE `commons_graph_to_edges` DISABLE KEYS */;
/*!40000 ALTER TABLE `commons_graph_to_edges` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `commons_graph_to_vertices`
--

DROP TABLE IF EXISTS `commons_graph_to_vertices`;
CREATE TABLE `commons_graph_to_vertices` (
  `GRAPH_ID` bigint(20) NOT NULL,
  `VERTEX_CLASS` varchar(255) default NULL,
  `VERTEX_ID` bigint(20) default NULL,
  KEY `FK2C4412F5A0494B1D` (`GRAPH_ID`),
  CONSTRAINT `FK2C4412F5A0494B1D` FOREIGN KEY (`GRAPH_ID`) REFERENCES `commons_graph` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `commons_graph_to_vertices`
--

LOCK TABLES `commons_graph_to_vertices` WRITE;
/*!40000 ALTER TABLE `commons_graph_to_vertices` DISABLE KEYS */;
/*!40000 ALTER TABLE `commons_graph_to_vertices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `curated_path`
--

DROP TABLE IF EXISTS `curated_path`;
CREATE TABLE `curated_path` (
  `curated_path_Id` bigint(20) NOT NULL auto_increment,
  `entity_ids` varchar(1000) default NULL,
  `selected` tinyint(1) default NULL,
  PRIMARY KEY  (`curated_path_Id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `curated_path`
--

LOCK TABLES `curated_path` WRITE;
/*!40000 ALTER TABLE `curated_path` DISABLE KEYS */;
/*!40000 ALTER TABLE `curated_path` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `curated_path_to_path`
--

DROP TABLE IF EXISTS `curated_path_to_path`;
CREATE TABLE `curated_path_to_path` (
  `curated_path_Id` bigint(20) NOT NULL default '0',
  `path_id` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`curated_path_Id`,`path_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `curated_path_to_path`
--

LOCK TABLES `curated_path_to_path` WRITE;
/*!40000 ALTER TABLE `curated_path_to_path` DISABLE KEYS */;
/*!40000 ALTER TABLE `curated_path_to_path` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `data_categorial_attribute`
--

DROP TABLE IF EXISTS `data_categorial_attribute`;
CREATE TABLE `data_categorial_attribute` (
  `ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK782EFCCB34ED55B7` (`ID`),
  CONSTRAINT `FK782EFCCB34ED55B7` FOREIGN KEY (`ID`) REFERENCES `abstract_categorial_attribute` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `data_categorial_attribute`
--

LOCK TABLES `data_categorial_attribute` WRITE;
/*!40000 ALTER TABLE `data_categorial_attribute` DISABLE KEYS */;
/*!40000 ALTER TABLE `data_categorial_attribute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `data_categorial_class`
--

DROP TABLE IF EXISTS `data_categorial_class`;
CREATE TABLE `data_categorial_class` (
  `ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK13067327F94A5493` (`ID`),
  CONSTRAINT `FK13067327F94A5493` FOREIGN KEY (`ID`) REFERENCES `abstract_categorial_class` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `data_categorial_class`
--

LOCK TABLES `data_categorial_class` WRITE;
/*!40000 ALTER TABLE `data_categorial_class` DISABLE KEYS */;
/*!40000 ALTER TABLE `data_categorial_class` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `databasechangelog`
--

DROP TABLE IF EXISTS `databasechangelog`;
CREATE TABLE `databasechangelog` (
  `ID` varchar(63) NOT NULL,
  `AUTHOR` varchar(63) NOT NULL,
  `FILENAME` varchar(200) NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `MD5SUM` varchar(32) default NULL,
  `DESCRIPTION` varchar(255) default NULL,
  `COMMENTS` varchar(255) default NULL,
  `TAG` varchar(255) default NULL,
  `LIQUIBASE` varchar(10) default NULL,
  PRIMARY KEY  (`ID`,`AUTHOR`,`FILENAME`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `databasechangelog`
--

LOCK TABLES `databasechangelog` WRITE;
/*!40000 ALTER TABLE `databasechangelog` DISABLE KEYS */;
/*!40000 ALTER TABLE `databasechangelog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `databasechangeloglock`
--

DROP TABLE IF EXISTS `databasechangeloglock`;
CREATE TABLE `databasechangeloglock` (
  `ID` int(11) NOT NULL,
  `LOCKED` tinyint(1) NOT NULL,
  `LOCKGRANTED` datetime default NULL,
  `LOCKEDBY` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `databasechangeloglock`
--

LOCK TABLES `databasechangeloglock` WRITE;
/*!40000 ALTER TABLE `databasechangeloglock` DISABLE KEYS */;
/*!40000 ALTER TABLE `databasechangeloglock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_coll_attr_record_values`
--

DROP TABLE IF EXISTS `de_coll_attr_record_values`;
CREATE TABLE `de_coll_attr_record_values` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `RECORD_VALUE` text,
  `COLLECTION_ATTR_RECORD_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK847DA57775255CA5` (`COLLECTION_ATTR_RECORD_ID`),
  CONSTRAINT `FK847DA57775255CA5` FOREIGN KEY (`COLLECTION_ATTR_RECORD_ID`) REFERENCES `dyextn_attribute_record` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_coll_attr_record_values`
--

LOCK TABLES `de_coll_attr_record_values` WRITE;
/*!40000 ALTER TABLE `de_coll_attr_record_values` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_coll_attr_record_values` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_13059`
--

DROP TABLE IF EXISTS `de_e_13059`;
CREATE TABLE `de_e_13059` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_13059`
--

LOCK TABLES `de_e_13059` WRITE;
/*!40000 ALTER TABLE `de_e_13059` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_13059` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_13061`
--

DROP TABLE IF EXISTS `de_e_13061`;
CREATE TABLE `de_e_13061` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_13071` text,
  `DE_AT_13070` text,
  `DE_AT_13069` bigint(38) default NULL,
  `DE_AT_13068` text,
  `DE_AT_13067` text,
  `DE_AT_13066` text,
  `DE_AT_13065` text,
  `DE_E_13059_13060_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_13061`
--

LOCK TABLES `de_e_13061` WRITE;
/*!40000 ALTER TABLE `de_e_13061` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_13061` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_13072`
--

DROP TABLE IF EXISTS `de_e_13072`;
CREATE TABLE `de_e_13072` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_13076` text,
  `DE_AT_13075` text,
  `DE_AT_13074` text,
  `DE_E_13061_13064_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_13072`
--

LOCK TABLES `de_e_13072` WRITE;
/*!40000 ALTER TABLE `de_e_13072` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_13072` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_13077`
--

DROP TABLE IF EXISTS `de_e_13077`;
CREATE TABLE `de_e_13077` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_13080` text,
  `DE_AT_13079` text,
  `DE_AT_13078` text,
  `DE_E_13072_13073_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_13077`
--

LOCK TABLES `de_e_13077` WRITE;
/*!40000 ALTER TABLE `de_e_13077` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_13077` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_13081`
--

DROP TABLE IF EXISTS `de_e_13081`;
CREATE TABLE `de_e_13081` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_13085` text,
  `DE_AT_13084` text,
  `DE_AT_13083` text,
  `DE_AT_13082` text,
  `DE_E_13061_13063_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_13081`
--

LOCK TABLES `de_e_13081` WRITE;
/*!40000 ALTER TABLE `de_e_13081` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_13081` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_13086`
--

DROP TABLE IF EXISTS `de_e_13086`;
CREATE TABLE `de_e_13086` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_13090` text,
  `DE_AT_13089` text,
  `DE_AT_13088` text,
  `DE_E_13061_13062_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_13086`
--

LOCK TABLES `de_e_13086` WRITE;
/*!40000 ALTER TABLE `de_e_13086` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_13086` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_13091`
--

DROP TABLE IF EXISTS `de_e_13091`;
CREATE TABLE `de_e_13091` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_13094` text,
  `DE_AT_13093` text,
  `DE_AT_13092` text,
  `DE_E_13086_13087_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_13091`
--

LOCK TABLES `de_e_13091` WRITE;
/*!40000 ALTER TABLE `de_e_13091` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_13091` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_13095`
--

DROP TABLE IF EXISTS `de_e_13095`;
CREATE TABLE `de_e_13095` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_13095`
--

LOCK TABLES `de_e_13095` WRITE;
/*!40000 ALTER TABLE `de_e_13095` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_13095` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_13097`
--

DROP TABLE IF EXISTS `de_e_13097`;
CREATE TABLE `de_e_13097` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_13104` text,
  `DE_AT_13103` text,
  `DE_AT_13102` text,
  `DE_AT_13101` text,
  `DE_AT_13100` text,
  `DE_AT_13099` text,
  `DE_E_13095_13096_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_13097`
--

LOCK TABLES `de_e_13097` WRITE;
/*!40000 ALTER TABLE `de_e_13097` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_13097` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_13105`
--

DROP TABLE IF EXISTS `de_e_13105`;
CREATE TABLE `de_e_13105` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_13110` text,
  `DE_AT_13109` text,
  `DE_AT_13108` text,
  `DE_AT_13107` text,
  `DE_E_13097_13098_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_13105`
--

LOCK TABLES `de_e_13105` WRITE;
/*!40000 ALTER TABLE `de_e_13105` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_13105` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_13111`
--

DROP TABLE IF EXISTS `de_e_13111`;
CREATE TABLE `de_e_13111` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_13120` text,
  `DE_AT_13119` tinyint(1) default NULL,
  `DE_AT_13118` text,
  `DE_AT_13117` bigint(38) default NULL,
  `DE_AT_13116` text,
  `DE_AT_13115` text,
  `DE_AT_13114` text,
  `DE_AT_13113` text,
  `DE_E_13105_13106_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_13111`
--

LOCK TABLES `de_e_13111` WRITE;
/*!40000 ALTER TABLE `de_e_13111` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_13111` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_13121`
--

DROP TABLE IF EXISTS `de_e_13121`;
CREATE TABLE `de_e_13121` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_13125` text,
  `DE_AT_13124` text,
  `DE_AT_13123` text,
  `DE_AT_13122` text,
  `DE_E_13111_13112_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_13121`
--

LOCK TABLES `de_e_13121` WRITE;
/*!40000 ALTER TABLE `de_e_13121` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_13121` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_14245`
--

DROP TABLE IF EXISTS `de_e_14245`;
CREATE TABLE `de_e_14245` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_14245`
--

LOCK TABLES `de_e_14245` WRITE;
/*!40000 ALTER TABLE `de_e_14245` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_14245` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_14247`
--

DROP TABLE IF EXISTS `de_e_14247`;
CREATE TABLE `de_e_14247` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_14257` text,
  `DE_AT_14256` text,
  `DE_AT_14255` text,
  `DE_AT_14254` text,
  `DE_AT_14253` bigint(38) default NULL,
  `DE_AT_14252` text,
  `DE_E_14245_14246_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_14247`
--

LOCK TABLES `de_e_14247` WRITE;
/*!40000 ALTER TABLE `de_e_14247` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_14247` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_14258`
--

DROP TABLE IF EXISTS `de_e_14258`;
CREATE TABLE `de_e_14258` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_14262` text,
  `DE_AT_14261` text,
  `DE_AT_14260` text,
  `DE_AT_14259` text,
  `DE_E_14247_14251_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_14258`
--

LOCK TABLES `de_e_14258` WRITE;
/*!40000 ALTER TABLE `de_e_14258` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_14258` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_14263`
--

DROP TABLE IF EXISTS `de_e_14263`;
CREATE TABLE `de_e_14263` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_14266` text,
  `DE_AT_14265` text,
  `DE_AT_14264` text,
  `DE_E_14247_14250_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_14263`
--

LOCK TABLES `de_e_14263` WRITE;
/*!40000 ALTER TABLE `de_e_14263` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_14263` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_14267`
--

DROP TABLE IF EXISTS `de_e_14267`;
CREATE TABLE `de_e_14267` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_14271` text,
  `DE_AT_14270` text,
  `DE_AT_14269` text,
  `DE_E_14247_14249_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_14267`
--

LOCK TABLES `de_e_14267` WRITE;
/*!40000 ALTER TABLE `de_e_14267` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_14267` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_14272`
--

DROP TABLE IF EXISTS `de_e_14272`;
CREATE TABLE `de_e_14272` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_14275` text,
  `DE_AT_14274` text,
  `DE_AT_14273` text,
  `DE_E_14267_14268_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_14272`
--

LOCK TABLES `de_e_14272` WRITE;
/*!40000 ALTER TABLE `de_e_14272` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_14272` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_14276`
--

DROP TABLE IF EXISTS `de_e_14276`;
CREATE TABLE `de_e_14276` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_14280` text,
  `DE_AT_14279` text,
  `DE_AT_14278` text,
  `DE_E_14247_14248_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_14276`
--

LOCK TABLES `de_e_14276` WRITE;
/*!40000 ALTER TABLE `de_e_14276` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_14276` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_14281`
--

DROP TABLE IF EXISTS `de_e_14281`;
CREATE TABLE `de_e_14281` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_14284` text,
  `DE_AT_14283` text,
  `DE_AT_14282` text,
  `DE_E_14276_14277_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_14281`
--

LOCK TABLES `de_e_14281` WRITE;
/*!40000 ALTER TABLE `de_e_14281` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_14281` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_14285`
--

DROP TABLE IF EXISTS `de_e_14285`;
CREATE TABLE `de_e_14285` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_14301` text,
  `DE_AT_14300` text,
  `DE_AT_14299` text,
  `DE_AT_14286` int(38) default NULL,
  `DE_AT_14287` text,
  `DE_AT_14288` int(38) default NULL,
  `DE_AT_14289` text,
  `DE_AT_14290` text,
  `DE_AT_14291` text,
  `DE_AT_14292` text,
  `DE_AT_14293` text,
  `DE_AT_14294` text,
  `DE_AT_14295` text,
  `DE_AT_14296` text,
  `DE_AT_14297` text,
  `DE_AT_14298` text,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_14285`
--

LOCK TABLES `de_e_14285` WRITE;
/*!40000 ALTER TABLE `de_e_14285` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_14285` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_14350`
--

DROP TABLE IF EXISTS `de_e_14350`;
CREATE TABLE `de_e_14350` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_14350`
--

LOCK TABLES `de_e_14350` WRITE;
/*!40000 ALTER TABLE `de_e_14350` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_14350` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_14352`
--

DROP TABLE IF EXISTS `de_e_14352`;
CREATE TABLE `de_e_14352` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_14358` text,
  `DE_AT_14357` text,
  `DE_AT_14356` int(38) default NULL,
  `DE_AT_14355` text,
  `DE_AT_14354` text,
  `DE_AT_14353` int(38) default NULL,
  `DE_E_14350_14351_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_14352`
--

LOCK TABLES `de_e_14352` WRITE;
/*!40000 ALTER TABLE `de_e_14352` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_14352` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_14359`
--

DROP TABLE IF EXISTS `de_e_14359`;
CREATE TABLE `de_e_14359` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_14359`
--

LOCK TABLES `de_e_14359` WRITE;
/*!40000 ALTER TABLE `de_e_14359` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_14359` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_14361`
--

DROP TABLE IF EXISTS `de_e_14361`;
CREATE TABLE `de_e_14361` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_14368` text,
  `DE_AT_14367` text,
  `DE_AT_14366` int(38) default NULL,
  `DE_AT_14365` text,
  `DE_AT_14364` text,
  `DE_AT_14363` int(38) default NULL,
  `DE_E_14359_14360_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_14361`
--

LOCK TABLES `de_e_14361` WRITE;
/*!40000 ALTER TABLE `de_e_14361` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_14361` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_14369`
--

DROP TABLE IF EXISTS `de_e_14369`;
CREATE TABLE `de_e_14369` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_14376` text,
  `DE_AT_14375` text,
  `DE_AT_14374` text,
  `DE_AT_14373` text,
  `DE_AT_14372` int(38) default NULL,
  `DE_AT_14371` int(38) default NULL,
  `DE_AT_14370` text,
  `DE_E_14361_14362_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_14369`
--

LOCK TABLES `de_e_14369` WRITE;
/*!40000 ALTER TABLE `de_e_14369` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_14369` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_18474`
--

DROP TABLE IF EXISTS `de_e_18474`;
CREATE TABLE `de_e_18474` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_18474`
--

LOCK TABLES `de_e_18474` WRITE;
/*!40000 ALTER TABLE `de_e_18474` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_18474` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_18476`
--

DROP TABLE IF EXISTS `de_e_18476`;
CREATE TABLE `de_e_18476` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_18484` text,
  `DE_AT_18483` text,
  `DE_AT_18482` text,
  `DE_AT_18481` bigint(38) default NULL,
  `DE_AT_18480` text,
  `DE_AT_18479` text,
  `DE_AT_18478` bigint(38) default NULL,
  `DE_E_18474_18475_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_18476`
--

LOCK TABLES `de_e_18476` WRITE;
/*!40000 ALTER TABLE `de_e_18476` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_18476` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_18485`
--

DROP TABLE IF EXISTS `de_e_18485`;
CREATE TABLE `de_e_18485` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_18490` text,
  `DE_AT_18489` text,
  `DE_AT_18488` text,
  `DE_AT_18487` bigint(38) default NULL,
  `DE_AT_18486` text,
  `DE_E_18476_18477_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_18485`
--

LOCK TABLES `de_e_18485` WRITE;
/*!40000 ALTER TABLE `de_e_18485` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_18485` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_18491`
--

DROP TABLE IF EXISTS `de_e_18491`;
CREATE TABLE `de_e_18491` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_18496` text,
  `DE_AT_18495` text,
  `DE_AT_18494` text,
  `DE_AT_18493` text,
  `DE_AT_18492` text,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_18491`
--

LOCK TABLES `de_e_18491` WRITE;
/*!40000 ALTER TABLE `de_e_18491` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_18491` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_18497`
--

DROP TABLE IF EXISTS `de_e_18497`;
CREATE TABLE `de_e_18497` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_18497`
--

LOCK TABLES `de_e_18497` WRITE;
/*!40000 ALTER TABLE `de_e_18497` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_18497` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_18499`
--

DROP TABLE IF EXISTS `de_e_18499`;
CREATE TABLE `de_e_18499` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_18508` text,
  `DE_AT_18507` text,
  `DE_AT_18506` text,
  `DE_AT_18505` bigint(38) default NULL,
  `DE_AT_18504` text,
  `DE_AT_18503` text,
  `DE_AT_18502` bigint(38) default NULL,
  `DE_E_18497_18498_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_18499`
--

LOCK TABLES `de_e_18499` WRITE;
/*!40000 ALTER TABLE `de_e_18499` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_18499` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_18509`
--

DROP TABLE IF EXISTS `de_e_18509`;
CREATE TABLE `de_e_18509` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_18514` text,
  `DE_AT_18513` text,
  `DE_AT_18512` int(38) default NULL,
  `DE_AT_18511` bigint(38) default NULL,
  `DE_AT_18510` text,
  `DE_E_18499_18501_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_18509`
--

LOCK TABLES `de_e_18509` WRITE;
/*!40000 ALTER TABLE `de_e_18509` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_18509` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_18515`
--

DROP TABLE IF EXISTS `de_e_18515`;
CREATE TABLE `de_e_18515` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_18523` text,
  `DE_AT_18522` text,
  `DE_AT_18521` text,
  `DE_AT_18520` text,
  `DE_AT_18519` bigint(38) default NULL,
  `DE_AT_18518` text,
  `DE_AT_18517` text,
  `DE_AT_18516` text,
  `DE_E_18499_18500_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_18515`
--

LOCK TABLES `de_e_18515` WRITE;
/*!40000 ALTER TABLE `de_e_18515` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_18515` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_18524`
--

DROP TABLE IF EXISTS `de_e_18524`;
CREATE TABLE `de_e_18524` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_18530` text,
  `DE_AT_18529` text,
  `DE_AT_18528` bigint(38) default NULL,
  `DE_AT_18527` text,
  `DE_AT_18526` text,
  `DE_AT_18525` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_18524`
--

LOCK TABLES `de_e_18524` WRITE;
/*!40000 ALTER TABLE `de_e_18524` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_18524` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_5361`
--

DROP TABLE IF EXISTS `de_e_5361`;
CREATE TABLE `de_e_5361` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_5361`
--

LOCK TABLES `de_e_5361` WRITE;
/*!40000 ALTER TABLE `de_e_5361` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_5361` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_5363`
--

DROP TABLE IF EXISTS `de_e_5363`;
CREATE TABLE `de_e_5363` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_5369` text,
  `DE_AT_5368` text,
  `DE_AT_5367` text,
  `DE_AT_5366` bigint(38) default NULL,
  `DE_AT_5365` date default NULL,
  `DE_AT_5364` text,
  `DE_E_5361_5362_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_5363`
--

LOCK TABLES `de_e_5363` WRITE;
/*!40000 ALTER TABLE `de_e_5363` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_5363` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_5370`
--

DROP TABLE IF EXISTS `de_e_5370`;
CREATE TABLE `de_e_5370` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_5370`
--

LOCK TABLES `de_e_5370` WRITE;
/*!40000 ALTER TABLE `de_e_5370` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_5370` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_5372`
--

DROP TABLE IF EXISTS `de_e_5372`;
CREATE TABLE `de_e_5372` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_5378` text,
  `DE_AT_5377` text,
  `DE_AT_5376` text,
  `DE_AT_5375` bigint(38) default NULL,
  `DE_AT_5374` date default NULL,
  `DE_AT_5373` text,
  `DE_E_5370_5371_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_5372`
--

LOCK TABLES `de_e_5372` WRITE;
/*!40000 ALTER TABLE `de_e_5372` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_5372` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_5379`
--

DROP TABLE IF EXISTS `de_e_5379`;
CREATE TABLE `de_e_5379` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_5379`
--

LOCK TABLES `de_e_5379` WRITE;
/*!40000 ALTER TABLE `de_e_5379` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_5379` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_5381`
--

DROP TABLE IF EXISTS `de_e_5381`;
CREATE TABLE `de_e_5381` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_5388` text,
  `DE_AT_5387` text,
  `DE_AT_5386` date default NULL,
  `DE_AT_5385` text,
  `DE_AT_5384` text,
  `DE_AT_5383` bigint(38) default NULL,
  `DE_E_5379_5380_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_5381`
--

LOCK TABLES `de_e_5381` WRITE;
/*!40000 ALTER TABLE `de_e_5381` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_5381` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_5389`
--

DROP TABLE IF EXISTS `de_e_5389`;
CREATE TABLE `de_e_5389` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_5393` text,
  `DE_AT_5392` text,
  `DE_AT_5391` bigint(38) default NULL,
  `DE_E_5381_5382_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_5389`
--

LOCK TABLES `de_e_5389` WRITE;
/*!40000 ALTER TABLE `de_e_5389` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_5389` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_5394`
--

DROP TABLE IF EXISTS `de_e_5394`;
CREATE TABLE `de_e_5394` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_5401` text,
  `DE_AT_5400` text,
  `DE_AT_5399` text,
  `DE_AT_5398` bigint(38) default NULL,
  `DE_AT_5397` date default NULL,
  `DE_AT_5396` text,
  `DE_E_5389_5390_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_5394`
--

LOCK TABLES `de_e_5394` WRITE;
/*!40000 ALTER TABLE `de_e_5394` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_5394` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_5402`
--

DROP TABLE IF EXISTS `de_e_5402`;
CREATE TABLE `de_e_5402` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_5424` text,
  `DE_AT_5423` text,
  `DE_AT_5422` bigint(38) default NULL,
  `DE_AT_5421` text,
  `DE_AT_5420` bigint(38) default NULL,
  `DE_AT_5419` text,
  `DE_AT_5418` text,
  `DE_AT_5417` text,
  `DE_AT_5416` text,
  `DE_AT_5403` tinyint(1) default NULL,
  `DE_AT_5404` text,
  `DE_AT_5405` text,
  `DE_AT_5406` text,
  `DE_AT_5407` text,
  `DE_AT_5408` text,
  `DE_AT_5409` text,
  `DE_AT_5410` text,
  `DE_AT_5411` date default NULL,
  `DE_AT_5412` text,
  `DE_AT_5413` text,
  `DE_AT_5414` int(38) default NULL,
  `DE_AT_5415` text,
  `DE_E_5394_5395_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_5402`
--

LOCK TABLES `de_e_5402` WRITE;
/*!40000 ALTER TABLE `de_e_5402` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_5402` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_5425`
--

DROP TABLE IF EXISTS `de_e_5425`;
CREATE TABLE `de_e_5425` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_5456` text,
  `DE_AT_5455` text,
  `DE_AT_5454` bigint(38) default NULL,
  `DE_AT_5453` text,
  `DE_AT_5452` text,
  `DE_AT_5451` text,
  `DE_AT_5438` text,
  `DE_AT_5437` date default NULL,
  `DE_AT_5436` bigint(38) default NULL,
  `DE_AT_5435` text,
  `DE_AT_5434` date default NULL,
  `DE_AT_5433` bigint(38) default NULL,
  `DE_AT_5432` text,
  `DE_AT_5431` text,
  `DE_AT_5430` text,
  `DE_AT_5429` tinyint(1) default NULL,
  `DE_AT_5428` text,
  `DE_AT_5427` text,
  `DE_AT_5426` text,
  `DE_AT_5439` text,
  `DE_AT_5440` bigint(38) default NULL,
  `DE_AT_5441` bigint(38) default NULL,
  `DE_AT_5442` text,
  `DE_AT_5443` text,
  `DE_AT_5444` text,
  `DE_AT_5445` text,
  `DE_AT_5446` int(38) default NULL,
  `DE_AT_5447` date default NULL,
  `DE_AT_5448` text,
  `DE_AT_5449` text,
  `DE_AT_5450` text,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_5425`
--

LOCK TABLES `de_e_5425` WRITE;
/*!40000 ALTER TABLE `de_e_5425` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_5425` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_5457`
--

DROP TABLE IF EXISTS `de_e_5457`;
CREATE TABLE `de_e_5457` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_5457`
--

LOCK TABLES `de_e_5457` WRITE;
/*!40000 ALTER TABLE `de_e_5457` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_5457` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_5459`
--

DROP TABLE IF EXISTS `de_e_5459`;
CREATE TABLE `de_e_5459` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_5474` text,
  `DE_AT_5473` date default NULL,
  `DE_AT_5460` text,
  `DE_AT_5461` text,
  `DE_AT_5462` text,
  `DE_AT_5463` text,
  `DE_AT_5464` double default NULL,
  `DE_AT_5465` text,
  `DE_AT_5466` tinyint(1) default NULL,
  `DE_AT_5467` int(38) default NULL,
  `DE_AT_5468` int(38) default NULL,
  `DE_AT_5469` text,
  `DE_AT_5470` text,
  `DE_AT_5471` text,
  `DE_AT_5472` bigint(38) default NULL,
  `DE_E_5457_5458_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_5459`
--

LOCK TABLES `de_e_5459` WRITE;
/*!40000 ALTER TABLE `de_e_5459` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_5459` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_5499`
--

DROP TABLE IF EXISTS `de_e_5499`;
CREATE TABLE `de_e_5499` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_5499`
--

LOCK TABLES `de_e_5499` WRITE;
/*!40000 ALTER TABLE `de_e_5499` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_5499` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_5501`
--

DROP TABLE IF EXISTS `de_e_5501`;
CREATE TABLE `de_e_5501` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_5506` text,
  `DE_AT_5505` text,
  `DE_AT_5504` text,
  `DE_AT_5503` text,
  `DE_AT_5502` bigint(38) default NULL,
  `DE_E_5499_5500_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_5501`
--

LOCK TABLES `de_e_5501` WRITE;
/*!40000 ALTER TABLE `de_e_5501` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_5501` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_5507`
--

DROP TABLE IF EXISTS `de_e_5507`;
CREATE TABLE `de_e_5507` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_5507`
--

LOCK TABLES `de_e_5507` WRITE;
/*!40000 ALTER TABLE `de_e_5507` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_5507` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_5509`
--

DROP TABLE IF EXISTS `de_e_5509`;
CREATE TABLE `de_e_5509` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_5525` text,
  `DE_AT_5524` date default NULL,
  `DE_AT_5523` text,
  `DE_AT_5510` text,
  `DE_AT_5511` text,
  `DE_AT_5512` text,
  `DE_AT_5513` text,
  `DE_AT_5514` text,
  `DE_AT_5515` text,
  `DE_AT_5516` bigint(38) default NULL,
  `DE_AT_5517` text,
  `DE_AT_5518` text,
  `DE_AT_5519` text,
  `DE_AT_5520` date default NULL,
  `DE_AT_5521` text,
  `DE_AT_5522` text,
  `DE_E_5507_5508_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_5509`
--

LOCK TABLES `de_e_5509` WRITE;
/*!40000 ALTER TABLE `de_e_5509` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_5509` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_5526`
--

DROP TABLE IF EXISTS `de_e_5526`;
CREATE TABLE `de_e_5526` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_5526`
--

LOCK TABLES `de_e_5526` WRITE;
/*!40000 ALTER TABLE `de_e_5526` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_5526` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_5528`
--

DROP TABLE IF EXISTS `de_e_5528`;
CREATE TABLE `de_e_5528` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_5534` text,
  `DE_AT_5533` text,
  `DE_AT_5532` text,
  `DE_AT_5531` bigint(38) default NULL,
  `DE_AT_5530` text,
  `DE_AT_5529` date default NULL,
  `DE_E_5526_5527_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_5528`
--

LOCK TABLES `de_e_5528` WRITE;
/*!40000 ALTER TABLE `de_e_5528` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_5528` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_5535`
--

DROP TABLE IF EXISTS `de_e_5535`;
CREATE TABLE `de_e_5535` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_5539` text,
  `DE_AT_5538` text,
  `DE_AT_5537` text,
  `DE_AT_5536` text,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_5535`
--

LOCK TABLES `de_e_5535` WRITE;
/*!40000 ALTER TABLE `de_e_5535` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_5535` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_5540`
--

DROP TABLE IF EXISTS `de_e_5540`;
CREATE TABLE `de_e_5540` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_5540`
--

LOCK TABLES `de_e_5540` WRITE;
/*!40000 ALTER TABLE `de_e_5540` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_5540` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_5542`
--

DROP TABLE IF EXISTS `de_e_5542`;
CREATE TABLE `de_e_5542` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_5549` text,
  `DE_AT_5548` text,
  `DE_AT_5547` text,
  `DE_AT_5546` bigint(38) default NULL,
  `DE_AT_5545` text,
  `DE_AT_5544` text,
  `DE_AT_5543` bigint(38) default NULL,
  `DE_E_5540_5541_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_5542`
--

LOCK TABLES `de_e_5542` WRITE;
/*!40000 ALTER TABLE `de_e_5542` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_5542` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6268`
--

DROP TABLE IF EXISTS `de_e_6268`;
CREATE TABLE `de_e_6268` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6268`
--

LOCK TABLES `de_e_6268` WRITE;
/*!40000 ALTER TABLE `de_e_6268` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6268` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6270`
--

DROP TABLE IF EXISTS `de_e_6270`;
CREATE TABLE `de_e_6270` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6278` text,
  `DE_AT_6277` text,
  `DE_AT_6276` text,
  `DE_AT_6275` text,
  `DE_AT_6274` int(38) default NULL,
  `DE_AT_6273` date default NULL,
  `DE_AT_6272` text,
  `DE_E_6268_6269_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6270`
--

LOCK TABLES `de_e_6270` WRITE;
/*!40000 ALTER TABLE `de_e_6270` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6270` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6279`
--

DROP TABLE IF EXISTS `de_e_6279`;
CREATE TABLE `de_e_6279` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6295` text,
  `DE_AT_6294` text,
  `DE_AT_6281` text,
  `DE_AT_6282` text,
  `DE_AT_6283` text,
  `DE_AT_6284` text,
  `DE_AT_6285` text,
  `DE_AT_6286` text,
  `DE_AT_6287` text,
  `DE_AT_6288` text,
  `DE_AT_6289` int(38) default NULL,
  `DE_AT_6290` text,
  `DE_AT_6291` text,
  `DE_AT_6292` text,
  `DE_AT_6293` text,
  `DE_E_6270_6271_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6279`
--

LOCK TABLES `de_e_6279` WRITE;
/*!40000 ALTER TABLE `de_e_6279` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6279` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6296`
--

DROP TABLE IF EXISTS `de_e_6296`;
CREATE TABLE `de_e_6296` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6308` text,
  `DE_AT_6307` text,
  `DE_AT_6306` text,
  `DE_AT_6305` text,
  `DE_AT_6304` text,
  `DE_AT_6303` int(38) default NULL,
  `DE_AT_6302` text,
  `DE_AT_6301` text,
  `DE_AT_6300` text,
  `DE_AT_6299` text,
  `DE_AT_6298` text,
  `DE_AT_6297` text,
  `DE_E_6279_6280_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6296`
--

LOCK TABLES `de_e_6296` WRITE;
/*!40000 ALTER TABLE `de_e_6296` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6296` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6309`
--

DROP TABLE IF EXISTS `de_e_6309`;
CREATE TABLE `de_e_6309` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6309`
--

LOCK TABLES `de_e_6309` WRITE;
/*!40000 ALTER TABLE `de_e_6309` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6309` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6311`
--

DROP TABLE IF EXISTS `de_e_6311`;
CREATE TABLE `de_e_6311` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6321` text,
  `DE_AT_6320` text,
  `DE_AT_6319` text,
  `DE_AT_6318` text,
  `DE_AT_6317` bigint(38) default NULL,
  `DE_AT_6316` text,
  `DE_AT_6315` text,
  `DE_AT_6314` text,
  `DE_AT_6313` text,
  `DE_AT_6312` text,
  `DE_E_6309_6310_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6311`
--

LOCK TABLES `de_e_6311` WRITE;
/*!40000 ALTER TABLE `de_e_6311` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6311` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6322`
--

DROP TABLE IF EXISTS `de_e_6322`;
CREATE TABLE `de_e_6322` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6322`
--

LOCK TABLES `de_e_6322` WRITE;
/*!40000 ALTER TABLE `de_e_6322` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6322` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6324`
--

DROP TABLE IF EXISTS `de_e_6324`;
CREATE TABLE `de_e_6324` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6331` text,
  `DE_AT_6330` text,
  `DE_AT_6329` text,
  `DE_AT_6328` text,
  `DE_AT_6327` int(38) default NULL,
  `DE_AT_6326` date default NULL,
  `DE_AT_6325` text,
  `DE_E_6322_6323_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6324`
--

LOCK TABLES `de_e_6324` WRITE;
/*!40000 ALTER TABLE `de_e_6324` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6324` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6332`
--

DROP TABLE IF EXISTS `de_e_6332`;
CREATE TABLE `de_e_6332` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6332`
--

LOCK TABLES `de_e_6332` WRITE;
/*!40000 ALTER TABLE `de_e_6332` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6332` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6334`
--

DROP TABLE IF EXISTS `de_e_6334`;
CREATE TABLE `de_e_6334` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6342` text,
  `DE_AT_6341` text,
  `DE_AT_6340` text,
  `DE_AT_6339` text,
  `DE_AT_6338` int(38) default NULL,
  `DE_AT_6337` date default NULL,
  `DE_AT_6336` text,
  `DE_E_6332_6333_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6334`
--

LOCK TABLES `de_e_6334` WRITE;
/*!40000 ALTER TABLE `de_e_6334` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6334` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6343`
--

DROP TABLE IF EXISTS `de_e_6343`;
CREATE TABLE `de_e_6343` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6355` text,
  `DE_AT_6354` text,
  `DE_AT_6353` text,
  `DE_AT_6352` bigint(38) default NULL,
  `DE_AT_6351` text,
  `DE_AT_6350` text,
  `DE_AT_6349` text,
  `DE_AT_6348` text,
  `DE_AT_6347` text,
  `DE_AT_6346` text,
  `DE_AT_6345` text,
  `DE_E_6334_6335_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6343`
--

LOCK TABLES `de_e_6343` WRITE;
/*!40000 ALTER TABLE `de_e_6343` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6343` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6356`
--

DROP TABLE IF EXISTS `de_e_6356`;
CREATE TABLE `de_e_6356` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6364` text,
  `DE_AT_6363` text,
  `DE_AT_6362` text,
  `DE_AT_6361` text,
  `DE_AT_6360` bigint(38) default NULL,
  `DE_AT_6359` text,
  `DE_AT_6358` text,
  `DE_E_6343_6344_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6356`
--

LOCK TABLES `de_e_6356` WRITE;
/*!40000 ALTER TABLE `de_e_6356` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6356` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6365`
--

DROP TABLE IF EXISTS `de_e_6365`;
CREATE TABLE `de_e_6365` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6375` text,
  `DE_AT_6374` text,
  `DE_AT_6373` bigint(38) default NULL,
  `DE_AT_6372` text,
  `DE_AT_6371` text,
  `DE_AT_6370` text,
  `DE_AT_6369` text,
  `DE_AT_6368` text,
  `DE_AT_6367` text,
  `DE_E_6356_6357_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6365`
--

LOCK TABLES `de_e_6365` WRITE;
/*!40000 ALTER TABLE `de_e_6365` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6365` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6376`
--

DROP TABLE IF EXISTS `de_e_6376`;
CREATE TABLE `de_e_6376` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6387` text,
  `DE_AT_6386` text,
  `DE_AT_6385` text,
  `DE_AT_6384` text,
  `DE_AT_6383` bigint(38) default NULL,
  `DE_AT_6382` text,
  `DE_AT_6381` text,
  `DE_AT_6380` text,
  `DE_AT_6379` text,
  `DE_AT_6378` text,
  `DE_E_6365_6366_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6376`
--

LOCK TABLES `de_e_6376` WRITE;
/*!40000 ALTER TABLE `de_e_6376` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6376` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6388`
--

DROP TABLE IF EXISTS `de_e_6388`;
CREATE TABLE `de_e_6388` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6404` date default NULL,
  `DE_AT_6403` text,
  `DE_AT_6402` text,
  `DE_AT_6389` text,
  `DE_AT_6390` text,
  `DE_AT_6391` text,
  `DE_AT_6392` text,
  `DE_AT_6393` text,
  `DE_AT_6394` text,
  `DE_AT_6395` text,
  `DE_AT_6396` bigint(38) default NULL,
  `DE_AT_6397` text,
  `DE_AT_6398` text,
  `DE_AT_6399` text,
  `DE_AT_6400` text,
  `DE_AT_6401` date default NULL,
  `DE_E_6376_6377_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6388`
--

LOCK TABLES `de_e_6388` WRITE;
/*!40000 ALTER TABLE `de_e_6388` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6388` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6405`
--

DROP TABLE IF EXISTS `de_e_6405`;
CREATE TABLE `de_e_6405` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6405`
--

LOCK TABLES `de_e_6405` WRITE;
/*!40000 ALTER TABLE `de_e_6405` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6405` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6407`
--

DROP TABLE IF EXISTS `de_e_6407`;
CREATE TABLE `de_e_6407` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6423` text,
  `DE_AT_6422` text,
  `DE_AT_6409` text,
  `DE_AT_6410` text,
  `DE_AT_6411` text,
  `DE_AT_6412` text,
  `DE_AT_6413` text,
  `DE_AT_6414` text,
  `DE_AT_6415` text,
  `DE_AT_6416` date default NULL,
  `DE_AT_6417` text,
  `DE_AT_6418` date default NULL,
  `DE_AT_6419` text,
  `DE_AT_6420` bigint(38) default NULL,
  `DE_AT_6421` text,
  `DE_E_6405_6406_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6407`
--

LOCK TABLES `de_e_6407` WRITE;
/*!40000 ALTER TABLE `de_e_6407` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6407` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6424`
--

DROP TABLE IF EXISTS `de_e_6424`;
CREATE TABLE `de_e_6424` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6431` text,
  `DE_AT_6430` text,
  `DE_AT_6429` date default NULL,
  `DE_AT_6428` text,
  `DE_AT_6427` bigint(38) default NULL,
  `DE_AT_6426` text,
  `DE_E_6407_6408_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6424`
--

LOCK TABLES `de_e_6424` WRITE;
/*!40000 ALTER TABLE `de_e_6424` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6424` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6432`
--

DROP TABLE IF EXISTS `de_e_6432`;
CREATE TABLE `de_e_6432` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6441` text,
  `DE_AT_6440` text,
  `DE_AT_6439` text,
  `DE_AT_6438` text,
  `DE_AT_6437` text,
  `DE_AT_6436` text,
  `DE_AT_6435` bigint(38) default NULL,
  `DE_AT_6434` text,
  `DE_E_6424_6425_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6432`
--

LOCK TABLES `de_e_6432` WRITE;
/*!40000 ALTER TABLE `de_e_6432` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6432` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6442`
--

DROP TABLE IF EXISTS `de_e_6442`;
CREATE TABLE `de_e_6442` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6443` text,
  `DE_AT_6444` text,
  `DE_AT_6445` text,
  `DE_AT_6446` int(38) default NULL,
  `DE_AT_6447` text,
  `DE_AT_6448` text,
  `DE_AT_6449` date default NULL,
  `DE_AT_6450` bigint(38) default NULL,
  `DE_AT_6451` text,
  `DE_AT_6452` tinyint(1) default NULL,
  `DE_AT_6453` int(38) default NULL,
  `DE_AT_6454` text,
  `DE_AT_6455` text,
  `DE_E_6432_6433_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6442`
--

LOCK TABLES `de_e_6442` WRITE;
/*!40000 ALTER TABLE `de_e_6442` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6442` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6456`
--

DROP TABLE IF EXISTS `de_e_6456`;
CREATE TABLE `de_e_6456` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6456`
--

LOCK TABLES `de_e_6456` WRITE;
/*!40000 ALTER TABLE `de_e_6456` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6456` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6458`
--

DROP TABLE IF EXISTS `de_e_6458`;
CREATE TABLE `de_e_6458` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6469` text,
  `DE_AT_6468` text,
  `DE_AT_6467` text,
  `DE_AT_6466` text,
  `DE_AT_6465` bigint(38) default NULL,
  `DE_AT_6464` text,
  `DE_AT_6463` text,
  `DE_AT_6462` text,
  `DE_AT_6461` text,
  `DE_AT_6460` text,
  `DE_E_6456_6457_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6458`
--

LOCK TABLES `de_e_6458` WRITE;
/*!40000 ALTER TABLE `de_e_6458` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6458` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6470`
--

DROP TABLE IF EXISTS `de_e_6470`;
CREATE TABLE `de_e_6470` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6472` text,
  `DE_AT_6473` text,
  `DE_AT_6474` text,
  `DE_AT_6475` int(38) default NULL,
  `DE_AT_6476` text,
  `DE_AT_6477` text,
  `DE_AT_6478` date default NULL,
  `DE_AT_6479` bigint(38) default NULL,
  `DE_AT_6480` text,
  `DE_AT_6481` tinyint(1) default NULL,
  `DE_AT_6482` int(38) default NULL,
  `DE_AT_6483` text,
  `DE_AT_6484` text,
  `DE_E_6458_6459_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6470`
--

LOCK TABLES `de_e_6470` WRITE;
/*!40000 ALTER TABLE `de_e_6470` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6470` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6485`
--

DROP TABLE IF EXISTS `de_e_6485`;
CREATE TABLE `de_e_6485` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6494` text,
  `DE_AT_6493` text,
  `DE_AT_6492` text,
  `DE_AT_6491` text,
  `DE_AT_6490` text,
  `DE_AT_6489` text,
  `DE_AT_6488` bigint(38) default NULL,
  `DE_AT_6487` text,
  `DE_E_6470_6471_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6485`
--

LOCK TABLES `de_e_6485` WRITE;
/*!40000 ALTER TABLE `de_e_6485` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6485` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6495`
--

DROP TABLE IF EXISTS `de_e_6495`;
CREATE TABLE `de_e_6495` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6502` text,
  `DE_AT_6501` text,
  `DE_AT_6500` date default NULL,
  `DE_AT_6499` text,
  `DE_AT_6498` bigint(38) default NULL,
  `DE_AT_6497` text,
  `DE_E_6485_6486_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6495`
--

LOCK TABLES `de_e_6495` WRITE;
/*!40000 ALTER TABLE `de_e_6495` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6495` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6503`
--

DROP TABLE IF EXISTS `de_e_6503`;
CREATE TABLE `de_e_6503` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6518` text,
  `DE_AT_6517` text,
  `DE_AT_6504` text,
  `DE_AT_6505` text,
  `DE_AT_6506` text,
  `DE_AT_6507` text,
  `DE_AT_6508` text,
  `DE_AT_6509` text,
  `DE_AT_6510` text,
  `DE_AT_6511` date default NULL,
  `DE_AT_6512` text,
  `DE_AT_6513` date default NULL,
  `DE_AT_6514` text,
  `DE_AT_6515` bigint(38) default NULL,
  `DE_AT_6516` text,
  `DE_E_6495_6496_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6503`
--

LOCK TABLES `de_e_6503` WRITE;
/*!40000 ALTER TABLE `de_e_6503` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6503` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6519`
--

DROP TABLE IF EXISTS `de_e_6519`;
CREATE TABLE `de_e_6519` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6519`
--

LOCK TABLES `de_e_6519` WRITE;
/*!40000 ALTER TABLE `de_e_6519` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6519` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6521`
--

DROP TABLE IF EXISTS `de_e_6521`;
CREATE TABLE `de_e_6521` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6532` text,
  `DE_AT_6531` text,
  `DE_AT_6530` text,
  `DE_AT_6529` text,
  `DE_AT_6528` text,
  `DE_AT_6527` text,
  `DE_AT_6526` text,
  `DE_AT_6525` bigint(38) default NULL,
  `DE_AT_6524` text,
  `DE_AT_6523` text,
  `DE_E_6519_6520_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6521`
--

LOCK TABLES `de_e_6521` WRITE;
/*!40000 ALTER TABLE `de_e_6521` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6521` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6533`
--

DROP TABLE IF EXISTS `de_e_6533`;
CREATE TABLE `de_e_6533` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6535` text,
  `DE_AT_6536` text,
  `DE_AT_6537` text,
  `DE_AT_6538` int(38) default NULL,
  `DE_AT_6539` text,
  `DE_AT_6540` text,
  `DE_AT_6541` date default NULL,
  `DE_AT_6542` bigint(38) default NULL,
  `DE_AT_6543` text,
  `DE_AT_6544` tinyint(1) default NULL,
  `DE_AT_6545` int(38) default NULL,
  `DE_AT_6546` text,
  `DE_AT_6547` text,
  `DE_E_6521_6522_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6533`
--

LOCK TABLES `de_e_6533` WRITE;
/*!40000 ALTER TABLE `de_e_6533` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6533` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6548`
--

DROP TABLE IF EXISTS `de_e_6548`;
CREATE TABLE `de_e_6548` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6557` text,
  `DE_AT_6556` text,
  `DE_AT_6555` text,
  `DE_AT_6554` text,
  `DE_AT_6553` text,
  `DE_AT_6552` text,
  `DE_AT_6551` bigint(38) default NULL,
  `DE_AT_6550` text,
  `DE_E_6533_6534_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6548`
--

LOCK TABLES `de_e_6548` WRITE;
/*!40000 ALTER TABLE `de_e_6548` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6548` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6558`
--

DROP TABLE IF EXISTS `de_e_6558`;
CREATE TABLE `de_e_6558` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6565` text,
  `DE_AT_6564` text,
  `DE_AT_6563` date default NULL,
  `DE_AT_6562` text,
  `DE_AT_6561` bigint(38) default NULL,
  `DE_AT_6560` text,
  `DE_E_6548_6549_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6558`
--

LOCK TABLES `de_e_6558` WRITE;
/*!40000 ALTER TABLE `de_e_6558` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6558` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6566`
--

DROP TABLE IF EXISTS `de_e_6566`;
CREATE TABLE `de_e_6566` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6581` text,
  `DE_AT_6580` text,
  `DE_AT_6567` text,
  `DE_AT_6568` text,
  `DE_AT_6569` text,
  `DE_AT_6570` text,
  `DE_AT_6571` text,
  `DE_AT_6572` text,
  `DE_AT_6573` text,
  `DE_AT_6574` date default NULL,
  `DE_AT_6575` text,
  `DE_AT_6576` date default NULL,
  `DE_AT_6577` text,
  `DE_AT_6578` bigint(38) default NULL,
  `DE_AT_6579` text,
  `DE_E_6558_6559_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6566`
--

LOCK TABLES `de_e_6566` WRITE;
/*!40000 ALTER TABLE `de_e_6566` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6566` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6582`
--

DROP TABLE IF EXISTS `de_e_6582`;
CREATE TABLE `de_e_6582` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6590` text,
  `DE_AT_6589` text,
  `DE_AT_6588` text,
  `DE_AT_6587` text,
  `DE_AT_6586` bigint(38) default NULL,
  `DE_AT_6585` text,
  `DE_AT_6584` bigint(38) default NULL,
  `DE_AT_6583` text,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6582`
--

LOCK TABLES `de_e_6582` WRITE;
/*!40000 ALTER TABLE `de_e_6582` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6582` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6591`
--

DROP TABLE IF EXISTS `de_e_6591`;
CREATE TABLE `de_e_6591` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6592` text,
  `DE_AT_6593` text,
  `DE_AT_6594` text,
  `DE_AT_6595` bigint(38) default NULL,
  `DE_AT_6596` text,
  `DE_AT_6597` text,
  `DE_AT_6598` text,
  `DE_AT_6599` text,
  `DE_AT_6600` text,
  `DE_AT_6601` text,
  `DE_AT_6602` text,
  `DE_AT_6603` text,
  `DE_AT_6604` text,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6591`
--

LOCK TABLES `de_e_6591` WRITE;
/*!40000 ALTER TABLE `de_e_6591` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6591` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6605`
--

DROP TABLE IF EXISTS `de_e_6605`;
CREATE TABLE `de_e_6605` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6605`
--

LOCK TABLES `de_e_6605` WRITE;
/*!40000 ALTER TABLE `de_e_6605` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6605` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6607`
--

DROP TABLE IF EXISTS `de_e_6607`;
CREATE TABLE `de_e_6607` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6614` text,
  `DE_AT_6613` text,
  `DE_AT_6612` text,
  `DE_AT_6611` text,
  `DE_AT_6610` int(38) default NULL,
  `DE_AT_6609` date default NULL,
  `DE_AT_6608` text,
  `DE_E_6605_6606_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6607`
--

LOCK TABLES `de_e_6607` WRITE;
/*!40000 ALTER TABLE `de_e_6607` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6607` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6615`
--

DROP TABLE IF EXISTS `de_e_6615`;
CREATE TABLE `de_e_6615` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6615`
--

LOCK TABLES `de_e_6615` WRITE;
/*!40000 ALTER TABLE `de_e_6615` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6615` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6617`
--

DROP TABLE IF EXISTS `de_e_6617`;
CREATE TABLE `de_e_6617` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6625` text,
  `DE_AT_6624` text,
  `DE_AT_6623` text,
  `DE_AT_6622` text,
  `DE_AT_6621` int(38) default NULL,
  `DE_AT_6620` date default NULL,
  `DE_AT_6619` text,
  `DE_E_6615_6616_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6617`
--

LOCK TABLES `de_e_6617` WRITE;
/*!40000 ALTER TABLE `de_e_6617` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6617` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6626`
--

DROP TABLE IF EXISTS `de_e_6626`;
CREATE TABLE `de_e_6626` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6638` text,
  `DE_AT_6637` text,
  `DE_AT_6636` text,
  `DE_AT_6635` bigint(38) default NULL,
  `DE_AT_6634` text,
  `DE_AT_6633` text,
  `DE_AT_6632` text,
  `DE_AT_6631` text,
  `DE_AT_6630` text,
  `DE_AT_6629` text,
  `DE_AT_6628` text,
  `DE_E_6617_6618_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6626`
--

LOCK TABLES `de_e_6626` WRITE;
/*!40000 ALTER TABLE `de_e_6626` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6626` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6639`
--

DROP TABLE IF EXISTS `de_e_6639`;
CREATE TABLE `de_e_6639` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6647` text,
  `DE_AT_6646` text,
  `DE_AT_6645` text,
  `DE_AT_6644` text,
  `DE_AT_6643` bigint(38) default NULL,
  `DE_AT_6642` text,
  `DE_AT_6641` text,
  `DE_E_6626_6627_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6639`
--

LOCK TABLES `de_e_6639` WRITE;
/*!40000 ALTER TABLE `de_e_6639` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6639` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6648`
--

DROP TABLE IF EXISTS `de_e_6648`;
CREATE TABLE `de_e_6648` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6658` text,
  `DE_AT_6657` text,
  `DE_AT_6656` bigint(38) default NULL,
  `DE_AT_6655` text,
  `DE_AT_6654` text,
  `DE_AT_6653` text,
  `DE_AT_6652` text,
  `DE_AT_6651` text,
  `DE_AT_6650` text,
  `DE_E_6639_6640_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6648`
--

LOCK TABLES `de_e_6648` WRITE;
/*!40000 ALTER TABLE `de_e_6648` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6648` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6659`
--

DROP TABLE IF EXISTS `de_e_6659`;
CREATE TABLE `de_e_6659` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6670` text,
  `DE_AT_6669` text,
  `DE_AT_6668` text,
  `DE_AT_6667` text,
  `DE_AT_6666` bigint(38) default NULL,
  `DE_AT_6665` text,
  `DE_AT_6664` text,
  `DE_AT_6663` text,
  `DE_AT_6662` text,
  `DE_AT_6661` text,
  `DE_E_6648_6649_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6659`
--

LOCK TABLES `de_e_6659` WRITE;
/*!40000 ALTER TABLE `de_e_6659` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6659` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6671`
--

DROP TABLE IF EXISTS `de_e_6671`;
CREATE TABLE `de_e_6671` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6687` date default NULL,
  `DE_AT_6686` text,
  `DE_AT_6685` text,
  `DE_AT_6672` text,
  `DE_AT_6673` text,
  `DE_AT_6674` text,
  `DE_AT_6675` text,
  `DE_AT_6676` text,
  `DE_AT_6677` text,
  `DE_AT_6678` text,
  `DE_AT_6679` bigint(38) default NULL,
  `DE_AT_6680` text,
  `DE_AT_6681` text,
  `DE_AT_6682` text,
  `DE_AT_6683` text,
  `DE_AT_6684` date default NULL,
  `DE_E_6659_6660_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6671`
--

LOCK TABLES `de_e_6671` WRITE;
/*!40000 ALTER TABLE `de_e_6671` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6671` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6688`
--

DROP TABLE IF EXISTS `de_e_6688`;
CREATE TABLE `de_e_6688` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6688`
--

LOCK TABLES `de_e_6688` WRITE;
/*!40000 ALTER TABLE `de_e_6688` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6688` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6690`
--

DROP TABLE IF EXISTS `de_e_6690`;
CREATE TABLE `de_e_6690` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6706` date default NULL,
  `DE_AT_6705` text,
  `DE_AT_6704` text,
  `DE_AT_6691` text,
  `DE_AT_6692` text,
  `DE_AT_6693` text,
  `DE_AT_6694` text,
  `DE_AT_6695` text,
  `DE_AT_6696` text,
  `DE_AT_6697` text,
  `DE_AT_6698` bigint(38) default NULL,
  `DE_AT_6699` text,
  `DE_AT_6700` text,
  `DE_AT_6701` text,
  `DE_AT_6702` text,
  `DE_AT_6703` date default NULL,
  `DE_E_6688_6689_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6690`
--

LOCK TABLES `de_e_6690` WRITE;
/*!40000 ALTER TABLE `de_e_6690` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6690` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6707`
--

DROP TABLE IF EXISTS `de_e_6707`;
CREATE TABLE `de_e_6707` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6707`
--

LOCK TABLES `de_e_6707` WRITE;
/*!40000 ALTER TABLE `de_e_6707` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6707` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6709`
--

DROP TABLE IF EXISTS `de_e_6709`;
CREATE TABLE `de_e_6709` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6717` text,
  `DE_AT_6716` text,
  `DE_AT_6715` text,
  `DE_AT_6714` text,
  `DE_AT_6713` int(38) default NULL,
  `DE_AT_6712` date default NULL,
  `DE_AT_6711` text,
  `DE_E_6707_6708_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6709`
--

LOCK TABLES `de_e_6709` WRITE;
/*!40000 ALTER TABLE `de_e_6709` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6709` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6718`
--

DROP TABLE IF EXISTS `de_e_6718`;
CREATE TABLE `de_e_6718` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6730` text,
  `DE_AT_6729` text,
  `DE_AT_6728` text,
  `DE_AT_6727` bigint(38) default NULL,
  `DE_AT_6726` text,
  `DE_AT_6725` text,
  `DE_AT_6724` text,
  `DE_AT_6723` text,
  `DE_AT_6722` text,
  `DE_AT_6721` text,
  `DE_AT_6720` text,
  `DE_E_6709_6710_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6718`
--

LOCK TABLES `de_e_6718` WRITE;
/*!40000 ALTER TABLE `de_e_6718` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6718` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6731`
--

DROP TABLE IF EXISTS `de_e_6731`;
CREATE TABLE `de_e_6731` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6739` text,
  `DE_AT_6738` text,
  `DE_AT_6737` text,
  `DE_AT_6736` text,
  `DE_AT_6735` bigint(38) default NULL,
  `DE_AT_6734` text,
  `DE_AT_6733` text,
  `DE_E_6718_6719_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6731`
--

LOCK TABLES `de_e_6731` WRITE;
/*!40000 ALTER TABLE `de_e_6731` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6731` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6740`
--

DROP TABLE IF EXISTS `de_e_6740`;
CREATE TABLE `de_e_6740` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6750` text,
  `DE_AT_6749` text,
  `DE_AT_6748` bigint(38) default NULL,
  `DE_AT_6747` text,
  `DE_AT_6746` text,
  `DE_AT_6745` text,
  `DE_AT_6744` text,
  `DE_AT_6743` text,
  `DE_AT_6742` text,
  `DE_E_6731_6732_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6740`
--

LOCK TABLES `de_e_6740` WRITE;
/*!40000 ALTER TABLE `de_e_6740` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6740` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6751`
--

DROP TABLE IF EXISTS `de_e_6751`;
CREATE TABLE `de_e_6751` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6762` text,
  `DE_AT_6761` text,
  `DE_AT_6760` text,
  `DE_AT_6759` text,
  `DE_AT_6758` bigint(38) default NULL,
  `DE_AT_6757` text,
  `DE_AT_6756` text,
  `DE_AT_6755` text,
  `DE_AT_6754` text,
  `DE_AT_6753` text,
  `DE_E_6740_6741_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6751`
--

LOCK TABLES `de_e_6751` WRITE;
/*!40000 ALTER TABLE `de_e_6751` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6751` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_6763`
--

DROP TABLE IF EXISTS `de_e_6763`;
CREATE TABLE `de_e_6763` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_6779` date default NULL,
  `DE_AT_6778` text,
  `DE_AT_6777` text,
  `DE_AT_6764` text,
  `DE_AT_6765` text,
  `DE_AT_6766` text,
  `DE_AT_6767` text,
  `DE_AT_6768` text,
  `DE_AT_6769` text,
  `DE_AT_6770` text,
  `DE_AT_6771` bigint(38) default NULL,
  `DE_AT_6772` text,
  `DE_AT_6773` text,
  `DE_AT_6774` text,
  `DE_AT_6775` text,
  `DE_AT_6776` date default NULL,
  `DE_E_6751_6752_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_6763`
--

LOCK TABLES `de_e_6763` WRITE;
/*!40000 ALTER TABLE `de_e_6763` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_6763` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_7554`
--

DROP TABLE IF EXISTS `de_e_7554`;
CREATE TABLE `de_e_7554` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_7554`
--

LOCK TABLES `de_e_7554` WRITE;
/*!40000 ALTER TABLE `de_e_7554` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_7554` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_7556`
--

DROP TABLE IF EXISTS `de_e_7556`;
CREATE TABLE `de_e_7556` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_7566` text,
  `DE_AT_7565` text,
  `DE_AT_7564` text,
  `DE_AT_7563` text,
  `DE_AT_7562` bigint(38) default NULL,
  `DE_AT_7561` text,
  `DE_AT_7560` text,
  `DE_AT_7559` text,
  `DE_AT_7558` text,
  `DE_AT_7557` text,
  `DE_E_7554_7555_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_7556`
--

LOCK TABLES `de_e_7556` WRITE;
/*!40000 ALTER TABLE `de_e_7556` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_7556` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_7567`
--

DROP TABLE IF EXISTS `de_e_7567`;
CREATE TABLE `de_e_7567` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_7567`
--

LOCK TABLES `de_e_7567` WRITE;
/*!40000 ALTER TABLE `de_e_7567` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_7567` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_7569`
--

DROP TABLE IF EXISTS `de_e_7569`;
CREATE TABLE `de_e_7569` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_7584` text,
  `DE_AT_7583` date default NULL,
  `DE_AT_7570` text,
  `DE_AT_7571` text,
  `DE_AT_7572` text,
  `DE_AT_7573` text,
  `DE_AT_7574` double default NULL,
  `DE_AT_7575` text,
  `DE_AT_7576` tinyint(1) default NULL,
  `DE_AT_7577` int(38) default NULL,
  `DE_AT_7578` int(38) default NULL,
  `DE_AT_7579` text,
  `DE_AT_7580` text,
  `DE_AT_7581` text,
  `DE_AT_7582` bigint(38) default NULL,
  `DE_E_7567_7568_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_7569`
--

LOCK TABLES `de_e_7569` WRITE;
/*!40000 ALTER TABLE `de_e_7569` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_7569` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_7585`
--

DROP TABLE IF EXISTS `de_e_7585`;
CREATE TABLE `de_e_7585` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_7585`
--

LOCK TABLES `de_e_7585` WRITE;
/*!40000 ALTER TABLE `de_e_7585` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_7585` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_7587`
--

DROP TABLE IF EXISTS `de_e_7587`;
CREATE TABLE `de_e_7587` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_7602` text,
  `DE_AT_7601` date default NULL,
  `DE_AT_7588` text,
  `DE_AT_7589` text,
  `DE_AT_7590` text,
  `DE_AT_7591` text,
  `DE_AT_7592` double default NULL,
  `DE_AT_7593` text,
  `DE_AT_7594` tinyint(1) default NULL,
  `DE_AT_7595` int(38) default NULL,
  `DE_AT_7596` int(38) default NULL,
  `DE_AT_7597` text,
  `DE_AT_7598` text,
  `DE_AT_7599` text,
  `DE_AT_7600` bigint(38) default NULL,
  `DE_E_7585_7586_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_7587`
--

LOCK TABLES `de_e_7587` WRITE;
/*!40000 ALTER TABLE `de_e_7587` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_7587` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_7603`
--

DROP TABLE IF EXISTS `de_e_7603`;
CREATE TABLE `de_e_7603` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_7603`
--

LOCK TABLES `de_e_7603` WRITE;
/*!40000 ALTER TABLE `de_e_7603` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_7603` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_7605`
--

DROP TABLE IF EXISTS `de_e_7605`;
CREATE TABLE `de_e_7605` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_7616` text,
  `DE_AT_7615` text,
  `DE_AT_7614` text,
  `DE_AT_7613` text,
  `DE_AT_7612` text,
  `DE_AT_7611` bigint(38) default NULL,
  `DE_AT_7610` text,
  `DE_AT_7609` text,
  `DE_AT_7608` text,
  `DE_AT_7607` text,
  `DE_E_7603_7604_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_7605`
--

LOCK TABLES `de_e_7605` WRITE;
/*!40000 ALTER TABLE `de_e_7605` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_7605` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_7617`
--

DROP TABLE IF EXISTS `de_e_7617`;
CREATE TABLE `de_e_7617` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_7624` text,
  `DE_AT_7623` text,
  `DE_AT_7622` text,
  `DE_AT_7621` text,
  `DE_AT_7620` text,
  `DE_AT_7619` text,
  `DE_E_7605_7606_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_7617`
--

LOCK TABLES `de_e_7617` WRITE;
/*!40000 ALTER TABLE `de_e_7617` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_7617` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_7625`
--

DROP TABLE IF EXISTS `de_e_7625`;
CREATE TABLE `de_e_7625` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_7631` text,
  `DE_AT_7630` text,
  `DE_AT_7629` int(38) default NULL,
  `DE_AT_7628` bigint(38) default NULL,
  `DE_AT_7627` text,
  `DE_AT_7626` int(38) default NULL,
  `DE_E_7617_7618_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_7625`
--

LOCK TABLES `de_e_7625` WRITE;
/*!40000 ALTER TABLE `de_e_7625` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_7625` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_7632`
--

DROP TABLE IF EXISTS `de_e_7632`;
CREATE TABLE `de_e_7632` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_7639` text,
  `DE_AT_7638` text,
  `DE_AT_7637` bigint(38) default NULL,
  `DE_AT_7636` text,
  `DE_AT_7635` text,
  `DE_AT_7634` int(38) default NULL,
  `DE_AT_7633` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_7632`
--

LOCK TABLES `de_e_7632` WRITE;
/*!40000 ALTER TABLE `de_e_7632` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_7632` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_7930`
--

DROP TABLE IF EXISTS `de_e_7930`;
CREATE TABLE `de_e_7930` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_7930`
--

LOCK TABLES `de_e_7930` WRITE;
/*!40000 ALTER TABLE `de_e_7930` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_7930` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_7932`
--

DROP TABLE IF EXISTS `de_e_7932`;
CREATE TABLE `de_e_7932` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_7948` text,
  `DE_AT_7947` text,
  `DE_AT_7946` date default NULL,
  `DE_AT_7933` bigint(38) default NULL,
  `DE_AT_7934` text,
  `DE_AT_7935` text,
  `DE_AT_7936` text,
  `DE_AT_7937` text,
  `DE_AT_7938` text,
  `DE_AT_7939` text,
  `DE_AT_7940` text,
  `DE_AT_7941` text,
  `DE_AT_7942` text,
  `DE_AT_7943` date default NULL,
  `DE_AT_7944` text,
  `DE_AT_7945` text,
  `DE_E_7930_7931_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_7932`
--

LOCK TABLES `de_e_7932` WRITE;
/*!40000 ALTER TABLE `de_e_7932` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_7932` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_7949`
--

DROP TABLE IF EXISTS `de_e_7949`;
CREATE TABLE `de_e_7949` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_7949`
--

LOCK TABLES `de_e_7949` WRITE;
/*!40000 ALTER TABLE `de_e_7949` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_7949` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_7951`
--

DROP TABLE IF EXISTS `de_e_7951`;
CREATE TABLE `de_e_7951` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_7967` date default NULL,
  `DE_AT_7966` text,
  `DE_AT_7965` text,
  `DE_AT_7952` text,
  `DE_AT_7953` text,
  `DE_AT_7954` text,
  `DE_AT_7955` text,
  `DE_AT_7956` text,
  `DE_AT_7957` text,
  `DE_AT_7958` text,
  `DE_AT_7959` bigint(38) default NULL,
  `DE_AT_7960` text,
  `DE_AT_7961` text,
  `DE_AT_7962` text,
  `DE_AT_7963` text,
  `DE_AT_7964` date default NULL,
  `DE_E_7949_7950_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_7951`
--

LOCK TABLES `de_e_7951` WRITE;
/*!40000 ALTER TABLE `de_e_7951` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_7951` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_7968`
--

DROP TABLE IF EXISTS `de_e_7968`;
CREATE TABLE `de_e_7968` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_7968`
--

LOCK TABLES `de_e_7968` WRITE;
/*!40000 ALTER TABLE `de_e_7968` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_7968` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_7970`
--

DROP TABLE IF EXISTS `de_e_7970`;
CREATE TABLE `de_e_7970` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_7981` text,
  `DE_AT_7980` text,
  `DE_AT_7979` text,
  `DE_AT_7978` text,
  `DE_AT_7977` bigint(38) default NULL,
  `DE_AT_7976` text,
  `DE_AT_7975` text,
  `DE_AT_7974` text,
  `DE_AT_7973` text,
  `DE_AT_7972` text,
  `DE_E_7968_7969_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_7970`
--

LOCK TABLES `de_e_7970` WRITE;
/*!40000 ALTER TABLE `de_e_7970` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_7970` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_e_7982`
--

DROP TABLE IF EXISTS `de_e_7982`;
CREATE TABLE `de_e_7982` (
  `ACTIVITY_STATUS` text,
  `IDENTIFIER` int(38) NOT NULL default '0',
  `DE_AT_7998` date default NULL,
  `DE_AT_7997` text,
  `DE_AT_7996` text,
  `DE_AT_7983` text,
  `DE_AT_7984` text,
  `DE_AT_7985` text,
  `DE_AT_7986` text,
  `DE_AT_7987` text,
  `DE_AT_7988` text,
  `DE_AT_7989` text,
  `DE_AT_7990` bigint(38) default NULL,
  `DE_AT_7991` text,
  `DE_AT_7992` text,
  `DE_AT_7993` text,
  `DE_AT_7994` text,
  `DE_AT_7995` date default NULL,
  `DE_E_7970_7971_IDENTIFIER` int(38) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_e_7982`
--

LOCK TABLES `de_e_7982` WRITE;
/*!40000 ALTER TABLE `de_e_7982` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_e_7982` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_file_attr_record_values`
--

DROP TABLE IF EXISTS `de_file_attr_record_values`;
CREATE TABLE `de_file_attr_record_values` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `CONTENT_TYPE` varchar(255) default NULL,
  `FILE_CONTENT` blob,
  `FILE_NAME` varchar(255) default NULL,
  `RECORD_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKE68334E74EB991B2` (`RECORD_ID`),
  CONSTRAINT `FKE68334E74EB991B2` FOREIGN KEY (`RECORD_ID`) REFERENCES `dyextn_attribute_record` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_file_attr_record_values`
--

LOCK TABLES `de_file_attr_record_values` WRITE;
/*!40000 ALTER TABLE `de_file_attr_record_values` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_file_attr_record_values` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `de_object_attr_record_values`
--

DROP TABLE IF EXISTS `de_object_attr_record_values`;
CREATE TABLE `de_object_attr_record_values` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `CLASS_NAME` varchar(255) default NULL,
  `OBJECT_CONTENT` blob,
  `RECORD_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK504EADC44EB991B2` (`RECORD_ID`),
  CONSTRAINT `FK504EADC44EB991B2` FOREIGN KEY (`RECORD_ID`) REFERENCES `dyextn_attribute_record` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `de_object_attr_record_values`
--

LOCK TABLES `de_object_attr_record_values` WRITE;
/*!40000 ALTER TABLE `de_object_attr_record_values` DISABLE KEYS */;
/*!40000 ALTER TABLE `de_object_attr_record_values` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_abstract_metadata`
--

DROP TABLE IF EXISTS `dyextn_abstract_metadata`;
CREATE TABLE `dyextn_abstract_metadata` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `CREATED_DATE` date default NULL,
  `DESCRIPTION` text,
  `LAST_UPDATED` date default NULL,
  `NAME` text,
  `PUBLIC_ID` varchar(255) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=InnoDB AUTO_INCREMENT=18701 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_abstract_metadata`
--

LOCK TABLES `dyextn_abstract_metadata` WRITE;
/*!40000 ALTER TABLE `dyextn_abstract_metadata` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_abstract_metadata` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_asso_display_attr`
--

DROP TABLE IF EXISTS `dyextn_asso_display_attr`;
CREATE TABLE `dyextn_asso_display_attr` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `SEQUENCE_NUMBER` int(11) default NULL,
  `DISPLAY_ATTRIBUTE_ID` bigint(20) default NULL,
  `SELECT_CONTROL_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKD12FD3827FD29CDD` (`SELECT_CONTROL_ID`),
  KEY `FKD12FD38235D6E973` (`DISPLAY_ATTRIBUTE_ID`),
  CONSTRAINT `FKD12FD38235D6E973` FOREIGN KEY (`DISPLAY_ATTRIBUTE_ID`) REFERENCES `dyextn_primitive_attribute` (`IDENTIFIER`),
  CONSTRAINT `FKD12FD3827FD29CDD` FOREIGN KEY (`SELECT_CONTROL_ID`) REFERENCES `dyextn_select_control` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_asso_display_attr`
--

LOCK TABLES `dyextn_asso_display_attr` WRITE;
/*!40000 ALTER TABLE `dyextn_asso_display_attr` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_asso_display_attr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_association`
--

DROP TABLE IF EXISTS `dyextn_association`;
CREATE TABLE `dyextn_association` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `DIRECTION` varchar(255) default NULL,
  `TARGET_ENTITY_ID` bigint(20) default NULL,
  `SOURCE_ROLE_ID` bigint(20) default NULL,
  `TARGET_ROLE_ID` bigint(20) default NULL,
  `IS_SYSTEM_GENERATED` bit(1) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK1046842440738A50` (`TARGET_ENTITY_ID`),
  KEY `FK1046842439780F7A` (`SOURCE_ROLE_ID`),
  KEY `FK104684242BD842F0` (`TARGET_ROLE_ID`),
  KEY `FK104684246D19A21F` (`IDENTIFIER`),
  CONSTRAINT `FK104684242BD842F0` FOREIGN KEY (`TARGET_ROLE_ID`) REFERENCES `dyextn_role` (`IDENTIFIER`),
  CONSTRAINT `FK1046842439780F7A` FOREIGN KEY (`SOURCE_ROLE_ID`) REFERENCES `dyextn_role` (`IDENTIFIER`),
  CONSTRAINT `FK1046842440738A50` FOREIGN KEY (`TARGET_ENTITY_ID`) REFERENCES `dyextn_entity` (`IDENTIFIER`),
  CONSTRAINT `FK104684246D19A21F` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_attribute` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_association`
--

LOCK TABLES `dyextn_association` WRITE;
/*!40000 ALTER TABLE `dyextn_association` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_association` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_attribute`
--

DROP TABLE IF EXISTS `dyextn_attribute`;
CREATE TABLE `dyextn_attribute` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `ENTIY_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK37F1E2FFF99EA906` (`ENTIY_ID`),
  KEY `FK37F1E2FF728B19BE` (`IDENTIFIER`),
  CONSTRAINT `FK37F1E2FF728B19BE` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_abstract_metadata` (`IDENTIFIER`),
  CONSTRAINT `FK37F1E2FFF99EA906` FOREIGN KEY (`ENTIY_ID`) REFERENCES `dyextn_entity` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_attribute`
--

LOCK TABLES `dyextn_attribute` WRITE;
/*!40000 ALTER TABLE `dyextn_attribute` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_attribute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_attribute_record`
--

DROP TABLE IF EXISTS `dyextn_attribute_record`;
CREATE TABLE `dyextn_attribute_record` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `ENTITY_ID` bigint(20) default NULL,
  `ATTRIBUTE_ID` bigint(20) default NULL,
  `RECORD_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK9B20ED914AC41F7E` (`ENTITY_ID`),
  KEY `FK9B20ED914DC2CD16` (`ATTRIBUTE_ID`),
  CONSTRAINT `FK9B20ED914AC41F7E` FOREIGN KEY (`ENTITY_ID`) REFERENCES `dyextn_entity` (`IDENTIFIER`),
  CONSTRAINT `FK9B20ED914DC2CD16` FOREIGN KEY (`ATTRIBUTE_ID`) REFERENCES `dyextn_primitive_attribute` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_attribute_record`
--

LOCK TABLES `dyextn_attribute_record` WRITE;
/*!40000 ALTER TABLE `dyextn_attribute_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_attribute_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_attribute_type_info`
--

DROP TABLE IF EXISTS `dyextn_attribute_type_info`;
CREATE TABLE `dyextn_attribute_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `PRIMITIVE_ATTRIBUTE_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK62596D531333996E` (`PRIMITIVE_ATTRIBUTE_ID`),
  CONSTRAINT `FK62596D531333996E` FOREIGN KEY (`PRIMITIVE_ATTRIBUTE_ID`) REFERENCES `dyextn_primitive_attribute` (`IDENTIFIER`)
) ENGINE=InnoDB AUTO_INCREMENT=10442 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_attribute_type_info`
--

LOCK TABLES `dyextn_attribute_type_info` WRITE;
/*!40000 ALTER TABLE `dyextn_attribute_type_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_attribute_type_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_barr_concept_value`
--

DROP TABLE IF EXISTS `dyextn_barr_concept_value`;
CREATE TABLE `dyextn_barr_concept_value` (
  `IDENTIFIER` bigint(20) NOT NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK89D27DF74641D513` (`IDENTIFIER`),
  CONSTRAINT `FK89D27DF74641D513` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_permissible_value` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_barr_concept_value`
--

LOCK TABLES `dyextn_barr_concept_value` WRITE;
/*!40000 ALTER TABLE `dyextn_barr_concept_value` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_barr_concept_value` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_boolean_concept_value`
--

DROP TABLE IF EXISTS `dyextn_boolean_concept_value`;
CREATE TABLE `dyextn_boolean_concept_value` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `VALUE` bit(1) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK57B6C4A64641D513` (`IDENTIFIER`),
  CONSTRAINT `FK57B6C4A64641D513` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_permissible_value` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_boolean_concept_value`
--

LOCK TABLES `dyextn_boolean_concept_value` WRITE;
/*!40000 ALTER TABLE `dyextn_boolean_concept_value` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_boolean_concept_value` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_boolean_type_info`
--

DROP TABLE IF EXISTS `dyextn_boolean_type_info`;
CREATE TABLE `dyextn_boolean_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK28F1809FE5294FA3` (`IDENTIFIER`),
  CONSTRAINT `FK28F1809FE5294FA3` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_attribute_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_boolean_type_info`
--

LOCK TABLES `dyextn_boolean_type_info` WRITE;
/*!40000 ALTER TABLE `dyextn_boolean_type_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_boolean_type_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_byte_array_type_info`
--

DROP TABLE IF EXISTS `dyextn_byte_array_type_info`;
CREATE TABLE `dyextn_byte_array_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `CONTENT_TYPE` varchar(255) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK18BDA73E5294FA3` (`IDENTIFIER`),
  CONSTRAINT `FK18BDA73E5294FA3` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_attribute_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_byte_array_type_info`
--

LOCK TABLES `dyextn_byte_array_type_info` WRITE;
/*!40000 ALTER TABLE `dyextn_byte_array_type_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_byte_array_type_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_cadsr_value_domain_info`
--

DROP TABLE IF EXISTS `dyextn_cadsr_value_domain_info`;
CREATE TABLE `dyextn_cadsr_value_domain_info` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `DATATYPE` varchar(255) default NULL,
  `NAME` varchar(255) default NULL,
  `TYPE` varchar(255) default NULL,
  `PRIMITIVE_ATTRIBUTE_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK1C9AA3641333996E` (`PRIMITIVE_ATTRIBUTE_ID`),
  CONSTRAINT `FK1C9AA3641333996E` FOREIGN KEY (`PRIMITIVE_ATTRIBUTE_ID`) REFERENCES `dyextn_primitive_attribute` (`IDENTIFIER`)
) ENGINE=InnoDB AUTO_INCREMENT=7543 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_cadsr_value_domain_info`
--

LOCK TABLES `dyextn_cadsr_value_domain_info` WRITE;
/*!40000 ALTER TABLE `dyextn_cadsr_value_domain_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_cadsr_value_domain_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_cadsrde`
--

DROP TABLE IF EXISTS `dyextn_cadsrde`;
CREATE TABLE `dyextn_cadsrde` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `PUBLIC_ID` varchar(255) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK588A250953CC4A77` (`IDENTIFIER`),
  CONSTRAINT `FK588A250953CC4A77` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_data_element` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_cadsrde`
--

LOCK TABLES `dyextn_cadsrde` WRITE;
/*!40000 ALTER TABLE `dyextn_cadsrde` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_cadsrde` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_check_box`
--

DROP TABLE IF EXISTS `dyextn_check_box`;
CREATE TABLE `dyextn_check_box` (
  `IDENTIFIER` bigint(20) NOT NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK4EFF925740F198C2` (`IDENTIFIER`),
  CONSTRAINT `FK4EFF925740F198C2` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_control` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_check_box`
--

LOCK TABLES `dyextn_check_box` WRITE;
/*!40000 ALTER TABLE `dyextn_check_box` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_check_box` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_column_properties`
--

DROP TABLE IF EXISTS `dyextn_column_properties`;
CREATE TABLE `dyextn_column_properties` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `PRIMITIVE_ATTRIBUTE_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK8FCE2B3F1333996E` (`PRIMITIVE_ATTRIBUTE_ID`),
  KEY `FK8FCE2B3F3AB6A1D3` (`IDENTIFIER`),
  CONSTRAINT `FK8FCE2B3F1333996E` FOREIGN KEY (`PRIMITIVE_ATTRIBUTE_ID`) REFERENCES `dyextn_primitive_attribute` (`IDENTIFIER`),
  CONSTRAINT `FK8FCE2B3F3AB6A1D3` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_database_properties` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_column_properties`
--

LOCK TABLES `dyextn_column_properties` WRITE;
/*!40000 ALTER TABLE `dyextn_column_properties` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_column_properties` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_combobox`
--

DROP TABLE IF EXISTS `dyextn_combobox`;
CREATE TABLE `dyextn_combobox` (
  `IDENTIFIER` bigint(20) NOT NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKABBC649ABF67AB26` (`IDENTIFIER`),
  CONSTRAINT `FKABBC649ABF67AB26` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_select_control` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_combobox`
--

LOCK TABLES `dyextn_combobox` WRITE;
/*!40000 ALTER TABLE `dyextn_combobox` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_combobox` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_constraint_properties`
--

DROP TABLE IF EXISTS `dyextn_constraint_properties`;
CREATE TABLE `dyextn_constraint_properties` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `SOURCE_ENTITY_KEY` varchar(255) default NULL,
  `TARGET_ENTITY_KEY` varchar(255) default NULL,
  `ASSOCIATION_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK82886CD87EE87FF6` (`ASSOCIATION_ID`),
  KEY `FK82886CD83AB6A1D3` (`IDENTIFIER`),
  CONSTRAINT `FK82886CD83AB6A1D3` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_database_properties` (`IDENTIFIER`),
  CONSTRAINT `FK82886CD87EE87FF6` FOREIGN KEY (`ASSOCIATION_ID`) REFERENCES `dyextn_association` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_constraint_properties`
--

LOCK TABLES `dyextn_constraint_properties` WRITE;
/*!40000 ALTER TABLE `dyextn_constraint_properties` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_constraint_properties` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_container`
--

DROP TABLE IF EXISTS `dyextn_container`;
CREATE TABLE `dyextn_container` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `BUTTON_CSS` varchar(255) default NULL,
  `CAPTION` varchar(255) default NULL,
  `ENTITY_ID` bigint(20) default NULL,
  `MAIN_TABLE_CSS` varchar(255) default NULL,
  `REQUIRED_FIELD_INDICATOR` varchar(255) default NULL,
  `REQUIRED_FIELD_WARNING_MESSAGE` varchar(255) default NULL,
  `TITLE_CSS` varchar(255) default NULL,
  `BASE_CONTAINER_ID` bigint(20) default NULL,
  `ENTITY_GROUP_ID` bigint(20) default NULL,
  `VIEW_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK1EAB84E44AC41F7E` (`ENTITY_ID`),
  KEY `FK1EAB84E4178865E` (`VIEW_ID`),
  KEY `FK1EAB84E488C075EF` (`ENTITY_GROUP_ID`),
  KEY `FK1EAB84E4BF901C84` (`BASE_CONTAINER_ID`),
  CONSTRAINT `FK1EAB84E4178865E` FOREIGN KEY (`VIEW_ID`) REFERENCES `dyextn_view` (`IDENTIFIER`),
  CONSTRAINT `FK1EAB84E44AC41F7E` FOREIGN KEY (`ENTITY_ID`) REFERENCES `dyextn_entity` (`IDENTIFIER`),
  CONSTRAINT `FK1EAB84E488C075EF` FOREIGN KEY (`ENTITY_GROUP_ID`) REFERENCES `dyextn_entity_group` (`IDENTIFIER`),
  CONSTRAINT `FK1EAB84E4BF901C84` FOREIGN KEY (`BASE_CONTAINER_ID`) REFERENCES `dyextn_container` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_container`
--

LOCK TABLES `dyextn_container` WRITE;
/*!40000 ALTER TABLE `dyextn_container` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_container` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_containment_control`
--

DROP TABLE IF EXISTS `dyextn_containment_control`;
CREATE TABLE `dyextn_containment_control` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `DISPLAY_CONTAINER_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK3F9D4AD351A77A33` (`DISPLAY_CONTAINER_ID`),
  KEY `FK3F9D4AD340F198C2` (`IDENTIFIER`),
  CONSTRAINT `FK3F9D4AD340F198C2` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_control` (`IDENTIFIER`),
  CONSTRAINT `FK3F9D4AD351A77A33` FOREIGN KEY (`DISPLAY_CONTAINER_ID`) REFERENCES `dyextn_container` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_containment_control`
--

LOCK TABLES `dyextn_containment_control` WRITE;
/*!40000 ALTER TABLE `dyextn_containment_control` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_containment_control` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_control`
--

DROP TABLE IF EXISTS `dyextn_control`;
CREATE TABLE `dyextn_control` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `CAPTION` varchar(255) default NULL,
  `CSS_CLASS` varchar(255) default NULL,
  `HIDDEN` bit(1) default NULL,
  `NAME` varchar(255) default NULL,
  `SEQUENCE_NUMBER` int(11) default NULL,
  `TOOLTIP` varchar(255) default NULL,
  `ABSTRACT_ATTRIBUTE_ID` bigint(20) default NULL,
  `CONTAINER_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK70FB5E8069935DD6` (`CONTAINER_ID`),
  KEY `FK70FB5E807769A811` (`ABSTRACT_ATTRIBUTE_ID`),
  CONSTRAINT `FK70FB5E8069935DD6` FOREIGN KEY (`CONTAINER_ID`) REFERENCES `dyextn_container` (`IDENTIFIER`),
  CONSTRAINT `FK70FB5E807769A811` FOREIGN KEY (`ABSTRACT_ATTRIBUTE_ID`) REFERENCES `dyextn_attribute` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_control`
--

LOCK TABLES `dyextn_control` WRITE;
/*!40000 ALTER TABLE `dyextn_control` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_control` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_data_element`
--

DROP TABLE IF EXISTS `dyextn_data_element`;
CREATE TABLE `dyextn_data_element` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `ATTRIBUTE_TYPE_INFO_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKB1153E4AA208204` (`ATTRIBUTE_TYPE_INFO_ID`),
  CONSTRAINT `FKB1153E4AA208204` FOREIGN KEY (`ATTRIBUTE_TYPE_INFO_ID`) REFERENCES `dyextn_attribute_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB AUTO_INCREMENT=1815 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_data_element`
--

LOCK TABLES `dyextn_data_element` WRITE;
/*!40000 ALTER TABLE `dyextn_data_element` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_data_element` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_data_grid`
--

DROP TABLE IF EXISTS `dyextn_data_grid`;
CREATE TABLE `dyextn_data_grid` (
  `IDENTIFIER` bigint(20) NOT NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK233EB73E40F198C2` (`IDENTIFIER`),
  CONSTRAINT `FK233EB73E40F198C2` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_control` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_data_grid`
--

LOCK TABLES `dyextn_data_grid` WRITE;
/*!40000 ALTER TABLE `dyextn_data_grid` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_data_grid` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_database_properties`
--

DROP TABLE IF EXISTS `dyextn_database_properties`;
CREATE TABLE `dyextn_database_properties` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=InnoDB AUTO_INCREMENT=18617 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_database_properties`
--

LOCK TABLES `dyextn_database_properties` WRITE;
/*!40000 ALTER TABLE `dyextn_database_properties` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_database_properties` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_date_concept_value`
--

DROP TABLE IF EXISTS `dyextn_date_concept_value`;
CREATE TABLE `dyextn_date_concept_value` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `VALUE` datetime default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK45F598A64641D513` (`IDENTIFIER`),
  CONSTRAINT `FK45F598A64641D513` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_permissible_value` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_date_concept_value`
--

LOCK TABLES `dyextn_date_concept_value` WRITE;
/*!40000 ALTER TABLE `dyextn_date_concept_value` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_date_concept_value` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_date_type_info`
--

DROP TABLE IF EXISTS `dyextn_date_type_info`;
CREATE TABLE `dyextn_date_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `FORMAT` varchar(255) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKFBA549FE5294FA3` (`IDENTIFIER`),
  CONSTRAINT `FKFBA549FE5294FA3` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_attribute_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_date_type_info`
--

LOCK TABLES `dyextn_date_type_info` WRITE;
/*!40000 ALTER TABLE `dyextn_date_type_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_date_type_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_datepicker`
--

DROP TABLE IF EXISTS `dyextn_datepicker`;
CREATE TABLE `dyextn_datepicker` (
  `IDENTIFIER` bigint(20) NOT NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKFEADD19940F198C2` (`IDENTIFIER`),
  CONSTRAINT `FKFEADD19940F198C2` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_control` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_datepicker`
--

LOCK TABLES `dyextn_datepicker` WRITE;
/*!40000 ALTER TABLE `dyextn_datepicker` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_datepicker` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_double_concept_value`
--

DROP TABLE IF EXISTS `dyextn_double_concept_value`;
CREATE TABLE `dyextn_double_concept_value` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `VALUE` double default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKB94E64494641D513` (`IDENTIFIER`),
  CONSTRAINT `FKB94E64494641D513` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_permissible_value` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_double_concept_value`
--

LOCK TABLES `dyextn_double_concept_value` WRITE;
/*!40000 ALTER TABLE `dyextn_double_concept_value` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_double_concept_value` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_double_type_info`
--

DROP TABLE IF EXISTS `dyextn_double_type_info`;
CREATE TABLE `dyextn_double_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKC83869C2BA4AE008` (`IDENTIFIER`),
  CONSTRAINT `FKC83869C2BA4AE008` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_numeric_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_double_type_info`
--

LOCK TABLES `dyextn_double_type_info` WRITE;
/*!40000 ALTER TABLE `dyextn_double_type_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_double_type_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_entity`
--

DROP TABLE IF EXISTS `dyextn_entity`;
CREATE TABLE `dyextn_entity` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `DATA_TABLE_STATE` int(11) default NULL,
  `IS_ABSTRACT` bit(1) default NULL,
  `PARENT_ENTITY_ID` bigint(20) default NULL,
  `INHERITANCE_STRATEGY` int(11) default NULL,
  `DISCRIMINATOR_COLUMN_NAME` varchar(255) default NULL,
  `DISCRIMINATOR_VALUE` varchar(255) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK8B2436402264D629` (`PARENT_ENTITY_ID`),
  KEY `FK8B243640728B19BE` (`IDENTIFIER`),
  CONSTRAINT `FK8B2436402264D629` FOREIGN KEY (`PARENT_ENTITY_ID`) REFERENCES `dyextn_entity` (`IDENTIFIER`),
  CONSTRAINT `FK8B243640728B19BE` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_abstract_metadata` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_entity`
--

LOCK TABLES `dyextn_entity` WRITE;
/*!40000 ALTER TABLE `dyextn_entity` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_entity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_entity_group`
--

DROP TABLE IF EXISTS `dyextn_entity_group`;
CREATE TABLE `dyextn_entity_group` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `LONG_NAME` varchar(255) default NULL,
  `SHORT_NAME` varchar(255) default NULL,
  `VERSION` varchar(255) default NULL,
  `IS_SYSTEM_GENERATED` bit(1) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK105DE7A0728B19BE` (`IDENTIFIER`),
  CONSTRAINT `FK105DE7A0728B19BE` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_abstract_metadata` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_entity_group`
--

LOCK TABLES `dyextn_entity_group` WRITE;
/*!40000 ALTER TABLE `dyextn_entity_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_entity_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_entity_group_rel`
--

DROP TABLE IF EXISTS `dyextn_entity_group_rel`;
CREATE TABLE `dyextn_entity_group_rel` (
  `ENTITY_GROUP_ID` bigint(20) NOT NULL,
  `ENTITY_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`ENTITY_ID`,`ENTITY_GROUP_ID`),
  KEY `FK5A0D835A4AC41F7E` (`ENTITY_ID`),
  KEY `FK5A0D835A88C075EF` (`ENTITY_GROUP_ID`),
  CONSTRAINT `FK5A0D835A4AC41F7E` FOREIGN KEY (`ENTITY_ID`) REFERENCES `dyextn_entity` (`IDENTIFIER`),
  CONSTRAINT `FK5A0D835A88C075EF` FOREIGN KEY (`ENTITY_GROUP_ID`) REFERENCES `dyextn_entity_group` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_entity_group_rel`
--

LOCK TABLES `dyextn_entity_group_rel` WRITE;
/*!40000 ALTER TABLE `dyextn_entity_group_rel` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_entity_group_rel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_entity_map`
--

DROP TABLE IF EXISTS `dyextn_entity_map`;
CREATE TABLE `dyextn_entity_map` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `CONTAINER_ID` bigint(20) default NULL,
  `STATUS` varchar(10) default NULL,
  `STATIC_ENTITY_ID` bigint(20) default NULL,
  `CREATED_DATE` date default NULL,
  `CREATED_BY` varchar(255) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_entity_map`
--

LOCK TABLES `dyextn_entity_map` WRITE;
/*!40000 ALTER TABLE `dyextn_entity_map` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_entity_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_entity_map_condns`
--

DROP TABLE IF EXISTS `dyextn_entity_map_condns`;
CREATE TABLE `dyextn_entity_map_condns` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `STATIC_RECORD_ID` bigint(20) default NULL,
  `TYPE_ID` bigint(20) default NULL,
  `FORM_CONTEXT_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK2A9D6029CFA08B13` (`FORM_CONTEXT_ID`),
  CONSTRAINT `FK2A9D6029CFA08B13` FOREIGN KEY (`FORM_CONTEXT_ID`) REFERENCES `dyextn_form_context` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_entity_map_condns`
--

LOCK TABLES `dyextn_entity_map_condns` WRITE;
/*!40000 ALTER TABLE `dyextn_entity_map_condns` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_entity_map_condns` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_entity_map_record`
--

DROP TABLE IF EXISTS `dyextn_entity_map_record`;
CREATE TABLE `dyextn_entity_map_record` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `FORM_CONTEXT_ID` bigint(20) default NULL,
  `STATIC_ENTITY_RECORD_ID` bigint(20) default NULL,
  `STATUS` varchar(10) default NULL,
  `DYNAMIC_ENTITY_RECORD_ID` bigint(20) default NULL,
  `MODIFIED_DATE` date default NULL,
  `CREATED_DATE` date default NULL,
  `CREATED_BY` varchar(255) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK43A45013CFA08B13` (`FORM_CONTEXT_ID`),
  CONSTRAINT `FK43A45013CFA08B13` FOREIGN KEY (`FORM_CONTEXT_ID`) REFERENCES `dyextn_form_context` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_entity_map_record`
--

LOCK TABLES `dyextn_entity_map_record` WRITE;
/*!40000 ALTER TABLE `dyextn_entity_map_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_entity_map_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_file_extensions`
--

DROP TABLE IF EXISTS `dyextn_file_extensions`;
CREATE TABLE `dyextn_file_extensions` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `FILE_EXTENSION` varchar(255) default NULL,
  `ATTRIBUTE_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKD49834FA56AF0834` (`ATTRIBUTE_ID`),
  CONSTRAINT `FKD49834FA56AF0834` FOREIGN KEY (`ATTRIBUTE_ID`) REFERENCES `dyextn_file_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_file_extensions`
--

LOCK TABLES `dyextn_file_extensions` WRITE;
/*!40000 ALTER TABLE `dyextn_file_extensions` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_file_extensions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_file_type_info`
--

DROP TABLE IF EXISTS `dyextn_file_type_info`;
CREATE TABLE `dyextn_file_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `MAX_FILE_SIZE` float default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKA00F0EDE5294FA3` (`IDENTIFIER`),
  CONSTRAINT `FKA00F0EDE5294FA3` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_attribute_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_file_type_info`
--

LOCK TABLES `dyextn_file_type_info` WRITE;
/*!40000 ALTER TABLE `dyextn_file_type_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_file_type_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_file_upload`
--

DROP TABLE IF EXISTS `dyextn_file_upload`;
CREATE TABLE `dyextn_file_upload` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `NO_OF_COLUMNS` int(11) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK2FAD41E740F198C2` (`IDENTIFIER`),
  CONSTRAINT `FK2FAD41E740F198C2` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_control` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_file_upload`
--

LOCK TABLES `dyextn_file_upload` WRITE;
/*!40000 ALTER TABLE `dyextn_file_upload` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_file_upload` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_float_concept_value`
--

DROP TABLE IF EXISTS `dyextn_float_concept_value`;
CREATE TABLE `dyextn_float_concept_value` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `VALUE` float default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK6785309A4641D513` (`IDENTIFIER`),
  CONSTRAINT `FK6785309A4641D513` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_permissible_value` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_float_concept_value`
--

LOCK TABLES `dyextn_float_concept_value` WRITE;
/*!40000 ALTER TABLE `dyextn_float_concept_value` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_float_concept_value` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_float_type_info`
--

DROP TABLE IF EXISTS `dyextn_float_type_info`;
CREATE TABLE `dyextn_float_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK7E1C0693BA4AE008` (`IDENTIFIER`),
  CONSTRAINT `FK7E1C0693BA4AE008` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_numeric_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_float_type_info`
--

LOCK TABLES `dyextn_float_type_info` WRITE;
/*!40000 ALTER TABLE `dyextn_float_type_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_float_type_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_form_context`
--

DROP TABLE IF EXISTS `dyextn_form_context`;
CREATE TABLE `dyextn_form_context` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `IS_INFINITE_ENTRY` bit(1) default NULL,
  `ENTITY_MAP_ID` bigint(20) default NULL,
  `STUDY_FORM_LABEL` varchar(255) default NULL,
  `NO_OF_ENTRIES` int(11) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKE56CCDB12B784475` (`ENTITY_MAP_ID`),
  CONSTRAINT `FKE56CCDB12B784475` FOREIGN KEY (`ENTITY_MAP_ID`) REFERENCES `dyextn_entity_map` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_form_context`
--

LOCK TABLES `dyextn_form_context` WRITE;
/*!40000 ALTER TABLE `dyextn_form_context` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_form_context` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_integer_concept_value`
--

DROP TABLE IF EXISTS `dyextn_integer_concept_value`;
CREATE TABLE `dyextn_integer_concept_value` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `VALUE` int(11) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKFBA33B3C4641D513` (`IDENTIFIER`),
  CONSTRAINT `FKFBA33B3C4641D513` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_permissible_value` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_integer_concept_value`
--

LOCK TABLES `dyextn_integer_concept_value` WRITE;
/*!40000 ALTER TABLE `dyextn_integer_concept_value` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_integer_concept_value` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_integer_type_info`
--

DROP TABLE IF EXISTS `dyextn_integer_type_info`;
CREATE TABLE `dyextn_integer_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK5F9CB235BA4AE008` (`IDENTIFIER`),
  CONSTRAINT `FK5F9CB235BA4AE008` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_numeric_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_integer_type_info`
--

LOCK TABLES `dyextn_integer_type_info` WRITE;
/*!40000 ALTER TABLE `dyextn_integer_type_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_integer_type_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_list_box`
--

DROP TABLE IF EXISTS `dyextn_list_box`;
CREATE TABLE `dyextn_list_box` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `MULTISELECT` bit(1) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK208395A7BF67AB26` (`IDENTIFIER`),
  CONSTRAINT `FK208395A7BF67AB26` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_select_control` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_list_box`
--

LOCK TABLES `dyextn_list_box` WRITE;
/*!40000 ALTER TABLE `dyextn_list_box` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_list_box` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_long_concept_value`
--

DROP TABLE IF EXISTS `dyextn_long_concept_value`;
CREATE TABLE `dyextn_long_concept_value` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `VALUE` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK3E1A6EF44641D513` (`IDENTIFIER`),
  CONSTRAINT `FK3E1A6EF44641D513` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_permissible_value` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_long_concept_value`
--

LOCK TABLES `dyextn_long_concept_value` WRITE;
/*!40000 ALTER TABLE `dyextn_long_concept_value` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_long_concept_value` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_long_type_info`
--

DROP TABLE IF EXISTS `dyextn_long_type_info`;
CREATE TABLE `dyextn_long_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK257281EDBA4AE008` (`IDENTIFIER`),
  CONSTRAINT `FK257281EDBA4AE008` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_numeric_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_long_type_info`
--

LOCK TABLES `dyextn_long_type_info` WRITE;
/*!40000 ALTER TABLE `dyextn_long_type_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_long_type_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_numeric_type_info`
--

DROP TABLE IF EXISTS `dyextn_numeric_type_info`;
CREATE TABLE `dyextn_numeric_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `MEASUREMENT_UNITS` varchar(255) default NULL,
  `DECIMAL_PLACES` int(11) default NULL,
  `NO_DIGITS` int(11) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK4DEC9544E5294FA3` (`IDENTIFIER`),
  CONSTRAINT `FK4DEC9544E5294FA3` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_attribute_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_numeric_type_info`
--

LOCK TABLES `dyextn_numeric_type_info` WRITE;
/*!40000 ALTER TABLE `dyextn_numeric_type_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_numeric_type_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_object_type_info`
--

DROP TABLE IF EXISTS `dyextn_object_type_info`;
CREATE TABLE `dyextn_object_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK74819FB0E5294FA3` (`IDENTIFIER`),
  CONSTRAINT `FK74819FB0E5294FA3` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_attribute_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_object_type_info`
--

LOCK TABLES `dyextn_object_type_info` WRITE;
/*!40000 ALTER TABLE `dyextn_object_type_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_object_type_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_permissible_value`
--

DROP TABLE IF EXISTS `dyextn_permissible_value`;
CREATE TABLE `dyextn_permissible_value` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `DESCRIPTION` varchar(255) default NULL,
  `ATTRIBUTE_TYPE_INFO_ID` bigint(20) default NULL,
  `USER_DEF_DE_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK136264E0AA208204` (`ATTRIBUTE_TYPE_INFO_ID`),
  KEY `FK136264E05521B106` (`USER_DEF_DE_ID`),
  CONSTRAINT `FK136264E05521B106` FOREIGN KEY (`USER_DEF_DE_ID`) REFERENCES `dyextn_userdefined_de` (`IDENTIFIER`),
  CONSTRAINT `FK136264E0AA208204` FOREIGN KEY (`ATTRIBUTE_TYPE_INFO_ID`) REFERENCES `dyextn_attribute_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB AUTO_INCREMENT=19333 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_permissible_value`
--

LOCK TABLES `dyextn_permissible_value` WRITE;
/*!40000 ALTER TABLE `dyextn_permissible_value` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_permissible_value` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_primitive_attribute`
--

DROP TABLE IF EXISTS `dyextn_primitive_attribute`;
CREATE TABLE `dyextn_primitive_attribute` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `IS_COLLECTION` bit(1) default NULL,
  `IS_IDENTIFIED` bit(1) default NULL,
  `IS_PRIMARY_KEY` bit(1) default NULL,
  `IS_NULLABLE` bit(1) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKA9F765C76D19A21F` (`IDENTIFIER`),
  CONSTRAINT `FKA9F765C76D19A21F` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_attribute` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_primitive_attribute`
--

LOCK TABLES `dyextn_primitive_attribute` WRITE;
/*!40000 ALTER TABLE `dyextn_primitive_attribute` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_primitive_attribute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_radiobutton`
--

DROP TABLE IF EXISTS `dyextn_radiobutton`;
CREATE TABLE `dyextn_radiobutton` (
  `IDENTIFIER` bigint(20) NOT NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK16F5BA9040F198C2` (`IDENTIFIER`),
  CONSTRAINT `FK16F5BA9040F198C2` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_control` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_radiobutton`
--

LOCK TABLES `dyextn_radiobutton` WRITE;
/*!40000 ALTER TABLE `dyextn_radiobutton` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_radiobutton` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_role`
--

DROP TABLE IF EXISTS `dyextn_role`;
CREATE TABLE `dyextn_role` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `ASSOCIATION_TYPE` varchar(255) default NULL,
  `MAX_CARDINALITY` int(11) default NULL,
  `MIN_CARDINALITY` int(11) default NULL,
  `NAME` varchar(255) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=InnoDB AUTO_INCREMENT=8821 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_role`
--

LOCK TABLES `dyextn_role` WRITE;
/*!40000 ALTER TABLE `dyextn_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_rule`
--

DROP TABLE IF EXISTS `dyextn_rule`;
CREATE TABLE `dyextn_rule` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) default NULL,
  `ATTRIBUTE_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKC27E09990F96714` (`ATTRIBUTE_ID`),
  CONSTRAINT `FKC27E09990F96714` FOREIGN KEY (`ATTRIBUTE_ID`) REFERENCES `dyextn_attribute` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_rule`
--

LOCK TABLES `dyextn_rule` WRITE;
/*!40000 ALTER TABLE `dyextn_rule` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_rule_parameter`
--

DROP TABLE IF EXISTS `dyextn_rule_parameter`;
CREATE TABLE `dyextn_rule_parameter` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) default NULL,
  `VALUE` varchar(255) default NULL,
  `RULE_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK2256736395D4A5AE` (`RULE_ID`),
  CONSTRAINT `FK2256736395D4A5AE` FOREIGN KEY (`RULE_ID`) REFERENCES `dyextn_rule` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_rule_parameter`
--

LOCK TABLES `dyextn_rule_parameter` WRITE;
/*!40000 ALTER TABLE `dyextn_rule_parameter` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_rule_parameter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_select_control`
--

DROP TABLE IF EXISTS `dyextn_select_control`;
CREATE TABLE `dyextn_select_control` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `SEPARATOR_STRING` varchar(255) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKDFEBB65740F198C2` (`IDENTIFIER`),
  CONSTRAINT `FKDFEBB65740F198C2` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_control` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_select_control`
--

LOCK TABLES `dyextn_select_control` WRITE;
/*!40000 ALTER TABLE `dyextn_select_control` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_select_control` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_semantic_property`
--

DROP TABLE IF EXISTS `dyextn_semantic_property`;
CREATE TABLE `dyextn_semantic_property` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `CONCEPT_CODE` varchar(255) default NULL,
  `TERM` varchar(255) default NULL,
  `THESAURAS_NAME` varchar(255) default NULL,
  `SEQUENCE_NUMBER` int(11) default NULL,
  `CONCEPT_DEFINITION` varchar(2048) default NULL,
  `ABSTRACT_METADATA_ID` bigint(20) default NULL,
  `ABSTRACT_VALUE_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKD2A0B5B15EB60E90` (`ABSTRACT_VALUE_ID`),
  KEY `FKD2A0B5B19AEB0CA3` (`ABSTRACT_METADATA_ID`),
  CONSTRAINT `FKD2A0B5B15EB60E90` FOREIGN KEY (`ABSTRACT_VALUE_ID`) REFERENCES `dyextn_permissible_value` (`IDENTIFIER`),
  CONSTRAINT `FKD2A0B5B19AEB0CA3` FOREIGN KEY (`ABSTRACT_METADATA_ID`) REFERENCES `dyextn_abstract_metadata` (`IDENTIFIER`)
) ENGINE=InnoDB AUTO_INCREMENT=33720 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_semantic_property`
--

LOCK TABLES `dyextn_semantic_property` WRITE;
/*!40000 ALTER TABLE `dyextn_semantic_property` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_semantic_property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_short_concept_value`
--

DROP TABLE IF EXISTS `dyextn_short_concept_value`;
CREATE TABLE `dyextn_short_concept_value` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `VALUE` smallint(6) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKC1945ABA4641D513` (`IDENTIFIER`),
  CONSTRAINT `FKC1945ABA4641D513` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_permissible_value` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_short_concept_value`
--

LOCK TABLES `dyextn_short_concept_value` WRITE;
/*!40000 ALTER TABLE `dyextn_short_concept_value` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_short_concept_value` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_short_type_info`
--

DROP TABLE IF EXISTS `dyextn_short_type_info`;
CREATE TABLE `dyextn_short_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK99540B3BA4AE008` (`IDENTIFIER`),
  CONSTRAINT `FK99540B3BA4AE008` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_numeric_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_short_type_info`
--

LOCK TABLES `dyextn_short_type_info` WRITE;
/*!40000 ALTER TABLE `dyextn_short_type_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_short_type_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_string_concept_value`
--

DROP TABLE IF EXISTS `dyextn_string_concept_value`;
CREATE TABLE `dyextn_string_concept_value` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `VALUE` varchar(255) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKADE7D8894641D513` (`IDENTIFIER`),
  CONSTRAINT `FKADE7D8894641D513` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_permissible_value` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_string_concept_value`
--

LOCK TABLES `dyextn_string_concept_value` WRITE;
/*!40000 ALTER TABLE `dyextn_string_concept_value` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_string_concept_value` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_string_type_info`
--

DROP TABLE IF EXISTS `dyextn_string_type_info`;
CREATE TABLE `dyextn_string_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `MAX_SIZE` int(11) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKDA35FE02E5294FA3` (`IDENTIFIER`),
  CONSTRAINT `FKDA35FE02E5294FA3` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_attribute_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_string_type_info`
--

LOCK TABLES `dyextn_string_type_info` WRITE;
/*!40000 ALTER TABLE `dyextn_string_type_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_string_type_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_table_properties`
--

DROP TABLE IF EXISTS `dyextn_table_properties`;
CREATE TABLE `dyextn_table_properties` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `ENTITY_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKE608E0814AC41F7E` (`ENTITY_ID`),
  KEY `FKE608E0813AB6A1D3` (`IDENTIFIER`),
  CONSTRAINT `FKE608E0813AB6A1D3` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_database_properties` (`IDENTIFIER`),
  CONSTRAINT `FKE608E0814AC41F7E` FOREIGN KEY (`ENTITY_ID`) REFERENCES `dyextn_entity` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_table_properties`
--

LOCK TABLES `dyextn_table_properties` WRITE;
/*!40000 ALTER TABLE `dyextn_table_properties` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_table_properties` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_tagged_value`
--

DROP TABLE IF EXISTS `dyextn_tagged_value`;
CREATE TABLE `dyextn_tagged_value` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `T_KEY` varchar(1024) default NULL,
  `T_VALUE` varchar(1024) default NULL,
  `ABSTRACT_METADATA_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKF79D055B9AEB0CA3` (`ABSTRACT_METADATA_ID`),
  CONSTRAINT `FKF79D055B9AEB0CA3` FOREIGN KEY (`ABSTRACT_METADATA_ID`) REFERENCES `dyextn_abstract_metadata` (`IDENTIFIER`)
) ENGINE=InnoDB AUTO_INCREMENT=13419 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_tagged_value`
--

LOCK TABLES `dyextn_tagged_value` WRITE;
/*!40000 ALTER TABLE `dyextn_tagged_value` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_tagged_value` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_textarea`
--

DROP TABLE IF EXISTS `dyextn_textarea`;
CREATE TABLE `dyextn_textarea` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `TEXTAREA_COLUMNS` int(11) default NULL,
  `TEXTAREA_ROWS` int(11) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK946EE25740F198C2` (`IDENTIFIER`),
  CONSTRAINT `FK946EE25740F198C2` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_control` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_textarea`
--

LOCK TABLES `dyextn_textarea` WRITE;
/*!40000 ALTER TABLE `dyextn_textarea` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_textarea` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_textfield`
--

DROP TABLE IF EXISTS `dyextn_textfield`;
CREATE TABLE `dyextn_textfield` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `NO_OF_COLUMNS` int(11) default NULL,
  `IS_PASSWORD` bit(1) default NULL,
  `IS_URL` bit(1) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKF9AFC85040F198C2` (`IDENTIFIER`),
  CONSTRAINT `FKF9AFC85040F198C2` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_control` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_textfield`
--

LOCK TABLES `dyextn_textfield` WRITE;
/*!40000 ALTER TABLE `dyextn_textfield` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_textfield` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_userdefined_de`
--

DROP TABLE IF EXISTS `dyextn_userdefined_de`;
CREATE TABLE `dyextn_userdefined_de` (
  `IDENTIFIER` bigint(20) NOT NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK630761FF53CC4A77` (`IDENTIFIER`),
  CONSTRAINT `FK630761FF53CC4A77` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_data_element` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_userdefined_de`
--

LOCK TABLES `dyextn_userdefined_de` WRITE;
/*!40000 ALTER TABLE `dyextn_userdefined_de` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_userdefined_de` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyextn_view`
--

DROP TABLE IF EXISTS `dyextn_view`;
CREATE TABLE `dyextn_view` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_view`
--

LOCK TABLES `dyextn_view` WRITE;
/*!40000 ALTER TABLE `dyextn_view` DISABLE KEYS */;
/*!40000 ALTER TABLE `dyextn_view` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inter_model_association`
--

DROP TABLE IF EXISTS `inter_model_association`;
CREATE TABLE `inter_model_association` (
  `ASSOCIATION_ID` bigint(20) NOT NULL,
  `LEFT_ENTITY_ID` bigint(20) NOT NULL,
  `LEFT_ATTRIBUTE_ID` bigint(20) NOT NULL,
  `RIGHT_ENTITY_ID` bigint(20) NOT NULL,
  `RIGHT_ATTRIBUTE_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`ASSOCIATION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `inter_model_association`
--

LOCK TABLES `inter_model_association` WRITE;
/*!40000 ALTER TABLE `inter_model_association` DISABLE KEYS */;
/*!40000 ALTER TABLE `inter_model_association` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `intra_model_association`
--

DROP TABLE IF EXISTS `intra_model_association`;
CREATE TABLE `intra_model_association` (
  `ASSOCIATION_ID` bigint(20) NOT NULL,
  `DE_ASSOCIATION_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`ASSOCIATION_ID`),
  UNIQUE KEY `DE_ASSOCIATION_ID` (`DE_ASSOCIATION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `intra_model_association`
--

LOCK TABLES `intra_model_association` WRITE;
/*!40000 ALTER TABLE `intra_model_association` DISABLE KEYS */;
/*!40000 ALTER TABLE `intra_model_association` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `output_class_urls`
--

DROP TABLE IF EXISTS `output_class_urls`;
CREATE TABLE `output_class_urls` (
  `CAB2B_QUERY_ID` bigint(20) NOT NULL,
  `OUTPUT_CLASS_URL` varchar(255) default NULL,
  `POSITION` int(11) NOT NULL,
  PRIMARY KEY  (`CAB2B_QUERY_ID`,`POSITION`),
  KEY `FKE131CD69A638FEFD` (`CAB2B_QUERY_ID`),
  CONSTRAINT `FKE131CD69A638FEFD` FOREIGN KEY (`CAB2B_QUERY_ID`) REFERENCES `cab2b_query` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `output_class_urls`
--

LOCK TABLES `output_class_urls` WRITE;
/*!40000 ALTER TABLE `output_class_urls` DISABLE KEYS */;
/*!40000 ALTER TABLE `output_class_urls` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `path`
--

DROP TABLE IF EXISTS `path`;
CREATE TABLE `path` (
  `PATH_ID` bigint(20) NOT NULL auto_increment,
  `FIRST_ENTITY_ID` bigint(20) default NULL,
  `INTERMEDIATE_PATH` varchar(1000) default NULL,
  `LAST_ENTITY_ID` bigint(20) default NULL,
  PRIMARY KEY  (`PATH_ID`),
  KEY `INDEX1` (`FIRST_ENTITY_ID`,`LAST_ENTITY_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=512115 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `path`
--

LOCK TABLES `path` WRITE;
/*!40000 ALTER TABLE `path` DISABLE KEYS */;
/*!40000 ALTER TABLE `path` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query`
--

DROP TABLE IF EXISTS `query`;
CREATE TABLE `query` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `CONSTRAINTS_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  UNIQUE KEY `CONSTRAINTS_ID` (`CONSTRAINTS_ID`),
  KEY `FK49D20A886AD86FC` (`IDENTIFIER`),
  KEY `FK49D20A89E2FD9C7` (`CONSTRAINTS_ID`),
  CONSTRAINT `FK49D20A886AD86FC` FOREIGN KEY (`IDENTIFIER`) REFERENCES `query_abstract_query` (`IDENTIFIER`),
  CONSTRAINT `FK49D20A89E2FD9C7` FOREIGN KEY (`CONSTRAINTS_ID`) REFERENCES `query_constraints` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query`
--

LOCK TABLES `query` WRITE;
/*!40000 ALTER TABLE `query` DISABLE KEYS */;
/*!40000 ALTER TABLE `query` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_abstract_query`
--

DROP TABLE IF EXISTS `query_abstract_query`;
CREATE TABLE `query_abstract_query` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `QUERY_NAME` varchar(255) default NULL,
  `QUERY_TYPE` varchar(30) default NULL,
  `DESCRIPTION` text,
  `CREATED_DATE` datetime NOT NULL,
  `CREATED_BY` bigint(20) NOT NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  UNIQUE KEY `QUERY_NAME` (`QUERY_NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=182 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_abstract_query`
--

LOCK TABLES `query_abstract_query` WRITE;
/*!40000 ALTER TABLE `query_abstract_query` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_abstract_query` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_arithmetic_operand`
--

DROP TABLE IF EXISTS `query_arithmetic_operand`;
CREATE TABLE `query_arithmetic_operand` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `LITERAL` varchar(255) default NULL,
  `TERM_TYPE` varchar(255) default NULL,
  `DATE_LITERAL` date default NULL,
  `TIME_INTERVAL` varchar(255) default NULL,
  `DE_ATTRIBUTE_ID` bigint(20) default NULL,
  `EXPRESSION_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK262AEB0BE92C814D` (`EXPRESSION_ID`),
  KEY `FK262AEB0BD635BD31` (`IDENTIFIER`),
  KEY `FK262AEB0B96C7CE5A` (`IDENTIFIER`),
  KEY `FK262AEB0BD006BE44` (`IDENTIFIER`),
  KEY `FK262AEB0B7223B197` (`IDENTIFIER`),
  KEY `FK262AEB0B687BE69E` (`IDENTIFIER`),
  CONSTRAINT `FK262AEB0B687BE69E` FOREIGN KEY (`IDENTIFIER`) REFERENCES `query_operand` (`IDENTIFIER`),
  CONSTRAINT `FK262AEB0B7223B197` FOREIGN KEY (`IDENTIFIER`) REFERENCES `query_operand` (`IDENTIFIER`),
  CONSTRAINT `FK262AEB0B96C7CE5A` FOREIGN KEY (`IDENTIFIER`) REFERENCES `query_operand` (`IDENTIFIER`),
  CONSTRAINT `FK262AEB0BD006BE44` FOREIGN KEY (`IDENTIFIER`) REFERENCES `query_operand` (`IDENTIFIER`),
  CONSTRAINT `FK262AEB0BD635BD31` FOREIGN KEY (`IDENTIFIER`) REFERENCES `query_operand` (`IDENTIFIER`),
  CONSTRAINT `FK262AEB0BE92C814D` FOREIGN KEY (`EXPRESSION_ID`) REFERENCES `query_base_expression` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_arithmetic_operand`
--

LOCK TABLES `query_arithmetic_operand` WRITE;
/*!40000 ALTER TABLE `query_arithmetic_operand` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_arithmetic_operand` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_base_expr_opnd`
--

DROP TABLE IF EXISTS `query_base_expr_opnd`;
CREATE TABLE `query_base_expr_opnd` (
  `BASE_EXPRESSION_ID` bigint(20) NOT NULL,
  `OPERAND_ID` bigint(20) NOT NULL,
  `POSITION` int(11) NOT NULL,
  PRIMARY KEY  (`BASE_EXPRESSION_ID`,`POSITION`),
  KEY `FKAE67EAF0712A4C` (`OPERAND_ID`),
  KEY `FKAE67EA48BA6890` (`BASE_EXPRESSION_ID`),
  CONSTRAINT `FKAE67EA48BA6890` FOREIGN KEY (`BASE_EXPRESSION_ID`) REFERENCES `query_base_expression` (`IDENTIFIER`),
  CONSTRAINT `FKAE67EAF0712A4C` FOREIGN KEY (`OPERAND_ID`) REFERENCES `query_operand` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_base_expr_opnd`
--

LOCK TABLES `query_base_expr_opnd` WRITE;
/*!40000 ALTER TABLE `query_base_expr_opnd` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_base_expr_opnd` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_base_expression`
--

DROP TABLE IF EXISTS `query_base_expression`;
CREATE TABLE `query_base_expression` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `EXPR_TYPE` varchar(255) NOT NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=InnoDB AUTO_INCREMENT=252 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_base_expression`
--

LOCK TABLES `query_base_expression` WRITE;
/*!40000 ALTER TABLE `query_base_expression` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_base_expression` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_baseexpr_to_connectors`
--

DROP TABLE IF EXISTS `query_baseexpr_to_connectors`;
CREATE TABLE `query_baseexpr_to_connectors` (
  `BASE_EXPRESSION_ID` bigint(20) NOT NULL,
  `CONNECTOR_ID` bigint(20) NOT NULL,
  `POSITION` int(11) NOT NULL,
  PRIMARY KEY  (`BASE_EXPRESSION_ID`,`POSITION`),
  KEY `FK3F0043482FCE1DA7` (`CONNECTOR_ID`),
  KEY `FK3F00434848BA6890` (`BASE_EXPRESSION_ID`),
  CONSTRAINT `FK3F0043482FCE1DA7` FOREIGN KEY (`CONNECTOR_ID`) REFERENCES `query_connector` (`IDENTIFIER`),
  CONSTRAINT `FK3F00434848BA6890` FOREIGN KEY (`BASE_EXPRESSION_ID`) REFERENCES `query_base_expression` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_baseexpr_to_connectors`
--

LOCK TABLES `query_baseexpr_to_connectors` WRITE;
/*!40000 ALTER TABLE `query_baseexpr_to_connectors` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_baseexpr_to_connectors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_composite_query`
--

DROP TABLE IF EXISTS `query_composite_query`;
CREATE TABLE `query_composite_query` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `OPERATION_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKD453833986AD86FC` (`IDENTIFIER`),
  KEY `FKD453833932224F67` (`OPERATION_ID`),
  CONSTRAINT `FKD453833932224F67` FOREIGN KEY (`OPERATION_ID`) REFERENCES `query_operation` (`IDENTIFIER`),
  CONSTRAINT `FKD453833986AD86FC` FOREIGN KEY (`IDENTIFIER`) REFERENCES `query_abstract_query` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_composite_query`
--

LOCK TABLES `query_composite_query` WRITE;
/*!40000 ALTER TABLE `query_composite_query` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_composite_query` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_condition`
--

DROP TABLE IF EXISTS `query_condition`;
CREATE TABLE `query_condition` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `ATTRIBUTE_ID` bigint(20) NOT NULL,
  `RELATIONAL_OPERATOR` varchar(255) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=InnoDB AUTO_INCREMENT=1770 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_condition`
--

LOCK TABLES `query_condition` WRITE;
/*!40000 ALTER TABLE `query_condition` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_condition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_condition_values`
--

DROP TABLE IF EXISTS `query_condition_values`;
CREATE TABLE `query_condition_values` (
  `CONDITION_ID` bigint(20) NOT NULL,
  `VALUE` varchar(255) default NULL,
  `POSITION` int(11) NOT NULL,
  PRIMARY KEY  (`CONDITION_ID`,`POSITION`),
  KEY `FK9997379D6458C2E7` (`CONDITION_ID`),
  CONSTRAINT `FK9997379D6458C2E7` FOREIGN KEY (`CONDITION_ID`) REFERENCES `query_condition` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_condition_values`
--

LOCK TABLES `query_condition_values` WRITE;
/*!40000 ALTER TABLE `query_condition_values` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_condition_values` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_connector`
--

DROP TABLE IF EXISTS `query_connector`;
CREATE TABLE `query_connector` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `OPERATOR` varchar(255) default NULL,
  `NESTING_NUMBER` int(11) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_connector`
--

LOCK TABLES `query_connector` WRITE;
/*!40000 ALTER TABLE `query_connector` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_connector` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_constraint_to_expr`
--

DROP TABLE IF EXISTS `query_constraint_to_expr`;
CREATE TABLE `query_constraint_to_expr` (
  `CONSTRAINT_ID` bigint(20) NOT NULL,
  `EXPRESSION_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`CONSTRAINT_ID`,`EXPRESSION_ID`),
  UNIQUE KEY `EXPRESSION_ID` (`EXPRESSION_ID`),
  KEY `FK2BD705CEE92C814D` (`EXPRESSION_ID`),
  KEY `FK2BD705CEA0A5F4C0` (`CONSTRAINT_ID`),
  CONSTRAINT `FK2BD705CEA0A5F4C0` FOREIGN KEY (`CONSTRAINT_ID`) REFERENCES `query_constraints` (`IDENTIFIER`),
  CONSTRAINT `FK2BD705CEE92C814D` FOREIGN KEY (`EXPRESSION_ID`) REFERENCES `query_base_expression` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_constraint_to_expr`
--

LOCK TABLES `query_constraint_to_expr` WRITE;
/*!40000 ALTER TABLE `query_constraint_to_expr` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_constraint_to_expr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_constraints`
--

DROP TABLE IF EXISTS `query_constraints`;
CREATE TABLE `query_constraints` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `QUERY_JOIN_GRAPH_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  UNIQUE KEY `QUERY_JOIN_GRAPH_ID` (`QUERY_JOIN_GRAPH_ID`),
  KEY `FKE364FCFF1C7EBF3B` (`QUERY_JOIN_GRAPH_ID`),
  CONSTRAINT `FKE364FCFF1C7EBF3B` FOREIGN KEY (`QUERY_JOIN_GRAPH_ID`) REFERENCES `query_join_graph` (`IDENTIFIER`)
) ENGINE=InnoDB AUTO_INCREMENT=184 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_constraints`
--

LOCK TABLES `query_constraints` WRITE;
/*!40000 ALTER TABLE `query_constraints` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_constraints` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_count_view`
--

DROP TABLE IF EXISTS `query_count_view`;
CREATE TABLE `query_count_view` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `COUNT_ENTITY_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK4A5C8BECF17325F` (`COUNT_ENTITY_ID`),
  KEY `FK4A5C8BEC89DB039E` (`IDENTIFIER`),
  CONSTRAINT `FK4A5C8BEC89DB039E` FOREIGN KEY (`IDENTIFIER`) REFERENCES `query_result_view` (`IDENTIFIER`),
  CONSTRAINT `FK4A5C8BECF17325F` FOREIGN KEY (`COUNT_ENTITY_ID`) REFERENCES `query_query_entity` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_count_view`
--

LOCK TABLES `query_count_view` WRITE;
/*!40000 ALTER TABLE `query_count_view` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_count_view` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_custom_formula`
--

DROP TABLE IF EXISTS `query_custom_formula`;
CREATE TABLE `query_custom_formula` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `OPERATOR` varchar(255) default NULL,
  `LHS_TERM_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK5C0EEAEFBE674D45` (`LHS_TERM_ID`),
  KEY `FK5C0EEAEF12D455EB` (`IDENTIFIER`),
  CONSTRAINT `FK5C0EEAEF12D455EB` FOREIGN KEY (`IDENTIFIER`) REFERENCES `query_operand` (`IDENTIFIER`),
  CONSTRAINT `FK5C0EEAEFBE674D45` FOREIGN KEY (`LHS_TERM_ID`) REFERENCES `query_base_expression` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_custom_formula`
--

LOCK TABLES `query_custom_formula` WRITE;
/*!40000 ALTER TABLE `query_custom_formula` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_custom_formula` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_data_view`
--

DROP TABLE IF EXISTS `query_data_view`;
CREATE TABLE `query_data_view` (
  `IDENTIFIER` bigint(20) NOT NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK2A3EA74389DB039E` (`IDENTIFIER`),
  CONSTRAINT `FK2A3EA74389DB039E` FOREIGN KEY (`IDENTIFIER`) REFERENCES `query_result_view` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_data_view`
--

LOCK TABLES `query_data_view` WRITE;
/*!40000 ALTER TABLE `query_data_view` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_data_view` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_expression`
--

DROP TABLE IF EXISTS `query_expression`;
CREATE TABLE `query_expression` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `IS_IN_VIEW` tinyint(1) default NULL,
  `IS_VISIBLE` tinyint(1) default NULL,
  `UI_EXPR_ID` int(11) default NULL,
  `QUERY_ENTITY_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK1B473A8F635766D8` (`QUERY_ENTITY_ID`),
  KEY `FK1B473A8F40EB75D4` (`IDENTIFIER`),
  CONSTRAINT `FK1B473A8F40EB75D4` FOREIGN KEY (`IDENTIFIER`) REFERENCES `query_base_expression` (`IDENTIFIER`),
  CONSTRAINT `FK1B473A8F635766D8` FOREIGN KEY (`QUERY_ENTITY_ID`) REFERENCES `query_query_entity` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_expression`
--

LOCK TABLES `query_expression` WRITE;
/*!40000 ALTER TABLE `query_expression` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_expression` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_formula_rhs`
--

DROP TABLE IF EXISTS `query_formula_rhs`;
CREATE TABLE `query_formula_rhs` (
  `CUSTOM_FORMULA_ID` bigint(20) NOT NULL,
  `RHS_TERM_ID` bigint(20) NOT NULL,
  `POSITION` int(11) NOT NULL,
  PRIMARY KEY  (`CUSTOM_FORMULA_ID`,`POSITION`),
  KEY `FKAE90F94D9A0B7164` (`CUSTOM_FORMULA_ID`),
  KEY `FKAE90F94D3BC37DCB` (`RHS_TERM_ID`),
  CONSTRAINT `FKAE90F94D3BC37DCB` FOREIGN KEY (`RHS_TERM_ID`) REFERENCES `query_base_expression` (`IDENTIFIER`),
  CONSTRAINT `FKAE90F94D9A0B7164` FOREIGN KEY (`CUSTOM_FORMULA_ID`) REFERENCES `query_operand` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_formula_rhs`
--

LOCK TABLES `query_formula_rhs` WRITE;
/*!40000 ALTER TABLE `query_formula_rhs` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_formula_rhs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_inter_model_association`
--

DROP TABLE IF EXISTS `query_inter_model_association`;
CREATE TABLE `query_inter_model_association` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `SOURCE_SERVICE_URL` varchar(255) NOT NULL,
  `TARGET_SERVICE_URL` varchar(255) NOT NULL,
  `SOURCE_ATTRIBUTE_ID` bigint(20) NOT NULL,
  `TARGET_ATTRIBUTE_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKD70658D15F5AB67E` (`IDENTIFIER`),
  CONSTRAINT `FKD70658D15F5AB67E` FOREIGN KEY (`IDENTIFIER`) REFERENCES `query_model_association` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_inter_model_association`
--

LOCK TABLES `query_inter_model_association` WRITE;
/*!40000 ALTER TABLE `query_inter_model_association` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_inter_model_association` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_intersection`
--

DROP TABLE IF EXISTS `query_intersection`;
CREATE TABLE `query_intersection` (
  `IDENTIFIER` bigint(20) NOT NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK2C1FACC0E201AD1D` (`IDENTIFIER`),
  CONSTRAINT `FK2C1FACC0E201AD1D` FOREIGN KEY (`IDENTIFIER`) REFERENCES `query_operation` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_intersection`
--

LOCK TABLES `query_intersection` WRITE;
/*!40000 ALTER TABLE `query_intersection` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_intersection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_intra_model_association`
--

DROP TABLE IF EXISTS `query_intra_model_association`;
CREATE TABLE `query_intra_model_association` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `DE_ASSOCIATION_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKF1EDBDD35F5AB67E` (`IDENTIFIER`),
  CONSTRAINT `FKF1EDBDD35F5AB67E` FOREIGN KEY (`IDENTIFIER`) REFERENCES `query_model_association` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_intra_model_association`
--

LOCK TABLES `query_intra_model_association` WRITE;
/*!40000 ALTER TABLE `query_intra_model_association` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_intra_model_association` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_join_graph`
--

DROP TABLE IF EXISTS `query_join_graph`;
CREATE TABLE `query_join_graph` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `COMMONS_GRAPH_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK2B41B5D09DBC4D94` (`COMMONS_GRAPH_ID`),
  CONSTRAINT `FK2B41B5D09DBC4D94` FOREIGN KEY (`COMMONS_GRAPH_ID`) REFERENCES `commons_graph` (`IDENTIFIER`)
) ENGINE=InnoDB AUTO_INCREMENT=184 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_join_graph`
--

LOCK TABLES `query_join_graph` WRITE;
/*!40000 ALTER TABLE `query_join_graph` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_join_graph` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_minus`
--

DROP TABLE IF EXISTS `query_minus`;
CREATE TABLE `query_minus` (
  `IDENTIFIER` bigint(20) NOT NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK7FD7D5F9E201AD1D` (`IDENTIFIER`),
  CONSTRAINT `FK7FD7D5F9E201AD1D` FOREIGN KEY (`IDENTIFIER`) REFERENCES `query_operation` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_minus`
--

LOCK TABLES `query_minus` WRITE;
/*!40000 ALTER TABLE `query_minus` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_minus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_model_association`
--

DROP TABLE IF EXISTS `query_model_association`;
CREATE TABLE `query_model_association` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_model_association`
--

LOCK TABLES `query_model_association` WRITE;
/*!40000 ALTER TABLE `query_model_association` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_model_association` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_operand`
--

DROP TABLE IF EXISTS `query_operand`;
CREATE TABLE `query_operand` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `OPND_TYPE` varchar(255) NOT NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=InnoDB AUTO_INCREMENT=296 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_operand`
--

LOCK TABLES `query_operand` WRITE;
/*!40000 ALTER TABLE `query_operand` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_operand` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_operation`
--

DROP TABLE IF EXISTS `query_operation`;
CREATE TABLE `query_operation` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `OPERAND_ONE` bigint(20) default NULL,
  `OPERAND_TWO` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKA13E4E70E4553443` (`OPERAND_ONE`),
  KEY `FKA13E4E70E4554829` (`OPERAND_TWO`),
  CONSTRAINT `FKA13E4E70E4553443` FOREIGN KEY (`OPERAND_ONE`) REFERENCES `query_abstract_query` (`IDENTIFIER`),
  CONSTRAINT `FKA13E4E70E4554829` FOREIGN KEY (`OPERAND_TWO`) REFERENCES `query_abstract_query` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_operation`
--

LOCK TABLES `query_operation` WRITE;
/*!40000 ALTER TABLE `query_operation` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_operation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_output_attribute`
--

DROP TABLE IF EXISTS `query_output_attribute`;
CREATE TABLE `query_output_attribute` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `EXPRESSION_ID` bigint(20) default NULL,
  `ATTRIBUTE_ID` bigint(20) NOT NULL,
  `PARAMETERIZED_QUERY_ID` bigint(20) default NULL,
  `POSITION` int(11) default NULL,
  `DATA_VIEW_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK22C9DB75E92C814D` (`EXPRESSION_ID`),
  KEY `FK22C9DB75F961BE22` (`DATA_VIEW_ID`),
  KEY `FK22C9DB75604D4BDA` (`PARAMETERIZED_QUERY_ID`),
  CONSTRAINT `FK22C9DB75604D4BDA` FOREIGN KEY (`PARAMETERIZED_QUERY_ID`) REFERENCES `query_parameterized_query` (`IDENTIFIER`),
  CONSTRAINT `FK22C9DB75E92C814D` FOREIGN KEY (`EXPRESSION_ID`) REFERENCES `query_base_expression` (`IDENTIFIER`),
  CONSTRAINT `FK22C9DB75F961BE22` FOREIGN KEY (`DATA_VIEW_ID`) REFERENCES `query_data_view` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_output_attribute`
--

LOCK TABLES `query_output_attribute` WRITE;
/*!40000 ALTER TABLE `query_output_attribute` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_output_attribute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_output_term`
--

DROP TABLE IF EXISTS `query_output_term`;
CREATE TABLE `query_output_term` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) default NULL,
  `TIME_INTERVAL` varchar(255) default NULL,
  `TERM_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK13C8A3D388C86B0D` (`TERM_ID`),
  CONSTRAINT `FK13C8A3D388C86B0D` FOREIGN KEY (`TERM_ID`) REFERENCES `query_base_expression` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_output_term`
--

LOCK TABLES `query_output_term` WRITE;
/*!40000 ALTER TABLE `query_output_term` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_output_term` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_parameter`
--

DROP TABLE IF EXISTS `query_parameter`;
CREATE TABLE `query_parameter` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) default NULL,
  `OBJECT_CLASS` varchar(255) default NULL,
  `OBJECT_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=InnoDB AUTO_INCREMENT=1615 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_parameter`
--

LOCK TABLES `query_parameter` WRITE;
/*!40000 ALTER TABLE `query_parameter` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_parameter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_parameterized_query`
--

DROP TABLE IF EXISTS `query_parameterized_query`;
CREATE TABLE `query_parameterized_query` (
  `IDENTIFIER` bigint(20) NOT NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKA272176B76177EFE` (`IDENTIFIER`),
  CONSTRAINT `FKA272176B76177EFE` FOREIGN KEY (`IDENTIFIER`) REFERENCES `query` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_parameterized_query`
--

LOCK TABLES `query_parameterized_query` WRITE;
/*!40000 ALTER TABLE `query_parameterized_query` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_parameterized_query` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_query_entity`
--

DROP TABLE IF EXISTS `query_query_entity`;
CREATE TABLE `query_query_entity` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `ENTITY_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=InnoDB AUTO_INCREMENT=252 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_query_entity`
--

LOCK TABLES `query_query_entity` WRITE;
/*!40000 ALTER TABLE `query_query_entity` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_query_entity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_result_view`
--

DROP TABLE IF EXISTS `query_result_view`;
CREATE TABLE `query_result_view` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_result_view`
--

LOCK TABLES `query_result_view` WRITE;
/*!40000 ALTER TABLE `query_result_view` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_result_view` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_rule_cond`
--

DROP TABLE IF EXISTS `query_rule_cond`;
CREATE TABLE `query_rule_cond` (
  `RULE_ID` bigint(20) NOT NULL,
  `CONDITION_ID` bigint(20) NOT NULL,
  `POSITION` int(11) NOT NULL,
  PRIMARY KEY  (`RULE_ID`,`POSITION`),
  KEY `FKC32D37AE6458C2E7` (`CONDITION_ID`),
  KEY `FKC32D37AE39F0A10D` (`RULE_ID`),
  CONSTRAINT `FKC32D37AE39F0A10D` FOREIGN KEY (`RULE_ID`) REFERENCES `query_operand` (`IDENTIFIER`),
  CONSTRAINT `FKC32D37AE6458C2E7` FOREIGN KEY (`CONDITION_ID`) REFERENCES `query_condition` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_rule_cond`
--

LOCK TABLES `query_rule_cond` WRITE;
/*!40000 ALTER TABLE `query_rule_cond` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_rule_cond` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_subexpr_operand`
--

DROP TABLE IF EXISTS `query_subexpr_operand`;
CREATE TABLE `query_subexpr_operand` (
  `IDENTIFIER` bigint(20) NOT NULL,
  `EXPRESSION_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK2BF760E8E92C814D` (`EXPRESSION_ID`),
  KEY `FK2BF760E832E875C8` (`IDENTIFIER`),
  CONSTRAINT `FK2BF760E832E875C8` FOREIGN KEY (`IDENTIFIER`) REFERENCES `query_operand` (`IDENTIFIER`),
  CONSTRAINT `FK2BF760E8E92C814D` FOREIGN KEY (`EXPRESSION_ID`) REFERENCES `query_base_expression` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_subexpr_operand`
--

LOCK TABLES `query_subexpr_operand` WRITE;
/*!40000 ALTER TABLE `query_subexpr_operand` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_subexpr_operand` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_to_output_terms`
--

DROP TABLE IF EXISTS `query_to_output_terms`;
CREATE TABLE `query_to_output_terms` (
  `QUERY_ID` bigint(20) NOT NULL,
  `OUTPUT_TERM_ID` bigint(20) NOT NULL,
  `POSITION` int(11) NOT NULL,
  PRIMARY KEY  (`QUERY_ID`,`POSITION`),
  UNIQUE KEY `OUTPUT_TERM_ID` (`OUTPUT_TERM_ID`),
  KEY `FK8A70E25691051647` (`QUERY_ID`),
  KEY `FK8A70E2565E5B9430` (`OUTPUT_TERM_ID`),
  CONSTRAINT `FK8A70E2565E5B9430` FOREIGN KEY (`OUTPUT_TERM_ID`) REFERENCES `query_output_term` (`IDENTIFIER`),
  CONSTRAINT `FK8A70E25691051647` FOREIGN KEY (`QUERY_ID`) REFERENCES `query` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_to_output_terms`
--

LOCK TABLES `query_to_output_terms` WRITE;
/*!40000 ALTER TABLE `query_to_output_terms` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_to_output_terms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_to_parameters`
--

DROP TABLE IF EXISTS `query_to_parameters`;
CREATE TABLE `query_to_parameters` (
  `QUERY_ID` bigint(20) NOT NULL,
  `PARAMETER_ID` bigint(20) NOT NULL,
  `POSITION` int(11) NOT NULL,
  PRIMARY KEY  (`QUERY_ID`,`POSITION`),
  UNIQUE KEY `PARAMETER_ID` (`PARAMETER_ID`),
  KEY `FK8060DAD739F0A314` (`QUERY_ID`),
  KEY `FK8060DAD7F84B9027` (`PARAMETER_ID`),
  CONSTRAINT `FK8060DAD739F0A314` FOREIGN KEY (`QUERY_ID`) REFERENCES `query_parameterized_query` (`IDENTIFIER`),
  CONSTRAINT `FK8060DAD7F84B9027` FOREIGN KEY (`PARAMETER_ID`) REFERENCES `query_parameter` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_to_parameters`
--

LOCK TABLES `query_to_parameters` WRITE;
/*!40000 ALTER TABLE `query_to_parameters` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_to_parameters` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_union`
--

DROP TABLE IF EXISTS `query_union`;
CREATE TABLE `query_union` (
  `IDENTIFIER` bigint(20) NOT NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK804AC458E201AD1D` (`IDENTIFIER`),
  CONSTRAINT `FK804AC458E201AD1D` FOREIGN KEY (`IDENTIFIER`) REFERENCES `query_operation` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_union`
--

LOCK TABLES `query_union` WRITE;
/*!40000 ALTER TABLE `query_union` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_union` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-03-11 16:20:36
