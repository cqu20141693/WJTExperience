### cassandra

#### cql

1. 登录 ./cqlsh 127.0.0.1 -u cassandra -p cassandra
2. keyspace
3. ```
   DESCRIBE KEYSPACES 
   use keyspace 
   DESCRIBE SCHEMA
   DROP KEYSPACE excelsior;
   DESCRIBE TABLES

```
3. 迁移schema

```markdown
vi migrate_schema.sh
................................
#!/bin/sh

rm -f db.cql

./cqlsh $5 -e "desc keyspace $1" -u $3 -p $4 > db.cql

sed "s/$1/$2/g" -i db.cql
sed "s/default_time_to_live = 0/default_time_to_live = 2678400/g" -i db.cql

./cqlsh 127.0.0.1 -u $3 -p $4 -f db.cql
................................

chmod 777 migrate_schema.sh
migrate_schema.sh iiot iiot_v3 cassandra cassandra 10.1.12.70
```



10.128.81.200

3. table

```sql

alter table test_ttl.empty_table with default_time_to_live= 120 AND gc_grace_seconds = 0;

CREATE TABLE monkey_species
(
    species      text PRIMARY KEY,
    common_name  text,
    population   varint,
    average_size int
) WITH comment = 'Important biological records';

CREATE TABLE timeline
(
    userid       uuid,
    posted_month int,
    posted_time  uuid,
    body         text,
    posted_by    text,
    PRIMARY KEY (userid, posted_month, posted_time)
) WITH compaction = { 'class' : 'LeveledCompactionStrategy' };

CREATE TABLE loads
(
    machine inet,
    cpu     int,
    mtime   timeuuid,
    load    float,
    PRIMARY KEY ((machine, cpu),
    mtime
) ) WITH CLUSTERING ORDER BY (mtime DESC);
```

#### 场景

```
采用分布式架构、无中心、支持多活、弹性可扩展、高可用、容错、一致性可调、提供类SQL查询语言CQL等。
Cassandra为互联网业务而生，已在全球广大互联网公司有成熟应用，是目前最流行的宽表数据库。
```

```
业务有事务的要求，建议使用MySQL,其提供的事务特性及SQL能力保障;
但是，对于互联网业务有如下特点：极致在线、高并发、高存储、可调的一致性、灵活，MySQL并不是最佳选择，Cassandra为此而生。

极致在线：架构支持单节点故障时，业务无影响（注：要求节点及数据副本均有冗余，配置Quorum读写一致性）。
扩展性强：支持从160 GB到10 PB的容量，支持从数千QPS到数千万的QPS，支持从单节点到多节点，支持从单机房到多机房。
可调一致性：通常配置Quorum读写一致性，以满足高可用强一致性要求。而对于物联网等业务，可以根据业务特点，适度降低一致性以获取更高的性能和更低的成本。
```

``` 
多活
Cassandra原生支持多DC部署方式，实现更好的可用性和容灾能力。云数据库Cassandra可以很容易添加新的数据中心，
不同的数据中心可以设定不同的副本数，既可以作为跨数据中心多活高可用，也可以作备份容灾或离线分析使用。

业务灵活多变
云数据库Cassandra的数据模型灵活，对表结构的变更是一个非常轻量级的操作，非常适用于初创型的业务需求，
让您的数据库能更快地跟上业务改进的步伐。

写密集、统计和分析型工作
Cassandra是为优异的写吞吐量而特别优化的，能够支持很高的多客户线程并发写性能和突发的峰值，
这些特性使得Cassandra能够很好支持写多于读的场景，例如用户状态更新、社交网络、建议/评价以及应用统计等。

数据驱动的业务
云数据库Cassandra可以支持数百个节点的集群规模，适合大数据量的存储。在一些需要应用大量数据对用户行为进行分析的场景中，
可以通过整合多种数据来源，存储用户行为数据，构建用户画像，实时存储在Cassandra中，提供大数据风控、推荐等服务。
```

#### nodetool

[nodetool 使用](https://blog.csdn.net/u011250186/article/details/106762617)

1.

#### 调优

```markdown
1. [调优](https://blog.csdn.net/u011250186/article/details/106768355)
```

