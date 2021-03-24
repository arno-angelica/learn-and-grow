# 2021.03.07 重构说明

1. 废弃 web-mvc 方法，新增并引入 tiny-web 工程
2. 剥离原 web-mvc Servlet#init() 加载方法，提取到 Listener init 中处理
3. 提供统一 API(com.arno.learn.grow.tiny.web.context.TinyWebApplicationContext),
   提取公共抽象类实现(com.arno.learn.grow.tiny.web.context.TinyAbstractInitializeWebApplicationContext)
4. 新增支持扫描注解 @Component、@Configuration 和 注入配置注解 @Value
5. Listener 继承 com.arno.learn.grow.tiny.web.context.TinyContextLoader, 
   其 createTinyWebApplicationContext() 可用于子类重写实现自己的 TinyWebApplicationContext  
6. 新增异常 NoUniqueBeanInfoException 和 ServiceConfigException
7. Servlet 中调整页面的判定改为判断返回 是否为 jsp/html 结尾
8. 新增 ContextBeanInfo

### 后续优化项

1. 结合 Apache Commons Configuration 实现多源配置读取，工具类需修改
2. Properties#getProperty() 调用 Hashtable#get() 方法，而后者为同步方法，
   需在初始化配置文件时将Properties 转为 HashMap<Object, Object> 存储
3. 需加入配置文件变动监听，使用到变动配置的属性也需重新注入新值