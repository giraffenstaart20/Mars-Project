drop table if exists users;

create table users
(
    id       int auto_increment,
    firstName varchar(255),
    lastName varchar(255),
    homeAddress varchar(255),
    premium  varchar(255)
);

drop table if exists domes;

create table domes
(
    id       int auto_increment,
    domename varchar(255),
    latitude double,
    longitude double,
    surface double
);

drop table if exists companies;

create table companies
(
    id       int auto_increment,
    name varchar(255),
    section varchar(255),
    ad_effectiveness double,
    user_id int
);

drop table if exists oxygen_leaks;

create table oxygen_leaks
(
    id      int auto_increment,
    danger_level varchar(10),
    dome_id int,
    date date,
    latitude double,
    longitude double
);

drop table if exists appointments;

create table appointments
(
    id      int auto_increment,
    date date,
    time time,
    topic varchar(255),
    employee_name varchar(255),
    expertise varchar(255)
);

drop table if exists population;

create table population
(
    id      int auto_increment,
    size int,
    dome_id int,
    date date,
    latitude double,
    longitude double,
    colony varchar(255)
);

drop table if exists medical_dispatches;

create table medical_dispatches
(
    id      int auto_increment,
    date date,
    dome_id int,
    latitude double,
    longitude double,
    dispatch_type varchar(255)
);

drop table if exists meteor_showers;

create table meteor_showers
(
    id      int auto_increment,
    date date,
    dome_id int,
    longitude  double,
    latitude double,
    damage_type varchar(255)
);

drop table if exists dust_storms;

create table dust_storms
(
    id      int auto_increment,
    date date,
    dome_id int,
    longitude  double,
    latitude double,
    damage_type varchar(255)
);