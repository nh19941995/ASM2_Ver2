-- MySQL dump 10.13  Distrib 9.0.1, for Linux (x86_64)
--
-- Host: localhost    Database: funix_asm_2
-- ------------------------------------------------------
-- Server version	9.0.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `apply_post`
--

DROP TABLE IF EXISTS `apply_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `apply_post` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_date` date NOT NULL,
  `text` varchar(255) NOT NULL,
  `cv_id` bigint DEFAULT NULL,
  `recruitment_id` bigint DEFAULT NULL,
  `status_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKlpgh2itnnyqygsepvqetxl8kw` (`cv_id`),
  KEY `FKsobes6hef7fhussdxgvntmalc` (`recruitment_id`),
  KEY `FKemvth85cvtahde3psb6yjo2qm` (`status_id`),
  CONSTRAINT `FKemvth85cvtahde3psb6yjo2qm` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`),
  CONSTRAINT `FKlpgh2itnnyqygsepvqetxl8kw` FOREIGN KEY (`cv_id`) REFERENCES `cv` (`id`),
  CONSTRAINT `FKsobes6hef7fhussdxgvntmalc` FOREIGN KEY (`recruitment_id`) REFERENCES `recruitment` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `apply_post`
--

LOCK TABLES `apply_post` WRITE;
/*!40000 ALTER TABLE `apply_post` DISABLE KEYS */;
INSERT INTO `apply_post` VALUES (21,'2024-09-21','1',3,1,1),(22,'2024-09-21','1',3,2,1),(23,'2024-09-21','1',3,3,1),(24,'2024-09-21','1',3,5,1),(25,'2024-09-21','1',3,4,1),(26,'2024-09-21','1',3,6,1),(27,'2024-09-21','1',6,1,1),(28,'2024-09-21','1',6,4,1),(29,'2024-09-21','1',6,5,1),(30,'2024-09-21','1',6,7,1),(31,'2024-09-21','1',6,9,1),(32,'2024-09-21','1',6,10,1);
/*!40000 ALTER TABLE `apply_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `number_choose` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'Java',9),(2,'Python',9),(3,'C#',6),(4,'C++',8),(5,'Ruby',8),(6,'PHP',9),(7,'JavaScript',4),(8,'HTML',3);
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `company`
--

DROP TABLE IF EXISTS `company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `company` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name_company` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `address` varchar(255) NOT NULL,
  `description` text NOT NULL,
  `email` varchar(255) NOT NULL,
  `logo` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) NOT NULL,
  `status_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqm7nvdm1ndyuybpx3uei4t4v1` (`status_id`),
  CONSTRAINT `FKqm7nvdm1ndyuybpx3uei4t4v1` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company`
--

LOCK TABLES `company` WRITE;
/*!40000 ALTER TABLE `company` DISABLE KEYS */;
INSERT INTO `company` VALUES (7,'Apple','1 Infinite Loop, Cupertino, CA 95014, USA','is an American multinational technology company that specializes in designing and manufacturing consumer electronics, computer software, and online services.','apple@gmail.com','nguyenvanan1.png','0773307333',1),(8,'Amazon','Usa','rich','amazon@gmail.com','hoangvanem5.png','4773307333',1),(9,'Samsung','165 Đ. Cầu Giấy, Quan Hoa, Cầu Giấy','Bán điện thoại','samsung@gmail.com','hoangthikim10.png','5773307333',1),(10,'Nokia','Mỹ Đình','Bán cá','nokia@gmail.com','nguyenthifung6.png','6773307333',1),(11,'Vinamilk','Sao Hỏa','Bán quần Áo','vinamilk@gmail.com','nguyenvanlam11.png','7773307333',1),(12,'Viettel','Hà Nội','Bán bánh rán','viettel@gmail.com','hieudaica1.png','0773307336',1),(13,'Vinasin','165 Đ. Cầu Giấy, Quan Hoa, Cầu Giấy','Sắp phá sản','vinasin@gmail.com','hieudaica1.png','2222222222',1),(14,'vinamil','165 Đ. Cầu Giấy, Quan Hoa, Cầu Giấy','1','fanozawaht@gmail.com','hieudaica1.png','1111111111',1);
/*!40000 ALTER TABLE `company` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `company_followers`
--

DROP TABLE IF EXISTS `company_followers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `company_followers` (
  `user_id` bigint NOT NULL,
  `company_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`company_id`),
  KEY `FK5xb9koylo6qjdpk098af1j3fa` (`company_id`),
  CONSTRAINT `FK5xb9koylo6qjdpk098af1j3fa` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`),
  CONSTRAINT `FKf2hfux937o3e24ha60cymty35` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company_followers`
--

LOCK TABLES `company_followers` WRITE;
/*!40000 ALTER TABLE `company_followers` DISABLE KEYS */;
INSERT INTO `company_followers` VALUES (3,7),(8,7),(9,7),(9,8),(9,9),(9,11),(2,12),(3,12),(8,13),(9,13);
/*!40000 ALTER TABLE `company_followers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cv`
--

DROP TABLE IF EXISTS `cv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cv` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cv`
--

LOCK TABLES `cv` WRITE;
/*!40000 ALTER TABLE `cv` DISABLE KEYS */;
INSERT INTO `cv` VALUES (1,'nguyenvanan1.pdf'),(2,'tranthivinh2.pdf'),(3,'phamvaninh9.pdf'),(4,'levancuong3.pdf'),(5,'phamthidung4.pdf'),(6,'lethihuong8.pdf');
/*!40000 ALTER TABLE `cv` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recruiment_followers`
--

DROP TABLE IF EXISTS `recruiment_followers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recruiment_followers` (
  `user_id` bigint NOT NULL,
  `recruiment_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`recruiment_id`),
  KEY `FKjgrjvqfrr2adh1ky9kcfedfh8` (`recruiment_id`),
  CONSTRAINT `FKjgrjvqfrr2adh1ky9kcfedfh8` FOREIGN KEY (`recruiment_id`) REFERENCES `recruitment` (`id`),
  CONSTRAINT `FKqj56s3wx0ea6abwaftgjrnc18` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recruiment_followers`
--

LOCK TABLES `recruiment_followers` WRITE;
/*!40000 ALTER TABLE `recruiment_followers` DISABLE KEYS */;
INSERT INTO `recruiment_followers` VALUES (8,1),(9,1),(19,1),(3,2),(8,2),(9,2),(19,2),(3,3),(8,3),(9,3),(19,3),(3,4),(8,4),(9,4),(19,4),(3,5),(8,5),(9,5),(19,5),(9,6),(19,6),(9,7),(19,7),(9,8),(19,8),(9,9),(19,9),(9,10);
/*!40000 ALTER TABLE `recruiment_followers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recruitment`
--

DROP TABLE IF EXISTS `recruitment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recruitment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `deadline` date NOT NULL,
  `description` text NOT NULL,
  `experience` varchar(255) NOT NULL,
  `quantity` int NOT NULL,
  `recruitment_rank` varchar(255) DEFAULT NULL,
  `salary` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `view` int DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  `company_id` bigint DEFAULT NULL,
  `status_id` bigint DEFAULT NULL,
  `type_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKtj8cu8x5ar1c5hmu0j3veteit` (`category_id`),
  KEY `FKc8ro055m1iceebbktg9epdci9` (`company_id`),
  KEY `FKbwua5wtr4onachekp0n4q922i` (`status_id`),
  KEY `FK19xfcm8nup3xd78cgra7cm2lk` (`type_id`),
  CONSTRAINT `FK19xfcm8nup3xd78cgra7cm2lk` FOREIGN KEY (`type_id`) REFERENCES `type` (`id`),
  CONSTRAINT `FKbwua5wtr4onachekp0n4q922i` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`),
  CONSTRAINT `FKc8ro055m1iceebbktg9epdci9` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`),
  CONSTRAINT `FKtj8cu8x5ar1c5hmu0j3veteit` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),
  CONSTRAINT `recruitment_chk_1` CHECK ((`quantity` >= 1))
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recruitment`
--

LOCK TABLES `recruitment` WRITE;
/*!40000 ALTER TABLE `recruitment` DISABLE KEYS */;
INSERT INTO `recruitment` VALUES (1,'123 Tech Street, Silicon Valley','2024-09-21 06:51:59.941745','2023-07-15','We are looking for a skilled Software Developer to join our team.','3-5 years',2,NULL,'$80,000 - $120,000','Senior Software Developer',NULL,6,13,1,1),(2,'456 Innovation Ave, San Francisco','2024-09-21 06:51:46.871969','2023-07-20','Seeking a Network Engineer to maintain and improve our infrastructure.','2-4 years',1,NULL,'$70,000 - $90,000','Network Engineer 2025',NULL,2,13,1,1),(3,'789 Data Lane, New York','2024-09-21 04:37:11.289872','2023-07-25','Data Analyst needed to interpret complex data sets.','1-3 years',3,NULL,'$60,000 - $80,000','Junior Data Analyst',NULL,3,13,1,1),(4,'321 Support Road, Chicago','2024-09-21 03:48:20.035445','2023-07-30','IT Support Specialist needed to assist with technical issues.','0-2 years',2,NULL,'$45,000 - $65,000','IT Support Specialist',NULL,4,9,1,1),(5,'654 Management Blvd, Boston','2024-09-21 00:16:27.833318','2023-08-05','Experienced Project Manager needed for our IT department.','5-7 years',66,NULL,'$100,000 - $140,000','IT Project Manager',NULL,5,7,1,1),(6,'987 Design Street, Los Angeles','2024-09-21 00:17:13.315230','2023-08-10','UI/UX Designer needed to create intuitive user interfaces.','3-5 years',200,NULL,'$75,000 - $95,000','UI/UX Designer',NULL,6,8,1,1),(7,'741 System Avenue, Seattle','2023-06-07 10:45:00.000000','2023-08-15','Systems Administrator needed to maintain our IT infrastructure.','4-6 years',1,'Senior','$85,000 - $115,000','Senior Systems Administrator',130,7,7,1,2),(8,'852 Database Road, Austin','2023-06-08 13:00:00.000000','2023-08-20','Database Administrator needed to manage and optimize our databases.','3-5 years',1,'Mid-level','$80,000 - $100,000','Database Administrator',140,8,7,1,1),(9,'369 Fullstack Street, San Jose','2023-06-09 14:30:00.000000','2023-08-25','Full Stack Developer needed for our web development team.','4-6 years',2,'Senior','$90,000 - $130,000','Senior Full Stack Developer',180,1,11,1,1),(10,'165 Đ. Cầu Giấy, Quan Hoa, Cầu Giấy','2024-09-21 05:20:09.398302','2024-09-28','Backend','5 year',4,NULL,'2000$','App Mobile',NULL,1,14,1,1);
/*!40000 ALTER TABLE `recruitment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `class_css` varchar(255) NOT NULL,
  `role_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'badge badge-info','ROLE_USER'),(2,'badge badge-success','ROLE_MANAGER');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `save_job`
--

DROP TABLE IF EXISTS `save_job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `save_job` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `recruitment_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKnc5ou90hb8txo41ofxeafvl53` (`recruitment_id`),
  UNIQUE KEY `UKhw3cv6918xat2mpyspb18jl2k` (`user_id`),
  CONSTRAINT `FK7mhpldb69f4sjyn9ijta42tep` FOREIGN KEY (`recruitment_id`) REFERENCES `recruitment` (`id`),
  CONSTRAINT `FKpkwn87ixxhdfek8rbljowpbdp` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `save_job`
--

LOCK TABLES `save_job` WRITE;
/*!40000 ALTER TABLE `save_job` DISABLE KEYS */;
/*!40000 ALTER TABLE `save_job` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `status` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `status_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `class_css` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status`
--

LOCK TABLES `status` WRITE;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO `status` VALUES (1,'ACTIVE','badge badge-info','User is active'),(2,'BLOCK','badge badge-primary','User is block'),(3,'INACTIVE','badge badge-danger','User is inactive');
/*!40000 ALTER TABLE `status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type`
--

DROP TABLE IF EXISTS `type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type`
--

LOCK TABLES `type` WRITE;
/*!40000 ALTER TABLE `type` DISABLE KEYS */;
INSERT INTO `type` VALUES (1,'Full Time','Full Time'),(2,'Part Time','Part Time');
/*!40000 ALTER TABLE `type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `token` varchar(255) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  `company_id` bigint DEFAULT NULL,
  `cv_id` bigint DEFAULT NULL,
  `role_id` bigint DEFAULT NULL,
  `status_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKob8kqyqqgmefl0aco34akdtpe` (`email`),
  UNIQUE KEY `UK589idila9li6a4arw1t8ht1gx` (`phone`),
  UNIQUE KEY `UKsb8bbouer5wak8vyiiy4pf2bx` (`username`),
  UNIQUE KEY `UKbteyn2vjkuydkfqefgaje2rhr` (`company_id`),
  UNIQUE KEY `UK8fcepkeasy5jx0y14f9sr5ibx` (`cv_id`),
  KEY `FKn82ha3ccdebhokx3a8fgdqeyy` (`role_id`),
  KEY `FKr62indkt0r2anb0m8hy5ldfpd` (`status_id`),
  CONSTRAINT `FK2xy5nulvty7anu7svqyskyu1r` FOREIGN KEY (`cv_id`) REFERENCES `cv` (`id`),
  CONSTRAINT `FK2yuxsfrkkrnkn5emoobcnnc3r` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`),
  CONSTRAINT `FKn82ha3ccdebhokx3a8fgdqeyy` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `FKr62indkt0r2anb0m8hy5ldfpd` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Ha Noi','Software Developer','nguyenvanan1@gmail.com','Nguyen Van An','nguyenvanan1.jpg','{bcrypt}$2a$10$08Y.m13TXWj3VfW4yk0.6ea2RcDPQ63..FebXXnLJVN2cMJEMoLI.','0123456801',NULL,'nguyenvanan1',7,NULL,2,1),(2,'Ho Chi Minh City','Data Analyst','tranthivinh2@gmail.com','Tran Thi Binh','tranthivinh2.jpg','{bcrypt}$2a$10$wWgvYFK69v91OxNyJKRvG.0zkJM6P.Hbszf7qTMRflxyZQ9W1JFpm','0123456802',NULL,'tranthivinh2',NULL,2,1,1),(3,'Da Nang','Project Manager','levancuong3@gmail.com','Le Van Cuong','levancuong3.jpg','{bcrypt}$2a$10$zYwW56WHOAURgVImWM86T.5rPSbyiVbPjHsGrQiod0KaN8rjAq7hi','0123456803',NULL,'levancuong3',NULL,4,1,1),(4,'Hai Phong','UI/UX Designer','phamthidung4@gmail.com','Pham Thi Dung','phamthidung4.jpg','{bcrypt}$2a$10$ntVMdbQDK7P/Ys9IQ6GcZ.rWaLd6xVSzM7.GZCJd/5Vcd3Hx6XBTm','0123456804',NULL,'phamthidung4',NULL,5,1,1),(5,'Can Tho','System Administrator','hoangvanem5@gmail.com','Hoang Van Em',NULL,'{bcrypt}$2a$10$1bLXRRbxVJGcJulqNPztTuhjyDvx8MGj7gUODlPrMnCe5tAdVE/ty','0123456805',NULL,'hoangvanem5',8,NULL,2,1),(6,'Nha Trang','Marketing Specialist','nguyenthifung6@gmail.com','Nguyen Thi Fung',NULL,'{bcrypt}$2a$10$94b54vJRcP5cQGl26dFhzu7R/MNmGuooMguNdUrANdQi8KXVdtqLq','0123456806',NULL,'nguyenthifung6',10,NULL,2,1),(7,'Vung Tau','Sales Representative','tranvangiang7@gmail.com','Tran Van Giang',NULL,'{bcrypt}$2a$10$wAP2K5S88KCrud9kgGFOJeHZUdmVBOp1sOHJJ/LvQqEcBeGZyvuOK','0123456807',NULL,'tranvangiang7',NULL,NULL,1,1),(8,'Hue','HR Manager','lethihuong8@gmail.com','Le Thi Huong','lethihuong8.jpg','{bcrypt}$2a$10$Pdj/QnV5ZCLvZ9.PdRNb7.dX7k3y8X.DkdGDzwLaGWhlgKn3vwFS.','0123456808',NULL,'lethihuong8',NULL,6,1,1),(9,'Quy Nhon','Financial Analyst','phamvaninh9@gmail.com','Pham Van Inh','phamvaninh9.jpg','{bcrypt}$2a$10$fZcoEhkgBwf3AIg7gIS3Auye.zlksW6SZM.1wJ205VoX1WmL7XwA.','0123456809',NULL,'phamvaninh9',14,3,2,1),(10,'Bien Hoa','Content Writer','hoangthikim10@gmail.com','Hoang Thi Kim',NULL,'{bcrypt}$2a$10$5LNQoF37sKJuXivzuisOJeOPO25GpFlhL2OS8xl56NQkLbgqNRzPy','0123456810',NULL,'hoangthikim10',9,NULL,2,1),(11,'Thai Nguyen','Network Engineer','nguyenvanlam11@gmail.com','Nguyen Van Lam',NULL,'{bcrypt}$2a$10$EpHGlz1soT1BLLumy03lWeDIV8.jzumscuh8WriveIN2aOJaGSyHq','0123456811',NULL,'nguyenvanlam11',11,NULL,2,1),(19,'165 Đ. Cầu Giấy, Quan Hoa, Cầu Giấy','1','fanozawaht@gmail.com','Nguyễn Trung Hiếu',NULL,'{bcrypt}$2a$10$avFQy3LEHKjYVY7ruBSzE.bmnJbmT.F0ZngX.H7Aoxh7YKiKGj7mG','0773307336','hieudaica11726850414767','hieudaica1',13,NULL,2,1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-09-21 15:29:17
