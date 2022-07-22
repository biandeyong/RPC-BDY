package com.bdy.core.transport.netty.server;

import com.bdy.core.hook.ShutdownHook;
import com.bdy.core.provider.ServiceProvider;
import com.bdy.core.provider.ServiceProviderImpl;
import com.bdy.core.registry.NacosServiceRegistry;
import com.bdy.core.registry.ServiceRegistry;
import com.bdy.core.transport.RpcServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * NIO方式服务提供侧
 * @author bdy
 */
public class NettyServer implements RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private final String host;
    private final int port;

    private final ServiceRegistry serviceRegistry;
    private final ServiceProvider serviceProvider;

    public NettyServer(String host, int port) {
        this.host = host;
        this.port = port;
        serviceRegistry = new NacosServiceRegistry();
        serviceProvider = new ServiceProviderImpl();
    }

    //netty基本启动配置
    @Override
    public void start() {
        //BossGroup 专门负责接收客户端的连接，WorkerGroup 专门负责网络的读写
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //通过ServerBootStrap配置整个 Netty 程序，串联各个组件
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)//配置组
                    .channel(NioServerSocketChannel.class)//配置网络通信的组件,
                    .handler(new LoggingHandler(LogLevel.INFO))//配置处理器
                    .option(ChannelOption.SO_BACKLOG, 256)//参数
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {//子处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();//ChannelPipeline 是保存 ChannelHandler 的 List，用于处理或拦截 Channel 的入站事件和出站
//                            pipeline.addLast(new CommonEncoder(new JsonSerializer()));//序列化
//                            pipeline.addLast(new CommonDecoder());
                          //netty编码与解码器实现
                          pipeline.addLast(new ObjectDecoder(1024*1024, ClassResolvers.cacheDisabled((this.getClass().getClassLoader()))));
                          pipeline.addLast(new ObjectEncoder());
                            pipeline.addLast(new NettyServerHandler());//服务处理器

                        }
                    });
            //绑定一个端口并且同步 生成了一个 ChannelFuture 对象 启动服务
            ChannelFuture future = serverBootstrap.bind(host,port).sync();//注册一个监听，当操作执行成功或失败时监听会自动触发注册的监听事件
            ShutdownHook.getShutdownHook().addClearAllHook();
            future.channel().closeFuture().sync();//对关闭通道进行侦听

        } catch (InterruptedException e) {
            logger.error("启动服务器时有错误发生: ", e);
        } finally {
            //最终关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public <T> void publishService(Object service, Class<T> serviceClass) {
        serviceProvider.addService(service);
        serviceRegistry.register(serviceClass.getCanonicalName(), new InetSocketAddress(host, port));
        start();
    }

}
