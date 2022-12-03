#!/bin/bash

echo "create dir"
mkdir -p /data/cassandra/data/cassandra
mkdir -p /data/cassandra/log/cassandra
chown -R cassandra:cassandra /data/cassandra
echo "cp data dir"
cp -r /var/lib/cassandra/ /data/cassandra/data/
echo "cp data log"
cp -r /var/log/cassandra/ /data/cassandra/log/
echo "chown cassandra"
chown -R cassandra:cassandra /data/cassandra/data
chown -R cassandra:cassandra /data/cassandra/log
echo "restart"
sh /home/cassandra/apache-cassandra-3.11.10/start_cassandra.sh restart
c
# grep /data/cassandra /home/cassandra/apache-cassandra-3.11.10/conf/cassandra.yaml
# ll /data/cassandra/data/cassandra
# ll /data/cassandra/log/cassandra
