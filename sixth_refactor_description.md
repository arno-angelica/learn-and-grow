# 2021.04.10 第六次重构说明

## 调整说明

- 新增模块 tiny-cache，根据 JSR107 实现 JCache

- 代码结构

  └── src

    ├── main.java

    │  │  └── com.arno.learn.grow.tiny.cache

    │  │              ├── caching

    │  │              │  ├── memory      ---  内存 cache

    │  │              │  └── redis           ---  Redis 实现

    │  │              │      ├── jedis         ---  Jedis 实现 CacheManager

    │  │              │      └── lettuce    ---  Lettuce 实现 CacheManager

    │  │              ├── configruation   ---  JCache configuration 实现

    │  │              ├── event                 ---  JCache 事件实现

    │  │              ├── integration       ---  JCache integration 实现

    │  │              └── serializable      ---  序列化相关

    │  └── resources.META-INF        ---  配置文件

    │      └── services                          ---  SPI

    └── test.java

  ​      └── com.arno.learn.grow.tiny.cache

  ​                  └── test                            --- 测试类

  ​                    └── event                       --- 测试事件

## **作业一：提供一套抽象 API 实现对象的序列化和反序列化**

### 作业内容

- 新增序列化和反序列化统一接口[**SerializableProvider**](https://github.com/arno-angelica/learn-and-grow/blob/sixth_refactor/tiny-cache/src/main/java/com/arno/learn/grow/tiny/cache/serializable/SerializableProvider.java)

- 新增`SerializablePorvider` 生成器[**SerializableManager**](https://github.com/arno-angelica/learn-and-grow/blob/sixth_refactor/tiny-cache/src/main/java/com/arno/learn/grow/tiny/cache/serializable/SerializableManager.java)
  - 生成方式`SerializableManager.getSerializableProvider(SERIALIZABLE_TYPE);`
  - SERIALIZABLE_TYPE 不传入或为空时是默认 java serializable
- java 序列话实现类 [**JavaSerializable**](https://github.com/arno-angelica/learn-and-grow/blob/sixth_refactor/tiny-cache/src/main/java/com/arno/learn/grow/tiny/cache/serializable/JavaSerializable.java)
- Jackson 序列号实现类[**JacksonSerializable**](https://github.com/arno-angelica/learn-and-grow/blob/sixth_refactor/tiny-cache/src/main/java/com/arno/learn/grow/tiny/cache/serializable/JacksonSerializable.java)
- 切换方式，通过` java -D` 方式切换
  - -Dserializable.type = JavaSerializable 为 java 序列化
  - -Dserializable.type = JacksonSerializable 为 Jackson 序列化
  - ![image-20210412123613995](/Users/arno/workspace/source/learn-and-grow/image-20210412123613995.png)

## 作业二：实现 lettuceManager

### 作业内容

- 新增 lettuce 实现类，在[**包 lettuce**](https://github.com/arno-angelica/learn-and-grow/tree/sixth_refactor/tiny-cache/src/main/java/com/arno/learn/grow/tiny/cache/caching/redis/lettuce)下
- 切换 redis 客户端的方式为：修改 [**Properties 配置**](https://github.com/arno-angelica/learn-and-grow/blob/sixth_refactor/tiny-cache/src/main/resources/META-INF/default-caching-provider.properties)
  - Jedis 配置
    - javax.cache.CacheManager.mappings.redis = com.arno.learn.grow.tiny.cache.caching.redis.jedis.JedisCacheManager
  - Lettuce 配置
    - javax.cache.CacheManager.mappings.redis=com.arno.learn.grow.tiny.cache.caching.redis.lettuce.LettuceCacheManager

## 其他改动

调整 [**AbstractCache**](https://github.com/arno-angelica/learn-and-grow/blob/sixth_refactor/tiny-cache/src/main/java/com/arno/learn/grow/tiny/cache/AbstractCache.java)，新增与子类共享属性 `SerializableProvider`

![image-20210412124251505](/Users/arno/workspace/source/learn-and-grow/abstractCache.png)

## 测试说明

测试类为[**CachingTest**](https://github.com/arno-angelica/learn-and-grow/blob/sixth_refactor/tiny-cache/src/test/java/com/arno/learn/grow/tiny/cache/test/CachingTest.java)

## 后续优化

- 过期策略、淘汰策略实现
- JMX 的实现