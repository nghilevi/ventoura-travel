DROP TABLE IF EXISTS "CurrentUserFilterSettings";
CREATE TABLE "CurrentUserFilterSettings" ("id" INTEGER PRIMARY KEY  NOT NULL ,"userid" TEXT DEFAULT (null) ,"gender" TEXT DEFAULT ('both') ,"userType" TEXT DEFAULT ('both') ,"ageMin" INTEGER DEFAULT (18) ,"ageMax" INTEGER DEFAULT (55) ,"priceMin" INTEGER DEFAULT (0) ,"priceMax" INTEGER DEFAULT (200) ,"autoMatch" INTEGER DEFAULT (0) );
