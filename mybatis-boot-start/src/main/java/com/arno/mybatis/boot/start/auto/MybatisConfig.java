package com.arno.mybatis.boot.start.auto;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

/**
 * @author Arno
 * @since 1.0.0
 */
@org.springframework.context.annotation.Configuration
@ConfigurationProperties(prefix = "mybatis")
public class MybatisConfig {
    private String dataSource;

    /**
     * the location of {@link Mapper}
     */
    private Resource[] mapperLocation;

    private String[] basePackages = {"com.arno.mybatis.boot.start.auto.mapper"};

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public Resource[] getMapperLocation() {
        return mapperLocation;
    }

    public void setMapperLocation(Resource[] mapperLocation) {
        this.mapperLocation = mapperLocation;
    }

    public String[] getBasePackages() {
        return basePackages;
    }

    public void setBasePackages(String[] basePackages) {
        this.basePackages = basePackages;
    }
}
