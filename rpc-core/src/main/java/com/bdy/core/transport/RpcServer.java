package com.bdy.core.transport;

/**
 * 服务器类通用接口
 * @author ziyang
 */
public interface RpcServer {

    void start();

    <T> void publishService(Object service, Class<T> serviceClass);


}
