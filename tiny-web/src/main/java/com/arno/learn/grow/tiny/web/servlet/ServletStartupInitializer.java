package com.arno.learn.grow.tiny.web.servlet;

import javax.servlet.ServletContext;

/**
 * @Author: Arno.KV
 * @Date: 2021/3/24 13:43
 * @Description:
 */
public interface ServletStartupInitializer {

    /**
     * 默认排序大小，从小到大
     */
    int DEFAULT_ORDER = Integer.MAX_VALUE;

    default int order() {
        return DEFAULT_ORDER;
    }

    default void loadUp(ServletContext servletContext) {}
}
