-- MySQL dump 10.11
--
-- Host: localhost    Database: openmind
-- ------------------------------------------------------
-- Server version	5.0.90-log

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
-- Table structure for table `join_group_role`
--

DROP TABLE IF EXISTS `join_group_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `join_group_role` (
  `id` bigint(20) NOT NULL auto_increment,
  `ROLE_NAME` varchar(255) default NULL,
  `GROUP_NAME` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `join_user_group`
--

DROP TABLE IF EXISTS `join_user_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `join_user_group` (
  `id` bigint(20) NOT NULL auto_increment,
  `EMAIL_USER` varchar(255) default NULL,
  `GROUP_NAME` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `join_user_role`
--

DROP TABLE IF EXISTS `join_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `join_user_role` (
  `id` bigint(20) NOT NULL auto_increment,
  `EMAIL_USER` varchar(255) default NULL,
  `ROLE_NAME` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `node`
--

DROP TABLE IF EXISTS `node`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `node` (
  `node_type` varchar(31) NOT NULL,
  `row_id` bigint(20) NOT NULL auto_increment,
  `binary_value` tinyblob,
  `content_type` varchar(255) default NULL,
  `id` bigint(20) default NULL,
  `long_value` bigint(20) default NULL,
  `modification_time` bigint(20) default NULL,
  `normalized_own_value` mediumtext character set utf8,
  `normalized_arabic_own_value` mediumtext character set utf8,
  `object_class` varchar(255) character set utf8 default NULL,
  `own_value` mediumtext character set utf8,
  `status` varchar(255) default NULL,
  `system_status` varchar(255) default NULL,
  `type` varchar(255) default NULL,
  `version` bigint(20) default NULL,
  `source_id` bigint(20) default NULL,
  `source_modif` bigint(20) default NULL,
  `source_obj_class` varchar(255) character set utf8 default NULL,
  `target_id` bigint(20) default NULL,
  `target_modif` bigint(20) default NULL,
  `target_obj_class` varchar(255) character set utf8 default NULL,
  `user` varchar(255) default NULL,
  `possible_value` mediumtext,
  `public` bit(1) default b'0',
  PRIMARY KEY  (`row_id`),
  KEY `om_id` (`id`),
  KEY `source_id` (`source_id`),
  KEY `target_id` (`target_id`),
  KEY `system_status` (`system_status`),
  KEY `source_mod` (`source_modif`),
  KEY `target_modif` (`target_modif`),
  KEY `obj_class` (`object_class`)
) ENGINE=MyISAM AUTO_INCREMENT=1414697 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `node_join`
--

DROP TABLE IF EXISTS `node_join`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `node_join` (
  `id` bigint(20) NOT NULL auto_increment,
  `source_id` bigint(20) default NULL,
  `source_modification_time` bigint(20) default NULL,
  `source_sys_status` varchar(255) default NULL,
  `target_id` bigint(20) default NULL,
  `target_modification_time` bigint(20) default NULL,
  `target_sys_status` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role_permission`
--

DROP TABLE IF EXISTS `role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_permission` (
  `id` bigint(20) NOT NULL auto_increment,
  `permission_name` varchar(255) default NULL,
  `role_name` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `row_sequence`
--

DROP TABLE IF EXISTS `row_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `row_sequence` (
  `id` bigint(20) NOT NULL auto_increment,
  `last_value` bigint(20) default NULL,
  `name` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_group`
--

DROP TABLE IF EXISTS `sys_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_group` (
  `group_name` varchar(255) NOT NULL,
  `description` varchar(255) default NULL,
  PRIMARY KEY  (`group_name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_permission`
--

DROP TABLE IF EXISTS `sys_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_permission` (
  `perm_name` varchar(255) NOT NULL,
  `description` varchar(255) default NULL,
  PRIMARY KEY  (`perm_name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_role` (
  `role_name` varchar(255) NOT NULL,
  `description` varchar(255) default NULL,
  PRIMARY KEY  (`role_name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL auto_increment,
  `email` varchar(255) character set utf8 default NULL,
  `first_name` varchar(255) character set utf8 default NULL,
  `last_name` varchar(255) character set utf8 default NULL,
  `password` varchar(255) character set utf8 default NULL,
  `user_name` varchar(255) character set utf8 default NULL,
  `role` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=26 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-04-14 13:07:36
