select * from associates_info;

select * from  continuity_actual_call_details limit 10;

delete from route_plan;
select * from route_plan order by route_id, resource_id, route_order asc;
select * from route_plan where resource_id='992297896';
select * from impact_actual_call_details where COMPLETED_BY_EMPLOYEE_NO = 992205762 and status = 'Completed';
select * from impact_actual_call_details where Actual_callID = 'A000-1863-0799C197';
select all_stores;
select * from  impact_actual_call_details where Actual_callID = 'A000-1872-4037C746';

select * from  impact_actual_call_details where Actual_callID = 'A000-1872-4037C746';

select  OriginalStartDate as route_date, ReqResource as resource_id  from impact_activity_24 group by OriginalStartDate, resource_id order by originalstartdate;

desc route_plan;

select count(*) from route_plan;


select * from route_plan where resource_id = '992310469' order by route_id, resource_id, route_order;

select count(*) from impact_activity_24 where Resource_No=2;
select * from impact_activity_24 order by startdate desc limit 10;

--
-- Route day extraction of resources/day for Impact
--
select distinct DATE_PLANNED, 'impact' as 'Activity_Type', COMPLETED_BY_EMPLOYEE_NO from impact_actual_call_details as ICD
WHERE
ICD.CALL_STATUS = 'Completed'
AND ICD.RESOURCE_NUMBER=1;

--
-- Route day extraction of resources/day for Continuity
--
select distinct DATE_PLANNED, 'continuity' as 'Activity_Type', COMPLETED_BY_EMPLOYEE_NO from continuity_actual_call_details as ICD
WHERE
ICD.CALL_STATUS = 'Completed'
AND ICD.RESOURCE_NUMBER=1;

-- Loading All Impact Distinct Day/Resource Combinations For Jobs
select distinct OriginalStartDate as route_date, ReqResource as resource_id from impact_activity_24;

-- Loading All Conitnuity Jobs (None Category)
select distinct OriginalStartDate as route_date, ReqResource as resource_id from continuity_activity_None;
select distinct OriginalStartDate as route_date, ReqResource as resource_id from continuity_activity_1PerMo;
select distinct OriginalStartDate as route_date, ReqResource as resource_id from continuity_activity_2PerMo;
select distinct OriginalStartDate as route_date, ReqResource as resource_id from continuity_activity_1PerQ;
select distinct OriginalStartDate as route_date, ReqResource as resource_id from continuity_activity_2PerQ;

