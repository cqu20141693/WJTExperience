## spring
### spring Spring的优点
### 用到了哪些设计模式
```
工厂设计模式: BeanFactory 或 ApplicationContext 
单例设计模式: 单例bean
代理设计模式： AOP 使用，比如事务，切面
模板方法: 各种templete模板
观察者模式 ： 事件的发布订阅
责任链模式： spring security中的web 链
适配器模式：
策略模式：
建造者模式：

```
### IOC和AOP的理解？
[IOC](https://blog.nowcoder.net/n/ac852374db244c5885cd07a65c8b622c)
```

```
AOP
```

```
### Spring AOP 和 AspectJ AOP 有什么区别?
```
 Spring AOP 属于运行时增强，而 AspectJ 是编译时增强。 Spring AOP 基于代理(Proxying)，而 AspectJ 基于字节码操作(Bytecode Manipulation)。
 Spring AOP 已经集成了 AspectJ ，AspectJ 应该算的上是 Java 生态系统中最完整的 AOP 框架了。AspectJ 相比于 Spring AOP 功能更加强大，但是 Spring AOP 相对来说更简单，
```
### BeanFactory、FactoryBean和ApplicationContext的区别？
[beanFactory](https://blog.csdn.net/hk1052606583/article/details/121143786)

### 解释Spring支持的几种bean的作用域？
[bean 作用域](https://blog.csdn.net/meism5/article/details/90446682)
### Bean的生命周期和扩展点？
[bean生命周期](https://developer.aliyun.com/article/601034)
```
bean 实例化
属性设置：Aware ,设置beanName,设置beanFactory,事件publiser
初始化：
使用：
销毁
```
### Spring如何处理单例Bean的循环依赖
[循环依赖](https://www.cnblogs.com/lifullmoon/p/14452887.html)
```
通过提前暴露和三级缓存实现：

主要通过再实例化bean,提前暴露单例bean，不等待bean的属性设置和初始化，将bean放入单例工厂map缓存中，
当bean依赖的对象也依赖bean时，先找单例bean缓存，找不到，找提前暴露的二级缓存，找不到则到提前暴露的单例工厂中去获取，
获取到将其放入提前暴露缓存，返回bean实现注入

但是不能解决构造器依赖的循环依赖，因为不能实例化。

使用工厂缓存是为了在多个循环依赖时比如B和C都依赖A,则只调用一次工厂方法进行初始化
为什么需要二级缓存，因为spring aop在对象初始化到singleton后会进行代理，在early阶段表示正在初始化阶段。
```


### spring 事务实现方式，传播规则和原理
``` 
实现方式：

编程式事务管理：这意味着你可以通过编程的方式管理事务，这种方式带来了很大的灵活性，但很难维护。
声明式事务管理：这种方式意味着你可以将事务管理和业务代码分离。你只需要通过注解或者XML工件管理事务。
```
```
传播规则:

PROPAGATION_REQUIRED: 支持当前事务，如果当前没有事务，就新建一个事务。这是最常见的选择。
PROPAGATION_SUPPORTS: 支持当前事务，如果当前没有事务，就以非事务方式执行。
PROPAGATION_MANDATORY: 支持当前事务，如果当前没有事务，就抛出异常。
PROPAGATION_REQUIRES_NEW: 新建事务，如果当前存在事务，把当前事务挂起。
PROPAGATION_NOT_SUPPORTED: 以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。
PROPAGATION_NEVER: 以非事务方式执行，如果当前存在事务，则抛出异常。
PROPAGATION_NESTED: 如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则进行与PROPAGATION_REQUIRED类似的操作。
```
```
实现原理：
通过统一的事务管理接口，实现事务管理器，对于编程事务，直接对事务进行创建和提交回滚；
对于声明事务注解，使用aop对事务方法进行代理增加事务创建和提交回滚等管理功能。
```