DROP TABLE IF EXISTS "City";
CREATE TABLE `City` (
	`id`	INTEGER NOT NULL,
	`cityName`	TEXT NOT NULL DEFAULT 'N/A',
	`countryId`	INTEGER,
	`countryName`	TEXT,
	PRIMARY KEY(id)
);
INSERT INTO "City" VALUES(1,'London',226,'United Kingdom');
INSERT INTO "City" VALUES(2,'Barcelona',200,'Spain');
INSERT INTO "City" VALUES(3,'Paris',74,'France');
INSERT INTO "City" VALUES(4,'Berlin',81,'Germany');
INSERT INTO "City" VALUES(5,'Rome',106,'Italy');
INSERT INTO "City" VALUES(6,'Madrid',200,'Spain');
INSERT INTO "City" VALUES(7,'Amsterdam',151,'Netherlands');
INSERT INTO "City" VALUES(8,'Munich',81,'Germany');
INSERT INTO "City" VALUES(9,'Venice',106,'Italy');
INSERT INTO "City" VALUES(10,'Istanbul',219,'Turkey');
INSERT INTO "City" VALUES(11,'Moscow',178,'Russia');
INSERT INTO "City" VALUES(12,'Prague',58,'Czech Republic');
INSERT INTO "City" VALUES(13,'Dublin',104,'Ireland');
INSERT INTO "City" VALUES(14,'Edinburgh',226,'United Kingdom');
INSERT INTO "City" VALUES(15,'Lisbon',173,'Portugal');
INSERT INTO "City" VALUES(16,'Seville',74,'France');
INSERT INTO "City" VALUES(17,'Nice',74,'France');
INSERT INTO "City" VALUES(18,'Brussels',22,'Belgium');
INSERT INTO "City" VALUES(19,'Frankfurt',81,'Germany');
INSERT INTO "City" VALUES(20,'Hamburg',81,'Germany');
INSERT INTO "City" VALUES(21,'Zurich',207,'Switzerland');
INSERT INTO "City" VALUES(22,'Vienna',15,'Austria');
INSERT INTO "City" VALUES(23,'Athens',64,'Greece');
INSERT INTO "City" VALUES(24,'Florence',106,'Italy');
INSERT INTO "City" VALUES(25,'Milan',106,'Italy');
INSERT INTO "City" VALUES(26,'Split',55,'Croatia');
INSERT INTO "City" VALUES(27,'Dubrovnik',55,'Croatia');
INSERT INTO "City" VALUES(28,'Warsaw',172,'Poland');
INSERT INTO "City" VALUES(29,'Copenhagen',59,'Denmark');
INSERT INTO "City" VALUES(30,'Helsinki',73,'Finland');
INSERT INTO "City" VALUES(31,'Stockholm',206,'Sweden');
INSERT INTO "City" VALUES(32,'Oslo',161,'Norway');
INSERT INTO "City" VALUES(33,'Talinn',68,'Estonia');
INSERT INTO "City" VALUES(34,'Riga',118,'Latvia');
INSERT INTO "City" VALUES(35,'Budapest',98,'Hungary');
INSERT INTO "City" VALUES(36,'Bucharest',177,'Romania');
INSERT INTO "City" VALUES(37,'Monaco',142,'Monaco');
INSERT INTO "City" VALUES(38,'St Petersburg',138,'Russia');
