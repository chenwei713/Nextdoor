/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE SCHEMA /*!32312 IF NOT EXISTS*/`nextdoor` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `nextdoor`;

DROP TABLE IF EXISTS `blocks`;
CREATE TABLE `blocks` (
  `blocks_id` int(11) NOT NULL AUTO_INCREMENT,
  `hoods_id` int(11) NOT NULL,
  `blocks_name` varchar(45) COLLATE utf8mb4_general_ci NOT NULL,
  `blocks_description` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `longitude1` double NOT NULL,
  `latitude1` double NOT NULL,
  `longitude2` double NOT NULL,
  `latitude2` double NOT NULL,
  PRIMARY KEY (`blocks_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `blocks_application`;
CREATE TABLE `blocks_application` (
  `application_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `blocks_id` int(11) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_approved` tinyint(4) NOT NULL DEFAULT '0',
  `is_valid` tinyint(4) NOT NULL DEFAULT '1',
  `approval1` int(11) DEFAULT NULL,
  `approval2` int(11) DEFAULT NULL,
  `approval3` int(11) DEFAULT NULL,
  PRIMARY KEY (`application_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `friends`;
CREATE TABLE `friends` (
  `user1_id` int(11) NOT NULL,
  `user2_id` int(11) NOT NULL,
  PRIMARY KEY (`user1_id`,`user2_id`),
  KEY `user_id_idx` (`user2_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `friends_application`;
CREATE TABLE `friends_application` (
  `application_id` int(11) NOT NULL AUTO_INCREMENT,
  `user1_id` int(11) NOT NULL,
  `user2_id` int(11) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `notes` varchar(256) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `is_approved` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`application_id`),
  KEY `user1_id` (`user1_id`),
  KEY `user2_id` (`user2_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `Hoods`;
CREATE TABLE `Hoods` (
  `hoods_id` int(11) NOT NULL AUTO_INCREMENT,
  `hoods_name` varchar(45) COLLATE utf8mb4_general_ci NOT NULL,
  `hoods_description` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `longitude1` double NOT NULL,
  `latitude1` double NOT NULL,
  `longitude2` double NOT NULL,
  `latitude2` double NOT NULL,
  PRIMARY KEY (`hoods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `in_blocks`;
CREATE TABLE `in_blocks` (
  `user_id` int(11) NOT NULL,
  `blocks_id` int(11) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `message_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `thread_id` varchar(128) NOT NULL,
  `parent_message_id` int(11) DEFAULT NULL,
  `content` varchar(256) COLLATE utf8mb4_general_ci NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `neighbors`;
CREATE TABLE `neighbors` (
  `user1_id` int(11) NOT NULL,
  `user2_id` int(11) NOT NULL,
  PRIMARY KEY (`user1_id`,`user2_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `read`;
DROP TABLE IF EXISTS `read_threads`;
CREATE TABLE `read_threads` (
  `user_id` int(11) NOT NULL,
  `thread_id` varchar(128) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`, `thread_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `read_messages`;
-- CREATE TABLE `read_messages` (
--   `user_id` int(11) NOT NULL,
--   `message_id` int(11) NOT NULL,
--   PRIMARY KEY (`user_id`)
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `setting`;
CREATE TABLE `setting` (
  `user_id` int(11) NOT NULL,
  `email_friends_feed` tinyint(4) NOT NULL DEFAULT '1',
  `email_neighbors_feed` tinyint(4) NOT NULL DEFAULT '1',
  `email_blocks_feed` tinyint(4) NOT NULL DEFAULT '1',
  `email_hoods_feed` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `sharing_list`;
CREATE TABLE `sharing_list` (
  `thread_id` varchar(128) NOT NULL,
  `sharing_user_id` int(11) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`thread_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `thread`;
CREATE TABLE `thread` (
  `thread_id` varchar(128) NOT NULL,
  `user_id` int(11) NOT NULL,
  `subject` varchar(128) COLLATE utf8mb4_general_ci NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `content` varchar(256) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`thread_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_first_name` varchar(45) COLLATE utf8mb4_general_ci NOT NULL,
  `user_last_name` varchar(45) COLLATE utf8mb4_general_ci NOT NULL,
  `user_street` varchar(45) COLLATE utf8mb4_general_ci NOT NULL,
  `user_city` varchar(45) COLLATE utf8mb4_general_ci NOT NULL,
  `user_state` varchar(45) COLLATE utf8mb4_general_ci NOT NULL,
  `longitude` double NOT NULL,
  `latitude` double NOT NULL,
  `user_profile` varchar(512) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `user_photo_url` varchar(45) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `account_name` varchar(45) COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(45) COLLATE utf8mb4_general_ci NOT NULL,
  `telephone_number` varchar(45) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
