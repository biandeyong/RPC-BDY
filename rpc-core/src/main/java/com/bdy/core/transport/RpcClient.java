package com.bdy.core.transport;


import com.bdy.entity.RpcRequest;

/**
 * 客户端类通用接口
 * @author ziyang
 */
public interface RpcClient {

    Object sendRequest(RpcRequest rpcRequest);

}
