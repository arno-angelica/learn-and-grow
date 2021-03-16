package com.arno.grow.user.web.configuration;

import com.arno.learn.grow.tiny.configuration.DefaultConfigProviderResolver;
import com.arno.learn.grow.tiny.web.annotation.Configuration;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import javax.annotation.PostConstruct;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/3/16 下午10:48
 * @version:
 */
@Configuration
public class CommonConfig {

    private Config config;

    @PostConstruct
    public void init() {
        DefaultConfigProviderResolver providerResolver = new DefaultConfigProviderResolver();
        this.config = providerResolver.getConfig();
    }

    public Config getConfig() {
        return config;
    }
}
