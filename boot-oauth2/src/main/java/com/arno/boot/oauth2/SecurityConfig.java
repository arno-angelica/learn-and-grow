package com.arno.boot.oauth2;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @Author: lingquan.liu@quvideo.com
 * @Date: 2021/4/26 11:26
 * @Description:
 */
//@Configuration
@Order(10000)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/enable").csrf();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }
}