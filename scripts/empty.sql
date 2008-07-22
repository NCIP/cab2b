-- MySQL dump 10.9
--
-- Host: localhost    Database: cab2b
-- ------------------------------------------------------
-- Server version	4.1.10-nt

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `abstract_categorial_attribute`
--


/*!40000 ALTER TABLE `abstract_categorial_attribute` DISABLE KEYS */;
LOCK TABLES `abstract_categorial_attribute` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `abstract_categorial_attribute` ENABLE KEYS */;

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `abstract_categorial_class`
--


/*!40000 ALTER TABLE `abstract_categorial_class` DISABLE KEYS */;
LOCK TABLES `abstract_categorial_class` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `abstract_categorial_class` ENABLE KEYS */;

--
-- Table structure for table `abstract_category`
--

DROP TABLE IF EXISTS `abstract_category`;
CREATE TABLE `abstract_category` (
  `ID` bigint(20) NOT NULL auto_increment,
  `PARENT_CATEGORY_ID` bigint(20) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `PARENT_CATEGORY_ID` (`PARENT_CATEGORY_ID`),
  KEY `FK92BB047B70295EEC` (`PARENT_CATEGORY_ID`),
  CONSTRAINT `FK92BB047B70295EEC` FOREIGN KEY (`PARENT_CATEGORY_ID`) REFERENCES `abstract_category` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `abstract_category`
--


/*!40000 ALTER TABLE `abstract_category` DISABLE KEYS */;
LOCK TABLES `abstract_category` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `abstract_category` ENABLE KEYS */;

--
-- Table structure for table `association`
--

DROP TABLE IF EXISTS `association`;
CREATE TABLE `association` (
  `ASSOCIATION_ID` bigint(20) NOT NULL default '0',
  `ASSOCIATION_TYPE` int(8) NOT NULL default '0',
  PRIMARY KEY  (`ASSOCIATION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `association`
--


/*!40000 ALTER TABLE `association` DISABLE KEYS */;
LOCK TABLES `association` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `association` ENABLE KEYS */;

--
-- Table structure for table `cab2b_abstract_domain_object`
--

DROP TABLE IF EXISTS `cab2b_abstract_domain_object`;
CREATE TABLE `cab2b_abstract_domain_object` (
  `ADO_ID` bigint(20) NOT NULL auto_increment,
  `ADO_ACTIVITY_STATUS` varchar(50) default NULL,
  PRIMARY KEY  (`ADO_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_abstract_domain_object`
--


/*!40000 ALTER TABLE `cab2b_abstract_domain_object` DISABLE KEYS */;
LOCK TABLES `cab2b_abstract_domain_object` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `cab2b_abstract_domain_object` ENABLE KEYS */;

--
-- Table structure for table `cab2b_additional_meta_data`
--

DROP TABLE IF EXISTS `cab2b_additional_meta_data`;
CREATE TABLE `cab2b_additional_meta_data` (
  `AMD_ID` bigint(20) NOT NULL default '0',
  `NAME` varchar(50) default NULL,
  `DESCRIPTION` varchar(255) default NULL,
  `CREATED_ON` date default NULL,
  `LAST_UPDATED_ON` date default NULL,
  PRIMARY KEY  (`AMD_ID`),
  KEY `FKC025F1F773486922` (`AMD_ID`),
  CONSTRAINT `FKC025F1F773486922` FOREIGN KEY (`AMD_ID`) REFERENCES `cab2b_abstract_domain_object` (`ADO_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_additional_meta_data`
--


/*!40000 ALTER TABLE `cab2b_additional_meta_data` DISABLE KEYS */;
LOCK TABLES `cab2b_additional_meta_data` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `cab2b_additional_meta_data` ENABLE KEYS */;

--
-- Table structure for table `cab2b_data_category`
--

DROP TABLE IF EXISTS `cab2b_data_category`;
CREATE TABLE `cab2b_data_category` (
  `ID` bigint(20) NOT NULL default '0',
  `description` varchar(255) default NULL,
  `name` varchar(255) default NULL,
  `ROOT_CLASS_ID` bigint(20) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `name` (`name`),
  UNIQUE KEY `ROOT_CLASS_ID` (`ROOT_CLASS_ID`),
  KEY `FKFA70BDE8DF75106F` (`ROOT_CLASS_ID`),
  CONSTRAINT `FKFA70BDE8DF75106F` FOREIGN KEY (`ROOT_CLASS_ID`) REFERENCES `data_categorial_class` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_data_category`
--


/*!40000 ALTER TABLE `cab2b_data_category` DISABLE KEYS */;
LOCK TABLES `cab2b_data_category` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `cab2b_data_category` ENABLE KEYS */;

--
-- Table structure for table `cab2b_data_list`
--

DROP TABLE IF EXISTS `cab2b_data_list`;
CREATE TABLE `cab2b_data_list` (
  `DL_ID` bigint(20) NOT NULL default '0',
  `CUSTOM_DATA_CATEGORY` tinyint(1) default NULL,
  `ROOT_ENTITY_ID` bigint(20) default NULL,
  PRIMARY KEY  (`DL_ID`),
  KEY `FK789CBE883E23832` (`DL_ID`),
  CONSTRAINT `FK789CBE883E23832` FOREIGN KEY (`DL_ID`) REFERENCES `cab2b_additional_meta_data` (`AMD_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_data_list`
--


/*!40000 ALTER TABLE `cab2b_data_list` DISABLE KEYS */;
LOCK TABLES `cab2b_data_list` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `cab2b_data_list` ENABLE KEYS */;

--
-- Table structure for table `cab2b_datalist_entity`
--

DROP TABLE IF EXISTS `cab2b_datalist_entity`;
CREATE TABLE `cab2b_datalist_entity` (
  `DATALIST_METADATA_ID` bigint(20) NOT NULL default '0',
  `ENTITY_ID` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`DATALIST_METADATA_ID`,`ENTITY_ID`),
  KEY `FK96B3076FD1F8DDB4` (`DATALIST_METADATA_ID`),
  CONSTRAINT `FK96B3076FD1F8DDB4` FOREIGN KEY (`DATALIST_METADATA_ID`) REFERENCES `cab2b_data_list` (`DL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_datalist_entity`
--


/*!40000 ALTER TABLE `cab2b_datalist_entity` DISABLE KEYS */;
LOCK TABLES `cab2b_datalist_entity` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `cab2b_datalist_entity` ENABLE KEYS */;

--
-- Table structure for table `cab2b_exp_dlmetadata_mapping`
--

DROP TABLE IF EXISTS `cab2b_exp_dlmetadata_mapping`;
CREATE TABLE `cab2b_exp_dlmetadata_mapping` (
  `EXP_ID` bigint(20) NOT NULL default '0',
  `DL_ID` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`EXP_ID`,`DL_ID`),
  KEY `FK223A61533E23832` (`DL_ID`),
  KEY `FK223A61537ABC429D` (`EXP_ID`),
  CONSTRAINT `FK223A61537ABC429D` FOREIGN KEY (`EXP_ID`) REFERENCES `cab2b_experiment` (`EXP_ID`),
  CONSTRAINT `FK223A61533E23832` FOREIGN KEY (`DL_ID`) REFERENCES `cab2b_data_list` (`DL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_exp_dlmetadata_mapping`
--


/*!40000 ALTER TABLE `cab2b_exp_dlmetadata_mapping` DISABLE KEYS */;
LOCK TABLES `cab2b_exp_dlmetadata_mapping` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `cab2b_exp_dlmetadata_mapping` ENABLE KEYS */;

--
-- Table structure for table `cab2b_exp_grp_mapping`
--

DROP TABLE IF EXISTS `cab2b_exp_grp_mapping`;
CREATE TABLE `cab2b_exp_grp_mapping` (
  `EXG_ID` bigint(20) NOT NULL default '0',
  `EXP_ID` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`EXP_ID`,`EXG_ID`),
  KEY `FK1154C2A77AB82B46` (`EXG_ID`),
  KEY `FK1154C2A77ABC429D` (`EXP_ID`),
  CONSTRAINT `FK1154C2A77ABC429D` FOREIGN KEY (`EXP_ID`) REFERENCES `cab2b_experiment` (`EXP_ID`),
  CONSTRAINT `FK1154C2A77AB82B46` FOREIGN KEY (`EXG_ID`) REFERENCES `cab2b_experiment_group` (`EXG_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_exp_grp_mapping`
--


/*!40000 ALTER TABLE `cab2b_exp_grp_mapping` DISABLE KEYS */;
LOCK TABLES `cab2b_exp_grp_mapping` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `cab2b_exp_grp_mapping` ENABLE KEYS */;

--
-- Table structure for table `cab2b_experiment`
--

DROP TABLE IF EXISTS `cab2b_experiment`;
CREATE TABLE `cab2b_experiment` (
  `EXP_ID` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`EXP_ID`),
  KEY `FKFF270C287ABC429D` (`EXP_ID`),
  CONSTRAINT `FKFF270C287ABC429D` FOREIGN KEY (`EXP_ID`) REFERENCES `cab2b_additional_meta_data` (`AMD_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_experiment`
--


/*!40000 ALTER TABLE `cab2b_experiment` DISABLE KEYS */;
LOCK TABLES `cab2b_experiment` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `cab2b_experiment` ENABLE KEYS */;

--
-- Table structure for table `cab2b_experiment_group`
--

DROP TABLE IF EXISTS `cab2b_experiment_group`;
CREATE TABLE `cab2b_experiment_group` (
  `EXG_ID` bigint(20) NOT NULL default '0',
  `PARENT_EXG_ID` bigint(20) default NULL,
  PRIMARY KEY  (`EXG_ID`),
  KEY `FK7AD2AF8864FE787B` (`PARENT_EXG_ID`),
  KEY `FK7AD2AF887AB82B46` (`EXG_ID`),
  CONSTRAINT `FK7AD2AF887AB82B46` FOREIGN KEY (`EXG_ID`) REFERENCES `cab2b_additional_meta_data` (`AMD_ID`),
  CONSTRAINT `FK7AD2AF8864FE787B` FOREIGN KEY (`PARENT_EXG_ID`) REFERENCES `cab2b_experiment_group` (`EXG_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_experiment_group`
--


/*!40000 ALTER TABLE `cab2b_experiment_group` DISABLE KEYS */;
LOCK TABLES `cab2b_experiment_group` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `cab2b_experiment_group` ENABLE KEYS */;

--
-- Table structure for table `cab2b_id_table`
--

DROP TABLE IF EXISTS `cab2b_id_table`;
CREATE TABLE `cab2b_id_table` (
  `NEXT_ASSOCIATION_ID` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`NEXT_ASSOCIATION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_id_table`
--


/*!40000 ALTER TABLE `cab2b_id_table` DISABLE KEYS */;
LOCK TABLES `cab2b_id_table` WRITE;
INSERT INTO `cab2b_id_table` VALUES (1);
UNLOCK TABLES;
/*!40000 ALTER TABLE `cab2b_id_table` ENABLE KEYS */;

--
-- Table structure for table `cab2b_query`
--

DROP TABLE IF EXISTS `cab2b_query`;
CREATE TABLE `cab2b_query` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `ENTITY_ID` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_query`
--


/*!40000 ALTER TABLE `cab2b_query` DISABLE KEYS */;
LOCK TABLES `cab2b_query` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `cab2b_query` ENABLE KEYS */;

--
-- Table structure for table `cab2b_service_url`
--

DROP TABLE IF EXISTS `cab2b_service_url`;
CREATE TABLE `cab2b_service_url` (
  `URL_ID` bigint(20) NOT NULL auto_increment,
  `ENTITY_GROUP_NAME` text NOT NULL,
  `URL` text NOT NULL,
  `ADMIN_DEFINED` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`URL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_service_url`
--


/*!40000 ALTER TABLE `cab2b_service_url` DISABLE KEYS */;
LOCK TABLES `cab2b_service_url` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `cab2b_service_url` ENABLE KEYS */;

--
-- Table structure for table `cab2b_user`
--

DROP TABLE IF EXISTS `cab2b_user`;
CREATE TABLE `cab2b_user` (
  `USER_ID` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(30) NOT NULL default '',
  `PASSWORD` varchar(30) NOT NULL default '',
  `IS_ADMIN` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_user`
--


/*!40000 ALTER TABLE `cab2b_user` DISABLE KEYS */;
LOCK TABLES `cab2b_user` WRITE;
INSERT INTO `cab2b_user` VALUES (1,'Admin','admin123',1),(2,'cab2bUser','cab2b123',0);
UNLOCK TABLES;
/*!40000 ALTER TABLE `cab2b_user` ENABLE KEYS */;

--
-- Table structure for table `cab2b_user_url_mapping`
--

DROP TABLE IF EXISTS `cab2b_user_url_mapping`;
CREATE TABLE `cab2b_user_url_mapping` (
  `SERVICE_URL_ID` bigint(20) NOT NULL default '0',
  `USER_ID` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`USER_ID`,`SERVICE_URL_ID`),
  KEY `FKC64BBF4AAEC86F2D` (`USER_ID`),
  KEY `FKC64BBF4AB2004842` (`SERVICE_URL_ID`),
  CONSTRAINT `FKC64BBF4AB2004842` FOREIGN KEY (`SERVICE_URL_ID`) REFERENCES `cab2b_service_url` (`URL_ID`),
  CONSTRAINT `FKC64BBF4AAEC86F2D` FOREIGN KEY (`USER_ID`) REFERENCES `cab2b_user` (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cab2b_user_url_mapping`
--


/*!40000 ALTER TABLE `cab2b_user_url_mapping` DISABLE KEYS */;
LOCK TABLES `cab2b_user_url_mapping` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `cab2b_user_url_mapping` ENABLE KEYS */;

--
-- Table structure for table `categorial_attribute`
--

DROP TABLE IF EXISTS `categorial_attribute`;
CREATE TABLE `categorial_attribute` (
  `ID` bigint(20) NOT NULL default '0',
  `DE_CATEGORY_ATTRIBUTE_ID` bigint(20) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK31F77B5634ED55B7` (`ID`),
  CONSTRAINT `FK31F77B5634ED55B7` FOREIGN KEY (`ID`) REFERENCES `abstract_categorial_attribute` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `categorial_attribute`
--


/*!40000 ALTER TABLE `categorial_attribute` DISABLE KEYS */;
LOCK TABLES `categorial_attribute` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `categorial_attribute` ENABLE KEYS */;

--
-- Table structure for table `categorial_class`
--

DROP TABLE IF EXISTS `categorial_class`;
CREATE TABLE `categorial_class` (
  `ID` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`ID`),
  KEY `FK9651EF32F94A5493` (`ID`),
  CONSTRAINT `FK9651EF32F94A5493` FOREIGN KEY (`ID`) REFERENCES `abstract_categorial_class` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `categorial_class`
--


/*!40000 ALTER TABLE `categorial_class` DISABLE KEYS */;
LOCK TABLES `categorial_class` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `categorial_class` ENABLE KEYS */;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `ID` bigint(20) NOT NULL default '0',
  `DE_ENTITY_ID` bigint(20) default NULL,
  `ROOT_CLASS_ID` bigint(20) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `ROOT_CLASS_ID` (`ROOT_CLASS_ID`),
  KEY `FK31A8ACFEA2330820` (`ID`),
  KEY `FK31A8ACFEC88316F9` (`ROOT_CLASS_ID`),
  CONSTRAINT `FK31A8ACFEC88316F9` FOREIGN KEY (`ROOT_CLASS_ID`) REFERENCES `categorial_class` (`ID`),
  CONSTRAINT `FK31A8ACFEA2330820` FOREIGN KEY (`ID`) REFERENCES `abstract_category` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `category`
--


/*!40000 ALTER TABLE `category` DISABLE KEYS */;
LOCK TABLES `category` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `category` ENABLE KEYS */;

--
-- Table structure for table `commons_graph`
--

DROP TABLE IF EXISTS `commons_graph`;
CREATE TABLE `commons_graph` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `commons_graph`
--


/*!40000 ALTER TABLE `commons_graph` DISABLE KEYS */;
LOCK TABLES `commons_graph` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `commons_graph` ENABLE KEYS */;

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `commons_graph_edge`
--


/*!40000 ALTER TABLE `commons_graph_edge` DISABLE KEYS */;
LOCK TABLES `commons_graph_edge` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `commons_graph_edge` ENABLE KEYS */;

--
-- Table structure for table `commons_graph_to_edges`
--

DROP TABLE IF EXISTS `commons_graph_to_edges`;
CREATE TABLE `commons_graph_to_edges` (
  `GRAPH_ID` bigint(20) NOT NULL default '0',
  `EDGE_ID` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`GRAPH_ID`,`EDGE_ID`),
  UNIQUE KEY `EDGE_ID` (`EDGE_ID`),
  KEY `FKA6B0D8BAA0494B1D` (`GRAPH_ID`),
  KEY `FKA6B0D8BAFAEF80D` (`EDGE_ID`),
  CONSTRAINT `FKA6B0D8BAFAEF80D` FOREIGN KEY (`EDGE_ID`) REFERENCES `commons_graph_edge` (`IDENTIFIER`),
  CONSTRAINT `FKA6B0D8BAA0494B1D` FOREIGN KEY (`GRAPH_ID`) REFERENCES `commons_graph` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `commons_graph_to_edges`
--


/*!40000 ALTER TABLE `commons_graph_to_edges` DISABLE KEYS */;
LOCK TABLES `commons_graph_to_edges` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `commons_graph_to_edges` ENABLE KEYS */;

--
-- Table structure for table `commons_graph_to_vertices`
--

DROP TABLE IF EXISTS `commons_graph_to_vertices`;
CREATE TABLE `commons_graph_to_vertices` (
  `GRAPH_ID` bigint(20) NOT NULL default '0',
  `VERTEX_CLASS` varchar(255) default NULL,
  `VERTEX_ID` bigint(20) default NULL,
  KEY `FK2C4412F5A0494B1D` (`GRAPH_ID`),
  CONSTRAINT `FK2C4412F5A0494B1D` FOREIGN KEY (`GRAPH_ID`) REFERENCES `commons_graph` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `commons_graph_to_vertices`
--


/*!40000 ALTER TABLE `commons_graph_to_vertices` DISABLE KEYS */;
LOCK TABLES `commons_graph_to_vertices` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `commons_graph_to_vertices` ENABLE KEYS */;

--
-- Table structure for table `curated_path`
--

DROP TABLE IF EXISTS `curated_path`;
CREATE TABLE `curated_path` (
  `curated_path_Id` bigint(20) NOT NULL auto_increment,
  `entity_ids` text,
  `selected` tinyint(1) default NULL,
  PRIMARY KEY  (`curated_path_Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `curated_path`
--


/*!40000 ALTER TABLE `curated_path` DISABLE KEYS */;
LOCK TABLES `curated_path` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `curated_path` ENABLE KEYS */;

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


/*!40000 ALTER TABLE `curated_path_to_path` DISABLE KEYS */;
LOCK TABLES `curated_path_to_path` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `curated_path_to_path` ENABLE KEYS */;

--
-- Table structure for table `data_categorial_attribute`
--

DROP TABLE IF EXISTS `data_categorial_attribute`;
CREATE TABLE `data_categorial_attribute` (
  `ID` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `data_categorial_attribute`
--


/*!40000 ALTER TABLE `data_categorial_attribute` DISABLE KEYS */;
LOCK TABLES `data_categorial_attribute` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `data_categorial_attribute` ENABLE KEYS */;

--
-- Table structure for table `data_categorial_class`
--

DROP TABLE IF EXISTS `data_categorial_class`;
CREATE TABLE `data_categorial_class` (
  `ID` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `data_categorial_class`
--


/*!40000 ALTER TABLE `data_categorial_class` DISABLE KEYS */;
LOCK TABLES `data_categorial_class` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `data_categorial_class` ENABLE KEYS */;

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


/*!40000 ALTER TABLE `de_coll_attr_record_values` DISABLE KEYS */;
LOCK TABLES `de_coll_attr_record_values` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `de_coll_attr_record_values` ENABLE KEYS */;

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


/*!40000 ALTER TABLE `de_file_attr_record_values` DISABLE KEYS */;
LOCK TABLES `de_file_attr_record_values` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `de_file_attr_record_values` ENABLE KEYS */;

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


/*!40000 ALTER TABLE `de_object_attr_record_values` DISABLE KEYS */;
LOCK TABLES `de_object_attr_record_values` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `de_object_attr_record_values` ENABLE KEYS */;

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_abstract_metadata`
--


/*!40000 ALTER TABLE `dyextn_abstract_metadata` DISABLE KEYS */;
LOCK TABLES `dyextn_abstract_metadata` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_abstract_metadata` ENABLE KEYS */;

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


/*!40000 ALTER TABLE `dyextn_asso_display_attr` DISABLE KEYS */;
LOCK TABLES `dyextn_asso_display_attr` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_asso_display_attr` ENABLE KEYS */;

--
-- Table structure for table `dyextn_association`
--

DROP TABLE IF EXISTS `dyextn_association`;
CREATE TABLE `dyextn_association` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `DIRECTION` varchar(255) default NULL,
  `TARGET_ENTITY_ID` bigint(20) default NULL,
  `SOURCE_ROLE_ID` bigint(20) default NULL,
  `TARGET_ROLE_ID` bigint(20) default NULL,
  `IS_SYSTEM_GENERATED` tinyint(1) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK1046842440738A50` (`TARGET_ENTITY_ID`),
  KEY `FK1046842439780F7A` (`SOURCE_ROLE_ID`),
  KEY `FK104684242BD842F0` (`TARGET_ROLE_ID`),
  KEY `FK104684246D19A21F` (`IDENTIFIER`),
  CONSTRAINT `FK104684246D19A21F` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_attribute` (`IDENTIFIER`),
  CONSTRAINT `FK104684242BD842F0` FOREIGN KEY (`TARGET_ROLE_ID`) REFERENCES `dyextn_role` (`IDENTIFIER`),
  CONSTRAINT `FK1046842439780F7A` FOREIGN KEY (`SOURCE_ROLE_ID`) REFERENCES `dyextn_role` (`IDENTIFIER`),
  CONSTRAINT `FK1046842440738A50` FOREIGN KEY (`TARGET_ENTITY_ID`) REFERENCES `dyextn_entity` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_association`
--


/*!40000 ALTER TABLE `dyextn_association` DISABLE KEYS */;
LOCK TABLES `dyextn_association` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_association` ENABLE KEYS */;

--
-- Table structure for table `dyextn_attribute`
--

DROP TABLE IF EXISTS `dyextn_attribute`;
CREATE TABLE `dyextn_attribute` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
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


/*!40000 ALTER TABLE `dyextn_attribute` DISABLE KEYS */;
LOCK TABLES `dyextn_attribute` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_attribute` ENABLE KEYS */;

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
  CONSTRAINT `FK9B20ED914DC2CD16` FOREIGN KEY (`ATTRIBUTE_ID`) REFERENCES `dyextn_primitive_attribute` (`IDENTIFIER`),
  CONSTRAINT `FK9B20ED914AC41F7E` FOREIGN KEY (`ENTITY_ID`) REFERENCES `dyextn_entity` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_attribute_record`
--


/*!40000 ALTER TABLE `dyextn_attribute_record` DISABLE KEYS */;
LOCK TABLES `dyextn_attribute_record` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_attribute_record` ENABLE KEYS */;

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_attribute_type_info`
--


/*!40000 ALTER TABLE `dyextn_attribute_type_info` DISABLE KEYS */;
LOCK TABLES `dyextn_attribute_type_info` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_attribute_type_info` ENABLE KEYS */;

--
-- Table structure for table `dyextn_barr_concept_value`
--

DROP TABLE IF EXISTS `dyextn_barr_concept_value`;
CREATE TABLE `dyextn_barr_concept_value` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK89D27DF74641D513` (`IDENTIFIER`),
  CONSTRAINT `FK89D27DF74641D513` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_permissible_value` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_barr_concept_value`
--


/*!40000 ALTER TABLE `dyextn_barr_concept_value` DISABLE KEYS */;
LOCK TABLES `dyextn_barr_concept_value` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_barr_concept_value` ENABLE KEYS */;

--
-- Table structure for table `dyextn_boolean_concept_value`
--

DROP TABLE IF EXISTS `dyextn_boolean_concept_value`;
CREATE TABLE `dyextn_boolean_concept_value` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `VALUE` tinyint(1) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK57B6C4A64641D513` (`IDENTIFIER`),
  CONSTRAINT `FK57B6C4A64641D513` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_permissible_value` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_boolean_concept_value`
--


/*!40000 ALTER TABLE `dyextn_boolean_concept_value` DISABLE KEYS */;
LOCK TABLES `dyextn_boolean_concept_value` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_boolean_concept_value` ENABLE KEYS */;

--
-- Table structure for table `dyextn_boolean_type_info`
--

DROP TABLE IF EXISTS `dyextn_boolean_type_info`;
CREATE TABLE `dyextn_boolean_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK28F1809FE5294FA3` (`IDENTIFIER`),
  CONSTRAINT `FK28F1809FE5294FA3` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_attribute_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_boolean_type_info`
--


/*!40000 ALTER TABLE `dyextn_boolean_type_info` DISABLE KEYS */;
LOCK TABLES `dyextn_boolean_type_info` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_boolean_type_info` ENABLE KEYS */;

--
-- Table structure for table `dyextn_byte_array_type_info`
--

DROP TABLE IF EXISTS `dyextn_byte_array_type_info`;
CREATE TABLE `dyextn_byte_array_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `CONTENT_TYPE` varchar(255) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK18BDA73E5294FA3` (`IDENTIFIER`),
  CONSTRAINT `FK18BDA73E5294FA3` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_attribute_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_byte_array_type_info`
--


/*!40000 ALTER TABLE `dyextn_byte_array_type_info` DISABLE KEYS */;
LOCK TABLES `dyextn_byte_array_type_info` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_byte_array_type_info` ENABLE KEYS */;

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_cadsr_value_domain_info`
--


/*!40000 ALTER TABLE `dyextn_cadsr_value_domain_info` DISABLE KEYS */;
LOCK TABLES `dyextn_cadsr_value_domain_info` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_cadsr_value_domain_info` ENABLE KEYS */;

--
-- Table structure for table `dyextn_cadsrde`
--

DROP TABLE IF EXISTS `dyextn_cadsrde`;
CREATE TABLE `dyextn_cadsrde` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `PUBLIC_ID` varchar(255) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK588A250953CC4A77` (`IDENTIFIER`),
  CONSTRAINT `FK588A250953CC4A77` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_data_element` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_cadsrde`
--


/*!40000 ALTER TABLE `dyextn_cadsrde` DISABLE KEYS */;
LOCK TABLES `dyextn_cadsrde` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_cadsrde` ENABLE KEYS */;

--
-- Table structure for table `dyextn_check_box`
--

DROP TABLE IF EXISTS `dyextn_check_box`;
CREATE TABLE `dyextn_check_box` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK4EFF925740F198C2` (`IDENTIFIER`),
  CONSTRAINT `FK4EFF925740F198C2` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_control` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_check_box`
--


/*!40000 ALTER TABLE `dyextn_check_box` DISABLE KEYS */;
LOCK TABLES `dyextn_check_box` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_check_box` ENABLE KEYS */;

--
-- Table structure for table `dyextn_column_properties`
--

DROP TABLE IF EXISTS `dyextn_column_properties`;
CREATE TABLE `dyextn_column_properties` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `PRIMITIVE_ATTRIBUTE_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK8FCE2B3F1333996E` (`PRIMITIVE_ATTRIBUTE_ID`),
  KEY `FK8FCE2B3F3AB6A1D3` (`IDENTIFIER`),
  CONSTRAINT `FK8FCE2B3F3AB6A1D3` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_database_properties` (`IDENTIFIER`),
  CONSTRAINT `FK8FCE2B3F1333996E` FOREIGN KEY (`PRIMITIVE_ATTRIBUTE_ID`) REFERENCES `dyextn_primitive_attribute` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_column_properties`
--


/*!40000 ALTER TABLE `dyextn_column_properties` DISABLE KEYS */;
LOCK TABLES `dyextn_column_properties` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_column_properties` ENABLE KEYS */;

--
-- Table structure for table `dyextn_combobox`
--

DROP TABLE IF EXISTS `dyextn_combobox`;
CREATE TABLE `dyextn_combobox` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKABBC649ABF67AB26` (`IDENTIFIER`),
  CONSTRAINT `FKABBC649ABF67AB26` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_select_control` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_combobox`
--


/*!40000 ALTER TABLE `dyextn_combobox` DISABLE KEYS */;
LOCK TABLES `dyextn_combobox` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_combobox` ENABLE KEYS */;

--
-- Table structure for table `dyextn_constraint_properties`
--

DROP TABLE IF EXISTS `dyextn_constraint_properties`;
CREATE TABLE `dyextn_constraint_properties` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
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


/*!40000 ALTER TABLE `dyextn_constraint_properties` DISABLE KEYS */;
LOCK TABLES `dyextn_constraint_properties` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_constraint_properties` ENABLE KEYS */;

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
  CONSTRAINT `FK1EAB84E4BF901C84` FOREIGN KEY (`BASE_CONTAINER_ID`) REFERENCES `dyextn_container` (`IDENTIFIER`),
  CONSTRAINT `FK1EAB84E4178865E` FOREIGN KEY (`VIEW_ID`) REFERENCES `dyextn_view` (`IDENTIFIER`),
  CONSTRAINT `FK1EAB84E44AC41F7E` FOREIGN KEY (`ENTITY_ID`) REFERENCES `dyextn_entity` (`IDENTIFIER`),
  CONSTRAINT `FK1EAB84E488C075EF` FOREIGN KEY (`ENTITY_GROUP_ID`) REFERENCES `dyextn_entity_group` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_container`
--


/*!40000 ALTER TABLE `dyextn_container` DISABLE KEYS */;
LOCK TABLES `dyextn_container` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_container` ENABLE KEYS */;

--
-- Table structure for table `dyextn_containment_control`
--

DROP TABLE IF EXISTS `dyextn_containment_control`;
CREATE TABLE `dyextn_containment_control` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
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


/*!40000 ALTER TABLE `dyextn_containment_control` DISABLE KEYS */;
LOCK TABLES `dyextn_containment_control` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_containment_control` ENABLE KEYS */;

--
-- Table structure for table `dyextn_control`
--

DROP TABLE IF EXISTS `dyextn_control`;
CREATE TABLE `dyextn_control` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `CAPTION` varchar(255) default NULL,
  `CSS_CLASS` varchar(255) default NULL,
  `HIDDEN` tinyint(1) default NULL,
  `NAME` varchar(255) default NULL,
  `SEQUENCE_NUMBER` int(11) default NULL,
  `TOOLTIP` varchar(255) default NULL,
  `ABSTRACT_ATTRIBUTE_ID` bigint(20) default NULL,
  `CONTAINER_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK70FB5E8069935DD6` (`CONTAINER_ID`),
  KEY `FK70FB5E807769A811` (`ABSTRACT_ATTRIBUTE_ID`),
  CONSTRAINT `FK70FB5E807769A811` FOREIGN KEY (`ABSTRACT_ATTRIBUTE_ID`) REFERENCES `dyextn_attribute` (`IDENTIFIER`),
  CONSTRAINT `FK70FB5E8069935DD6` FOREIGN KEY (`CONTAINER_ID`) REFERENCES `dyextn_container` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_control`
--


/*!40000 ALTER TABLE `dyextn_control` DISABLE KEYS */;
LOCK TABLES `dyextn_control` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_control` ENABLE KEYS */;

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_data_element`
--


/*!40000 ALTER TABLE `dyextn_data_element` DISABLE KEYS */;
LOCK TABLES `dyextn_data_element` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_data_element` ENABLE KEYS */;

--
-- Table structure for table `dyextn_data_grid`
--

DROP TABLE IF EXISTS `dyextn_data_grid`;
CREATE TABLE `dyextn_data_grid` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK233EB73E40F198C2` (`IDENTIFIER`),
  CONSTRAINT `FK233EB73E40F198C2` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_control` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_data_grid`
--


/*!40000 ALTER TABLE `dyextn_data_grid` DISABLE KEYS */;
LOCK TABLES `dyextn_data_grid` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_data_grid` ENABLE KEYS */;

--
-- Table structure for table `dyextn_database_properties`
--

DROP TABLE IF EXISTS `dyextn_database_properties`;
CREATE TABLE `dyextn_database_properties` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_database_properties`
--


/*!40000 ALTER TABLE `dyextn_database_properties` DISABLE KEYS */;
LOCK TABLES `dyextn_database_properties` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_database_properties` ENABLE KEYS */;

--
-- Table structure for table `dyextn_date_concept_value`
--

DROP TABLE IF EXISTS `dyextn_date_concept_value`;
CREATE TABLE `dyextn_date_concept_value` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `VALUE` datetime default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK45F598A64641D513` (`IDENTIFIER`),
  CONSTRAINT `FK45F598A64641D513` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_permissible_value` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_date_concept_value`
--


/*!40000 ALTER TABLE `dyextn_date_concept_value` DISABLE KEYS */;
LOCK TABLES `dyextn_date_concept_value` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_date_concept_value` ENABLE KEYS */;

--
-- Table structure for table `dyextn_date_type_info`
--

DROP TABLE IF EXISTS `dyextn_date_type_info`;
CREATE TABLE `dyextn_date_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `FORMAT` varchar(255) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKFBA549FE5294FA3` (`IDENTIFIER`),
  CONSTRAINT `FKFBA549FE5294FA3` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_attribute_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_date_type_info`
--


/*!40000 ALTER TABLE `dyextn_date_type_info` DISABLE KEYS */;
LOCK TABLES `dyextn_date_type_info` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_date_type_info` ENABLE KEYS */;

--
-- Table structure for table `dyextn_datepicker`
--

DROP TABLE IF EXISTS `dyextn_datepicker`;
CREATE TABLE `dyextn_datepicker` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKFEADD19940F198C2` (`IDENTIFIER`),
  CONSTRAINT `FKFEADD19940F198C2` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_control` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_datepicker`
--


/*!40000 ALTER TABLE `dyextn_datepicker` DISABLE KEYS */;
LOCK TABLES `dyextn_datepicker` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_datepicker` ENABLE KEYS */;

--
-- Table structure for table `dyextn_double_concept_value`
--

DROP TABLE IF EXISTS `dyextn_double_concept_value`;
CREATE TABLE `dyextn_double_concept_value` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `VALUE` double default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKB94E64494641D513` (`IDENTIFIER`),
  CONSTRAINT `FKB94E64494641D513` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_permissible_value` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_double_concept_value`
--


/*!40000 ALTER TABLE `dyextn_double_concept_value` DISABLE KEYS */;
LOCK TABLES `dyextn_double_concept_value` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_double_concept_value` ENABLE KEYS */;

--
-- Table structure for table `dyextn_double_type_info`
--

DROP TABLE IF EXISTS `dyextn_double_type_info`;
CREATE TABLE `dyextn_double_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKC83869C2BA4AE008` (`IDENTIFIER`),
  CONSTRAINT `FKC83869C2BA4AE008` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_numeric_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_double_type_info`
--


/*!40000 ALTER TABLE `dyextn_double_type_info` DISABLE KEYS */;
LOCK TABLES `dyextn_double_type_info` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_double_type_info` ENABLE KEYS */;

--
-- Table structure for table `dyextn_entity`
--

DROP TABLE IF EXISTS `dyextn_entity`;
CREATE TABLE `dyextn_entity` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `DATA_TABLE_STATE` int(11) default NULL,
  `IS_ABSTRACT` tinyint(1) default NULL,
  `PARENT_ENTITY_ID` bigint(20) default NULL,
  `INHERITANCE_STRATEGY` int(11) default NULL,
  `DISCRIMINATOR_COLUMN_NAME` varchar(255) default NULL,
  `DISCRIMINATOR_VALUE` varchar(255) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK8B2436402264D629` (`PARENT_ENTITY_ID`),
  KEY `FK8B243640728B19BE` (`IDENTIFIER`),
  CONSTRAINT `FK8B243640728B19BE` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_abstract_metadata` (`IDENTIFIER`),
  CONSTRAINT `FK8B2436402264D629` FOREIGN KEY (`PARENT_ENTITY_ID`) REFERENCES `dyextn_entity` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_entity`
--


/*!40000 ALTER TABLE `dyextn_entity` DISABLE KEYS */;
LOCK TABLES `dyextn_entity` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_entity` ENABLE KEYS */;

--
-- Table structure for table `dyextn_entity_group`
--

DROP TABLE IF EXISTS `dyextn_entity_group`;
CREATE TABLE `dyextn_entity_group` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `LONG_NAME` varchar(255) default NULL,
  `SHORT_NAME` varchar(255) default NULL,
  `VERSION` varchar(255) default NULL,
  `IS_SYSTEM_GENERATED` tinyint(1) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK105DE7A0728B19BE` (`IDENTIFIER`),
  CONSTRAINT `FK105DE7A0728B19BE` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_abstract_metadata` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_entity_group`
--


/*!40000 ALTER TABLE `dyextn_entity_group` DISABLE KEYS */;
LOCK TABLES `dyextn_entity_group` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_entity_group` ENABLE KEYS */;

--
-- Table structure for table `dyextn_entity_group_rel`
--

DROP TABLE IF EXISTS `dyextn_entity_group_rel`;
CREATE TABLE `dyextn_entity_group_rel` (
  `ENTITY_GROUP_ID` bigint(20) NOT NULL default '0',
  `ENTITY_ID` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`ENTITY_ID`,`ENTITY_GROUP_ID`),
  KEY `FK5A0D835A4AC41F7E` (`ENTITY_ID`),
  KEY `FK5A0D835A88C075EF` (`ENTITY_GROUP_ID`),
  CONSTRAINT `FK5A0D835A88C075EF` FOREIGN KEY (`ENTITY_GROUP_ID`) REFERENCES `dyextn_entity_group` (`IDENTIFIER`),
  CONSTRAINT `FK5A0D835A4AC41F7E` FOREIGN KEY (`ENTITY_ID`) REFERENCES `dyextn_entity` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_entity_group_rel`
--


/*!40000 ALTER TABLE `dyextn_entity_group_rel` DISABLE KEYS */;
LOCK TABLES `dyextn_entity_group_rel` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_entity_group_rel` ENABLE KEYS */;

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


/*!40000 ALTER TABLE `dyextn_entity_map` DISABLE KEYS */;
LOCK TABLES `dyextn_entity_map` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_entity_map` ENABLE KEYS */;

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


/*!40000 ALTER TABLE `dyextn_entity_map_condns` DISABLE KEYS */;
LOCK TABLES `dyextn_entity_map_condns` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_entity_map_condns` ENABLE KEYS */;

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


/*!40000 ALTER TABLE `dyextn_entity_map_record` DISABLE KEYS */;
LOCK TABLES `dyextn_entity_map_record` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_entity_map_record` ENABLE KEYS */;

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


/*!40000 ALTER TABLE `dyextn_file_extensions` DISABLE KEYS */;
LOCK TABLES `dyextn_file_extensions` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_file_extensions` ENABLE KEYS */;

--
-- Table structure for table `dyextn_file_type_info`
--

DROP TABLE IF EXISTS `dyextn_file_type_info`;
CREATE TABLE `dyextn_file_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `MAX_FILE_SIZE` float default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKA00F0EDE5294FA3` (`IDENTIFIER`),
  CONSTRAINT `FKA00F0EDE5294FA3` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_attribute_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_file_type_info`
--


/*!40000 ALTER TABLE `dyextn_file_type_info` DISABLE KEYS */;
LOCK TABLES `dyextn_file_type_info` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_file_type_info` ENABLE KEYS */;

--
-- Table structure for table `dyextn_file_upload`
--

DROP TABLE IF EXISTS `dyextn_file_upload`;
CREATE TABLE `dyextn_file_upload` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `NO_OF_COLUMNS` int(11) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK2FAD41E740F198C2` (`IDENTIFIER`),
  CONSTRAINT `FK2FAD41E740F198C2` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_control` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_file_upload`
--


/*!40000 ALTER TABLE `dyextn_file_upload` DISABLE KEYS */;
LOCK TABLES `dyextn_file_upload` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_file_upload` ENABLE KEYS */;

--
-- Table structure for table `dyextn_float_concept_value`
--

DROP TABLE IF EXISTS `dyextn_float_concept_value`;
CREATE TABLE `dyextn_float_concept_value` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `VALUE` float default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK6785309A4641D513` (`IDENTIFIER`),
  CONSTRAINT `FK6785309A4641D513` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_permissible_value` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_float_concept_value`
--


/*!40000 ALTER TABLE `dyextn_float_concept_value` DISABLE KEYS */;
LOCK TABLES `dyextn_float_concept_value` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_float_concept_value` ENABLE KEYS */;

--
-- Table structure for table `dyextn_float_type_info`
--

DROP TABLE IF EXISTS `dyextn_float_type_info`;
CREATE TABLE `dyextn_float_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK7E1C0693BA4AE008` (`IDENTIFIER`),
  CONSTRAINT `FK7E1C0693BA4AE008` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_numeric_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_float_type_info`
--


/*!40000 ALTER TABLE `dyextn_float_type_info` DISABLE KEYS */;
LOCK TABLES `dyextn_float_type_info` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_float_type_info` ENABLE KEYS */;

--
-- Table structure for table `dyextn_form_context`
--

DROP TABLE IF EXISTS `dyextn_form_context`;
CREATE TABLE `dyextn_form_context` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `IS_INFINITE_ENTRY` tinyint(1) default NULL,
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


/*!40000 ALTER TABLE `dyextn_form_context` DISABLE KEYS */;
LOCK TABLES `dyextn_form_context` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_form_context` ENABLE KEYS */;

--
-- Table structure for table `dyextn_integer_concept_value`
--

DROP TABLE IF EXISTS `dyextn_integer_concept_value`;
CREATE TABLE `dyextn_integer_concept_value` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `VALUE` int(11) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKFBA33B3C4641D513` (`IDENTIFIER`),
  CONSTRAINT `FKFBA33B3C4641D513` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_permissible_value` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_integer_concept_value`
--


/*!40000 ALTER TABLE `dyextn_integer_concept_value` DISABLE KEYS */;
LOCK TABLES `dyextn_integer_concept_value` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_integer_concept_value` ENABLE KEYS */;

--
-- Table structure for table `dyextn_integer_type_info`
--

DROP TABLE IF EXISTS `dyextn_integer_type_info`;
CREATE TABLE `dyextn_integer_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK5F9CB235BA4AE008` (`IDENTIFIER`),
  CONSTRAINT `FK5F9CB235BA4AE008` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_numeric_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_integer_type_info`
--


/*!40000 ALTER TABLE `dyextn_integer_type_info` DISABLE KEYS */;
LOCK TABLES `dyextn_integer_type_info` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_integer_type_info` ENABLE KEYS */;

--
-- Table structure for table `dyextn_list_box`
--

DROP TABLE IF EXISTS `dyextn_list_box`;
CREATE TABLE `dyextn_list_box` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `MULTISELECT` tinyint(1) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK208395A7BF67AB26` (`IDENTIFIER`),
  CONSTRAINT `FK208395A7BF67AB26` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_select_control` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_list_box`
--


/*!40000 ALTER TABLE `dyextn_list_box` DISABLE KEYS */;
LOCK TABLES `dyextn_list_box` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_list_box` ENABLE KEYS */;

--
-- Table structure for table `dyextn_long_concept_value`
--

DROP TABLE IF EXISTS `dyextn_long_concept_value`;
CREATE TABLE `dyextn_long_concept_value` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `VALUE` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK3E1A6EF44641D513` (`IDENTIFIER`),
  CONSTRAINT `FK3E1A6EF44641D513` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_permissible_value` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_long_concept_value`
--


/*!40000 ALTER TABLE `dyextn_long_concept_value` DISABLE KEYS */;
LOCK TABLES `dyextn_long_concept_value` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_long_concept_value` ENABLE KEYS */;

--
-- Table structure for table `dyextn_long_type_info`
--

DROP TABLE IF EXISTS `dyextn_long_type_info`;
CREATE TABLE `dyextn_long_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK257281EDBA4AE008` (`IDENTIFIER`),
  CONSTRAINT `FK257281EDBA4AE008` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_numeric_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_long_type_info`
--


/*!40000 ALTER TABLE `dyextn_long_type_info` DISABLE KEYS */;
LOCK TABLES `dyextn_long_type_info` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_long_type_info` ENABLE KEYS */;

--
-- Table structure for table `dyextn_numeric_type_info`
--

DROP TABLE IF EXISTS `dyextn_numeric_type_info`;
CREATE TABLE `dyextn_numeric_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
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


/*!40000 ALTER TABLE `dyextn_numeric_type_info` DISABLE KEYS */;
LOCK TABLES `dyextn_numeric_type_info` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_numeric_type_info` ENABLE KEYS */;

--
-- Table structure for table `dyextn_object_type_info`
--

DROP TABLE IF EXISTS `dyextn_object_type_info`;
CREATE TABLE `dyextn_object_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK74819FB0E5294FA3` (`IDENTIFIER`),
  CONSTRAINT `FK74819FB0E5294FA3` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_attribute_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_object_type_info`
--


/*!40000 ALTER TABLE `dyextn_object_type_info` DISABLE KEYS */;
LOCK TABLES `dyextn_object_type_info` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_object_type_info` ENABLE KEYS */;

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_permissible_value`
--


/*!40000 ALTER TABLE `dyextn_permissible_value` DISABLE KEYS */;
LOCK TABLES `dyextn_permissible_value` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_permissible_value` ENABLE KEYS */;

--
-- Table structure for table `dyextn_primitive_attribute`
--

DROP TABLE IF EXISTS `dyextn_primitive_attribute`;
CREATE TABLE `dyextn_primitive_attribute` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `IS_COLLECTION` tinyint(1) default NULL,
  `IS_IDENTIFIED` tinyint(1) default NULL,
  `IS_PRIMARY_KEY` tinyint(1) default NULL,
  `IS_NULLABLE` tinyint(1) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKA9F765C76D19A21F` (`IDENTIFIER`),
  CONSTRAINT `FKA9F765C76D19A21F` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_attribute` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_primitive_attribute`
--


/*!40000 ALTER TABLE `dyextn_primitive_attribute` DISABLE KEYS */;
LOCK TABLES `dyextn_primitive_attribute` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_primitive_attribute` ENABLE KEYS */;

--
-- Table structure for table `dyextn_radiobutton`
--

DROP TABLE IF EXISTS `dyextn_radiobutton`;
CREATE TABLE `dyextn_radiobutton` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK16F5BA9040F198C2` (`IDENTIFIER`),
  CONSTRAINT `FK16F5BA9040F198C2` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_control` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_radiobutton`
--


/*!40000 ALTER TABLE `dyextn_radiobutton` DISABLE KEYS */;
LOCK TABLES `dyextn_radiobutton` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_radiobutton` ENABLE KEYS */;

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_role`
--


/*!40000 ALTER TABLE `dyextn_role` DISABLE KEYS */;
LOCK TABLES `dyextn_role` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_role` ENABLE KEYS */;

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


/*!40000 ALTER TABLE `dyextn_rule` DISABLE KEYS */;
LOCK TABLES `dyextn_rule` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_rule` ENABLE KEYS */;

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


/*!40000 ALTER TABLE `dyextn_rule_parameter` DISABLE KEYS */;
LOCK TABLES `dyextn_rule_parameter` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_rule_parameter` ENABLE KEYS */;

--
-- Table structure for table `dyextn_select_control`
--

DROP TABLE IF EXISTS `dyextn_select_control`;
CREATE TABLE `dyextn_select_control` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `SEPARATOR_STRING` varchar(255) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKDFEBB65740F198C2` (`IDENTIFIER`),
  CONSTRAINT `FKDFEBB65740F198C2` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_control` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_select_control`
--


/*!40000 ALTER TABLE `dyextn_select_control` DISABLE KEYS */;
LOCK TABLES `dyextn_select_control` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_select_control` ENABLE KEYS */;

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
  `CONCEPT_DEFINITION` varchar(255) default NULL,
  `ABSTRACT_METADATA_ID` bigint(20) default NULL,
  `ABSTRACT_VALUE_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKD2A0B5B15EB60E90` (`ABSTRACT_VALUE_ID`),
  KEY `FKD2A0B5B19AEB0CA3` (`ABSTRACT_METADATA_ID`),
  CONSTRAINT `FKD2A0B5B19AEB0CA3` FOREIGN KEY (`ABSTRACT_METADATA_ID`) REFERENCES `dyextn_abstract_metadata` (`IDENTIFIER`),
  CONSTRAINT `FKD2A0B5B15EB60E90` FOREIGN KEY (`ABSTRACT_VALUE_ID`) REFERENCES `dyextn_permissible_value` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_semantic_property`
--


/*!40000 ALTER TABLE `dyextn_semantic_property` DISABLE KEYS */;
LOCK TABLES `dyextn_semantic_property` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_semantic_property` ENABLE KEYS */;

--
-- Table structure for table `dyextn_short_concept_value`
--

DROP TABLE IF EXISTS `dyextn_short_concept_value`;
CREATE TABLE `dyextn_short_concept_value` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `VALUE` smallint(6) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKC1945ABA4641D513` (`IDENTIFIER`),
  CONSTRAINT `FKC1945ABA4641D513` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_permissible_value` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_short_concept_value`
--


/*!40000 ALTER TABLE `dyextn_short_concept_value` DISABLE KEYS */;
LOCK TABLES `dyextn_short_concept_value` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_short_concept_value` ENABLE KEYS */;

--
-- Table structure for table `dyextn_short_type_info`
--

DROP TABLE IF EXISTS `dyextn_short_type_info`;
CREATE TABLE `dyextn_short_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK99540B3BA4AE008` (`IDENTIFIER`),
  CONSTRAINT `FK99540B3BA4AE008` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_numeric_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_short_type_info`
--


/*!40000 ALTER TABLE `dyextn_short_type_info` DISABLE KEYS */;
LOCK TABLES `dyextn_short_type_info` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_short_type_info` ENABLE KEYS */;

--
-- Table structure for table `dyextn_string_concept_value`
--

DROP TABLE IF EXISTS `dyextn_string_concept_value`;
CREATE TABLE `dyextn_string_concept_value` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `VALUE` varchar(255) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKADE7D8894641D513` (`IDENTIFIER`),
  CONSTRAINT `FKADE7D8894641D513` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_permissible_value` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_string_concept_value`
--


/*!40000 ALTER TABLE `dyextn_string_concept_value` DISABLE KEYS */;
LOCK TABLES `dyextn_string_concept_value` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_string_concept_value` ENABLE KEYS */;

--
-- Table structure for table `dyextn_string_type_info`
--

DROP TABLE IF EXISTS `dyextn_string_type_info`;
CREATE TABLE `dyextn_string_type_info` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `MAX_SIZE` int(11) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKDA35FE02E5294FA3` (`IDENTIFIER`),
  CONSTRAINT `FKDA35FE02E5294FA3` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_attribute_type_info` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_string_type_info`
--


/*!40000 ALTER TABLE `dyextn_string_type_info` DISABLE KEYS */;
LOCK TABLES `dyextn_string_type_info` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_string_type_info` ENABLE KEYS */;

--
-- Table structure for table `dyextn_table_properties`
--

DROP TABLE IF EXISTS `dyextn_table_properties`;
CREATE TABLE `dyextn_table_properties` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
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


/*!40000 ALTER TABLE `dyextn_table_properties` DISABLE KEYS */;
LOCK TABLES `dyextn_table_properties` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_table_properties` ENABLE KEYS */;

--
-- Table structure for table `dyextn_tagged_value`
--

DROP TABLE IF EXISTS `dyextn_tagged_value`;
CREATE TABLE `dyextn_tagged_value` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `T_KEY` varchar(255) default NULL,
  `T_VALUE` varchar(255) default NULL,
  `ABSTRACT_METADATA_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKF79D055B9AEB0CA3` (`ABSTRACT_METADATA_ID`),
  CONSTRAINT `FKF79D055B9AEB0CA3` FOREIGN KEY (`ABSTRACT_METADATA_ID`) REFERENCES `dyextn_abstract_metadata` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_tagged_value`
--


/*!40000 ALTER TABLE `dyextn_tagged_value` DISABLE KEYS */;
LOCK TABLES `dyextn_tagged_value` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_tagged_value` ENABLE KEYS */;

--
-- Table structure for table `dyextn_textarea`
--

DROP TABLE IF EXISTS `dyextn_textarea`;
CREATE TABLE `dyextn_textarea` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `TEXTAREA_COLUMNS` int(11) default NULL,
  `TEXTAREA_ROWS` int(11) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK946EE25740F198C2` (`IDENTIFIER`),
  CONSTRAINT `FK946EE25740F198C2` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_control` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_textarea`
--


/*!40000 ALTER TABLE `dyextn_textarea` DISABLE KEYS */;
LOCK TABLES `dyextn_textarea` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_textarea` ENABLE KEYS */;

--
-- Table structure for table `dyextn_textfield`
--

DROP TABLE IF EXISTS `dyextn_textfield`;
CREATE TABLE `dyextn_textfield` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `NO_OF_COLUMNS` int(11) default NULL,
  `IS_PASSWORD` tinyint(1) default NULL,
  `IS_URL` tinyint(1) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKF9AFC85040F198C2` (`IDENTIFIER`),
  CONSTRAINT `FKF9AFC85040F198C2` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_control` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_textfield`
--


/*!40000 ALTER TABLE `dyextn_textfield` DISABLE KEYS */;
LOCK TABLES `dyextn_textfield` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_textfield` ENABLE KEYS */;

--
-- Table structure for table `dyextn_userdefined_de`
--

DROP TABLE IF EXISTS `dyextn_userdefined_de`;
CREATE TABLE `dyextn_userdefined_de` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK630761FF53CC4A77` (`IDENTIFIER`),
  CONSTRAINT `FK630761FF53CC4A77` FOREIGN KEY (`IDENTIFIER`) REFERENCES `dyextn_data_element` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dyextn_userdefined_de`
--


/*!40000 ALTER TABLE `dyextn_userdefined_de` DISABLE KEYS */;
LOCK TABLES `dyextn_userdefined_de` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_userdefined_de` ENABLE KEYS */;

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


/*!40000 ALTER TABLE `dyextn_view` DISABLE KEYS */;
LOCK TABLES `dyextn_view` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dyextn_view` ENABLE KEYS */;

--
-- Table structure for table `inter_model_association`
--

DROP TABLE IF EXISTS `inter_model_association`;
CREATE TABLE `inter_model_association` (
  `ASSOCIATION_ID` bigint(20) NOT NULL default '0',
  `LEFT_ENTITY_ID` bigint(20) NOT NULL default '0',
  `LEFT_ATTRIBUTE_ID` bigint(20) NOT NULL default '0',
  `RIGHT_ENTITY_ID` bigint(20) NOT NULL default '0',
  `RIGHT_ATTRIBUTE_ID` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`ASSOCIATION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `inter_model_association`
--


/*!40000 ALTER TABLE `inter_model_association` DISABLE KEYS */;
LOCK TABLES `inter_model_association` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `inter_model_association` ENABLE KEYS */;

--
-- Table structure for table `intra_model_association`
--

DROP TABLE IF EXISTS `intra_model_association`;
CREATE TABLE `intra_model_association` (
  `ASSOCIATION_ID` bigint(20) NOT NULL default '0',
  `DE_ASSOCIATION_ID` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`ASSOCIATION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `intra_model_association`
--


/*!40000 ALTER TABLE `intra_model_association` DISABLE KEYS */;
LOCK TABLES `intra_model_association` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `intra_model_association` ENABLE KEYS */;

--
-- Table structure for table `output_class_urls`
--

DROP TABLE IF EXISTS `output_class_urls`;
CREATE TABLE `output_class_urls` (
  `CAB2B_QUERY_ID` bigint(20) NOT NULL default '0',
  `OUTPUT_CLASS_URL` varchar(255) default NULL,
  `POSITION` int(11) NOT NULL default '0',
  PRIMARY KEY  (`CAB2B_QUERY_ID`,`POSITION`),
  KEY `FKE131CD69A638FEFD` (`CAB2B_QUERY_ID`),
  CONSTRAINT `FKE131CD69A638FEFD` FOREIGN KEY (`CAB2B_QUERY_ID`) REFERENCES `cab2b_query` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `output_class_urls`
--


/*!40000 ALTER TABLE `output_class_urls` DISABLE KEYS */;
LOCK TABLES `output_class_urls` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `output_class_urls` ENABLE KEYS */;

--
-- Table structure for table `path`
--

DROP TABLE IF EXISTS `path`;
CREATE TABLE `path` (
  `PATH_ID` bigint(20) NOT NULL auto_increment,
  `FIRST_ENTITY_ID` bigint(20) default NULL,
  `INTERMEDIATE_PATH` text,
  `LAST_ENTITY_ID` bigint(20) default NULL,
  PRIMARY KEY  (`PATH_ID`),
  KEY `INDEX1` (`FIRST_ENTITY_ID`,`LAST_ENTITY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `path`
--


/*!40000 ALTER TABLE `path` DISABLE KEYS */;
LOCK TABLES `path` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `path` ENABLE KEYS */;

--
-- Table structure for table `query`
--

DROP TABLE IF EXISTS `query`;
CREATE TABLE `query` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `QUERY_CONSTRAINTS_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  UNIQUE KEY `QUERY_CONSTRAINTS_ID` (`QUERY_CONSTRAINTS_ID`),
  KEY `FK49D20A84B0F861E` (`QUERY_CONSTRAINTS_ID`),
  CONSTRAINT `FK49D20A84B0F861E` FOREIGN KEY (`QUERY_CONSTRAINTS_ID`) REFERENCES `query_constraints` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query`
--


/*!40000 ALTER TABLE `query` DISABLE KEYS */;
LOCK TABLES `query` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `query` ENABLE KEYS */;

--
-- Table structure for table `query_arithmetic_operand`
--

DROP TABLE IF EXISTS `query_arithmetic_operand`;
CREATE TABLE `query_arithmetic_operand` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `LITERAL` varchar(255) default NULL,
  `TERM_TYPE` varchar(255) default NULL,
  `DATE_LITERAL` date default NULL,
  `TIME_INTERVAL` varchar(255) default NULL,
  `DE_ATTRIBUTE_ID` bigint(20) default NULL,
  `EXPRESSION_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK262AEB0BD635BD31` (`IDENTIFIER`),
  KEY `FK262AEB0B96C7CE5A` (`IDENTIFIER`),
  KEY `FK262AEB0BD006BE44` (`IDENTIFIER`),
  KEY `FK262AEB0B7223B197` (`IDENTIFIER`),
  KEY `FK262AEB0B687BE69E` (`IDENTIFIER`),
  KEY `FK262AEB0BE92C814D` (`EXPRESSION_ID`),
  CONSTRAINT `FK262AEB0BE92C814D` FOREIGN KEY (`EXPRESSION_ID`) REFERENCES `query_base_expression` (`IDENTIFIER`),
  CONSTRAINT `FK262AEB0B687BE69E` FOREIGN KEY (`IDENTIFIER`) REFERENCES `query_operand` (`IDENTIFIER`),
  CONSTRAINT `FK262AEB0B7223B197` FOREIGN KEY (`IDENTIFIER`) REFERENCES `query_operand` (`IDENTIFIER`),
  CONSTRAINT `FK262AEB0B96C7CE5A` FOREIGN KEY (`IDENTIFIER`) REFERENCES `query_operand` (`IDENTIFIER`),
  CONSTRAINT `FK262AEB0BD006BE44` FOREIGN KEY (`IDENTIFIER`) REFERENCES `query_operand` (`IDENTIFIER`),
  CONSTRAINT `FK262AEB0BD635BD31` FOREIGN KEY (`IDENTIFIER`) REFERENCES `query_operand` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_arithmetic_operand`
--


/*!40000 ALTER TABLE `query_arithmetic_operand` DISABLE KEYS */;
LOCK TABLES `query_arithmetic_operand` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `query_arithmetic_operand` ENABLE KEYS */;

--
-- Table structure for table `query_base_expr_opnd`
--

DROP TABLE IF EXISTS `query_base_expr_opnd`;
CREATE TABLE `query_base_expr_opnd` (
  `BASE_EXPRESSION_ID` bigint(20) NOT NULL default '0',
  `OPERAND_ID` bigint(20) NOT NULL default '0',
  `POSITION` int(11) NOT NULL default '0',
  PRIMARY KEY  (`BASE_EXPRESSION_ID`,`POSITION`),
  KEY `FKAE67EAF0712A4C` (`OPERAND_ID`),
  KEY `FKAE67EA48BA6890` (`BASE_EXPRESSION_ID`),
  CONSTRAINT `FKAE67EA48BA6890` FOREIGN KEY (`BASE_EXPRESSION_ID`) REFERENCES `query_base_expression` (`IDENTIFIER`),
  CONSTRAINT `FKAE67EAF0712A4C` FOREIGN KEY (`OPERAND_ID`) REFERENCES `query_operand` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_base_expr_opnd`
--


/*!40000 ALTER TABLE `query_base_expr_opnd` DISABLE KEYS */;
LOCK TABLES `query_base_expr_opnd` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `query_base_expr_opnd` ENABLE KEYS */;

--
-- Table structure for table `query_base_expression`
--

DROP TABLE IF EXISTS `query_base_expression`;
CREATE TABLE `query_base_expression` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `EXPR_TYPE` varchar(255) NOT NULL default '',
  `QUERY_CONSTRAINT_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKE8FA3F2FCF83E189` (`QUERY_CONSTRAINT_ID`),
  CONSTRAINT `FKE8FA3F2FCF83E189` FOREIGN KEY (`QUERY_CONSTRAINT_ID`) REFERENCES `query_constraints` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_base_expression`
--


/*!40000 ALTER TABLE `query_base_expression` DISABLE KEYS */;
LOCK TABLES `query_base_expression` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `query_base_expression` ENABLE KEYS */;

--
-- Table structure for table `query_baseexpr_to_connectors`
--

DROP TABLE IF EXISTS `query_baseexpr_to_connectors`;
CREATE TABLE `query_baseexpr_to_connectors` (
  `BASE_EXPRESSION_ID` bigint(20) NOT NULL default '0',
  `CONNECTOR_ID` bigint(20) NOT NULL default '0',
  `POSITION` int(11) NOT NULL default '0',
  PRIMARY KEY  (`BASE_EXPRESSION_ID`,`POSITION`),
  KEY `FK3F0043482FCE1DA7` (`CONNECTOR_ID`),
  KEY `FK3F00434848BA6890` (`BASE_EXPRESSION_ID`),
  CONSTRAINT `FK3F00434848BA6890` FOREIGN KEY (`BASE_EXPRESSION_ID`) REFERENCES `query_base_expression` (`IDENTIFIER`),
  CONSTRAINT `FK3F0043482FCE1DA7` FOREIGN KEY (`CONNECTOR_ID`) REFERENCES `query_connector` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_baseexpr_to_connectors`
--


/*!40000 ALTER TABLE `query_baseexpr_to_connectors` DISABLE KEYS */;
LOCK TABLES `query_baseexpr_to_connectors` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `query_baseexpr_to_connectors` ENABLE KEYS */;

--
-- Table structure for table `query_condition`
--

DROP TABLE IF EXISTS `query_condition`;
CREATE TABLE `query_condition` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `ATTRIBUTE_ID` bigint(20) NOT NULL default '0',
  `RELATIONAL_OPERATOR` varchar(255) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_condition`
--


/*!40000 ALTER TABLE `query_condition` DISABLE KEYS */;
LOCK TABLES `query_condition` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `query_condition` ENABLE KEYS */;

--
-- Table structure for table `query_condition_values`
--

DROP TABLE IF EXISTS `query_condition_values`;
CREATE TABLE `query_condition_values` (
  `QUERY_CONDITION_ID` bigint(20) NOT NULL default '0',
  `VALUE` varchar(255) default NULL,
  `POSITION` int(11) NOT NULL default '0',
  PRIMARY KEY  (`QUERY_CONDITION_ID`,`POSITION`),
  KEY `FK9997379D4D1598FE` (`QUERY_CONDITION_ID`),
  CONSTRAINT `FK9997379D4D1598FE` FOREIGN KEY (`QUERY_CONDITION_ID`) REFERENCES `query_condition` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_condition_values`
--


/*!40000 ALTER TABLE `query_condition_values` DISABLE KEYS */;
LOCK TABLES `query_condition_values` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `query_condition_values` ENABLE KEYS */;

--
-- Table structure for table `query_connector`
--

DROP TABLE IF EXISTS `query_connector`;
CREATE TABLE `query_connector` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `OPERATOR` varchar(255) default NULL,
  `NESTING_NUMBER` int(11) default NULL,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_connector`
--


/*!40000 ALTER TABLE `query_connector` DISABLE KEYS */;
LOCK TABLES `query_connector` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `query_connector` ENABLE KEYS */;

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_constraints`
--


/*!40000 ALTER TABLE `query_constraints` DISABLE KEYS */;
LOCK TABLES `query_constraints` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `query_constraints` ENABLE KEYS */;

--
-- Table structure for table `query_custom_formula`
--

DROP TABLE IF EXISTS `query_custom_formula`;
CREATE TABLE `query_custom_formula` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
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


/*!40000 ALTER TABLE `query_custom_formula` DISABLE KEYS */;
LOCK TABLES `query_custom_formula` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `query_custom_formula` ENABLE KEYS */;

--
-- Table structure for table `query_expression`
--

DROP TABLE IF EXISTS `query_expression`;
CREATE TABLE `query_expression` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `IS_IN_VIEW` tinyint(1) default NULL,
  `IS_VISIBLE` tinyint(1) default NULL,
  `UI_EXPR_ID` int(11) default NULL,
  `QUERY_QUERY_ENTITY_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK1B473A8F40EB75D4` (`IDENTIFIER`),
  KEY `FK1B473A8F526D4561` (`QUERY_QUERY_ENTITY_ID`),
  CONSTRAINT `FK1B473A8F526D4561` FOREIGN KEY (`QUERY_QUERY_ENTITY_ID`) REFERENCES `query_query_entity` (`IDENTIFIER`),
  CONSTRAINT `FK1B473A8F40EB75D4` FOREIGN KEY (`IDENTIFIER`) REFERENCES `query_base_expression` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_expression`
--


/*!40000 ALTER TABLE `query_expression` DISABLE KEYS */;
LOCK TABLES `query_expression` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `query_expression` ENABLE KEYS */;

--
-- Table structure for table `query_formula_rhs`
--

DROP TABLE IF EXISTS `query_formula_rhs`;
CREATE TABLE `query_formula_rhs` (
  `CUSTOM_FORMULA_ID` bigint(20) NOT NULL default '0',
  `RHS_TERM_ID` bigint(20) NOT NULL default '0',
  `POSITION` int(11) NOT NULL default '0',
  PRIMARY KEY  (`CUSTOM_FORMULA_ID`,`POSITION`),
  KEY `FKAE90F94D3BC37DCB` (`RHS_TERM_ID`),
  KEY `FKAE90F94D9A0B7164` (`CUSTOM_FORMULA_ID`),
  CONSTRAINT `FKAE90F94D9A0B7164` FOREIGN KEY (`CUSTOM_FORMULA_ID`) REFERENCES `query_operand` (`IDENTIFIER`),
  CONSTRAINT `FKAE90F94D3BC37DCB` FOREIGN KEY (`RHS_TERM_ID`) REFERENCES `query_base_expression` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_formula_rhs`
--


/*!40000 ALTER TABLE `query_formula_rhs` DISABLE KEYS */;
LOCK TABLES `query_formula_rhs` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `query_formula_rhs` ENABLE KEYS */;

--
-- Table structure for table `query_inter_model_association`
--

DROP TABLE IF EXISTS `query_inter_model_association`;
CREATE TABLE `query_inter_model_association` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `SOURCE_SERVICE_URL` text NOT NULL,
  `TARGET_SERVICE_URL` text NOT NULL,
  `SOURCE_ATTRIBUTE_ID` bigint(20) NOT NULL default '0',
  `TARGET_ATTRIBUTE_ID` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKD70658D15F5AB67E` (`IDENTIFIER`),
  CONSTRAINT `FKD70658D15F5AB67E` FOREIGN KEY (`IDENTIFIER`) REFERENCES `query_model_association` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_inter_model_association`
--


/*!40000 ALTER TABLE `query_inter_model_association` DISABLE KEYS */;
LOCK TABLES `query_inter_model_association` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `query_inter_model_association` ENABLE KEYS */;

--
-- Table structure for table `query_intra_model_association`
--

DROP TABLE IF EXISTS `query_intra_model_association`;
CREATE TABLE `query_intra_model_association` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `DE_ASSOCIATION_ID` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FKF1EDBDD35F5AB67E` (`IDENTIFIER`),
  CONSTRAINT `FKF1EDBDD35F5AB67E` FOREIGN KEY (`IDENTIFIER`) REFERENCES `query_model_association` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_intra_model_association`
--


/*!40000 ALTER TABLE `query_intra_model_association` DISABLE KEYS */;
LOCK TABLES `query_intra_model_association` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `query_intra_model_association` ENABLE KEYS */;

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_join_graph`
--


/*!40000 ALTER TABLE `query_join_graph` DISABLE KEYS */;
LOCK TABLES `query_join_graph` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `query_join_graph` ENABLE KEYS */;

--
-- Table structure for table `query_model_association`
--

DROP TABLE IF EXISTS `query_model_association`;
CREATE TABLE `query_model_association` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_model_association`
--


/*!40000 ALTER TABLE `query_model_association` DISABLE KEYS */;
LOCK TABLES `query_model_association` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `query_model_association` ENABLE KEYS */;

--
-- Table structure for table `query_operand`
--

DROP TABLE IF EXISTS `query_operand`;
CREATE TABLE `query_operand` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `OPND_TYPE` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_operand`
--


/*!40000 ALTER TABLE `query_operand` DISABLE KEYS */;
LOCK TABLES `query_operand` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `query_operand` ENABLE KEYS */;

--
-- Table structure for table `query_output_attribute`
--

DROP TABLE IF EXISTS `query_output_attribute`;
CREATE TABLE `query_output_attribute` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `EXPRESSION_ID` bigint(20) default NULL,
  `ATTRIBUTE_ID` bigint(20) NOT NULL default '0',
  `PARAMETERIZED_QUERY_ID` bigint(20) default NULL,
  `POSITION` int(11) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK22C9DB75604D4BDA` (`PARAMETERIZED_QUERY_ID`),
  KEY `FK22C9DB75E92C814D` (`EXPRESSION_ID`),
  CONSTRAINT `FK22C9DB75E92C814D` FOREIGN KEY (`EXPRESSION_ID`) REFERENCES `query_base_expression` (`IDENTIFIER`),
  CONSTRAINT `FK22C9DB75604D4BDA` FOREIGN KEY (`PARAMETERIZED_QUERY_ID`) REFERENCES `query_parameterized_query` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_output_attribute`
--


/*!40000 ALTER TABLE `query_output_attribute` DISABLE KEYS */;
LOCK TABLES `query_output_attribute` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `query_output_attribute` ENABLE KEYS */;

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


/*!40000 ALTER TABLE `query_output_term` DISABLE KEYS */;
LOCK TABLES `query_output_term` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `query_output_term` ENABLE KEYS */;

--
-- Table structure for table `query_parameterized_condition`
--

DROP TABLE IF EXISTS `query_parameterized_condition`;
CREATE TABLE `query_parameterized_condition` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `CONDITION_INDEX` int(11) default NULL,
  `CONDITION_NAME` varchar(255) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK9BE75A3E4B9044D1` (`IDENTIFIER`),
  CONSTRAINT `FK9BE75A3E4B9044D1` FOREIGN KEY (`IDENTIFIER`) REFERENCES `query_condition` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_parameterized_condition`
--


/*!40000 ALTER TABLE `query_parameterized_condition` DISABLE KEYS */;
LOCK TABLES `query_parameterized_condition` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `query_parameterized_condition` ENABLE KEYS */;

--
-- Table structure for table `query_parameterized_query`
--

DROP TABLE IF EXISTS `query_parameterized_query`;
CREATE TABLE `query_parameterized_query` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `QUERY_NAME` varchar(255) default NULL,
  `DESCRIPTION` text,
  PRIMARY KEY  (`IDENTIFIER`),
  UNIQUE KEY `QUERY_NAME` (`QUERY_NAME`),
  KEY `FKA272176B76177EFE` (`IDENTIFIER`),
  CONSTRAINT `FKA272176B76177EFE` FOREIGN KEY (`IDENTIFIER`) REFERENCES `query` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_parameterized_query`
--


/*!40000 ALTER TABLE `query_parameterized_query` DISABLE KEYS */;
LOCK TABLES `query_parameterized_query` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `query_parameterized_query` ENABLE KEYS */;

--
-- Table structure for table `query_query_entity`
--

DROP TABLE IF EXISTS `query_query_entity`;
CREATE TABLE `query_query_entity` (
  `IDENTIFIER` bigint(20) NOT NULL auto_increment,
  `ENTITY_ID` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_query_entity`
--


/*!40000 ALTER TABLE `query_query_entity` DISABLE KEYS */;
LOCK TABLES `query_query_entity` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `query_query_entity` ENABLE KEYS */;

--
-- Table structure for table `query_rule_cond`
--

DROP TABLE IF EXISTS `query_rule_cond`;
CREATE TABLE `query_rule_cond` (
  `RULE_ID` bigint(20) NOT NULL default '0',
  `CONDITION_ID` bigint(20) NOT NULL default '0',
  `POSITION` int(11) NOT NULL default '0',
  PRIMARY KEY  (`RULE_ID`,`POSITION`),
  KEY `FKC32D37AE6458C2E7` (`CONDITION_ID`),
  KEY `FKC32D37AE39F0A10D` (`RULE_ID`),
  CONSTRAINT `FKC32D37AE39F0A10D` FOREIGN KEY (`RULE_ID`) REFERENCES `query_operand` (`IDENTIFIER`),
  CONSTRAINT `FKC32D37AE6458C2E7` FOREIGN KEY (`CONDITION_ID`) REFERENCES `query_condition` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_rule_cond`
--


/*!40000 ALTER TABLE `query_rule_cond` DISABLE KEYS */;
LOCK TABLES `query_rule_cond` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `query_rule_cond` ENABLE KEYS */;

--
-- Table structure for table `query_subexpr_operand`
--

DROP TABLE IF EXISTS `query_subexpr_operand`;
CREATE TABLE `query_subexpr_operand` (
  `IDENTIFIER` bigint(20) NOT NULL default '0',
  `EXPRESSION_ID` bigint(20) default NULL,
  PRIMARY KEY  (`IDENTIFIER`),
  KEY `FK2BF760E832E875C8` (`IDENTIFIER`),
  KEY `FK2BF760E8E92C814D` (`EXPRESSION_ID`),
  CONSTRAINT `FK2BF760E8E92C814D` FOREIGN KEY (`EXPRESSION_ID`) REFERENCES `query_base_expression` (`IDENTIFIER`),
  CONSTRAINT `FK2BF760E832E875C8` FOREIGN KEY (`IDENTIFIER`) REFERENCES `query_operand` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_subexpr_operand`
--


/*!40000 ALTER TABLE `query_subexpr_operand` DISABLE KEYS */;
LOCK TABLES `query_subexpr_operand` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `query_subexpr_operand` ENABLE KEYS */;

--
-- Table structure for table `query_to_output_terms`
--

DROP TABLE IF EXISTS `query_to_output_terms`;
CREATE TABLE `query_to_output_terms` (
  `QUERY_ID` bigint(20) NOT NULL default '0',
  `OUTPUT_TERM_ID` bigint(20) NOT NULL default '0',
  `POSITION` int(11) NOT NULL default '0',
  PRIMARY KEY  (`QUERY_ID`,`POSITION`),
  UNIQUE KEY `OUTPUT_TERM_ID` (`OUTPUT_TERM_ID`),
  KEY `FK8A70E2565E5B9430` (`OUTPUT_TERM_ID`),
  KEY `FK8A70E25691051647` (`QUERY_ID`),
  CONSTRAINT `FK8A70E25691051647` FOREIGN KEY (`QUERY_ID`) REFERENCES `query` (`IDENTIFIER`),
  CONSTRAINT `FK8A70E2565E5B9430` FOREIGN KEY (`OUTPUT_TERM_ID`) REFERENCES `query_output_term` (`IDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query_to_output_terms`
--


/*!40000 ALTER TABLE `query_to_output_terms` DISABLE KEYS */;
LOCK TABLES `query_to_output_terms` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `query_to_output_terms` ENABLE KEYS */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

