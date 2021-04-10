package com.arno.learn.grow.tiny.cache;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;
import java.net.URI;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

/**
 * Desc:
 * Author: angelica
 * Date: 2021/4/10
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractCacheManager implements CacheManager {

    private final Logger logger;

    private final static Consumer<Cache> CLEAR_CACHE_OPERATION = Cache::clear;

    private final static Consumer<Cache> CLOSE_CACHE_OPERATION = Cache::close;

    private final CachingProvider cachingProvider;

    private final URI uri;

    private final ClassLoader classLoader;

    private final Properties properties;

    private volatile boolean closed;

    private ConcurrentMap<String, Map<KeyValueTypePair, Cache>> cacheRepository = new ConcurrentHashMap<>();

    public AbstractCacheManager(CachingProvider cachingProvider, URI uri, ClassLoader classLoader, Properties properties) {
        this.cachingProvider = cachingProvider;
        this.uri = uri == null ? cachingProvider.getDefaultURI() : uri;
        this.properties = properties == null ? cachingProvider.getDefaultProperties() : properties;
        this.classLoader = classLoader == null ? cachingProvider.getDefaultClassLoader() : classLoader;
        this.logger = LoggerFactory.getLogger(this.getClass().getName());
    }

    @Override
    public CachingProvider getCachingProvider() {
        return cachingProvider;
    }

    @Override
    public URI getURI() {
        return uri;
    }

    @Override
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    @Override
    public Properties getProperties() {
        return properties;
    }

    @Override
    public <K, V, C extends Configuration<K, V>> Cache<K, V> createCache(String cacheName, C configuration) throws IllegalArgumentException {
        return getOrCreateCache(cacheName, configuration, true);
    }

    @Override
    public <K, V> Cache<K, V> getCache(String cacheName, Class<K> keyType, Class<V> valueType) {
        MutableConfiguration<K, V> configuration = new MutableConfiguration<K, V>().setTypes(keyType, valueType);
        return getOrCreateCache(cacheName, configuration, false);
    }

    @Override
    public <K, V> Cache<K, V> getCache(String cacheName) {
        return getCache(cacheName, (Class<K>) Object.class, (Class<V>) Object.class);
    }

    @Override
    public Iterable<String> getCacheNames() {
        assertNotClosed();
        return cacheRepository.keySet();
    }

    @Override
    public void destroyCache(String cacheName) {
        assertNotClosed();
        Map<KeyValueTypePair, Cache> cacheMap = cacheRepository.get(cacheName);
        if (cacheMap != null) {
            iterateCaches(cacheMap.values(), CLEAR_CACHE_OPERATION, CLOSE_CACHE_OPERATION);
        }
    }

    @Override
    public void enableManagement(String cacheName, boolean enabled) {
        assertNotClosed();
        // TODO
    }

    @Override
    public void enableStatistics(String cacheName, boolean enabled) {
        assertNotClosed();
        // TODO
    }

    @Override
    public final void close() {
        if (isClosed()) {
            logger.warn("The CacheManager has closed");
            return;
        }
        for (Map<KeyValueTypePair, Cache> cacheMap : cacheRepository.values()) {
            iterateCaches(cacheMap.values(), CLOSE_CACHE_OPERATION);
        }
        doClose();
        this.closed = true;
    }

    @Override
    public boolean isClosed() {
        return this.closed;
    }

    @Override
    public <T> T unwrap(Class<T> clazz) {
        return null;
    }

    protected abstract <K, V, C extends Configuration<K, V>> Cache doCreateCache(String cacheName, C configuration);

    protected void doClose() {
    }

    protected <K, V, C extends Configuration<K, V>> Cache<K, V> getOrCreateCache(String cacheName, C configuration,
                                                                                 boolean created) throws IllegalArgumentException, IllegalStateException {
        assertNotClosed();

        Map<KeyValueTypePair, Cache> cacheMap = cacheRepository.computeIfAbsent(cacheName, n -> new ConcurrentHashMap<>());

        return cacheMap.computeIfAbsent(new KeyValueTypePair(configuration.getKeyType(), configuration.getValueType()),
                key -> created ? doCreateCache(cacheName, configuration) : null);
    }

    @SafeVarargs
    protected final void iterateCaches(Iterable<Cache> caches, Consumer<Cache>... cacheOperations) {
        for (Cache cache : caches) {
            for (Consumer<Cache> cacheOperation : cacheOperations) {
                cacheOperation.accept(cache);
            }
        }
    }

    private void assertNotClosed() throws IllegalStateException {
        if (isClosed()) {
            throw new IllegalStateException("The CacheManager has been closed, current operation should not be invoked!");
        }
    }
}
