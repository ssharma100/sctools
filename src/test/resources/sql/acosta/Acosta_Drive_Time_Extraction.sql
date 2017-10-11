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
(RM.g_drive_time * .1583) as 'Time Cost' 
from route_metrics as RM
JOIN associates_info as AI on AI.EMPLOYEE_NO = RM.resource_id and (AI.CONTINUITY=1 or AI.CONTINUITY_WM = 1)
where (RM.from_activity not like 'str%' and RM.to_activity not like 'end%');

create view `impact_drive_work` AS
select RM.*, 
(RM.g_drive_distance * 0.33) as 'Miles Cost', 
(RM.g_drive_time * .1583) as 'Time Cost' 
from route_metrics as RM
JOIN associates_info as AI on AI.EMPLOYEE_NO = RM.resource_id and (AI.CONTINUITY=0 or AI.CONTINUITY_WM = 0 and AI.Impact = 1)
where (RM.from_activity not like 'str%' and RM.to_activity not like 'end%');

-- View Of Work Activities Continuity
create view `continuity_drive_commute` AS
select RM.*, 
if (RM.g_drive_distance > 20, (RM.g_drive_distance - 20) * 0.33, 0) as 'Miles Cost', 
if (RM.g_drive_time > 60, (RM.g_drive_time - 60) * .1583 , 0) as 'Time Cost' 
from route_metrics as RM
JOIN associates_info as AI on AI.EMPLOYEE_NO = RM.resource_id and (AI.CONTINUITY=1 or AI.CONTINUITY_WM = 1)
where (RM.from_activity like 'str%' or RM.to_activity like 'end%');

create view `impact_drive_commute` AS
select RM.*, 
if (RM.g_drive_distance > 20, (RM.g_drive_distance - 20) * 0.33, 0) as 'Miles Cost', 
if (RM.g_drive_time > 60, (RM.g_drive_time - 60) * .1583 , 0) as 'Time Cost' 
from route_metrics as RM
JOIN associates_info as AI on AI.EMPLOYEE_NO = RM.resource_id and (AI.CONTINUITY=0 and AI.CONTINUITY_WM = 0 and AI.IMPACT=1)
where (RM.from_activity like 'str%' or RM.to_activity like 'end%');


-- Extract Continuity Commute Information
select *, from continuity_drive_commute where route_day >= '2017-01-01' and route_day <= '2017-01-31' order by resource_id, route_day;

select route_day, resource_id, sum(g_drive_distance) as 'Total Drive Distance', 
sum(g_drive_time) as 'Total Drive Time',
sum(`Miles Cost`) as 'Total Miles Paid' from continuity_drive_commute where route_day >= '2017-01-01' and route_day <= '2017-01-31' 
group by route_day, resource_id order by route_day, resource_id;

select sum(g_drive_distance) as 'Total Drive Distance', 
sum(g_drive_time) as 'Total Drive Time',
sum(`Miles Cost`) as 'Total Miles Paid' from continuity_drive_commute where route_day >= '2017-01-01' and route_day <= '2017-01-31' ;

-- Extract Impact Commute Information
select * from impact_drive_commute where route_day >= '2017-01-01' and route_day <= '2017-01-31' order by resource_id, route_day;

select route_day, resource_id, sum(g_drive_distance) as 'Total Drive Distance', 
sum(g_drive_time) as 'Total Drive Time',
sum(`Miles Cost`) as 'Total Miles Paid' from impact_drive_commute where route_day >= '2017-01-01' and route_day <= '2017-01-31' 
group by route_day, resource_id order by route_day, resource_id;

select sum(g_drive_distance) as 'Total Drive Distance', 
sum(g_drive_time) as 'Total Drive Time',
sum(`Miles Cost`) as 'Total Miles Paid' from impact_drive_commute where route_day >= '2017-01-01' and route_day <= '2017-01-31' ;


-- Extract Impact Work Information
select * from impact_drive_work where route_day >= '2017-01-01' and route_day <= '2017-01-31' order by resource_id, route_day;

select route_day, resource_id, sum(g_drive_distance) as 'Total Drive Distance', 
sum(g_drive_time) as 'Total Drive Time',
sum(`Miles Cost`) as 'Total Miles Paid' from impact_drive_commute where route_day >= '2017-01-01' and route_day <= '2017-01-31' 
group by route_day, resource_id order by route_day, resource_id;

select sum(g_drive_distance) as 'Total Drive Distance', 
sum(g_drive_time) as 'Total Drive Time',
sum(`Miles Cost`) as 'Total Miles Paid' from impact_drive_commute where route_day >= '2017-01-01' and route_day <= '2017-01-31' ;

select * from impact_drive_commute limit 1000;

select * from continuity_actual_call_details limit 10;

select avg(g_drive_distance), avg(g_drive_time) from route_metrics where route_day >= '2017-01-01' and route_day <= '2017-01-31' 
and 
(from_activity not like 'str%' and to_activity not like 'end%');