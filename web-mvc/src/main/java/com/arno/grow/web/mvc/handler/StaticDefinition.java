package com.arno.grow.web.mvc.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/3/1 下午10:36
 * @version:
 */
public class StaticDefinition {

    public static Map<String, BeanDefinition> CONTEXT_MAP = new ConcurrentHashMap<>();

}
