## redis doc

- [redis doc](#redis-doc)
    - [redis 常用命令和使用规范](#redis-常用命令和使用规范)
    - [redis hash 冲突怎么办？](#redis-hash-冲突怎么办)
    - [redis 为什么快？](#redis-为什么快)
    - [Redis大key问题讨论及解决方案](#redis大key问题讨论及解决方案)
    - [过期策略和内存淘汰策略](#过期策略和内存淘汰策略)
        - [过期策略](#过期策略)
        - [内存淘汰策略](#内存淘汰策略)
    - [数据类型](#数据类型)
        - [string](#string)
        - [list](#list)
        - [zset](#zset)
        - [hash](#hash)
        - [set](#set)
        - [bitmap](#bitmap)
        - [HyperLogLog](#hyperloglog)
        - [GEO](#geo)
        - [stream](#stream)
    - [高级特性](#高级特性)
        - [scan and pipeline](#scan-and-pipeline)
        - [事务和script](#事务和script)
        - [编码](#编码)
            - [ziplist](#ziplist)
    - [redis sentinel](#redis-sentinel)
    - [redis cluster](#redis-cluster)
        - [集群原理](#集群原理)
        - [gossip](#gossip)
    - [hash，一致性hash,hash solt](#hash一致性hashhash-solt)
    - [redis 持久化和主从复制](#redis-持久化和主从复制)
        - [持久化](#持久化)
        - [主从复制](#主从复制)
    - [数据库缓存一致性](#数据库缓存一致性)
    - [redis 分布式锁](#redis-分布式锁)
    - [redis bigKeys & hotKeys](#redis-bigkeys--hotkeys)
    - [缓存雪崩，击穿，穿透](#缓存雪崩击穿穿透)
    - [redis MQ](#redis MQ)

### redis 常用命令和使用规范

[常用命令及其时间复杂度](http://blog.caoxl.com/2018/11/28/Redis-Time-Complexity/)

### redis hash 冲突怎么办？

```
Redis 通过链式哈希解决冲突：也就是同一个 桶里面的元素使用链表保存。但是当链表过长就会导致查找性能变差可能，所以 Redis 为了追求快，使用了两个全局哈希表.
开始默认使用 hash 表 1 保存键值对数据，哈希表 2 此刻没有分配空间。当数据越来多触发 rehash 操作，则执行以下操作：

1. 给 hash 表 2 分配更大的空间；
2. 将 hash 表 1 的数据重新映射拷贝到 hash 表 2 中；
3. 释放 hash 表 1 的空间。
值得注意的是,将 hash 表 1 的数据重新映射到 hash 表 2 的过程中并不是一次性的,采用了渐进式 rehash，每次处理客户端请求的时候，
先从 hash 表 1 中第一个索引开始，将这个位置的 所有数据拷贝到 hash 表 2 中，就这样将 rehash 分散到多次请求过程中，避免耗时阻塞。

```

### redis 为什么快？

```
1. 基于内存操作
2. 采用多路复用IO网络模型，增加了网络处理能力
3. 使用单线程执行事务：基于内存操作，cpu不是性能的瓶颈；减少了线程切换消耗；减少了对共享资源的同步原语消耗
4. 高效的数据结构：SDS动态字符串，hashTable(string,hash,set),linkedList(list),zipList(list,hash,zset),skiplist(zset),quicklist,intset(set)

1. SDS动态字符串: 具有free(buf中空闲空间),len(buf中已使用空间),buf(存储空间)三个属性
SDS 与 C 字符串区别和优势：

O(1) 时间复杂度获取字符串长度
C 语言字符串布吉路长度信息，需要遍历整个字符串时间复杂度为 O(n)，C 字符串遍历时遇到 '\0' 时结束。
SDS 中 len 保存这字符串的长度，O(1) 时间复杂度。

空间预分配
SDS 被修改后，程序不仅会为 SDS 分配所需要的必须空间，还会分配额外的未使用空间。

惰性空间释放
当对 SDS 进行缩短操作时，程序并不会回收多余的内存空间，而是使用 free 字段将这些字节数量记录下来不释放，
后面如果需要 append 操作，则直接使用 free 中未使用的空间，减少了内存的分配。

二进制安全
在 Redis 中不仅可以存储 String 类型的数据，也可能存储一些二进制数据。
二进制数据并不是规则的字符串格式，其中会包含一些特殊的字符如 '\0'，在 C 中遇到 '\0' 则表示字符串的结束，但在 SDS 中，标志字符串结束的是 len 属性。

2. zipList 压缩列表
压缩列表是 List 、hash、 sorted Set 三种数据类型底层实现之一。

3. linkedList双端列表
Redis List 数据类型通常被用于队列、微博关注人时间轴列表等场景。
Redis 的链表实现的特性可以总结如下：

双端：链表节点带有 prev 和 next 指针，获取某个节点的前置节点和后置节点的复杂度都是 O（1）。
无环：表头节点的 prev 指针和表尾节点的 next 指针都指向 NULL，对链表的访问以 NULL 为终点。
带表头指针和表尾指针：通过 list 结构的 head 指针和 tail 指针，程序获取链表的表头节点和表尾节点的复杂度为 O（1）。
带链表长度计数器：程序使用 list 结构的 len 属性来对 list 持有的链表节点进行计数，程序获取链表中节点数量的复杂度为 O（1）。

quicklist 是 ziplist 和 linkedlist 的混合体，它将 linkedlist 按段切分，每一段使用 ziplist 来紧凑存储，多个 ziplist 之间使用双向指针串接起来。

4. Redis hash 字典
Redis 整体就是一个 哈希表来保存所有的键值对，无论数据类型是 5 种的任意一种。哈希表，本质就是一个数组，每个元素被叫做哈希桶，
不管什么数据类型，每个桶里面的 entry 保存着实际具体值的指针。

5. skiplist跳表
redis 跳表中主要的元素有header(头节点，增删改查询起点),tail,length(元素个数),level(最大的level);
每个节点都有前置节点指针，和level[](层级索引，记录了每一级的下一个节点),score和数据。

当通过score查询时，时间复杂度为log N,最坏每个元素都在同一级，时间复杂度为O(N)。当直接通过数据查询时，通过字典查询时间复杂度为O(1);

```

### Redis大key问题讨论及解决方案

问题

```
1.读写bigkey会导致超时严重，甚至阻塞服务。
2.大key相关的删除或者自动过期时，会出现qps突降或者突升的情况，极端情况下，会造成主从复制异常，Redis服务阻塞无法响应请求。

大key的发现与删除方法
1、redis-rdb-tools工具。redis实例上执行bgsave，然后对dump出来的rdb文件进行分析，找到其中的大KEY。
2、redis-cli --bigkeys命令。可以找到某个实例5种数据类型(String、hash、list、set、zset)的最大key。memory usage命令和lazyfree机制，
3、自定义的扫描脚本，以Python脚本居多，方法与redis-cli --bigkeys类似。
```

解决方案

```
将大key进行拆分到多个key中，可以本地做hash分key;
如果大key是因设计不合理，则重新设计key，将数据分散，比如冷热数据放置一起导致key一致不过期。
```

### 过期策略和内存淘汰策略

#### 过期策略

```
定时过期：每个设置过期时间的key都需要创建一个定时器，到过期时间就会立即清除。该策略可以立即清除过期的数据，对内存很友好；但是会占用大量的CPU资源
去处理过期的数据，从而影响缓存的响应时间和吞吐量。
惰性过期：只有当访问一个key时，才会判断该key是否已过期，过期则清除。该策略可以最大化地节省CPU资源，却对内存非常不友好。极端情况可能出现大量的过期
key没有再次被访问，从而不会被清除，占用大量内存。
定期过期：每隔一定的时间，会扫描一定数量的数据库的expires字典中一定数量的key，并清除其中已过期的key。该策略是前两者的一个折中方案。通过调整定时
扫描的时间间隔和每次扫描的限定耗时，可以在不同情况下使得CPU和内存资源达到最优的平衡效果。(expires字典会保存所有设置了过期时间的key的过期时间数据，
其中key是指向键空间中的某个键的指针，value是该键的毫秒精度的UNIX时间戳表示的过期时间。键空间是指该Redis集群中保存的所有键。)

Redis同时使用了惰性过期和定期过期两种过期策略。但是Redis定期删除是随机抽取机制，不可能扫描删除掉所有的过期Key。因此需要内存淘汰机制。

定期删除：每隔一定的时间，会扫描一定数量的数据库的expires字典中一定数量的key，并清除其中已过期的key。
Redis 默认会每秒进行十次过期扫描（100ms一次），过期扫描不会遍历过期字典中所有的 key，而是采用了一种简单的贪心策略。
1.从过期字典中随机 20 个 key；
2.删除这 20 个 key 中已经过期的 key；
3.如果过期的 key 比率超过 1/4，那就重复步骤 1；

惰性过期：只有当访问一个key时，才会判断该key是否已过期，过期则清除。该策略可以最大化地节省CPU资源，却对内存非常不友好。
极端情况可能出现大量的过期key没有再次被访问，从而不会被清除，占用大量内存。
Redis同时使用了惰性过期和定期过期两种过期策略。但是Redis定期删除是随机抽取机制，不可能扫描删除掉所有的过期Key。因此需要内存淘汰机制。
```

#### 内存淘汰策略

```
lru,lfu,random 和volatile 和allKey的组合
volatile-ttl和noeviction
Redis的内存淘汰策略是指在Redis的用于缓存的内存不足时，怎么处理需要新写入且需要申请额外空间的数据。

1. no-eviction：当内存不足以容纳新写入数据时，新写入操作会报错。
2. allkeys-lru：当内存不足以容纳新写入数据时，在键空间中，移除最近最少使用的key。
3. allkeys-lfu：从所有键中驱逐使用频率最少的键
4. allkeys-random：当内存不足以容纳新写入数据时，在键空间中，随机移除某个key。
5. volatile-lru：当内存不足以容纳新写入数据时，在设置了过期时间的键空间中，移除最近最少使用的key。
6. volatile-random：当内存不足以容纳新写入数据时，在设置了过期时间的键空间中，随机移除某个key。
7. volatile-lfu：从所有工件了过期时间的键中驱逐使用频率最少的键
8. volatile-ttl：当内存不足以容纳新写入数据时，在设置了过期时间的键空间中，有更早过期时间的key优先移除。
```

### 数据类型

#### string

1. 分布式锁 set NX EX
2. 统计 incr
3. 限流 incr
4. session(路由表): key-value 缓存

#### list

```
list-max-ziplist-entries 512
list-max-ziplist-value 64
```

#### zset

有序集合zset就是基本数据类型之一，并且每个member都带有score（可用于排序），因此很适合在打赏日榜、近一周收益这类场景中运用。 有序集合对象的编码可以是ziplist或者skiplist。
同时满足以下条件时使用ziplist编码： 元素数量小于128个 所有member的长度都小于64字节 以上两个条件的上限值可通过zset-max-ziplist-entries和zset-max-ziplist-value来修改
ziplist编码的有序集合使用紧挨在一起的压缩列表节点来保存，第一个节点保存member，第二个保存score。 ziplist内的集合元素按score从小到大排序， score较小的排在表头位置。

其他情况skiplist： skiplist编码的有序集合底层是一个命名为zset的结构体，而一个zset结构同时包含一个字典和一个跳跃表。
跳跃表按score从小到大保存所有集合元素。而字典则保存着从member到score的映射，这样就可以用O(1)的复杂度来查找member对应的score值。
虽然同时使用两种结构，但它们会通过指针来共享相同元素的member和score，因此不会浪费额外的内存。

跳表结构：最左边的示 zskiplist 结构，该结构包含以下属性：

header ：指向跳跃表的表头节点。 其结构是一个Node tail ：指向跳跃表的表尾节点。其结构是一个Node level ：记录目前跳跃表内，层数最大的那个节点的层数（表头节点的层数不计算在内）。 length
：记录跳跃表的长度，也即是，跳跃表目前包含节点的数量（表头节点不计算在内）。

zskiplistNode 结构， 该结构包含以下属性：

1. 层（level）：节点中用 L1 、 L2 、 L3 等字样标记节点的各个层， L1 代表第一层， L2 代表第二层，以此类推。每个层都带有两个属性：前进指针和跨度。
   前进指针用于访问位于表尾方向的其他节点，而跨度则记录了前进指针所指向节点和当前节点的距离。在上面的图片中，连线上带有数字的箭头就代表前进指针， 而那个数字就是跨度。当程序从表头向表尾进行遍历时，访问会沿着层的前进指针进行。
2. 后退（backward）指针：节点中用 BW 字样标记节点的后退指针，它指向位于当前节点的前一个节点。后退指针在程序从表尾向表头遍历时使用。
3. 分值（score）：各个节点中的 1.0 、 2.0 和 3.0 是节点所保存的分值。在跳跃表中，节点按各自所保存的分值从小到大排列。
4. 成员对象（obj）：各个节点中的 o1 、 o2 和 o3 是节点所保存的成员对象。

redis level 最大值为32，level 随机生成，并会记录当前最大level 每次查找数据时，先通过跳表查找节点： 以查找某个score为例，header节点取出最高层对应的next节点比较，如果next节点数据小于score,
则读取next几点的next 1几点比较，如果next1 数据大于score，则去next节点下一层next2指针，如果next2数据小于score，则将next2按照next进行寻找 直到第一层不满足或者需要到score节点

数据插入：首先是随机生成level,然后构建结构体，然后查找对应level的位置并更新next指针和前一个节点的next指针为当前节点 数据删除相反

#### hash

当哈希对象可以同时满足以下两个条件时， 哈希对象使用 ziplist 编码： 哈希对象保存的键值对数量小于 512 个； 哈希对象保存的所有键值对的键和值的字符串长度都小于 64 字节； 这两个条件的上限值是可以修改的，
具体请看工件文件中关于 hash-max-ziplist-value 选项和 hash-max-ziplist-entries 选项的说明。 保存了同一键值对的两个节点总是紧挨在一起， 保存键的节点在前， 保存值的节点在后；
先添加到哈希对象中的键值对会被放在压缩列表的表头方向， 而后来添加到哈希对象中的键值对会被放在压缩列表的表尾方向。

不能满足这两个条件的哈希对象需要使用 hashtable 编码。

1. 统计限流：incr
2. 缓存关联数据

#### set

1. 缓存过滤：真实场景时设备上传数据流统计
2. 缓存全局路由表基础信息：用于恢复路由信息

#### bitmap

1. setBit,getBit,bitCount,bitFiled 获取，设置，统计，局部统计
2. 存储空间小，适用于确定数字访问的统计
3. 周活跃，月活跃度，实际用于千万级设备状态更新过滤
4. 布隆过滤

```
原理： redis client通过guava的hash函数计算位置数组，获取位置是否都为true；其判断不存在一定不存在，其判断存在不一定存在
用于推荐过滤消息是否已经推送给用户
```

#### HyperLogLog

1. add ,delete ,union,count
2. 存储空间小，默认100以内统计精确，可用于海量数据统计，存在误差，可以设置误差
3. 一般用于访问量统计，实际场景千万级设备在线情况和api访问情况

#### GEO

```
地理位置类型，可以实现地理位置查询和计算
```

#### stream

1. 发布订阅：实际应用场景：数据和事件推送，其他服务集成订阅数据

### 高级特性

#### scan and pipeline

1. scan

```
2. allKey scan
3. hash scan
4. set scan
```

2. pipeline

```
使用场景一个业务操作中需要多个redis网络请求时，可以放到一个请求中去获取数据
```

#### 事务和script

1. 事务

```
Redis使用 MULTI 命令标记事务开始，它总是返回OK。MULTI执行之后，客户端可以发送多条命令，Redis会把这些命令保存在队列当中，
而不是立刻执行这些命令。所有的命令会在调用EXEC 命令之后执行。 

如果不调用EXEC，调用 DISCARD 会清空事务队列并退出事务。

```

2. script

```
lua脚本进行原子性操作。redis保证脚本在一个事务中完成

路由表更新，路由表添加，当路由表不存在时进行add,存在则需要先下线已存在的设备
bloom add，fliter脚本

```

#### 编码

##### ziplist

```
ziplist结构：
zlbytes：表示ziplist占用字节数，在执行resize操作时使用
zltail：表示最后节点的偏移量，也是避免了整体遍历list
zllen：表示ziplist节点个数（节点数超过65535,zllen字段值无效,需要遍历才能得到真实数量）
entry: 压缩列表包含的各个节点，节点的长度由节点保存的内容决定。一个压缩列表可以包含任意多个节点（entry）
zlend：表示ziplist结束的标识符

ziplist节点数据结构（抽象）：
每个压缩列表节点都由previous_entry_length、encoding、content三个部分组成（不是指实际结构体的字段）
previous_entry_length：前一个节点的长度，用来由后向前遍历，根据前一个节点的长度，可能需要一个或五个字节。
encoding：记录节点保存的数据类型和数据长度。
content：节点保存的数据内容。
ziplist节点结构体（和上面的有点不一样）:

//ziplist.c
typedef struct zlentry {
    unsigned int prevrawlensize, prevrawlen;
    unsigned int lensize, len;
    unsigned int headersize;
    unsigned char encoding;
    unsigned char *p;
} zlentry;
```

### redis sentinel

[sentinel](https://blog.51cto.com/u_8939110/2429771)

哨兵模式是redis高可用方案，哨兵会和主从架构的redis节点建立连接，通过发送及进行状态获取，节点信息获取，和节点主从切换。 Sentinel作用： 1）Master状态检测
2）如果Master异常，则会进行Master-Slave切换，将其中一个Slave作为Master，将之前的Master作为Slave。
3）Master-Slave切换后，master_redis.conf、slave_redis.conf和sentinel.conf的内容都会发生改变，即master_redis.conf中会多一行slaveof的工件，sentinel.conf的监控目标会随之调换。

Sentinel工作方式（每个Sentinel实例都执行的定时任务） 1）每个Sentinel以每秒钟一次的频率向它所知的Master，Slave以及其他 Sentinel 实例发送一个PING命令。
2）如果一个实例（instance）距离最后一次有效回复PING命令的时间超过 own-after-milliseconds 选项所指定的值，则这个实例会被Sentinel标记为主观下线。
3）如果一个Master被标记为主观下线，则正在监视这个Master的所有 Sentinel 要以每秒一次的频率确认Master的确进入了主观下线状态。
4）当有足够数量的Sentinel（大于等于工件文件指定的值）在指定的时间范围内确认Master的确进入了主观下线状态，则Master会被标记为客观下线。 5）在一般情况下，每个Sentinel
会以每10秒一次的频率向它已知的所有Master，Slave发送 INFO 命令。 6）当Master被Sentinel标记为客观下线时，Sentinel 向下线的 Master 的所有Slave发送
INFO命令的频率会从10秒一次改为每秒一次。 7）若没有足够数量的Sentinel同意Master已经下线，Master的客观下线状态就会被移除。 若 Master重新向Sentinel
的PING命令返回有效回复，Master的主观下线状态就会被移除。

sentinel的三个定时任务： 每1秒每个sentinel对其他sentinel和redis节点执行ping操作，心跳检测。当监测到master节点回复故障（超时回复），则会标记位SDOWN主观下线，后续会通过gossip流言协议
询问其他的sentinel是否主管下线，当超过配额数，则会标记为客观下线，这时sentinel的领导者会选择slave升级为master，修改其他slave节点集群master工件（故障转移）。
每10秒每个sentinel会对master和slave执行info命令，目的是发现slave结点，确定主从关系。
每2秒每个sentinel通过master节点的channel交换信息（pub/sub）。master节点上有一个发布订阅的频道(sentinel:hello)。 sentinel节点通过__sentinel__:
hello频道进行信息交换(对节点的"看法"和自身的信息)，达成共识.

### redis cluster

集群目的： 解决高并发，高可用，大数据量

#### 集群原理

```
主从模式解决了主库读取压力，读写分离；也可以实现容灾备份，但是存在主库宕机后，无法主动恢复，不能高可用

sentinel 模式在主从模式上增加了监视能力，当主节点宕机后，sentinel会自动的选择slave为主，并切换其他slave主节点信息，实现了高可用。
但是存在内存瓶颈，当缓存的数据量大后，单机的内存不够，同时对主从复制和持久化造成性能问题

cluster 模式：集群节点分为多master。每个master负责一部分的缓存槽，实现数据的分片存储，解决了内存瓶颈。
同时每个master也可以具有slave,可以实现读写分离；当master宕机后，slave可以自主选举为master，实现高可用。

```

1. 集群元数据有哪些，怎么存储的

```
元数据：
 所有的master slave 列表和关系
 hash solt 对应master节点的映射

每个节点都有一份全量的元数据。

```

2. 集群元数据怎么保证的一致性

```
元数据的存储方式一般有集中式和gossip两种方式；
集中式时效性高，但是存在单点瓶颈

gossip 方式通过将节点间通过连通图的方式建立连接，每个节点连接固定的节点，这样节点间的网络开销是比较小的。

redis采用的gossip 协议方式进行节点间通信，通过节点间信息互传的方式实现数据的最终一致性，但是存在延迟。
断口为服务端口+10000


```

3. 集群如何进行slave选举master

```
当master宕机后，slave节点发现master节点宕机，则会等待一定时间
500+rand(0-500)+offset rank*1000 毫秒的时间后，将集群的周期+1,发起推举自己为master
然后其他的master接受信息后会进行回复，当有半数以上的master节点同意，则发送消息通知自己为新的master节点，其他的节点更新solt映射，salve节点修改同步master节点.


```

4. Redis集群为什么至少需要三个master节点，并且推荐节点数为奇数？

```
集群至少三个节点是因为，要保证集群的可用性，当一个master节点宕机后，需要能够选举slave节点为新的master,需要保证半数以上的master节点同意，
1个和2个情况下宕机后不能进行选举

奇数时因为选举只需要半数以上，当为偶数时，挂了一半集群就不可用，计数也是挂同样的节点不可用，所以计数情况下已经可以保证集群在可用性下的最小资源消耗。
如果需要更多资源，用偶数也是可以的
```

5. cluster client 如何请求的？

```
集群客户端 
```

#### gossip

```
 Gossip 协议实现的,从类型上来说其分为了四种，分别是：
Meet 消息，用于通知新节点加入。就好像上面例子中提到的新节点上线会给老节点发送 Meet 消息，表示有“新成员”加入。
Ping 消息，这个消息使用得最为频繁，该消息中封装了自身节点和其他节点的状态数据，有规律地发给其他节点。
Pong 消息，在接受到 Meet 和 Ping 消息以后，也将自己的数据状态发给对方。同时也可以对集群中所有的节点发起广播，告知大家的自身状态。
Fail 消息，如果一个节点下线或者挂掉了，会向集群中广播这个消息。

```

### hash，一致性hash,hash solt

1. hash

```
hash 算法：可以将同一个hash的数据进行区分，可以用于缓存服务数据分流和分而治之的场景

```

2. 一致性Hash

```
一致性hash: hash算法可以实现缓存命中缓存服务，但是当缓存服务变更时，则会出现大面积的缓存失效；一致性hash 通过将0-2^32-1个hash桶形成hash环，
每个缓存节点根据ip:porthash后都会存在一个hash值，hash值一定在hash环上。当client需要进行key查询时，首先计算key的hash值，然后顺时针寻找hash环
上的节点进行请求，这样当节点减少时，只会影响节点逆时针上的hash值的key，其他的缓存依旧命中。当节点增加时，只会将节点逆时针到最近的其他结点间的hash
值的key的缓存。影响范围是有限的。
一致性hash存在缓存倾斜的可能：可能缓存节点hash值很接近，导致存在某个节点管理了绝大多数的hash值。此时就需增加虚拟节点，一般每个节点增加32个以上，
虚拟节点通过ip:port#1,ip:port#2... 这种结构计算得到虚拟节点。
实现方式：client 拥有原始的节点ip1:port,ip2:port。通过hash得到hash1->ip1:port,hash2->ip2:port;增加虚拟节点，得到hashN->ip1:port#N,hashM->
ip2:port#M,将节点安装hash值升序排序并map。当需要访问缓存时，计算key的hashcode,查找第一个比hashcode大的节点，对地址进行split("#")得到地址进行请求

```

3. hash solt

```
将每个服务器映射一部分hash槽，将映射元数据存储到每个客户端和服务端，当客户端请求时，通过计算hash solt，映射hash槽到服务端进行请求。

```

### redis 持久化和主从复制

https://segmentfault.com/a/1190000038761633

#### 持久化

```
AOF的优点
AOF可以更好的保护数据不丢失，一般AOF会以每隔1秒，通过后台的一个线程去执行一次fsync操作，如果redis进程挂掉，最多丢失1秒的数据。
AOF以appen-only的模式写入，所以没有任何磁盘寻址的开销，写入性能非常高。
AOF日志文件的命令通过非常可读的方式进行记录，这个非常适合做灾难性的误删除紧急恢复。
AOF的缺点
对于同一份文件AOF文件比RDB数据快照要大。
AOF开启后支持写的QPS会比RDB支持的写的QPS低，因为AOF一般会工件成每秒fsync操作，每秒的fsync操作还是很高的。
数据恢复比较慢，不适合做冷备。

RDB优点
适合大规模的数据恢复。
如果业务对数据完整性和一致性要求不高，RDB是很好的选择。
RDB 缺点
数据的完整性和一致性不高，因为RDB可能在最后一次备份时宕机了。
备份时占用内存，因为Redis 在备份时会独立创建一个子进程，将数据写入到一个临时文件，最后再将临时文件替换之前的备份文件。所以要考虑到大概两倍的数据膨胀性。
针对RDB不适合实时持久化的问题，Redis提供了AOF持久化方式来解决
```

RDB和AOF 对比

|            |        |              |
| ------------ | -------- | -------------- |
| 命令       | RDB    | AOF          |
| 启动优先级 | 低     | 高           |
| 体积       | 小     | 大           |
| 恢复速度   | 快     | 慢           |
| 数据安全性 | 丢数据 | 根据策略决定 |
| 轻重       | 重     | 轻           |

#### 主从复制

1. 作用和原理

```
1. 确保数据安全；做数据的热备，作为后备数据库，主数据库服务器故障后，可切换到从数据库继续工作，避免数据的丢失。
2. 提升I/O性能；随着日常生产中业务量越来越大，I/O访问频率越来越高，单机无法满足，此时做多库的存储，有效降低磁盘I/O访问的频率，提高了单个设备的I/O性能。
3. 读写分离；使数据库能支持更大的并发。

复制原理：https://segmentfault.com/a/1190000039242024
从节点与主节点建立连接后，需要从节点使用命令PSYNC <replid> <offset>向主节点发起同步请求，从节点会从server.cache_master中取出两个参数构建命令，
然后发给主节点。注意：如果该从节点是全新的，从未与任何主节点进行主从复制，那么会使用特殊的命令：PSYNC ? -1。
主节点接收到命令，解析请求中的复制ID和offset，然后去判断当前请求是否可以使用部分同步。
能够使用部分同步需要满足以下两个条件（这里先不考虑主从切换导致的多复制ID情况）：
  1 复制ID与主节点的复制ID一致；
  2 复制偏移量offset必须在backlog_off和offset的范围之间；
不能使用部分同步，就不得不使用全量同步了。


```

2. 全量复制

```
用于初次复制或其它无法进行部分复制的情况，将主节点中的所有数据都发送给从节点，是一个IO非常重型的操作，当数据量较大时，会对主从节点和网络造成很大的开销。

Redis 内部会发出一个同步命令，刚开始是 psync 命令，psync ? -1表示要求 master 主机同步数据。
主机会向从机发送 runid （redis-cli info server）和 offset，因为 slave 并没有对应的 offset，所以是全量复制。
从机会保存主机的基本信息save masterinfo。
主节点收到全量复制的命令后，执行bgsave（异步执行），在后台生成RDB文件（快照），并使用一个缓冲区（称为复制缓冲区）记录从现在开始执行的所有命令。
主机send RDB发送RDB文件给从机。
发送缓冲区数据。
刷新旧的数据，从节点在载入主节点的数据之前要先将老数据清除。
加载RDB文件将数据库状态更新至主节点执行bgsave时的数据库状态和缓冲区数据加载。
```

3. 部分复制

```
部分同步其实是以全量同步为基础（得到复制ID），用复制积压缓冲区blockLog中的缓存命令做命令重放的增量同步逻辑，不过受制于复制积压缓冲区的容量，
它可容忍的范围是有限的。这与持久化机制的AOF混合持久化如出一辙，也与mysql中主从复制的Binlog思路不谋而合。

根据发送的runID和offset 是否匹配主节点和blockLog，匹配则找到offset之后的数据重新发送一次
```

4. 命令传播

```
当完成同步操作之后，master-slave便会进入命令传播阶段，此时master-slave的数据是一致的。

当maste执行完新的写命令后，会通过传播程序把该命令追加至复制积压缓冲区，然后异步地发送给slave。slave接收命令并执行，同时更新slave维护的复制偏移量offset
如果slave可以收到每条传播指令，并执行成功，便可以保持与master的数据一致状态。但是master并不等待slave节点的返回，master与slave是通过网络通信，
由于网络抖动等因素，命令传播过程不保证slave真正接收到，那如何在传播阶段确保主从数据一致呢？

在命令传播阶段，每隔一秒，slave节点向master节点发送一次心跳信息，命令格式为REPLCONF ACK <offset>。
这里可以想象，slave接受的传播命令都是连续的，如果不是，则不会执行下面的命令，则心跳是相同的ack

如果出现多次一样的ack，表示有数据丢失，从blockLog 中拿出ack之后的数据重新发送
```

### 数据库缓存一致性

[双写一致性问题](https://segmentfault.com/a/1190000040130178)

1. cache aside + expire

```
先写数据库，再删除缓存+过期时间
当读请求打到达时，缓存为空，则读数据库，写缓存+expire，返回数据。后续请求直接都缓存

存在的问题为更细数据库后，缓存删除失败，此时因数据库事务已提交，不可回滚。第一依靠expire最终一致性，或者发送事件通知其他程序删除。
// 提示一下，read write through 通过将缓存组件化或者服务化提供一样的功能，能方便缓存管理和优化，但是提高了开发的成本。
```

1. canal 订阅binlog 进行缓存写入

```
对于请求不大的接口：
先删除缓存，再写数据库；
当读请求到达时，此时缓存为空，请求会打到数据库，不会写缓存。
当cannal订阅binlog 写入缓存后，缓存有效

请求量大的接口：
直接写数据库，
当cannal订阅binlog 写入缓存后，缓存数据最终一致

```

### redis 分布式锁

[分布式锁]（https://mp.weixin.qq.com/s/aXsmLxApZdJQfmUaEs9zJg)

1. set nx +expire ：原子性问题

```
redis 2.6
问题：
两个命令无法保证原子性，过期设置失败，则锁不能具有超时机制，当释放锁失败，锁永久不可用

可以利用lua script 解决。
```

2. SET key value [EX seconds|PX milliseconds] [NX|XX] [KEEPTTL] ：存在锁释放错乱问题，出现进程并行获取锁

```
redis 支持在set nx 同时设置过期时间，一个原子命令

问题：
当A获取锁后，执行时间过长，导致锁超时释放，B进程获取锁
A执行完任务后，进行锁释放，删除锁key，此时的锁已经不属于A,出现锁释放错乱。此时C就能提前获取到锁

解决：
添加锁的时候，设置一个UUID 作为值，释放的时候先获取锁的值，对比UUID,相同则释放删除锁key.
两个操作非原子性，可以用lua script 保证。
```

3. redisson 解决上面的所有问题，并提供了多种api和功能,比如限流器，锁等 a. JUC API

```
源码中加锁/释放锁操作都是用lua脚本完成的，封装的非常完善，开箱即用。

https://zhuanlan.zhihu.com/p/135864820

Redis 实现分布式锁主要步骤
1. 指定一个 key 作为锁标记，存入 Redis 中，指定一个 唯一的用户标识 作为 value。
2. 当 key 不存在时才能设置值，确保同一时间只有一个客户端进程获得锁，满足 互斥性 特性。
3. 设置一个过期时间，防止因系统异常导致没能删除这个 key，满足 防死锁 特性。
4. 当处理完业务之后需要清除这个 key 来释放锁，清除 key 时需要校验 value 值，需要满足 只有加锁的人才能释放锁 。

获取可重入锁为例：使用lua脚本实现
需要参数锁key, 有效时长argu1,客户端锁标识argu2

使用exits 命名判断key是否存在，
不存在： 
则hincr key argu2    : argu2 为加锁进程唯一标识
pexpire key argu1   : 设置锁的过期时间，毫秒级
return nil;

存在： 
则判断hexists key argu2 是否存在
则hincr key argu2    : 锁的可重入性
pexpire key argu1   : 设置锁的过期时间，毫秒级
return nil;

否则
retrun pttl key ： 返回锁的有效期

返回 null 则说明加锁成功，返回一个数值，则说明已经存在该锁，ttl 为锁的剩余存活时间。

源码实现过程：
首先是调用tryAcquire();
如果返回null, 加锁成功，返回，
如果ttl不为空，首先是判断是否需要等待获取，
需要，则订阅锁释放事件通知channel,
并使用await超时方法： 触发条件两个：一个是锁key被删除，一个是等待超时没有被删除
如果超时，则取消订阅并返回失败
如果订阅到锁解除事件
则重新获取锁，如果返回值空，则获取到锁，如果返回值不为空，则判断是否还需要等待获取
如果需要，则while循环获取锁，依旧是等待key删除释放或者等待time超时，


解除锁的过程：
首先是判断锁是否存在，不存在返回nil,可能已经过期
存在则hincr key argu1 -1
并判断返回结果，如果为0，则需要删除key,并发送锁key删除事件channel，返回1
不为0，则执行pexpire 命令重置过期时间，返回0



```

b. 对主从，哨兵，集群等模式支持

```

```

4. redLock

```
redLock算法虽然是需要多个实例，但是这些实例都是独自部署的，没有主从关系。
RedLock作者指出，之所以要用独立的，是避免了redis异步复制造成的锁丢失，

红锁算法认为，只要(N/2) + 1个节点加锁成功，那么就认为获取了锁， 解锁时将所有实例解锁。流程为：

1. 顺序向N个节点请求加锁
2. 根据一定的超时时间来推断是不是跳过该节点
3. N/2+1个节点加锁成功并且花费时间小于锁的有效期
4. 认定加锁成功

```

### redis bigKeys & hotKeys

[bigKeys&hotKeys](https://mp.weixin.qq.com/s/FPYE1B839_8Yk1-YSiW-1Q)

### 缓存雪崩，击穿，穿透

### redis MQ

[redis MQ](https://www.cnblogs.com/zhuminghui/p/14781231.html)
