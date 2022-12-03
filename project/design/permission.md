## 权限

### 资产权限

资产： 需要用于租户隔离的数据

```    
大概的思路：
新增资产数据时绑定资产关系
查询资产时，自动关联资产关系表查询
更新，删除和共享时都需要做权限查询校验，校验失败，异常处理，成功则继续业务处理，比如删除后需要清理关系表，共享后需要新增关系表

更新，删除可以指定对象或者时index,可以提前做权限校验
删除需要做解绑操作，解绑所有的关系
添加时，需要新增资产绑定信息
查询时，需要对查询的接口做拦截，增加exist 查询：资产id=asset_relation.asset_id and target_id= 登录信息中的targetId and permission >=PermissonType
共享授权操作： 需要检查共享权限，共享权限

```

#### relation

1. Asset table

```java
import java.lang.annotation.Target;

class AssetsAuthorizeRelation {
    private String id;
    private AssetType assetType;
    private String assetId;
    private String targetId;
    private TargetType targetType;

    /**
     * {@link PermissionType}
     */
    private Integer permission;

}

enum AssetType {
    device,
    product,
    firmware,
    ;
}

enum TargetType {
    user,
    tenant,
    org,
    ;
}

enum PermissionType {
    // 有权限查看
    read,
    // 有权限共享给别人
    share,
    // 有权限修改
    update,
    // 有权限删除
    delete,
    ;
}

/**
 * 主要是配置方法或者类的资产权限注解：
 * 可配置read,update,delete,share 权限
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface AssetsController {
    String type() default AssetType.device;
}

/**
 * 拦截方法AssetsController 注解，是实现权限功能
 */
class AssetsAopInterceptor {

}
```

2. @AssetsController

```java


//查询时，需要联表查询
```

### 菜单权限

``` 

用户权限:
用户组织： tenant,org,super， 用于租户管理
用户role: tenant_admin,org_admin,role_admin 用于权限管理，角色只能能是角色管理员统一管理；可进行复用
用户： 具有role和组织信息
系统管理项目：
super创建tenant,org 时都会创建管理员用户: 一个菜单
管理员用户可创建组织普通用户和配置菜单权限： 一个菜单
用户权限：
用户管理权限：用户增，删，查，改（role）
角色管理权限：角色增，删， 改，查
菜单权限： 可由后端定义或者时新增功能配置： 元数据信息
配置用户


```
