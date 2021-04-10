package com.arno.learn.grow.tiny.cache;

import javax.cache.Cache;
import java.util.Map;

/**
 * Desc:
 * Author: angelica
 * Date: 2021/4/10
 */
public class EntryAdapter<K, V> implements Cache.Entry<K, V> {

    private final K key;

    private final V value;

    private EntryAdapter(K key, V value) {
        this.key = key;
        this.value = value;
    }

    private EntryAdapter(Map.Entry<K, V> entry) {
        this(entry.getKey(), entry.getValue());
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public <T> T unwrap(Class<T> clazz) {
        T value = null;
        try {
            value = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return value;
    }

    public static <K, V> Cache.Entry<K, V> of(Map.Entry<K, V> entry) {
        return new EntryAdapter(entry);
    }

    public static <K, V> Cache.Entry<K, V> of(K key, V value) {
        return new EntryAdapter(key, value);
    }
}
