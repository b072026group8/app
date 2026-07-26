package com.cscb07.taamapp.util;

/**
 * A simple listener to be called when some data is updated.
 * @param <T> the type of data that is listened to.
 */
public interface UpdateListener<T> {

    /**
     * Called when an update happens and observers must be notified
     * @param value The new or updated value
     */
    void onChange(T value);
}
