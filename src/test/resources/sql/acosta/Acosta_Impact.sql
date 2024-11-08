
desc impact_actual_call_details;
show create table impact_actual_call_details;
alter table impact_actual_call_details
change `DATE_PLANNED` `DATE_PLANNED` date,
change `TIME_PLANNED_LOCAL` `TIME_PLANNED_LOCAL` time,
change `CALL_TYPE` `CALL_TYPE` varchar(50),
change `CALL_STATUS` `CALL_STATUS` varchar(70),
change `CALL_STATUS_DETAILS` `CALL_STATUS_DETAILS` varchar(75),
change `CALL_STARTED_LOCAL` `CALL_STARTED_LOCAL` datetime,
change `SERVICE_ORDER_NAME` `SERVICE_ORDER_NAME` varchar(128),
change `CALLID` `CALLID` VARCHAR(20),
change `STATUS` `STATUS` varchar(20),
change `LUNCH_UNPAID_TIME_MINUTES` `LUNCH_UNPAID_TIME_MINUTES` int,
change `PAYABLE_DRIVE_TIME_MINUTES` `PAYABLE_DRIVE_TIME_MINUTES` int,
change `ADMIN_TIME_MINUTES` `ADMIN_TIME_MINUTES` int;

ALter table impact_actual_call_details
drop `PLANNED_FOR`;
ALter table impact_actual_call_details
drop `PLANNED_FOR_THIRDPARTYACCOUNT`;
ALter table impact_actual_call_details
drop `STARTED_BY`;
ALter table impact_actual_call_details
drop `STARTED_BY_THIRDPARTYACCOUNT`;
ALter table impact_actual_call_details
drop `COMPLETED_BY`;
ALter table impact_actual_call_details
drop `COMPLETED_BY_THIRDPARTYACCOUNT`;

ALter table impact_actual_call_details
drop `AREA_MANAGER_NAME`,
drop `MARKET_MANAGER_NAME`,
drop `RETAIL_DIRECTOR_NAME`;

create index `icd_date` on impact_actual_call_details (DATE_PLANNED);
create index `icd_store` on impact_actual_call_details (STORE);
create index `icd_Acosta_no` on impact_actual_call_details (ACOSTA_NO);
create index `icd_visit` on impact_actual_call_details (VISIT_ID);
create index `icd_Serv_no` on impact_actual_call_details (SERVICE_ORDER_NUMBER);
create index `icd_resource_no` on impact_actual_call_details (PLANNED_FOR_EMPLOYEE_NO, RESOURCE_NUMBER);

select * from c;

delete from impact_actual_call_details;

LOAD DATA LOCAL INFILE '/Users/ssharma/Desktop/OneDrive/Documents/TOA/Acosta/Database/IMPACTActualCallDetails-EditedUnix.csv'
INTO TABLE impact_actual_call_details
FIELDS TERMINATED BY ',' ENCLOSED BY '"' 
LINES TERMINATED BY '\n' 
IGNORE 1 LINES;


-- --------
-- Views --
-- --------
-- 24 Weeks Ahead
create view impact_activity_24 as 
SELECT 
concat('ImpA_', ICD.ACTUAL_CALLID) as ActivityKey,
DATE(ICD.CALL_STARTED_LOCAL) as OriginalStartDate, 
DATE(date_add(ICD.DATE_PLANNED, INTERVAL +24 WEEK)) as StartDate,
DATE(DATE_ADD(date_add(ICD.DATE_PLANNED, INTERVAL +24 WEEK), INTERVAL ICD.DURATION_PLANNED_MINUTES MINUTE)) as EndDate,
'bucket' as ExternalID, concat('imp', ICD.DURATION_PLANNED_MINUTES) as ActivityType,
STORE.LATITUDE as Latitude, STORE.LONGITUDE as Longitude,
TS.OFSC_Label as 'timeslot',
ICD.STARTED_BY_EMPLOYEE_NO as 'ReqResource',
STORE.acosta_no as 'StoreID',
STORE.address as 'Address', STORE.city as 'City', STORE.State as 'State', STORE.ZIPCODE as 'Zip',
ALLCALL.DEFAULTCALLDURATION as 'DefaultCallDuration', ICD.DURATION_PLANNED_MINUTES as 'PlannedDuration',
TIME(ICR.EARLIEST_VISIT_TIME) as 'StartTime', TIME(ICR.LATEST_VISIT_TIME) as 'EndTime',
ICD.STORE as Store,
ICR.RESOURCE_NUMBER as Resource_No,
CONCAT_WS('|', ICR.MONDAY, ICR.TUESDAY, ICR.WEDNESDAY, ICR.THURSDAY, ICR.FRIDAY, ICR.Saturday, ICR.Sunday) as DOW
FROM 
impact_actual_call_details as ICD
JOIN all_stores as STORE on STORE.acosta_no = ICD.acosta_no and STORE.STOREID = ICD.STOREID
JOIN all_call_types as ALLCALL on ALLCALL.CallType_Code = concat('imp', ICD.DURATION_PLANNED_MINUTES)
JOIN impact_call_requirements as ICR on 
ICR.SERVICE_ORDER_NUMBER = ICD.SERVICE_ORDER_NUMBER and ICR.ACOSTA_NO = ICD.ACOSTA_NO AND ICR.Visit_id = ICD.Visit_id
JOIN time_slots as TS on (TS.Start_Time = TIME(ICR.EARLIEST_VISIT_TIME) AND TS.End_Time = TIME(ICR.LATEST_VISIT_TIME))
WHERE
ICD.CALL_STATUS_DETAILS = 'Successful'
AND 
ICD.Store NOT LIKE 'Wal%'
AND ICD.RESOURCE_NUMBER=1;

drop view impact_activity_24;

select count(*) from impact_activity_24;
select * from impact_activity_24 limit 100;
select * from impact_activity_24 order by StartDate asc limit 10;
select * from impact_activity_24 where resource_No=2 limit 100;
select * from impact_actual_call_details limit 100;
select * from impact_call_requirements limit 100;

--
-- View For Calls Into Aug Time Frame
--
create view impact_activity_aug as 
SELECT 
concat('ImpB_', ICD.ACTUAL_CALLID) as ActivityKey,
DATE(ICD.CALL_STARTED_LOCAL) as OriginalStartDate, 
DATE(date_add(ICD.DATE_PLANNED, INTERVAL + 210 DAY)) as StartDate,
DATE(DATE_ADD(date_add(ICD.DATE_PLANNED, INTERVAL + 210 DAY), INTERVAL ICD.DURATION_PLANNED_MINUTES MINUTE)) as EndDate,
'bucket' as ExternalID,
concat('imp', ICD.DURATION_PLANNED_MINUTES) as ActivityType,
STORE.LATITUDE as Latitude, 
STORE.LONGITUDE as Longitude,
TS.OFSC_Label as 'timeslot',
ICD.STARTED_BY_EMPLOYEE_NO as 'ReqResource',
STORE.acosta_no as 'StoreID',
STORE.address as 'Address', STORE.city as 'City', 
STORE.State as 'State', 
STORE.ZIPCODE as 'Zip',
ICD.EXECUTION_DURATION_MINUTES as 'CallDuration', 
ICD.DURATION_PLANNED_MINUTES as 'PlannedDuration',
TIME(ICR.EARLIEST_VISIT_TIME) as 'StartTime', 
TIME(ICR.LATEST_VISIT_TIME) as 'EndTime',
ICD.STORE as Store,
ICR.RESOURCE_NUMBER as Resource_No,
CONCAT_WS('|', ICR.MONDAY, ICR.TUESDAY, ICR.WEDNESDAY, ICR.THURSDAY, ICR.FRIDAY, ICR.Saturday, ICR.Sunday) as DOW
FROM 
impact_actual_call_details as ICD
JOIN all_stores as STORE on STORE.acosta_no = ICD.acosta_no and STORE.STOREID = ICD.STOREID
JOIN all_call_types as ALLCALL on ALLCALL.CallType_Code = concat('imp', ICD.DURATION_PLANNED_MINUTES)
JOIN impact_call_requirements as ICR on 
ICR.SERVICE_ORDER_NUMBER = ICD.SERVICE_ORDER_NUMBER and ICR.ACOSTA_NO = ICD.ACOSTA_NO AND ICR.Visit_id = ICD.Visit_id
JOIN time_slots as TS on (TS.Start_Time = TIME(ICR.EARLIEST_VISIT_TIME) AND TS.End_Time = TIME(ICR.LATEST_VISIT_TIME))
WHERE
ICD.CALL_STATUS_DETAILS = 'Successful'
AND ICD.RESOURCE_NUMBER=1;

drop view impact_activity_aug;

--
-- View For Calls Into Aug Time Frame
--
create view impact_activity_jan17 as 
SELECT 
concat('ImpC_', ICD.ACTUAL_CALLID) as ActivityKey,
DATE(ICD.CALL_STARTED_LOCAL) as OriginalStartDate, 
DATE(date_add(ICD.DATE_PLANNED, INTERVAL + 364 DAY)) as StartDate,
DATE(DATE_ADD(date_add(ICD.DATE_PLANNED, INTERVAL + 364 DAY), INTERVAL ICD.DURATION_PLANNED_MINUTES MINUTE)) as EndDate,
'bucket' as ExternalID,
concat('imp', ICD.DURATION_PLANNED_MINUTES) as ActivityType,
STORE.LATITUDE as Latitude, 
STORE.LONGITUDE as Longitude,
TS.OFSC_Label as 'timeslot',
ICD.STARTED_BY_EMPLOYEE_NO as 'ReqResource',
STORE.acosta_no as 'StoreID',
STORE.address as 'Address', STORE.city as 'City', 
STORE.State as 'State', 
STORE.ZIPCODE as 'Zip',
ICD.EXECUTION_DURATION_MINUTES as 'CallDuration', 
ICD.DURATION_PLANNED_MINUTES as 'PlannedDuration',
TIME(ICR.EARLIEST_VISIT_TIME) as 'StartTime', 
TIME(ICR.LATEST_VISIT_TIME) as 'EndTime',
ICD.STORE as Store,
ICR.RESOURCE_NUMBER as Resource_No,
CONCAT_WS('|', ICR.MONDAY, ICR.TUESDAY, ICR.WEDNESDAY, ICR.THURSDAY, ICR.FRIDAY, ICR.Saturday, ICR.Sunday) as DOW
FROM 
impact_actual_call_details as ICD
JOIN all_stores as STORE on STORE.acosta_no = ICD.acosta_no and STORE.STOREID = ICD.STOREID
JOIN all_call_types as ALLCALL on ALLCALL.CallType_Code = concat('imp', ICD.DURATION_PLANNED_MINUTES)
JOIN impact_call_requirements as ICR on 
ICR.SERVICE_ORDER_NUMBER = ICD.SERVICE_ORDER_NUMBER and ICR.ACOSTA_NO = ICD.ACOSTA_NO AND ICR.Visit_id = ICD.Visit_id
JOIN time_slots as TS on (TS.Start_Time = TIME(ICR.EARLIEST_VISIT_TIME) AND TS.End_Time = TIME(ICR.LATEST_VISIT_TIME))
WHERE
ICD.CALL_STATUS_DETAILS = 'Successful'
AND ICD.RESOURCE_NUMBER=1;

drop view impact_activity_jan17;



-- Testing the view
select count(*) from impact_activity_aug;
select * from impact_activity_aug where resource_no = 2 limit 100;
explain select * from impact_activity_aug  limit 100;


select * from impact_call_requirements where RESOURCE_NUMBER=2;
select * from impact_actual_call_details where ACOSTA_NO='561648';
select * from impact_actual_call_details where Service_order_name ='0102_NOF_DEV_PIR_Ingram TRU Jan 2017 RTV _0109_0120' and ACOSTA_NO='561648';
select * from impact_actual_call_details where Service_order_name ='0804-NOF-ANY-PNR-Winn Dixie Bilo Cookie Cracker Reset' and ACOSTA_NO='561648';

select Resource_number, count(*) from impact_actual_call_details where CALL_STATUS_DETAILS = 'Successful'
AND 
Store NOT LIKE 'Wal%' and Resource_Number = 2 group by resource_number;

select * from impact_actual_call_details where CALL_STATUS_DETAILS = 'Successful'
AND 
Store NOT LIKE 'Wal%' and Resource_Number = 2;


select * from impact_actual_call_details where resource_number<>1 and CALL_STATUS_DETAILS = 'Successful' AND Store NOT LIKE 'Wal%' ;
select resource_number, count(*) from impact_actual_call_details where resource_number<>1 and CALL_STATUS_DETAILS = 'Successful' AND Store NOT LIKE 'Wal%'  
group by RESOURCE_NUMBER;
select * from impact_actual_call_details where storeid = 246 and Acosta_no = 588758;
select * from impact_actual_call_details where ACTUAL_CALLID='A000-1904-6060C652';
select * from impact_call_requirements as ICR where ICR.SERVICE_ORDER_NUMBER = 21362 and ICR.ACOSTA_NO = 667327;

select distinct(ActivityType) from impact_activity;
drop view impact_activity_24;


desc impact_activity;
select * from impact_activity_24 limit 100;
select count(*) from impact_activity_24;
select ActivityKey, count(*) from impact_activity_24 group by ActivityKey;
select * from impact_activity_24 where ActivityKey = 'BLine1_A000-1872-0432C459';
select * from impact_actual_call_details where ACTUAL_CALLID = 'A000-1872-0432C459';
select * from impact_actual_call_details where Service_order_number = '22099' and acosta_no = '555472' and visit_id = '16750412';

select * from impact_actual_call_details where callid = 'A000-1872-0432';
select * from impact_actual_call_details where STARTED_BY_EMPLOYEE_NO = '992336834';

-- Checking For Duplications On The Actual CallID)
select left(actual_callID, 14), count(*) from impact_actual_call_details where 
CALL_STATUS_DETAILS = 'Successful'
AND 
Store NOT LIKE 'Wal%' group by left(actual_callID, 14) order by count(*) desc;

-- Over All Count
select count(*) from impact_actual_call_details where 
CALL_STATUS_DETAILS = 'Successful'
AND 
Store NOT LIKE 'Wal%';

select resource_number, count(*) from impact_actual_call_details where 
CALL_STATUS_DETAILS = 'Successful'
AND 
Store NOT LIKE 'Wal%'
group by resource_number;


select * from impact_activity_24 limit 10;

select distinct (startdate) from impact_activity_24 limit 100;


-- Extraction Queries (old)
select ActivityKey, ReqResource as ResourceId, activitytype, startdate, enddate, Latitude, Longitude, PlannedDuration as Duration, StartTime, EndTime, Store, city as City, state as State, zip as Zip, 'Eastern' as Timezone, TimeSlot as TimeSlot, Resource_No , DOW from impact_activity_24 limit 2;
select ActivityKey, ReqResource as ResourceId, activitytype, startdate, enddate, Latitude, Longitude, PlannedDuration as Duration, StartTime, EndTime, Store, city as City, state as State, zip as Zip, 'Eastern' as Timezone, TimeSlot as TimeSlot, Resource_No , DOW from impact_activity_24 where Resource_No=2;
select ActivityKey, ReqResource as ResourceId, activitytype, startdate, enddate, Latitude, Longitude, PlannedDuration as Duration, StartTime, EndTime, Store, city as City, state as State, zip as Zip, 'Eastern' as Timezone, TimeSlot as TimeSlot, Resource_No , DOW from impact_activity_24 where startdate >= '2017-06-01' and startdate <= '2017-06-31';
select ActivityKey, ReqResource as ResourceId, activitytype, startdate, enddate, Latitude, Longitude, PlannedDuration as Duration, StartTime, EndTime, Store, city as City, state as State, zip as Zip, 'Eastern' as Timezone, TimeSlot as TimeSlot, Resource_No , DOW from impact_activity_24 where startdate >= '2017-07-01' and startdate <= '2017-07-31';

-- Extraction Queries (Aug)
select ActivityKey, ReqResource as ResourceId, activitytype, startdate, enddate, Latitude, Longitude, PlannedDuration as Duration, StartTime, EndTime, Store, city as City, state as State, zip as Zip, 'Eastern' as Timezone, TimeSlot as TimeSlot, Resource_No , DOW from impact_activity_aug limit 2;
select ActivityKey, ReqResource as ResourceId, activitytype, startdate, enddate, Latitude, Longitude, PlannedDuration as Duration, StartTime, EndTime, Store, city as City, state as State, zip as Zip, 'Eastern' as Timezone, TimeSlot as TimeSlot, Resource_No , DOW from impact_activity_aug where Resource_No=2;
select ActivityKey, ReqResource as ResourceId, activitytype, startdate, enddate, Latitude, Longitude, PlannedDuration as Duration, StartTime, EndTime, Store, city as City, state as State, zip as Zip, 'Eastern' as Timezone, TimeSlot as TimeSlot, Resource_No , DOW from impact_activity_aug where startdate >= '2017-07-25' and startdate <= '2017-08-15';
select ActivityKey, ReqResource as ResourceId, activitytype, startdate, enddate, Latitude, Longitude, PlannedDuration as Duration, StartTime, EndTime, Store, city as City, state as State, zip as Zip, 'Eastern' as Timezone, TimeSlot as TimeSlot, Resource_No , DOW from impact_activity_aug where startdate >= '2017-08-16' and startdate <= '2017-09-05';

-- Extraction Queries (Jan-2018)
select ActivityKey, ReqResource as ResourceId, activitytype, startdate, enddate, Latitude, Longitude, PlannedDuration as Duration, StartTime, EndTime, Store, city as City, state as State, zip as Zip, 'Eastern' as Timezone, TimeSlot as TimeSlot, Resource_No , DOW from impact_activity_jan17 limit 2;
select ActivityKey, ReqResource as ResourceId, activitytype, startdate, enddate, Latitude, Longitude, PlannedDuration as Duration, StartTime, EndTime, Store, city as City, state as State, zip as Zip, 'Eastern' as Timezone, TimeSlot as TimeSlot, Resource_No , DOW from impact_activity_jan17 where Resource_No=2;
select ActivityKey, ReqResource as ResourceId, activitytype, startdate, enddate, Latitude, Longitude, PlannedDuration as Duration, StartTime, EndTime, Store, city as City, state as State, zip as Zip, 'Eastern' as Timezone, TimeSlot as TimeSlot, Resource_No , DOW from impact_activity_jan17 where startdate >= '2017-12-25' and startdate <= '2018-01-15';
select ActivityKey, ReqResource as ResourceId, activitytype, startdate, enddate, Latitude, Longitude, PlannedDuration as Duration, StartTime, EndTime, Store, city as City, state as State, zip as Zip, 'Eastern' as Timezone, TimeSlot as TimeSlot, Resource_No , DOW from impact_activity_jan17 where startdate >= '2018-01-16' and startdate <= '2018-02-05';


select * from all_stores limit 100;
select * from impact_call_requirements limit 100;
select * from impact_actual_call_details where acosta_no='561648';
select * from impact_actual_call_details where callid = 'A000-1872-0432';

select * from impact_actual_call_details where Service_order_number = '22540' and acosta_no = '551450';
select * from impact_call_requirements where acosta_no = '551450';
select * from impact_call_requirements where Service_order_number = '22099' and acosta_no = '555472' and visit_id = '16750412';
select * from impact_call_requirements where Service_order_number = '21362' and acosta_no = '667327' and visit_id = '16395298' order by EARLIEST_VISIT_DATE desc;
select earliest_visit_date, LATEST_VISIT_DATE from impact_call_requirements where Service_order_number = '21362' and acosta_no = '667327' order by EARLIEST_VISIT_DATE desc;
select * from all_call_types where CALLTYPE_CODE='imp30';
select * from all_stores where acosta_no='551450';

select count(*) from impact_actual_call_details as ICD
WHERE
ICD.CALL_STATUS_DETAILS = 'Successful'
AND 
ICD.Store NOT LIKE 'Wal%'
AND ICD.RESOURCE_NUMBER=1;

select * from impact_actual_call_details as ICD
WHERE
ICD.CALL_STATUS_DETAILS = 'Successful'
AND 
ICD.Store NOT LIKE 'Wal%'
AND ICD.RESOURCE_NUMBER=1 limit 100;