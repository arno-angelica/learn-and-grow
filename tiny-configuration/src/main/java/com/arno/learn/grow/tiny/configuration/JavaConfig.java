package com.arno.learn.grow.tiny.configuration;


import com.arno.learn.grow.tiny.configuration.converter.Converter;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigValue;
import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;

public class JavaConfig implements Config {

    /**
     * 内部可变的集合，不要直接暴露在外面
     */
    private final List<ConfigSource> configSources = new LinkedList<>();

    private final Map<String, Converter> converterMap = new HashMap<>();

    private static final Comparator<ConfigSource> configSourceComparator = (o1, o2) -> Integer.compare(o2.getOrdinal(), o1.getOrdinal());

    public JavaConfig() {
        ClassLoader classLoader = getClass().getClassLoader();
        ServiceLoader<ConfigSource> serviceLoader = ServiceLoader.load(ConfigSource.class, classLoader);
        serviceLoader.forEach(configSources::add);
        // 排序
        configSources.sort(configSourceComparator);
        ServiceLoader<Converter> convertServiceLoader = ServiceLoader.load(Converter.class, classLoader);
        convertServiceLoader.forEach(item -> converterMap.put(item.covertType(), item));
    }

    @Override
    public <T> T getValue(String propertyName, Class<T> propertyType) {
        String propertyValue = getPropertyValue(propertyName);
        Converter converter = converterMap.get(propertyType.getSimpleName());
        if (converter == null) {
            return (T) propertyValue;
        }
        // String 转换成目标类型
        return (T) converter.convert(propertyValue);
    }

    @Override
    public ConfigValue getConfigValue(String propertyName) {
        return null;
    }

    protected String getPropertyValue(String propertyName) {
        String propertyValue = null;
        for (ConfigSource configSource : configSources) {
            propertyValue = configSource.getValue(propertyName);
            if (propertyValue != null) {
                break;
            }
        }
        return propertyValue;
    }

    @Override
    public <T> Optional<T> getOptionalValue(String propertyName, Class<T> propertyType) {
        T value = getValue(propertyName, propertyType);
        return Optional.ofNullable(value);
    }

    @Override
    public Iterable<String> getPropertyNames() {
        return null;
    }

    @Override
    public Iterable<ConfigSource> getConfigSources() {
        return Collections.unmodifiableList(configSources);
    }

    @Override
    public <T> Optional<org.eclipse.microprofile.config.spi.Converter<T>> getConverter(Class<T> forType) {
        return Optional.empty();
    }

    @Override
    public <T> T unwrap(Class<T> type) {
        return null;
    }
}
