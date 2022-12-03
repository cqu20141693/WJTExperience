## kafka

### bin
1.  topic
``` 
bin/kafka-topics.sh --create --bootstrap-server 192.168.96.161:9092 --replication-factor 1 --partitions 1 --topic test-1

bin/kafka-topics.sh --list --bootstrap-server 192.168.96.161:9092
```
2. producer
```
bin/kafka-console-producer.sh --broker-list 192.168.96.161:9092 --topic test-1
```
3. consumer
``` 
bin/kafka-console-consumer.sh --bootstrap-server 192.168.96.161:9092 --topic test-1 --from-beginning
```
4. restart
```shell
#!bin/bash

pid=$(ps -ef |grep kafka |grep -v grep | awk '{print $2}')
if [-z $pid];then
  kill -9 $pid
  echo stop kafka pid=$pid
else
  echo first start kafka
fi
sh ./bin/kafka-server-start.sh -daemon ./config/server.properties
echo start kafka

```
