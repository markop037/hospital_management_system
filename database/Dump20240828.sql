-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: hospitalsystem
-- ------------------------------------------------------
-- Server version	5.5.5-10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `appointment`
--

DROP TABLE IF EXISTS `appointment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `patientId` int(11) NOT NULL,
  `doctorId` int(11) NOT NULL,
  `dateTime` date DEFAULT NULL,
  `status` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `patientId` (`patientId`),
  KEY `doctorId` (`doctorId`),
  CONSTRAINT `appointment_ibfk_1` FOREIGN KEY (`patientId`) REFERENCES `patient` (`id`),
  CONSTRAINT `appointment_ibfk_2` FOREIGN KEY (`doctorId`) REFERENCES `doctor` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointment`
--

LOCK TABLES `appointment` WRITE;
/*!40000 ALTER TABLE `appointment` DISABLE KEYS */;
INSERT INTO `appointment` VALUES (21,3,1,'2024-09-01','Hospitalized'),(22,4,2,'2024-09-02','Discharged'),(23,5,3,'2024-09-03','Awaiting Examination'),(24,6,4,'2024-09-04','Hospitalized'),(25,7,5,'2024-09-05','Discharged'),(26,8,6,'2024-09-06','Awaiting Examination'),(27,9,7,'2024-09-07','Hospitalized'),(28,10,1,'2024-09-08','Discharged'),(29,11,2,'2024-09-09','Awaiting Examination'),(30,12,3,'2024-09-10','Hospitalized');
/*!40000 ALTER TABLE `appointment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctor`
--

DROP TABLE IF EXISTS `doctor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `firstName` varchar(50) NOT NULL,
  `lastName` varchar(50) NOT NULL,
  `specialization` varchar(50) NOT NULL,
  `phoneNumber` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor`
--

LOCK TABLES `doctor` WRITE;
/*!40000 ALTER TABLE `doctor` DISABLE KEYS */;
INSERT INTO `doctor` VALUES (1,'John','Doe','Cardiology','123-456-7890'),(2,'Jane','Smith','Neurology','234-567-8901'),(3,'Robert','Brown','Pediatrics','345-678-9012'),(4,'Emily','Davis','Dermatology','456-789-0123'),(5,'Michael','Johnson','Orthopedics','567-890-1234'),(6,'Sarah','Wilson','General Surgery','678-901-2345'),(7,'David','Lee','Ophthalmology','789-012-3456');
/*!40000 ALTER TABLE `doctor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expense`
--

DROP TABLE IF EXISTS `expense`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `expense` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `patientId` int(11) NOT NULL,
  `amount` double NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `patientId` (`patientId`),
  CONSTRAINT `expense_ibfk_1` FOREIGN KEY (`patientId`) REFERENCES `patient` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expense`
--

LOCK TABLES `expense` WRITE;
/*!40000 ALTER TABLE `expense` DISABLE KEYS */;
INSERT INTO `expense` VALUES (1,3,150,'2024-09-01'),(2,4,200,'2024-09-02'),(3,5,100,'2024-09-03'),(4,6,250,'2024-09-04'),(5,7,300,'2024-09-05'),(6,8,120,'2024-09-06'),(7,9,180,'2024-09-07'),(8,10,220,'2024-09-08'),(9,11,140,'2024-09-09'),(10,12,160,'2024-09-10');
/*!40000 ALTER TABLE `expense` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicalrecord`
--

DROP TABLE IF EXISTS `medicalrecord`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicalrecord` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `patientId` int(11) NOT NULL,
  `diagnosis` text NOT NULL,
  `treatment` text NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `patientId` (`patientId`),
  CONSTRAINT `medicalrecord_ibfk_1` FOREIGN KEY (`patientId`) REFERENCES `patient` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicalrecord`
--

LOCK TABLES `medicalrecord` WRITE;
/*!40000 ALTER TABLE `medicalrecord` DISABLE KEYS */;
INSERT INTO `medicalrecord` VALUES (1,3,'Hypertension','Antihypertensive Medication','2024-09-01'),(2,4,'Diabetes Mellitus','Insulin Therapy','2024-09-02'),(3,5,'Chronic Kidney Disease','Dialysis','2024-09-03'),(4,6,'Pneumonia','Antibiotic Therapy','2024-09-04'),(5,7,'Fractured Femur','Surgical Repair','2024-09-05'),(6,8,'Myocardial Infarction','Cardiac Rehabilitation','2024-09-06'),(7,9,'Hypertension','Antihypertensive Medication','2024-09-07'),(8,10,'Diabetes Mellitus','Insulin Therapy','2024-09-08'),(9,11,'Chronic Kidney Disease','Dialysis','2024-09-09'),(10,12,'Pneumonia','Antibiotic Therapy','2024-09-10');
/*!40000 ALTER TABLE `medicalrecord` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient`
--

DROP TABLE IF EXISTS `patient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patient` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `firstName` varchar(50) NOT NULL,
  `lastName` varchar(50) NOT NULL,
  `umcn` varchar(13) NOT NULL,
  `dateOfBirth` date NOT NULL,
  `address` varchar(100) NOT NULL,
  `phoneNumber` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient`
--

LOCK TABLES `patient` WRITE;
/*!40000 ALTER TABLE `patient` DISABLE KEYS */;
INSERT INTO `patient` VALUES (3,'John','Doe','1234567890123','1985-03-25','123 Maple Street, Springfield, IL','3125550123'),(4,'Emily','Smith','2345678901234','1990-07-12','456 Oak Avenue, Brooklyn, NY','7185550456'),(5,'Michael','Johnson','3456789012345','1983-05-14','789 Pine Road, Austin, TX','5125550789'),(6,'Sarah','Williams','4567890123456','1995-11-01','101 Birch Lane, Seattle, WA','2065550912'),(7,'David','Brown','5678901234567','1988-09-22','202 Cedar Street, Miami, FL','3055551234'),(8,'Jessica','Jones','6789012345678','1992-02-10','303 Elm Drive, Denver, CO','7205551456'),(9,'Daniel','Garcia','7890123456789','1979-12-30','404 Willow Court, Boston, MA','6175551678'),(10,'Ashley','Martinez','8901234567890','1987-06-18','505 Aspen Circle, Los Angeles, CA','2135551890'),(11,'James','Davis','9012345678901','1993-04-05','606 Redwood Boulevard, San Francisco, CA','4155552012'),(12,'Amanda','Miller','0123456789012','1998-08-27','707 Maple Avenue, Chicago, IL','7735552234');
/*!40000 ALTER TABLE `patient` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-08-28 14:27:47
