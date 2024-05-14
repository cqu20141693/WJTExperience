## VMWare
### 安装
1. [下载](https://www.vmware.com/cn/products/workstation-pro/workstation-pro-evaluation.html)
2. [安装](https://zhuanlan.zhihu.com/p/141033713)
3. [密钥](https://www.cnblogs.com/wind-under-the-wing/p/14122040.html)
### 安装centos
1. [下载centos](http://mirrors.aliyun.com/centos/7/isos/x86_64/)
2. vmware 创建虚拟机
[网络讲解](https://segmentfault.com/a/1190000024580532)
```
工件用户：cc/cc@123 , root 密码一致
选择镜像，工件网络
```
[VMware的三种网络类型](https://blog.csdn.net/taotongning/article/details/81477472)
[虚拟机Nat模式下设置静态ip](https://www.jianshu.com/p/6fdbba039d79)

#### NAT


3. 配置网卡 vi /etc/sysconfig/network-scripts/ifcfg-ens33
```
TYPE="Ethernet"
PROXY_METHOD="none"
BROWSER_ONLY="no"
# BOOTPROTO="dhcp"
DEFROUTE="yes"
IPV4_FAILURE_FATAL="no"
IPV6INIT="yes"
IPV6_AUTOCONF="yes"
IPV6_DEFROUTE="yes"
IPV6_FAILURE_FATAL="no"
IPV6_ADDR_GEN_MODE="stable-privacy"
NAME="ens33"
UUID="5a6c3800-a0e4-4b28-94c9-0e7f0df161b6"
DEVICE="ens33"
ONBOOT="yes"
BOOTPROTO="static"
NM_CONTROLLED=yes
IPADDR=192.168.96.161
NETMASK=255.255.255.0
GATEWAY=192.168.96.2
DNS1=192.168.0.20
DNS2=192.168.0.19
```
4. 修改当前网卡ip为静态ip
``` 

systemctl restart network.service

# 临时修改将ens33的IP改为192.168.96.161
ifconfig ens33 192.168.96.161 netmask 255.255.255.0

```
### 使用centos
1. root 账户切换
```
su
```

### 快照管理
#### 创建快照
#### 快照复制

