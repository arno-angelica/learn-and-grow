package com.arno.learn.grow.tiny.web.context;

import com.arno.learn.grow.tiny.web.function.ThrowableAction;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @desc:
 * @author: angelica
 * @date: 2021/3/7 下午4:06
 * @version:
 */
public class JavaNamingInitializeWebApplicationContext extends TinyAbstractInitializeWebApplicationContext {

    private static final String COMPONENT_ENV_CONTEXT_NAME = "java:comp/env";
    private Context envContext; // Component Env Context

    private final ClassLoader classLoader;
    public JavaNamingInitializeWebApplicationContext(ServletContext servletContext) {
        this.classLoader = servletContext.getClassLoader();
        initEnvContext();
        super.createWebApplicationContext(servletContext);
    }

    @Override
    protected void loadComponents() {
        // 遍历获取所有的组件名称
        List<String> componentNames = listAllComponentNames();
        // 通过依赖查找，实例化对象（ Tomcat BeanFactory setter 方法的执行，仅支持简单类型）
        componentNames.forEach(name -> beanClassesMap.put(name, lookupComponent(name)));
    }

    private void initEnvContext() throws RuntimeException {
        if (this.envContext != null) {
            return;
        }
        Context context = null;
        try {
            context = new InitialContext();
            this.envContext = (Context) context.lookup(COMPONENT_ENV_CONTEXT_NAME);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        } finally {
            close(context);
        }
    }

    private List<String> listAllComponentNames() {
        return listComponentNames("/");
    }

    protected List<String> listComponentNames(String name) {
        return executeNotIgnoredThrowable(this.envContext, context -> {
            NamingEnumeration<NameClassPair> e = executeNotIgnoredThrowable(context, ctx -> ctx.list(name));
            // 目录 - Context
            // 节点 -
            if (e == null) { // 当前 JNDI 名称下没有子节点
                return Collections.emptyList();
            }

            List<String> fullNames = new LinkedList<>();
            while (e.hasMoreElements()) {
                NameClassPair element = e.nextElement();
                String className = element.getClassName();
                Class<?> targetClass = classLoader.loadClass(className);
                if (Context.class.isAssignableFrom(targetClass)) {
                    // 如果当前名称是目录（Context 实现类）的话，递归查找
                    fullNames.addAll(listComponentNames(element.getName()));
                } else {
                    // 否则，当前名称绑定目标类型的话话，添加该名称到集合中
                    String fullName = name.startsWith("/") ?
                            element.getName() : name + "/" + element.getName();
                    fullNames.add(fullName);
                }
            }
            return fullNames;
        });
    }

    private Class<?> lookupComponent(String name) {
        return executeNotIgnoredThrowable(this.envContext, context -> context.lookup(name).getClass());
    }

    private static void close(Context context) {
        if (context != null) {
            ThrowableAction.execute(context::close);
        }
    }
}
