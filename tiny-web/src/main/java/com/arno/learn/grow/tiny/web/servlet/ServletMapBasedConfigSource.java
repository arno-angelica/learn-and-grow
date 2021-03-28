package com.arno.learn.grow.tiny.web.servlet;

import com.arno.learn.grow.tiny.configuration.source.MapBasedConfigSource;

import javax.servlet.ServletContext;
import java.util.Map;

/**
 * @desc:
 * @author: angelica
 * @date: 2021/3/28 上午12:31
 * @version:
 */
public abstract class ServletMapBasedConfigSource extends MapBasedConfigSource {

    protected ServletContext servletContext;

    protected ServletMapBasedConfigSource(){}

    public ServletMapBasedConfigSource(String name, int ordinal, ServletContext servletContext) {
        super(name, ordinal, true);
        this.servletContext = servletContext;
        setSource();
    }

    @Override
    protected void prepareConfigData(Map configData) throws Throwable {
        prepareServletData(configData);
    }

    protected abstract void prepareServletData(Map configData) throws Throwable;
}
