package com.arno.learn.grow.tiny.web.servlet;

import com.arno.learn.grow.tiny.configuration.DefaultConfigProviderResolver;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigSource;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.arno.learn.grow.tiny.web.context.TinyWebApplicationContext.CONFIG_ATTRIBUTE;

/**
 * @Author: angelica
 * @Date: 2021/3/23 14:28
 * @Description:
 */
@HandlesTypes({ServletConfigInitializer.class, ServletStartupInitializer.class})
public class ServletInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> contextClasses, ServletContext servletContext) throws ServletException {
        List<ServletConfigInitializer> configInitializers = new ArrayList<>();
        List<ServletStartupInitializer> startupInitializers = new LinkedList<>();
        if (contextClasses != null) {
            for (Class<?> clazz : contextClasses) {
                try {
                    // 如果当前类未初始化且非抽象类且是 ServletContextInitializer 的实现类
                    if (!clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers()) &&
                            ServletConfigInitializer.class.isAssignableFrom(clazz)) {
                        configInitializers.add((ServletConfigInitializer) clazz.newInstance());
                    } else if (!clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers()) &&
                            ServletStartupInitializer.class.isAssignableFrom(clazz)) {
                        startupInitializers.add((ServletStartupInitializer) clazz.newInstance());
                    }
                } catch (Throwable t) {
                    throw new ServletException("Failed to instantiate ServletConfigInitializer", t);
                }
            }
        }
        loadContextInitializers(servletContext, configInitializers, startupInitializers);
    }

    /**
     * 初始化
     * @param servletContext servlet context
     * @param configInitializers 配置源实现
     * @param startupInitializers 自定义实现
     */
    private void loadContextInitializers(ServletContext servletContext,
                                         List<ServletConfigInitializer> configInitializers,
                                         List<ServletStartupInitializer> startupInitializers) {
        List<ConfigSource> configSources = new ArrayList<>();
        for (ServletConfigInitializer initializer : configInitializers) {
            ConfigSource configSource = initializer.loadConfig(servletContext);
            if (configSource != null) {
                configSources.add(configSource);
            }
        }
        // 初始化 config，并存到 servletContext attribute 中
        initConfigProviderResolver(servletContext, configSources.toArray(new ConfigSource[0]));
//        servletContext.addListener(TinyContextLoaderListener.class);
        // 排序后依次执行
        OrderComparator.sort(startupInitializers);
        for (ServletStartupInitializer initializer : startupInitializers) {
            initializer.loadUp(servletContext);
        }
    }


    /**
     * 初始化配置
     *
     * @param servletContext 容器上下文
     * @param configSources  自定义 config 配置源
     */
    private void initConfigProviderResolver(ServletContext servletContext, ConfigSource[] configSources) {
        ClassLoader classLoader = servletContext.getClassLoader();
        DefaultConfigProviderResolver configProviderResolver = DefaultConfigProviderResolver.instance();
        ConfigBuilder configBuilder = configProviderResolver.getBuilder();
        // 配置 ClassLoader
        configBuilder.forClassLoader(classLoader);
        if (configSources.length > 0) {
            // 增加扩展配置源（基于 Servlet 引擎）
            configBuilder.withSources(configSources);
        }
        // 默认配置源（内建的，静态的）
        configBuilder.addDefaultSources();
        // 通过发现配置源（动态的）
        configBuilder.addDiscoveredConverters();
        // 获取 Config
        Config config = configBuilder.build();
        // 注册 Config 关联到当前 ClassLoader
        configProviderResolver.registerConfig(config, classLoader);

        servletContext.setAttribute(CONFIG_ATTRIBUTE, configProviderResolver);
    }
}
