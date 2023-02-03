## 环境安装
### go 安装
1. [下载安装包](https://go.dev/dl/)
2. 配置环境变量
```  
export PATH=$PATH:$HOME/go/bin
export GOPATH=$HOME/go
export GOROOT=$HOME/goroot
export GOPROXY=https://mirrors.aliyun.com/goproxy/,https://goproxy.io,direct
export GOPRIVATE=git.mycompany.com,github.com/my/private
go version 检查安装情况
```
###goland 安装
1. 下载jetbrains
2. 安装goland
3. 配置go path
4. 如果没有配置全局代理，需要配置GOPROXY
