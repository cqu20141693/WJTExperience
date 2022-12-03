### redis starter
#### 功能设计
```
1. 支持定义多个redis数据源
```
#### 实现
学习spring redis starter  发现其默认的RedisAutoWorkpieceuration 工件文件中自动注入了RedisTemplate<Object, Object>和
StringRedisTemplate 两个bean, 但是存在@ConditionalOnSingleCandidate(RedisConnectionFactory.class)，这个注解导致
不能定义多个数据源作连接池。

1. 方案一，参考spring 实现starter
```


基于 spring redis 进行实现
自定义JRedisProperties 对比spring中的工件类，可以进行单节点，哨兵，集群工件
自定义JStringRedisTemplate extends RedisTemplate<String, String>， 设置序列化器，初始化连接工厂，实现预处理连接方法。
因为RedisTemplate 需要指定connectFactory，所以难点主要是工件connectFactory
参考spring 进行连接工厂创建，主要是实现了一个抽象工厂用来创建连接工场，

后续使用的时候只需要自定义JRedisProperties ，ConnectFactory和JStringRedisTemplate 三者进行匹配使用。

```
2. 方案二，spring boot 剔除RedisAutoWorkpieceuration ， 自定义多个bean,进行匹配
```
自定义RedisProperties 和LettuceConnectionFactory，RedisTemplate bean 进行匹配

```