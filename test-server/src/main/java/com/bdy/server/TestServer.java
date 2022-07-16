package com.bdy.server;


import com.bdy.api.HelloService;
import com.bdy.core.server.HelloServiceImpl;
import com.bdy.core.server.RpcServer;

/**
 * 服务端测试类
 * 创建rpcserver，注册
 */
public class TestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        RpcServer rpcServer = new RpcServer();

        rpcServer.register(helloService, 9000);
    }
}
