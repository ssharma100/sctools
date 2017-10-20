select appoint_id, route_order from route_plan where resource_id= 992362846 and route_day='2018-05-08' order by route_order;

-- Charon's Impact Jobs
select substring(appoint_id,6), route_order from route_plan where resource_id=992362846 and route_day='2018-05-08' 
and 
appoint_id like 'ImpM_%'
order by route_order;

-- The Original Impact Assignments
select ACTUAL_CALLID, COMPLETED_BY_EMPLOYEE_NO, EFFECTIVE_DURATION_MINUTES from impact_actual_call_details where ACTUAL_CALLID in (
select substring(appoint_id,6) from route_plan where resource_id= 992362846 and route_day='2018-05-08' 
and 
appoint_id like 'ImpM_%'
);

-- Employee Information
select * from associates_info where employee_no = 992362846;
-- Do you want to Work Continuity Work?
select * from continuity_associates_avail where employee_no = 992362846;
select * from continuity_associates_fullhours where employee_no = 992362846;



select appoint_id from route_plan where resource_id= 992369365 and route_day='2017-01-03' order by route_order;
select appoint_id from route_plan where resource_id= 992369365 and route_day='2018-05-01' order by route_order;
select * from route_plan where resource_id= 992369365 and route_day='2017-01-03' and concat('ContyEW_', appoint_id) not in (
select appoint_id from route_plan where resource_id= 992369365 and route_day='2018-05-01'
)order by route_order;


select * from continuity_activity_may18_wm where ActivityKey = 'ContyEW_N418-8431-3328C561';
select * from continuity_actual_call_details where actual_callid='N418-8431-3328C561';
select * from continuity_actual_call_details where date(CALL_STARTED_LOCAL) = '2017-01-03' and COMPLETED_BY_EMPLOYEE_NO = 992369365;

select * from associates_info where EMPLOYEE_NO = 992369365;


select * from route_plan where resource_id= 992354004 and route_day='2017-01-03' order by route_order;
select * from route_plan where resource_id= 992359962 and route_day='2018-05-01' order by route_order;
select * from route_plan where resource_id= 992353118 and route_day='2018-05-01' order by route_order;

select route_day, count(*) from route_plan where route_day >= '2018-05-16' and (appoint_id like 'ImpM_%') group by route_day;
-- Show Drive Pairs:
select * from route_metrics where resource_id= 992362846 and route_day='2018-05-08';
select * from route_plan where resource_id= 992362846 and route_day='2018-05-08' order by route_order;


select * from route_plan where appoint_id in (select concat('ImpM_', appoint_id) from route_plan where resource_id= 992354004 and route_day='2017-01-03' and appoint_id not like 'end%' and appoint_id not like 'str%');
select concat('ImpM_', appoint_id) from route_plan where resource_id= 992354004 and route_day='2017-01-03';

-- 992354004 Is On Vacation Now - Zero Jobs....

-- Assigned To: 992353118, 992359962, 992363986

-- Original User Compare Current To Jan 2017 (Stops and Drive Metrics)
select route_day, count(*), sum(g_drive_time), sum(g_drive_distance) from route_metrics where resource_id= 992362846 and (route_day='2018-05-08' or route_day='2017-01-10') and (from_activity not like 'str%' and to_activity not like 'end%')
group by route_day;
-- With Commute Metrics
select route_day, count(*), su	m(g_drive_time), sum(g_drive_distance) from route_metrics where resource_id= 992362846 and (route_day='2018-05-08' or route_day='2017-01-10')
group by route_day;
-- Costs Of Travel (Original)
select route_day, count(*), sum(g_drive_time), sum(g_drive_distance), sum(`Miles Cost`), sum(`Time Cost`)  from continuity_drive_work where resource_id= 992362846 and (route_day='2017-01-10') and (from_activity not like 'str%' and to_activity not like 'end%')
group by route_day;
-- Cost Of Commute (Original)
select route_day, count(*), sum(g_drive_time), sum(g_drive_distance), sum(`Miles Cost`), sum(`Time Cost`)  from continuity_drive_commute where resource_id= 992362846 and (route_day='2017-01-10')
group by route_day;

-- Costs Of Travel (Optimized)
select route_day, count(*), sum(g_drive_time), sum(g_drive_distance), sum(`Miles Cost`), sum(`Time Cost`)  from continuity_drive_work where resource_id= 992362846 and (route_day='2018-05-08') and (from_activity not like 'str%' and to_activity not like 'end%')
group by route_day;
-- Cost Of Commute (Optimized)
select route_day, count(*), sum(g_drive_time), sum(g_drive_distance), sum(`Miles Cost`), sum(`Time Cost`)  from continuity_drive_commute where resource_id= 992362846 and (route_day='2018-05-08')
group by route_day;

-- Cost Of Impact From Other's Work Tasks
select * from impact_drive_work where from_activity in ('A000-1875-5124C949', 'A000-1885-6137C881', 'A000-1869-3125C081', 'A000-1869-3266C391') 
or 
to_activity in ('A000-1875-5124C949', 'A000-1885-6137C881', 'A000-1869-3125C081', 'A000-1869-3266C391');

select sum(`Miles Cost`), sum(`Time Cost`) from impact_drive_work where from_activity in ('A000-1875-5124C949', 'A000-1885-6137C881', 'A000-1869-3125C081', 'A000-1869-3266C391') 
or 
to_activity in ('A000-1875-5124C949', 'A000-1885-6137C881', 'A000-1869-3125C081', 'A000-1869-3266C391');

select * from route_metrics where resource_id= 992369365 and (route_day='2017-01-10');
select * from route_metrics where resource_id= 992369365 and (route_day='2018-05-08');

-- In Jan These guys Did Comparitive Milage/Time Of:

-- Peth - is Impact Worker
select route_day, count(*), sum(g_drive_time), sum(g_drive_distance) from route_metrics where resource_id= 992353118 and (route_day='2018-05-01' or route_day='2017-01-03') and (from_activity not like 'str%' and to_activity not like 'end%')
group by route_day;

-- Rodrize
select route_day, count(*), sum(g_drive_time), sum(g_drive_distance) from route_metrics where resource_id= 992359962 and (route_day='2018-05-01' or route_day='2017-01-03') and (from_activity not like 'str%' and to_activity not like 'end%')
group by route_day;

-- Beatrice Alvarez 
select route_day, count(*), sum(g_drive_time), sum(g_drive_distance) from route_metrics where resource_id= 992363986 and (route_day='2018-05-01' or route_day='2017-01-03')
group by route_day;


-- ImpM_A000-1872-3703C531
-- ImpM_A000-1848-9550C342
-- ImpM_A000-1872-3675C341

select * from route_plan where appoint_id in ('A000-1872-3703C531', 'A000-1848-9550C342', 'A000-1872-3675C341');

select * from route_plan where resource_id= 992363986 and route_day='2017-01-03' order by route_order;
select * from route_plan where resource_id= 992354004 and route_day='2017-01-03' order by route_order;

select * from route_metrics where to_activity='ImpM_A000-1872-4510C758' and route_day = '2018-05-01';
select * from route_metrics where to_activity='A000-1872-4510C758' and route_day = '2017-01-03';


select route_day, count(*), sum(g_drive_time), sum(g_drive_distance), sum(`Miles Cost`), sum(`Time Cost`)  from continuity_drive_work where resource_id= 992363986 and (route_day='2018-05-01' or route_day='2017-01-03') and (from_activity not like 'str%' and to_activity not like 'end%')
group by route_day;

select route_day, count(*), sum(g_drive_time), sum(g_drive_distance) from route_metrics where resource_id= 992363986 and (route_day='2018-05-01' or route_day='2017-01-03')
group by route_day;

select route_day, count(*), sum(g_drive_time), sum(g_drive_distance), sum(`Miles Cost`), sum(`Time Cost`)  from impact_drive_commute where resource_id= 992354004 and (route_day='2018-05-01' or route_day='2017-01-03')
group by route_day;

-- Do they have commute times (Conty)
select route_day, resource_id, count(*), sum(g_drive_time), sum(g_drive_distance), sum(`Miles Cost`), sum(`Time Cost`)   from impact_drive_commute where resource_id in(992353118, 992359962, 992363986)  and (route_day='2018-05-01' or route_day='2017-01-03')
group by route_day, resource_id;

show create table route_metrics;

select route_day, count(*), sum(g_drive_time), sum(g_drive_distance), sum(`Miles Cost`), sum(`Time Cost`)  from impact_drive_work where resource_id= 992353118 and (route_day='2018-05-01' or route_day='2017-01-03')
group by route_day;

select route_day, count(*), sum(g_drive_time), sum(g_drive_distance), sum(`Miles Cost`), sum(`Time Cost`)  from impact_drive_work where resource_id= 992353118 and (route_day='2018-05-01' or route_day='2017-01-03')
group by route_day;



select count(*), sum(g_drive_time)/count(*), sum(g_drive_distance)/count(*) from route_metrics where resource_id= 992363986 and route_day='2018-05-01';
select count(*), sum(g_drive_time)/count(*), sum(g_drive_distance)/count(*) from route_metrics where resource_id= 992354004 and route_day='2017-01-03';