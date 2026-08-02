package com.cscb07.taamapp.util;

import java.util.ArrayList;

/**
 * Provides methods to enable the Observer pattern.
 * <p>
 * Provided as a reimplementation of {@link android.database.Observable} that doesn't rely on
 * android, so it and subclasses can be used in unit tests.
 */
public class Observable<T> extends android.database.Observable<T> {
    protected final ArrayList<T> mObservers = new ArrayList<T>();

    /**
     * Register the observer.
     * @param observer The observer to register
     * @throws IllegalArgumentException observer is null
     * @throws IllegalStateException couldn't add observer
     */
    @Override
    public void registerObserver(T observer) throws IllegalArgumentException, IllegalStateException {
        if (observer == null) {
            throw new IllegalArgumentException("argument 'observer' is null");
        }
        if (mObservers.contains(observer)) {
            throw new IllegalStateException("observer already registered.");
        }
        mObservers.add(observer);
    }

    /**
     * Unregister the observer.
     * @param observer The observer to remove
     * @throws IllegalArgumentException observer is null
     * @throws IllegalStateException couldn't remove register.
     */
    @Override
    public void unregisterObserver(T observer) throws IllegalArgumentException, IllegalStateException {
        if (observer == null) {
            throw new IllegalArgumentException("argument 'observer' is null");
        }
        if (!mObservers.remove(observer)) {
            throw new IllegalStateException("");
        }
    }

    /** Remove all registers */
    @Override
    public final void unregisterAll() {
        while (!mObservers.isEmpty()) {
            unregisterObserver(mObservers.get(0));
        }
    }
}
