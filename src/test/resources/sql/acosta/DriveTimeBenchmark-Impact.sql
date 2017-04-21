select count(*) from impact_activity_24;
select * from impact_activity_24 limit 100;

select * from impact_activity_24 where ReqResource='992209790' order by originalstartdate;
select * from impact_activity_24 where ReqResource='992210267' order by originalstartdate;

-- --------
-- Views --
-- --------
-- Distance Query
create view impact_activity_travel as 
SELECT 
concat('BLine1_', ICD.ACTUAL_CALLID) as ActivityKey,
ICD.CALL_STARTED_LOCAL as OriginalStartDate, 
date_add(ICD.CALL_STARTED_LOCAL, INTERVAL +24 WEEK) as Start,
concat('imp', ICD.DURATION_PLANNED_MINUTES) as ActivityType,
STORE.LATITUDE as Latitude, 
STORE.LONGITUDE as Longitude,
ICD.STARTED_BY_EMPLOYEE_NO as 'ResourceId',
STORE.address as 'Address', STORE.city as 'City', STORE.State as 'State', STORE.ZIPCODE as 'Zip',
ALLCALL.DEFAULTCALLDURATION as 'CallDuration', ICD.DURATION_PLANNED_MINUTES as 'PlannedDuration',
ICD.CALL_STARTED_LOCAL as 'StartTime', ICD.COMPLETED_ON_LOCAL as 'EndTime'
FROM 
continuity_actual_call_details as ICD
JOIN all_stores as STORE on STORE.acosta_no = ICD.acosta_no
JOIN all_call_types as ALLCALL on ALLCALL.CallType_Code = concat('imp', ICD.DURATION_PLANNED_MINUTES)
where ICD.CALL_STATUS_DETAILS = 'Successful'
AND 
(ICD.Store not like 'Wal Mart%' or ICD.Store not like 'Walmart%');

select * from impact_activity_travel limit 100;

-- Location For The Resource
select EMPLOYEE_NO as ResourceId,
Associate_name as Name,
ADDRESS_1_PRIM As Address,
CITY_PRIM as City,
STATE_PROV_PRIM as State,
ZIP_CODE_PRIM as Zip,
Country as Country,
Latitude, Longitude from associates_info;
