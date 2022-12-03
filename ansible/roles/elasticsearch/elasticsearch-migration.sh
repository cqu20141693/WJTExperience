#!/bin/bash

echo "create dir"
mkdir -p /data/es/log
mkdir -p /data/es/data

echo "sed config"
sed -i 's/\/var\/log\/elasticsearch\/log/\/data\/es\/log/g' /home/elasticsearch/elasticsearch-7.16.2/config/elasticsearch.yml
sed -i 's/\/var\/lib\/elasticsearch\/data/\/data\/es\/data/g' /home/elasticsearch/elasticsearch-7.16.2/config/elasticsearch.yml

cat /home/elasticsearch/elasticsearch-7.16.2/config/elasticsearch.yml

echo "cp log"
cp -r /var/log/elasticsearch/log/.  /data/es/log/
echo "cp data"
cp -r /var/lib/elasticsearch/data/.  /data/es/data/

echo "chown"
chown -R elasticsearch:elasticsearch /data/es


echo "restart elasticsearch"
ps -ef | grep  elasticsearch-7.16.2 |  grep -v grep  | awk '{print $2}'  |  uniq | xargs -I {} kill -9 {}
su - elasticsearch -c "sh /home/elasticsearch/elasticsearch-7.16.2/bin/elasticsearch  -d"

# curl 10.1.12.78:9200/_cluster/health?pretty
