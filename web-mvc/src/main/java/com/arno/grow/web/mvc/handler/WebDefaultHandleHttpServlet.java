package com.arno.grow.web.mvc.handler;

import com.alibaba.fastjson.JSONObject;
import com.arno.grow.web.mvc.annotation.WebController;
import com.arno.grow.web.mvc.annotation.WebRequestMapping;
import com.arno.grow.web.mvc.controller.Controller;
import com.arno.grow.web.mvc.utils.MvcClassUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import static org.apache.commons.lang.StringUtils.substringAfter;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/2/28 下午1:48
 * @version:
 */
public class WebDefaultHandleHttpServlet extends HttpServlet {
    private static final long serialVersionUID = 5653494896979640797L;

    private static final String SCAN_PACKAGE_KEY = "scan.package";
    private static final String CONFIG_PATH = "application.properties";
    private static final String SPLIT = ",";
    private static final String PATH_START = "/";
    private static final String ENCODE_TYPE = "utf-8";
    private static final String CONTEXT_TYPE = "application/json; charset=utf-8";

    private static String[] SCAN_PATHS;
    private static final Map<String, SupportMethodInfo> PATH_METHOD_PATH_MAP = new HashMap<>();

    protected static final Logger log = Logger
            .getLogger(WebDefaultHandleHttpServlet.class.getName());

    private DefaultBeanDefinitionInitHandler handler;


    @Override
    public void init() throws ServletException {
        handler = new DefaultBeanDefinitionInitHandler();
        initPropertiesConfig();
        List<Class<?>> allClasses = MvcClassUtils.findLoadClassInPackages(SCAN_PATHS);
        if (allClasses != null && allClasses.size() > 0) {
            initHandleClassMethod(allClasses);
            initSubclass(allClasses);
        }
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String servletContextPath = request.getContextPath();
        String requestMappingPath = substringAfter(requestURI,
                StringUtils.replace(servletContextPath, "//", "/"));
        SupportMethodInfo supportMethodInfo = PATH_METHOD_PATH_MAP.get(requestMappingPath);
        if (supportMethodInfo == null) {
            // 404
//            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        PrintWriter writer = null;
        try {
            if (!supportMethodInfo.getSupportMethods().contains(request.getMethod())) {
                // HTTP 方法不支持
                response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                return;
            }
            Object respBody;
            Method method = supportMethodInfo.getMethod();
            Parameter[] parameters = method.getParameters();
            if (parameters != null && parameters.length > 0) {
                List<Object> params = new LinkedList<>();
                for (Parameter param : parameters) {
                    Object paramObj = parseRequestParam(request, param);
                    params.add(paramObj);
                }
                // 反射调用
                respBody = method.invoke(supportMethodInfo.getInstance(), params.toArray());
            } else {
                respBody = method.invoke(supportMethodInfo.getInstance());
            }
            WebRequestMapping mapping = method.getAnnotation(WebRequestMapping.class);
            if (mapping != null && mapping.page()) {
                String page = respBody != null ?
                        (!respBody.toString().startsWith(PATH_START) ?
                                PATH_START + respBody.toString()
                                : respBody.toString()) : "";
                ServletContext servletContext = request.getServletContext();
                RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(page);
                requestDispatcher.forward(request, response);
                return;
            }
            // 输出响应结果
            String respStr = JSONObject.toJSONString(respBody);
            response.setCharacterEncoding(ENCODE_TYPE);
            response.setContentType(CONTEXT_TYPE);
            writer = response.getWriter();
            writer.append(respStr);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * 处理请求参数
     *
     * @param request
     * @param parameter
     * @return
     * @throws IOException
     */
    private Object parseRequestParam(HttpServletRequest request, Parameter parameter) throws IOException {
        JSONObject json = new JSONObject();
        String contentType = request.getContentType();
        // 如果为 form 表单提交则使用 getParameter()
        if (MediaType.APPLICATION_FORM_URLENCODED.equals(contentType)) {
            Enumeration<String> enumeration = request.getParameterNames();
            while (enumeration.hasMoreElements()) {
                String parameterName = enumeration.nextElement();
                json.put(parameterName, request.getParameter(parameterName));
            }
        } else if (MediaType.APPLICATION_JSON.equals(contentType)) {
            // json 格式使用 body reader
            StringBuilder body = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                body.append(line);
            }
            json.putAll(JSONObject.parseObject(body.toString()));
        }
        // TODO 其他方式待添加
        return JSONObject.parseObject(json.toJSONString(), parameter.getType());
    }


    /**
     * 加载配置文件，并读取需要扫描的包路径
     */
    private void initPropertiesConfig() {
        // 初始化配置，读取项目根路径下 application.properties 文件
        URL url = Thread.currentThread().getContextClassLoader().getResource(CONFIG_PATH);
        if (url == null) {
            throw new RuntimeException("application.properties does not exist");
        }
        String scanPath;
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(url.getFile()));
            Properties properties = new Properties();
            properties.load(in);
            // 获取配置文件中的 scan.package 配置
            scanPath = properties.getProperty(SCAN_PACKAGE_KEY);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        if (StringUtils.isBlank(scanPath)) {
            throw new RuntimeException(SCAN_PACKAGE_KEY + "  does not exist");
        }
        SCAN_PATHS = scanPath.split(SPLIT);
    }

    /**
     * 初始化请求处理类
     */
    private void  initHandleClassMethod(List<Class<?>> allClasses) {
        // 获取 controller 类
        List<Class<?>> scanClasses = scanClasses(allClasses);
        if (scanClasses.size() == 0) {
            return;
        }
        try {
            for (Class<?> clazz : scanClasses) {
                // 获取类上标注的请求路径
                String classRequestPath = getClassRequestPath(clazz);
                Method[] publicMethods = clazz.getDeclaredMethods();
                // 初始化实例对象，反射时使用
                if (StaticDefinition.CONTEXT_MAP.containsKey(clazz.getSimpleName().toUpperCase())) {
                    throw new RuntimeException("named : " + clazz.getSimpleName() + "already exists");
                }
                Object instance = clazz.newInstance();
                BeanDefinition definition = new BeanDefinition(instance, clazz.getSimpleName(), clazz);
                StaticDefinition.CONTEXT_MAP.put(clazz.getSimpleName().toUpperCase(), definition);

                for (Method method : publicMethods) {
                    // 获取方法上的请求路径
                    String methodRequestPath = getMethodRequestPath(method);
                    // 拼接请求全路径
                    String requestPath = classRequestPath + methodRequestPath;
                    // 初始化 SupportMethodInfo
                    SupportMethodInfo supportMethodInfo = new SupportMethodInfo(requestPath, method, getMethodSupportRequestType(method), instance);
                    if (PATH_METHOD_PATH_MAP.containsKey(requestPath)) {
                        throw new RuntimeException("The path : " + requestPath + " already exists");
                    }
                    // 存入缓存
                    PATH_METHOD_PATH_MAP.put(requestPath, supportMethodInfo);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 初始化子类
     * @param classes
     */
    protected void initSubclass(List<Class<?>> classes) {
        handler.initSubclass(classes);
    }

    /**
     * 获取指定的类
     *
     * @return
     */
    private List<Class<?>> scanClasses(List<Class<?>> allClasses) {
        List<Class<?>> scanClasses = new ArrayList<>();
        // 获取包下所有的类
        // 获取标注 @WebController 注解的类
        List<Class<?>> annotationClasses = MvcClassUtils.findAnnotationClasses(Collections.singletonList(WebController.class), allClasses);
        if (annotationClasses.size() > 0) {
            scanClasses.addAll(annotationClasses);
        }
        // 获取实现 Controller 接口的类
        List<Class<?>> assignableClasses = MvcClassUtils.findAssignableClasses(Collections.singletonList(Controller.class), allClasses);
        if (assignableClasses.size() > 0) {
            scanClasses.addAll(assignableClasses);
        }
        return scanClasses;
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
}
