DROP TABLE IF EXISTS "Booking";
CREATE TABLE "Booking" ("bookingId" INTEGER PRIMARY KEY  NOT NULL , "bookingStatus" INTEGER, "guideId" INTEGER, "statusLastUpdatedTime" DATETIME, "tourDate" DATETIME, "tourPrice" FLOAT, "tourType" INTEGER, "travellerId" INTEGER, "ownerId" INTEGER);
