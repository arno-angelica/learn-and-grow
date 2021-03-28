package com.arno.learn.grow.tiny.configuration.source;

import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static java.util.ServiceLoader.load;
import static java.util.stream.Stream.of;

/**
 * @desc:
 * @author: angelica
 * @date: 2021/3/22 下午10:05
 * @version:
 */
public class ConfigSourcesManager implements Iterable<ConfigSource> {

    /**
     * 是否已加载默认配置源
     */
    private boolean addedDefaultConfigSources;

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
        this.classLoader = classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void addDefaultSources() {
        if (addedDefaultConfigSources) {
            return;
        }
        addConfigSources(JavaSystemPropertiesConfigSource.class,
                DefaultResourceConfigSource.class,
                OperationSystemEnvironmentVariablesConfigSource.class
        );
        addedDefaultConfigSources = true;
    }

    public void addDiscoveredSources() {
        if (addedDiscoveredConfigSources) {
            return;
        }

        addConfigSources(load(ConfigSource.class, classLoader));
        addedDiscoveredConfigSources = true;
    }

    @SafeVarargs
    public final void addConfigSources(Class<? extends ConfigSource>... configSourceClasses) {
        addConfigSources(
                of(configSourceClasses)
                        .map(this::newInstance)
                        .toArray(ConfigSource[]::new)
        );
    }

    public void addConfigSources(ConfigSource... configSources) {
        addConfigSources(Arrays.asList(configSources));
    }

    public void addConfigSources(Iterable<ConfigSource> configSources) {
        configSources.forEach(this.configSources::add);
        this.configSources.sort(ConfigSourceOrdinalComparator.INSTANCE);
    }

    private ConfigSource newInstance(Class<? extends ConfigSource> configSourceClass) {
        ConfigSource instance = null;
        try {
            instance = configSourceClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
        return instance;
    }

    @Override
    public Iterator<ConfigSource> iterator() {
        return configSources.iterator();
    }

    public boolean isAddedDefaultConfigSources() {
        return addedDefaultConfigSources;
    }

    public boolean isAddedDiscoveredConfigSources() {
        return addedDiscoveredConfigSources;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }
}
