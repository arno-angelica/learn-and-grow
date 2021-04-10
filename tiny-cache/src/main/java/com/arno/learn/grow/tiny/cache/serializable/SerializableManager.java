package com.arno.learn.grow.tiny.cache.serializable;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Desc:
 * Author: angelica
 * Date: 2021/4/10
 */
public class SerializableManager {

    private static final SerializableRepository REPOSITORY = new SerializableRepository();

    /**
     * Set up the default class loader
     * @param classLoader
     */
    public static void setDefaultClassLoader(ClassLoader classLoader) {
        REPOSITORY.setDefaultClassLoader(classLoader);
    }

    public static SerializableProvider getSerializableProvider() {
       return REPOSITORY.getSerializableProvider();
    }

    public static SerializableProvider getSerializableProvider(String type) {
        return REPOSITORY.getSerializableProvider(type);
    }

    public static SerializableProvider getSerializableProvider(String type, ClassLoader classLoader) {
        return REPOSITORY.getSerializableProvider(classLoader, type);
    }

    /**
     * private serializable repository
     */
    private static class SerializableRepository {

        private static final String DEFAULT_SERIALIZABLE_TYPE = "JavaSerializable";
        /**
         * Stores the SerializableProvider implementation class
         */
        private static final ConcurrentMap<String, SerializableProvider> REPOSITORY = new ConcurrentHashMap<>();

        /**
         * classLoader
         */
        private volatile ClassLoader classLoader;

        public void setDefaultClassLoader(ClassLoader classLoader) {
            this.classLoader = classLoader;
        }

        public ClassLoader getDefaultClassLoader() {
            ClassLoader cl = this.classLoader;
            return cl == null ? Thread.currentThread().getContextClassLoader() : cl;
        }

        public String getDefaultSerializableType() {
            return DEFAULT_SERIALIZABLE_TYPE;
        }

        /**
         * Find SerializableProvider implementations, if it doesn't exist in the REPOSITORY <br/>
         * the implementation class is loaded by SPI
         * @param classLoader classLoader
         * @param serializableType serializable type
         * @return exist return SerializableProvider， else throw NoSuchElementException
         */
        public SerializableProvider getSerializableProvider(ClassLoader classLoader, String serializableType) {
            final ClassLoader serviceClassLoader = classLoader == null ? getDefaultClassLoader() : classLoader;
            final String defaultType = serializableType == null || serializableType.trim().length() == 0
                    ? getDefaultSerializableType() : serializableType;

            SerializableProvider provider = REPOSITORY.get(defaultType);
            if (provider == null) {

                provider = AccessController.doPrivileged((PrivilegedAction<SerializableProvider>)() -> {
                    ServiceLoader<SerializableProvider> serviceLoader = ServiceLoader.load(SerializableProvider.class, serviceClassLoader);
                    for (SerializableProvider tempProvider : serviceLoader) {
                        REPOSITORY.put(tempProvider.getClass().getSimpleName(), tempProvider);
                    }
                    return REPOSITORY.get(defaultType);
                });
            }
            if (provider == null) {
                throw new NoSuchElementException("no serialization implementation was found by " + defaultType);
            }
            return provider;
        }

        public SerializableProvider getSerializableProvider(String serializableType) {
            return getSerializableProvider(getDefaultClassLoader(), serializableType);
        }

        public SerializableProvider getSerializableProvider() {
            return getSerializableProvider(getDefaultClassLoader(), getDefaultSerializableType());
        }

    }


}
