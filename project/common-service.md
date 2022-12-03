- [common-service](#common-service)
  - [响应模型和工具](#响应模型和工具)
  - [全局异常处理](#全局异常处理)
  - [FeignErrorDecoder](#feignerrordecoder)
  - [编解码和签名，加密工具](#编解码和签名加密工具)
    - [编解码](#编解码)
    - [签名，加解密工具](#签名加解密工具)
  - [mybatis 类型转换](#mybatis-类型转换)
  - [spirng 上下文，用于获取bean](#spirng-上下文用于获取bean)
  - [record](#record)
    - [RequestRecrod](#requestrecrod)
    - [OperationRecord](#operationrecord)
  - [公共工件](#公共工件)
    - [logback-spring.xml](#logback-springxml)
    - [spring boot、cloud公共工件](#spring-bootcloud公共工件)
### common-service
#### 响应模型和工具
1. 定义了CommonResult<T>，包含code,msg,data, 
2. 定义了错误码接口IErrorCode，并实现了功能更的错误码
CommonCodeType： SUCCESS，CALL_FAIL，PARAM_CHECK_ERROR，AUTHORITY_ERROR，BIZ_ERROR，UNKNOWN_ERROR
3. 定义了ResultUtil，用于快速生成成功响应和失败响应
#### 全局异常处理
1. 定义了CallException，用于请求调用异常
2. 定义了CommonBizException,用于请求处理业务异常
3. 定义了GlobalHttpCallExceptionHandler + @ControllerAdvice
```
将异常处理为公共响应模型数据，
将CallException使用CALL_FAIL
将BindException，ConstraintViolationException，ConstraintViolationException，转换为PARAM_CHECK_ERROR
将权限异常，转换为AUTHORITY_ERROR
将CommonBizException转为BIZ_ERROR
```
#### FeignErrorDecoder
将请求失败包装为CallException
#### 编解码和签名，加密工具
##### 编解码
主要用于平台数据的校验和转换

1. 定义了平台支持的数据类型
2. 定义了类型转换接口
```
提供了数据，字符串，byte[] 之间的相互转换和验证接口

    ConvertResponse<T> rawDataConvert(byte[] payload);

    T strDataConvert(String data);

    byte[] objectDataConvert(Object obj);

    String objectDataConvertStr(Object obj);

    boolean validType(Object obj);

```
3. 争对各种类型进行了实现
##### 签名，加解密工具
1. 支持md5,bcrypt,HMacSHA256，SM3签名
2. 支持AES,SM4 对称加密工具
#### mybatis 类型转换
主要用于mysql json字段的查询解析
1. JsonArrayTypeHandler ：jsonString 转JSONArray
2. JsonObjectTypeHandler : jsonString转JSONObject
3. JsonTypeHandler : jsonString 转JSON
#### spirng 上下文，用于获取bean
#### record
##### RequestRecrod
用于记录web请求日志，每个请求都会打印
1. 实现原理
```
通过aop 切面每一个controller ,controller 实现了BaseController,
aop的切点为target BaseController，切面为Around
try,catch
当请求进来时，会先通过spring RequestContextHolder 获取请求对象
然后回去请求的url和方法
记录开始时间
然后开始执行请求
记录结束时间，

finally :
记录结束时间，
判断请求时间间隔，如果大于1s(可工件) ,则直接打印info日志
```
##### OperationRecord
1. 功能设计
```
1. 支持注解式的进行操作日志采集
2. 支持扩展实现操作记录的send 和save 
3. 支持日志定时入库和count 触发入库，既是批量入库
4. 支持可以工件化count,time，and localCache

操作记录具有一定的延迟性，为定时任务周期时长
```
2. 实现
```
利用spring 一个请求在一个线程中执行，使用ThreadLocal 存储当前请求的日志记录模型到RecordOperationContext中，
利用aop 定义方法注解切点RecordOperation 注解，使用After 切面，提供业务开发处逻辑中记录日志模型到ThreadLocal
切面通过RecordOperationContext 获取日志模型，不存在直接放回，存在则调用发送日志接口，finally 清理ThreadLocal。

定义发送接口，接口需要实现将日志记录调用writer组件写入本地缓存，并尝试做真正的save 动作（条件为本地缓存数据量>最大数据量 || 时间是否超过最大时间(time1)）
执行cas 修改状态获取执行权限，然后在通过锁将缓存数据copy,并重置缓存集合为 空集合(使用volatile)；然后将copy 数据批量save.

write 组件会定时周期任务(time2)进行doWrite操作，检查数据量和时间

time1: 距离上一次写入的最大时间
time2: 定时检查时间
time2>time1

save 接口，实现批量的数据写入数据库(mysql，es)
```
3. 实现技术
```
利用了ThreadLocal 保存日志记录，方便解耦记录的创建和写入，在aop中需要对ThreadLocal 进行清理
利用了aop 实现切面进行记录的发送，

利用spi的方式，让使用者自定义发送和保留接口实现，
send 接口主要是为了解耦发送到本地缓存和save到数据量
save 接口主要是为了实现批量写入

```
#### 公共工件
##### logback-spring.xml
```
工件了平台日志，
工件了日志文件格式：服务名.log
日志文件路径为/work/app/服务名
日志文件格式
日志文件切分规则为7天，1GB
工件了appender： console,file
```
##### spring boot、cloud公共工件
1. 监控检测保留端口，info,health,promethus
2. spirng cloud loadbalance 工件