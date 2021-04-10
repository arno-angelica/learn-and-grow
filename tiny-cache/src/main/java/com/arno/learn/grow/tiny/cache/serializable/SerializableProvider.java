package com.arno.learn.grow.tiny.cache.serializable;

/**
 * Desc:
 * Author: angelica
 * Date: 2021/4/10
 */
public interface SerializableProvider {

    byte[] serialize(Object value);

    <T> T deserialize(byte[] bytes);
}
