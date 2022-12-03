- [netty](#netty)
  - [架构](#架构)
    - [高性能](#高性能)
      - [线程池](#线程池)
      - [内存池](#内存池)
    - [Reactor](#reactor)
    - [零拷贝](#零拷贝)
      - [ByteBuf](#bytebuf)
      - [CompositeByteBuf](#compositebytebuf)
      - [FileChannel](#filechannel)
  - [NioEventLoopGroup](#nioeventloopgroup)
    - [线程池组](#线程池组)
    - [EventExecutorChooser](#eventexecutorchooser)
  - [NioEventLoop](#nioeventloop)
    - [run func](#run-func)
    - [SelectorProvider](#selectorprovider)
    - [SelectStrategy](#selectstrategy)
    - [RejectedExecutionHandler](#rejectedexecutionhandler)
    - [queueFactory](#queuefactory)
  - [ServerBootstrap](#serverbootstrap)
    - [Boss & work NioEventLoopGroup](#boss--work-nioeventloopgroup)
    - [NioServerSocketChannel](#nioserversocketchannel)
    - [NioChannelOption](#niochanneloption)
    - [child NioChannelOption](#child-niochanneloption)
    - [Handler](#handler)
      - [ServerBootstrapAcceptor](#serverbootstrapacceptor)
    - [child Handler](#child-handler)
  - [Pipeline](#pipeline)
    - [Handler](#handler-1)
  - [FastThreadLocal](#fastthreadlocal)
  - [FastThreadLocalThread](#fastthreadlocalthread)
    - [InternalThreadLocalMap](#internalthreadlocalmap)
    - [FastThreadLocal](#fastthreadlocal-1)
  - [ByteBuf](#bytebuf-1)
    - [内存回收](#内存回收)
    - [CompositeByteBuf](#compositebytebuf-1)
    - [](#)
    - [ByteBufAllocator](#bytebufallocator)
    - [PooledByteBufAllocator](#pooledbytebufallocator)
      - [method 分析](#method-分析)
      - [PoolThreadLocalCache extends FastThreadLocal<PoolThreadCache>](#poolthreadlocalcache-extends-fastthreadlocalpoolthreadcache)
      - [PoolThreadCache](#poolthreadcache)
      - [PoolArena](#poolarena)
    - [UnPooledByteBufAllocator](#unpooledbytebufallocator)
  - [各种协议编解码器](#各种协议编解码器)
    - [redis,mqtt,http ...](#redismqtthttp-)
    - [ProtobufDecoder](#protobufdecoder)
    - [LineBasedFrameDecoder](#linebasedframedecoder)
    - [StringDecoder](#stringdecoder)
    - [DelimiterBasedFrameDecoder](#delimiterbasedframedecoder)
    - [FixedLengthFrameDecoder](#fixedlengthframedecoder)
    - [LengthFieldBasedFrameDecoder](#lengthfieldbasedframedecoder)

## netty
### 架构
异步，事件驱动，高性能，可扩展，易使用，稳定性强
#### 高性能
##### 线程池
##### 内存池
#### Reactor 
```
单线程模型：EventLoopGroup 只包含一个 EventLoop，Boss 和 Worker 使用同一个EventLoopGroup；
多线程模型：EventLoopGroup 包含多个 EventLoop，Boss 和 Worker 使用同一个EventLoopGroup；
主从多线程模型：EventLoopGroup 包含多个 EventLoop，Boss 是主 Reactor，Worker 是从 Reactor，
它们分别使用不同的 EventLoopGroup，主 Reactor 负责新的网络连接 Channel 创建，然后把 Channel 注册到从 Reactor。
```
#### 零拷贝
##### ByteBuf
1. 直接内存使用
```
Netty的接收和发送ByteBuffer采用DIRECT BUFFERS，使用堆外直接内存进行Socket读写，不需要进行字节缓冲区的二次拷贝。如果使用传统的堆内存（HEAP BUFFERS）进行Socket读写，JVM会将堆内存Buffer拷贝一份到直接内存中，然后才写入Socket中。相比于堆外直接内存，消息在发送过程中多了一次缓冲区的内存拷贝。
```

##### CompositeByteBuf
1. 组合
```
Netty提供了组合Buffer对象，可以聚合多个ByteBuffer对象，用户可以像操作一个Buffer那样方便的对组合Buffer进行操作，避免了传统通过内存拷贝的方式将几个小Buffer合并成一个大的Buffer。
```
2. 切片
##### FileChannel
1. transferTo
```
Netty的文件传输采用了transferTo方法，它可以直接将文件缓冲区的数据发送到目标Channel，避免了传统通过循环write方式导致的内存拷贝问题。
```
### NioEventLoopGroup
#### 线程池组
数组线程
NioEventLoop
#### EventExecutorChooser
线程选择器，当线程数量为2的幂时，时候 与& 进行选择
### NioEventLoop
属性selector, 用于管理channel
#### run func
for(;;) 
1. 执行channel 事件
查看selector中准备好的SelectorKey,然后根据绑定的AbstractNioChannel 处理不同的注册事件，ACCEPT,READ,WRITE,CONNECT
通过pipeline进行处理io数据
2. run task
默认工件ioRatio为50，表示任务执行事件为io时间的一半
3. epoll 空循环检测
默认为512次空事件通知，则重建selector进行channel管理

#### SelectorProvider
绑定线程的Selector,并对selector进行了优化，底层存储SelectorKey使用数组替代Set.
#### SelectStrategy
hasTasks ? NioEventLoop.this.selectNow() : SelectStrategy.SELECT;
#### RejectedExecutionHandler
#### queueFactory

### ServerBootstrap
#### Boss & work NioEventLoopGroup
#### NioServerSocketChannel
在初始化时
处理channelRead事件，事件消息为child channel
首先是工件childHandler
工件tcp参数
使用workGorup 注册channel
#### NioChannelOption
#### child NioChannelOption
#### Handler
##### ServerBootstrapAcceptor
#### child Handler

### Pipeline
#### Handler

### FastThreadLocal
### FastThreadLocalThread
```
public class FastThreadLocalThread extends Thread {

    private InternalThreadLocalMap threadLocalMap;
    ...
}
```
#### InternalThreadLocalMap
```
class InternalThreadLocalMap{
  // 当线程不是FastThreadLocalThread使用ThreadLocal
  ThreadLocal<InternalThreadLocalMap> slowThreadLocalMap = new ThreadLocal<InternalThreadLocalMap>();
   private Object[] indexedVariables;  // 存储本地实例数据，利用数组效率读取更高
   AtomicInteger nextIndex = new AtomicInteger(); index 计数器，每次创建FastThreadLocal时返回index
}
```
1. get()
```
当线程为FastThreadLocalThread时，直接获取其内部属性map；
否则通过内部ThreadLocal<InternalThreadLocalMap> 属性进行查找获取
```
2. nextVariableIndex()
```
FastThreadLocal 创建时调用该方法初始化内部index属性
```
#### FastThreadLocal
```
 class FastThreadLocal<V> {
 private final int index;
 }
```
1. get
```
 InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.get();
  Object v = threadLocalMap.indexedVariable(index);
  if (v != InternalThreadLocalMap.UNSET) {
      return (V) v;
  }
  // initialize
V v = null;
  try {
      v = initialValue();
  } catch (Exception e) {
      PlatformDependent.throwException(e);
  }

  threadLocalMap.setIndexedVariable(index, v);
  addToVariablesToRemove(threadLocalMap, this);
  return v;
```

### ByteBuf
[引用计数]()
netty 通过引用计数算法统计对象的引用数，由开发者自己进行管理，调用newBuffer时初始化为1，调用retain增加引用，release释放引用；当引用为0时释放对象和内存，可进行对象池回收和内存池回收。
![内存管理图](../images/netty内存管理.png)
[内存管理](https://mp.weixin.qq.com/s/wh9sBajNczrKWtexf1beUA)
[内存分配](https://miaowenting.site/2020/02/09/Netty%E5%86%85%E5%AD%98%E6%B1%A0%E5%8C%96%E7%AE%A1%E7%90%86/)
Netty 内存管理分为池化和非池化，对象使用对象池管理；Netty的内存池(或者称内存管理机制)涉及的是针对内存的分配和回收，而Netty的ByteBuf的回收则是另一种叫做对象池的技术(通过Recycler实现)
池化管理内存减少内存的申请和回收开销，从而提高内存使用率。
Netty 利用Arena组件实现内存的分配，每次先向操作系统申请最小单位为chunk,默认16M，Arena内存申请和分配的最小page 8k,
netty 通过完全二叉树管理chunck下的page,正好2048个page,树的高度为11，使用Memory和depth两个数组通过堆的方式表示完全二叉树，如果parent索引为i，子节点为2i,2i_+1;
Arena分配内存时，首先时计算需求内存对用的树的深度，比如6K,则时11层，然后找到11层的开始索引进行遍历(同一层级节点在数组上连续)，进行分配（如果判断是否可以分配，每个Memroy节点value值存储其层数，当被分配后，其value+1，并parent直到根节点都会+1，因此只需要判断当前节点value值是否为层数即可）
Netty 为了更好的利用和分配内存，page 细分为subPage 16B-4K，netty将内存块管理细分为tiny[16-512],small[512,4k],normal[8K,16M],huge >16m
当申请内存类型为small时，使用subpage分配，normal时使用page，huge时直接申请内存（大于了chunk）,
Netty 通过Arena分配8K内存后，如果是tiny,small内存是，则会将8k内存均分为请求内存对应的大小，然后放入线程本地链表中，并通过bitMap表示是否分配，进行分配回收管理。
通过PoolThreadLocalCache实现线程绑定内存池Arena组件,通过将多个Arena分配到不同的线程减少内存分配竞争，并将分配的内存进行线程本地缓存，再次分配时可以使用，不会出现竞争。(默认处理器数*2个Arena)
Netty将Arena申请的chunk内存按照使用率分配到6个链表，一个是init(0-25),000(1-50),025(25-75),050(50-100),075(75-100),100(100),各个链表之间会进行相互移动，比如申请时在init,到达30移动到000，当到达70移动到025，到达90到050，到达100时到100，当100释放到80时放到075，当释放到0时放到init;每次分配内存的时候让使用率小的chunk先分配内存，提高chunk的利用率。
Netty PooledByteBufAllocator 在分配内存时，先通过本地缓存队列(回收的内存)进行分配，如果分配成功直接返回，缓存分配失败，则通过Arena申请内存分配
[对象池回收](https://learn.lianglianglee.com/)
当内存使用完后，回收内存到内存池中，对象使用完了放回对象池中。

#### 内存回收
内存回收使用的是引用计数方式；
开发者在申请内存时，增加对象的引用计数，每次使用完成后主动释放，引用计数减少，直到引用计数为0时，
会调用内存分配器进行内存回收。回收后的内存放到内存池中，对象放到对象池中。
内存池通过树管理内存，可以直接查找是否可以分配内存，当内存分配后，则标记为已分配，对象池则是直接获取对象
#### CompositeByteBuf
#### 
#### ByteBufAllocator
ByteBufAllocator，内存分配器，负责为ByteBuf分配内存， 线程安全。
#### PooledByteBufAllocator
池化内存分配器，默认的ByteBufAllocator，预先从操作系统中申请一大块内存，在该内存上分配内存给ByteBuf，可以提高性能和减小内存碎片。
##### method 分析
1. ByteBuf newDirectBuffer(int initialCapacity, int maxCapacity)
```
// 通过ThreadLocalCache获取PoolThreadCache，并通过cache获取Arena
// 通过arena.allocate()进行内存分配
PoolThreadCache cache = threadCache.get();
        PoolArena<ByteBuffer> directArena = cache.directArena;

        final ByteBuf buf;
        if (directArena != null) {
            buf = directArena.allocate(cache, initialCapacity, maxCapacity);
        } else {
            buf = PlatformDependent.hasUnsafe() ?
                    UnsafeByteBufUtil.newUnsafeDirectByteBuf(this, initialCapacity, maxCapacity) :
                    new UnpooledDirectByteBuf(this, initialCapacity, maxCapacity);
        }

        return toLeakAwareBuffer(buf);
```
##### PoolThreadLocalCache extends FastThreadLocal<PoolThreadCache>
  
##### PoolThreadCache
线程级缓存内存缓存，用于管理PoolArena，MemoryRegionCache
```
class PoolThreadCache {
 
}

```
##### PoolArena
[PoolArena分析](https://hengyoush.github.io/netty/2019/07/26/netty-memorypool-concept.html)
内存池，用于分配内存。
netty基于JEMalloc算法构建了一套高性能的内存分配机制, 下面依据netty源码来介绍一下其原理.
最顶层的是PoolArena, Arena下分为Chunk, Chunk下分为Page, Page又可分为SubPage. Arena下的Chunk被划分为了几个ChunkList(双向链表)
Chunk 是 Netty 向操作系统申请内存的单位，所有的内存分配操作也是基于 Chunk 完成的，Chunk 可以理解为 Page 的集合，每个 Chunk 默认大小为 16M。 一个Chunk默认分为2048个Page, 每个Page大小为16 * 1024 * 1024 / 2048 = 8192 = 8K, Page由一个完全平衡二叉树管理, 其中叶子为2048个Page.

使用内存池主要是减少内存的申请分配和释放，当第一次申请分配后，如果使用完，直接归还给内存池，当再次需要内存的时候，内存池直接将回收的内存分配使用。netty在16M以内使用Pool管理，大于则直接重新申请分配。
```
class PoolArena{

 PoolSubpage<T>[] tinySubpagePools； // 新版中已经直接使用small装载
 PoolSubpage<T>[] smallSubpagePools;  // subPage,用于分配8k以下的内存
 PoolChunkList<T> q050;
 PoolChunkList<T> q025;
 PoolChunkList<T> q000;
 PoolChunkList<T> qInit;
 PoolChunkList<T> q075;
 PoolChunkList<T> q100;
}
```
1. PooledByteBuf<T> allocate(PoolThreadCache cache, int reqCapacity, int maxCapacity)
```
由PooledByteBufAllocator作为入口，重头梳理一遍内存申请的过程:

1. PooledByteBufAllocator.newHeapBuffer()开始申请内存
2. 获取线程本地的变量PoolThreadCache以及和线程绑定的PoolArena
3. 通过PoolArena分配内存，先获取ByteBuf对象(可能是对象池回收的也可能是创建的)，在开始内存分配
4. 分配前先判断此次内存的等级，尝试从PoolThreadCache的找相同规格的缓存内存块使用，没有则从PoolArena中分配内存
5. 对于Normal等级内存而言，从PoolChunkList的链表中找合适的PoolChunk来分配内存，如果没有则先像OS申请一个PoolChunk，在由PoolChunk分配相应的Page
6. 对于Tiny和Small等级的内存而言，从对应的PoolSubpage缓存池中找内存分配，如果没有PoolSubpage，线会到第5步，先分配PoolChunk，再由PoolChunk分配Page给PoolSubpage使用
7. 对于Huge等级的内存而言，不会缓存，会在用的时候申请，释放的时候直接回收
8. 将得到的内存给ByteBuf使用，就完成了一次内存申请的过程
```
#### UnPooledByteBufAllocator
非池化内存分配器，每次都从操作系统中申请内存。


### 各种协议编解码器
#### redis,mqtt,http ...
#### ProtobufDecoder
#### LineBasedFrameDecoder
通过在包尾添加回车换行符 \r\n 来区分整包消息；
#### StringDecoder
字符串解码器；
#### DelimiterBasedFrameDecoder
特殊字符作为分隔符来区分整包消息；
#### FixedLengthFrameDecoder
报文大小固定长度，不够空格补全；
#### LengthFieldBasedFrameDecoder
指定长度来标识整包消息，通过在包头指定整包长度来约定包长。