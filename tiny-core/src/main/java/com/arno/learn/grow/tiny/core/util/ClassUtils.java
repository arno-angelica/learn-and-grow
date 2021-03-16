package com.arno.learn.grow.tiny.core.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/2/28 上午11:48
 * @version:
 */
public class ClassUtils {

    private static final String END_SUFFIX = ".*";
    private static final String SUFFIX = ".";
    private static final String EXT = "class";
    private static final String[] EXTENSIONS = new String[]{EXT};

    /**
     * 从源类中查询出指定注解的的类对象
     * @param annotations 注解列表
     * @param sourceClasses 源类集合
     * @return class 对象列表
     */
    public static List<Class<?>> findAnnotationClasses(Collection<Class<? extends Annotation>> annotations, List<Class<?>> sourceClasses) {
        Set<Class<?>> annotationClasses = new HashSet<>();
        for (Class<? extends Annotation> annotation : annotations) {
            for (Class<?> clazz : sourceClasses) {
                if (clazz.isAnnotationPresent(annotation)) {
                    annotationClasses.add(clazz);
                }
            }
        }
        return new ArrayList<>(annotationClasses);
    }


    /**
     * 从源类中查询出目标类的实现类或子类
     * @param targetClasses 指定目标类
     * @param sourceClasses 源类集合
     * @return class 对象列表
     */
    public static List<Class<?>> findAssignableClasses(Collection<Class<?>> targetClasses, List<Class<?>> sourceClasses) {
        Set<Class<?>> assignableClazz = new HashSet<>();
        for (Class<?> clazz : targetClasses) {
            for (Class<?> temp : sourceClasses) {
                if (clazz.isAssignableFrom(temp)) {
                    assignableClazz.add(clazz);
                }
            }
        }
        return new ArrayList<>(assignableClazz);
    }



    /**
     * 从源类中查询出目标类的实现类或子类
     * @param targetClasses 指定目标类
     * @param sourceClass 源类
     * @return class 对象列表
     */
    public static <T> List<Class<? extends T>> findAssignableClasses(Collection<Class<?>> targetClasses, Class<T> sourceClass) {
        Set<Class<? extends T>> assignableClazz = new HashSet<>();
        for (Class<?> clazz : targetClasses) {
                if (clazz.isAssignableFrom(clazz)) {
                    assignableClazz.add((Class<? extends T>) clazz);
                }
        }
        return new ArrayList<>(assignableClazz);
    }
    /**
     * 获取多个包内的 class 文件
     *
     * @param packagePaths 包路径，如 "{\"com.arno.grown.web.mvc.controller\"}"
     * @return class 对象列表
     */
    public static List<Class<?>> findLoadClassInPackages(String[] packagePaths) {
        if (packagePaths == null || packagePaths.length == 0) {
            return null;
        }
        // 去重路径
        Set<String> pathSet = Stream.of(packagePaths)
                .filter(StringUtils::hasText).collect(Collectors.toSet());

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        List<Class<?>> classes = new ArrayList<>();
        pathSet.forEach(path -> classes.addAll(findLoadClassInPackage(cl, path)));
        return classes;
    }


    /**
     * 获取一个包路径下的 class 文件
     *
     * @param cl          类加载器
     * @param packagePath 文件路径，如 com.arno.grown.web.mvc.controller
     * @return class 对象列表
     */
    public static List<Class<?>> findLoadClassInPackage(ClassLoader cl, String packagePath) {
        if (cl == null) {
            cl = Thread.currentThread().getContextClassLoader();
        }
        // 是否查询子文件夹内文件
        boolean recursive = packagePath.endsWith(END_SUFFIX);
        if (recursive) {
            packagePath = packagePath.substring(0, packagePath.lastIndexOf(SUFFIX));
        }
        // 获取资源路径
        String resourcePath = packagePath.replace('.', File.separatorChar);
        URL url = cl.getResource(resourcePath);
        if (url == null) {
            throw new RuntimeException("The specified resource was not found ：" + resourcePath);
        }
        String filePath = url.getFile();
        // 查找 class 文件
        File file = new File(filePath);
        if (!file.exists() && !file.isDirectory()) {
            throw new RuntimeException("the directory is empty ：" + filePath);
        }
        Collection<File> files = FileUtils.listFiles(file, EXTENSIONS, recursive);
        // 加载 class 对象
        return loadClass(cl, resourcePath, files);
    }

    /**
     * 加载类
     * @param cl 类加载器
     * @param resourcePath 资源全路径 com/arno/grow/web/mvc/controller
     * @param files class 文件列表 如 /Users/arno/workspace/source/learn-and-grow/web-mvc/target/classes/com/arno/grow/web/mvc/controller/MvcController.class
     * @return class 对象列表
     */
    private static List<Class<?>> loadClass(ClassLoader cl, String resourcePath, Collection<File> files) {
        List<Class<?>> classes = new ArrayList<>();
        try {
            for (File file : files) {
                // file.getAbsolutePath() ：/Users/xxx/workspace/source/learn-and-grow/web-mvc/target/classes/com/arno/grow/web/mvc/controller/MvcController.class
                // 去除 .class 得到 /Users/xxx/workspace/source/learn-and-grow/web-mvc/target/classes/com/arno/grow/web/mvc/controller/MvcController
                String absolutePath = file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - EXT.length() - 1);
                String className = absolutePath.substring(absolutePath.indexOf(resourcePath)).replace(File.separatorChar, '.');
                // 加载 class 对象
                Class<?> clazz = cl.loadClass(className);
                classes.add(clazz);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
        return classes;
    }

    public static void main(String[] args) {
        String[] path = {"com.arno.grow.web.mvc.controller"};
        findLoadClassInPackages(path);
    }

}
