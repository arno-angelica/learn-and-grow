# Spring Cache & Session

## 作业内容：

- Spring Cache 与 Redis 整合
- 如何将 RedisCacheManager 与 @Cacheable 注解打通

## 非作业内容

- 将 spring cache 和自己实现的 JCache 打通

## 作业实现

### 1. Spring Cache 与 Redis 整合

- [**基于 Redis 实现的 Spring Cache 类**](https://github.com/arno-angelica/learn-and-grow/blob/ninth_refactor/spring-user-web/src/main/java/com/arno/spring/user/web/config/cache/RedisCache.java)

### 2. 如何将 RedisCacheManager 与 @Cacheable 注解打通

- [**Spring CacheManager Bean Config 类**](https://github.com/arno-angelica/learn-and-grow/blob/ninth_refactor/spring-user-web/src/main/java/com/arno/spring/user/web/config/cache/SpringRedisConfig.java)
- [**Spring CacheManager 实现类**](https://github.com/arno-angelica/learn-and-grow/blob/ninth_refactor/spring-user-web/src/main/java/com/arno/spring/user/web/config/cache/SpringCacheRedisManager.java)

- [**测试类**](https://github.com/arno-angelica/learn-and-grow/blob/ninth_refactor/spring-user-web/src/test/java/com/arno/spring/user/web/config/cache/test/SpringCacheConfigTest.java)

## 非作业内容

### 将 spring cache 和自己实现的 JCache 打通

- [**打通自己实现的 JCache 和 Spring Cache**](https://github.com/arno-angelica/learn-and-grow/blob/ninth_refactor/spring-user-web/src/main/java/com/arno/spring/user/web/config/cache/JCacheConfig.java)
- [**测试类**](https://github.com/arno-angelica/learn-and-grow/blob/ninth_refactor/spring-user-web/src/test/java/com/arno/spring/user/web/config/cache/test/JCacheConfigTest.java)
- 详见 [**原 JCache 的实现项目 tiny-cache**](https://github.com/arno-angelica/learn-and-grow/blob/ninth_refactor/tiny-cache/src/main/java/com/arno/learn/grow/tiny/cache/AbstractCacheManager.java) 文档详见 [**第六次重构说明**](https://github.com/arno-angelica/learn-and-grow/blob/ninth_refactor/sixth_refactor_description.md)

