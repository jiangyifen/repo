#!/bin/bash
export PGPASSWORD='postgres'
backup_dir='/db_192.168.1.242_bak'
db_ip='192.168.1.242'
db_port='5432'


today()
{
        date +%Y-%m-%d
}

seven_days_ago()
{
        date --date='7 day ago' +%Y-%m-%d
}


echo backup db from ip:$db_ip port:$db_port to $backup_dir/db_bak_$(today)_* ...

pg_dumpall -h $db_ip -p $db_port -U postgres | split -d -b 1000m - /$backup_dir/db_bak_$(today)_

echo remove backup before 7 days ago ...

rm $backup_dir/db_bak_$(seven_days_ago)_* -f
