--
-- Clear Out Drive Times For Scenario 1 (April 29th - June 02, 2018)
--
select count(*) from route_metrics where route_day >= '2018-04-29' and route_day <= '2018-06-10' and from_activity like 'Conty%';
select count(*) from route_metrics where route_day >= '2018-04-29' and route_day <= '2018-06-10' and (from_activity like 'Im%' or to_activity like 'Im%');

select count(*) from route_metrics where route_day >= '2018-04-29' and route_day <= '2018-06-10';

select * from route_metrics where route_day >= '2018-04-29' and route_day <= '2018-06-10' and from_activity like 'Im%';

select * from route_metrics where route_day >= '2018-04-29' and route_day <= '2018-06-10';


select count(*) from route_plan where route_day >= '2018-04-29' and route_day <= '2018-06-10';
select * from route_plan where route_day >= '2018-04-29' and route_day <= '2018-06-10';
-- delete from route_plan where route_day >= '2018-04-29' and route_day <= '2018-06-10' limit 62008;

select count(*) from route_metrics where route_day >= '2018-04-29' and route_day <= '2018-06-10';

drop view continuity_drive_commute;
drop view impact_drive_commute;
drop view continuity_drive_work;
drop view impact_drive_work;


-- View Of Non-Commute Activities 
create view `continuity_drive_work` AS
select RM.*, 
(RM.g_drive_distance * 0.33) as 'Miles Cost', 
(RM.g_drive_time * 15.83) as 'Time Cost' 
from route_metrics as RM
JOIN associates_info as AI on AI.EMPLOYEE_NO = RM.resource_id and (AI.CONTINUITY=1 or AI.CONTINUITY_WM = 1)
where (RM.from_activity not like 'str%' and RM.to_activity not like 'end%');

create view `impact_drive_work` AS
select RM.*, 
(RM.g_drive_distance * 0.33) as 'Miles Cost', 
(RM.g_drive_time * 15.83) as 'Time Cost' 
from route_metrics as RM
JOIN associates_info as AI on AI.EMPLOYEE_NO = RM.resource_id and (AI.CONTINUITY=0 or AI.CONTINUITY_WM = 0 and AI.Impact = 1)
where (RM.from_activity not like 'Imp%' and RM.to_activity not like 'Imp%') limit 100;

-- View Of Work Activities Continuity
create view `continuity_drive_commute` AS
select RM.*, 
if (RM.g_drive_distance > 20, (RM.g_drive_distance - 20) * 0.33, 0) as 'Miles Cost', 
if (RM.g_drive_time > 60, (RM.g_drive_time - 60) * 15.83 , 0) as 'Time Cost' 
from route_metrics as RM
JOIN associates_info as AI on AI.EMPLOYEE_NO = RM.resource_id and (AI.CONTINUITY=1 or AI.CONTINUITY_WM = 1)
where (RM.from_activity like 'str%' or RM.to_activity like 'end%') limit 100;

create view `impact_drive_commute` AS
select RM.*, 
if (RM.g_drive_distance > 20, (RM.g_drive_distance - 20) * 0.33, 0) as 'Miles Cost', 
if (RM.g_drive_time > 60, (RM.g_drive_time - 60) * 15.83 , 0) as 'Time Cost' 
from route_metrics as RM
JOIN associates_info as AI on AI.EMPLOYEE_NO = RM.resource_id and (AI.CONTINUITY=0 and AI.CONTINUITY_WM = 0 and AI.IMPACT=1)
where (RM.from_activity like 'str%' or RM.to_activity like 'end%') limit 100;


-- Testing The Views
select * from continuity_drive_commute limit 1000;
select * from impact_drive_commute limit 1000;