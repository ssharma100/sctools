-- Table that acts as the input queue for routing events
-- This table will be observed and drained by the Camel application
-- All contents will be use to send origination/destination information to
-- the Google distance API

create table route_queue (
`id` varchar(128) NOT null COMMENT 'Unique Identifier for route - also activity ID',
`resource_id` varchar(80) COMMENT 'Resource Idenifier For The Route',
`callid` varchar(80) COMMENT 'Reference between actual call and apppointment requirement',
`acosta_no` varchar(25) COMMENT 'Acosta specific reference number',
`visitid` varchar(25) COMMENT 'Reference between actual call and apppointment requirement',
`service_number` varchar(25) COMMENT 'Reference between actual call and apppointment requirement',
`origin_lat` varchar(15) NOT NULL COMMENT 'Origination Latitude',
`origin_long` varchar(15) NOT NULL COMMENT 'Origination Longitude',
`dest_lat` varchar(15) NOT NULL COMMENT 'Destiniation Latitude',
`dest_long` varchar(15) NOT NULL COMMENT 'Desitnation Latitude',

PRIMARY KEY (`id`),
KEY `KEY_RQ_PK` (`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Travel Time/Distance Results Table

create table distance_results (
`id` varchar(128) NOT null COMMENT 'Unique Identifier for route - also activity ID',
`resource_id` varchar(80) COMMENT 'Resource Idenifier For The Route',
`g_request` varchar(200) COMMENT 'Google Request Identifier',
`result` enum('OK', 'ERROR') DEFAULT 'OK',
`result_msg` varchar(256),
`origin_address` varchar(300) COMMENT 'Origination Resolved Address',
`dest_address` varchar(300) COMMENT 'Destination Resolved Address',
`drive_time` varchar(20) COMMENT 'Google Provided Drive Time',
`drive_distance` varchar(20) COMMENT 'Google PRovided Drive Distance',

PRIMARY KEY (`id`, `g_request`),
KEY `KEY_ResQ_PK` (`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
