- [用户中心](#用户中心)
  - [authentic，authorize](#authenticauthorize)
    - [shiro and spring](#shiro-and-spring)
  - [web security](#web-security)
    - [cookie 安全](#cookie-安全)
    - [reference 验证](#reference-验证)
    - [csrf 防备，通过head携带请求的唯一session](#csrf-防备通过head携带请求的唯一session)
    - [跨域支持](#跨域支持)
    - [CSP，xss,reference,contentType 工件](#cspxssreferencecontenttype-工件)
  - [SSO 单点登录](#sso-单点登录)
    - [同父域](#同父域)
    - [不同父域](#不同父域)
  - [三方集成](#三方集成)
  - [集成三方登录](#集成三方登录)
    - [微信扫码登录](#微信扫码登录)
  - [消息通知](#消息通知)
### 用户中心
用户中心，提供用户注册，单点登录，session 身份认证，权限校验，web 安全,集成三方(微信)登录，三方登录集成用户体系
#### authentic，authorize 
#####  shiro and spring
1. 功能设计
```
1. 用户注册
2. 用户登录
3. 用户session 访问身份认证
4. 用户session 访问权限校验
5. 用户登出

```
2. 实现
```
使用shiro框架，一个轻量级的基于filter的框架，基本架构包括
Subject,其持有Token,具有登录登出接口
SecurityManager，其持有所有的Releam
Releam 分为Authenticate和authorize 两种，通过Support 接口标识处理的Token，第一种通过login Token 获取
平台的token, 通过工件的CredentialsMatcher进行匹配；第二种通过pricipal 获取用户角色和权限信息，框架进行角色和权限校验

1. 注册通过REST API的方式，入库前需要对密码进行hash再入库，这里为了支持密码升级和密码更高的安全性，设计了密码有签名方式
和签名数据共同组成，再进行密码匹配时，先通过解析签名方式对用户输入的密码进行签名进行比较。支持md5,bcrypt两种方式。
2. 登录通过工件login 不进行校验，接口进行构建Subject login; 添加PasswordReleam 进行身份认证，使用密码匹配器。
验证通过则回写jwtTOken 到cookie，

3. session 访问时：通过JWTFilter 获取cookie构建Token进行login，此时会进入JWTReleam 进行jwt验证，包括签名验证，有效期验证和refresh等。
4. 权限验证： 使用spring的aop 配合shiro 权限认证实现，  @Before + annotation
自定义权限注解RequirePermissions， 两个参数，一个是权限字符串数组，一个是逻辑表达符，支持and or
当方法添加权限注解后，aop 会执行切面进行权限认证， 切面通过注解中的字符串数组进行el 解析，主要是将
权限中的可变参数解析为传入参数的数据得到真正的权限集，然后调用shiro的Subject.permission 方法进入框架验证，
框架中具有用户信息，在通过spring的RequestContextHolder 获取请求中携带的farmId，再结合权限进行用户权限的查询，
shiro将查询的权限和解析出的权限集进行匹配，满足则通过，否则403.


 通过开启shiro权限注解，工件权限，当身份认证通过后，对jwt进行权限验证，判断角色是否满足。

```
3. jwt 设计
```
使用cookie 进行携带，具有同父级域的cookie可以被子域携带，SSO的一种实现
cookie安全使用httpOnly,security,sameSite

jwt 功能：
tokenExp: cookie携带的jwt的有效期
tokenRefreshExp: cookie 在浏览器端有效期
before: jwt token 最大的有效期，
userKey: 用户的唯一标识，用于用户信息获取
role: 用户角色，用于权限校验

当请求携带cookie时，before必须满足，不满足则直接401，满足时，
如果tokenExp满足，则通过，如果不满足，则看refreshExp是否满足，满足时，
则重新生成jwt,token+5天，refreshToken+5,但是都不能超过before, 

```
#### web security
安全都可以通过nginx 工件
##### cookie 安全
##### reference 验证
```
查看请求来源，
```
##### csrf 防备，通过head携带请求的唯一session
```
请求前，携带csrf header，后端获取用户session中的header,匹配放行
如果不匹配，返回401,重新登录，
用户登录的时候会返回一个csrf token, 后端会存储
前端也会存储，当不存在时，则前端需要去请求，获取数据

```
##### 跨域支持
```
option :是否为支持的域名,如果是，设置设置支持的orign,method,head,credentials和max-age，
简单请求和真实请求时，检查orign 域，检查method和header，通过则继续请求
如果不满足则直接403 拒绝请求
```
##### CSP，xss,reference,contentType 工件
```
 CSP : content-security-policy : 工件哪些脚本，url可以访问
 xss: 工件浏览器是否开启XSS保护，以及检查后的策略
 contentType: 主要是工件内容需要和content-type一致，防止出现mime攻击。导致前端对文件按照html解析执行脚本
```
#### SSO 单点登录
##### 同父域
```
使用统一的登录中心，登录成功后写入domain为顶级域域名，后续所有的子域都可以携带cookie进行访问
```
##### 不同父域
```
1. 通过前端存储进行应用间共享
2. 通过集成SSO 登录，当应用需要登录时，直接跳转到sso，
```
#### 三方集成
三方集成平台用户体系
1. 提供ACS： 认证中心服务，参考auth2
#### 集成三方登录
##### 微信扫码登录
1. 功能设计
```
1. 网站提供二维码，用户手机扫码授权登录
2. 用户授权登录后，自动绑定平台用户，通过建立三方用户关系表
3. 微信登录用户未绑定平台用户，跳转绑定页面，
```
2. 实现原理
```
首先再微信开发者平台注册账号，申请应用得到appKey,appSecret

1. 前端生成二维码，不断的通过js请求获取用户授权登录code,用户一授权，就能获取到
2. 然后前端使用code进行登录，
3. 后端通过code获取accessToken,
4. 后端根据token 获取用户信息，信息中具有unionid,openid, 和用户信息
5. 后端检查unionid是否绑定平台用户，绑定，则登录完成，写jwtToken cookie
6. 如果不满足，则返回thirdId, 前端跳转到用户绑定页面,
7. 前端输入用户注册信息或者用户的登录信息，获取用户的userId（已存在用户验证用户，注册则输入新用户信息，后端处理返回userId）
8. 前端通过userId，thirdId进行绑定
9. 后端进行用户绑定并返回jwt cookie
```
#### 消息通知
1. 功能设计
```
1. 支持触发消息通知，提供内部触发和外部触发两种方式
2. 支持查看通知
3. 支持修改通知状态

```