create schema TC;

CREATE TABLE TC.TRADES(
    TRADE_ID VARCHAR(75) PRIMARY KEY,
    COUNTERPARTY_ID varchar(75),
    BOOK_ID VARCHAR(75),
    MATURITY_DATE DATE,
    CREATED_DATE DATE,
    EXPIRED char(1),
    VERSION INT
);

CREATE TRIGGER versionchkTrigger
  BEFORE UPDATE
  ON TC.TRADES
  FOR EACH ROW
  CALL "com.mycompany.tradecapture.dal.VersionCheckTrigger";

CREATE TRIGGER maturityDtchkTrigger
  BEFORE INSERT
  ON TC.TRADES
  FOR EACH ROW
  CALL "com.mycompany.tradecapture.dal.MaturityDateCheckTrigger";




