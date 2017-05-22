CREATE VIEW resource_utilization_week1
AS
select ReqResource, sum(PlannedDuration) as Total_PlannedDuration, sum(ExecutionDuration) as Total_ExecutionDuration, 
ceil((sum(ExecutionDuration)/60)/5) as Hours_Per_Day,
ceil((sum(ExecutionDuration)/60)) as Hours_Per_Week
from continuity_activity_may18 where startdate >= '2018-04-30' and startdate <= '2018-05-04' group by ReqResource order by ReqResource;

CREATE VIEW resource_utilization_week2
AS
select ReqResource, sum(PlannedDuration) as Total_PlannedDuration, sum(ExecutionDuration) as Total_ExecutionDuration, 
ceil((sum(ExecutionDuration)/60)/5) as Hours_Per_Day,
ceil((sum(ExecutionDuration)/60)) as Hours_Per_Week
from continuity_activity_may18 where startdate >= '2018-05-07' and startdate <= '2018-05-11' group by ReqResource order by ReqResource;

CREATE VIEW resource_utilization_week3
AS
select ReqResource, sum(PlannedDuration) as Total_PlannedDuration, sum(ExecutionDuration) as Total_ExecutionDuration, 
ceil((sum(ExecutionDuration)/60)/5) as Hours_Per_Day,
ceil((sum(ExecutionDuration)/60)) as Hours_Per_Week
from  continuity_activity_may18 where startdate >= '2018-05-14' and startdate <= '2018-05-18' group by ReqResource order by ReqResource;

CREATE VIEW resource_utilization_week4
AS
select ReqResource, sum(PlannedDuration) as Total_PlannedDuration, sum(ExecutionDuration) as Total_ExecutionDuration, 
ceil((sum(ExecutionDuration)/60)/5) as Hours_Per_Day,
ceil((sum(ExecutionDuration)/60)) as Hours_Per_Week
from continuity_activity_may18 where startdate >= '2018-05-21' and startdate <= '2018-05-25' group by ReqResource order by ReqResource;

CREATE VIEW resource_utilization_week5
AS
select ReqResource, sum(PlannedDuration) as Total_PlannedDuration, sum(ExecutionDuration) as Total_ExecutionDuration, 
ceil((sum(ExecutionDuration)/60)/5) as Hours_Per_Day,
ceil((sum(ExecutionDuration)/60)) as Hours_Per_Week
from  continuity_activity_may18 where startdate >= '2018-05-28' and startdate <= '2018-06-01' group by ReqResource order by ReqResource;


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

CREATE VIEW resource_utilization_week5_wm
AS
select ReqResource, sum(PlannedDuration) as Total_PlannedDuration, sum(ExecutionDuration) as Total_ExecutionDuration, 
ceil((sum(ExecutionDuration)/60)/5) as Hours_Per_Day,
ceil((sum(ExecutionDuration)/60)) as Hours_Per_Week
from  continuity_activity_may18_wm where startdate >= '2018-05-28' and startdate <= '2018-06-01' group by ReqResource order by ReqResource;


select * from resource_utilization_week1_wm;
select * from resource_utilization_week2_wm;
select * from resource_utilization_week3_wm;
select * from resource_utilization_week4_wm;
select * from resource_utilization_week5_wm;

--
-- Utilization For Weekends
--
select COMPLETED_BY_EMPLOYEE_NO, '8am-5pm' as CONTY_SAT_SHIFT, dayname(CALL_STARTED_LOCAL) from continuity_actual_call_details where DAYNAME(CALL_STARTED_LOCAL) = 'Saturday';

CREATE TABLE 
continuity_assciates_weekend 
`EMPLOYEE_NO` int(9) PRIMARY,


