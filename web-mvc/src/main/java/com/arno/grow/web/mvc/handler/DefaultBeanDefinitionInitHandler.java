package com.arno.grow.web.mvc.handler;

import com.arno.grow.web.mvc.annotation.Autowired;
import com.arno.grow.web.mvc.annotation.DbRepository;
import com.arno.grow.web.mvc.annotation.Service;
import com.arno.grow.web.mvc.utils.MvcClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/3/1 下午10:04
 * @version:
 */
public class DefaultBeanDefinitionInitHandler {
    protected void initSubclass(List<Class<?>> classes) {
        List<Class<? extends Annotation>> annotations = new ArrayList<>();
        annotations.add(DbRepository.class);
        annotations.add(Service.class);
        List<Class<?>> annotationClasses = MvcClassUtils.findAnnotationClasses(annotations, classes);
        if (annotationClasses.size() > 0) {
            try {
                for (Class<?> clazz : annotationClasses) {
                    initBeanInstance(clazz);
                }
            } catch (Exception e) {
                throw new RuntimeException("init context definition error, cause " + e.getMessage());
            }
        }
        autowiredField();
    }

    private void autowiredField() {
        for (Map.Entry<String, BeanDefinition> entry : StaticDefinition.CONTEXT_MAP.entrySet()) {
            BeanDefinition currentDefinition = entry.getValue();
            Field[] fields = currentDefinition.getClazz().getDeclaredFields();
            if (fields.length > 0) {
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (null != field.getAnnotation(Autowired.class)) {
                        String name = field.getGenericType().getTypeName();
                        name = name.substring(name.lastIndexOf(".") + 1);
                        if (name.equalsIgnoreCase(entry.getKey())) {
                            throw new RuntimeException("BeanDefinition named " + currentDefinition.getName() + "can not autowiring own bean");
                        }
                        BeanDefinition definition = StaticDefinition.CONTEXT_MAP.get(name.toUpperCase());
                        if (definition == null) {
                            throw new RuntimeException("BeanDefinition named " + name + " could not be found");
                        }
                        try {
                            field.set(currentDefinition.getInstance(), definition.getInstance());
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("autowiring bean" + currentDefinition.getName() + "field " + name + " error");
                        }
                    }
                }
            }
        }
    }

    /**
     * 实例化 beanDefinition
     *
     * @param clazz
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private void initBeanInstance(Class<?> clazz) throws IllegalAccessException, InstantiationException {
        String name = clazz.getSimpleName();
        if (StaticDefinition.CONTEXT_MAP.containsKey(name.toUpperCase())) {
            throw new RuntimeException("named : " + name + "already exists");
        }
        Object obj = clazz.newInstance();
        BeanDefinition definition = new BeanDefinition(obj, name, clazz);
        StaticDefinition.CONTEXT_MAP.put(name.toUpperCase(), definition);
    }

}
