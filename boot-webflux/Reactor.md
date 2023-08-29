## 响应式编程

### Reactor

#### 接口

##### Publisher<T>

当存在订阅者时，通过Subscription将消息通知到订阅者消费数据

##### Subscriber<T>

订阅者通过Subscription发起对生产者流的订阅，事件通知消费

##### Subscription

订阅

##### Processor<T,R>

实现了Publisher和Subscriber，所以既可以发布，也可以订阅

#### Scheduler

调度器:用于任务异步线程执行

#### Sinks

#### StepVerifier
