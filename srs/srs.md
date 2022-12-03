### srs

#### docker

```
tcp://1935, for RTMP live streaming server
tcp://1985, HTTP API server, for HTTP-API, WebRTC
tcp://8080, HTTP live streaming server, HTTP-FLV, HLS as such.
udp://8000, WebRTC Media server.


docker run -d -p 1935:1935 -p 1985:1985 -p 8080:8080 -p 5060:5060/udp -p 9000:9000/udp \
--name srs \
--restart=always \
-v /work/srs4/conf/:/usr/local/srs/conf/ \
ossrs/srs:v4.0.117 ./objs/srs -c ./conf/push.gb28181.conf
docker run -d -p 1935:1935 -p 1985:1985 -p 8080:8080 -p 5060:5060/udp -p 9000:9000/udp \
--name srs \
--restart=always \
-v /work/srs4/conf/:/usr/local/srs/conf/ \
registry.cn-shenzhen.aliyuncs.com/wcc/srs ./objs/srs -c ./conf/push.gb28181.conf

docker run --rm -it -p 1935:1935 -p 1985:1985 -p 8080:8080  -p 8000:8000/udp -p 5060:5060/udp -p 9000:9000/udp \
-d --name srs  \
-v /work/srs4/conf/:/usr/local/srs/conf/ \
ossrs/srs:3 ./objs/srs -c conf/push.gb28181.conf


```
