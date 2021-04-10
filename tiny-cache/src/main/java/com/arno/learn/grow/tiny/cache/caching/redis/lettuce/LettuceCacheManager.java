package com.arno.learn.grow.tiny.cache.caching.redis.lettuce;

import com.arno.learn.grow.tiny.cache.AbstractCacheManager;
import io.lettuce.core.RedisClient;

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
public class LettuceCacheManager extends AbstractCacheManager {

    private final RedisClient redisClient;

    public LettuceCacheManager(CachingProvider cachingProvider, URI uri, ClassLoader classLoader, Properties properties) {
        super(cachingProvider, uri, classLoader, properties);
        this.redisClient = RedisClient.create(uri.toString());
    }

    @Override
    protected <K, V, C extends Configuration<K, V>> Cache doCreateCache(String cacheName, C configuration) {
        return new LettuceCache(this, cacheName, configuration, redisClient.connect());
    }

    @Override
    protected void doClose() {
        redisClient.shutdown();
    }
}
