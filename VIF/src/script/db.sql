
/* Data for the `user` table  (Records 1 - 1): admin - pass:123456 */
CREATE TABLE `USER` (
  `ID` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `BIRTHDAY` DATE DEFAULT NULL,
  `CREATED_BY` BIGINT(20) DEFAULT NULL,
  `CREATED_DATE` DATE DEFAULT NULL,
  `EMAIL` LONGTEXT,
  `FULL_NAME` VARCHAR(100)  COLLATE utf8_general_ci DEFAULT NULL,
  `LOCKED` BIT(1) DEFAULT NULL,
  `PASSWORD` VARCHAR(100)  COLLATE utf8_general_ci DEFAULT NULL,
  `ROLE` VARCHAR(255)  COLLATE utf8_general_ci DEFAULT NULL,
  `UPDATED_BY` BIGINT(20) DEFAULT NULL,
  `UPDATED_DATE` DATE DEFAULT NULL,
  `USER_NAME` VARCHAR(100)  COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID` (`ID`),
  UNIQUE KEY `USER_NAME` (`USER_NAME`)

)

INSERT INTO `USER` (`ID`, `BIRTHDAY`, `CREATED_BY`, `CREATED_DATE`, `EMAIL`, `FULL_NAME`, `LOCKED`, `PASSWORD`, `ROLE`, `UPDATED_BY`, `UPDATED_DATE`, `USER_NAME`) VALUES 
  (1, NULL, NULL, NULL, NULL, 'Admin', False, '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'ADMIN', NULL, NULL, 'vif');

 
CREATE TABLE `ORDER_ITEM` (
  `ID` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(100)  COLLATE utf8_general_ci DEFAULT NULL,
  `DESCCRIPTION` VARCHAR(200)  COLLATE utf8_general_ci DEFAULT NULL,
  `PRICE` BIGINT(20) DEFAULT NULL,
  `MINI_PRICE` BIGINT(20) DEFAULT NULL,
  `IMAGE` VARCHAR(250)  COLLATE utf8_general_ci DEFAULT NULL,
  `MO` Int(10) DEFAULT NULL,
  `TU` Int(10) DEFAULT NULL,
  `WE` Int(10) DEFAULT NULL,
  `TH` Int(10) DEFAULT NULL,
  `FR` Int(10) DEFAULT NULL,
  `SA` Int(10) DEFAULT NULL,
  `SU` Int(10) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID` (`ID`)

)

CREATE TABLE `ADDRESS_NOTE` (
  `ID` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `DISTRICT_ID` BIGINT(3)  DEFAULT NULL,
  `STREET` VARCHAR(50)  COLLATE utf8_general_ci DEFAULT NULL,
  `ADDRESS` VARCHAR(50)  COLLATE utf8_general_ci DEFAULT NULL,
  `OFFICE_NAME` VARCHAR(70)  COLLATE utf8_general_ci DEFAULT NULL,
  `OFFICE_LEVEL` VARCHAR(50)  COLLATE utf8_general_ci DEFAULT NULL,

  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID` (`ID`)
)