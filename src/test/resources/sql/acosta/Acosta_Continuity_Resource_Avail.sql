select * from continuity_associates_avail limit 200;

DROP TABLE continuity_associates_avail;

CREATE TABLE `continuity_associates_avail` (
  `ASSOCIATE_NAME` varchar(85),
  `EMPLOYEE_NO` varchar(10) NOT NULL,
  `EMPLOYEE_TYPE` varchar(15),
  `POSITION_HRS` tinyint NOT NULL DEFAULT 0,
  `TEAM` varchar(80),
  `CONTINUITY` boolean,
  `CONTINUITY_VM` boolean,
  `IMPACT_HOURS` tinyint(1) DEFAULT '0',
  `IMPACT_MON_SHIFT` varchar(15) DEFAULT NULL,
  `IMPACT_TUES_SHIFT` varchar(15) DEFAULT NULL,
  `IMPACT_Wed_SHIFT` varchar(15) DEFAULT NULL,
  `IMPACT_THURS_SHIFT` varchar(15) DEFAULT NULL,
  `IMPACT_FRI_SHIFT` varchar(15) DEFAULT NULL,
  `IMPACT_SAT_SHIFT` varchar(15) DEFAULT NULL,
  `IMPACT_SUN_SHIFT` varchar(15) DEFAULT NULL,
  `CONTY_MON_SHIFT` varchar(15) DEFAULT NULL,
  `CONTY_TUES_SHIFT` varchar(15) DEFAULT NULL,
  `CONTY_WED_SHIFT` varchar(15) DEFAULT NULL,
  `CONTY_THURS_SHIFT` varchar(15) DEFAULT NULL,
  `CONTY_FRI_SHIFT` varchar(15) DEFAULT NULL,
  `CONTY_SAT_SHIFT` varchar(15) DEFAULT NULL,
  `CONTY_SUN_SHIFT` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`EMPLOYEE_NO`),
  INDEX `Indx_WorkHr` (`POSITION_HRS`, `IMPACT_HOURS`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

desc continuity_associates_avail;

-- Load From CSV Export
--

LOAD DATA LOCAL INFILE '/Users/ssharma/Desktop/OneDrive/Documents/TOA/Acosta/Database/Continuity_User_Availability_ETL.csv'
INTO TABLE continuity_associates_avail
FIELDS TERMINATED BY ',' ENCLOSED BY '"' 
LINES TERMINATED BY '\n' 
IGNORE 1 LINES;

alter table `continuity_associates_avail` 
CHANGE `POSITION_HRS` `POSITION_HRS` tinyint(5) NOT NULL DEFAULT 0;

select * from continuity_associates_avail;
select CONTY_MON_SHIFT, count(*) from continuity_associates_avail group by CONTY_MON_SHIFT;
select CONTY_TUES_SHIFT, count(*) from continuity_associates_avail group by CONTY_TUES_SHIFT;
select CONTY_WED_SHIFT, count(*) from continuity_associates_avail group by CONTY_WED_SHIFT;
select CONTY_THURS_SHIFT, count(*) from continuity_associates_avail group by CONTY_THURS_SHIFT;
select CONTY_FRI_SHIFT, count(*) from continuity_associates_avail group by CONTY_FRI_SHIFT;
select CONTY_SAT_SHIFT, count(*) from continuity_associates_avail group by CONTY_SAT_SHIFT;
select CONTY_SUN_SHIFT, count(*) from continuity_associates_avail group by CONTY_SUN_SHIFT;


