package com.bdy.client;


import com.bdy.api.HelloObject;
import com.bdy.api.HelloService;
import com.bdy.core.RpcClient;
import com.bdy.core.RpcClientProxy;
import com.bdy.core.netty.client.NettyClient;

/**
 * 测试用Netty消费者
 * @author bdy
 */
public class NettyTestClient {

    public static void main(String[] args) {
        RpcClient client = new NettyClient("127.0.0.1", 9998);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);

    }

}