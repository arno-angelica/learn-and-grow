package com.arno.grow.web.mvc.handler;

import java.io.Serializable;

/**
 * @desc: 上下文
 * @author: Arno.KV
 * @date: 2021/3/1 下午10:14
 * @version:
 */
public class ContextDefinition implements Serializable {
    private static final long serialVersionUID = -7076638999075945188L;
    /**
     * 实例对象
     */
    private Object instance;

    /**
     * 实例名称
     */
    private String name;

    public ContextDefinition() {

    }

    public ContextDefinition(Object instance, String name) {
        this.instance = instance;
        this.name = name;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
