package com.arno.learn.grow.tiny.configuration;

import com.arno.learn.grow.tiny.configuration.converter.ConvertersManager;
import com.arno.learn.grow.tiny.configuration.source.ConfigSourcesManager;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.Converter;

/**
 * @see {@link ConfigSourcesManager}
 */
public class DefaultConfigBuilder implements ConfigBuilder {

    private final ConfigSourcesManager configSourcesManager;

    private final ConvertersManager convertersManager;

    public DefaultConfigBuilder(ClassLoader classLoader) {
        this.configSourcesManager = new ConfigSourcesManager(classLoader);
        this.convertersManager = new ConvertersManager(classLoader);
    }

    @Override
    public ConfigBuilder addDefaultSources() {
        configSourcesManager.addDefaultSources();
        return this;
    }

    @Override
    public ConfigBuilder addDiscoveredSources() {
        configSourcesManager.addDiscoveredSources();
        return this;
    }

    @Override
    public ConfigBuilder addDiscoveredConverters() {
        convertersManager.addDiscoveredConverters();
        return this;
    }

    @Override
    public ConfigBuilder forClassLoader(ClassLoader loader) {
        configSourcesManager.setClassLoader(loader);
        convertersManager.setClassLoader(loader);
        return this;
    }

    @Override
    public ConfigBuilder withSources(ConfigSource... sources) {
        configSourcesManager.addConfigSources(sources);
        return this;
    }

    @Override
    public ConfigBuilder withConverters(Converter<?>... converters) {
        this.convertersManager.addConverters(converters);
        return this;
    }

    @Override
    public <T> ConfigBuilder withConverter(Class<T> type, int priority, Converter<T> converter) {
        this.convertersManager.addConverter(converter, priority, type);
        return this;
    }

    @Override
    public Config build() {
        Config config = new DefaultConfig(configSourcesManager, convertersManager);
        return config;
    }
}
