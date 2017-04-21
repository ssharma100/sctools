-- Information For Team And Frequencies
-- continuity_store_teams

CREATE TABLE associates_teams (
`STORE` varchar(128) NOT NULL,
`STORE_ID` varchar(15) DEFAULT 'None',
`ACOSTA_NO` varchar(15) NOT NULL,
`TEAM` varchar(80),
`FREQUENCY` varchar(25),
`DURATION` int(5),
`CONTINUITY_WM` boolean,

PRIMARY KEY (`STORE`, `ACOSTA_NO`, `TEAM`, `FREQUENCY`, `DURATION`),
INDEX `team_a_no` (`ACOSTA_NO`),
INDEX `team_store` (`STORE`),
INDEX `team_st_id` (`STORE_ID`)

) ENGINE=InnoDB DEFAULT CHARSET=latin1;

drop table associates_teams;

LOAD DATA LOCAL INFILE '/Users/ssharma/Desktop/OneDrive/Documents/TOA/Acosta/Generated/Continuity_Store_Teams_ETL_Clean.csv'
INTO TABLE associates_teams
FIELDS TERMINATED BY ',' ENCLOSED BY '"' 
LINES TERMINATED BY '\n' 
IGNORE 1 LINES;

select count(*) from associates_teams;
select * from associates_teams;
