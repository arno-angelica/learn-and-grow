package com.arno.learn.grow.tiny.cache.caching.memory;

import com.arno.learn.grow.tiny.cache.AbstractCacheManager;

import javax.cache.Cache;
import javax.cache.configuration.Configuration;
import javax.cache.spi.CachingProvider;
import java.net.URI;
import java.util.Properties;

/**
 * Desc:
 * Author: angelica
 * Date: 2021/4/10
 */
public class InMemoryCacheManager extends AbstractCacheManager {

    public InMemoryCacheManager(CachingProvider cachingProvider, URI uri, ClassLoader classLoader, Properties properties) {
        super(cachingProvider, uri, classLoader, properties);
    }

    @Override
    protected <K, V, C extends Configuration<K, V>> Cache doCreateCache(String cacheName, C configuration) {
        return new InMemoryCache<K, V>(this, cacheName, configuration);
    }
}
