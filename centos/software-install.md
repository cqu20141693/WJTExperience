### software install

#### rpm 安装卸载

``` 
卸载mysql
rpm -qa |grep -i mysql
yum remove mysql-community-common-5.7.20-1.el7.x86_64
```

#### yum online安装

``` 
备份 yuam 源
 mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.backup
 
下centos7 源
wget -O /etc/yum.repos.d/CentOS-Base.repo https://mirrors.aliyun.com/repo/Centos-7.repo

运行 yum makecache 生成缓存

yum update和yum upgrade的功能都是一样的，都是将需要更新的package更新到源中的最新版。
唯一不同的是，yum upgrade会删除旧版本的package，而yum update则会保留(obsoletes=0)。
生产环境中建议使用yum update，防止因为替换，导致旧的软件包依赖出现问题。
```

##### rabbitmq

``` 
yum update -y

yum install epel-release -y

yum install erlang -y

yum install rabbitmq-server -y

rabbitmq-plugins enable rabbitmq_management
rabbitmqctl add_user admin apaas2022
rabbitmqctl set_user_tags admin administrator
rabbitmqctl set_permissions -p / admin ".*" ".*" ".*"
#重启rabbitmq
systemctl restart rabbitmq-server.service
```
##### golang
``` 
# 先卸载旧的golang
yum remove golang
# 然后找到最新版本
https://golang.google.cn/dl/
# 下载安装
cd /usr/local/src	
wget https://golang.google.cn/dl/go1.18.4.linux-amd64.tar.gz
tar -zxvf go1.18.4.linux-amd64.tar.gz -C /usr/local/
# 增加配置文件
vim /etc/profile

export GOROOT=/usr/local/go
export PATH=$PATH:$GOROOT/bin
export GOPATH=/opt/go
export PATH=$PATH:$GOPATH/BIN
# 应用改变
source /etc/profile
# 查看版本
go version
# 依赖下载速度过慢
go env -w GO111MODULE=on
go env -w GOPROXY=https://goproxy.cn,direct

```
