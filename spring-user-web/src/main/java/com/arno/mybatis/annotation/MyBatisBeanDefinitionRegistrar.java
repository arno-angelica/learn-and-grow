package com.arno.mybatis.annotation;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

import static org.springframework.beans.factory.support.BeanDefinitionBuilder.genericBeanDefinition;

/**
 * @author Angelica.arno
 * @since 1.0.0
 */
public class MyBatisBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder beanDefinitionBuilder = genericBeanDefinition(SqlSessionFactoryBean.class);
        Map<String, Object> attributes = importingClassMetadata.getAnnotationAttributes(EnableMybatis.class.getName());
        beanDefinitionBuilder.addPropertyReference("dataSource", (String) attributes.get("dataSource"));
        // Spring String 类型可以自动转化 Spring Resource
        beanDefinitionBuilder.addPropertyValue("configLocation", attributes.get("configLocation"));
        beanDefinitionBuilder.addPropertyValue("mapperLocations", attributes.get("mapperLocations"));
        beanDefinitionBuilder.addPropertyValue("environment", resolvePlaceholder(attributes.get("environment")));
        BeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
        String beanName = (String) attributes.get("value");
        registry.registerBeanDefinition(beanName, beanDefinition);
    }

    /**
     *
     *   private Configuration configuration;
     *
     *   private Resource[] mapperLocations;
     *
     *   private DataSource dataSource;
     *
     *   private TransactionFactory transactionFactory;
     *
     *   private Properties configurationProperties;
     *
     *   private SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
     *
     *   private SqlSessionFactory sqlSessionFactory;
     *
     *   // EnvironmentAware requires spring 3.1
     *   private String environment = SqlSessionFactoryBean.class.getSimpleName();
     *
     *   private boolean failFast;
     *
     *   private Interceptor[] plugins;
     *
     *   private TypeHandler<?>[] typeHandlers;
     *
     *   private String typeHandlersPackage;
     *
     *   @SuppressWarnings("rawtypes")
     *   private Class<? extends TypeHandler> defaultEnumTypeHandler;
     *
     *   private Class<?>[] typeAliases;
     *
     *   private String typeAliasesPackage;
     *
     *   private Class<?> typeAliasesSuperType;
     *
     *   private LanguageDriver[] scriptingLanguageDrivers;
     *
     *   private Class<? extends LanguageDriver> defaultScriptingLanguageDriver;
     *
     *   // issue #19. No default provider.
     *   private DatabaseIdProvider databaseIdProvider;
     *
     *   private Class<? extends VFS> vfs;
     *
     *   private Cache cache;
     *
     *   private ObjectFactory objectFactory;
     *
     *   private ObjectWrapperFactory objectWrapperFactory;
     */

    private Object resolvePlaceholder(Object value) {
        if (value instanceof String) {
            return environment.resolvePlaceholders((String) value);
        }
        return value;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
