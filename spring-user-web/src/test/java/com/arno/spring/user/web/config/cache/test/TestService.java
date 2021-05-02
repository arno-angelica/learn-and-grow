package com.arno.spring.user.web.config.cache.test;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author Angelica.arno
 * @since 1.0.0
 */
@Service
public class TestService {

    @Cacheable(cacheNames = "spring.redis.cache", key = "#id", cacheManager = "springCacheManager")
    public String getName(String id) {
        return "test" + id;
    }
}
