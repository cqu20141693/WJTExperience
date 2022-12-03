## mongo

### docker 安装

### 用户创建
```
docker exec -it mongo mongo admin
```
```sql
use
admin;
db.createUser(
  {
    user: "ccAdmin",
    pwd: "cc@123456", // or cleartext password
    roles: [
      { role: "userAdminAnyDatabase", db: "admin" },
      { role: "readWriteAnyDatabase", db: "admin" }
    ]
  }
)
db.createUser(
  {
    user: "iiot",
    pwd: "cqu1234567", // or cleartext password
    roles: [
      { role: "dbOwner", db: "iotTopology" }
    ],
   "mechanisms" : [
                        "SCRAM-SHA-1",
                        "SCRAM-SHA-256"
                ]
  }
)
//configuration
```
``` 
密码特殊字符：  @的url编码为%40，:的编码为%3a
```
### 创建数据库表
1. [授权角色](https://www.cnblogs.com/Neeo/articles/14275882.html#%E5%86%85%E7%BD%AE%E8%A7%92%E8%89%B2%E5%92%8C%E6%9D%83%E9%99%90)
``` 
use admin;
db.grantRolesToUser( "iiot" , [ { role: "userAdmin", db: "iotTopoloy" } ])
db.grantRolesToUser( "iiot" , [ { role: "dbOwner", db: "iotFileServer" } ])
db.getUser("iiot")

db.revokeRolesFromUser("iiot",[{role:"userAdmin",db:"iotTopoloy"}])
db.revokeRolesFromUser("iiot",[{role:"userAdmin",db:"iotTopology"}])
```
