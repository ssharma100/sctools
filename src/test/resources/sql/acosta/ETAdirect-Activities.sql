create table eta_activities (
`folder` varchar(128) COMMENT 'Activity resides within this folder',
`a_id` integer NOT null COMMENT 'ETAdirect assigned activity id',
`activity_key` varchar(128) NOT null COMMENT 'Activity ID Assigned By Tool And Acosta',
`time_slot` varchar(15) COMMENT 'ETA-assigned time slot name',
`activity_type` varchar(15) COMMENT 'ETA-assigned activity type',
`store_name` varchar(255) COMMENT 'Acosta provided store name',

PRIMARY KEY (`activity_key`),
INDEX `KEY_EtaID` (`a_id`),
INDEX `KEY_Store` (`store_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

drop table eta_activities;

delete from eta_activities;

LOAD DATA LOCAL INFILE '/Users/ssharma/Desktop/SkyDrive/Documents/TOA/Acosta/Database/ETAdirect-Non-scheduled-Activities-Clean.csv'
INTO TABLE eta_activities
FIELDS TERMINATED BY ',' ENCLOSED BY '"' 
LINES TERMINATED BY '\n' 
IGNORE 1 LINES;
