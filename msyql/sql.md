### sql

### 新建用户

```
CREATE USER “test”@”localhost” IDENTIFIED BY “1234”; #本地登录
CREATE USER “superset”@”%” IDENTIFIED BY “superset”; #远程登录

利用root 用户为用户创建一个数据库(testDB)：
create database testDB;
create database testDB default charset utf8 collate utf8_general_ci;

授权test用户拥有testDB数据库的所有权限：

grant all privileges on testDB.* to “test”@”localhost” identified by “1234”;
flush privileges; #刷新系统权限表

利用root 用户修改指定用户密码

update mysql.user set authentication_string=password(“新密码”) where User=”test” and Host=”localhost”;
flush privileges;
```

#### 登录

``` 
1.  mysql -u root -p 
MySQL 8.0 Public Key Retrieval is not allowed 
在连接后面添加allowPublicKeyRetrieval=true
```

#### 清表数据

``` 
truncate table table_name;
delete * from table_name;
```

### 聚合函数

1. 字符串max

``` 
select potno , min(ddate), max(ddate), max(concat(ddate, '#', slot_days)) 
from dwd_daytable dd 
where ddate >= '2019-1-1' and ddate <= '2020-1-1' 
group by potno ;
```

2. 数字精度

```
SELECT workshop_name,round(avg(sysv),3) as sysv,round(avg(sysi),3) as sysi 
FROM dwd_workshop_sysvi 
WHERE (workshop_code = ? AND ddate >= ? AND ddate <= ?) 
GROUP BY workshop_name
```
