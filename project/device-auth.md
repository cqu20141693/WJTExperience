### device auth
提供业务级的鉴权接口和鉴权工件接口
#### api 鉴权
主要实现原理：
``` 
提供的功能：
1. 一个signatureToken可以重复使用。用于api的调用，一次计算后，后续都可以重复使用
平台通过设备信息获取鉴权Token两次MD5得到signatureToken，直接比较token.

2. 一个一定时间内有效的signatureToken,主要用于媒体播放token。signature:timestamp
平台通过设备信息获取鉴权Token，首先时校验时间是否在有效时间内，在则将token：timestamp 进行HmacSha256 签名对比

3. 一个一次有效的signatureToken,主要用于MQTT client登录，signature:timestap:nonce
平台通过设备信息获取鉴权token,首先校验时间是否在一个小时内，然后判断是否存在nonce，并存储nonce（redis script）,
然后将token:timestamp:nonce 进行HmacSHA256签名对比，需要保证一个nonce在一个小时内不能重复，同时timestamp必须
是一个小时内的请求。通过保证同一个时间段内的nonce不重复，不同时间段内nonce不同，得到签名也就不同

4. 需要支持过密签名SM3
实现了一个签名工厂用于根据不同的签名生成不同的签名实现，签名实现都是实现了签名接口

5. 需要支持AES对称加密和过密SM4对称加密
通过实现一个加密工厂根据不同的加密方式进行加密实现，加密实现了加密接口，实现了加密和解密两个行为。

6. MQTT登录实现了明文登录，签名登录，签名加密登录三种模式鉴权校验，分别需要支持HmacSHA256/AES和过密方式
通过设计登录密码开头为D:,DHMacSHA256,DCHMacShHA256 分别表示设备明文，设备签名，设备加密登录，
加密登录；不同的密码前缀使用不同的签名方式和加密方式，从而实现多种鉴权方式和加密算法。

7. 维护设备全局路由表，包括添加，刷新，删除
主要需要使用redis lua script 进行检查和数据操作


```