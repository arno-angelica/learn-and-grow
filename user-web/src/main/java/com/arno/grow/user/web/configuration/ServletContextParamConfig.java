package com.arno.grow.user.web.configuration;

import com.arno.learn.grow.tiny.configuration.source.MapBasedConfigSource;
import com.arno.learn.grow.tiny.web.servlet.ServletConfigInitializer;
import org.eclipse.microprofile.config.spi.ConfigSource;

import javax.servlet.ServletContext;
import java.util.Enumeration;
import java.util.Map;

/**
 * @Author: Arno.KV
 * @Date: 2021/3/24 16:37
 * @Description:
 */
public class ServletContextParamConfig extends MapBasedConfigSource implements ServletConfigInitializer {

    @Override
    public ConfigSource loadConfig(ServletContext servletContext) {
        return new ServletContextParamConfig(servletContext);
    }
    public ServletContextParamConfig(){}

    public ServletContextParamConfig(ServletContext servletContext){
        super("ServletContext Init Parameters", 500, servletContext);
    }

    @Override
    protected void prepareConfigData(Map configData) throws Throwable {
        Enumeration<String> parameterNames = servletContext.getInitParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            configData.put(parameterName, servletContext.getInitParameter(parameterName));
        }
    }

}
