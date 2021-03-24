package com.arno.learn.grow.tiny.web.context;

import com.arno.learn.grow.tiny.web.servlet.ServletStartupInitializer;

import javax.servlet.ServletContext;

/**
 * @Author: Arno.KV
 * @Date: 2021/3/24 14:29
 * @Description:
 */
public class TinyApplicationContext extends TinyContextLoader implements ServletStartupInitializer {

    @Override
    public int order() {
        return 1;
    }

    @Override
    public void loadUp(ServletContext servletContext) {
        super.initApplicationContext(servletContext);
    }
}
