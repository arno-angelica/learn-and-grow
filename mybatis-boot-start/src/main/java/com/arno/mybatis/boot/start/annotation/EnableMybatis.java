package com.arno.mybatis.boot.start.annotation;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Angelica.arno
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
@Import(MyBatisBeanDefinitionRegistrar.class)
public @interface EnableMybatis {

    /**
     * @return the bean name of {@link SqlSessionFactoryBean}
     */
    String value() default "sqlSessionFactoryBean";

    /**
     * @return DataSource bean name
     */
    String dataSource();

    /**
     * the location of {@link Configuration}
     *
     * @return
     */
    String configLocation() default "";

    String typeAliasesPackage();

    /**
     * @return the location of {@link Mapper}
     * @see MapperScan
     */
    String[] mapperLocations();

    String environment() default "SqlSessionFactoryBean";

    /**
     * typeHandlersPackage
     * @return
     */
    String typeHandlersPackage() default "";

    /**
     * custom Cache  Bean name
     * @return
     */
    String cache() default "";

    /**
     * custom transactionFactory bean name
     * @return
     */
    String transactionFactory() default "";

    /**
     * custom SqlSessionFactory bean name
     * @return
     */
    String sqlSessionFactory() default "";

    boolean failFast() default false;

    /**
     * Base packages to scan for MyBatis interfaces. Note that only interfaces with at least one method will be
     * registered; concrete classes will be ignored.
     *
     * @return base package names for scanning mapper interface
     */
    String[] basePackages();

    /**
     * Type-safe alternative to {@link #basePackages()} for specifying the packages to scan for annotated components. The
     * package of each class specified will be scanned.
     * <p>
     * Consider creating a special no-op marker class or interface in each package that serves no purpose other than being
     * referenced by this attribute.
     *
     * @return classes that indicate base package for scanning mapper interface
     */
    Class<?>[] basePackageClasses() default {};

    /**
     * The {@link BeanNameGenerator} class to be used for naming detected components within the Spring container.
     *
     * @return the class of {@link BeanNameGenerator}
     */
    Class<? extends BeanNameGenerator> nameGenerator() default BeanNameGenerator.class;

    /**
     * This property specifies the annotation that the scanner will search for.
     * <p>
     * The scanner will register all interfaces in the base package that also have the specified annotation.
     * <p>
     * Note this can be combined with markerInterface.
     *
     * @return the annotation that the scanner will search for
     */
    Class<? extends Annotation> annotationClass() default Annotation.class;

    /**
     * This property specifies the parent that the scanner will search for.
     * <p>
     * The scanner will register all interfaces in the base package that also have the specified interface class as a
     * parent.
     * <p>
     * Note this can be combined with annotationClass.
     *
     * @return the parent that the scanner will search for
     */
    Class<?> markerInterface() default Class.class;

    /**
     * Specifies which {@code SqlSessionTemplate} to use in the case that there is more than one in the spring context.
     * Usually this is only needed when you have more than one datasource.
     *
     * @return the bean name of {@code SqlSessionTemplate}
     */
    String sqlSessionTemplateRef() default "";

    /**
     * Specifies which {@code SqlSessionFactory} to use in the case that there is more than one in the spring context.
     * Usually this is only needed when you have more than one datasource.
     *
     * @return the bean name of {@code SqlSessionFactory}
     */
    String sqlSessionFactoryRef() default "";

    /**
     * Whether enable lazy initialization of mapper bean.
     *
     * <p>
     * Default is {@code false}.
     * </p>
     *
     * @return set {@code true} to enable lazy initialization
     * @since 2.0.2
     */
    String lazyInitialization() default "";

    /**
     * Specifies the default scope of scanned mappers.
     *
     * <p>
     * Default is {@code ""} (equiv to singleton).
     * </p>
     *
     * @return the default scope
     */
    String defaultScope() default AbstractBeanDefinition.SCOPE_DEFAULT;
}
