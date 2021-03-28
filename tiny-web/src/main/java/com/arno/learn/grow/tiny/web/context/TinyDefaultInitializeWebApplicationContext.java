package com.arno.learn.grow.tiny.web.context;

import com.arno.learn.grow.tiny.core.util.ClassUtils;
import com.arno.learn.grow.tiny.web.annotation.Component;
import com.arno.learn.grow.tiny.web.annotation.Configuration;
import com.arno.learn.grow.tiny.web.annotation.DbRepository;
import com.arno.learn.grow.tiny.web.annotation.Service;
import com.arno.learn.grow.tiny.web.annotation.WebController;
import com.arno.learn.grow.tiny.web.exception.ServiceConfigException;
import com.arno.learn.grow.tiny.web.supoort.Controller;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletContext;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @desc:
 * @author: angelica
 * @date: 2021/3/7 下午2:51
 * @version:
 */
public class TinyDefaultInitializeWebApplicationContext extends TinyAbstractInitializeWebApplicationContext {

    private static final String SCAN_PACKAGE_KEY = "scan.package";
    private static final String SPLIT = ",";

    private static final Set<Class<? extends Annotation>> scanAnnotationSet = new HashSet<>(5);

    static {
        scanAnnotationSet.add(DbRepository.class);
        scanAnnotationSet.add(Service.class);
        scanAnnotationSet.add(Component.class);
        scanAnnotationSet.add(Configuration.class);
        scanAnnotationSet.add(WebController.class);
    }

    public TinyDefaultInitializeWebApplicationContext(ServletContext servletContext) {
        super.createWebApplicationContext(servletContext);
    }

    /**
     * 加载组件
     */
    @Override
    protected void loadComponents() {
        String scanPath = this.config.getValue(SCAN_PACKAGE_KEY, String.class);
        if (StringUtils.isBlank(scanPath)) {
            throw new ServiceConfigException("can not find scan.package in properties");
        }
        String[] scanPaths = scanPath.split(SPLIT);
        List<Class<?>> allClasses = ClassUtils.findLoadClassInPackages(scanPaths);
        if (allClasses != null && allClasses.size() > 0) {
            List<Class<?>> scanClasses = new ArrayList<>();
            // 获取标注注解的类
            List<Class<?>> annotationClasses = ClassUtils.findAnnotationClasses(scanAnnotationSet, allClasses);
            if (annotationClasses.size() > 0) {
                scanClasses.addAll(annotationClasses);
            }
            // 获取实现 Controller 接口的类
            List<Class<?>> assignableClasses = ClassUtils.findAssignableClasses(Collections.singletonList(Controller.class), allClasses);
            if (assignableClasses.size() > 0) {
                scanClasses.addAll(assignableClasses);
            }
            if (scanClasses.size() > 0) {
                beanClassesMap.putAll(scanClasses.stream().collect(Collectors.toMap(this::getBeanName, a -> a, (k1, k2) -> k1)));
            }
        }
    }

    /**
     * 获取 bean 名称
     * @param clazz
     * @return
     */
    @Override
    protected String getBeanName(Class<?> clazz) {
        DbRepository dbRepository = clazz.getAnnotation(DbRepository.class);
        if (dbRepository != null && StringUtils.isNotBlank(dbRepository.value())) {
            return dbRepository.value();
        }

        Service service = clazz.getAnnotation(Service.class);
        if (service != null && StringUtils.isNotBlank(service.value())) {
            return service.value();
        }

        Component component = clazz.getAnnotation(Component.class);
        if (component != null && StringUtils.isNotBlank(component.value())) {
            return component.value();
        }

        Configuration configuration = clazz.getAnnotation(Configuration.class);
        if (configuration != null && StringUtils.isNotBlank(configuration.value())) {
            return configuration.value();
        }
        return super.getBeanName(clazz);
    }
}
