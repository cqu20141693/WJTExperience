package com.wcc.oss.minio.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * minio配置类
 *
 * @author baiyan
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.minio")
public class MinioConfig {
    /**
     * ip：minio地址，分布式节点情况下推荐配置一个nginx路由，转接给nginx的负载均衡
     */
    private String endpoint;

    /**
     * 端口：minio地址，分布式节点情况推荐配置一个nginx路由，转接给nginx的负载均衡
     */
    private int port;

    /**
     * 账号
     */
    private String accessKey;

    /**
     * 秘钥
     */
    private String secretKey;

    /**
     * 如果是true，则用的是https而不是http,默认值是true
     */
    private Boolean secure;

    /**
     * 桶名称，默认为baiyan
     */
    private String bucketName = "baiyan";

    /**
     * 是否开启nginx路由，与nginxLoadUrl对应
     */
    private Boolean nginxLoadUrlEnable = false;

    /**
     *
     * 预览的url在nginx中的前缀，minio中生成的文件预览或者下载的url是直接展示成ip：端口形式的，这个是不安全的，需要在nginx中做一层路由。保证安全性，默认不开启。
     */
    private String nginxLoadUrl = "api/9c16ff1ecec";
}
