package com.arno.learn.grow.tiny.web.supoort;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @desc: 实例对象定义
 * @author: Arno.KV
 * @date: 2021/3/7 上午12:29
 * @version:
 */
public class ContextBeanInfo implements Serializable {

    private static final long serialVersionUID = 7402801418914384656L;
    /**
     * 实例化对象
     */
    private Object instance;

    /**
     * 实例初始化后执行的方法
     */
    private List<Method> initMethods;

    /**
     * 实例销毁前执行的方法
     */
    private List<Method> destroyMethods;

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public List<Method> getInitMethods() {
        return initMethods;
    }

    public void setInitMethods(List<Method> initMethods) {
        this.initMethods = initMethods;
    }

    public List<Method> getDestroyMethods() {
        return destroyMethods;
    }

    public void setDestroyMethods(List<Method> destroyMethods) {
        this.destroyMethods = destroyMethods;
    }
}
