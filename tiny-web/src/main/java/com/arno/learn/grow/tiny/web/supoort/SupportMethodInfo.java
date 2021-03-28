package com.arno.learn.grow.tiny.web.supoort;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * @desc:
 * @author: angelica
 * @date: 2021/2/28 下午2:49
 * @version:
 */
public class SupportMethodInfo implements Serializable {

    private static final long serialVersionUID = -7848621686887101965L;
    /**
     * 请求路径
     */
    private String requestPath;

    /**
     * 请求方法
     */
    private Method method;

    /**
     * 支持的请求方式，如 post
     */
    private Set<String> supportMethods;

    /**
     * 对象实例
     */
    private Object instance;

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setSupportMethods(Set<String> supportMethods) {
        this.supportMethods = supportMethods;
    }

    public void setInstance(Object instance) {
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



    public static class SupportMethodInfoBuilder {

        private final SupportMethodInfo supportMethodInfo;

        private SupportMethodInfoBuilder() {
            supportMethodInfo = new SupportMethodInfo();
        }

        public static SupportMethodInfoBuilder builder() {
            return new SupportMethodInfoBuilder();
        }

        public SupportMethodInfoBuilder requestPath(String requestPath) {
            supportMethodInfo.setRequestPath(requestPath);
            return this;
        }

        public SupportMethodInfoBuilder method(Method method) {
            supportMethodInfo.setMethod(method);
            return this;
        }

        public SupportMethodInfoBuilder supportMethods(Set<String> supportMethods) {
            supportMethodInfo.setSupportMethods(supportMethods);
            return this;
        }

        public SupportMethodInfoBuilder instance(Object instance) {
            supportMethodInfo.setInstance(instance);
            return this;
        }

        public SupportMethodInfo build() {
            return this.supportMethodInfo;
        }
    }
}
