CREATE TABLE sources (
  idsource  INT         NOT NULL,
  name      VARCHAR(45) NOT NULL,
  version   VARCHAR(12) DEFAULT NULL,
  wnversion VARCHAR(12) DEFAULT NULL,
  url       TEXT        DEFAULT NULL,
  provider  VARCHAR(45) DEFAULT NULL,
  reference TEXT        DEFAULT NULL,
  PRIMARY KEY (idsource)
);
