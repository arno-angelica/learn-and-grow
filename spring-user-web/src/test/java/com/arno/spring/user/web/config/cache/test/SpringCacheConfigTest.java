package com.arno.spring.user.web.config.cache.test;

import com.arno.spring.user.web.config.cache.SpringRedisConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Angelica.arno
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringCacheConfigTest.class, SpringRedisConfig.class, TestService.class})
public class SpringCacheConfigTest {

    @Autowired
    private TestService testService;


    @Test
    public void testSpringCache() {
        System.out.println(testService.getName("3"));
    }
}
