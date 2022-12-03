## mybatis
### TypeHandler
#### JsonObjectTypeHandler
```
@MappedTypes({JSONObject.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class JsonObjectTypeHandler extends BaseTypeHandler<JSONObject>
```
### [generator plugin](https://github.com/itfsw/mybatis-generator-plugin)
#### generatorWorkpiece.xml
#### FluentBuilderMethodsPlugin
#### ToStringPlugin
#### SelectOneByExamplePlugin
```
<!-- 查询单条数据插件 -->
<plugin type="com.itfsw.mybatis.generator.plugins.SelectOneByExamplePlugin"/>
```
#### LimitPlugin
```
<!-- MySQL分页插件 -->
<plugin type="com.itfsw.mybatis.generator.plugins.LimitPlugin">
    <!-- 通过工件startPage影响Example中的page方法开始分页的页码，默认分页从0开始 -->
    <property name="startPage" value="0"/>
</plugin>
```
#### BatchInsertPlugin
```
<!-- 批量插入插件 -->
<plugin type="com.itfsw.mybatis.generator.plugins.BatchInsertPlugin">
    <!-- 
    开启后可以实现官方插件根据属性是否为空决定是否插入该字段功能
    ！需开启allowMultiQueries=true多条sql提交操作，所以不建议使用！插件默认不开启
    -->
    <property name="allowMultiQueries" value="false"/>lk
</plugin>

```
#### ModelColumnPlugin
```
<!-- 数据Model属性对应Column获取插件 -->
<!-- 配合批量插入插件（BatchInsertPlugin）使用，batchInsertSelective(@Param("list") List list, @Param("selective") XXX.Column ... insertColumns) -->
<plugin type="com.itfsw.mybatis.generator.plugins.ModelColumnPlugin"/>
```