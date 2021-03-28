package com.arno.learn.grow.tiny.web.supoort;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @desc:
 * @author: angelica
 * @date: 2021/3/7 下午1:50
 * @version:
 */
public class ContextBeanInfoBuilder implements Serializable {

    private static final long serialVersionUID = 5431808903484849974L;
    private final ContextBeanInfo contextBeanInfo;

    private ContextBeanInfoBuilder() {
        this.contextBeanInfo = new ContextBeanInfo();
    }

    public ContextBeanInfoBuilder initMethods(List<Method> initMethods) {
        contextBeanInfo.setInitMethods(initMethods);
        return this;
    }

    public ContextBeanInfoBuilder destroyMethods(List<Method> initMethods) {
        contextBeanInfo.setDestroyMethods(initMethods);
        return this;
    }

    public ContextBeanInfoBuilder instance(Object instance) {
        contextBeanInfo.setInstance(instance);
        return this;
    }

    public static ContextBeanInfoBuilder builder() {
        return new ContextBeanInfoBuilder();
    }
    public ContextBeanInfo build() {
        return this.contextBeanInfo;
    }
}
