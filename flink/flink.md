## flink

### deploy

[install and run](https://nightlies.apache.org/flink/flink-docs-release-1.15/docs/try-flink/local_installation/)

#### standalone

[集群standalone](https://blog.csdn.net/oMaFei/article/details/109575333)

[flink standalone](https://nightlies.apache.org/flink/flink-docs-release-1.14/docs/deployment/resource-providers/standalone/docker/)

##### docker

1. jobmanger

```
session 模式: 客户端提交任务，再调度任务
docker run --rm -d --name=jobmanager --network flink-network -p 8081:8081 -p 8088:8088 \
-e jobmanager.rpc.address: jobmanager flink:1.14.4-scala_2.11 jobmanager

application 模式: 启动时指定任务，只运行单一任务
https://nightlies.apache.org/flink/flink-docs-release-1.14/docs/deployment/resource-providers/standalone/docker/#app-cluster-yml
```

2. task manager

```
cluster:
docker run --rm -d --name=taskmanager --network flink-network --env JOB_MANAGER_RPC_ADDRESS=jobmanager flink:1.14.4-scala_2.11 taskmanager
```

##### 虚拟机

```

```

### flink 使用场景

#### 数据分析监测

1. 交易欺诈场景
2. 服务内存泄露监测： 将服务的内存信息通过flink做分析，监测内存变化
3. 用户账号异常： 通过分析用户的登录地址进行通知

#### 实时报表

1. real-time dashboard

#### 数据清洗

#### ETL

### flink CDC

### flink connector

### flink Table & SQL

### flink Gelly(图计算)

### flink CEP(复杂事件处理)

### flink ML

### flink Table Store

[Flink 机器学习库](http://flink.iteblog.com/dev/libs/ml/index.html)
