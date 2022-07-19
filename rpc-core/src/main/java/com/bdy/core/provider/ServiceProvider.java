package com.bdy.core.provider;



public interface ServiceProvider {
    <T> void addService(T service);
    Object getService(String serviceName);
}
