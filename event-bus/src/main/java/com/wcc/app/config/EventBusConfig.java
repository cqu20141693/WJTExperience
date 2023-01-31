package com.wcc.app.config;

import com.wcc.event.BrokerEventBus;
import com.wcc.event.EventBroker;
import com.wcc.event.EventBus;
import com.wcc.event.cluster.ClusterManager;
import com.wcc.event.cluster.RedisClusterEventBroker;
import com.wcc.event.cluster.RedisClusterManager;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.core.scheduler.Schedulers;

@Configuration
public class EventBusConfig {

    @Bean(initMethod = "startup")
    public RedisClusterManager clusterManager(ClusterProperties properties, ReactiveRedisTemplate<Object, Object> template) {
        return new RedisClusterManager(properties.getClusterName(),properties.getServerId(), template);
    }

    @Bean
    public EventBus eventBus(ObjectProvider<EventBroker> provider) {

        BrokerEventBus eventBus = new BrokerEventBus();
        eventBus.setPublishScheduler(Schedulers.parallel());
        for (EventBroker eventBroker : provider) {
            eventBus.addBroker(eventBroker);
        }

        return eventBus;
    }

    @Bean(destroyMethod = "shutdown")
    public RedisClusterEventBroker redisClusterEventBroker(ClusterManager clusterManager,
                                                           ReactiveRedisConnectionFactory connectionFactory) {
        return new RedisClusterEventBroker(clusterManager, connectionFactory);
    }
}
