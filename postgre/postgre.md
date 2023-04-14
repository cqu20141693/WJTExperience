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
4. \d tableName 查看表结构 
5. \dt 查询当前数据库下的表名称
6. select  * from pg_stat_activity ; 查看pg活跃的连接
7. SELECT pg_terminate_backend(31207); 关闭活跃进程连接，31207 为pid
8. show max_connections; ： 查看最大连接数
### CLI
``` 
启动命令： pg_ctl -D /data/postgresql/data/ -l /data/postgresql/data/logfile start
```
