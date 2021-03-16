package com.arno.learn.grow.tiny.web.context;

import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletContext;
import java.util.Properties;
import java.util.logging.Logger;

import static com.arno.learn.grow.tiny.core.util.ReloadConfigUtils.loadConfigFile;
import static com.arno.learn.grow.tiny.web.context.TinyWebApplicationContext.APPLICATION_JNDI_TYPE;
import static com.arno.learn.grow.tiny.web.context.TinyWebApplicationContext.APPLICATION_TINY_TYPE;

/**
 * @desc: 主要处理类
 * @author: Arno.KV
 * @date: 2021/3/7 上午12:11
 * @version:
 */
public class TinyContextLoader {

    protected static final Logger logger = Logger.getLogger(TinyContextLoader.class.getName());

    private static final String APPLICATION_CONTEXT_TYPE = "application.context.type";

    private TinyWebApplicationContext context;

    /**
     * 初始化 web 上下文
     * @param servletContext
     * @return
     */
    public TinyWebApplicationContext initApplicationContext(ServletContext servletContext) {
        if (servletContext.getAttribute(TinyWebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_NAME) != null) {
            throw new IllegalStateException("Cannot initialize application context because there is already exist, please check your web.xml!");
        }
        long startTime = System.currentTimeMillis();
        // 读取配置文件信息
        Properties defaultProperties = loadConfigFile(servletContext.getClassLoader());
        String applicationContextType = defaultProperties.getProperty(APPLICATION_CONTEXT_TYPE);
        if (StringUtils.isBlank(applicationContextType)) {
            applicationContextType = APPLICATION_TINY_TYPE;
        }
        // 创建 applicationContext
        if (APPLICATION_JNDI_TYPE.equalsIgnoreCase(applicationContextType)) {
            context = new JavaNamingInitializeWebApplicationContext(servletContext, defaultProperties);
        } else {
            context = createTinyWebApplicationContext(defaultProperties, servletContext);
        }
        // 写入 servlet Context 上下文中
        servletContext.setAttribute(TinyWebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_NAME, context);
        long elapsedTime = System.currentTimeMillis() - startTime;
        logger.info("Tiny WebApplicationContext initialized in " + elapsedTime + " ms");
        return context;
    }

    /**
     * 销毁
     */
    public void destroy() {
        if (context == null) return;
        context.destroyWebApplication();
    }

    /**
     * 默认 tiny 实现
     * @param defaultProperties
     * @param servletContext
     * @return
     */
    protected TinyWebApplicationContext createTinyWebApplicationContext(Properties defaultProperties, ServletContext servletContext) {
        return new TinyDefaultInitializeWebApplicationContext(defaultProperties);
    }

}
