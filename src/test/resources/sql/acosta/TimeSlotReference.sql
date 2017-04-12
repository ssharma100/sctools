--
-- Acosta Time Slot Reference
--
create table time_slots (
`Start_Time` time not null Comment 'Start Time Of Timeslot',
`End_Time` time not null Comment 'End Time Of Timeslot',
`OFSC_Label` varchar(25) UNIQUE not null PRIMARY KEY Comment 'OFSC Label of TimeSlot',
INDEX `TS_TIMES` (`Start_Time`, `End_Time`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

drop table time_slots;

-- Upload Of Information To The Table.
--

LOAD DATA LOCAL INFILE '/Users/ssharma/Desktop/OneDrive/Documents/TOA/Acosta/Database/TimeSlotBreakDown.csv'
INTO TABLE time_slots
FIELDS TERMINATED BY ',' ENCLOSED BY '"' 
LINES TERMINATED BY '\n' 
IGNORE 1 LINES;


select * from time_slots order by Start_Time, End_time;