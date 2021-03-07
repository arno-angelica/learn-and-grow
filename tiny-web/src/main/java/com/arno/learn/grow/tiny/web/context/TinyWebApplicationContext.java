package com.arno.learn.grow.tiny.web.context;

import com.arno.learn.grow.tiny.web.supoort.SupportMethodInfo;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/3/6 下午11:54
 * @version:
 */
public interface TinyWebApplicationContext {

    /**
     * tiny web application context 根名称
     */
    String ROOT_WEB_APPLICATION_CONTEXT_NAME = TinyWebApplicationContext.class.getName() + ".ROOT";

    String PATH_START = "/";

    String APPLICATION_TINY_TYPE = "tiny";

    String APPLICATION_JNDI_TYPE = "jndi";


    void destroyWebApplication();

    SupportMethodInfo getSupportMethod(String requestPath);
}
