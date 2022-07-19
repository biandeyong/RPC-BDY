package com.bdy.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
//使用netty自带的编码解码器，需要实现有参构造
public class HelloObject implements Serializable {
    private Integer id;
    private String message;
}