package com.arno.spring.user.web.config.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;
import redis.clients.jedis.JedisPool;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Angelica.arno
 * @since 1.0.0
 */
public class SpringCacheRedisManager extends AbstractCacheManager {

    private final JedisPool jedisPool;

    public SpringCacheRedisManager(URI uri) {
        jedisPool = new JedisPool(uri);
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return Collections.singletonList(new RedisCache(jedisPool.getResource(), "spring.redis.cache"));
    }
}
