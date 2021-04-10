package com.arno.learn.grow.tiny.cache.integration;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

/**
 * Desc:
 * Author: angelica
 * Date: 2021/4/10
 */
@SuppressWarnings("rawtypes")
public class CompositeFallbackStorage extends AbstractFallbackStorage<Object, Object> {

    private static final ConcurrentMap<ClassLoader, List<FallbackStorage>> fallbackStoragesCache =
            new ConcurrentHashMap<>();

    private final List<FallbackStorage> fallbackStorageList;

    public CompositeFallbackStorage(ClassLoader classLoader) {
        super(Integer.MIN_VALUE);
        this.fallbackStorageList = fallbackStoragesCache.computeIfAbsent(classLoader, this::loadFallbackStorageList);
    }

    private List<FallbackStorage> loadFallbackStorageList(ClassLoader classLoader) {
        return stream(ServiceLoader.load(FallbackStorage.class, classLoader).spliterator(), false)
                .sorted(PRIORITY_COMPARATOR)
                .collect(toList());
    }

    public CompositeFallbackStorage() {
        this(Thread.currentThread().getContextClassLoader());
    }

    @Override
    public Object load(Object key) throws CacheLoaderException {
        Object value = null;
        for (FallbackStorage fallbackStorage : fallbackStorageList) {
            value = fallbackStorage.load(key);
            if (value != null) {
                break;
            }
        }
        return value;
    }

    @Override
    public void write(Cache.Entry entry) throws CacheWriterException {
        fallbackStorageList.forEach(fallbackStorage -> fallbackStorage.write(entry));
    }

    @Override
    public void delete(Object key) throws CacheWriterException {
        fallbackStorageList.forEach(fallbackStorage -> fallbackStorage.delete(key));
    }
}
