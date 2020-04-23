CREATE SEQUENCE my_sequence START 1;

CREATE TABLE characters(
   ID             integer       DEFAULT nextval('my_sequence'::regclass) NOT NULL UNIQUE,
   PERSON_ID      integer       NOT NULL UNIQUE,
   PERSON_NAME    varchar(50)   NOT NULL,
   CREATED_AT     TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE characterInfo(
   ID                   integer       DEFAULT nextval('my_sequence'::regclass) NOT NULL UNIQUE,
   CHARACTER_INFO_ID    integer       NOT NULL UNIQUE,
   PERSON_ID            integer       REFERENCES characters(PERSON_ID)
   SPECIES              varchar(50)   NOT NULL,
   BIRTH_YEAR           varchar(50)   NOT NULL,
   CREATED_AT           TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE specifiesInfo(
   ID             integer       DEFAULT nextval('my_sequence'::regclass) NOT NULL UNIQUE,
   SPECIES_ID     integer       NOT NULL UNIQUE,
   PERSON_ID      integer       REFERENCES characters(PERSON_ID)
   SPECIES        varchar(50)   NOT NULL,
   LIFESPAN       integer       NOT NULL,
   AVG_HEIGHT     float(3)      NOT NULL,
   CREATED_AT     TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL
);