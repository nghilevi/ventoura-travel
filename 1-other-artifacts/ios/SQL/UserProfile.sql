DROP TABLE IF EXISTS "UserProfile";
CREATE TABLE UserProfile(ProfileId text primary key,ownerID text, name text, lastName text, firstName text, age text, gender text, textBiography text,country integer,city integer,dateOfBirth text,isGuide text, useravgReviewScoreRole text,tourLength text,tourPrice text,paymentMethod text, "lastUpdated" DATETIME, "tourType" TEXT);
