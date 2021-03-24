package com.arno.learn.grow.tiny.web.context;

import com.arno.learn.grow.tiny.configuration.DefaultConfigProviderResolver;
import com.arno.learn.grow.tiny.web.annotation.Autowired;
import com.arno.learn.grow.tiny.web.annotation.Value;
import com.arno.learn.grow.tiny.web.annotation.WebController;
import com.arno.learn.grow.tiny.web.annotation.WebRequestMapping;
import com.arno.learn.grow.tiny.web.exception.NoUniqueBeanInfoException;
import com.arno.learn.grow.tiny.web.function.ThrowableFunction;
import com.arno.learn.grow.tiny.web.supoort.ContextBeanInfo;
import com.arno.learn.grow.tiny.web.supoort.ContextBeanInfoBuilder;
import com.arno.learn.grow.tiny.web.supoort.Controller;
import com.arno.learn.grow.tiny.web.supoort.SupportMethodInfo;
import org.apache.commons.lang.StringUtils;
import org.eclipse.microprofile.config.Config;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * @desc: 初始化抽象类
 * @author: Arno.KV
 * @date: 2021/3/7 上午12:15
 * @version:
 */
public abstract class TinyAbstractInitializeWebApplicationContext implements TinyWebApplicationContext {

    protected static final Logger logger = Logger.getLogger(TinyAbstractInitializeWebApplicationContext.class.getName());

    private static final String PATTERN = "@";
    protected Config config;

    /**
     * 存储请求路径和方法
     */
    private static final Map<String, SupportMethodInfo> PATH_METHOD_PATH_MAP = new HashMap<>();

    /**
     * 存储实例化后的 bean
     */
    protected Map<String, ContextBeanInfo> contextBeanInfoMap = new ConcurrentHashMap<>(128);

    /**
     * 存储 bean name 和 bean class
     */
    protected Map<String, Class<?>> beanClassesMap = new HashMap<>();

    /**
     * 通过请求路径获取对应实体类和方法
     *
     * @param requestPath
     * @return
     */
    public SupportMethodInfo getSupportMethod(String requestPath) {
        return PATH_METHOD_PATH_MAP.get(requestPath);
    }

    /**
     * 初始化上下文
     */
    protected void createWebApplicationContext(ServletContext servletContext) {
        // 读取配置源，存储到 map 中
        DefaultConfigProviderResolver configProviderResolver = (DefaultConfigProviderResolver) servletContext.getAttribute(CONFIG_ATTRIBUTE);
        this.config = configProviderResolver.getConfig(servletContext.getClassLoader());
        contextBeanInfoMap.put(CONFIG_ATTRIBUTE, ContextBeanInfoBuilder.builder().instance(config).build());

        // 加载组件列表
        loadComponents();
        if (beanClassesMap == null || beanClassesMap.isEmpty()) {
            return;
        }
        // 创建实例对象
        instanceComponents();
        // 对象属性注入
        injectComponent();
        // 调用对象 init 方法
        invokeInitMethod();
        // 初始化 controller 层
        initController();
    }

    /**
     * 销毁方法
     */
    public void destroyWebApplication() {
        if (contextBeanInfoMap == null || contextBeanInfoMap.isEmpty()) {
            return;
        }
        contextBeanInfoMap.values().stream()
                .filter(info -> info.getDestroyMethods() != null && info.getDestroyMethods().size() > 0)
                .forEach(info -> {
                    info.getDestroyMethods().forEach(method ->
                            executeIgnoredThrowable(method, m -> method.invoke(info.getInstance())));
                });
        contextBeanInfoMap.clear();
    }

    /**
     * 加载组件
     */
    protected abstract void loadComponents();


    /**
     * 创建实例对象
     */
    private void instanceComponents() {
        beanClassesMap.forEach((name, beanClass) -> {
            if (contextBeanInfoMap.containsKey(name)) {
                throw new NoUniqueBeanInfoException("named " + name + " already exists");
            }
            // 创建实例
            Object instance = executeNotIgnoredThrowable(beanClass, Class::newInstance);
            List<Method> initMethods = new LinkedList<>();
            List<Method> destroyMethods = new LinkedList<>();
            // 记录初始化和销毁方法
            for (Method method : beanClass.getDeclaredMethods()) {
                if (!method.isAnnotationPresent(PostConstruct.class)
                        && !method.isAnnotationPresent(PreDestroy.class)) {
                    continue;
                }
                if (method.isAnnotationPresent(PostConstruct.class)) {
                    initMethods.add(method);
                }
                if (method.isAnnotationPresent(PreDestroy.class)) {
                    destroyMethods.add(method);
                }
            }
            contextBeanInfoMap.put(name, ContextBeanInfoBuilder.builder()
                    .instance(instance).initMethods(initMethods)
                    .destroyMethods(destroyMethods).build());
        });
    }

    /**
     * 注入实例对象
     */
    private void injectComponent() {
        contextBeanInfoMap.values().forEach(beanInfo -> Stream.of(beanInfo.getInstance()
                .getClass().getDeclaredFields())
                .filter(field -> {
                    // 取出非静态且需要注入的的字段
                    int mods = field.getModifiers();
                    return !Modifier.isStatic(mods)
                            && (field.isAnnotationPresent(Autowired.class)
                            || field.isAnnotationPresent(Resource.class)
                            || field.isAnnotationPresent(Value.class));
                }).forEach(field -> {
            Object obj;
            if (field.isAnnotationPresent(Autowired.class)
                    || field.isAnnotationPresent(Resource.class)) {
                obj = getAutowiredFiled(field);
            } else {
                Value value = field.getAnnotation(Value.class);
                String val = value.value();
                obj = val;
                if (StringUtils.isNotBlank(val) && val.startsWith(PATTERN)) {
                    obj = config.getValue(val.substring(1), String.class);
                }
            }
            field.setAccessible(true);
            try {
                field.set(beanInfo.getInstance(), obj);
            } catch (Exception e) {
                throw new RuntimeException("autowired " + field.getName() + " bean info error : " + e.getMessage());
            }
        }));
    }

    /**
     * 获取注入属性
     *
     * @param field
     * @return
     */
    private Object getAutowiredFiled(Field field) {
        // 遍历需要注入的字段，做属性注入
        String autowiredName;
        Resource resource = field.getAnnotation(Resource.class);
        if (resource != null) {
            autowiredName = resource.name();
        } else {
            autowiredName = field.getAnnotation(Autowired.class).value();
        }
        autowiredName = StringUtils.isNotBlank(autowiredName) ? autowiredName : getBeanName(field.getType());
        // 从 map 中获取实例对象
        ContextBeanInfo info = contextBeanInfoMap.get(autowiredName);
        if (info == null) {
            for (ContextBeanInfo beanInfo : contextBeanInfoMap.values()) {
                if (field.getType().isAssignableFrom(beanInfo.getInstance().getClass())) {
                    info = beanInfo;
                    break;
                }
            }
            if (info == null) {
                throw new NoSuchElementException("can not find named " + autowiredName + " bean info");
            }
        }
        return info.getInstance();
    }

    /**
     * 调用 init 方法
     */
    private void invokeInitMethod() {
        contextBeanInfoMap.values().stream().filter(beanInfo ->
                // 取出初始化方法不为空的
                beanInfo.getInitMethods() != null && beanInfo.getInitMethods().size() > 0)
                .forEach(beanInfo -> beanInfo.getInitMethods()
                        // 依次调用初始化
                        .forEach(method -> executeNotIgnoredThrowable(method,
                                m -> m.invoke(beanInfo.getInstance()))));
    }

    /**
     * 初始化 controller 层
     */
    private void initController() {
        contextBeanInfoMap.values().stream().filter(beanInfo ->
                beanInfo.getInstance().getClass()
                        .isAnnotationPresent(WebController.class)
                        || beanInfo.getInstance().getClass().isAssignableFrom(Controller.class))
                .forEach(beanInfo -> {
                    Class<?> clazz = beanInfo.getInstance().getClass();
                    // 获取类上的请求路径
                    String classPath = getClassRequestPath(clazz);
                    Stream.of(clazz.getDeclaredMethods())
                            .filter(method -> method.isAnnotationPresent(Path.class)
                                    || method.isAnnotationPresent(WebRequestMapping.class))
                            .forEach(method -> {
                                // 获取方法上的请求路径
                                String methodRequestPath = getMethodRequestPath(method); // 拼接请求全路径
                                String requestPath = classPath + methodRequestPath;
                                // 初始化 SupportMethodInfo
                                SupportMethodInfo supportMethodInfo = SupportMethodInfo.SupportMethodInfoBuilder
                                        .builder().method(method)
                                        .supportMethods(getMethodSupportRequestType(method))
                                        .requestPath(requestPath)
                                        .instance(beanInfo.getInstance())
                                        .build();
                                if (PATH_METHOD_PATH_MAP.containsKey(requestPath)) {
                                    throw new RuntimeException("The path : " + requestPath + " already exists");
                                }
                                // 存入缓存
                                PATH_METHOD_PATH_MAP.put(requestPath, supportMethodInfo);
                            });
                });
    }

    /**
     * 获取 bean 名称
     *
     * @param clazz
     * @return
     */
    protected String getBeanName(Class<?> clazz) {
        return clazz.getName();
    }

    /**
     * 不忽略执行异常
     *
     * @param t
     * @param function
     * @param <T>
     * @param <R>
     * @return
     */
    protected <T, R> R executeNotIgnoredThrowable(T t, ThrowableFunction<T, R> function) {
        return executeThrowable(t, function, false);
    }

    /**
     * 忽略执行异常
     *
     * @param t
     * @param function
     * @param <T>
     * @param <R>
     * @return
     */
    protected <T, R> R executeIgnoredThrowable(T t, ThrowableFunction<T, R> function) {
        return executeThrowable(t, function, true);
    }

    /**
     * 在 Context 中执行，通过指定 ThrowableFunction 返回计算结果
     *
     * @param function         ThrowableFunction
     * @param ignoredException 是否忽略异常
     * @param <R>              返回结果类型
     * @return 返回
     * @see ThrowableFunction#apply(Object)
     */
    private <T, R> R executeThrowable(T t, ThrowableFunction<T, R> function,
                                      boolean ignoredException) {
        R result = null;
        try {
            result = ThrowableFunction.execute(t, function);
        } catch (Throwable e) {
            if (ignoredException) {
                logger.warning(e.getMessage());
            } else {
                throw new RuntimeException(e);
            }
        }
        return result;
    }


    /**
     * 获取类上标注的请求路径
     *
     * @param clazz 类
     * @return 请求路径
     */
    private String getClassRequestPath(Class<?> clazz) {
        String classRequestPath = "";
        Path pathFromClass = clazz.getAnnotation(Path.class);
        if (pathFromClass != null) {
            classRequestPath = pathFromClass.value();
        } else {
            WebRequestMapping mapping = clazz.getAnnotation(WebRequestMapping.class);
            if (mapping != null) {
                classRequestPath = mapping.value();
            }
        }
        if (StringUtils.isNotBlank(classRequestPath) && !classRequestPath.startsWith(PATH_START)) {
            classRequestPath = PATH_START + classRequestPath;
        }
        return classRequestPath;
    }

    /**
     * 获取方法上标注的请求路径
     *
     * @param method
     * @return
     */
    private String getMethodRequestPath(Method method) {
        String methodRequetPath = "";
        Path pathFromMethod = method.getAnnotation(Path.class);
        if (pathFromMethod != null) {
            methodRequetPath = pathFromMethod.value();
        } else {
            WebRequestMapping mapping = method.getAnnotation(WebRequestMapping.class);
            if (mapping != null) {
                methodRequetPath = mapping.value();
            }
        }
        if (StringUtils.isNotBlank(methodRequetPath) && !methodRequetPath.startsWith(PATH_START)) {
            methodRequetPath = PATH_START + methodRequetPath;
        }
        return methodRequetPath;
    }

    /**
     * 获取方法支持的请求方式
     *
     * @param method 方法
     * @return 请求方式
     */
    private Set<String> getMethodSupportRequestType(Method method) {
        Set<String> supportedHttpMethods = new LinkedHashSet<>();
        for (Annotation annotationFromMethod : method.getAnnotations()) {
            HttpMethod httpMethod = annotationFromMethod.annotationType().getAnnotation(HttpMethod.class);
            if (httpMethod != null) {
                supportedHttpMethods.add(httpMethod.value());
            }
        }
        return supportedHttpMethods;
    }
}
