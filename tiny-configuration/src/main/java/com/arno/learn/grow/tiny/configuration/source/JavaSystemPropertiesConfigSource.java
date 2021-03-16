package com.arno.learn.grow.tiny.configuration.source;

import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class JavaSystemPropertiesConfigSource implements ConfigSource {

    /**
     * Java 系统属性最好通过本地变量保存，使用 Map 保存，尽可能运行期不去调整
     * -Dapplication.name=user-web
     */
    private final Map<Object, Object> properties;

    public JavaSystemPropertiesConfigSource() {
        this.properties = System.getProperties();
    }

    @Override
    public Set<String> getPropertyNames() {
        return properties.keySet().stream().filter(Objects::nonNull).map(String::valueOf).collect(Collectors.toSet());
    }

    @Override
    public String getValue(String propertyName) {
        Object obj = properties.get(propertyName);
        return obj == null ? null : String.valueOf(obj);
    }

    @Override
    public String getName() {
        return "Java System Properties";
    }

    @Override
    public int getOrdinal() {
        return 400;
    }
}
