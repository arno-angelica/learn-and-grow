package com.arno.learn.grow.tiny.configuration;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.Converter;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/3/22 下午10:03
 * @version:
 */
public class DefaultConfigBuilder implements ConfigBuilder {

    @Override
    public ConfigBuilder addDefaultSources() {
        return null;
    }

    @Override
    public ConfigBuilder addDiscoveredSources() {
        return null;
    }

    @Override
    public ConfigBuilder addDiscoveredConverters() {
        return null;
    }

    @Override
    public ConfigBuilder forClassLoader(ClassLoader classLoader) {
        return null;
    }

    @Override
    public ConfigBuilder withSources(ConfigSource... configSources) {
        return null;
    }

    @Override
    public ConfigBuilder withConverters(Converter<?>... converters) {
        return null;
    }

    @Override
    public <T> ConfigBuilder withConverter(Class<T> aClass, int i, Converter<T> converter) {
        return null;
    }

    @Override
    public Config build() {
        return null;
    }
}
