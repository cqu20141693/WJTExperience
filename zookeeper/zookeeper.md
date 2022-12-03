- [zookeeper](#zookeeper)
  - [特点和应用场景](#特点和应用场景)
  - [zookeeper 服务端状态](#zookeeper-服务端状态)
  - [架构](#架构)
    - [zookeeper 数据读取流程](#zookeeper-数据读取流程)
    - [zookeeper 数据写入流程](#zookeeper-数据写入流程)
    - [ZAB 崩溃恢复](#zab-崩溃恢复)
    - [ZAB 原子广播](#zab-原子广播)
  - [数据模型](#数据模型)
    - [ZNode](#znode)
    - [Watch](#watch)
## zookeeper
### 特点和应用场景
```
zookeeper是一个分布式场景下提供的一个保证原子性，数据一致性的服务，
可用于元数据工件管理，命名服务，高可用方案，选主，分布式锁

```
### zookeeper 服务端状态
```
Looking : 竞选状态
Leader
Following
Observering
```

### 架构
zookeeper分为客户端和服务端，client通过session连接到服务端，可实现数据读取，写入，事件监听能力。
#### zookeeper 数据读取流程
```
zookeeper 读请求每个节点都可以完成，但是包保证严格顺序性，需要使用sync, 请求会转发到leader进行查询返回。
如果不使用sync,则当前节点查询数据，可能出现写请求再读请求前面，但是事务并没有完成。导致读取不到写请求数据。
```
#### zookeeper 数据写入流程
```
zookeeper写请求Following 会将请求转发到leader，leader开始生成事务原子广播日志，写完成后返回数据到client。
```
#### ZAB 崩溃恢复
[崩溃恢复](https://mp.weixin.qq.com/s/Oi5hRITyXTiBxM9zfEK8LQ)
```
leader 选举：
算法： FastLeaderElection 是paxos算法变种
选举过程：获取投票信息->决策->通知决策->选举结果
投票信息：epoch(选举周期)，zxid(事务ID)，sid(服务id)
决策：选择选举周期大->zxid大->sid大
决策通知： 当B接受A的投票时，会发送通知<B,A>给其他的节点
在集群创建时，每个节点都处于Looking状态(竞选状态)，此时每个节点会首先投票给自身，然后发起投票<A,A>并携带信息，
集群其他节点在接受到A的投票信息后，会进行决策和决策通知，同时其他Looking节点也会发起投票，直到最终出现半数的节点
支持一个节点或者收到所有节点的投票信息，选举结束。

日志同步：
被支持节点开始通知自己为leader,其他following节点收到后，会发送自己的事务信息
leader决策如果Leader的最新提交的日志zxid比Follower的最新日志的zxid大，那就将多的日志发送给Follower，让他补齐；
如果Leader的最新提交的日志zxid比Follower的最新日志的zxid小，那就发送命令给Follower，将其多余的日志截断；
如果两者恰好一样，那什么都不用做。

集群进入可用状态。
```
#### ZAB 原子广播
```
1. 客户端首先向zookeeper任意节点发起写请求（事务）。
2. 如果接收的节点是Fellower/Observer类型，就将请求转发给Leader节点。
3. Leader节点接收到消息之后对消息进行处理
  1. Leader节点对每条消息（事务）生成一个对应的zxid（全局唯一，递增）
  2. 将带有zxid的消息包装成一个proposal转发给所有的Follower节点。
4. Follower将proposal这个事务写到磁盘，将结果（ack）返回给leader。
5. Leader节点统计ack数量。
  1.如果有一半以上的节点返回成功，则向所有的Follower节点（包括自己）发送commit消息提交事务，并且给Observer发送INFORM消息。
  2.如果ack数量小于一半则发送rollback消息进行事务回滚。
6. 最后返回给客户端

```
### 数据模型
[数据模型](https://xie.infoq.cn/article/6afee96c9e4f78f3adeaeec21)
#### ZNode
```
zooKeeper 的数据模型，在结构上和标准文件系统的非常相似，拥有一个层次的命名空间，都是采用树形层次结构，ZooKeeper 树中的每个节点
被称为—Znode。和文件系统的目录树一样，ZooKeeper 树中的每个节点可以拥有子节点。

Znode 由 3 部分组成:
① stat：此为状态信息, 描述该 Znode 的版本, 权限等信息
② data：与该 Znode 关联的数据
③children：该 Znode 下的子节点

Znode 有两种，分别为临时节点和永久节点。
```
#### Watch
```
分布式数据发布/订阅功能
ZooKeeper 允许客户端向服务端注册一个 Watcher 监听，当服务端的一些事件触发了这个 Watcher，那么就会向指定客户端发送一个事件
通知来实现分布式的通知功能。触发事件种类很多，如：节点创建，节点删除，节点改变，子节点改变等。
```