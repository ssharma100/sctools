select * from acosta.all_stores limit 100;
select * from all_stores where city = 'Madeira Be';

alter table all_stores
CHANGE `City` `City` varchar(60),
CHANGE `State` `State` varchar(25),
CHANGE `LATITUDE` `LATITUDE` DECIMAL(7,4),
CHANGE `LONGITUDE` `LONGITUDE` DECIMAL(7,4);

Alter Table all_stores
ADD INDEX `Store_Freq` (`FREQUENCY`);

desc all_stores;

select * from all_stores where acosta_no = '998580';

desc all_stores;
select distinct frequency, count(*) from all_stores group by FREQUENCY;