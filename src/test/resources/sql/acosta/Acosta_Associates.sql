select * from all_call_types where calltype_code='GRN';
select * from all_stores;
select * from all_stores where acosta_no=417551;

alter table all_stores
ADD INDEX (`ACOSTA_No`);

select * from impact_call_requirements;
show create table all_call_types;


-- Set The Coordinates To The Proper Precision
alter table associates_info
CHANGE `LATITUDE` `LATITUDE` DECIMAL(7,4),
CHANGE `LONGITUDE` `LONGITUDE` DECIMAL(7,4);
--
-- Associate Information Management
desc associates_info;
select * from associates_info;
select distinct(TIME_ZONE) from associates_info;
select * from associates_info where CONTINUITY='1' and IMPACT ='1';


-- View for Impact Associates
-- Have Impact Skill Only, Not Part Of Team WalMart
-- Not Hired In Feb of 2017
create view impact_only_resources_upload as 
SELECT  AI.ASSOCIATE_NAME as Name,
	AI.EMPLOYEE_NO as ResourceID,
    'impact' as 'WorkSkillList',
    round(AI.LATITUDE,4) as 'Latitude',
    round(AI.LONGITUDE, 4) as 'Longitude',
	AI.ADDRESS_1_PRIM as 'Address',
    AI.CITY_PRIM as 'City',
    AI.STATE_PROV_PRIM as 'State',
    AI.ZIP_CODE_PRIM as 'Zip',
    AI.TIME_ZONE,
    AI.HOURS_PER_WEEK as 'HoursPerWeek',
    AI.Team as 'Affiliation',
    CONCAT('acosta_', AI.EMPLOYEE_NO) as Login,
    'test123' as Password
FROM associates_info as AI
WHERE (AI.CONTINUITY='0' and AI.IMPACT='1')
AND AI.Team not like 'Walmart%'
AND AI.ZIP_CODE_PRIM <> ''
AND AI.HIREDATE < '2017-02-01 00:00:00';

select * from impact_only_resources_upload;

drop view impact_only_resources_upload;

-- 
-- View For Resource Home Locations (Non-Walmart)
--
create view resource_homeloc as
SELECT
AI.Employee_NO as ResourceID,
Concat('PRIMARY_', AI.Employee_No) as Name,
AI.Address_1_PRIM as Street,
AI.City_PRIM as City,
AI.State_PROV_PRIM as State,
LEFT(AI.Zip_code_PRIM,5) as Zip,
AI.Country as Country,
'5555657788' as Phone,
'5555651122' as Pager,
concat(AI.EMPLOYEE_NO, '@acosta.com') as email,
AI.LONGITUDE as Longitude,
AI.LATITUDE as Latitude
FROM associates_info as AI
WHERE AI.Team not like 'Walmart%'
AND AI.ZIP_CODE_PRIM <> ''
AND AI.HIREDATE < '2017-02-01 00:00:00';

Select * from resource_homeloc;
Select * from resource_homeloc where resourceid='992290033';
drop view resource_homeloc;

-- 
-- View For Resource Home Locations (WalMart)
--
create view resource_homeloc_walmart as
SELECT
AI.Employee_NO as ResourceID,
Concat('PRIMARY_', AI.Employee_No) as Name,
AI.Address_1_PRIM as Street,
AI.City_PRIM as City,
AI.State_PROV_PRIM as State,
LEFT(AI.Zip_code_PRIM,5) as Zip,
AI.Country as Country,
'5555657788' as Phone,
'5555651122' as Pager,
concat(AI.EMPLOYEE_NO, '@acosta.com') as email,
AI.LONGITUDE as Longitude,
AI.LATITUDE as Latitude
FROM associates_info as AI
WHERE AI.Team like 'Walmart%'
AND AI.ZIP_CODE_PRIM <> ''
AND AI.HIREDATE < '2017-02-01 00:00:00';

select * from resource_homeloc_walmart;
select resourceId, '' as Locations,
'' as MonStart, '' as MonEnd, '' As MonHome,
'' as TuesStart, '' as TuesEnd, '' As TuesHome,
'' as WedStart, '' as WedEnd, '' As WedHome,
'' as ThrStart, '' as ThuEnd, '' As ThuHome,
'' as FriStart, '' as FriEnd, '' As FriHome,
'' as SatStart, '' as SatEnd, '' As SatHome,
'' as SunStart, '' as SunEnd, '' As SunHome
from 
resource_homeloc_walmart;

select * from continuity_walmart_resources_upload where resourceID = '992346064';
select * from continuity_associates_avail where EMPLOYEE_NO = '992346064';


-- View for Continuity Associates
-- Have Impact Skill Only, Not Part Of Team WalMart
-- Not Hired In Feb of 2017
create view continuity_only_resources_upload as 
SELECT  AI.ASSOCIATE_NAME as Name,
	AI.EMPLOYEE_NO as ResourceID,
    'continuity' as 'WorkSkillList',
	round(AI.LATITUDE,4) as 'Latitude',
    round(AI.LONGITUDE, 4) as 'Longitude',
	AI.ADDRESS_1_PRIM as 'Address',
    AI.CITY_PRIM as 'City',
    AI.STATE_PROV_PRIM as 'State',
    AI.ZIP_CODE_PRIM as 'Zip',
    AI.TIME_ZONE,
    AI.HOURS_PER_WEEK as 'HoursPerWeek',
    AI.Team as 'Affiliation',
    CONCAT('acosta_', AI.EMPLOYEE_NO) as Login,
    'test123' as Password
FROM associates_info as AI
WHERE (AI.CONTINUITY='1' and AI.IMPACT='0')
AND AI.Team not like 'Walmart%'
AND AI.ZIP_CODE_PRIM <> ''
AND AI.HIREDATE < '2017-02-01 00:00:00';

select * from continuity_only_resources_upload;

drop view continuity_only_resources_upload;

-- View for Continuity Associates (Walmart Specific)
-- Have Continuity Skill Only, Are Part Of Team WalMart
-- Not Hired In Feb of 2017
create view continuity_walmart_resources_upload as 
SELECT  AI.ASSOCIATE_NAME as Name,
	AI.EMPLOYEE_NO as ResourceID,
    'continuity' as 'WorkSkillList',
	round(AI.LATITUDE,4) as 'Latitude',
    round(AI.LONGITUDE, 4) as 'Longitude',
	AI.ADDRESS_1_PRIM as 'Address',
    AI.CITY_PRIM as 'City',
    AI.STATE_PROV_PRIM as 'State',
    AI.ZIP_CODE_PRIM as 'Zip',
    AI.TIME_ZONE,
    AI.HOURS_PER_WEEK as 'HoursPerWeek',
    AI.Team as 'Affiliation',
    CONCAT('acosta_', AI.EMPLOYEE_NO) as Login,
    'test123' as Password
FROM associates_info as AI
WHERE (AI.CONTINUITY='1' and AI.IMPACT='0')
AND AI.Team  like 'Walmart%'
AND AI.ZIP_CODE_PRIM <> ''
AND AI.HIREDATE < '2017-02-01 00:00:00';

select count(*) from continuity_walmart_resources_upload;
select * from continuity_walmart_resources_upload;

-- View for Mixed Associates
-- Have Impact Skill AND Continuity, Not Part Of Team WalMart
-- Not Hired In Feb of 2017
create view mixed_resources_upload as 
SELECT  AI.ASSOCIATE_NAME as Name,
	AI.EMPLOYEE_NO as ResourceID,
    'continuity|impact' as 'WorkSkillList',
    round(AI.LATITUDE,4) as 'Latitude',
    round(AI.LONGITUDE, 4) as 'Longitude',
	AI.ADDRESS_1_PRIM as 'Address',
    AI.CITY_PRIM as 'City',
    AI.STATE_PROV_PRIM as 'State',
    AI.ZIP_CODE_PRIM as 'Zip',
    AI.TIME_ZONE,
    AI.HOURS_PER_WEEK as 'HoursPerWeek',
    AI.Team as 'Affiliation',
    CONCAT('acosta_', AI.EMPLOYEE_NO) as Login,
    'test123' as Password
FROM associates_info as AI
WHERE (AI.CONTINUITY='1' and AI.impact='1')
AND AI.Team not like 'Walmart%'
AND AI.ZIP_CODE_PRIM <> ''
AND AI.HIREDATE < '2017-02-01 00:00:00';

select * from mixed_resources_upload;

drop view mixed_resources_upload;

select EMPLOYEE_NO from associates_info;

select * from associates_info where employee_no = '992314042';
select * from continuity_associates_avail where employee_no = '992309237' and CONTINUITY = 1 and TEAM NOT LIKE 'Wal%';
select * from continuity_associates_avail limit 2;

INSERT INTO `acosta`.`continuity_associates_avail` (`ASSOCIATE_NAME`, `EMPLOYEE_NO`, `EMPLOYEE_TYPE`, `POSITION_HRS`, `TEAM`, `CONTINUITY`, `CONTINUITY_VM`, `IMPACT_HOURS`, `IMPACT_MON_SHIFT`, `IMPACT_TUES_SHIFT`, `IMPACT_Wed_SHIFT`, `IMPACT_THURS_SHIFT`, `IMPACT_FRI_SHIFT`) 
VALUES ('Butler, Anita, G', '992281311', 'Full Time', '40', 'Grocery', '1', '0', '40', '1', '1', '1', '1', '1');

