package com.cscb07.taamapp.util;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton allowing abstractions to be assigned implementations, and other classes to gain those
 * implementations for the specified abstractions.
 * <p>
 * An alternative to DI for Android fragments, in the case they're reconstructed.
 */
public class ServiceProvider {
    private static ServiceProvider instance;
    public static ServiceProvider getInstance() {
        if (instance == null) {
            instance = new ServiceProvider();
        }
        return instance;
    }
    private ServiceProvider() {
    }
    private final Map<Class<?>, Object> implementations = new HashMap<>();

    /**
     * gets an implementation for a service.
     * @param clazz The service's class object
     * @return An implementation of the service.
     * @param <T> The type of the service.
     */
    public <T> T getService(@NonNull Class<T> clazz) {
        Object implementation = implementations.get(clazz);
        if (implementation == null) {
            throw new IllegalStateException("An implementation for '" + clazz.getName() + "'  has not been set.");
        }

        if (implementation instanceof ServiceConstructor) {
            //noinspection unchecked
            return ((ServiceConstructor<T>) implementation).create();
        }
        //noinspection unchecked
        return (T) implementation;
    }

    /**
     * Adds a singleton implementation to the specified service, which is reused for every request
     * for said service.
     * @param clazz The class object of the service.
     * @param service An instance of the service's implementer.
     * @param <T> Type of service.
     */
    public <T> void addSingleton(@NonNull Class<T> clazz, @NonNull T service) {
        implementations.put(clazz, service);
    }

    /**
     * Adds a transient implementation for the specified service, so that a new instance is used 
     * for every different request.
     * @param clazz The class object of the service. 
     * @param constructor A method to create an implementation for the service.
     * @param <T> Type of service.
     */
    public <T> void addTransient(@NonNull Class<T> clazz, @NonNull ServiceConstructor<T> constructor) {
        implementations.put(clazz, constructor);
    }


    /**
     * Defines a way to create a service.
     * @param <T> The type of the service to create.
     */
    public interface ServiceConstructor<T> {
        T create();
    }
}
