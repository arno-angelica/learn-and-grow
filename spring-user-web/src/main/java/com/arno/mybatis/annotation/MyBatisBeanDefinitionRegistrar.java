package com.arno.mybatis.annotation;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScannerRegistrar;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.beans.factory.support.BeanDefinitionBuilder.genericBeanDefinition;

/**
 * @author Angelica.arno
 * @since 1.0.0
 */
public class MyBatisBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        Map<String, Object> attributes = metadata.getAnnotationAttributes(EnableMybatis.class.getName());
        AnnotationAttributes mapperScanAttrs = AnnotationAttributes.fromMap(attributes);
        registerBeanSqlSessionFactory(mapperScanAttrs, registry);
        registerMapperScanConfig(registry, mapperScanAttrs, generateBaseBeanName(metadata, 0));
    }

    /**
     * register SqlSessionFactory Bean
     * @param attributes
     * @param registry
     * @see SqlSessionFactoryBean
     */
    private void registerBeanSqlSessionFactory(AnnotationAttributes attributes, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder beanDefinitionBuilder = genericBeanDefinition(SqlSessionFactoryBean.class);
        beanDefinitionBuilder.addPropertyReference("dataSource", attributes.getString("dataSource"));
        // Spring String 类型可以自动转化 Spring Resource
        if (attributes.getStringArray("mapperLocations").length > 0) {
            beanDefinitionBuilder.addPropertyValue("mapperLocations", attributes.getStringArray("mapperLocations"));
        }
        if (StringUtils.hasText(attributes.getString("environment"))) {
            beanDefinitionBuilder.addPropertyValue("environment", resolvePlaceholder(attributes.get("environment")));
        }
        if (StringUtils.hasText(attributes.getString("typeHandlersPackage"))) {
            beanDefinitionBuilder.addPropertyValue("typeHandlersPackage", attributes.getString("typeHandlersPackage"));
        }
        if (StringUtils.hasText(attributes.getString("configLocation"))) {
            beanDefinitionBuilder.addPropertyValue("configLocation", attributes.getString("configLocation"));
        }
        if (StringUtils.hasText(attributes.getString("cache"))) {
            beanDefinitionBuilder.addPropertyReference("cache", attributes.getString("cache"));
        }
        if (StringUtils.hasText(attributes.getString("transactionFactory"))) {
            beanDefinitionBuilder.addPropertyReference("transactionFactory", (String) attributes.get("transactionFactory"));
        }
        if (StringUtils.hasText(attributes.getString("sqlSessionFactory"))) {
            beanDefinitionBuilder.addPropertyReference("sqlSessionFactory", (String) attributes.get("sqlSessionFactory"));
        }
        beanDefinitionBuilder.addPropertyValue("failFast", attributes.getBoolean("failFast"));
        BeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
        String beanName = (String) attributes.get("value");
        registry.registerBeanDefinition(beanName, beanDefinition);
    }

    /**
     * register MapperScannerConfigurer
     * @param registry
     * @param annoAttrs
     * @param beanName
     * @see MapperScannerConfigurer
     */
    private void registerMapperScanConfig(BeanDefinitionRegistry registry,
                                          AnnotationAttributes annoAttrs, String beanName) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class);
        builder.addPropertyValue("processPropertyPlaceHolders", true);

        Class<? extends Annotation> annotationClass = annoAttrs.getClass("annotationClass");
        if (!Annotation.class.equals(annotationClass)) {
            builder.addPropertyValue("annotationClass", annotationClass);
        }

        Class<?> markerInterface = annoAttrs.getClass("markerInterface");
        if (!Class.class.equals(markerInterface)) {
            builder.addPropertyValue("markerInterface", markerInterface);
        }

        Class<? extends BeanNameGenerator> generatorClass = annoAttrs.getClass("nameGenerator");
        if (!BeanNameGenerator.class.equals(generatorClass)) {
            builder.addPropertyValue("nameGenerator", BeanUtils.instantiateClass(generatorClass));
        }

        String sqlSessionTemplateRef = annoAttrs.getString("sqlSessionTemplateRef");
        if (StringUtils.hasText(sqlSessionTemplateRef)) {
            builder.addPropertyValue("sqlSessionTemplateBeanName", annoAttrs.getString("sqlSessionTemplateRef"));
        }

        String sqlSessionFactoryRef = annoAttrs.getString("sqlSessionFactoryRef");
        if (StringUtils.hasText(sqlSessionFactoryRef)) {
            builder.addPropertyValue("sqlSessionFactoryBeanName", annoAttrs.getString("sqlSessionFactoryRef"));
        } else {
            builder.addPropertyValue("sqlSessionFactoryBeanName", annoAttrs.get("value"));
        }
        List<String> basePackages = new ArrayList<>();
        basePackages.addAll(
                Arrays.stream(annoAttrs.getStringArray("value")).filter(StringUtils::hasText).collect(Collectors.toList()));

        basePackages.addAll(Arrays.stream(annoAttrs.getStringArray("basePackages")).filter(StringUtils::hasText)
                .collect(Collectors.toList()));

        basePackages.addAll(Arrays.stream(annoAttrs.getClassArray("basePackageClasses")).map(ClassUtils::getPackageName)
                .collect(Collectors.toList()));

        String lazyInitialization = annoAttrs.getString("lazyInitialization");
        if (StringUtils.hasText(lazyInitialization)) {
            builder.addPropertyValue("lazyInitialization", lazyInitialization);
        }

        String defaultScope = annoAttrs.getString("defaultScope");
        if (!AbstractBeanDefinition.SCOPE_DEFAULT.equals(defaultScope)) {
            builder.addPropertyValue("defaultScope", defaultScope);
        }

        builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(basePackages));

        registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
    }

    private Object resolvePlaceholder(Object value) {
        if (value instanceof String) {
            return environment.resolvePlaceholders((String) value);
        }
        return value;
    }

    /**
     * create MapperScannerConfigurer bean name
     * @param importingClassMetadata
     * @param index
     * @return
     */
    private static String generateBaseBeanName(AnnotationMetadata importingClassMetadata, int index) {
        return importingClassMetadata.getClassName() + "#" + MapperScannerRegistrar.class.getSimpleName() + "#" + index;
    }


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
