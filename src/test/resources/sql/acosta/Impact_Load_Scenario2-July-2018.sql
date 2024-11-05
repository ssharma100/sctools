--
-- Impact Scenario 1 July 2018 View
--

--
-- View With Effective Duration (Rather Then Actuals)
--
create view impact_activity_jul18 as 
SELECT 
concat('ImpM_', ICD.ACTUAL_CALLID) as ActivityKey,
DATE(ICD.CALL_STARTED_LOCAL) as OriginalStartDate, 
DATE(date_add(ICD.CALL_STARTED_LOCAL, INTERVAL + 546 DAY)) as StartDate,
DATE(DATE_ADD(date_add(ICD.CALL_STARTED_LOCAL, INTERVAL + 546 DAY), INTERVAL ICD.EFFECTIVE_DURATION_MINUTES MINUTE)) as EndDate,
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
ICD.EFFECTIVE_DURATION_MINUTES as 'CallDuration', 
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

--
-- Testing And Validation
--

select * from impact_activity_jul18 limit 100;


--
-- Extraction
--
-- Extraction Queries (Jan-2018)
select ActivityKey, ReqResource as ResourceId, activitytype, startdate, enddate, Latitude, Longitude, PlannedDuration as Duration, StartTime, EndTime, Store, city as City, state as State, zip as Zip, 'Eastern' as Timezone, TimeSlot as TimeSlot, Resource_No , DOW from impact_activity_jul18 where Resource_No=2;
select ActivityKey, ReqResource as ResourceId, activitytype, startdate, enddate, Latitude, Longitude, PlannedDuration as Duration, StartTime, EndTime, Store, city as City, state as State, zip as Zip, 'Eastern' as Timezone, TimeSlot as TimeSlot, Resource_No , DOW from impact_activity_jul18 where startdate >= '2018-01-01' and startdate <= '2018-01-15';
select ActivityKey, ReqResource as ResourceId, activitytype, startdate, enddate, Latitude, Longitude, PlannedDuration as Duration, StartTime, EndTime, Store, city as City, state as State, zip as Zip, 'Eastern' as Timezone, TimeSlot as TimeSlot, Resource_No , DOW from impact_activity_jul18 where startdate >= '2018-01-16' and startdate <= '2018-02-05';
