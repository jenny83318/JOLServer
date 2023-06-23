-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: localhost    Database: joldb
-- ------------------------------------------------------
-- Server version	8.0.29

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
-- Table structure for table `jol_product`
--

DROP TABLE IF EXISTS `jol_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `jol_product` (
  `prodId` int NOT NULL AUTO_INCREMENT,
  `name` char(50) NOT NULL,
  `descript` char(255) DEFAULT NULL,
  `createDt` datetime NOT NULL,
  `updateDt` datetime NOT NULL,
  `price` int NOT NULL,
  `cost` int DEFAULT NULL,
  `qty` int DEFAULT NULL,
  `category` char(20) NOT NULL,
  `imgURL` varchar(150) DEFAULT NULL,
  `sizeInfo` char(15) DEFAULT NULL,
  `series` char(15) DEFAULT NULL,
  PRIMARY KEY (`prodId`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jol_product`
--

LOCK TABLES `jol_product` WRITE;
/*!40000 ALTER TABLE `jol_product` DISABLE KEYS */;
INSERT INTO `jol_product` VALUES (1,'柔彈慵懶針織衫上衣','ejejjekrllr','2023-05-07 00:00:00','2023-05-07 00:00:00',390,233,54,'1','https://drive.google.com/uc?id=1OBy2pivExzCcK8zfca3-KJMAQIxYzw9y,https://drive.google.com/uc?id=1nxzHM2enRyT8JjmtY4yZISBNP8lIQz8X','S,M,L','new'),(2,'MIT 法式logo細肩BraTop','ekekrkkrkrk','2023-05-09 00:00:00','2023-05-09 00:00:00',399,250,12,'1','https://drive.google.com/uc?id=1HV29-tcno89Ig9z_y1vqkgWHhhx1Hih2,https://drive.google.com/uc?id=12q9PGSMpO3XQlegKofFFWjmj7aJsPkpx','S,M,L','new'),(3,'A\' 日常刺繡logo短版tee','jojkeokro','2023-05-09 00:00:00','2023-05-09 00:00:00',299,160,12,'1','https://drive.google.com/uc?id=1KI-4wjuhVFf3hr0cgSTcIyiPEd4ZP8g0,https://drive.google.com/uc?id=1oirCPUlSAU4-QOILyaAHTK6kfrlf40eR','S,M,L','sales'),(4,'小直筒九分彈性牛仔褲','wklkelkelkew','2023-05-09 00:00:00','2023-05-09 00:00:00',799,450,188,'2','https://drive.google.com/uc?id=1iCLUPCJmh8RQLn2RIoXEXvXChq4ia3zz,https://drive.google.com/uc?id=1v7VROX7owyZjpBuZscBQzcrXCymoprk7','S,M,L','sales'),(5,'MIT Holiday Lover 貓咪印花Tee','wklkelkelkewwklkelkelkew','2023-05-12 00:00:00','2023-05-12 00:00:00',599,400,120,'1','https://drive.google.com/uc?id=1LlIV-equCDEnuxGppSmXMPANQSqCEY_V,https://drive.google.com/uc?id=12WDhx4ErvIt7vj_5UHxDy9YK-vfbfGns','S,M,L','sales'),(6,'時髦綁帶美背洋裝','wklkelkelkew','2023-05-12 00:00:00','2023-05-12 00:00:00',399,120,50,'3','https://drive.google.com/uc?id=11ozEIRUCjpwm_cU8ANts40X8qrRtdYPC,https://drive.google.com/uc?id=1p6vJvT490BVCZm0SGK2DzKWOVuWiIvEz','S,M,L','new'),(7,'棉麻澎澎袖綁帶上衣','wklkelkelkew','2023-05-12 00:00:00','2023-05-12 00:00:00',298,100,13,'1','https://drive.google.com/uc?id=1o5wx4TYUFP4N-NH_MkZFMVVqK-tNhwQw,https://drive.google.com/uc?id=1uVqdEKnKZ5qVTutDQUVJFMUjOqnVpQ23','S,M,L','new'),(8,'立體粗編織微削肩背心','wklkelkelkew','2023-05-12 00:00:00','2023-05-12 00:00:00',690,345,44,'1','https://drive.google.com/uc?id=1Qv-hxAZEeB4Tw0n9n_LyLzGi64xs2P2q,https://drive.google.com/uc?id=1OyPQ_P2PKB9wfMvBClAlNG1ne6XVvcJm','S,M,L','sales'),(9,'透膚感波浪圓領上衣','wklkelkelkew','2023-05-12 00:00:00','2023-05-12 00:00:00',780,339,40,'1','https://drive.google.com/uc?id=1E4_A8dfKkigz02WNcqlMklWsrMrEWLbf,https://drive.google.com/uc?id=1fsWb78Jr9tfA9jZZm7EjHnLT4n3yHRKh','S,M,L','sales'),(10,'澎袖荷葉領短襯衫','wklkelkelkew','2023-05-12 00:00:00','2023-05-12 00:00:00',540,236,50,'1','https://drive.google.com/uc?id=1aYZ7TH6CperZ_Y7jgDuNhm8RZyUYf-7C,https://drive.google.com/uc?id=19jaa9c4lqoMYDvIhP2TyIzrj_h_u_wgf','S,M,L','new'),(11,'寬肩後開岔西裝外套','wklkelkelkew','2023-05-12 00:00:00','2023-05-12 00:00:00',299,100,29,'1','https://drive.google.com/uc?id=17rMibdlm2YHWodcplNWwUekzM-i9HTFk,https://drive.google.com/uc?id=1T_qpd5A_y6EVsFHlM3HLhAJlAXcz1qTq','S,M,L','new'),(12,'男友落地牛仔吊帶褲','wklkelkelkew','2023-05-12 00:00:00','2023-05-12 00:00:00',499,283,30,'3','https://drive.google.com/uc?id=1bHyUynX-erzEKTDDGONmnIBABELNdjUi,https://drive.google.com/uc?id=1kp3kYUYeHD9pgyXbwgAQX0RH1Y2J0t4Z','S,M,L','new'),(13,'柔滑涼感絲鬆緊長腿褲','skkkekek','2023-05-12 00:00:00','2023-05-12 00:00:00',690,400,34,'2','https://drive.google.com/uc?id=1d96xlx0bWJM5lED9tlkWRNHRGE4xO7hT,https://drive.google.com/uc?id=1bLid3HJBXgmEGjTu6dVIThgQZDDKrlaX','S,M,L','sport'),(14,'MIT 超舒適涼感彈力內搭褲(涼感紗)','skkkekek','2023-05-12 00:00:00','2023-05-12 00:00:00',399,123,47,'2','https://drive.google.com/uc?id=1yifiaLdNSBpdljtP5GpiAx4dBN2zQ6gb,https://drive.google.com/uc?id=1E1ZCvwvIFdqLV1jIjGElNcYGIXJd-GV7','S,M,L','sport'),(15,'MIT 超彈力運動緊身褲','skkkekek','2023-05-12 00:00:00','2023-05-12 00:00:00',499,150,33,'2','https://drive.google.com/uc?id=1QnOH8Gvw31ISx95OuN2lHGHJuxtX3drz,https://drive.google.com/uc?id=1FzYvxHssMyIQvJFEN9pfVIgOH9Tyww0g','S,M,L','sport'),(16,'MIT 零著感牛奶絲短褲(涼感紗)','skkkekek','2023-05-12 00:00:00','2023-05-12 00:00:00',399,178,45,'2','https://drive.google.com/uc?id=1gzfx0nCORs-HDfFYK4Z8ZsTJyTFAkpAY,https://drive.google.com/uc?id=1aFyKpoYv_ErkrzWFPWEDHvIZuiSrvNmR','S,M,L','sport'),(17,'品牌寬摺小圓裙','skkkekek','2023-05-12 00:00:00','2023-05-12 00:00:00',790,450,24,'2','https://drive.google.com/uc?id=1DKOKNmom7woq9Km0t8J13ZNlrLSeGoh6,https://drive.google.com/uc?id=1XbxE8LM23hd0CTc72mpPohopJntYv4o8','S,M,L','new'),(18,'A\'a刺繡長腿斜紋牛仔褲','skkkekek','2023-05-12 00:00:00','2023-05-12 00:00:00',1290,789,55,'2','https://drive.google.com/uc?id=151tr0IpYZQrYghuk9v5W6DgrFRJcBxSy,https://drive.google.com/uc?id=18qktWenGqOhmG0tvzE3kWhjNhFzx-Qf0','S,M,L','new'),(19,'小香毛呢細肩洋裝','skkkekek','2023-05-12 00:00:00','2023-05-12 00:00:00',1090,630,22,'3','https://drive.google.com/uc?id=1xuLxMAsf9Admbwcs3rHzWhmCDiQP_t8w,https://drive.google.com/uc?id=1MMFTbSL-OTBP29cS_d8jc1QN9z6alB9f','S,M,L','sales'),(20,'小香風排釦造型短洋裝','skkkekek','2023-05-12 00:00:00','2023-05-12 00:00:00',990,500,23,'3','https://drive.google.com/uc?id=1IkOJhwszARxJz3QjdaPHcNSNhxja454E,https://drive.google.com/file/d/1E72hpZmt7Gk4VSMIgbTKVZBHtsFi30Mv/view?usp=drive_link','S,M,L','sales'),(21,'微透肌美背綁帶短洋裝','skkkekek','2023-05-12 00:00:00','2023-05-12 00:00:00',890,790,50,'3','https://drive.google.com/uc?id=1ofJMd4FlgeaP6r6Q-B-me-B8nUEonNJ9,https://drive.google.com/uc?id=1bqDUxF31wYVbO7gPSgh4fxf66rK4-wxO','S,M,L','sales'),(22,'質感反摺褲頭修身短裙','skkkekek','2023-05-12 00:00:00','2023-05-12 00:00:00',690,500,35,'2','https://drive.google.com/uc?id=1wfIqb2dp6NC-dwlLaDR7YbKEW4GFlHIp,https://drive.google.com/uc?id=16fgj_l1MbQkmlWpT-PRcQxhlf28lgI1r','S,M,L','sales'),(23,'時髦及膝半身牛仔裙','skkkekek','2023-05-12 00:00:00','2023-05-12 00:00:00',890,640,35,'2','https://drive.google.com/uc?id=1GhyqOMMfJNfFOdvFaluD8XhTrTEfdeHS,https://drive.google.com/file/d/1Kh8Aqia7GE4xF0SB3CVCPQEey5SryUMx/view?usp=drive_link','S,M,L','new'),(24,'MORE REAL 高彈力提臀四分運動褲','skkkekek','2023-05-12 00:00:00','2023-05-12 00:00:00',399,200,89,'2','https://drive.google.com/uc?id=1idAQPHMNW5FFV0XpuK-YBpJqEXNJiqLH,https://drive.google.com/uc?id=1pEbRYQfz4HOFYfKdqKdGdi4JeD0KKTRS','S,M,L','sport'),(25,'& IN THING 時髦運動短褲','skkkekek','2023-05-12 00:00:00','2023-05-12 00:00:00',490,300,57,'2','https://drive.google.com/uc?id=1YAvHBiKn6IPE92EqwOLdZ_alS80KXWIk,https://drive.google.com/uc?id=1wwGB9Q0YPQ9NKHiWh4YFKRIHbQZatG-x','S,M,L','sport'),(26,'MIT 品牌刺繡彈性羅紋五分褲','skkkekek','2023-05-12 00:00:00','2023-05-12 00:00:00',490,200,23,'2','https://drive.google.com/uc?id=1giXi0caYt8qZserKzh12PkOVvgCQmbi4,https://drive.google.com/uc?id=1E631nkE9wdcz6z21Gp-9FJeGcgXi7i6D','S,M,L','sport'),(27,'日常小個性牛仔吊帶褲','skkkekek','2023-05-12 00:00:00','2023-05-12 00:00:00',1190,890,23,'3','https://drive.google.com/uc?id=1eXvDy1Jnro64KhL6MTISvGPJf7utUkw-,https://drive.google.com/uc?id=1kUJoiLmELfzicOyW7t3X349AzGyedd-h','S,M,L','new'),(28,'品牌LOGO環保托特兩用包','skkkekek','2023-05-12 00:00:00','2023-05-12 00:00:00',390,180,80,'4','https://drive.google.com/uc?id=1AQJo6tPeSj2mKl4Y8A3Id3rpMp5Kl656,https://drive.google.com/uc?id=1sD37-ce0LQ2Y1nsfn7rfuay5wXXaRh4_','F','sales'),(29,'URBAN RESEARCH聯名款條紋帆布包','skkkekek','2023-05-12 00:00:00','2023-05-12 00:00:00',490,270,100,'4','https://drive.google.com/uc?id=1r8hHQypbsRurUz2onb6jx7INloMuV37c,https://drive.google.com/uc?id=1rfZ_11uYnWyaoQ5j733NUCGByJePC6qH','F','new'),(30,'品牌LOGO菱格紋立鋪棉包','skkkekek','2023-05-12 00:00:00','2023-05-12 00:00:00',590,389,100,'4','https://drive.google.com/uc?id=1QQELB9Y02u28Io16oFnWKtMAWPCvfr4T,https://drive.google.com/uc?id=1zsnK55k4UxEbBbGNC9QKChITrJzA5ECt','F','sales'),(31,'細緻光澤立體花朵針式耳環','skkkekek','2023-05-12 00:00:00','2023-05-12 00:00:00',299,100,70,'4','https://drive.google.com/uc?id=1VLFPSwQQq2AYIP4pJ1jVnZ2-Y4V4Rb4d,https://drive.google.com/uc?id=1XRoPYS7tYv8bQ7t4DFmSxpMMtqo_gEe0','F','sales'),(32,'品牌LOGO造型刷刷漁夫帽','skkkekek','2023-05-12 00:00:00','2023-05-12 00:00:00',690,299,80,'4','https://drive.google.com/uc?id=11VUl37m3YTs846fru6X7Sq6hQIroSV5b,https://drive.google.com/uc?id=1jZdOTl9OejU_0HH_7HjGVxD0rtbK19qQ','F','new'),(33,'夏日日常刺繡棒球帽','skkkekek','2023-05-12 00:00:00','2023-05-12 00:00:00',690,240,70,'4','https://drive.google.com/uc?id=1ECVFZND544iRZ4UkIJ_bJvHdHbPGt9g8,https://drive.google.com/uc?id=1y0cRXSrPOn3a7cTqirfsXrxgg9aCKw8n','F','sales'),(34,'圓領透紗澎袖上衣','skkkekek','2023-05-12 00:00:00','2023-05-12 00:00:00',590,300,45,'1','https://drive.google.com/uc?id=1rcGaXKsb3KmRgioP60sOIHYo1RVFZMWp,https://drive.google.com/uc?id=1V8Ged-N1jT_R1EVomMghSrH-aaFHKYrt','S,M,L','new'),(35,'愛心釦簍空針織外套','skkkekek','2023-05-12 00:00:00','2023-05-12 00:00:00',890,600,50,'1','https://drive.google.com/uc?id=1MwDl6HfpfPka5Tf_KRcVVo-cDtukeM9q,https://drive.google.com/uc?id=1qRzMOIgIP-1kSWBp1zpdFD915F4mL-JA','S,M,L','sales'),(36,'率性設計感牛仔外套(寬鬆版)','skkkekek','2023-05-12 00:00:00','2023-05-12 00:00:00',1390,890,30,'1','https://drive.google.com/uc?id=1uI50rI6KDxBimuLKVc4ao2q_y9vpzfkJ,https://drive.google.com/uc?id=1loxY0bXMDCOukgYqlJPts_bQqt3KqHQ0','S,M,L','new'),(37,'時髦亮皮單肩包','skkkekek','2023-05-12 00:00:00','2023-05-12 00:00:00',690,350,28,'4','https://drive.google.com/uc?id=17IvrjeEJxOFuL_KgsF969UawasCNfjRa,https://drive.google.com/uc?id=1mPibo7F41sN9BSnTldRxansy9YZK1cc_','F','new'),(38,'品牌經典鋪棉圍巾','skkkekek','2023-05-12 00:00:00','2023-05-12 00:00:00',590,389,20,'4','https://drive.google.com/uc?id=18PObRIvdMxxa8RSIITNOHkVz-QumtyJr,https://drive.google.com/file/d/11xnYWa0tNwc39HAYj59a0ZpH7_DrRJOn/view?usp=drive_link','F','sales'),(39,'立體麻花針織中筒襪','skkkekek','2023-05-12 00:00:00','2023-05-12 00:00:00',190,100,70,'4','https://drive.google.com/uc?id=1TwPUtnvFx3eFg7awwJRxKutppm1W9a18,https://drive.google.com/uc?id=1SA82hQxDMb1RsAdrzpA3FJffP1fFVvog','F','sales');
/*!40000 ALTER TABLE `jol_product` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-06-23 21:50:27
