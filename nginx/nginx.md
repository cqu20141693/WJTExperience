- [nginx](#nginx)
  - [Nginx应用场景？](#nginx应用场景)
  - [http](#http)
    - [server](#server)
      - [listen](#listen)
      - [location](#location)
  - [upstream](#upstream)
  - [log](#log)
  - [incloud](#incloud)
  - [示例](#示例)
## nginx
1. [blog](https://juejin.cn/post/6844904125784653837)
1. [blog](https://www.moyundong.com/netsite/nginx/1windows%E4%B8%8B%E5%AE%89%E8%A3%85nginx.html)
### Nginx应用场景？

http服务器: Nginx是一个http服务可以独立提供http服务。可以做网页静态服务器。
虚拟主机: 可以实现在一台服务器虚拟出多个网站，例如个人网站使用的虚拟机。
反向代理，负载均衡: 当网站的访问量达到一定程度后，单台服务器不能满足用户的请求时，需要用多台服务器集群可以使用nginx做反向代理。并且多台服务器可以平均分担负载，不会应为某台服务器负载高宕机而某台服务器闲置的情况。

### http
#### server
##### listen
##### location
```
a. prefix strings (pathnames) 

location /some/path/ {
    # 匹配/some/path/...
    # 其他工件
}

b.  regular expressions
A regular expression is preceded with the tilde (~) for case-sensitive matching, or the tilde-asterisk (~*) for case-insensitive matching
// 表示URL中任意地方匹配到.html字符串
location ~ \.html? {
    root 
}
// 表示结尾.html
location ~ \.html$ {
    root /work/console/html
}
```
1. root
```
root说明是静态资源,表明了资源的路径
```
2. proxy_pass
```
proxy_pass说明是动态请求，需要进行转发
location /api {
    proxy_pass 127.0.0.1:9000 # 网关服务器
}

```
3. server_name : 请求的Host，用于反向代理多个域名

4. 自定义设置HTTP HEADER
```

```
5. 缓存设置
```


```

### upstream
1. 配合location 负载均衡代理
```
Nginx负载均衡实现的策略有以下五种：
1 轮询(默认)
upstream backserver { 
 server 192.168.0.12; 
 server 192.168.0.13; 
} 

2 权重 weight
到的访问概率越高，主要用于后端每台服务器性能不均衡的情况下。其次是为在主从的情况下设置不同的权值，达到合理有效的地利用主机资源。
upstream backserver { 
 server 192.168.0.12 weight=2; 
 server 192.168.0.13 weight=8; 
} 

3 ip_hash( IP绑定)
每个请求按访问IP的哈希结果分配，使来自同一个IP的访客固定访问一台后端服务器，并且可以有效解决动态网页存在的session共享问题
upstream backserver { 
 ip_hash; 
 server 192.168.0.12:88; 
 server 192.168.0.13:80; 
} 

4 fair(第三方插件)
必须安装upstream_fair模块。
对比 weight、ip_hash更加智能的负载均衡算法，fair算法可以根据页面大小和加载时间长短智能地进行负载均衡，响应时间短的优先分配。
upstream backserver { 
 server server1; 
 server server2; 
 fair; 
} 

5、url_hash(第三方插件)
必须安装Nginx的hash软件包
按访问url的hash结果来分配请求，使每个url定向到同一个后端服务器，可以进一步提高后端缓存服务器的效率。
upstream backserver { 
 server squid1:3128; 
 server squid2:3128; 
 hash $request_uri; 
 hash_method crc32; 
} 

```


### log
``` 
# 日志格式化
    log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';
    # 日志使用main格式
    access_log  logs/access.log  main;
```
### incloud
``` 


```
### 示例
1. nginx.conf
``` 
# 如果 nginx 使用 root 用户运行，那么最好将 nginx.conf 中的 user 工件为 root (默认为 nobody)。不然，如果转发为目录时，如果目录权限不
# 是 777，那么将访问不到转发的目录而报 403 错误。
user root;
#表示引入 vhost 目录下的所有 .conf 结尾的文件内容
include vhost/*.conf 
```
```
user root;

```
