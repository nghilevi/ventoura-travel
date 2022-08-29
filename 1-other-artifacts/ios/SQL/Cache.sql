DROP TABLE IF EXISTS "Cache";
CREATE TABLE "Cache" ("cacheId" INTEGER PRIMARY KEY  NOT NULL , "bookingLastUpdated" DATETIME, "galleryLastUpdated" DATETIME, "matchProfileLastUpdated" DATETIME, "tourLastUpdated" DATETIME, "ownerId" INTEGER, "vSettingLastUpdated" DATETIME, "reviewLastupdated" DATETIME);
