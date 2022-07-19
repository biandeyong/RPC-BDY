package com.bdy.client;


import com.bdy.api.HelloObject;
import com.bdy.api.HelloService;
import com.bdy.core.transport.RpcClient;
import com.bdy.core.transport.RpcClientProxy;
import com.bdy.core.transport.netty.client.NettyClient;

/**
 * 测试用Netty消费者
 * @author bdy
 */
public class NettyTestClient {
    public static void main(String[] args) {
        RpcClient client = new NettyClient();
//        client.setSerializer(new ProtobufSerializer());
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
