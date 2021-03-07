package com.arno.learn.grow.tiny.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @desc: 标记 DB 配置类
 * @author: Arno.KV
 * @date: 2021/3/1 下午10:03
 * @version:
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DbRepository {

    String value() default "";
}
