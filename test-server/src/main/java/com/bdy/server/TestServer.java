package com.bdy.server;


import com.bdy.api.HelloService;
import com.bdy.core.server.HelloServiceImpl;
import com.bdy.core.server.RpcServer;
import com.bdy.core.server.register.DefaultServiceRegistry;
import com.bdy.core.server.register.ServiceRegistry;


public class TestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        //可以注册多个服务在注册表
        serviceRegistry.register(helloService);
        //开启服务
        new RpcServer(serviceRegistry).start(9000);


    }
}
