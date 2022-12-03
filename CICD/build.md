## 构建流水线

### 制作golang 自动部署

1. 安装git 环境

``` 
安装git ，通过git拉取源代码
```

2. 找一个打包虚拟机安装golang/java 环境

``` 
安装go
配置环境变量
ENV GO111MODULE=on \
    CGO_ENABLED=0 \
    GOOS=linux \
    GOARCH=amd64 \
    GOPROXY="https://goproxy.io,direct"
    GOPATH=/data/gopath


安装jre or jdk
配置环境变量
安装maven

```

3. 构建流水线

``` 
登录虚拟机，git 拉去代码，进入代码
通过Dockerfile 构建docker image
将image 推送到docker hub
登录部署机，拉去docker 镜像部署docker
```
