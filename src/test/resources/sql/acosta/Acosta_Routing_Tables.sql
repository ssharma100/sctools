-- Table that acts as the input queue for routing events
-- This table will be observed and drained by the Camel application
-- All contents will be use to send origination/destination information to
-- the Google distance API

CREATE TABLE `route_metrics` (
  `g_request` varchar(255) NOT NULL COMMENT 'Google Request Identifier',
  `g_result` varchar(32) DEFAULT 'NOTRUN',
  `g_msg` varchar(256) DEFAULT NULL,
  `route_day` DATE NOT NULL,
  `from_activity` varchar(128) NOT NULL COMMENT 'Unique Identifier for route - also activity ID',
  `to_activity` varchar(128) NOT NULL COMMENT 'Unique Identifier for route - also activity ID',
  `resource_id` varchar(80) DEFAULT NULL COMMENT 'Resource Identifier For The Route',
  `origin_lat` varchar(15) NOT NULL COMMENT 'Origination Latitude',
  `origin_long` varchar(15) NOT NULL COMMENT 'Origination Longitude',
  `dest_lat` varchar(15) NOT NULL COMMENT 'Destination Latitude',
  `dest_long` varchar(15) NOT NULL COMMENT 'Destination Latitude',
  `g_drive_time` int(11) DEFAULT NULL COMMENT 'Google Provided Drive Time',
  `g_drive_distance` decimal(6,2) DEFAULT NULL,
  `origin` varchar(164) DEFAULT NULL,
  `dest` varchar(164) DEFAULT NULL,
  `created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`g_request`, `route_day`),
  KEY `KEY_RQ_PK` (`resource_id`),
  KEY (`from_activity`, `to_activity`),
  KEY `Key_DriveTime` (`g_drive_time`),
  KEY `Key_DriveDist` (`g_drive_distance`),
  KEY (`created`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


drop table route_metrics;

-- Travel Time/Distance Results Table

create table route_plan (
`route_day` date NOT NULL COMMENT 'Unique Identifier For Route - date of the route',
`resource_id` varchar(80)NOT NULL COMMENT 'Resource Idenifier For The Route',
`appoint_id` varchar(128) NOT NULL COMMENT 'OFSC Or Actual_Call_ID Identification Number',
`start_time` time NOT NULL COMMENT 'Start Time Of The Appointment',
`end_time` time NOT NULL COMMENT 'End Time Of The Appointment',
`latitude` DECIMAL(7,4),
`longitude`DECIMAL(7,4),
`route_order` integer NOT NULL COMMENT 'Order In The Days Route',
`g_request` varchar(200) COMMENT 'Google Request Identifier (FK)',
`origin_address` varchar(300) COMMENT 'Origination Resolved Address Single Line',
`dest_address` varchar(300) COMMENT 'Destination Resolved Address',
`ofsc_est_drive` integer COMMENT 'OFSC Estiamted drive time',
`ofsc_est_work` integer COMMENT 'OFSC Estiamted work time',

PRIMARY KEY (`route_day`, `resource_id`, `appoint_id`),
INDEX `KEY_ResQ_PK` (`resource_id`),
INDEX `KEY_START_Time` (`start_time`, `end_time`),
INDEX `Key_EstDrive` (`ofsc_est_drive`),
INDEX `Key_DriInfo`  (`g_request`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

drop table route_plan;
delete from route_plan;

select * from route_plan order by route_day, resource_id, route_order;

select * from impact_actual_call_details where COMPLETED_BY_EMPLOYEE_NO = '992372942' and actual_callid='A000-1905-3755C487' order by COMPLETED_ON_LOCAL;

select * from route_plan limit 100;
select * from route_plan where ofsc_est_work is not null limit 100;
select * from route_plan where resource_id = '992316544';
select count(*) from route_plan;

select * from continuity_actual_call_details where COMPLETED_BY_EMPLOYEE_NO = '992316544';
select longitude, Latitude from associates_info where Employee_no ='992212292';
select * from route_plan where appoint_id  like 'ImpA%' limit 100;
select * from route_plan where resource_id = '992212292' and route_day='2017-06-19' order by route_day, route_order;
select * from route_plan where appoint_id like '%N421-8048-5323C118' and resource_id = '992212292';

select * from route_plan where resource_id = '992309237' and route_day='2017-06-19' order by route_day, route_order;
delete from route_plan where resource_id = '992309237' and route_day = '2017-06-19';

select * from route_plan where resource_id = '992212292' order by route_day, route_order;
select sum(ofsc_est_work) from route_plan where resource_id = '992212292' and route_day='2017-06-19';
delete from route_plan where resource_id = '992212292' and route_day='2017-06-19';

select count(*) from route_plan;

select * from route_plan where resource_id = '992352257' and route_day = '2017-01-06' order by route_day, route_order;
delete from route_plan where resource_id = '992352257' and route_day = '2017-06-19' limit 2;

select route_day, count(*) as 'Appointments', sum(g_drive_time) as 'Total Drive Time', avg(g_drive_time) as 'Avg Drive Time',
                  sum(g_drive_distance) as 'Total Drive Distance', avg(g_drive_distance) as 'Avg Drive Distance', max(g_drive_distance) as 'Max Driven',
                  min(g_drive_distance) as 'Min Driven' from route_metrics where route_day = '2017-01-02' group by route_day;
