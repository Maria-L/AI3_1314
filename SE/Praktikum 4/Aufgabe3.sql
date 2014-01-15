DROP TABLE GAST CASCADE CONSTRAINTS PURGE;
DROP TABLE ZUSATZLEISTUNG CASCADE CONSTRAINTS PURGE;
DROP TABLE RESERVIERUNG CASCADE CONSTRAINTS PURGE;
DROP TABLE ZUSATZLEISTZUNG_RESERVIERUNG CASCADE CONSTRAINTS PURGE;

CREATE TABLE GAST (
gastID integer not null PRIMARY KEY,
gastName varchar(60),
emailName varchar(20),
emailServer varchar(20),
emailDomain varchar(20),
istStammKunde int);

CREATE TABLE RESERVIERUNG (
reservierungID integer not null PRIMARY KEY,
zimmerNr int,
fGastNr int );

CREATE TABLE ZUSATZLEISTUNG (
zusatzID integer not null PRIMARY KEY,
leistungsArt varchar(50) );

CREATE TABLE ZUSATZLEISTZUNG_RESERVIERUNG(
reservierungID integer not null PRIMARY KEY,
zusatzID int );


