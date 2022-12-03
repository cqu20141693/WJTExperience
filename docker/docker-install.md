## docker install
### mysql
``` 
docker run --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=cc@123 -d mysql:5.7.38
```
### influxDB
``` 
docker run -p 8086:8086 \
	--name influxdb \
	--restart unless-stopped \
	-e DOCKER_INFLUXDB_INIT_USERNAME=admin \
	-e DOCKER_INFLUXDB_INIT_PASSWORD=cc@123 \
    -v /work/influxdb/data:/var/lib/influxdb \
    -v /work/influxdb/config/influxdb.conf:/etc/influxdb/influxdb.conf \
    -v /etc/localtime:/etc/localtime \
    -d influxdb:1.8

      
      
docker run -p 8086:8086 \
      -v /work/influxdb:/var/lib/influxdb \
      -v /work/influxdb2:/var/lib/influxdb2 \
      -e DOCKER_INFLUXDB_INIT_MODE=upgrade \
      -e DOCKER_INFLUXDB_INIT_USERNAME=my-user \
      -e DOCKER_INFLUXDB_INIT_PASSWORD=my-password \
      -e DOCKER_INFLUXDB_INIT_ORG=my-org \
      -e DOCKER_INFLUXDB_INIT_BUCKET=my-bucket \
      influxdb:2.0
```
### rabbitmq
```
docker run -d --name rabbitmq3.8 \
-p 5672:5672 -p 9672:15672 -v //data:/var/lib/rabbitmq \
--hostname gRabbit -e RABBITMQ_DEFAULT_VHOST=my_vhost  \
-e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin@123  rabbitmq:3.8-management
```

### mongodb
``` 
docker search mongo
docker pull mongo
docker run -d -p 27017:27017 -v mongo_configdb:/data/configdb -v mongo_db:/data/db --name mongo docker.io/mongo

docker run -d -p 27017:27017 -v mongo_configdb:/data/configdb -v mongo_db:/data/db --name mongo docker.io/mongo --auth
```
