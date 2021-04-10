package com.arno.learn.grow.tiny.cache.caching.redis.jedis;

import com.arno.learn.grow.tiny.cache.AbstractCache;
import redis.clients.jedis.Jedis;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import java.util.Iterator;

/**
 * Desc:
 * Author: angelica
 * Date: 2021/4/10
 */
public class JedisCache<K, V> extends AbstractCache<K, V> {

    private final Jedis jedis;

    public JedisCache(CacheManager cacheManager, String cacheName,
                      Configuration<K, V> configuration, Jedis jedis) {
        super(cacheManager, cacheName, configuration);
        this.jedis = jedis;
    }

    @Override
    protected V doGet(K key) throws CacheException, ClassCastException {
        return serializableProvider.deserialize(jedis.get(serializableProvider.serialize(key)));
    }

    @Override
    protected V doPut(K key, V value) throws CacheException, ClassCastException {
        V oldValue = doGet(key);
        jedis.set(serializableProvider.serialize(key), serializableProvider.serialize(value));
        return oldValue;
    }

    @Override
    protected V doRemove(K key) throws CacheException, ClassCastException {
        V oldValue = doGet(key);
        jedis.del(serializableProvider.serialize(key));
        return oldValue;
    }

    @Override
    protected void doClear() throws CacheException {

    }

    @Override
    protected Iterator<Cache.Entry<K, V>> newIterator() {
        return null;
    }

    @Override
    protected void doClose() {
        this.jedis.close();
    }

}