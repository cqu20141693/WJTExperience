## [NiFi](https://github.com/apache/nifi)
### [环境搭建](https://www.cnblogs.com/it-abang/p/14370705.html)
1. 拉取代码
git clone https://github.com/apache/nifi.git
2. IDE 编译运行
```
注意： 官方推荐的jdk和maven版本，1.18 版本前可以用jdk8,maven3.6
mvn -T 2.0C clean install -DskipTests

配置nifi-runtime模块的NiFi 启动类，需要添加打包编译后的conf和lib目录到类库中，
配置jvm 启动参数和环境变量 ： 才能运行，启动后可以直接进行断点调试

```
3. 打包部署
``` 
mvn -T 2.0C clean install -DskipTests 编译
编译完成后到nifi-assembly 模块target下找到打包zip包，将zip包解压，运行./bin/nifi.sh

如果需要用docker运行，将zip包拷贝到jdk8的centos环境中，执行启动脚本。
```
### 二次开发
####  用户接入
新增加nifi-guc-iaa-bundle 模块
1. 身份认证
所有用户登录后权限和数据为一份，自定义实现GucUserProvider implements LoginIdentityProvider
其中可以接入用户体现做身份认证； 自定义实现CustomManagedAuthorizer implements ManagedAuthorizer
授权时所有的请求都通过。
2. 数据隔离
用户登录后权限一致，但是数据隔离，同一租户用户登录后看到的数据一致
当前研究查询出
#### 权限接入
租户间数据隔离，租户内用户权限由租户管理员分配各自不同。
#### 自定义组件


