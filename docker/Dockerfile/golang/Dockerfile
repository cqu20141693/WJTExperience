FROM golang:1.18

# 环境
ENV GO111MODULE=on \
    CGO_ENABLED=0 \
    GOOS=linux \
    GOARCH=amd64 \
    GOPROXY="https://goproxy.io,direct"
WORKDIR /go/scada
COPY . /go/scada
# 编译二进制文件
RUN go build -a -o scada .

# 启动容器时运行的命令
ENTRYPOINT ["./scada"]
