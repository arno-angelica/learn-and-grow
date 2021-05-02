package com.arno.spring.user.web.config.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

/**
 * @author Angelica.arno
 * @since 1.0.0
 */
@EnableCaching
@Configuration
public class SpringRedisConfig {

    @Bean
    public CacheManager springCacheManager() {
        return new SpringCacheRedisManager(URI.create("redis://127.0.0.1:6379/"));
    }
}
