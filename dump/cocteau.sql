-- MySQL dump 10.13  Distrib 8.0.16, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: ideaslab2020
-- ------------------------------------------------------
-- Server version	8.0.16

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `achievement`
--

DROP TABLE IF EXISTS `achievement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `achievement` (
  `id_achievement` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(45) NOT NULL,
  `description` varchar(200) NOT NULL,
  `img_achievement` longblob NOT NULL,
  PRIMARY KEY (`id_achievement`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `administrator`
--

DROP TABLE IF EXISTS `administrator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `administrator` (
  `id_administrator` varchar(19) NOT NULL,
  `username` varchar(45) NOT NULL,
  `password` varchar(150) NOT NULL,
  `cookie_consent` tinyint(4) NOT NULL DEFAULT '1',
  `root` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_administrator`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `answer`
--

DROP TABLE IF EXISTS `answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `answer` (
  `id_answer` int(11) NOT NULL AUTO_INCREMENT,
  `comment` varchar(1000) DEFAULT NULL,
  `answer_number` int(11) DEFAULT NULL,
  `cookie` varchar(20) NOT NULL,
  `id_question` int(11) NOT NULL,
  PRIMARY KEY (`id_answer`),
  KEY `id_user_idx` (`cookie`),
  KEY `id_question_idx` (`id_question`),
  CONSTRAINT `id_question` FOREIGN KEY (`id_question`) REFERENCES `question` (`id_question`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `id_user` FOREIGN KEY (`cookie`) REFERENCES `user` (`cookie`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=986 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `keyword`
--

DROP TABLE IF EXISTS `keyword`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `keyword` (
  `id_keyword` int(11) NOT NULL AUTO_INCREMENT,
  `Keyword` varchar(100) NOT NULL,
  PRIMARY KEY (`id_keyword`)
) ENGINE=InnoDB AUTO_INCREMENT=410 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `leaderboard`
--

DROP TABLE IF EXISTS `leaderboard`;
/*!50001 DROP VIEW IF EXISTS `leaderboard`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8mb4;
/*!50001 CREATE VIEW `leaderboard` AS SELECT 
 1 AS `cookie`,
 1 AS `points`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `matchmaking`
--

DROP TABLE IF EXISTS `matchmaking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `matchmaking` (
  `id_match` int(11) NOT NULL AUTO_INCREMENT,
  `player` varchar(20) NOT NULL,
  `vision_challenger` int(11) NOT NULL,
  `points` int(11) NOT NULL DEFAULT '0',
  `thoughts` varchar(1000) DEFAULT NULL,
  `guessed_feeling` int(11) NOT NULL,
  `played_date` datetime NOT NULL,
  PRIMARY KEY (`id_match`),
  KEY `vision_challenger_idx` (`vision_challenger`),
  KEY `user_match_idx` (`player`),
  CONSTRAINT `user_match` FOREIGN KEY (`player`) REFERENCES `user` (`cookie`),
  CONSTRAINT `vision_challenger` FOREIGN KEY (`vision_challenger`) REFERENCES `vision` (`id_vision`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=268 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `narrative`
--

DROP TABLE IF EXISTS `narrative`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `narrative` (
  `id_narrative` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(200) NOT NULL,
  `lang` varchar(2) NOT NULL,
  PRIMARY KEY (`id_narrative`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `query_word`
--

DROP TABLE IF EXISTS `query_word`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `query_word` (
  `id_query_word` int(11) NOT NULL AUTO_INCREMENT,
  `query_word` varchar(100) NOT NULL,
  PRIMARY KEY (`id_query_word`)
) ENGINE=InnoDB AUTO_INCREMENT=193 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `question` (
  `id_question` int(11) NOT NULL AUTO_INCREMENT,
  `id_quiz` int(11) NOT NULL,
  `question_text` varchar(1000) NOT NULL,
  `answer_one` varchar(1000) NOT NULL,
  `answer_two` varchar(1000) NOT NULL,
  `answer_three` varchar(1000) NOT NULL,
  `answer_four` varchar(1000) NOT NULL,
  `answer_five` varchar(1000) NOT NULL,
  `comment_question_text` varchar(1000) NOT NULL,
  `show_question` tinyint(4) NOT NULL,
  `show_comment_question` tinyint(4) NOT NULL,
  `language` varchar(2) NOT NULL,
  `question_number` int(11) NOT NULL,
  PRIMARY KEY (`id_question`),
  KEY `id_quiz_idx` (`id_quiz`),
  CONSTRAINT `id_quiz` FOREIGN KEY (`id_quiz`) REFERENCES `quiz` (`id_quiz`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `quiz`
--

DROP TABLE IF EXISTS `quiz`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `quiz` (
  `id_quiz` int(11) NOT NULL AUTO_INCREMENT,
  `quiz_name` varchar(200) NOT NULL,
  PRIMARY KEY (`id_quiz`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `referral_code`
--

DROP TABLE IF EXISTS `referral_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `referral_code` (
  `id_referral_code` int(11) NOT NULL AUTO_INCREMENT,
  `value` varchar(15) NOT NULL,
  `name` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id_referral_code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `referral_code_scenario`
--

DROP TABLE IF EXISTS `referral_code_scenario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `referral_code_scenario` (
  `id_referral_code_scenario` int(11) NOT NULL AUTO_INCREMENT,
  `referral_code` int(11) NOT NULL,
  `scenario` int(11) NOT NULL,
  `released` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_referral_code_scenario`),
  KEY `referral_idx` (`referral_code`),
  KEY `scenario_idx` (`scenario`),
  CONSTRAINT `ref_sce` FOREIGN KEY (`referral_code`) REFERENCES `referral_code` (`id_referral_code`),
  CONSTRAINT `scenario_ref` FOREIGN KEY (`scenario`) REFERENCES `scenario` (`id_scenario`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `scenario`
--

DROP TABLE IF EXISTS `scenario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `scenario` (
  `id_scenario` int(11) NOT NULL AUTO_INCREMENT,
  `question_vision_first` varchar(500) NOT NULL,
  `description` varchar(1000) NOT NULL,
  `id_narrative` int(11) NOT NULL,
  `title` varchar(200) NOT NULL,
  `lang` varchar(2) NOT NULL,
  `quiz` int(11) NOT NULL,
  `img_path` longblob,
  `scenario_group` int(11) NOT NULL,
  `published` tinyint(1) NOT NULL DEFAULT '0',
  `creator` varchar(19) NOT NULL,
  `deleted` tinyint(1) NOT NULL,
  `img_credits` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_scenario`),
  KEY `narrative_idx` (`id_narrative`),
  KEY `quiz_idx` (`quiz`),
  KEY `creator` (`creator`),
  CONSTRAINT `creator` FOREIGN KEY (`creator`) REFERENCES `administrator` (`id_administrator`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `narrative` FOREIGN KEY (`id_narrative`) REFERENCES `narrative` (`id_narrative`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `quiz` FOREIGN KEY (`quiz`) REFERENCES `quiz` (`id_quiz`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user` (
  `cookie` varchar(20) NOT NULL,
  `country` varchar(90) DEFAULT NULL,
  `region` varchar(90) DEFAULT NULL,
  `nationality` varchar(90) DEFAULT NULL,
  `gender` enum('M','F','N') DEFAULT 'N',
  `age` int(11) DEFAULT '0',
  `mail` varchar(254) DEFAULT NULL,
  `skip` tinyint(4) NOT NULL DEFAULT '0',
  `cookie_consent` tinyint(4) NOT NULL DEFAULT '1',
  `referral_code` int(11) DEFAULT NULL,
  `education` int(11) DEFAULT '0',
  `interest` int(11) DEFAULT '0',
  `access_code` varchar(9) DEFAULT '000000000',
  `username` varchar(45) DEFAULT NULL,
  `password` varchar(150) DEFAULT NULL,
  `profile_picture` longblob,
  `activated` tinyint(1) NOT NULL DEFAULT '0',
  `entry_number` int(11) NOT NULL,
  PRIMARY KEY (`cookie`),
  KEY `refcode_idx` (`referral_code`),
  CONSTRAINT `refcode` FOREIGN KEY (`referral_code`) REFERENCES `referral_code` (`id_referral_code`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_achievement`
--

DROP TABLE IF EXISTS `user_achievement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user_achievement` (
  `id_user_achievement` int(11) NOT NULL AUTO_INCREMENT,
  `cookie` varchar(20) NOT NULL,
  `achievement` int(11) NOT NULL,
  PRIMARY KEY (`id_user_achievement`),
  KEY `achievement_ext_k_idx` (`achievement`),
  KEY `cookie_achievement` (`cookie`),
  CONSTRAINT `achievement_ext_k` FOREIGN KEY (`achievement`) REFERENCES `achievement` (`id_achievement`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `cookie_achievement` FOREIGN KEY (`cookie`) REFERENCES `user` (`cookie`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=297 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vision`
--

DROP TABLE IF EXISTS `vision`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `vision` (
  `id_vision` int(11) NOT NULL AUTO_INCREMENT,
  `cookie` varchar(20) NOT NULL,
  `scenario` int(11) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `img_path` longblob NOT NULL,
  `feelings` int(11) NOT NULL,
  `share_date` datetime NOT NULL,
  PRIMARY KEY (`id_vision`),
  KEY `user1_idx` (`cookie`),
  KEY `scenario_idx` (`scenario`),
  CONSTRAINT `scenario` FOREIGN KEY (`scenario`) REFERENCES `scenario` (`id_scenario`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user1` FOREIGN KEY (`cookie`) REFERENCES `user` (`cookie`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=176 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vision_keyword`
--

DROP TABLE IF EXISTS `vision_keyword`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `vision_keyword` (
  `id_vision_keyword` int(11) NOT NULL AUTO_INCREMENT,
  `id_vision` int(11) NOT NULL,
  `id_keyword` int(11) NOT NULL,
  PRIMARY KEY (`id_vision_keyword`),
  KEY `kw_idx` (`id_keyword`),
  KEY `v_idx` (`id_vision`),
  CONSTRAINT `kw` FOREIGN KEY (`id_keyword`) REFERENCES `keyword` (`id_keyword`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `v` FOREIGN KEY (`id_vision`) REFERENCES `vision` (`id_vision`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=530 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vision_query_word`
--

DROP TABLE IF EXISTS `vision_query_word`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `vision_query_word` (
  `id_vision_query_word` int(11) NOT NULL AUTO_INCREMENT,
  `vision` int(11) NOT NULL,
  `query_word` int(11) NOT NULL,
  `chosen` tinyint(4) NOT NULL,
  `position_x` double DEFAULT NULL,
  `position_y` double DEFAULT NULL,
  `url` varchar(500) DEFAULT NULL,
  `size_multiplier` double DEFAULT NULL,
  PRIMARY KEY (`id_vision_query_word`),
  KEY `vision_vqw_idx` (`vision`),
  KEY `query_word_vqw_idx` (`query_word`),
  CONSTRAINT `query_word_vqw` FOREIGN KEY (`query_word`) REFERENCES `query_word` (`id_query_word`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `vision_vqw` FOREIGN KEY (`vision`) REFERENCES `vision` (`id_vision`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=265 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'ideaslab2020'
--

--
-- Dumping routines for database 'ideaslab2020'
--

--
-- Final view structure for view `leaderboard`
--

/*!50001 DROP VIEW IF EXISTS `leaderboard`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `leaderboard` AS select `matchmaking`.`player` AS `cookie`,sum(`matchmaking`.`points`) AS `points` from `matchmaking` group by `matchmaking`.`player` union all select `user`.`cookie` AS `cookie`,0 AS `0` from `user` where (`user`.`cookie` in (select `matchmaking`.`player` from `matchmaking`) is false) order by `points` desc */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-05-19 12:52:44
