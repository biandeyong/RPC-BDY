package com.bdy.client;


import com.bdy.api.HelloObject;
import com.bdy.api.HelloService;
import com.bdy.core.client.RpcClientProxy;

/**
 * 客户端测试类
 * 动态代理调用helloservice
 *
 */
public class TestClient {
    public static void main(String[] args) {
        RpcClientProxy proxy = new RpcClientProxy("127.0.0.1", 9000);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}

