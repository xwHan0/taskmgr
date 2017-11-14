

select * from descriptions 
where datetime(date)>=datetime('2017-11-01 00:00:00')
and datetime(date)<datetime('2017-11-30 00:00:00')
order by date desc;

