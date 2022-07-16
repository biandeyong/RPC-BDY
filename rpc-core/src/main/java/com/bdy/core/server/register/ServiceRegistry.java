package com.bdy.core.server.register;



public interface ServiceRegistry {
    <T> void register(T service);
    Object getService(String serviceName);
}
