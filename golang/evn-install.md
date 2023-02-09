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

### go 命令
#### go version
#### go run
#### go build
```   
编译go 项目
编译Linux 可执行文件
CGO_ENABLED=0  GOOS=linux  GOARCH=amd64  go build main.go
编译mac可执行文件
CGO_ENABLED=0 GOOS=darwin  GOARCH=amd64  go build main.go
编译windos可执行文件
CGO_ENABLED=0 GOOS=windows  GOARCH=amd64  go  build  main.go

go build
go build 文件列表
go build+包

参数：-o 指定输出可执行文件名
-o myexec 
```
#### go mod
1. go mod init
初始化创建一个模块，会生产go.mod 文件
2. go mod tidy
下载依赖
#### go env
1. go env 
查看go 环境变量
2. go env set
```  
设置go 环境变量
set GOHOSTARCH=amd64
set GOHOSTOS=windows
set GOOS=windows
set GOPATH=F:\go\path
set GOROOT=F:\go\root
···
```
