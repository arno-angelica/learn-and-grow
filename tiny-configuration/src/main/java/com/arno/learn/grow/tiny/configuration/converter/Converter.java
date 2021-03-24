package com.arno.learn.grow.tiny.configuration.converter;

import java.io.Serializable;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/3/16 下午10:12
 * @version:
 */
@Deprecated
public interface Converter<T> extends Serializable {

    T convert(String source) throws IllegalArgumentException, NullPointerException;

    String covertType();
}
