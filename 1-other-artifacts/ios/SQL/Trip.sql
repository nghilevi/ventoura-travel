DROP TABLE IF EXISTS "Trip";
CREATE TABLE "Trip" ("tripId" INTEGER PRIMARY KEY  NOT NULL , "city" INTEGER, "country" INTEGER, "endTime" DATETIME, "startTime" DATETIME, "ownerId" INTEGER);
