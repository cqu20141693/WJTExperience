## RabbitMQ

1. [rabbitmq 四种交换机](https://blog.csdn.net/Mrqiang9001/article/details/120711222)

### install

``` 
yum update -y

yum install epel-release -y

yum install erlang -y

yum install rabbitmq-server -y
systemctl start rabbitmq-server.service
```

### CLI

``` 

rabbitmq-plugins enable rabbitmq_management
rabbitmqctl add_user user_name pwd
rabbitmqctl set_user_tags user_name administrator
rabbitmqctl set_permissions -p / user_name ".*" ".*" ".*"
#重启rabbitmq
systemctl restart rabbitmq-server.service

# 查看当前用户列表
rabbitmqctl  list_users  

# 修改密码
rabbitmqctl  change_password  user_name  'cc@123456'
```
