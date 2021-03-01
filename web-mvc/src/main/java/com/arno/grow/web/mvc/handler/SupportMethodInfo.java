package com.arno.grow.web.mvc.handler;

import javax.ws.rs.core.MediaType;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/2/28 下午2:49
 * @version:
 */
public class SupportMethodInfo implements Serializable {
    private static final long serialVersionUID = 4888763257730540102L;

    /**
     * 请求路径
     */
    private final String requestPath;

    /**
     * 请求方法
     */
    private final Method method;

    /**
     * 支持的请求方式，如 post
     */
    private final Set<String> supportMethods;

    /**
     * 对象实例
     */
    private final Object instance;

    public SupportMethodInfo(String requestPath, Method method, Set<String> supportMethods, Object instance) {
        this.requestPath = requestPath;
        this.method = method;
        this.supportMethods = supportMethods;
        this.instance = instance;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public Method getMethod() {
        return method;
    }
    public Set<String> getSupportMethods() {
        return supportMethods;
    }

    public Object getInstance() {
        return instance;
    }
}
