package com.arno.grow.web.mvc.handler;

import com.arno.grow.web.mvc.annotation.DbRepository;
import com.arno.grow.web.mvc.utils.MvcClassUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/3/1 下午10:04
 * @version:
 */
public class DatabaseInitHandler extends WebDefaultHandleHttpServlet {
    private static final long serialVersionUID = -7629434957670874334L;

    @Override
    protected void initSubclass(List<Class<?>> classes) {
        List<Class<?>> dbClass = MvcClassUtils.findAnnotationClasses(Collections.singletonList(DbRepository.class), classes);
        if (dbClass.size() > 0) {
            try {
                for (Class<?> clazz : classes) {
                    DbRepository dbRepository = clazz.getAnnotation(DbRepository.class);
                    String name = StringUtils.isNotBlank(dbRepository.value()) ? dbRepository.value() : clazz.getSimpleName();
                    if (StaticDefinition.CONTEXT_MAP.containsKey(name)) {
                        throw new RuntimeException("named : " + name + "already exists");
                    }
                    name = name.toUpperCase();
                    Object obj = clazz.newInstance();
                    ContextDefinition definition = new ContextDefinition(obj, name);
                    StaticDefinition.CONTEXT_MAP.put(name, definition);
                }
            } catch (Exception e) {
                log.log(Level.SEVERE, "init context definition error, cause " + e.getMessage());
                throw new RuntimeException("init context definition error, cause " + e.getMessage());
            }

        }
    }
}
