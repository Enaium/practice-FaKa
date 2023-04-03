-- MariaDB dump 10.19  Distrib 10.11.2-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: faka
-- ------------------------------------------------------
-- Server version	10.11.2-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `t_category`
--

DROP TABLE IF EXISTS `t_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_category` (
  `id` uuid NOT NULL COMMENT '分类主键',
  `user_id` uuid NOT NULL COMMENT '分类主键',
  `name` varchar(40) NOT NULL COMMENT '分类名称',
  `priority` int(11) NOT NULL DEFAULT 0 COMMENT '显示优先级',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品分类';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_category`
--

LOCK TABLES `t_category` WRITE;
/*!40000 ALTER TABLE `t_category` DISABLE KEYS */;
INSERT INTO `t_category` VALUES
('494fde25-10d6-4055-a0a0-54909ae035bf','a9f3cc40-f6cc-4abc-bbed-2b6379546d5e','软件',1,'2023-03-29 17:09:59','2023-03-29 17:11:09');
/*!40000 ALTER TABLE `t_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_goods`
--

DROP TABLE IF EXISTS `t_goods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_goods` (
  `id` uuid NOT NULL COMMENT '分类主键',
  `user_id` uuid NOT NULL COMMENT '分类主键',
  `category_id` uuid NOT NULL COMMENT '分类主键',
  `name` varchar(40) NOT NULL COMMENT '商品名称',
  `priority` int(11) NOT NULL DEFAULT 0 COMMENT '优先级',
  `price` decimal(6,1) NOT NULL COMMENT '价格',
  `description` varchar(500) NOT NULL COMMENT '商品描述',
  `type` enum('single','multi') NOT NULL DEFAULT 'multi' COMMENT '商品类型',
  `sale_order` enum('new','old') NOT NULL DEFAULT 'old' COMMENT '出售顺序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_goods`
--

LOCK TABLES `t_goods` WRITE;
/*!40000 ALTER TABLE `t_goods` DISABLE KEYS */;
INSERT INTO `t_goods` VALUES
('0000b040-09fa-44cd-96bc-571e6c603648','a9f3cc40-f6cc-4abc-bbed-2b6379546d5e','494fde25-10d6-4055-a0a0-54909ae035bf','Photoshop',1,0.9,'Adobe Photoshop，简称“PS”，是由Adobe Systems开发和发行的图像处理软件。Photoshop主要处理以像素所构成的数字图像。使用其众多的编修与绘图工具，可以有效地进行图片编辑和创造工作。PS 有很多功能，在图像、图形、文字、视频、出版等各方面都有涉及。','single','new','2023-03-29 17:12:41','2023-03-29 17:12:41'),
('b6e32e64-0787-4f32-88c9-a0da3ceb3c3e','a9f3cc40-f6cc-4abc-bbed-2b6379546d5e','494fde25-10d6-4055-a0a0-54909ae035bf','Office',1,2.0,'Microsoft Office [11]  是由Microsoft(微软)公司开发的一套办公软件套装。常用组件有 Word、Excel、PowerPoint等。','multi','old','2023-03-29 17:21:33','2023-03-29 17:21:33');
/*!40000 ALTER TABLE `t_goods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_kami`
--

DROP TABLE IF EXISTS `t_kami`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_kami` (
  `id` uuid NOT NULL COMMENT '主键',
  `goods_id` uuid NOT NULL,
  `user_id` uuid NOT NULL COMMENT '分类主键',
  `content` text NOT NULL,
  `status` enum('sell','sold','lock') NOT NULL DEFAULT 'sell' COMMENT '状态',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='卡密';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_kami`
--

LOCK TABLES `t_kami` WRITE;
/*!40000 ALTER TABLE `t_kami` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_kami` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_order`
--

DROP TABLE IF EXISTS `t_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_order` (
  `id` uuid NOT NULL COMMENT '注解',
  `goods_id` uuid NOT NULL COMMENT '商品ID',
  `user_id` uuid NOT NULL COMMENT '分类主键',
  `status` enum('closed','finished','success','wait_create','wait_pay') NOT NULL,
  `pay_type` enum('alipay','wechat') NOT NULL COMMENT '支付类型',
  `amount` decimal(7,1) NOT NULL,
  `quantity` int(11) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `t_order_id_index` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_order`
--

LOCK TABLES `t_order` WRITE;
/*!40000 ALTER TABLE `t_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_order_to_kami`
--

DROP TABLE IF EXISTS `t_order_to_kami`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_order_to_kami` (
  `order_id` uuid NOT NULL,
  `kami_id` uuid NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_order_to_kami`
--

LOCK TABLES `t_order_to_kami` WRITE;
/*!40000 ALTER TABLE `t_order_to_kami` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_order_to_kami` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_store`
--

DROP TABLE IF EXISTS `t_store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_store` (
  `id` uuid NOT NULL COMMENT '分类主键',
  `user_id` uuid NOT NULL COMMENT '分类主键',
  `name` varchar(20) NOT NULL COMMENT '店铺名称',
  `path` varchar(10) NOT NULL COMMENT '店铺地址',
  `closing` tinyint(1) NOT NULL DEFAULT 0,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_store_pk` (`user_id`),
  UNIQUE KEY `t_store_pk2` (`path`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='店铺';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_store`
--

LOCK TABLES `t_store` WRITE;
/*!40000 ALTER TABLE `t_store` DISABLE KEYS */;
INSERT INTO `t_store` VALUES
('5e0105b8-21b8-4b8a-9fb0-dfa106a54779','a9f3cc40-f6cc-4abc-bbed-2b6379546d5e','Enaium的店铺','enaium',0,'2023-03-29 18:01:25','2023-03-29 18:01:25');
/*!40000 ALTER TABLE `t_store` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_user`
--

DROP TABLE IF EXISTS `t_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_user` (
  `id` uuid NOT NULL COMMENT '分类主键',
  `username` varchar(18) NOT NULL COMMENT '用户名',
  `password` varchar(32) NOT NULL COMMENT '密码',
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_user_pk` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user`
--

LOCK TABLES `t_user` WRITE;
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
INSERT INTO `t_user` VALUES
('a9f3cc40-f6cc-4abc-bbed-2b6379546d5e','enaium','0e774a9797b1fe220c1e7cdc3736623f');
/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_user_info`
--

DROP TABLE IF EXISTS `t_user_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_user_info` (
  `id` uuid NOT NULL COMMENT '分类主键',
  `user_id` uuid NOT NULL COMMENT '分类主键',
  `avatar_url` varchar(50) DEFAULT NULL,
  `phone_number` bigint(11) DEFAULT NULL COMMENT '电话号码',
  `wechat_number` varchar(20) DEFAULT NULL COMMENT '微信号',
  `email` varchar(32) DEFAULT NULL COMMENT '邮箱',
  `contact_type` enum('tencent_qq','tencent_qq_group','wechat','phone','email','custom','none') NOT NULL DEFAULT 'none' COMMENT '联系方式',
  `contact` varchar(50) DEFAULT NULL COMMENT '联系',
  `name` varchar(10) DEFAULT NULL COMMENT '商家名称',
  `website` varchar(100) DEFAULT NULL COMMENT '店铺网站',
  `bulletin` varchar(500) DEFAULT NULL COMMENT '店铺公告',
  `pay_tip` varchar(500) DEFAULT NULL COMMENT '付款通知',
  `real_name` varchar(8) DEFAULT NULL COMMENT '真实名字',
  `alipay_number` varchar(32) DEFAULT NULL COMMENT '支付宝账号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_user_info_pk4` (`user_id`),
  UNIQUE KEY `t_user_info_pk` (`phone_number`),
  UNIQUE KEY `t_user_info_pk2` (`wechat_number`),
  UNIQUE KEY `t_user_info_pk3` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user_info`
--

LOCK TABLES `t_user_info` WRITE;
/*!40000 ALTER TABLE `t_user_info` DISABLE KEYS */;
INSERT INTO `t_user_info` VALUES
('454890d8-24d0-4ad4-89d2-1f04f8355382','a9f3cc40-f6cc-4abc-bbed-2b6379546d5e',NULL,NULL,'111111',NULL,'custom','自定义联系方式','Enaium','www.enaium.cn','店铺公告店铺公告店铺公告店铺公告','付款提示付款提示付款提示付款提示付款提示付款提示',NULL,NULL);
/*!40000 ALTER TABLE `t_user_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_user_wallet`
--

DROP TABLE IF EXISTS `t_user_wallet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_user_wallet` (
  `id` uuid NOT NULL COMMENT '分类主键',
  `user_id` uuid NOT NULL COMMENT '分类主键',
  `balance` decimal(11,2) NOT NULL DEFAULT 0.00 COMMENT '余额',
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_user_wallet_pk` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户钱包';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user_wallet`
--

LOCK TABLES `t_user_wallet` WRITE;
/*!40000 ALTER TABLE `t_user_wallet` DISABLE KEYS */;
INSERT INTO `t_user_wallet` VALUES
('fe0fb835-4fc8-49b0-af17-7ecf4b09a263','a9f3cc40-f6cc-4abc-bbed-2b6379546d5e',0.00);
/*!40000 ALTER TABLE `t_user_wallet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_wallet_record`
--

DROP TABLE IF EXISTS `t_wallet_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_wallet_record` (
  `id` uuid NOT NULL,
  `user_id` uuid NOT NULL,
  `amount` decimal(11,2) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_wallet_record`
--

LOCK TABLES `t_wallet_record` WRITE;
/*!40000 ALTER TABLE `t_wallet_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_wallet_record` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-04-03 19:34:09
