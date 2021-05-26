# Mybatis - Spring

## 作业内容：

- 完善 `@org.geektimes.projects.user.mybatis.annotation.Enable MyBatis` 实现，尽可能多地注入 `org.mybatis.spring.SqlSessionFactoryBean` 中依赖的组件非作业内容

## 作业实现

### 1. 实现代码

- [**@EnableMybatis**](https://github.com/arno-angelica/learn-and-grow/blob/tenth_refactor/spring-user-web/src/main/java/com/arno/mybatis/annotation/EnableMybatis.java)
- [**BeanDefiniitionRegistrar**](https://github.com/arno-angelica/learn-and-grow/blob/tenth_refactor/spring-user-web/src/main/java/com/arno/mybatis/annotation/EnableMybatis.java)

### 2. 测试用例

[**测试类目录**](https://github.com/arno-angelica/learn-and-grow/tree/tenth_refactor/spring-user-web/src/test/java/com/arno/mybatis)

[**Mapper.xml 文件**](https://github.com/arno-angelica/learn-and-grow/tree/tenth_refactor/spring-user-web/src/test/resources/mapper)

[**EnableMybatisTest**](https://github.com/arno-angelica/learn-and-grow/blob/tenth_refactor/spring-user-web/src/test/java/com/arno/mybatis/EnableMybatisTest.java)

### 3. 输出结果

```text
[Person{id=2, name='3'}, Person{id=3, name='2'}, Person{id=123, name='呜啦啦啦'}]
```

