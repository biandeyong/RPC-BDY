package com.bdy.core.registry;


import java.net.InetSocketAddress;

/**
 *服务注册中心通用接口
 */
public interface ServiceRegistry {
    /**
     *  将服务注册进注册表
     * @param serviceName 服务名称
     * @param inetSocketAddress 提供服务的地址
     */
    void register(String serviceName, InetSocketAddress inetSocketAddress);

    /**
     * 根据服务名查找服务
     * @param serviceName 服务名称
     * @return 服务实体的地址
     */
    InetSocketAddress lookupService(String serviceName);
}
