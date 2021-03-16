package com.arno.learn.grow.tiny.core.util;


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/3/7 下午6:44
 * @version:
 */
public class ReloadConfigUtils {

    private static final String TINY_CONFIG_PATH = "tiny.config.path";
    private static final String DEFAULT_CONFIG = "application.properties";

    private static Properties properties;
    /**
     * 加载配置文件
     * @param cl
     */
    public static Properties loadConfigFile(ClassLoader cl) {
        if (properties != null) {
            return properties;
        }
        String systemPath = System.getProperty(TINY_CONFIG_PATH);
        if (!StringUtils.hasText(systemPath)) {
            systemPath = DEFAULT_CONFIG;
        }
        // 获取配置文件
        URL url = cl.getResource(systemPath);
        if (url == null) {
            throw new RuntimeException("application.properties does not exist");
        }
        // 读取配置文件
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(url.getFile()));
            properties = new Properties();
            properties.load(in);
            return properties;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
