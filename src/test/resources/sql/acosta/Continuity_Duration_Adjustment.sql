--
-- Modifications To Continuity Actual To Accommodate The New Duration Computation
--
ALTER TABLE continuity_actual_call_details
ADD column `EFFECTIVE_DURATION_MINUTES` integer
AFTER `DURATION_PLANNED_MINUTES`;

select DURATION_PLANNED_MINUTES, EXECUTION_DURATION_MINUTES, EFFECTIVE_DURATION_MINUTES
FROM continuity_actual_call_details limit 100;

select count(*)
FROM continuity_actual_call_details where EXECUTION_DURATION_MINUTES <= DURATION_PLANNED_MINUTES
AND CALL_STATUS = 'Completed';

select count(*)
FROM continuity_actual_call_details where EXECUTION_DURATION_MINUTES > DURATION_PLANNED_MINUTES
AND CALL_STATUS = 'Completed';

update continuity_actual_call_details set EFFECTIVE_DURATION_MINUTES = DURATION_PLANNED_MINUTES 
WHERE
EXECUTION_DURATION_MINUTES > DURATION_PLANNED_MINUTES
AND CALL_STATUS = 'Completed' limit 12687; 

update continuity_actual_call_details set EFFECTIVE_DURATION_MINUTES =  EXECUTION_DURATION_MINUTES
WHERE
EXECUTION_DURATION_MINUTES <= DURATION_PLANNED_MINUTES
AND CALL_STATUS = 'Completed' limit 30275; 
