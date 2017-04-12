
select * from continuity_actual_call_details limit 100;

select * from all_call_types where CALLTYPE_CODE like 'imp%';

-- --------
-- Views --
-- --------
-- 24 Weeks Ahead
create view continuity_activity_24 as 
SELECT 
concat('Conty_', ICD.ACTUAL_CALLID) as ActivityKey,
DATE(ICD.CALL_STARTED_LOCAL) as OriginalStartDate, 
DATE(date_add(ICD.DATE_PLANNED, INTERVAL +24 WEEK)) as StartDate,
DATE(DATE_ADD(date_add(ICD.DATE_PLANNED, INTERVAL +24 WEEK), INTERVAL ICD.DURATION_PLANNED_MINUTES MINUTE)) as EndDate,
'bucket' as ExternalID, 
concat('imp', ICD.DURATION_PLANNED_MINUTES) as ActivityType,
STORE.LATITUDE as Latitude, STORE.LONGITUDE as Longitude,
'all-day' as 'timeslot',
ICD.STARTED_BY_EMPLOYEE_NO as 'ReqResource',
STORE.acosta_no as 'StoreID',
STORE.address as 'Address', STORE.city as 'City', STORE.State as 'State', STORE.ZIPCODE as 'Zip',
ALLCALL.DEFAULTCALLDURATION as 'DefaultCallDuration',
ICD.EXECUTION_DURATION_MINUTES as 'PlannedDuration',
'08:00:00' as 'StartTime', '17:00:00' as 'EndTime',
ICD.STORE as Store
FROM 
continuity_actual_call_details as ICD
JOIN all_stores as STORE on STORE.acosta_no = ICD.acosta_no
JOIN all_call_types as ALLCALL on ALLCALL.CallType_Code = concat('imp', ICD.DURATION_PLANNED_MINUTES)
WHERE
ICD.CALL_STATUS_DETAILS = 'Successful'
AND 
ICD.Store NOT LIKE 'Wal%';

drop view continuity_activity_24;

select * from continuity_activity_24 limit 100;

select * from continuity_actual_call_details as ICD WHERE
ICD.CALL_STATUS_DETAILS = 'Successful'
AND 
ICD.Store NOT LIKE 'Wal%' limit 100;

select * from all_stores limit 100;
select distinct frequency from all_stores;
select * from all_call_types where CALLTYPE_code in ('RSC') limit 100;
select * from all_call_types limit 100;

select * from continuity_store_teams limit 100;
