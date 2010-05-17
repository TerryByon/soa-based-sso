CREATE TABLE SERVER (
       ID                   int NOT NULL identity,
       IP                   VARCHAR(20) NULL,
       SECURITY_KEY         LONGVARBINARY NULL
);


CREATE TABLE USER (
       ID                   INTEGER NOT NULL identity,
       USER_ID              VARCHAR(20) NOT NULL,
       USER_INFO            LONGVARCHAR NULL,
       VALID_TIME           VARCHAR(17) NULL,
       SERVER_ID            int NOT NULL
);


ALTER TABLE USER
       ADD   FOREIGN KEY (SERVER_ID)
                             REFERENCES SERVER
                             ON DELETE SET NULL  ;