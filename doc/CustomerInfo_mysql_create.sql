use joldb;
SHOW VARIABLES LIKE 'lower_case_table_names';
CREATE TABLE `jol_customer` (
  `account` char(45) NOT NULL,
  `password` char(45) NOT NULL,
  `email` char(100) NOT NULL,
  `phone` char(45) NOT NULL,
  `name` char(45) NOT NULL,
  `address` char(150) NOT NULL,
  `status` tinyint(1) NOT NULL,
  `payment` char(45) NOT NULL,
  `token` varchar(100) DEFAULT NULL,
  `token_expired` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`account`),
  UNIQUE KEY `account` (`account`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `token_UNIQUE` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `jol_product` (
	`prodId` int(11) NOT NULL AUTO_INCREMENT,
	`name` char(50) NOT NULL,
	`descript` char(100),
	`img` blob NOT NULL,
	`createDt` DATETIME NOT NULL,
	`updateDt` DATETIME NOT NULL,
	`price` int NOT NULL,
	`cost` int,
	`qty` int,
	`category` char(20) NOT NULL,
	`size` char(50) NOT NULL,
	PRIMARY KEY (`prodId`)
);

CREATE TABLE `jol_order` (
	`orderNo` int NOT NULL AUTO_INCREMENT UNIQUE,
	`account` char(45) NOT NULL UNIQUE,
	`prodId` int NOT NULL,
	`qty` int NOT NULL,
	`price` int NOT NULL,
	`orderTime` DATETIME NOT NULL,
	`shipNo` char(20) NOT NULL,
	`status` char(20) NOT NULL,
	`updateDt` DATETIME NOT NULL,
	PRIMARY KEY (`orderNo`)
);

CREATE TABLE `jol_cart` (
	`cartId` int NOT NULL AUTO_INCREMENT,
	`prodId` int NOT NULL,
	`account` char(45) NOT NULL,
	`qty` int NOT NULL,
	`size` char(10) NOT NULL,
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

ALTER TABLE `jol_order` ADD CONSTRAINT `jol_order_fk1` FOREIGN KEY (`prodId`) REFERENCES `jol_product`(`prodId`);

ALTER TABLE `jol_cart` ADD CONSTRAINT `jol_cart_fk0` FOREIGN KEY (`prodId`) REFERENCES `jol_product`(`prodId`);

