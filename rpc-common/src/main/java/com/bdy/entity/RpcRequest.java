package com.bdy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 请求类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest implements Serializable {
        //空参构造
        //要实现反序列化必须实现空参构造
        /**
         * 请求号
         */
        private String requestId;
        /**
         * 待调用接口名称
         */
        private String interfaceName;
        /**
         * 待调用方法名称
         */
        private String methodName;
        /**
         * 调用方法的参数
         */
        private Object[] parameters;
        /**
         * 调用方法的参数类型
         */
        private Class<?>[] paramTypes;

}
