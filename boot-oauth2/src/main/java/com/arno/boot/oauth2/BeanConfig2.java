package com.arno.boot.oauth2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @Author: lingquan.liu@quvideo.com
 * @Date: 2021/4/28 10:53
 * @Description:
 */
@Configuration
public class BeanConfig2 {
    @Bean
    public SecurityFilterChain filterChain1(HttpSecurity http) throws Exception {
        return http.antMatcher("/**").build();
    }
}
