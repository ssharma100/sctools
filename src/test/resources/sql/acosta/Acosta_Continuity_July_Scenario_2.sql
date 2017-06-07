--
-- Views for July 2018 Scenario 2
--

--
-- Walmart Jobs - Moved Over Day Of Job (As Is, Non-SLA)
CREATE 
VIEW `continuity_activity_jul18_wm` AS
    SELECT 
        CONCAT('ContyFW_', `CCD`.`ACTUAL_CALLID`) AS `ActivityKey`,
        CAST(`CCD`.`CALL_STARTED_LOCAL` AS DATE) AS `OriginalStartDate`,
        CAST((`CCD`.`CALL_STARTED_LOCAL` + INTERVAL 546 DAY)
            AS DATE) AS `StartDate`,
        CAST((`CCD`.`COMPLETED_ON_LOCAL` + INTERVAL 546 DAY)
            AS DATE) AS `EndDate`,
        'bucket' AS `ExternalID`,
        `CCD`.`CALL_TYPE_CODE` AS `ActivityType`,
        `STORE`.`LATITUDE` AS `Latitude`,
        `STORE`.`LONGITUDE` AS `Longitude`,
        'all-day' AS `timeslot`,
        `CCD`.`STARTED_BY_EMPLOYEE_NO` AS `ReqResource`,
        `STORE`.`ACOSTA_NO` AS `StoreID`,
        `STORE`.`ADDRESS` AS `Address`,
        `STORE`.`City` AS `City`,
        `STORE`.`State` AS `State`,
        `STORE`.`ZIPCODE` AS `Zip`,
        `CCD`.`DURATION_PLANNED_MINUTES` AS `PlannedDuration`,
        `CCD`.`EFFECTIVE_DURATION_MINUTES` AS `ExecutionDuration`,
        '08:00:00' AS `StartTime`,
        '17:00:00' AS `EndTime`,
        `CCD`.`STORE` AS `Store`,
        '1' AS `Resource_No`,
        '1|1|1|1|1|1|1' AS `DOW`
    FROM
        ((`continuity_actual_call_details` `CCD`
        JOIN `all_stores` `STORE` ON (((`STORE`.`STOREID` = `CCD`.`STOREID`)
            AND (`STORE`.`ACOSTA_NO` = `CCD`.`ACOSTA_NO`))))
        JOIN `all_call_types` `ALLCALL` ON ((`ALLCALL`.`CALLTYPE_CODE` = `CCD`.`CALL_TYPE_CODE`)))
    WHERE
        (`CCD`.`CALL_STATUS_DETAILS` = 'Successful' AND (CCD.Store LIKE 'Wal%'
OR
CCD.Store LIKE 'Sams Club%'));

select * from continuity_activity_jul18_wm;
drop view continuity_activity_jul18_wm;


-- 
-- Non Walmart Jobs With Open SLA In The Month Range)
--
CREATE 
VIEW `continuity_activity_jul18` AS
    SELECT 
        CONCAT('ContyF_', `CCD`.`ACTUAL_CALLID`) AS `ActivityKey`,
        CAST(`CCD`.`CALL_STARTED_LOCAL` AS DATE) AS `OriginalStartDate`,
        CAST('2018-07-01' AS DATE) AS `StartDate`,
        CAST('2018-07-31' AS DATE) AS `EndDate`,
        'bucket' AS `ExternalID`,
        `CCD`.`CALL_TYPE_CODE` AS `ActivityType`,
        `STORE`.`LATITUDE` AS `Latitude`,
        `STORE`.`LONGITUDE` AS `Longitude`,
        'all-day' AS `timeslot`,
        `CCD`.`STARTED_BY_EMPLOYEE_NO` AS `ReqResource`,
        `STORE`.`ACOSTA_NO` AS `StoreID`,
        `STORE`.`ADDRESS` AS `Address`,
        `STORE`.`City` AS `City`,
        `STORE`.`State` AS `State`,
        `STORE`.`ZIPCODE` AS `Zip`,
        `CCD`.`DURATION_PLANNED_MINUTES` AS `PlannedDuration`,
        `CCD`.`EFFECTIVE_DURATION_MINUTES` AS `ExecutionDuration`,
        '08:00:00' AS `StartTime`,
        '17:00:00' AS `EndTime`,
        `CCD`.`STORE` AS `Store`,
        '1' AS `Resource_No`,
        '1|1|1|1|1|1|1' AS `DOW`
    FROM
        ((`continuity_actual_call_details` `CCD`
        JOIN `all_stores` `STORE` ON (((`STORE`.`STOREID` = `CCD`.`STOREID`)
            AND (`STORE`.`ACOSTA_NO` = `CCD`.`ACOSTA_NO`))))
        JOIN `all_call_types` `ALLCALL` ON ((`ALLCALL`.`CALLTYPE_CODE` = `CCD`.`CALL_TYPE_CODE`)))
    WHERE
        (`CCD`.`CALL_STATUS_DETAILS` = 'Successful' AND (CCD.Store NOT LIKE 'Wal%'
AND
CCD.Store NOT LIKE 'Sams Club%'));

select * from continuity_activity_jul18;


-- Data Extractors:
select ActivityKey, ReqResource as ResourceId, activitytype, startdate, enddate, Latitude, Longitude, PlannedDuration as Duration, StartTime, EndTime, Store, city as City, state as State, zip as Zip, 'Eastern' as Timezone, TimeSlot as TimeSlot, Resource_No, DOW 
from continuity_activity_jul18 order by startdate;
select ActivityKey, ReqResource as ResourceId, activitytype, startdate, enddate, Latitude, Longitude, PlannedDuration as Duration, StartTime, EndTime, Store, city as City, state as State, zip as Zip, 'Eastern' as Timezone, TimeSlot as TimeSlot, Resource_No, DOW 
from continuity_activity_jul18_wm;