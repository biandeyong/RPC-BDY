package com.bdy.server;


import com.bdy.api.HelloService;
import com.bdy.core.netty.server.NettyServer;
import com.bdy.core.registry.DefaultServiceRegistry;
import com.bdy.core.registry.ServiceRegistry;

/**
 * 测试用Netty服务提供者（服务端）
 * @author bdy
 */
public class NettyTestServer {

    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry registry = new DefaultServiceRegistry();
        registry.register(helloService);
        NettyServer server = new NettyServer();
        server.start(9998);
    }

}
