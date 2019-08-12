--
-- Clear Out Drive Times For Scenario 1 (April 29th - June 02, 2018)
--
select count(*) from route_metrics where route_day >= '2018-04-29' and route_day <= '2018-06-10' and from_activity like 'Conty%';
select count(*) from route_metrics where route_day >= '2018-04-29' and route_day <= '2018-06-10' and (from_activity like 'Im%' or to_activity like 'Im%');

select count(*) from route_metrics where route_day >= '2018-04-29' and route_day <= '2018-06-10';

select * from route_metrics where route_day >= '2018-04-29' and route_day <= '2018-05-29';

select * from route_metrics where route_day >= '2018-04-29' and route_day <= '2018-06-10';


select count(*) from route_plan where route_day >= '2018-04-29' and route_day <= '2018-06-10';
select * from route_plan where route_day >= '2018-04-29' and route_day <= '2018-06-10';
-- delete from route_plan where route_day >= '2018-04-29' and route_day <= '2018-06-10' limit 62008;

select count(*) from route_metrics where route_day >= '2018-04-29' and route_day <= '2018-06-10';

-- View Of Non-Commute Activities 
create view `continuity_base_drive_work` AS
select RM.*, 
(RM.g_drive_distance * 0.33) as 'Miles Cost', 
(RM.g_drive_time * .1917) as 'Time Cost'
from route_metrics as RM
JOIN associates_info as AI on AI.EMPLOYEE_NO = RM.resource_id and (AI.CONTINUITY=1 or AI.CONTINUITY_WM = 1)
where
	(RM.from_activity not like 'str%' and RM.to_activity not like 'end%')
	AND
	route_day between '2017-01-01' AND '2017-01-31';

create view `impact_base_drive_work` AS
select RM.*, 
(RM.g_drive_distance * 0.33) as 'Miles Cost', 
(RM.g_drive_time * .1917) as 'Time Cost'
from route_metrics as RM
JOIN associates_info as AI on AI.EMPLOYEE_NO = RM.resource_id and (AI.CONTINUITY=0 or AI.CONTINUITY_WM = 0 and AI.Impact = 1)
where
	(RM.from_activity not like 'str%' and RM.to_activity not like 'end%')
	AND
	RM.route_day between '2017-01-01' AND '2017-01-31';

-- View Of Work Activities Continuity
create view `continuity_base_drive_commute` AS
select RM.*, 
if (RM.g_drive_distance > 20, (RM.g_drive_distance - 20) * 0.33, 0) as 'Miles Cost', 
if (RM.g_drive_time > 60, (RM.g_drive_time - 60) * .1917 , 0) as 'Time Cost'
from route_metrics as RM
JOIN associates_info as AI on AI.EMPLOYEE_NO = RM.resource_id and (AI.CONTINUITY=1 or AI.CONTINUITY_WM = 1)
where
	(RM.from_activity like 'str%' or RM.to_activity like 'end%')
	AND
	RM.route_day between '2017-01-01' AND '2017-01-31';

create view `impact_base_drive_commute` AS
select RM.*, 
if (RM.g_drive_distance > 20, (RM.g_drive_distance - 20) * 0.33, 0) as 'Miles Cost', 
if (RM.g_drive_time > 60, (RM.g_drive_time - 60) * .1917 , 0) as 'Time Cost'
from route_metrics as RM
JOIN associates_info as AI on AI.EMPLOYEE_NO = RM.resource_id and (AI.CONTINUITY=0 and AI.CONTINUITY_WM = 0 and AI.IMPACT=1)
where
	(RM.from_activity like 'str%' or RM.to_activity like 'end%')
	AND
	RM.route_day between '2017-01-01' AND '2017-01-31';


drop view impact_base_drive_commute;
drop view impact_base_drive_work;
drop view continuity_base_drive_commute;
drop view continuity_base_drive_work;
drop view impact_scenario1_drive_work;
drop view impact_scenario1_drive_commute;

-- View Of Non-Commute Activities
create view `continuity_scenario1_drive_work` AS
	select RM.*,
		(RM.g_drive_distance * 0.33) as 'Miles Cost',
		(RM.g_drive_time * .1917) as 'Time Cost'
	from route_metrics as RM
		JOIN associates_info as AI on AI.EMPLOYEE_NO = RM.resource_id and (AI.CONTINUITY=1 or AI.CONTINUITY_WM = 1)
	where
		(RM.from_activity not like 'str%' and RM.to_activity not like 'end%')
		AND
		route_day between '2018-04-29' AND '2018-05-29';

create view `impact_scenario1_drive_work` AS
	select RM.*,
		(RM.g_drive_distance * 0.33) as 'Miles Cost',
		(RM.g_drive_time * .1917) as 'Time Cost'
	from route_metrics as RM
		JOIN associates_info as AI on AI.EMPLOYEE_NO = RM.resource_id and (AI.CONTINUITY=0 or AI.CONTINUITY_WM = 0 and AI.Impact = 1)
	where
		(RM.from_activity not like 'str%' and RM.to_activity not like 'end%')
		AND
		RM.route_day between '2018-04-29' AND '2018-05-29';

-- View Of Work Activities Continuity
create view `continuity_scenario1_drive_commute` AS
	select RM.*,
		if (RM.g_drive_distance > 20, (RM.g_drive_distance - 20) * 0.33, 0) as 'Miles Cost',
		if (RM.g_drive_time > 60, (RM.g_drive_time - 60) * .1917 , 0) as 'Time Cost'
	from route_metrics as RM
		JOIN associates_info as AI on AI.EMPLOYEE_NO = RM.resource_id and (AI.CONTINUITY=1 or AI.CONTINUITY_WM = 1)
	where
		(RM.from_activity like 'str%' or RM.to_activity like 'end%')
		AND
		route_day between '2018-04-29' AND '2018-05-29';

create view `impact_scenario1_drive_commute` AS
	select RM.*,
		if (RM.g_drive_distance > 20, (RM.g_drive_distance - 20) * 0.33, 0) as 'Miles Cost',
		if (RM.g_drive_time > 60, (RM.g_drive_time - 60) * .1917 , 0) as 'Time Cost'
	from route_metrics as RM
		JOIN associates_info as AI on AI.EMPLOYEE_NO = RM.resource_id and (AI.CONTINUITY=0 and AI.CONTINUITY_WM = 0 and AI.IMPACT=1)
	where
		(RM.from_activity like 'str%' or RM.to_activity like 'end%')
		AND
		RM.route_day between '2018-04-29' AND '2018-05-29';

-- Extract Continuity Commute Information
select *, `Miles Cost` + `Time Cost` as 'Total Travel Pay' from continuity_drive_commute 
where route_day >= '2018-04-29' and route_day <= '2018-05-29' order by resource_id, route_day;

select route_day, resource_id, sum(g_drive_distance) as 'Total Drive Distance', 
sum(g_drive_time) as 'Total Drive Time',
sum(`Miles Cost`) as 'Total Miles Paid',
sum(`Time Cost`) as 'Total Time Paid',
sum(`Miles Cost`) + sum(`Time Cost`) as 'Total Travel Pay' from continuity_drive_commute 
where route_day >= '2018-04-29' and route_day <= '2018-05-29' 
group by route_day, resource_id order by route_day, resource_id;

select sum(g_drive_distance) as 'Total Drive Distance', 
sum(g_drive_time) as 'Total Drive Time',
sum(`Miles Cost`) as 'Total Miles Paid',
sum(`Time Cost`) as 'Total Time Paid',
sum(`Miles Cost`) + sum(`Time Cost`) as 'Total Travel Pay'
from impact_drive_commute where route_day >= '2018-04-29' and route_day <= '2018-05-29' and date (created) = '2017-06-18';

-- Extract Continuity Work Information
select *, `Miles Cost` + `Time Cost` as 'Total Travel Pay' from continuity_drive_work 
where route_day >= '2018-04-29' and route_day <= '2018-05-29' order by resource_id, route_day;

select route_day, resource_id, sum(g_drive_distance) as 'Total Drive Distance', 
sum(g_drive_time) as 'Total Drive Time',
sum(`Miles Cost`) as 'Total Miles Paid',
sum(`Time Cost`) as 'Total Time Paid',
sum(`Miles Cost`) + sum(`Time Cost`) as 'Total Travel Pay' from continuity_drive_work 
where route_day >= '2018-04-29' and route_day <= '2018-05-29' 
group by route_day, resource_id order by route_day, resource_id;

select sum(g_drive_distance) as 'Total Drive Distance', 
sum(g_drive_time) as 'Total Drive Time',
sum(`Miles Cost`) as 'Total Miles Paid',
sum(`Time Cost`) as 'Total Time Paid',
sum(`Miles Cost`) + sum(`Time Cost`) as 'Total Travel Pay'
from continuity_drive_commute where route_day >= '2018-04-29' and route_day <= '2018-05-29' ;

-- Extract Impact Commute Information
select *, `Miles Cost` + `Time Cost` as 'Total Travel Pay' from impact_drive_commute 
where route_day >= '2017-01-01' and route_day <= '2017-01-31' order by resource_id, route_day;

select route_day, resource_id, 
sum(g_drive_distance) as 'Total Drive Distance', 
sum(g_drive_time) as 'Total Drive Time',
sum(`Miles Cost`) as 'Total Miles Paid',
sum(`Time Cost`) as 'Total Time Paid',
sum(`Miles Cost`) + sum(`Time Cost`) as 'Total Travel Pay'
from impact_drive_commute where route_day >= '2018-04-29' and route_day <= '2018-05-29' 
group by route_day, resource_id order by route_day, resource_id;

select 
sum(g_drive_distance) as 'Total Drive Distance', 
sum(g_drive_time) as 'Total Drive Time',
sum(`Miles Cost`) as 'Total Miles Paid',
sum(`Time Cost`) as 'Total Time Paid',
sum(`Miles Cost`) + sum(`Time Cost`) as 'Total Travel Pay'
from continuity_drive_commute where route_day >= '2017-01-01' and route_day <= '2017-01-31' ;

-- Extract Impact Work Information
select *, `Miles Cost` + `Time Cost` as 'Total Travel Pay' from impact_drive_work where route_day >= '2017-01-01' and route_day <= '2017-01-31' order by resource_id, route_day;

select route_day, resource_id, 
sum(g_drive_distance) as 'Total Drive Distance', 
sum(g_drive_time) as 'Total Drive Time',
sum(`Miles Cost`) as 'Total Miles Paid',
sum(`Time Cost`) as 'Total Time Paid',
sum(`Miles Cost`) + sum(`Time Cost`) as 'Total Travel Pay'
from impact_drive_work where route_day >= '2017-01-01' and route_day <= '2017-01-31' 
group by route_day, resource_id order by route_day, resource_id;

select 
sum(g_drive_distance) as 'Total Drive Distance', 
sum(g_drive_time) as 'Total Drive Time',
sum(`Miles Cost`) as 'Total Miles Paid',
sum(`Time Cost`) as 'Total Time Paid',
sum(`Miles Cost`) + sum(`Time Cost`) as 'Total Travel Pay'
from impact_drive_work where route_day >= '2017-01-01' and route_day <= '2017-01-31' ;

-- Extract Impact Work Information
select *, `Miles Cost` + `Time Cost` as 'Total Travel Pay' from continuity_drive_work where route_day >= '2017-01-01' and route_day <= '2017-01-31' order by resource_id, route_day;

select route_day, resource_id, 
sum(g_drive_distance) as 'Total Drive Distance', 
sum(g_drive_time) as 'Total Drive Time',
sum(`Miles Cost`) as 'Total Miles Paid',
sum(`Time Cost`) as 'Total Time Paid',
sum(`Miles Cost`) + sum(`Time Cost`) as 'Total Travel Pay'
from continuity_drive_commute where route_day >= '2017-01-01' and route_day <= '2017-01-31' 
group by route_day, resource_id order by route_day, resource_id;

select 
sum(g_drive_distance) as 'Total Drive Distance', 
sum(g_drive_time) as 'Total Drive Time',
sum(`Miles Cost`) as 'Total Miles Paid',
sum(`Time Cost`) as 'Total Time Paid',
sum(`Miles Cost`) + sum(`Time Cost`) as 'Total Travel Pay'
from impact_scenario1_drive_work where date(created) = '2017-06-18';

select date(created), count(*) from impact_drive_commute where route_day >= '2018-04-29' and route_day <= '2018-05-29' 
group by (date(created));

select route_day, resource_id, 
sum(g_drive_distance) as 'Total Drive Distance', 
sum(g_drive_time) as 'Total Drive Time',
sum(`Miles Cost`) as 'Total Miles Paid',
sum(`Time Cost`) as 'Total Time Paid',
sum(`Miles Cost`) + sum(`Time Cost`) as 'Total Travel Pay'
from impact_scenario1_drive_commute where date(created) = '2017-06-18'
group by route_day, resource_id order by route_day, resource_id;


select * from impact_drive_commute where resource_id='992208487' and route_day >= '2018-04-29' and route_day <= '2018-05-29' order by created;
select * from impact_drive_work where resource_id='992208487' and route_day >= '2018-04-29' and route_day <= '2018-05-29';

select resource_id,
sum(g_drive_distance) as 'Total Drive Distance', 
sum(g_drive_time) as 'Total Drive Time',
sum(`Time Cost`) as 'Total Time Paid',
sum(`Miles Cost`) + sum(`Time Cost`) as 'Total Travel Pay'
from impact_drive_work where route_day >= '2018-04-29' and route_day <= '2018-05-29' group by resource_id order by sum(`Miles Cost`) + sum(`Time Cost`) desc;

select *
from continuity_drive_commute where route_day >= '2018-04-29' and route_day <= '2018-05-29' ;

select * from route_metrics where resource_id =992361293 and route_day >= '2018-04-29' and route_day <= '2018-05-29' ;

select date(created), count(*) from impact_drive_commute where route_day >= '2018-04-29' and route_day <= '2018-05-29' group by date(created);

select from_activity, substring(from_activity, 8), substring(from_activity, 9) from continuity_drive_work where route_day >= '2018-05-03' and from_activity not like 'ImpM%' limit  100;
select from_activity, substring(from_activity, 11), substring(from_activity, 12) from continuity_drive_commute where route_day >= '2018-05-03' and from_activity not like 'ImpM%' limit  100;
--
-- Post Adjustment
--

select 
sum(I.g_drive_distance) as 'Total Drive Distance', 
sum(I.g_drive_time) as 'Total Drive Time',
sum(I.`Miles Cost`) as 'Total Miles Paid',
sum(I.`Time Cost`) as 'Total Time Paid',
sum(I.`Miles Cost`) + sum(I.`Time Cost`) as 'Total Travel Pay'
from continuity_base_drive_work AS I
join continuity_scenario1_drive_work as OPT on I.from_activity in (substring(OPT.from_activity,8), substring(OPT.from_activity,9))
	and (OPT.route_day >= '2018-04-29' and OPT.route_day <= '2018-05-29')
where I.route_day >= '2017-01-01' and I.route_day <= '2017-01-31' ;

-- Just the Counts With Exclusion:
select 
count(*)
from continuity_base_drive_work AS I
join continuity_scenario1_drive_work as OPT on I.from_activity in (substring(OPT.from_activity,8), substring(OPT.from_activity,9))
	and (OPT.route_day >= '2018-04-29' and OPT.route_day <= '2018-05-29')
where I.route_day >= '2017-01-01' and I.route_day <= '2017-01-31' ;

select 
sum(I.g_drive_distance) as 'Total Drive Distance', 
sum(I.g_drive_time) as 'Total Drive Time',
sum(I.`Miles Cost`) as 'Total Miles Paid',
sum(I.`Time Cost`) as 'Total Time Paid',
sum(I.`Miles Cost`) + sum(I.`Time Cost`) as 'Total Travel Pay'
from continuity_base_drive_commute AS I
join continuity_scenario1_drive_commute as OPT on I.from_activity in (substring(OPT.from_activity,11), substring(OPT.from_activity,12))
	and (OPT.route_day >= '2018-04-29' and OPT.route_day <= '2018-05-29')
where I.route_day >= '2017-01-01' and I.route_day <= '2017-01-31' ;

-- Without Exclusions
select
count(*), 
sum(I.g_drive_distance) as 'Total Drive Distance', 
sum(I.g_drive_time) as 'Total Drive Time',
sum(I.`Miles Cost`) as 'Total Miles Paid',
sum(I.`Time Cost`) as 'Total Time Paid',
sum(I.`Miles Cost`) + sum(I.`Time Cost`) as 'Total Travel Pay'
from impact_drive_work AS I
where I.route_day >= '2017-01-01' and I.route_day <= '2017-01-31' ;



