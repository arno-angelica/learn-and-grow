# 2021.03.15 重构说明

主界面 http://localhost:8080/user/home，配置文件 config.properties

多配置源的配置类 com.arno.learn.grow.tiny.configuration.DefaultConfig

1. 加入 Jolokia 和 JMX

2. 新增 monitor 和 configuration

   2.1 configuration 支持 OS 环境变量、classPath/properties 读取，详见包 com.arno.learn.grow.tiny.configuration.source

   2.2 monitor 模块需后续填充，时间紧迫暂未处理

   2.3 加入配置值类型转换 详见包 com.arno.learn.grow.tiny.configuration.converter


### 