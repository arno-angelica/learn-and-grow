# 2021.03.24 重构说明

## 改动点

1. 调整 [**tiny-configuration**](https://github.com/arno-angelica/learn-and-grow/tree/fourth_refactor/tiny-configuration) 模块代码
2. 加入 [**servletContext param**](https://github.com/arno-angelica/learn-and-grow/blob/fourth_refactor/user-web/src/main/java/com/arno/grow/user/web/configuration/ServletContextParamConfig.java) 的配置读取方式
3. 新增 [**ServletContainerInitializer 实现**](https://github.com/arno-angelica/learn-and-grow/blob/fourth_refactor/tiny-web/src/main/java/com/arno/learn/grow/tiny/web/servlet/ServletInitializer.java)
4. [**DefaultConfigProviderResolver#cloneConfigProviderResolver**](https://github.com/arno-angelica/learn-and-grow/blob/918de208f2fb8cbdf14f101564b62efda88b2f63/tiny-configuration/src/main/java/com/arno/learn/grow/tiny/configuration/DefaultConfigProviderResolver.java#L35) 后续拓展原型模式的ConfigProviderResolver
5. 调整注入方式，可通过 bean name 和 bean 类型两种方式实现注入

## 问题

- ServletContextParamConfig 创建时如果直接实现父类（MapBasedConfigSource）之前的构造器会导致 servletContext 为空

  ```java
  // 之前默认的构造器
  protected MapBasedConfigSource(String name, int ordinal) {
    this.name = name;
    this.ordinal = ordinal;
    this.source = getProperties();
  }
  // 新增如下
  protected ServletContext servletContext;
  // 默认无参构造器
  protected MapBasedConfigSource() {}
  // 带servletContext 的构造器
  protected MapBasedConfigSource(String name, int ordinal, ServletContext servletContext) {
    this.name = name;
    this.ordinal = ordinal;
    this.servletContext = servletContext;
    this.source = getProperties();
  }
  ```

  - MapBasedConfigSource 中新增构造器和 servletContext 属性，子类创建时将 servletContext 交给父类托管，后续子类直接使用。
  - 需注意子类创建有参构造器后，需再创建一个无参构造器，用于 class.instance 

- DefaultResourceConfigSource 的 logger 属性为空，原实现如下

  ```java
  private final Logger logger = Logger.getLogger(this.getClass().getName());
  ```
  - 方案一：将 Logger 属性的定义放置到父类
  - 方案二：把 Logger 属性改为 static，本项目使用此方案

  ```java
  private static final Logger logger = Logger.getLogger(DefaultResourceConfigSource.class.getName());
  ```

- 使用 servletContext.addListener() 方法后，会出现如下异常

  ```java
  UnsupportedOperationException: Servlet 3.0规范的第4.4节不允许从未在web.xml，web-fragment.xml文件中定义或未用@WebListener注释的ServletContextListener调用此方法。
  ```
  - 本项目解决方式，自定接口 `ServletStartupInitializer`, 其实现类 `TinyApplicationContext` 中初始化各个组件。

## 后续优化点

自研的这套 MVC 框架中用到了大量的反射，后续需结合 Java 的安全机制处理，如下

```java
if (System.getSecurityManager() != null) {
  AccessController.doPrivileged((PrivilegedAction<Object>) () -> {});
  try {
    return AccessController.doPrivileged((PrivilegedExceptionAction<Object>) () ->{});
  } catch (PrivilegedActionException pae) {
    throw pae.getException();
  }
}
```

