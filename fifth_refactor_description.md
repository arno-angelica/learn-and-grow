# 2021.03.28 第五次重构说明

## 本次主要目标

1. **修复本程序 org.geektimes.reactive.streams 包下**
2. **继续完善 my-rest-client POST 方法**
3. **（可选）读一下 Servlet 3.0 关于 Servlet 异步**
   1. **AsyncContext**

## 改动点

1. 调整主 pom.xml 的依赖顺序
2. 去除 tiny-configuration 中的 servlet-api 依赖
3. 在 tiny-web 中新增 `ServletMapBasedConfigSource` 类，继承 `MapBasedConfigSource`
4. `ServletContextParamConfig` 调整，由原来继承 `MapBasedConfigSource` 改为继承 `ServletMapBasedConfigSource` 

### 完成以下作业

- **继续完善 my-rest-client POST 方法** 
  - 提取抽象类

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

