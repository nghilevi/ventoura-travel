Drop TABLE 'City';
CREATE TABLE `City` (
	`id`	INTEGER NOT NULL,
	`cityName`	TEXT NOT NULL DEFAULT 'N/A',
	`countryId`	INTEGER,
	`countryName`	TEXT,
	PRIMARY KEY(id)
);
INSERT INTO `City` VALUES(1,'London',179,'United Kingdom');
INSERT INTO `City` VALUES(2,'Barcelona',158,'Spain');
INSERT INTO `City` VALUES(3,'Paris',58,'France');
INSERT INTO `City` VALUES(4,'Berlin',62,'Germany');
INSERT INTO `City` VALUES(5,'Roma',79,'Italy');
INSERT INTO `City` VALUES(6,'Madrid',158,'Spain');
INSERT INTO `City` VALUES(7,'Amsterdam',118,'Netherlands');
INSERT INTO `City` VALUES(8,'Munich',62,'Germany');
INSERT INTO `City` VALUES(9,'Venice',79,'Italy');
INSERT INTO `City` VALUES(10,'Istanbul',173,'Turkey');
INSERT INTO `City` VALUES(11,'Moscow',138,'Russia');
INSERT INTO `City` VALUES(12,'Prague',43,'Czech Republic');
INSERT INTO `City` VALUES(13,'Dublin',77,'Ireland');
INSERT INTO `City` VALUES(14,'Edinburgh',179,'United Kingdom');
INSERT INTO `City` VALUES(15,'Lisbon',134,'Portugal');
INSERT INTO `City` VALUES(16,'Seville',58,'France');
INSERT INTO `City` VALUES(17,'Nice',58,'France');
INSERT INTO `City` VALUES(18,'Brussels',17,'Belgium');
INSERT INTO `City` VALUES(19,'Frankfurt',62,'Germany');
INSERT INTO `City` VALUES(20,'Hamburg',62,'Germany');
INSERT INTO `City` VALUES(21,'Zurich',164,'Switzerland');
INSERT INTO `City` VALUES(22,'Vienna',10,'Austria');
INSERT INTO `City` VALUES(23,'Athens',64,'Greece');
INSERT INTO `City` VALUES(24,'Florence',79,'Italy');
INSERT INTO `City` VALUES(25,'Milan',79,'Italy');
INSERT INTO `City` VALUES(26,'Split',40,'Croatia');
INSERT INTO `City` VALUES(27,'Dubrovnik',40,'Croatia');
INSERT INTO `City` VALUES(28,'Warsaw',133,'Poland');
INSERT INTO `City` VALUES(29,'Copenhagen',45,'Denmark');
INSERT INTO `City` VALUES(30,'Helsinki',57,'Finland');
INSERT INTO `City` VALUES(31,'Stockholm',163,'Sweden');
INSERT INTO `City` VALUES(32,'Oslo',124,'Norway');
INSERT INTO `City` VALUES(33,'Talinn',54,'Estonia');
INSERT INTO `City` VALUES(34,'Riga',90,'Latvia');
INSERT INTO `City` VALUES(35,'Budapest',72,'Hungary');
INSERT INTO `City` VALUES(36,'Bucharest',137,'Romania');
INSERT INTO `City` VALUES(37,'Monaco',191,'Monaco');
INSERT INTO `City` VALUES(38,'St Petersburg',138,'Russia');
