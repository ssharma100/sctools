--
-- Extracts the information for the resources working on a specific day.
-- This is for extraction of the routes for the given resource on a given day.
--
-- output format should be: resource_id, route_day

select reqResource as 'resource_id', date(startDate) as 'route_day' from continuity_activity_may18_consol
where date(startdate) >= '2018-04-29' and date (startdate) <= '2018-05-29' order by date(startdate);