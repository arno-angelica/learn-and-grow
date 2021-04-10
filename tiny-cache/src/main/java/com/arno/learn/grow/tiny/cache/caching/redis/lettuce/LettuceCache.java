package com.arno.learn.grow.tiny.cache.caching.redis.lettuce;

import com.arno.learn.grow.tiny.cache.AbstractCache;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * Desc:
 * Author: angelica
 * Date: 2021/4/10
 */
public class LettuceCache<K extends Serializable, V extends Serializable> extends AbstractCache<K, V> {

    private final StatefulRedisConnection<K, String> connection;

    private final RedisCommands<K, String> sync;

    public LettuceCache(CacheManager cacheManager, String cacheName,
                        Configuration<K, V> configuration, StatefulRedisConnection<K, String> connection) {
        super(cacheManager, cacheName, configuration);
        this.connection = connection;
        this.sync = connection.sync();
    }

    @Override
    protected V doGet(K key) throws CacheException, ClassCastException {
        String value = sync.get(key);
        if (value == null) {
            return null;
        }
        return serializableProvider.deserialize(value.getBytes(Charset.defaultCharset()));
    }

    @Override
    protected V doPut(K key, V value) throws CacheException, ClassCastException {
        V oldValue = doGet(key);
        sync.set(key, new String(serializableProvider.serialize(value), Charset.defaultCharset()));
        return oldValue;
    }

    @Override
    protected V doRemove(K key) throws CacheException, ClassCastException {
        V oldValue = doGet(key);
        sync.del(key);
        return oldValue;
    }

    @Override
    protected void doClear() throws CacheException {

    }

    @Override
    protected Iterator<Entry<K, V>> newIterator() {
        return null;
    }

    @Override
    protected void doClose() {
        if (connection.isOpen()) {
            connection.close();
        }
    }
}
