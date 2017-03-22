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
-- View For Resource Home Locations
--
create view resource_homeloc as
SELECT
AI.Employee_NO as ResourceID,
Concat('PRIMARY_', AI.Employee_No) as Name,
AI.Address_1_PRIM as Street,
AI.City_PRIM as City,
AI.State_PROV_PRIM as State,
AI.Zip_code_PRIM as Zip,
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

Select * from resource_homeloc limit 2;
drop view resource_homeloc;

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

-- View for Mixed Associates
-- Have Impact Skill Only, Not Part Of Team WalMart
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

select * from associates_info where employee_no='992201250';