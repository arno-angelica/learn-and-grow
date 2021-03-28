package com.arno.learn.grow.tiny.web.servlet;

import org.eclipse.microprofile.config.spi.ConfigSource;

import javax.servlet.ServletContext;

/**
 * @Author: angelica
 * @Date: 2021/3/23 15:11
 * @Description:
 */
public interface ServletConfigInitializer {

    default ConfigSource loadConfig(ServletContext servletContext) {
        return null;
    }
}
