package com.arno.learn.grow.tiny.configuration.source;

import org.eclipse.microprofile.config.spi.ConfigSource;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/3/16 下午10:42
 * @version:
 */
public class ClassPathPropertiesConfigSource implements ConfigSource {

    private static final String CONFIG_FILE = "config.properties";

    private Map<Object, Object> propertiesMap;

    public ClassPathPropertiesConfigSource() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL url = cl.getResource(CONFIG_FILE);
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(url.getFile()));
            Properties properties = new Properties();
            properties.load(in);
            this.propertiesMap = properties;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public Set<String> getPropertyNames() {
        return propertiesMap.keySet().stream().filter(Objects::nonNull).map(String::valueOf).collect(Collectors.toSet());
    }

    @Override
    public String getValue(String propertyName) {
        Object obj = propertiesMap.get(propertyName);
        return obj == null ? null : String.valueOf(obj);
    }

    @Override
    public String getName() {
        return "Class Path Properties";
    }

    @Override
    public int getOrdinal() {
        return 200;
    }
}
