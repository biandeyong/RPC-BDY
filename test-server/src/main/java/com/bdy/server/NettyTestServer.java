package com.bdy.server;


import com.bdy.api.HelloService;
import com.bdy.core.transport.netty.server.NettyServer;
import com.bdy.core.provider.ServiceProviderImpl;
import com.bdy.core.provider.ServiceProvider;

/**
 * 测试用Netty服务提供者（服务端）
 * @author bdy
 */
public class NettyTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        NettyServer server = new NettyServer("127.0.0.1", 9999);
//        server.setSerializer(new ProtobufSerializer());
        server.publishService(helloService, HelloService.class);
    }
}

