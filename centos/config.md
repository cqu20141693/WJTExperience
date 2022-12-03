### centos config
#### 关闭swap,开启TCP参数
``` 
vim /etc/sysctl.conf
在文件末尾添加：
vm.swappiness=10
net.ipv4.tcp_syncookies=1
net.ipv4.tcp_tw_reuse=1
net.ipv4.tcp_tw_recycle=1
net.ipv4.tcp_fin_timeout=30
# WARNING: IPv4 forwarding is disabled. Networking will not work.
net.ipv4.ip_forward=1
#需要重启生效或者运行sysctl -p 生效

```
#### 增大最大文件句柄数
``` 
vim /etc/secruriy/limits.conf
在文件末尾添加：
*soft nofile 512000
*hard nofile 512000
*soft nproc 512000
*hard nproc 512000

```
