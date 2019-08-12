create view continuity_activity_mar18 as 
SELECT 
concat('ContyAD_', CCD.ACTUAL_CALLID) as ActivityKey,
DATE(CCD.CALL_STARTED_LOCAL) as OriginalStartDate,
DATE(date_add(CCD.CALL_STARTED_LOCAL, INTERVAL + 420 DAY)) as StartDate,
DATE(date_add(CCD.COMPLETED_ON_LOCAL, INTERVAL + 420 DAY)) as EndDate,
'bucket' as ExternalID, 
CCD.CALL_TYPE_CODE as ActivityType,
STORE.LATITUDE as Latitude, STORE.LONGITUDE as Longitude,
'all-day' as 'timeslot',
CCD.STARTED_BY_EMPLOYEE_NO as 'ReqResource',
STORE.acosta_no as 'StoreID',
STORE.address as 'Address', STORE.city as 'City', STORE.State as 'State', STORE.ZIPCODE as 'Zip',
CCD.DURATION_PLANNED_MINUTES as 'PlannedDuration',
CCD.EXECUTION_DURATION_MINUTES as 'ExecutionDuration',
'08:00:00' as 'StartTime',
'17:00:00' as 'EndTime',
CCD.STORE as Store,
'1' as Resource_No,
'1|1|1|1|1|1|1' as DOW
FROM
continuity_actual_call_details as CCD
join all_stores as STORE on STORE.STOREID = CCD.STOREID and STORE.ACOSTA_NO = CCD.ACOSTA_NO
JOIN all_call_types as ALLCALL on ALLCALL.CallType_Code = CCD.CALL_TYPE_CODE
where 
CCD.CALL_STATUS_DETAILS = 'Successful'
AND 
CCD.Store NOT LIKE 'Wal%'
AND
CCD.Store NOT LIKE 'Sams Club%'; 

drop view continuity_activity_mar18;

select count(*) from continuity_activity_mar18;
select * from continuity_activity_mar18 limit 10;
select count(*) from continuity_actual_call_details where CALL_STATUS_DETAILS = 'Successful' AND Store NOT LIKE 'Wal%';


create view continuity_activity_mar18_wm as 
SELECT 
concat('ContyADW_', CCD.ACTUAL_CALLID) as ActivityKey,
DATE(CCD.CALL_STARTED_LOCAL) as OriginalStartDate,
DATE(date_add(CCD.CALL_STARTED_LOCAL, INTERVAL + 420 DAY)) as StartDate,
DATE(date_add(CCD.COMPLETED_ON_LOCAL, INTERVAL + 420 DAY)) as EndDate,
'bucket' as ExternalID, 
CCD.CALL_TYPE_CODE as ActivityType,
STORE.LATITUDE as Latitude, STORE.LONGITUDE as Longitude,
'all-day' as 'timeslot',
CCD.STARTED_BY_EMPLOYEE_NO as 'ReqResource',
STORE.acosta_no as 'StoreID',
STORE.address as 'Address', STORE.city as 'City', STORE.State as 'State', STORE.ZIPCODE as 'Zip',
CCD.DURATION_PLANNED_MINUTES as 'PlannedDuration',
CCD.EXECUTION_DURATION_MINUTES as 'ExecutionDuration',
'08:00:00' as 'StartTime',
'17:00:00' as 'EndTime',
CCD.STORE as Store,
'1' as Resource_No,
'1|1|1|1|1|1|1' as DOW
FROM
continuity_actual_call_details as CCD
join all_stores as STORE on STORE.STOREID = CCD.STOREID and STORE.ACOSTA_NO = CCD.ACOSTA_NO
JOIN all_call_types as ALLCALL on ALLCALL.CallType_Code = CCD.CALL_TYPE_CODE
where 
CCD.CALL_STATUS_DETAILS = 'Successful'
AND 
(CCD.Store LIKE 'Wal%' OR CCD.Store LIKE 'Sams Club%'); 

drop view continuity_activity_mar18_wm;

select * from continuity_activity_mar18_wm limit 100;
select count(*) from continuity_activity_mar18_wm;

select * from continuity_actual_call_details as CCD where CCD.CALL_STATUS_DETAILS = 'Successful'
AND 
(CCD.Store LIKE 'Wal%'
OR
CCD.Store LIKE 'Sams Club%');

-- 
-- Data Upload 
--
select ActivityKey, ReqResource as ResourceId, activitytype, startdate, enddate, Latitude, Longitude, PlannedDuration as Duration, StartTime, EndTime, Store, city as City, state as State, zip as Zip, 'Eastern' as Timezone, TimeSlot as TimeSlot, Resource_No, DOW 
from continuity_activity_mar18 order by startdate;
select ActivityKey, ReqResource as ResourceId, activitytype, startdate, enddate, Latitude, Longitude, PlannedDuration as Duration, StartTime, EndTime, Store, city as City, state as State, zip as Zip, 'Eastern' as Timezone, TimeSlot as TimeSlot, Resource_No, DOW 
from continuity_activity_mar18_wm;

select ActivityKey, ReqResource as ResourceId, activitytype, startdate, enddate, Latitude, Longitude, PlannedDuration as Duration, StartTime, EndTime, Store, city as City, state as State, zip as Zip, 'Eastern' as Timezone, TimeSlot as TimeSlot, Resource_No, DOW 
from continuity_activity_mar18 where startdate >= '2018-02-25' and startdate <= '2018-03-02';
select ActivityKey, ReqResource as ResourceId, activitytype, startdate, enddate, Latitude, Longitude, PlannedDuration as Duration, StartTime, EndTime, Store, city as City, state as State, zip as Zip, 'Eastern' as Timezone, TimeSlot as TimeSlot, Resource_No, DOW 
from continuity_activity_mar18_wm where startdate >= '2018-02-25' and startdate <= '2018-03-02';