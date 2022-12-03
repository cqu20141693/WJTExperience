### Postgre

#### SQL
[PG 语法](https://www.runoob.com/postgresql/postgresql-syntax.html)
1. 登录PG

``` 
psql -h host -p port -U user database
psql -h localhost -p 5432 -U postgres runoobdb

// 查看副本节点
select client_addr,sync_state from pg_stat_replication;
```

2. \l 用于查看已经存在的数据库
3. \c + 数据库名 来进入数据库


### CLI
``` 
启动命令： pg_ctl -D /data/postgresql/data/ -l /data/postgresql/data/logfile start
```
