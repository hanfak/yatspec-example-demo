CREATE SCHEMA IF NOT EXISTS records;

CREATE SEQUENCE characters_sequence START 1;
CREATE SEQUENCE characterInfo_sequence START 1;
CREATE SEQUENCE specifiesInfo_sequence START 1;

CREATE TABLE records.characters(
   ID             integer       DEFAULT nextval('characters_sequence'::regclass) NOT NULL UNIQUE,
   PERSON_ID      integer       NOT NULL UNIQUE PRIMARY KEY,
   PERSON_NAME    varchar(50)   NOT NULL,
   CREATED_AT     TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL
);

INSERT INTO records.characters (PERSON_ID, PERSON_NAME) VALUES ('1', 'Luke');
INSERT INTO records.characters (PERSON_ID, PERSON_NAME) VALUES ('1', 'Luke');
INSERT INTO records.characters (PERSON_ID, PERSON_NAME) VALUES ('1', 'Luke');
INSERT INTO records.characters (PERSON_ID, PERSON_NAME) VALUES ('1', 'Luke');

CREATE TABLE records.characterInfo(
   ID                   integer       DEFAULT nextval('characterInfo_sequence'::regclass) NOT NULL UNIQUE,
   CHARACTER_INFO_ID    integer       NOT NULL UNIQUE PRIMARY KEY,
   PERSON_ID            integer       REFERENCES records.characters(PERSON_ID),
   BIRTH_YEAR           varchar(50)   NOT NULL,
   CREATED_AT           TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE records.specifiesInfo(
   ID             integer       DEFAULT nextval('specifiesInfo_sequence'::regclass) NOT NULL UNIQUE,
   SPECIES_ID     integer       NOT NULL UNIQUE PRIMARY KEY,
   PERSON_ID      integer       REFERENCES records.characters(PERSON_ID),
   SPECIES        varchar(50)   NOT NULL,
   LIFESPAN       integer       NOT NULL,
   AVG_HEIGHT     float(3)      NOT NULL,
   CREATED_AT     TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL
);