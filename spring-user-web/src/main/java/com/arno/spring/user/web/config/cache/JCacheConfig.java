package com.arno.spring.user.web.config.cache;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;
import java.net.URI;

/**
 * @author Angelica.arno
 * @since 1.0.0
 */
@EnableCaching
@Configuration
public class JCacheConfig {

    private static final String CACHE_NAME = "redisCache";

    @Bean
    public CacheManager redisCacheManager() {
        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager(URI.create("redis://127.0.0.1:6379/"), null);
        // configure the cache
        MutableConfiguration<Object, Object> config =
                new MutableConfiguration<>()
                        .setTypes(Object.class, Object.class);
        cacheManager.createCache(CACHE_NAME, config);
        return cacheManager;
    }

    @Bean
    public Cache<Object, Object> jCacheTemplate() {
        return redisCacheManager().getCache(CACHE_NAME);
    }

    @Bean
    public JCacheCacheManager jCacheCacheManager() {
        return new JCacheCacheManager(redisCacheManager());
    }
}
