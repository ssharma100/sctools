--
-- Modifications To Imapct Actual To Accommodate The New Duration Computation
--
ALTER TABLE impact_actual_call_details
ADD column `EFFECTIVE_DURATION_MINUTES` integer
AFTER `DURATION_PLANNED_MINUTES`;

select count(*) from impact_actual_call_details;

select count(*) from impact_actual_call_details
where
CALL_STATUS_DETAILS = 'Successful' AND CALL_STATUS = 'Completed';

select DURATION_PLANNED_MINUTES, EXECUTION_DURATION_MINUTES, EFFECTIVE_DURATION_MINUTES
FROM impact_actual_call_details limit 100;

select count(*)
FROM impact_actual_call_details where EXECUTION_DURATION_MINUTES <= DURATION_PLANNED_MINUTES
                                      AND CALL_STATUS_DETAILS = 'Successful' AND CALL_STATUS = 'Completed';

select count(*)
FROM impact_actual_call_details where EXECUTION_DURATION_MINUTES > DURATION_PLANNED_MINUTES
                                          AND CALL_STATUS_DETAILS = 'Successful' AND CALL_STATUS = 'Completed';

update impact_actual_call_details set EFFECTIVE_DURATION_MINUTES = DURATION_PLANNED_MINUTES
WHERE
EXECUTION_DURATION_MINUTES > DURATION_PLANNED_MINUTES
AND CALL_STATUS_DETAILS = 'Successful' AND CALL_STATUS = 'Completed' limit 1656;

update impact_actual_call_details set EFFECTIVE_DURATION_MINUTES =  EXECUTION_DURATION_MINUTES
WHERE
EXECUTION_DURATION_MINUTES <= DURATION_PLANNED_MINUTES
AND CALL_STATUS_DETAILS = 'Successful' AND CALL_STATUS = 'Completed' limit 8177;
