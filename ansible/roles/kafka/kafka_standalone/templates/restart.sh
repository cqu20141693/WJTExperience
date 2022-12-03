#!bin/bash

pid=$(ps -ef |grep kafka |grep -v grep | awk '{print $2}')
if [-z $pid];then
  kill -9 $pid
  echo stop kafka pid=$pid
else
  echo first start kafka
fi
sh /usr/local/kafka_2.12-2.2.1/bin/kafka-server-start.sh -daemon /usr/local/kafka_2.12-2.2.1/config/server.properties
echo start kafka
