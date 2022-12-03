- [Go](#go)
  - [go 中常见的问题](#go-中常见的问题)
  - [go 基础面试题](#go-基础面试题)
  - [go 并发控制](#go-并发控制)
    - [sync mutex](#sync-mutex)
    - [sync atomic](#sync-atomic)
    - [sync atomic Value](#sync-atomic-value)
    - [sync WaitGroup](#sync-waitgroup)
    - [sync Once](#sync-once)
    - [sync Pool](#sync-pool)
    - [sync](#sync)
    - [chan](#chan)
      - [保证接受消息一定发生在发送之后](#保证接受消息一定发生在发送之后)
      - [生产消费模式](#生产消费模式)
      - [最大并发限制](#最大并发限制)
      - [select 操作多个chan](#select-操作多个chan)
    - [context 消息通知](#context-消息通知)
      - [请求链路传值](#请求链路传值)
      - [链路cancel通知，一定要调用](#链路cancel通知一定要调用)
      - [链路超时通知](#链路超时通知)
  - [go 程序初始化流程？](#go-程序初始化流程)
  - [go GC原理分析](#go-gc原理分析)
## Go

### go 中常见的问题
[Go语言常见坑](https://github.com/chai2010/advanced-go-programming-book/blob/master/appendix/appendix-a-trap.md)
[四个重要知识点](https://zhuanlan.zhihu.com/p/105554073)
### go 基础面试题
[基础面试题](https://zhuanlan.zhihu.com/p/353071138) 
[面试题](https://www.cnblogs.com/wpgraceii/p/10528183.html)
[面试题必备](https://mp.weixin.qq.com/s/tKuecP_dSAbldGTKb9-74Q)
### go 并发控制
[面向并发的内存模型](https://github.com/chai2010/advanced-go-programming-book/blob/master/ch1-basic/ch1-05-mem.md)
[sync 原语](https://segmentfault.com/a/1190000022545889)
#### sync mutex
#### sync atomic
#### sync atomic Value
#### sync WaitGroup
#### sync Once
#### sync Pool
#### sync 
#### chan
##### 保证接受消息一定发生在发送之后
##### 生产消费模式
##### 最大并发限制
##### select 操作多个chan
#### context 消息通知
##### 请求链路传值
##### 链路cancel通知，一定要调用
##### 链路超时通知

### go 程序初始化流程？
Go程序的初始化和执行总是从 main.main 函数开始的。但是如果 main 包里导入了其它的包，则会按照顺序将它们包含进 main 包里（这里的导入顺序依赖具体实现，
一般可能是以文件名或包路径名的字符串顺序导入）。如果某个包被多次导入的话，在执行的时候只会导入一次。当一个包被导入时，如果它还导入了其它的包，则先将
其它的包包含进来，然后创建和初始化这个包的常量和变量。然后就是调用包里的 init 函数，如果一个包有多个 init 函数的话，实现可能是以文件名的顺序调用，
同一个文件内的多个 init 则是以出现的顺序依次调用（init不是普通函数，可以定义有多个，所以不能被其它函数调用）。最终，在 main 包的所有包常量、包变量
被创建和初始化，并且 init 函数被执行后，才会进入 main.main 函数，程序开始正常执行。
![](../images/go初始化流程.png)
要注意的是，在 main.main 函数执行之前所有代码都运行在同一个 Goroutine 中，也是运行在程序的主系统线程中。如果某个 init 函数内部用 go 关键字启动了新
的 Goroutine 的话，新的 Goroutine 和 main.main 函数是并发执行的。

### go GC原理分析
[golang gc机制](https://www.zhihu.com/question/403065438/answer/1426765597)
[三色标记](https://www.jianshu.com/p/ff3d6da5d71a)