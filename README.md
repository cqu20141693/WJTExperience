## 个人经验
主要是记录一下笔记，demo，设计
### [docs](https://github.com/cqu20141693/WJTExperience/tree/main)
### [java-basic](https://github.com/cqu20141693/WJTExperience/tree/java-basic)
#### JDK/IDE
1. 安装jdk，配置环境变量
2. 安装idea，配置idea
3. 利用idea创建java项目

#### 基础知识点
#### 面向对象
#### 集合
#### 并发
#### 单元测试
#### IO流
#### 网络
#### 日期时间
#### 异常
#### [日志](https://segmentfault.com/a/1190000003806086)
#### 加解密和签名
#### java8新特性
#### java9新特性

#### JVM
### 技术架构
#### 功能场景
### 协议
#### TCP
#### HTTP
#### 
### BI
#### Superset

### [iot-gateway](https://github.com/cqu20141693/WJTExperience/tree/iot-gateway)
#### Nifi
1. mqtt
2. js/python脚本处理
3. JDBC
4. PLC
```
modbus
plc
s7
OPC UA

```
5. ftp
6. file
7. http
8. sql
9. kafka

#### 数采网关

### [iot](https://github.com/cqu20141693/WJTExperience/tree/iot)
#### device-link
##### MQTT Server
##### TCP Server
##### UDP Server
##### WebSocket
##### SIP Server
##### Coap
##### OPC UA


##### Domain Event
领域事件，主要是平台支持的领域业务模型，用于区分平台事件模型，将各种领域事件转化为平台物模型事件。
1. 事件上报
2. 数据上报
3. 命令回复
4. 属性读取回复
5. 子设备消息

##### Protocol Codec
将采集到设备数据解析为平台的领域模型，通过领域事件触发平台能力。

#### rule-engine
平台组件构建业务数据流转。
##### EventBus
支持数据订阅，构建订阅树，事件发布时对平台事件数据按主题和订阅路由分发。
##### Websocket
支持数据实时订阅
#### 系统配置
##### 数据源配置
##### 通知配置
#### 设备管理
1. 设备模版
设备基础模版包括：物模型，场景联动
```
TSL : 物模型
```
2. 设备分组
通过设备分组对设备进行分类管理，方便搜索和查看
3. 设备标签
对设备进行标签管理
4. 设备影子
设备的影子状态，包括属性状态(可读写)，时序状态（上报最新值），下行功能函数（具有有效期）
5. 地理位置
通过主动设置设备地理位置或者通过属性上报设备地理位置管理设备地理位置信息，可以将地理位置信息进行展示；
同时可以对地理位置进行搜索和查找。
6. 物实例（物模型到设备变成了实例）

7. 场景联动（告警规则体现）
``` 
触发，转换，执行
```
8. 设备模拟器
模拟设备连接平台，可以进行数据上报，下发指令
9. 设备配置
``` 
 认证配置
 拓扑配置 
```
10. ota 管理
11. 设备日志
12. 视频设备管理
``` 
利用SIP服务进行视频设备的控制，同时利用开源流媒体服务器SRS进行流的接受和转发。
```
#### 三方适配
#### OpenAPI
#### device-operation
设备操作模块，主要是支持对设备进行指令下发和响应处理。
#### 时序数据库
1. mongodb
2. influxdb
当平台数据的保留时间有限，且支持的并发写入数量在压测范围内。
3. cassandra
支持集群，水平扩展
#### 多云交互
提供一个消息中间件进行数据的透传转发到另一个平台，可以实现数据汇总分析和展示
#### 属性规则计算
数据在平台进行脚本计算后作为上报的最终值在事件总线中流转，可以实现数据的简单计算。
比如a的规则为a*10，则在数据总线中传播的数据为a*10，比如b属性为a*10，则a上报属性
后，会自动为b生成一个事件在总线中传播。
#### 场景联动
通过订阅设备数据进行规则过滤触发，数据映射，执行动作（告警，设备联动）
1. 触发
```
支持上线下事件
支持设备属性，批量属性
支持设备事件

```
2. 过滤
```
周期性触发，比如一段事件内只能触发一次
次数触发，比如连续n次有m次触发，就触发最后一次数据
时间抖动触发，比如3秒内发生n次触发，只会触发一次。
数据过滤，比如属性a的值大于0小于100
```
3. 数据映射
``` 
当数据触发时，此时的数据上下文实体可以进行键映射，比如把deviceId-> id

```
4. 执行动作
```
告警： 短信，邮件，语音，站内信，钉钉，微信等
设备联动： 获取设备属性，设备下发指令
```
#### 系统指标监控和告警
1. 并发支持设备在线数
``` 
每个长连接socket占用内存10k，10连接在1G左右，但是需要考虑文件句柄数限制，一个是操作系统，一个是容器。 
```
2. 同一时间支持并发设备上线数
``` 
 
```
3. 并发支持上数量
4. 上数全流程时间指标和告警
5. 
#### 流计算
做数据实时统计
#### 数据统计分析
#### SAAS应用
#### 用户管理
#### 认证管理

### [docker](https://github.com/cqu20141693/WJTExperience/tree/docker)
### [ansible](https://github.com/cqu20141693/WJTExperience/tree/ansible)
### [netty](https://github.com/cqu20141693/WJTExperience/tree/netty)
### [spring](https://github.com/cqu20141693/WJTExperience/tree/spirng)
### [spring boot](https://github.com/cqu20141693/WJTExperience/tree/spring-boot)
### [spring cloud](https://github.com/cqu20141693/WJTExperience/tree/spring-cloud)
#### Nacos Registry
#### Nacos Config
#### sentinel
#### openfeign
#### gateway
#### loadbalancer
#### Seata

### [nginx](https://github.com/cqu20141693/WJTExperience/tree/nginx)
### [mysql](https://github.com/cqu20141693/WJTExperience/tree/mysql)
### [mybatis](https://github.com/cqu20141693/WJTExperience/tree/mybatis)
### [redis](https://github.com/cqu20141693/WJTExperience/tree/redis)
### [elasticsearch](https://github.com/cqu20141693/WJTExperience/tree/elasticsearch)
### [cassandra](https://github.com/cqu20141693/WJTExperience/tree/cassandra)
### [kafka](https://github.com/cqu20141693/WJTExperience/tree/kafka)
### [rabbitmq](https://github.com/cqu20141693/WJTExperience/tree/rabbitmq)
### [pulsar](https://github.com/cqu20141693/WJTExperience/tree/pulsar)
### [flink](https://github.com/cqu20141693/WJTExperience/tree/flink)
### [SMS](https://github.com/cqu20141693/WJTExperience/tree/sms)
#### 阿里云短信
### [SSO](https://github.com/cqu20141693/WJTExperience/tree/sso)
#### 阿里云
#### 华为云
#### minio
### [定时任务](https://github.com/cqu20141693/WJTExperience/tree/scheduler)
### [zookeeper](https://github.com/cqu20141693/WJTExperience/tree/zookeeper)
1. etcd
### java