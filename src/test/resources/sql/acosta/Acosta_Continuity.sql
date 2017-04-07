
select * from continuity_actual_call_details limit 100;

select * from all_call_types where CALLTYPE_CODE like 'imp%';

select * from continuity_actual_call_details as ICD WHERE
ICD.CALL_STATUS_DETAILS = 'Successful'
AND
ICD.Store NOT LIKE 'Wal%' limit 100;

select * from all_stores limit 100;

select * from all_stores limit 100;
select * from all_call_types where CALLTYPE_code in ('RSC') limit 100;
select * from all_call_types limit 100;

desc continuity_actual_call_details;
alter table continuity_actual_call_details
add Index `Idx_Store` (`StoreID`),
add Index `Idx_AccostNo` (`Acosta_No`);

-- Compare Continity vs. Frequency

select STORE.Frequency, count(*) from continuity_actual_call_details as CCD join all_stores as STORE on STORE.ACOSTA_NO = CCD.ACOSTA_NO and STORE.STOREID = CCD.STOREID 
where 
CCD.CALL_STATUS_DETAILS = 'Successful'
AND 
CCD.Store NOT LIKE 'Wal%'
group by FREQUENCY;

select CCD.* from continuity_actual_call_details as CCD join all_stores as STORE on STORE.ACOSTA_NO = CCD.ACOSTA_NO and STORE.STOREID = CCD.STOREID 
where 
CCD.CALL_STATUS_DETAILS = 'Successful'
AND 
CCD.Store NOT LIKE 'Wal%'
AND 
STORE.FREQUENCY='1 per mont'
limit 100; 

create table frequency (
`name` VARCHAR(10),
`cycle` integer NOT NULL COMMENT 'Cycle Indentifier',
`start_offset` integer COMMENT 'Number Of Days From Starting Date',
`end_offset` integer COMMENT 'Number Of Ddays From End Route',
PRIMARY KEY (`name`, `cycle`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

select * from frequency;
delete from frequency where name = '2 per quar' and cycle =2; 

-- Frequency View - 1 Month

create view continuity_activity_1PerMo as 
SELECT 
concat('Conty_', CCD.ACTUAL_CALLID) as ActivityKey,
DATE(CCD.CALL_STARTED_LOCAL) as OriginalStartDate, 
F.Cycle,
'2017-06-18' as StartDate,
'2017-07-19' as EndDate,
'bucket' as ExternalID, 
CCD.CALL_TYPE_CODE as ActivityType,
STORE.LATITUDE as Latitude, STORE.LONGITUDE as Longitude,
'all-day' as 'timeslot',
CCD.STARTED_BY_EMPLOYEE_NO as 'ReqResource',
STORE.acosta_no as 'StoreID',
STORE.address as 'Address', STORE.city as 'City', STORE.State as 'State', STORE.ZIPCODE as 'Zip',
ALLCALL.DEFAULTCALLDURATION as 'DefaultCallDuration',
CCD.EXECUTION_DURATION_MINUTES as 'PlannedDuration',
'08:00:00' as 'StartTime',
'17:00:00' as 'EndTime',
CCD.STORE as Store,
'1' as Resource_No,
'1|1|1|1|1|1|1' as DOW
FROM frequency as F
join all_stores as STORE on  STORE.FREQUENCY = F.name
join continuity_actual_call_details as CCD on F.name = STORE.frequency
JOIN all_call_types as ALLCALL on ALLCALL.CallType_Code = CCD.CALL_TYPE_CODE
where 
F.name = '1 per mont'
AND 
STORE.ACOSTA_NO = CCD.ACOSTA_NO and STORE.STOREID = CCD.STOREID
AND
CCD.CALL_STATUS_DETAILS = 'Successful'
AND 
CCD.Store NOT LIKE 'Wal%'; 

drop view continuity_activity_1PerMo;
select * from continuity_activity_1PerMo;

-- Frequency View - 1Per Quarter

create view continuity_activity_1PerQ as 
SELECT 
concat('Conty_', CCD.ACTUAL_CALLID) as ActivityKey,
DATE(CCD.CALL_STARTED_LOCAL) as OriginalStartDate, 
F.Cycle,
'2017-06-18' as StartDate,
'2017-07-19' as EndDate,
'bucket' as ExternalID, 
CCD.CALL_TYPE_CODE as ActivityType,
STORE.LATITUDE as Latitude, STORE.LONGITUDE as Longitude,
'all-day' as 'timeslot',
CCD.STARTED_BY_EMPLOYEE_NO as 'ReqResource',
STORE.acosta_no as 'StoreID',
STORE.address as 'Address', STORE.city as 'City', STORE.State as 'State', STORE.ZIPCODE as 'Zip',
ALLCALL.DEFAULTCALLDURATION as 'DefaultCallDuration',
CCD.EXECUTION_DURATION_MINUTES as 'PlannedDuration',
'08:00:00' as 'StartTime',
'17:00:00' as 'EndTime',
CCD.STORE as Store,
'1' as Resource_No,
'1|1|1|1|1|1|1' as DOW
FROM frequency as F
join all_stores as STORE on  STORE.FREQUENCY = F.name
join continuity_actual_call_details as CCD on F.name = STORE.frequency
JOIN all_call_types as ALLCALL on ALLCALL.CallType_Code = CCD.CALL_TYPE_CODE
where 
F.name = '1 per quar'
AND 
STORE.ACOSTA_NO = CCD.ACOSTA_NO and STORE.STOREID = CCD.STOREID
AND
CCD.CALL_STATUS_DETAILS = 'Successful'
AND 
CCD.Store NOT LIKE 'Wal%'; 

drop view continuity_activity_1PerQ;
select * from continuity_activity_1PerQ;

-- Frequency View - 2PerQuarter

create view continuity_activity_2PerQ as 
SELECT 
concat('Conty_', CCD.ACTUAL_CALLID) as ActivityKey,
DATE(CCD.CALL_STARTED_LOCAL) as OriginalStartDate, 
F.Cycle,
'2017-06-18' as StartDate,
'2017-07-19' as EndDate,
'bucket' as ExternalID, 
CCD.CALL_TYPE_CODE as ActivityType,
STORE.LATITUDE as Latitude, STORE.LONGITUDE as Longitude,
'all-day' as 'timeslot',
CCD.STARTED_BY_EMPLOYEE_NO as 'ReqResource',
STORE.acosta_no as 'StoreID',
STORE.address as 'Address', STORE.city as 'City', STORE.State as 'State', STORE.ZIPCODE as 'Zip',
ALLCALL.DEFAULTCALLDURATION as 'DefaultCallDuration',
CCD.EXECUTION_DURATION_MINUTES as 'PlannedDuration',
'08:00:00' as 'StartTime',
'17:00:00' as 'EndTime',
CCD.STORE as Store,
'1' as Resource_No,
'1|1|1|1|1|1|1' as DOW
FROM frequency as F
join all_stores as STORE on  STORE.FREQUENCY = F.name
join continuity_actual_call_details as CCD on F.name = STORE.frequency
JOIN all_call_types as ALLCALL on ALLCALL.CallType_Code = CCD.CALL_TYPE_CODE
where 
F.name = '2 per quar'
AND 
STORE.ACOSTA_NO = CCD.ACOSTA_NO and STORE.STOREID = CCD.STOREID
AND
CCD.CALL_STATUS_DETAILS = 'Successful'
AND 
CCD.Store NOT LIKE 'Wal%'; 

drop view continuity_activity_2PerQ;
select * from continuity_activity_2PerQ;

-- Frequency View - 2 Per Month
create view continuity_activity_2PerMo as 
SELECT 
concat('Conty_', CCD.ACTUAL_CALLID) as ActivityKey,
DATE(CCD.CALL_STARTED_LOCAL) as OriginalStartDate, 
F.Cycle,
DATE(DATE_ADD('2017-06-18', INTERVAL + F.start_offset DAY)) as StartDate,
DATE(DATE_ADD('2017-06-18', INTERVAL + F.end_offset DAY)) as EndDate,
'bucket' as ExternalID, 
CCD.CALL_TYPE_CODE as ActivityType,
STORE.LATITUDE as Latitude, STORE.LONGITUDE as Longitude,
'all-day' as 'timeslot',
CCD.STARTED_BY_EMPLOYEE_NO as 'ReqResource',
STORE.acosta_no as 'StoreID',
STORE.address as 'Address', STORE.city as 'City', STORE.State as 'State', STORE.ZIPCODE as 'Zip',
ALLCALL.DEFAULTCALLDURATION as 'DefaultCallDuration',
CCD.EXECUTION_DURATION_MINUTES as 'PlannedDuration',
'08:00:00' as 'StartTime',
'17:00:00' as 'EndTime',
CCD.STORE as Store,
'1' as Resource_No,
'1|1|1|1|1|1|1' as DOW
FROM frequency as F
join all_stores as STORE on  STORE.FREQUENCY = F.name
join continuity_actual_call_details as CCD on F.name = STORE.frequency
JOIN all_call_types as ALLCALL on ALLCALL.CallType_Code = CCD.CALL_TYPE_CODE
where 
F.name = '2 per mont'
AND 
STORE.ACOSTA_NO = CCD.ACOSTA_NO and STORE.STOREID = CCD.STOREID
AND
CCD.CALL_STATUS_DETAILS = 'Successful'
AND 
CCD.Store NOT LIKE 'Wal%'; 

drop view continuity_activity_2PerMo;
select * from continuity_activity_2PerMo order by originalstartdate, cycle;

-- Frequency View - None
create view continuity_activity_None as 
SELECT 
concat('Conty_', CCD.ACTUAL_CALLID) as ActivityKey,
DATE(CCD.CALL_STARTED_LOCAL) as OriginalStartDate, 
F.Cycle,
'2017-06-18' as StartDate,
'2017-07-19' as EndDate,
'bucket' as ExternalID, 
CCD.CALL_TYPE_CODE as ActivityType,
STORE.LATITUDE as Latitude, STORE.LONGITUDE as Longitude,
'all-day' as 'timeslot',
CCD.STARTED_BY_EMPLOYEE_NO as 'ReqResource',
STORE.acosta_no as 'StoreID',
STORE.address as 'Address', STORE.city as 'City', STORE.State as 'State', STORE.ZIPCODE as 'Zip',
ALLCALL.DEFAULTCALLDURATION as 'DefaultCallDuration',
CCD.EXECUTION_DURATION_MINUTES as 'PlannedDuration',
'08:00:00' as 'StartTime',
'17:00:00' as 'EndTime',
CCD.STORE as Store,
'1' as Resource_No,
'1|1|1|1|1|1|1' as DOW
FROM frequency as F
join all_stores as STORE on  STORE.FREQUENCY = F.name
join continuity_actual_call_details as CCD on F.name = STORE.frequency
JOIN all_call_types as ALLCALL on ALLCALL.CallType_Code = CCD.CALL_TYPE_CODE
where 
F.name = 'None'
AND 
STORE.ACOSTA_NO = CCD.ACOSTA_NO and STORE.STOREID = CCD.STOREID
AND
CCD.CALL_STATUS_DETAILS = 'Successful'
AND 
CCD.Store NOT LIKE 'Wal%'; 

drop view continuity_activity_None;
select count(*) from continuity_activity_None;
-- Loaders

select ActivityKey, ReqResource as ResourceId, activitytype, startdate, enddate, Latitude, Longitude, PlannedDuration as Duration, StartTime, EndTime, Store, city as City, state as State, zip as Zip, 'Eastern' as Timezone, TimeSlot as TimeSlot, Resource_No , DOW from continuity_activity_1PerMo;

select ActivityKey, ReqResource as ResourceId, activitytype, startdate, enddate, Latitude, Longitude, PlannedDuration as Duration, StartTime, EndTime, Store, city as City, state as State, zip as Zip, 'Eastern' as Timezone, TimeSlot as TimeSlot, Resource_No , DOW from continuity_activity_2PerMo;

select ActivityKey, ReqResource as ResourceId, activitytype, startdate, enddate, Latitude, Longitude, PlannedDuration as Duration, StartTime, EndTime, Store, city as City, state as State, zip as Zip, 'Eastern' as Timezone, TimeSlot as TimeSlot, Resource_No , DOW from continuity_activity_1PerQ;

select ActivityKey, ReqResource as ResourceId, activitytype, startdate, enddate, Latitude, Longitude, PlannedDuration as Duration, StartTime, EndTime, Store, city as City, state as State, zip as Zip, 'Eastern' as Timezone, TimeSlot as TimeSlot, Resource_No , DOW from continuity_activity_2PerQ;

select ActivityKey, ReqResource as ResourceId, activitytype, startdate, enddate, Latitude, Longitude, PlannedDuration as Duration, StartTime, EndTime, Store, city as City, state as State, zip as Zip, 'Eastern' as Timezone, TimeSlot as TimeSlot, Resource_No , DOW from continuity_activity_None;

select distinct CALL_TYPE_CODE from continuity_actual_call_details where CALL_STATUS_DETAILS='Successful' and Store NOT LIKE 'Wal%';

select * 
from continuity_activity_2PerMo order by activityKey and StartDate;