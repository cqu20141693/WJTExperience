### ansible

#### 安装Ansible

1. 解压安装包

```bash
tar -xvf ansible_v2.9.9_install.tar.gz 
```

2. 执行安装

```bash
cd ansible_v2.9.9_install/
sh ansible_v2.9.0_install.sh
```

3. 免密登录

```bash
# 生成key
ssh-keygen
# 将生成的 .ssh/id_rsa.pub 内容拷贝到目标机器 /root/.ssh/authorized_keys 中
cat .ssh/id_rsa.pub
```

#### 编写部署任务

1. 配置hosts

``` 
配置inventory 清单，用于部署
```

2. 配置部署任务play（nginx.yml）

``` 
配置任务编排顺序： 
初始执行基础环境安装：防火墙关闭，jdk,python环境，docker环境
先安装数据库mysql
再安装中间件： nacos
再安装微服务： iiot
......
```

3. 配置roles 具体任务

```
1. 编写role 任务： 定义tasks 任务，定义templates 模板（sh,conf）,可以利用变量替换能力，定义vars 变量
2. 配置全局变量group_var(配置全局变动变量)
比如： minio role
```
4. 部署说明

#### ansible 教程
```markdown
1. [基础入门](https://www.redhat.com/zh/topics/automation/learning-ansible-tutorial)
2. [使用博客](https://www.cnblogs.com/mauricewei/tag/ansible/)
```

