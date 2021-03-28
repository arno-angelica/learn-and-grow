package com.arno.grow.user.web.configuration;

import com.arno.learn.grow.tiny.web.servlet.ServletConfigInitializer;
import com.arno.learn.grow.tiny.web.servlet.ServletMapBasedConfigSource;
import org.eclipse.microprofile.config.spi.ConfigSource;

import javax.servlet.ServletContext;
import java.util.Enumeration;
import java.util.Map;

/**
 * @Author: angelica
 * @Date: 2021/3/24 16:37
 * @Description:
 */
public class ServletContextParamConfig extends ServletMapBasedConfigSource implements ServletConfigInitializer {

    public ServletContextParamConfig(){}

    public ServletContextParamConfig(ServletContext servletContext){
        super("ServletContext Init Parameters", 500, servletContext);
    }

    @Override
    public ConfigSource loadConfig(ServletContext servletContext) {
        return new ServletContextParamConfig(servletContext);
    }

    @Override
    protected void prepareServletData(Map configData) throws Throwable {
        Enumeration<String> parameterNames = servletContext.getInitParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            configData.put(parameterName, servletContext.getInitParameter(parameterName));
        }
    }

}
