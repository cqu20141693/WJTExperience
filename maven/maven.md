### maven

是一个标准化的Java项目管理和构建工具，它的主要功能有：

提供了一套标准化的项目结构； 提供了一套标准化的构建流程（编译，测试，打包，发布……）； 提供了一套依赖管理机制。

#### Maven项目结构

``` 
一个使用Maven管理的普通的Java项目，它的目录结构默认如下：
a-maven-project
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   └── resources
│   └── test
│       ├── java
│       └── resources
└── target

项目的根目录a-maven-project是项目名，它有一个项目描述文件pom.xml，存放Java源码的目录是src/main/java，
存放资源文件的目录是src/main/resources，存放测试源码的目录是src/test/java，存放测试资源的目录是src/test/resources，
最后，所有编译、打包生成的文件都放在target目录里。这些就是一个Maven项目的标准目录结构。

Maven使用pom.xml定义项目内容，并使用预设的目录结构；
在Maven中声明一个依赖项可以自动下载并导入classpath；
Maven使用groupId，artifactId和version唯一定位一个依赖。
```

#### Maven 安装和配置

1. 安装Maven和本地环境配置 如果idea 默认版本可以用，可以不用安装
2. ide maven 配置 可以使用ide自带配置和maven插件

``` 
File -> setting -> Build,Execution -> Build Tool -> maven 

maven home path ： idea默认版本，（配置本地地址）
user setting file:   配置远程包仓库地址： 默认配置文件使用的是中央仓库，国内镜像仓库（阿里）和私有仓库配置
local repositroy: 配置本地仓库地址： 建议配置到非c盘
``` 

3. idea maven 创建java项目

``` 
1. File -> new -> project -> Maven
2. 配置： 项目名，advance 配置groupID

```

#### maven 命令

1. mvn -v 终端输入命令或者idea maven 功能中的lifecycle
2. mvn clean compile test package install deploy

3. maven profile

``` 
模块开发
pom中定义profiles
编译cassandra-samples及其依赖的模块
mvn clean install -pl cassandra-samples -am
忽略test 生命周期 -DskipTests
mvn clean install -pl cassandra-samples -am -Dmaven.test.skip=true
将things 模块也打包到 cassandra-samples 模块
mvn clean install -P things -pl cassandra-samples -am -Dmaven.test.skip=true
```
