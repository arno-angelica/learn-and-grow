package com.arno.learn.grow.tiny.configuration.source;

import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/3/22 下午10:05
 * @version:
 */
public class ConfigSourcesManager implements Iterable<ConfigSource> {

    /**
     * 是否已加载默认配置源
     */
    private boolean addedDefaultSources;

    /**
     * 是否已加载 SPI 发现的配置源
     */
    private boolean addedDiscoveredConfigSources;

    /**
     * 类加载器
     */
    private ClassLoader classLoader;

    /**
     * 配置源列表
     */
    private List<ConfigSource> configSources = new LinkedList<>();

    public ConfigSourcesManager(ClassLoader classLoader) {
        setClassLoader(classLoader);
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public Iterator<ConfigSource> iterator() {
        return null;
    }
}
