use joldb;
CREATE TABLE `jol_customer` (
  `account` char(45) NOT NULL,
  `password` char(45) NOT NULL,
  `email` char(100) NOT NULL,
  `phone` char(45) NOT NULL,
  `name` char(45) NOT NULL,
  `city` char(20) NOT NULL,
  `district` char(20) NOT NULL,
  `address` char(150) NOT NULL,
  `status` tinyint(1) NOT NULL,
  `token` varchar(100) DEFAULT NULL,
  `token_expired` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`account`),
  UNIQUE KEY `account` (`account`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `token_UNIQUE` (`token`)
) ;

CREATE TABLE `jol_product` (
	`prodId` int(11) NOT NULL AUTO_INCREMENT,
	`name` char(50) NOT NULL,
	`descript` char(255),
	`category` char(20) NOT NULL,
	`series` char(20) NOT NULL,
	`price` int NOT NULL,
	`cost` int,
	`qty` int,
	`imgURL` char(150) NOT NULL,
	`sizeInfo` char(50) NOT NULL,
	`createDt` DATETIME NOT NULL,
	`updateDt` DATETIME NOT NULL,
	PRIMARY KEY (`prodId`)
);

CREATE TABLE `jol_order` (
  `orderNo` int(11) NOT NULL AUTO_INCREMENT,
  `orderTime` datetime NOT NULL,
  `account` char(45) NOT NULL,
  `totalAmt` int(11) NOT NULL,
  `status` char(20) NOT NULL,
  `email` char(50) DEFAULT NULL,
  `deliveryWay` char(20) NOT NULL,
  `deliveryNo` char(20) NOT NULL,
  `orderName` char(20) DEFAULT NULL,
  `orderPhone` char(20) DEFAULT NULL,
  `orderCity` char(10) DEFAULT NULL,
  `orderDistrict` char(10) DEFAULT NULL,
  `orderAddress` char(150) DEFAULT NULL,
  `sendName` char(20) DEFAULT NULL,
  `sendPhone` char(20) DEFAULT NULL,
  `sendCity` char(10) DEFAULT NULL,
  `sendDistrict` char(10) DEFAULT NULL,
  `sendAddress` char(150) DEFAULT NULL,
  `vehicle` char(20) DEFAULT NULL,  
  `payBy` char(15) DEFAULT NULL,  
  `vehicleType` char(15) DEFAULT NULL,  
  `updateDt` datetime NOT NULL,
  PRIMARY KEY (`orderNo`),
  UNIQUE KEY `orderNo` (`orderNo`)
);

CREATE TABLE `jol_order_detail` (
	`orderDetailNo` int NOT NULL AUTO_INCREMENT UNIQUE,
	`orderNo` int NOT NULL,
	`account` char(45) NOT NULL,
	`prodId` int NOT NULL,
	`qty` int NOT NULL,
	`price` int NOT NULL,
	`size` char(10) NOT NULL,
	`status` char(20) NOT NULL,
	`updateDt` DATETIME NOT NULL,
	PRIMARY KEY (`orderDetailNo`)
);

CREATE TABLE `jol_cart` (
	`cartId` int NOT NULL AUTO_INCREMENT,
	`account` char(45) NOT NULL,
	`prodId` int NOT NULL,
	`qty` int NOT NULL,
	`size` char(10) NOT NULL,
	`isCart` BOOLEAN NOT NULL,
	`updateDt` DATETIME NOT NULL,
	PRIMARY KEY (`cartId`)
);

CREATE TABLE `jol_setting` (
	`type` char(45) NOT NULL,
	`keyName` char(45) NOT NULL,
	`value` char(50) NOT NULL,
	`description` char(100) NOT NULL,
	`crUser` char(45) NOT NULL,
	`crDt` DATETIME NOT NULL,
	`upUser` char(45) NOT NULL,
	`upDt` DATETIME NOT NULL,
	PRIMARY KEY (`type`,`keyName`)
);

CREATE TABLE `jol_employee` (
	`empNo` int NOT NULL AUTO_INCREMENT UNIQUE,
	`account` char(45) NOT NULL UNIQUE,
	`name` char(45) NOT NULL,
	`auth` int NOT NULL,
	`crUser` char(45) NOT NULL,
	`crDt` DATETIME NOT NULL,
	`upUser` char(45) NOT NULL,
	`upDt` DATETIME NOT NULL,
	`phone` char(20) NOT NULL,
	`email` char(50) NOT NULL,
	`address` char(150) NOT NULL,
	`password` char(45) NOT NULL,
	PRIMARY KEY (`empNo`)
);

ALTER TABLE `jol_order` ADD CONSTRAINT `jol_order_fk0` FOREIGN KEY (`account`) REFERENCES `jol_customer`(`account`);

ALTER TABLE `jol_order_detail` ADD CONSTRAINT `jol_order_fk1` FOREIGN KEY (`prodId`) REFERENCES `jol_product`(`prodId`);

ALTER TABLE `jol_cart` ADD CONSTRAINT `jol_cart_fk0` FOREIGN KEY (`prodId`) REFERENCES `jol_product`(`prodId`);

