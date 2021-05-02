package com.arno.spring.user.web.config.cache;

import com.arno.learn.grow.tiny.cache.AbstractCache;
import com.arno.learn.grow.tiny.cache.serializable.SerializableManager;
import com.arno.learn.grow.tiny.cache.serializable.SerializableProvider;
import org.springframework.cache.Cache;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * @author Angelica.arno
 * @since 1.0.0
 */
public class RedisCache implements Cache {

    private final Jedis jedis;
    private final String name;
    private final SerializableProvider serializableProvider;
    private final byte[] keySet;

    public RedisCache(Jedis jedis, String name) {
        this.jedis = jedis;
        this.name = StringUtils.hasText(name) ? name : "spring.redis.cache";
        this.serializableProvider = SerializableManager.getSerializableProvider(AbstractCache.SERIALIZABLE_TYPE);
        this.keySet = serializableProvider.serialize("spring.redis.key");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return jedis;
    }

    @Override
    public ValueWrapper get(Object key) {
        byte[] keyBytes = serializableProvider.serialize(key);
        byte[] valueBytes = jedis.get(keyBytes);
        if (valueBytes == null) {
            return null;
        }
        return ()-> serializableProvider.deserialize(valueBytes);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        return null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return null;
    }

    @Override
    public void put(Object key, Object value) {
        byte[] keyBytes = serializableProvider.serialize(key);
        byte[] valueBytes = serializableProvider.serialize(value);
        jedis.set(keyBytes, valueBytes);
        saveKeySet(key);
    }

    @Override
    public void evict(Object key) {
        byte[] keyBytes = serializableProvider.serialize(key);
        jedis.del(keyBytes);
    }

    @Override
    public void clear() {
        Object keySetValue = serializableProvider.deserialize(jedis.get(keySet));
        if (keySetValue != null) {
            Set<Object> keySets = (Set<Object>) keySetValue;
            keySets.stream().map(serializableProvider::serialize).forEach(jedis::del);
            jedis.del(keySet);
        }
    }

    private void saveKeySet(Object key) {
        Object keySetValue = serializableProvider.deserialize(jedis.get(keySet));
        Set<Object> keySets;
        if (keySetValue != null) {
            keySets = (Set<Object>) keySetValue;
        } else {
            keySets = new HashSet<>();
        }
        keySets.add(key);
        byte[] valueBytes = serializableProvider.serialize(keySets);
        jedis.set(keySet, valueBytes);
    }
}
