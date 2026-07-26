package com.cscb07.taamapp.util;

import android.database.Observable;

/**
 * Provides some value that may or may not change, and alerts registered listeners
 * when it does.
 * @param <T> the provided type.
 */
public abstract class Provider<T> extends Observable<UpdateListener<T>> {
    /**
     * Gets the currently provided value of {@link T}.
     * The provided instance may change upon update.
     * @return The currently provided value of {@link T}.
     */
    public abstract T getValue();

    /**
     * Updates the currently provided value. Updates registered listeners (even if nothing changes).
     */
    public void updateValue() {
        notifyListeners();
    }

    /**
     * A helper method to notify all listeners in {@link Observable#mObservers}.
     */
    protected final void notifyListeners() {
        for (UpdateListener<T> listener : mObservers) {
            listener.onChange(getValue());
        }
    }

    /**
     * Adds an observer/listener, then calls its {@link UpdateListener#onChange} method.
     * @param listener the listener to add.
     */
    @Override
    public void registerObserver(UpdateListener<T> listener) {
        super.registerObserver(listener);
        listener.onChange(getValue());
    }
}
