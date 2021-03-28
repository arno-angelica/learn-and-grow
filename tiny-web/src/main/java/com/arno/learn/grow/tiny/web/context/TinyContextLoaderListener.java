package com.arno.learn.grow.tiny.web.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @desc: web 容器启动时的监听器，初始化
 * @author: angelica
 * @date: 2021/3/6 下午11:48
 * @version:
 */
public class TinyContextLoaderListener extends TinyContextLoader implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        super.initApplicationContext(sce.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        super.destroy();
    }
}
