package com.wcc.app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "wcc")
@Data
public class ClusterProperties {
    private String serverId = "iot";

    private String clusterName = "iot";
}
