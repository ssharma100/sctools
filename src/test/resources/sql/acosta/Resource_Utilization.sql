CREATE VIEW resource_utilization_week1
AS
select ReqResource, sum(PlannedDuration) as Total_PlannedDuration, sum(ExecutionDuration) as Total_ExecutionDuration,
ceil((sum(ExecutionDuration)/60)/5) as Hours_Per_Day,
ceil((sum(ExecutionDuration)/60)) as Hours_Per_Week
from continuity_activity_may18_consol where startdate >= '2018-04-29' and startdate <= '2018-05-05' group by ReqResource order by ReqResource;

CREATE VIEW resource_utilization_week2
AS
select ReqResource, sum(PlannedDuration) as Total_PlannedDuration, sum(ExecutionDuration) as Total_ExecutionDuration, 
ceil((sum(ExecutionDuration)/60)/5) as Hours_Per_Day,
ceil((sum(ExecutionDuration)/60)) as Hours_Per_Week
from continuity_activity_may18_consol where startdate >= '2018-05-06' and startdate <= '2018-05-12' group by ReqResource order by ReqResource;

CREATE VIEW resource_utilization_week3
AS
select ReqResource, sum(PlannedDuration) as Total_PlannedDuration, sum(ExecutionDuration) as Total_ExecutionDuration, 
ceil((sum(ExecutionDuration)/60)/5) as Hours_Per_Day,
ceil((sum(ExecutionDuration)/60)) as Hours_Per_Week
from  continuity_activity_may18_consol where startdate >= '2018-05-13' and startdate <= '2018-05-19' group by ReqResource order by ReqResource;

CREATE VIEW resource_utilization_week4
AS
select ReqResource, sum(PlannedDuration) as Total_PlannedDuration, sum(ExecutionDuration) as Total_ExecutionDuration, 
ceil((sum(ExecutionDuration)/60)/5) as Hours_Per_Day,
ceil((sum(ExecutionDuration)/60)) as Hours_Per_Week
from continuity_activity_may18_consol where startdate >= '2018-05-20' and startdate <= '2018-05-26' group by ReqResource order by ReqResource;

CREATE VIEW resource_utilization_week5
AS
select ReqResource, sum(PlannedDuration) as Total_PlannedDuration, sum(ExecutionDuration) as Total_ExecutionDuration, 
ceil((sum(ExecutionDuration)/60)/5) as Hours_Per_Day,
ceil((sum(ExecutionDuration)/60)) as Hours_Per_Week
from  continuity_activity_may18_consol where startdate >= '2018-05-27' and startdate <= '2018-06-02' group by ReqResource order by ReqResource;


drop view resource_utilization_week1;
drop view resource_utilization_week2;
drop view resource_utilization_week3;
drop view resource_utilization_week4;
drop view resource_utilization_week5;

select * from resource_utilization_week1;
select * from resource_utilization_week2;
select * from resource_utilization_week3;
select * from resource_utilization_week4;
select * from resource_utilization_week5;


CREATE VIEW resource_utilization_week1_wm
AS
select ReqResource, sum(PlannedDuration) as Total_PlannedDuration, sum(ExecutionDuration) as Total_ExecutionDuration, 
ceil((sum(ExecutionDuration)/60)/5) as Hours_Per_Day,
ceil((sum(ExecutionDuration)/60)) as Hours_Per_Week
from continuity_activity_may18_wm where startdate >= '2018-04-30' and startdate <= '2018-05-04' group by ReqResource order by ReqResource;

CREATE VIEW resource_utilization_week2_wm
AS
select ReqResource, sum(PlannedDuration) as Total_PlannedDuration, sum(ExecutionDuration) as Total_ExecutionDuration, 
ceil((sum(ExecutionDuration)/60)/5) as Hours_Per_Day,
ceil((sum(ExecutionDuration)/60)) as Hours_Per_Week
from continuity_activity_may18_wm where startdate >= '2018-05-07' and startdate <= '2018-05-11' group by ReqResource order by ReqResource;

CREATE VIEW resource_utilization_week3_wm
AS
select ReqResource, sum(PlannedDuration) as Total_PlannedDuration, sum(ExecutionDuration) as Total_ExecutionDuration, 
ceil((sum(ExecutionDuration)/60)/5) as Hours_Per_Day,
ceil((sum(ExecutionDuration)/60)) as Hours_Per_Week
from  continuity_activity_may18_wm where startdate >= '2018-05-14' and startdate <= '2018-05-18' group by ReqResource order by ReqResource;

CREATE VIEW resource_utilization_week4_wm
AS
select ReqResource, sum(PlannedDuration) as Total_PlannedDuration, sum(ExecutionDuration) as Total_ExecutionDuration, 
ceil((sum(ExecutionDuration)/60)/5) as Hours_Per_Day,
ceil((sum(ExecutionDuration)/60)) as Hours_Per_Week
from continuity_activity_may18_wm where startdate >= '2018-05-21' and startdate <= '2018-05-25' group by ReqResource order by ReqResource;

drop view resource_utilization_week5_wm;
CREATE VIEW resource_utilization_week5_wm
AS
select ReqResource, sum(PlannedDuration) as Total_PlannedDuration, sum(ExecutionDuration) as Total_ExecutionDuration, 
ceil((sum(ExecutionDuration)/60)/5) as Hours_Per_Day,
ceil((sum(ExecutionDuration)/60)) as Hours_Per_Week
from  continuity_activity_may18_wm 
where startdate >= '2018-05-28' and startdate <= '2018-06-01' group by ReqResource order by ReqResource;


select * from resource_utilization_week1_wm;
select * from resource_utilization_week2_wm;
select * from resource_utilization_week3_wm;
select * from resource_utilization_week4_wm;
select * from resource_utilization_week5_wm;

--
-- Utilization For Weekends
--
Create View continuity_assocaites_avail_sat
as
select distinct COMPLETED_BY_EMPLOYEE_NO as EMPLOYEE_NO, WEEK(CALL_STARTED_LOCAL, 0) as WEEK, date(CALL_STARTED_LOCAL) AS 'Start_Date', '8am-5pm' as CONTY_SAT_SHIFT, dayname(CALL_STARTED_LOCAL) from continuity_actual_call_details where DAYNAME(CALL_STARTED_LOCAL) = 'Saturday';

Create View continuity_assocaites_avail_sun
as
select distinct COMPLETED_BY_EMPLOYEE_NO as EMPLOYEE_NO, WEEK(CALL_STARTED_LOCAL, 0) as WEEK, date(CALL_STARTED_LOCAL) AS 'Start_Date', '8am-5pm' as CONTY_SUN_SHIFT, dayname(CALL_STARTED_LOCAL) from continuity_actual_call_details where DAYNAME(CALL_STARTED_LOCAL) = 'Sunday';

select * from continuity_assocaites_avail_sat;
select * from continuity_assocaites_avail_sun  order by week;

drop view continuity_assocaites_avail_sat;
drop view continuity_assocaites_avail_sun;

--
-- Create Associate View For Specific "Full Schedule"
SELECT wk1.ReqResource, wk1.HOURS_PER_WEEK, CONAV.IMPACT_HOURS, CSAT.CONTY_SAT_SHIFT, CSUN.CONTY_SUN_SHIFT
from resource_utilization_week1 as wk1
LEFT OUTER JOIN continuity_assocaites_avail_sat AS CSAT on CSAT.EMPLOYEE_NO = wk1.reqresource and CSAT.week = 1
LEFT OUTER JOIN continuity_assocaites_avail_sun AS CSUN on CSUN.EMPLOYEE_NO = wk1.ReqResource and CSUN.week = 1
LEFT OUTER JOIN continuity_associates_avail AS CONAV on CONAV.EMPLOYEE_NO = wk1.ReqResource;

select count(*) from resource_utilization_week1;

select * from resource_utilization_week1 where ReqResource = '992325652';
select * from continuity_assocaites_avail_sat where employee_no = '992325652';
select * from continuity_assocaites_avail_sun where employee_no = '992325652';

select * from resource_utilization_week1 where ReqResource = '992293148';
select * from continuity_assocaites_avail_sat where employee_no = '992293148';
select * from continuity_assocaites_avail_sun where employee_no = '992293148';

select * from continuity_assocaites_avail_sun  where week = 1;

SELECT wk1.ReqResource, wk1.HOURS_PER_WEEK, CONAV.IMPACT_HOURS, CSAT.CONTY_SAT_SHIFT, CSUN.CONTY_SUN_SHIFT
from resource_utilization_week1 as wk1
  LEFT OUTER JOIN continuity_assocaites_avail_sat AS CSAT on CSAT.EMPLOYEE_NO = wk1.reqresource and CSAT.week = 1
  LEFT OUTER JOIN continuity_assocaites_avail_sun AS CSUN on CSUN.EMPLOYEE_NO = wk1.ReqResource and CSUN.week = 1
  LEFT OUTER JOIN continuity_associates_avail AS CONAV on CONAV.EMPLOYEE_NO = wk1.ReqResource
where CSUN.CONTY_SUN_SHIFT is not null;

select distinct COMPLETED_BY_EMPLOYEE_NO from continuity_actual_call_details where date(CALL_STARTED_LOCAL) = '2017-01-01' and CALL_STATUS_DETAILS='Successful';