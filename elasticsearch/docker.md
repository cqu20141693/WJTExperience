### [docker 部署](https://www.elastic.co/guide/en/elasticsearch/reference/7.17/docker.html)
```
docker pull docker.elastic.co/elasticsearch/elasticsearch:7.17.2


docker run -d --name es7 -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" -e "node.name=es7" -v /work/es/data:/usr/share/elasticsearch/data -v /work/es/config:/usr/share/elasticsearch/config docker.elastic.co/elasticsearch/elasticsearch:7.17.2

```