drop table guideboard;

CREATE TABLE guideboard (
    id INTEGER NOT NULL AUTO_INCREMENT,
	userID INTEGER NOT NULL,
    title VARCHAR(255),
	description VARCHAR(255),
	createdAt datetime,
	updatedAt datetime,
	deletedAt datetime default NULL,
    PRIMARY KEY (id)
) ;

drop table site;

CREATE TABLE site (
    id INTEGER NOT NULL AUTO_INCREMENT,
	userID INTEGER NOT NULL,
	type INTEGER NOT NULL,
	url  VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
	description text,
	html text,
	createdAt datetime,
	updatedAt datetime,
	deletedAt datetime default NULL,
    PRIMARY KEY (id)
) ;

drop table guideboard_site;

CREATE TABLE guideboard_site (
    id INTEGER NOT NULL AUTO_INCREMENT,
	guideboardID INTEGER NOT NULL,
    siteID INTEGER NOT NULL,
	createdAt datetime,
	updatedAt datetime,
	deletedAt datetime default NULL,
    PRIMARY KEY (id),
	CONSTRAINT fk_guideboard FOREIGN KEY (guideboardID) REFERENCES guideboard (id),
    CONSTRAINT fk_site FOREIGN KEY (siteID) REFERENCES site (id)
) ;

drop table guideboard_star;

CREATE TABLE guideboard_star (
    id INTEGER NOT NULL AUTO_INCREMENT,
	guideboardID INTEGER NOT NULL,
	userID INTEGER NOT NULL,
	starCount INTEGER NOT NULL,
	createdAt datetime,
	updatedAt datetime,
	deletedAt datetime default NULL,
    PRIMARY KEY (id),
	CONSTRAINT fk_guideboard FOREIGN KEY (guideboardID) REFERENCES guideboard (id),
    CONSTRAINT fk_user FOREIGN KEY (userID) REFERENCES user (id)
) ;

drop table user;

CREATE TABLE user (
    id INTEGER NOT NULL AUTO_INCREMENT,
	officeId VARCHAR(64) ,
	createdAt datetime,
	updatedAt datetime,
	deletedAt datetime default NULL,
    PRIMARY KEY (id)
) ;

drop table comment;

CREATE TABLE comment (
    id INTEGER NOT NULL AUTO_INCREMENT,
	userID INTEGER NOT NULL,
	guideboardID INTEGER NOT NULL,
    comment text,
	createdAt datetime,
	updatedAt datetime,
	deletedAt datetime default NULL,
    PRIMARY KEY (id)
) ;


drop table user_site;

CREATE TABLE user_site (
    id INTEGER NOT NULL AUTO_INCREMENT,
	userID INTEGER NOT NULL,
	siteID INTEGER NOT NULL,
	comment text,
	createdAt datetime,
	updatedAt datetime,
	deletedAt datetime  default NULL,
    PRIMARY KEY (id)
) ;