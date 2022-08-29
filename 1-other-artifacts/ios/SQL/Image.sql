DROP TABLE IF EXISTS "Image";
CREATE TABLE "Image" ("imageId" INTEGER PRIMARY KEY  NOT NULL ,"ventouraImageId" INTEGER,"ownerId" TEXT DEFAULT (null) ,"isUserGuide" BOOL,"isPortal" BOOL,"order" INTEGER,"userId" INTEGER);
