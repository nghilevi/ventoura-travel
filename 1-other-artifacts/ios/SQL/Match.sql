DROP TABLE IF EXISTS "Match";
CREATE TABLE "Match" ("matchId" text PRIMARY KEY  NOT NULL  DEFAULT (null) ,"ownerId" text,"matchUserId" text,"unreadCount" INTEGER DEFAULT (0) ,"isNewMatch" BOOL,"matchDate" DATETIME DEFAULT (CURRENT_TIMESTAMP) ,"matchUserName" text);
