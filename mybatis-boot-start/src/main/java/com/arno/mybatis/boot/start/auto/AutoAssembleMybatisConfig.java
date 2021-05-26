package com.arno.mybatis.boot.start.auto;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Arno
 * @since 1.0.0
 */
@Configuration
public class AutoAssembleMybatisConfig implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(MybatisConfig.class)
    public SqlSessionFactoryBean sqlSessionFactoryBean(MybatisConfig config) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(applicationContext.getBean(config.getDataSource(), DataSource.class));
        if (config.getMapperLocation() != null) {
            sqlSessionFactoryBean.setMapperLocations(config.getMapperLocation());
        }
        return sqlSessionFactoryBean;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(MybatisConfig.class)
    public MapperScannerConfigurer mapperScannerConfigurer(MybatisConfig config) {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactoryBean");
        if (config.getBasePackages() != null && config.getBasePackages().length > 0) {
            mapperScannerConfigurer.setBasePackage(StringUtils.collectionToCommaDelimitedString(Arrays.stream(config.getBasePackages()).filter(StringUtils::hasText)
                    .collect(Collectors.toList())));
        }
        return mapperScannerConfigurer;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
