### mongo 

#### 

#### 复制集
#### GridFS 文件存储
#### 分库分表设计
1. 功能设计
```
1. 路由表工件
2. 数据路由，查询和写入
3. 数据迁移

```
2. 难点分析
```
调研mongo 鉴权方式和client连接？
MongoClient 连接以库为单位，一个服务器只会建立一个连接，可以直接切换
mongo用户鉴权通过用户名和密码，每个用户分配有权限，如果想要一个

```