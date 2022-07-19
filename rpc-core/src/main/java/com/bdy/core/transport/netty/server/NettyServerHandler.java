package com.bdy.core.transport.netty.server;

import com.bdy.core.provider.ServiceProviderImpl;
import com.bdy.core.provider.ServiceProvider;
import com.bdy.core.handler.RequestHandler;
import com.bdy.entity.RpcRequest;
import com.bdy.entity.RpcResponse;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Netty中处理RpcRequest的Handler
 * @author bdy
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    private static RequestHandler requestHandler;//服务处理
    private static ServiceProvider serviceProvider;//注册表

    static {
        requestHandler = new RequestHandler();
        serviceProvider = new ServiceProviderImpl();
        //这里获取注册表，表里的内容已经注册好了
    }
    //当类实例化后，相当于开启线程
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        try {
            logger.info("服务器接收到请求: {}", msg);
            String interfaceName = msg.getInterfaceName();
            Object service = serviceProvider.getService(interfaceName);
            //通过请求处理获取结果
            Object result = requestHandler.handle(msg, service);
            //结果写入
            ChannelFuture future = ctx.writeAndFlush(RpcResponse.success(result));
            future.addListener(ChannelFutureListener.CLOSE);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("处理过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }

}
