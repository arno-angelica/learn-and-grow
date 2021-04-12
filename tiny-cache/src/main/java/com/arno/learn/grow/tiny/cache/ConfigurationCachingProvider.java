package com.arno.learn.grow.tiny.cache;

import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.OptionalFeature;
import javax.cache.spi.CachingProvider;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URL;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

import static java.lang.String.format;

/**
 * Desc:
 * Author: angelica
 * Date: 2021/4/10
 */
public class ConfigurationCachingProvider implements CachingProvider {

    /**
     * The resource name of {@link #getDefaultProperties() the default properties}.
     */
    public static final String DEFAULT_PROPERTIES_RESOURCE_NAME = "META-INF/default-caching-provider.properties";

    /**
     * The prefix of property name for the mappings of {@link CacheManager CacheManager}, e.g:
     * javax.cache.CacheManager.mappings.${uri.scheme}=com.acme.SomeSchemeCache
     */
    public static final String CACHE_MANAGER_MAPPINGS_PROPERTY_PREFIX = "javax.cache.CacheManager.mappings.";

    /**
     * Gets the encode format, default UTF-8 if not present
     */
    public static final String DEFAULT_ENCODING = System.getProperty("file.encoding", "UTF-8");

    /**
     * The cache default uri of {@link #getDefaultURI() DefaultURI}.
     */
    public static final URI DEFAULT_URL = URI.create("in-memory://localhost/");

    private Properties defaultProperties;

    private ConcurrentMap<String, CacheManager> cacheManagersRepository = new ConcurrentHashMap<>();


    @Override
    public ClassLoader getDefaultClassLoader() {
        ClassLoader classLoader = Caching.getDefaultClassLoader();
        if (classLoader == null) {
            classLoader = this.getClass().getClassLoader();
        }
        return classLoader;
    }

    @Override
    public URI getDefaultURI() {
        return DEFAULT_URL;
    }

    @Override
    public Properties getDefaultProperties() {
        if (this.defaultProperties == null) {
            this.defaultProperties = loadDefaultProperties();
        }
        return defaultProperties;
    }


    @Override
    public synchronized CacheManager getCacheManager(URI uri, ClassLoader classLoader, Properties properties) {
        final URI actualURI = getOrDefault(uri, this::getDefaultURI);
        final ClassLoader actualClassLoader = getOrDefault(classLoader, this::getDefaultClassLoader);
        final Properties actualProperties = getOrDefault(properties, this::getDefaultProperties);
        final String key = generateCacheManagerKey(actualURI, actualClassLoader, actualProperties);
        return cacheManagersRepository.computeIfAbsent(key, k ->
                newCacheManager(actualURI, actualClassLoader, actualProperties));
    }

    @Override
    public CacheManager getCacheManager(URI uri, ClassLoader classLoader) {
        return getCacheManager(uri, classLoader, getDefaultProperties());
    }

    @Override
    public CacheManager getCacheManager() {
        return getCacheManager(getDefaultURI(), getDefaultClassLoader(), getDefaultProperties());
    }


    @Override
    public synchronized void close(URI uri, ClassLoader classLoader) {
        for (CacheManager cacheManager : cacheManagersRepository.values()) {
            if (Objects.equals(cacheManager.getURI(), uri)
                    && Objects.equals(cacheManager.getClassLoader(), classLoader)) {
                cacheManager.close();
            }
        }
    }

    @Override
    public void close(ClassLoader classLoader) {
        this.close(getDefaultURI(), classLoader);
    }

    @Override
    public void close() {
        this.close(getDefaultURI(), getDefaultClassLoader());
    }

    @Override
    public boolean isSupported(OptionalFeature optionalFeature) {
        return false;
    }

    /**
     * Gets value， if value is null will be return defaultValue
     * @param value Object value
     * @param defaultValue Supplier
     * @return if value is null will be return defaultValue
     */
    private <T> T getOrDefault(T value, Supplier<T> defaultValue) {
        return value == null ? defaultValue.get() : value;
    }

    /**
     * load default properties
     * @return Properties
     */
    private Properties loadDefaultProperties() {
        ClassLoader classLoader = getDefaultClassLoader();
        Properties defaultProperties = new Properties();
        try {
            Enumeration<URL> defaultPropertiesResources = classLoader.getResources(DEFAULT_PROPERTIES_RESOURCE_NAME);
            while (defaultPropertiesResources.hasMoreElements()) {
                URL defaultPropertiesResource = defaultPropertiesResources.nextElement();
                try (InputStream inputStream = defaultPropertiesResource.openStream();
                     Reader reader = new InputStreamReader(inputStream, DEFAULT_ENCODING)) {
                    defaultProperties.load(reader);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return defaultProperties;
    }


    /**
     * create cache manager key，store in {@link #cacheManagersRepository cacheManagersRepository}
     * @param uri URI
     * @param classLoader classLoader
     * @param properties properties
     * @return key
     */
    private String generateCacheManagerKey(URI uri, ClassLoader classLoader, Properties properties) {
        return new StringBuilder(uri.toASCIIString())
                .append("-").append(classLoader)
                .append("-").append(properties).toString();
    }



    private CacheManager newCacheManager(URI uri, ClassLoader classLoader, Properties properties) {
        CacheManager cacheManager = null;
        try {
            Class<? extends AbstractCacheManager> cacheManagerClass = getCacheManagerClass(uri, classLoader, properties);
            Class[] parameterTypes = new Class[]{CachingProvider.class, URI.class, ClassLoader.class, Properties.class};
            Constructor<? extends AbstractCacheManager> constructor = cacheManagerClass.getConstructor(parameterTypes);
            cacheManager = constructor.newInstance(this, uri, classLoader, properties);
        } catch (Throwable e) {
            throw new CacheException(e);
        }
        return cacheManager;
    }



    private Class<? extends AbstractCacheManager> getCacheManagerClass(URI uri, ClassLoader classLoader, Properties properties)
            throws ClassNotFoundException, ClassCastException {

        String className = getCacheManagerClassName(uri, properties);
        Class<? extends AbstractCacheManager> cacheManagerImplClass = null;
        Class<?> cacheManagerClass = classLoader.loadClass(className);
        // The AbstractCacheManager class must be extended by the implementation class,
        // because the constructor of the implementation class must have four arguments in order:
        // [0] - CachingProvider
        // [1] - URI
        // [2] - ClassLoader
        // [3] - Properties
        if (!AbstractCacheManager.class.isAssignableFrom(cacheManagerClass)) {
            throw new ClassCastException(format("The implementation class of %s must extend %s",
                    CacheManager.class.getName(), AbstractCacheManager.class.getName()));
        }

        cacheManagerImplClass = (Class<? extends AbstractCacheManager>) cacheManagerClass;

        return cacheManagerImplClass;
    }


    private String getCacheManagerClassName(URI uri, Properties properties) {
        String propertyName = getCacheManagerClassNamePropertyName(uri);
        String className = properties.getProperty(propertyName);
        if (className == null) {
            throw new IllegalStateException(format("The implementation class name of %s that is the value of property '%s' " +
                    "must be configured in the Properties[%s]", CacheManager.class.getName(), propertyName, properties));
        }
        return className;
    }

    private static String getCacheManagerClassNamePropertyName(URI uri) {
        String scheme = uri.getScheme();
        return CACHE_MANAGER_MAPPINGS_PROPERTY_PREFIX + scheme;
    }

}