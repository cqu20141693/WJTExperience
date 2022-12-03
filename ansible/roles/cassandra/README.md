启动

```bash
su - cassandra
cassandra
```

查看集群状态

```bash
nodetool status
```

默认用户登录

```bash
cqlsh $HOST -ucassandra -pcassandra
```

创建用户

```
create user cc_iiot with password 'cc@123' superuser;

-- alter user cc_iiot WITH password 'cc@123' ;
```

删除默认用户

```
-- 退出cassandra登录，用新建用户登录后执行
drop user cassandra;
```

创建keyspace和表

```bash
# 参考代码建表语句
```

