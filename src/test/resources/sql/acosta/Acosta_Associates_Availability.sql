--
-- Create Associate View For Specific "Full Schedule"
create view continuity_associates_fullshifts as
SELECT ASSOCIATE_NAME, EMPLOYEE_NO, EMPLOYEE_TYPE, 40 as POSITION_HRS, TEAM, CONTINUITY,
CONTINUITY_VM, IMPACT_HOURS, IMPACT_MON_SHIFT,
IMPACT_TUES_SHIFT, IMPACT_Wed_SHIFT, IMPACT_THURS_SHIFT, IMPACT_FRI_SHIFT,
IMPACT_SAT_SHIFT, IMPACT_SUN_SHIFT,
'8am-5pm' as 'CONTY_MON_SHIFT',
'8am-5pm' as 'CONTY_TUES_SHIFT',
'8am-5pm' as 'CONTY_WED_SHIFT',
'8am-5pm' as 'CONTY_THURS_SHIFT',
'8am-5pm' as 'CONTY_FRI_SHIFT',
'8am-5pm' as 'CONTY_SAT_SHIFT',
'8am-5pm' as 'CONTY_SUN_SHIFT'
FROM continuity_associates_avail;

drop view continuity_associates_fullshifts;

select * from continuity_associates_avail;

select * from continuity_associates_fullshifts;

-- Create Associate View For Specific "Full Schedule"
create view continuity_associates_fullhours as
  SELECT ASSOCIATE_NAME, EMPLOYEE_NO, EMPLOYEE_TYPE, 40 as POSITION_HRS, TEAM, CONTINUITY,
    CONTINUITY_VM, IMPACT_HOURS, IMPACT_MON_SHIFT,
    IMPACT_TUES_SHIFT, IMPACT_Wed_SHIFT, IMPACT_THURS_SHIFT, IMPACT_FRI_SHIFT,
    IMPACT_SAT_SHIFT, IMPACT_SUN_SHIFT,
                                                     '8am-5pm' as 'CONTY_MON_SHIFT',
                                                     '8am-5pm' as 'CONTY_TUES_SHIFT',
                                                     '8am-5pm' as 'CONTY_WED_SHIFT',
                                                     '8am-5pm' as 'CONTY_THURS_SHIFT',
                                                     '8am-5pm' as 'CONTY_FRI_SHIFT',
                                                     '8am-5pm' as 'CONTY_SAT_SHIFT',
                                                     '8am-5pm' as 'CONTY_SUN_SHIFT'
  FROM continuity_associates_avail;