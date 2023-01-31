package com.wcc.event.cluster;


/**
 * 集群管理器
 */
public interface ClusterManager {

    /**
     * @return 集群名称
     */
    String getClusterName();

    /**
     * @return 当前服务节点ID
     */
    String getCurrentServerId();

    /**
     * @return 高可用管理器
     */
    HaManager getHaManager();

    /**
     * 获取集群队列
     *
     * @param queueId 队列标识
     * @param <T>     队列中元素类型
     * @return 集群队列
     */
    <T> ClusterQueue<T> getQueue(String queueId);

    /**
     * 获取集群广播
     *
     * @param topic 广播标识
     * @param <T>   数据类型
     * @return 集群广播
     */
    <T> ClusterTopic<T> getTopic(String topic);


}
