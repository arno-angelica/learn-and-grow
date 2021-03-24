# 2021.03.24 重构说明

1. 调整 [**tiny-configuration**](https://github.com/arno-angelica/learn-and-grow/tree/fourth_refactor/tiny-configuration) 模块代码
2. 加入 [**servletContext param**](https://github.com/arno-angelica/learn-and-grow/blob/fourth_refactor/user-web/src/main/java/com/arno/grow/user/web/configuration/ServletContextParamConfig.java) 的配置读取方式
3. 新增 [**ServletContainerInitializer 实现**](https://github.com/arno-angelica/learn-and-grow/blob/fourth_refactor/tiny-web/src/main/java/com/arno/learn/grow/tiny/web/servlet/ServletInitializer.java)
4. [**DefaultConfigProviderResolver#cloneConfigProviderResolver**](https://github.com/arno-angelica/learn-and-grow/blob/918de208f2fb8cbdf14f101564b62efda88b2f63/tiny-configuration/src/main/java/com/arno/learn/grow/tiny/configuration/DefaultConfigProviderResolver.java#L35) 后续拓展原型模式的ConfigProviderResolver
5. 调整注入方式，可通过 bean name 和 bean 类型两种方式实现注入