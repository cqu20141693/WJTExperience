## centos

### 解压缩

``` 
unzip deploy-tool.zip
zip -r mydata.zip mydata #压缩mydata目录
zip iiot-web-3.2.zip iiot-web-3.2.tar all.zip

压缩

tar -czvf 打包后生成的文件名全路径 要打包的目录
tar -czvf /home/xahot.tar.gz /xahot

tar –czf jpg.tar.gz *.jpg //将目录里所有jpg文件打包成jpg.tar后，并且将其用gzip压缩，生成一个gzip压缩过的包，命名为jpg.tar.gz


解压

tar –xvf file.tar //解压 tar包

tar -xzvf file.tar.gz //解压tar.gz

tar -xjvf file.tar.bz2   //解压 tar.bz2tar –xZvf file.tar.Z //解压tar.Z

```

### 磁盘

``` 
df -hl 查看磁盘剩余空间
df -h 查看每个根路径的分区大小
du -sh [目录名] 返回该目录的大小
du -sm [文件夹] 返回该文件夹总M数 
du -h [目录名] 查看指定文件夹下的所有文件大小（包含子文件夹）
查看当前目录下一级子文件和子目录占用的磁盘容量
du -lh --max-depth=1
统计当前文件夹(目录)大小，并按文件大小排序
du -sh * | sort -n
查看指定文件大小
du -sk filename

```

### 进程命令

``` 
top
kill -9/15

netstat -tunlp 
```

### systemctl

``` 
systemctl start mysqld.service 
systemctl status mysqld.service 
systemctl stop mysqld.service 
systemctl enable mysqld.service 

systemctl disable firewalld.service  // 永久关闭防火墙
network.service
mysqld.service
docker.service
```

### 时间

1. 查看，修改时区

``` 
列出时区：  timedatectl list-timezones
设置时区：timedatectl set-timezone Asia/Shanghai
```

2. 查看修改时间

``` 
1.显示时间 ：  date
2.修改时间  date -s  时间
如：设置当前时间为：2018年12月10点50分：date -s  ‘2018-12-14 10:50:00’
```

3. 同步时间

``` 
安装：yum install ntp
同步：ntpdate pool.ntp.org
```

### 文件管理

#### 文件权限

```
// 修改文件夹所有者
chown [-R] 账号名称 文件或目录 
chown [-R] 账号名称:用户组名称 文件或目录
chown -R cassandra:cassandra /data/cassandra
-R : 进行递归( recursive )的持续更改，即连同子目录下的所有文件、目录都更新成为这个用户组。常常用在更改某一目录的情况。

```

### vi、vim

``` 
:$ 跳转到最后一行
:1 跳转到第一行
```

#### 文件内容替换

``` 
sed -i 's/Search_String/Replacement_String/g' Input_File

```

#### dd 命令删除一整行，使用 . 命令会 ==重复删除当前行==。

### ssh 免密登录

``` 
ssh-keygen 
cd /root/.ssh

// 发送pub key 到192.168.96.234
ssh-copy-id -i ~/.ssh/id_rsa.pub root@192.168.96.234
免密登录
ssh 'root@192.168.96.234'

```

### 账户管理

#### 添加用户

``` 
sudo useradd username -m #创建用户
sudo userdel -r username #删除用户
su username #切换到指定用户，username指的是用户名
sudo -i 切换到root
```

#### 账户切换

``` 
普通用户切换root
su root 
输入密码

root 切换普通用户


```

### yum

``` 

yum install python37 --downloadonly --downloaddir=/work/repo

```

### ifconfig

1. 添加虚拟网卡到wlp2s0命令 sudo ifconfig wlp2s0:1 192.168.10.11 up
2. 查看某个网段地址 ifconfig |grep 192
3. 修改ip : # ifconfig ens33 192.168.96.160 netmask 255.255.255.0

### vim

#### Esc 模式

1. 搜索文本

```
/value
继续查找此关键字，敲字符 n
```

2. 删除单个字符 x 命令会 ==删除光标下的字符==，使用 . 会让 ==重复删除光标下的字符==。

3. 删除一行 dd 命令删除一整行，使用 . 命令会 ==重复删除当前行==

4. 撤销修改 多次输入 u 撤销上述修改

#### Insert 模式

### ps

1. 查看进程

``` 
1. 查看进程启动信息
ps -ef |grep mysql

2. 查看进程内存和cpu使用情况，PID后的信息
ps -aux | grep kafka 

ps aux | sort -k4,4nr | head -n 10 查看内存占用前10名的程序
```

### top

1. 查看进程情况

```
top |grep mysql
```

2. 查看某个进程的内存使用情况

```
top -p PID
```

### grep

1. 搜索文件

``` 
grep file value ：搜索
```

2. -v : 排除

``` 
 ps -ef | grep zookeeper | grep -v ‘grep’ : 搜索zookeeper的启动命令，排查包括grep指令的
```

### wget

``` 
wget --no-check-certificate --quiet \
  --method GET \
  --timeout=0 \
  --header '' \
  -O ansible_v2.9.9_install.tar.gz \
   'http://10.113.75.63:9090/deploy/ansible_v2.9.9_install.tar.gz'
```
### yum
1. yum list只会列出最新版本
   yum list docker-ce --showduplicates | sort -r
2. yum 下载rpm包
```
rpm provides repotrack : 下载repotrack
下载yum-utils

repotrack -p savepath 安装软件名（ansible）

```
3. yum 安装指定rpm包
```   
 rpm -ivh *.rpm --nodeps --force
 
 yum localinstall file.rpm ｜ yum localinstall *.rpm
 
```
### scp
``` 
scp [OPTIONS] [[user@]src_host:]file [[user@]dest_host:]file

options: 
P（大写）- 指定要连接到远程主机上的端口
p（小写）- 保留用于修改和访问的时间戳
r - 递归复制整个目录
q - 安静模式，不显示进度或消息
C - 在传输过程中压缩数据

scp ansible_v2.9.0_install.sh root@10.0.70.89:/tmp
scp ansible_v2.9.0_install.sh root@10.168.220.17:/tmp

可以配置免密，或者使用密码访问
```
