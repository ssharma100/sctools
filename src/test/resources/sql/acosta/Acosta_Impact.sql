
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
concat('BLine1_', ICD.ACTUAL_CALLID) as ActivityKey,
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
JOIN all_stores as STORE on STORE.acosta_no = ICD.acosta_no
JOIN all_call_types as ALLCALL on ALLCALL.CallType_Code = concat('imp', ICD.DURATION_PLANNED_MINUTES)
JOIN impact_call_requirements as ICR on 
ICR.SERVICE_ORDER_NUMBER = ICD.SERVICE_ORDER_NUMBER and ICR.ACOSTA_NO = ICD.ACOSTA_NO AND ICR.Visit_id = ICD.Visit_id
JOIN time_slots as TS on (TS.Start_Time = TIME(ICR.EARLIEST_VISIT_TIME) AND TS.End_Time = TIME(ICR.LATEST_VISIT_TIME))
WHERE
ICD.CALL_STATUS_DETAILS = 'Successful'
AND 
ICD.Store NOT LIKE 'Wal%';

drop view impact_activity_24;
-- 29 Weeks Ahead
-- 34 Weeks Ahead
-- 39 Weeks Ahead

select count(*) from impact_activity_24;
select * from impact_activity_24 limit 100;
select * from impact_activity_24 order by StartDate asc limit 10;
select * from impact_activity_24 where resource_No=2 limit 100;
select * from impact_actual_call_details limit 100;
select * from impact_call_requirements limit 100;

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
drop view impact_activity_29;
drop view impact_activity_34;
drop view impact_activity_39;

desc impact_activity;
select * from impact_activity_24 limit 100;
select count(*) from impact_activity_24;
select ActivityKey, count(*) from impact_activity_24 group by ActivityKey;
select * from impact_activity_24 where ActivityKey = 'BLine1_A000-1872-0432C459';
select * from impact_actual_call_details where ACTUAL_CALLID = 'A000-1872-0432C459';
select * from impact_actual_call_details where Service_order_number = '22099' and acosta_no = '555472' and visit_id = '16750412';


select * from impact_activity_24 limit 10;

select distinct (startdate) from impact_activity_24 limit 100;
select count(*) from impact_activity_24 where startdate = '2017-07-07';

-- Extraction Queries
select ActivityKey, ReqResource as ResourceId, activitytype, startdate, Latitude, Longitude, PlannedDuration as Duration, StartTime, EndTime, Store, city as City, state as State, zip as Zip, 'Eastern' as Timezone, TimeSlot as TimeSlot from impact_activity_24 limit 50;
select ActivityKey, ReqResource as ResourceId, activitytype, startdate, EndDate, Latitude, Longitude, PlannedDuration as Duration, StartTime, EndTime, Store, city as City, state as State, zip as Zip, 'Eastern' as Timezone, TimeSlot as TimeSlot from impact_activity_24 where startdate >= '2017-08-02' and startdate <= '2017-08-15';


select * from all_stores limit 100;
select * from impact_call_requirements limit 100;
select * from impact_actual_call_details where acosta_no='561648';

select * from impact_actual_call_details where Service_order_number = '22540' and acosta_no = '551450';
select * from impact_call_requirements where acosta_no = '551450';
select * from impact_call_requirements where Service_order_number = '22099' and acosta_no = '555472' and visit_id = '16750412';
select * from impact_call_requirements where Service_order_number = '21362' and acosta_no = '667327' and visit_id = '16395298' order by EARLIEST_VISIT_DATE desc;
select earliest_visit_date, LATEST_VISIT_DATE from impact_call_requirements where Service_order_number = '21362' and acosta_no = '667327' order by EARLIEST_VISIT_DATE desc;
select * from all_call_types where CALLTYPE_CODE='imp30';
select * from all_stores where acosta_no='551450';
