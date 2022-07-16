package com.bdy.core.server;


import com.bdy.entity.RpcRequest;
import com.bdy.entity.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * 服务端实现
 */
public class RpcServer {

    private final ExecutorService threadPool;
    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);
    //创建线程池
    public RpcServer() {
        int corePoolSize = 5;
        int maximumPoolSize = 50;
        long keepAliveTime = 60;
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(100);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workingQueue, threadFactory);
    }

    public void register(Object service, int port) {
        //监听端口
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("服务器正在启动...");
            Socket socket;
            while((socket = serverSocket.accept()) != null) {
                logger.info("客户端连接！Ip为：" + socket.getInetAddress());
                //创建新线程
                threadPool.execute(new WorkerThread(socket, service));
            }
        } catch (IOException e) {
            logger.error("连接时有错误发生：", e);
        }
    }

    //线程实现类
    private class WorkerThread implements Runnable {
        Socket socket;
        Object service;
        public WorkerThread(Socket socket, Object service) {
            this.service = service;
            this.socket = socket;
        }
        @Override
        public void run() {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
                //从流中读取RPCRequest对象
                RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
                Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
                Object returnObject = method.invoke(service, rpcRequest.getParameters());
                objectOutputStream.writeObject(RpcResponse.success(returnObject));
                objectOutputStream.flush();
            } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                logger.error("调用或发送时有错误发生：", e);
            }
        }

    }
}
