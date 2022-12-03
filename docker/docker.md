## docker

### window 安装dokcer

[Windows Docker 安装](https://www.runoob.com/docker/windows-docker-install.html)
![RUNOOB 图标](http://static.runoob.com/images/runoob-logo.png)

### linux 安装docker

1. 卸载旧版本（如果之前安装过的话） yum remove docker docker-common docker-selinux docker-engine
2. 工件安装源

```
1.安装需要的软件包， yum-util 提供yum-Workpiece-manager功能，另两个是devicemapper驱动依赖

yum install -y yum-utils device-mapper-persistent-data lvm2

2.设置 yum 源
yum-Workpiece-manager --add-repo http://download.docker.com/linux/centos/docker-ce.repo（中央仓库）

yum-Workpiece-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo（阿里仓库）
```

3. 选择docker版本并安装 yum list docker-ce --showduplicates | sort -r
4. 选择一个版本并安装 yum install docker-ce-版本号 yum -y install docker-ce-18.03.1.ce

### 安装docker-compose

[安装](https://juejin.cn/post/7003161972535476237)

### 重启docker

systemctl daemon-reload && systemctl restart docker

### docker cli

#### docker build

构建镜像 docker build --no-cache -t "仓库名称" .

#### docker images

查看本地镜像

#### docker login

``` 
// 备注需要配置docker的镜像源insecure-registries
docker login [ip:port]
```

#### docker search

```
docker search nginx
```

#### docker pull

```
docker pull docker.elastic.co/elasticsearch/elasticsearch:7.17.2
```

#### docker tag

```
打本地tag
docker tag local-image:tagname new-repo:tagname
docker push new-repo:tagname
```

#### docker push

``` 
推送镜像 docker push
docker push new-repo:tagname
```

#### docker image

```
docker images : 列出本地镜像
docker image rm containerId 删除镜像
docker rmi repository:tag :删除指定仓库版本镜像
```

``` 
删除所有名字中带 “none” 关键字的镜像
docker rmi $(docker images | grep "none" | awk '{print $3}') 

# 直接删除所有镜像
docker rmi `docker images -q`

# 直接删除所有容器
docker rm `docker ps -aq`


# 按条件筛选之后删除镜像
docker rmi `docker images | grep xxxxx | awk '{print $3}'`

# 按条件筛选之后删除容器
docker rm `docker ps -a | grep xxxxx | awk '{print $1}'`

docker image prune --force --all或者docker image prune -f -a` : 删除所有不使用的镜像
docker container prune -f: 删除所有停止的容器

```

#### docker run

```

docker rm es7

docker run -d --name es7 --cpus=1 -m 2g \
-p 9200:9200 -p 9300:9300 \
-e "discovery.type=single-node" -e "node.name=es7" \
-v /work/es/data:/usr/share/elasticsearch/data docker.elastic.co/elasticsearch/elasticsearch:7.17.2


```

#### docker logs

```
 // 过滤name1|name2的日志 输出到log.txt
docker logs -f es7 |grep 'name1|name2' >> log.txt
// 查看最新两百条并持续查看
docker logs -f --tail=200 es7 |grep -v 'nacos.client'
```

#### docker exec

```
docker exec -it es7 /bin/bash
```

#### docker save

``` 
docker save [options] images [images...]
示例 
docker save -o nginx.tar nginx:latest 
```

#### docker load

``` 
加载保存的镜像
docker load -i {{ path }}/images/{{ image_name }}
```

#### docker cp

```
复制容器文件到宿主机
docker cp 容器id:容器内文件路径 目标路径
#示例
docker cp 6741xxxxxxxx:/xxxx/xxx/xxx/xxxx/common.log /home/rhlog/yyy.log  #注意没有"."
 
```

#### docker history

``` 
查看dockerfile 构建命令
docker history wujt114655/superset-py:2.0.1
--no-trunc : 查看完整命令

```
