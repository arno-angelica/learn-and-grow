package com.arno.spring.user.web.config.cache.test;

import com.arno.spring.user.web.config.cache.JCacheConfig;
import com.arno.spring.user.web.config.cache.SpringRedisConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.cache.Cache;

/**
 * @author Angelica.arno
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JCacheConfigTest.class, JCacheConfig.class, SpringRedisConfig.class, TestService.class})
public class JCacheConfigTest {

    @Autowired
    private Cache<Object, Object> jCacheTemplate;

    @Test
    public void testJCacheInSpring() {
        jCacheTemplate.put("2", "测试test");
        System.out.println(jCacheTemplate.get("2"));
//        jCacheTemplate.remove("2");
    }

}
