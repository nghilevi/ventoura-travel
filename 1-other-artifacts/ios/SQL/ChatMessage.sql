DROP TABLE IF EXISTS "ChatMessage";
CREATE TABLE ChatMessage(messageId integer primary key, fromId text, toId text, ownerID text, ReceivedTime timestamp, messageContent text);
