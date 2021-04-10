package com.arno.learn.grow.tiny.cache;

import com.arno.learn.grow.tiny.cache.configruation.ConfigurationUtils;
import com.arno.learn.grow.tiny.cache.event.CacheEntryEventPublisher;
import com.arno.learn.grow.tiny.cache.integration.CompositeFallbackStorage;
import com.arno.learn.grow.tiny.cache.integration.FallbackStorage;
import com.arno.learn.grow.tiny.cache.serializable.SerializableManager;
import com.arno.learn.grow.tiny.cache.serializable.SerializableProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.CacheEntryListenerConfiguration;
import javax.cache.configuration.CompleteConfiguration;
import javax.cache.configuration.Configuration;
import javax.cache.integration.CompletionListener;
import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.EntryProcessorResult;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.arno.learn.grow.tiny.cache.event.GenericCacheEntryEvent.createdEvent;
import static com.arno.learn.grow.tiny.cache.event.GenericCacheEntryEvent.expiredEvent;
import static com.arno.learn.grow.tiny.cache.event.GenericCacheEntryEvent.removedEvent;
import static com.arno.learn.grow.tiny.cache.event.GenericCacheEntryEvent.updatedEvent;

/**
 * Desc:
 * Author: angelica
 * Date: 2021/4/10
 */
public abstract class AbstractCache<K, V> implements Cache<K, V> {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public static final String SERIALIZABLE_TYPE = System.getProperty("serializable.type");

    protected final CacheManager cacheManager;

    protected final String cacheName;

    protected final CompleteConfiguration<K, V> configuration;

    protected final FallbackStorage fallbackStorage;

    private final CacheEntryEventPublisher entryEventPublisher;

    private volatile boolean closed = false;

    protected final SerializableProvider serializableProvider;

    protected AbstractCache(CacheManager cacheManager, String cacheName, Configuration<K, V> configuration) {
        this.cacheManager = cacheManager;
        this.cacheName = cacheName;
        this.configuration = ConfigurationUtils.completeConfiguration(configuration);
        this.fallbackStorage = new CompositeFallbackStorage(cacheManager.getClassLoader());
        this.entryEventPublisher = new CacheEntryEventPublisher();
        this.serializableProvider = SerializableManager.getSerializableProvider(SERIALIZABLE_TYPE);
    }

    @Override
    public V get(K key) {
        assertNotClosed();
        assertNotNull(key, "key");
        V value = null;
        try {
            value = doGet(key);
        } catch (Throwable e) {
            logger.error(e.getMessage());
        }
        return loadValueIfReadThrough(key, value);
    }

    private V loadValueIfReadThrough(K key, V value) {
        V result = value;
        if (value == null && configuration.isReadThrough()) {
            result = (V) fallbackStorage.load(key);
            // re-write into cache
            if (result != null) {
                put(key, result);
            }
        }
        return result;
    }

    protected abstract V doGet(K key) throws CacheException, ClassCastException;

    @Override
    public Map<K, V> getAll(Set<? extends K> keys) {
        // Keep the order of keys
        Map<K, V> result = new LinkedHashMap<>();
        for (K key : keys) {
            result.put(key, get(key));
        }
        return result;
    }

    @Override
    public boolean containsKey(K key) {
        assertNotClosed();
        assertNotNull(key, "key");
        return get(key) != null;
    }

    @Override
    public void loadAll(Set<? extends K> keys, boolean replaceExistingValues,
                        CompletionListener completionListener) {
        assertNotClosed();
        // TODO
        throw new UnsupportedOperationException("This feature will be supported in the future");
    }

    @Override
    public void put(K key, V value) {
        assertNotClosed();
        assertNotNull(key, "key");
        assertNotNull(value, "value");
        try {
            V oldValue = doPut(key, value);
            if (oldValue == null) {
                publishCreatedEvent(key, value);
            } else {
                publishUpdatedEvent(key, oldValue, value);
            }
        } finally {
            writeIfWriteThrough(key, value);
        }
    }

    /**
     * Associates the specified value with the specified key in this Cache
     * (optional operation).  If the map previously contained a mapping for
     * the key, the old value is replaced by the specified value.  (A map
     * <tt>m</tt> is said to contain a mapping for a key <tt>k</tt> if and only
     * if {@link #containsKey(Object)} (Object) m.containsKey(k)} would return
     * <tt>true</tt>.)
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with <tt>key</tt>, or
     * <tt>null</tt> if there was no mapping for <tt>key</tt>.
     * (A <tt>null</tt> return can also indicate that the map
     * previously associated <tt>null</tt> with <tt>key</tt>,
     * if the implementation supports <tt>null</tt> values.)
     */
    protected abstract V doPut(K key, V value) throws CacheException, ClassCastException;

    @Override
    public V getAndPut(K key, V value) {
        V oldValue = get(key);
        put(key, value);
        return oldValue;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public boolean putIfAbsent(K key, V value) {
        if (!containsKey(key)) {
            put(key, value);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean remove(K key) {
        assertNotClosed();
        assertNotNull(key, "key");
        boolean removed;
        try {
            V oldValue = doRemove(key);
            removed = oldValue != null;
            if (removed) {
                publishRemovedEvent(key, oldValue);
            }
        } finally {
            deleteIfWriteThrough(key);
        }
        return removed;
    }

    private void deleteIfWriteThrough(K key) {
        if (configuration.isWriteThrough()) {
            fallbackStorage.delete(key);
        }
    }

    protected abstract V doRemove(K key) throws CacheException, ClassCastException;

    @Override
    public boolean remove(K key, V oldValue) {
        if (containsKey(key) && Objects.equals(get(key), oldValue)) {
            remove(key);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public V getAndRemove(K key) {
        V oldValue = get(key);
        remove(key);
        return oldValue;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        assertNotNull(key, "oldValue");
        if (Objects.equals(get(key), oldValue)) {
            put(key, newValue);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean replace(K key, V value) {
        if (containsKey(key)) {
            put(key, value);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public V getAndReplace(K key, V value) {
        V oldValue = get(key);
        if (oldValue != null) {
            put(key, value);
        }
        return oldValue;
    }

    @Override
    public void removeAll(Set<? extends K> keys) {
        for (K key : keys) {
            remove(key);
        }
    }

    @Override
    public void removeAll() {
        for (Entry<K, V> entry : this) {
            K key = entry.getKey();
            remove(key);
        }
    }

    @Override
    public void clear() {
        assertNotClosed();
        doClear();
    }

    protected abstract void doClear() throws CacheException;

    @Override
    public <C extends Configuration<K, V>> C getConfiguration(Class<C> clazz) {
        Configuration<K, V> configuration = unwrap(clazz);
        return (C) ConfigurationUtils.completeConfiguration(configuration);
    }

    @Override
    public <T> T invoke(K key, EntryProcessor<K, V, T> entryProcessor, Object... arguments) throws EntryProcessorException {
        // TODO
        throw new UnsupportedOperationException("This feature will be supported in the future");
    }

    @Override
    public <T> Map<K, EntryProcessorResult<T>> invokeAll(Set<? extends K> keys, EntryProcessor<K, V, T> entryProcessor, Object... arguments) {
        // TODO
        throw new UnsupportedOperationException("This feature will be supported in the future");
    }


    @Override
    public void registerCacheEntryListener(CacheEntryListenerConfiguration<K, V> cacheEntryListenerConfiguration) {
        entryEventPublisher.registerCacheEntryListener(cacheEntryListenerConfiguration);
    }

    @Override
    public void deregisterCacheEntryListener(CacheEntryListenerConfiguration<K, V> cacheEntryListenerConfiguration) {
        entryEventPublisher.deregisterCacheEntryListener(cacheEntryListenerConfiguration);
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        assertNotClosed();
        return newIterator();
    }

    protected abstract Iterator<Entry<K, V>> newIterator();

    @Override
    public <T> T unwrap(Class<T> clazz) {
        T value;
        try {
            value = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return value;
    }

    @Override
    public final String getName() {
        return cacheName;
    }

    @Override
    public final CacheManager getCacheManager() {
        return cacheManager;
    }

    @Override
    public final void close() {
        if (isClosed()) {
            return;
        }
        doClose();
        closed = true;
    }

    /**
     * Subclass could override this method.
     */
    protected void doClose() {
    }

    @Override
    public final boolean isClosed() {
        return closed;
    }


    private void publishCreatedEvent(K key, V value) {
        entryEventPublisher.publish(createdEvent(this, key, value));
    }

    private void publishUpdatedEvent(K key, V oldValue, V value) {
        entryEventPublisher.publish(updatedEvent(this, key, oldValue, value));
    }

    private void publishExpiredEvent(K key, V oldValue) {
        entryEventPublisher.publish(expiredEvent(this, key, oldValue));
    }

    private void publishRemovedEvent(K key, V oldValue) {
        entryEventPublisher.publish(removedEvent(this, key, oldValue));
    }

    private void writeIfWriteThrough(K key, V value) {
        if (configuration.isWriteThrough()) {
            fallbackStorage.write(EntryAdapter.of(key, value));
        }
    }

    private void assertNotClosed() {
        if (isClosed()) {
            throw new IllegalStateException("Current cache has been closed! No operation should be executed.");
        }
    }

    protected static void assertNotNull(Object object, String source) {
        if (object == null) {
            throw new NullPointerException(source + " must not be null!");
        }
    }
}
