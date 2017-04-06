-- Table that acts as the input queue for routing events
-- This table will be observed and drained by the Camel application
-- All contents will be use to send origination/destination information to
-- the Google distance API

create table route_queue (
`g_request` varchar(200) COMMENT 'Google Request Identifier',
`g_result` enum('NotRun', 'OK', 'ERROR') DEFAULT 'NotRun',
`g_msg` varchar(256),
`from_activity` varchar(128) NOT null COMMENT 'Unique Identifier for route - also activity ID',
`to_activity` varchar(128) NOT null COMMENT 'Unique Identifier for route - also activity ID',
`resource_id` varchar(80) COMMENT 'Resource Idenifier For The Route',
`origin_lat` varchar(15) NOT NULL COMMENT 'Origination Latitude',
`origin_long` varchar(15) NOT NULL COMMENT 'Origination Longitude',
`dest_lat` varchar(15) NOT NULL COMMENT 'Destiniation Latitude',
`dest_long` varchar(15) NOT NULL COMMENT 'Desitnation Latitude',
`g_drive_time` integer COMMENT 'Google Provided Drive Time',
`g_drive_distance` varchar(20) COMMENT 'Google PRovided Drive Distance',

PRIMARY KEY (`g_request`),
KEY `KEY_RQ_PK` (`resource_id`),
INDEX `Key_DriveTime` (`g_drive_time`),
INDEX `Key_DriveDist` (`g_drive_distance`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


drop table route_queue;

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