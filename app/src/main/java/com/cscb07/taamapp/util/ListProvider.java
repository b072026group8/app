package com.cscb07.taamapp.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * A list provider fills and periodically refills a provided {@link List} instance, and notifies
 * observers of any changes.
 * @param <T> The type of the list.
 */
public interface ListProvider<T> {
    /**
     * Sets the list instance to populate.
     * @param instance the instance to populate
     */
    void setList(@NonNull List<T> instance);

    /**
     * Adds a listener to call upon any changes to the provided list.
     * @param listener the listener to add.
     */
    void addOnChangeListener(ListChangeListener<T> listener);

    /**
     * A listener that is called whenever a {@link ListProvider} instance changes the provided list.
     * @param <T> The type of the list.
     */
    interface ListChangeListener<T> {
        /**
         * Called when an update happens to the provided list of {@link ListProvider}.
         * @param list The updated list.
         */
         void onChange(List<T> list);
    }
}
