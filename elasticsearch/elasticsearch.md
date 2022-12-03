## elasticseaarch

### 原理分析

#### 名词

1. Term(词条)
   索引里面最小的存储和查询单元，对于英文来说是一个单词，对于中文来说一般指分词后的一个词

2. Term Dictionary

``` 
词典：也称字典，是词条Term 的集合。搜索引擎的通常索引单位是单词，单词词典是由文档集合中出现过的所有单词构成的字符串集合， 
单词词典内每条索引项记载单词本身的一些信息以及指向“倒排列表”的指针
为了解决词典数据量大，ES 提供Term Index，主要解决的是快速定位到Term，使用的时共享前缀树。
ES的Term Index的原因，ES在检索上面，有的时候表现的比Mysql更快，Mysql只有Term Dictionary，而且这一层是以B+树的方式存储在磁盘上的。
检索一个Term 需要若干次的 random access 的磁盘操作。而 Lucene 在Term Dictionary 的基础上添加了Term Index来加速检索，
Term Index以树的形式缓存在内存中，从Term Index查到对应的Term dictionary 的 block 位置之后，再去磁盘上找Term，大大减少了磁盘的 random access 次数。
```

3. 倒排索引（inverted index） lucene索引的通用叫法，即实现了term到doc list的映射。倒排索引建立的是分词（Term）和文档（Document）之间的映射关系，
   在倒排索引中，数据是面向词（Term）而不是面向文档的。每一个field 会建立一个倒排索引。
4. index
``` 
文档数据集合： 表
Metadata元数据：标注文档的相关信息
_index: 文档所属的索引名称
_type:文档所属类型名称，7后只支持_doc
_id:Doc的主键。在写入的时候，可以指定该Doc的ID值，如果不指定，则系统自动生成一个唯一的UUID值。
_version:版本信息可以解决部分冲突问题
_seq_no 严格递增的顺序号，每个文档一个，Shard级别严格递增，保证后写入的Doc的_seq_no大于先写入的Doc的_seq_no。
primary_term:primary_term也和_seq_no一样是一个整数，每当Primary Shard发生重新分配时，比如重启，Primary选举等，_primary_term会递增1
found:查询的ID正确那么ture, 如果 Id 不正确，就查不到数据，found字段就是false。
_source:文档原始的json数量？ ->现在项目中没有用到
_score:相关性打分
```

   

#### es的写入，查询过程？

#### es集群架构，

#### 集群脑裂问题，怎么产生，怎么解决

#### es 深度分页查询原理和问题

### 应用场景

#### 推荐

##### 打分匹配

#### 搜索

##### 深度分页查询

1. Elasticsearch搜索高亮标签自定义

#### 数据统计

##### 聚合

##### 数据分析

商品搜索，设备搜索

#### geo 地理位置

##### 位置范围搜索

#### 日志

##### Logstash

```
配置读取日志文件输入，输出到es中
```

##### kibana

```
日志查看和搜索
日志告警分析
```

#### 持久化存储

不支持包含频繁更新、事务（transaction）的操作

##### 时序数据

```
利用时间做index,可以动态的创建索引或者提前创建好索引，
查询的时候可以根据时间多索引查询

```

##### mysql大表

```
利用关系型数据库写入数据，同步工具将数据同步到es进行数据持久化，可以做查询

```
