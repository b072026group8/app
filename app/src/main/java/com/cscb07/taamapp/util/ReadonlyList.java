package com.cscb07.taamapp.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * A specification of {@link List} representing a readonly list that cannot be modified.
 * This is done by providing default implementations that error or provide dummy values.
 * <p>
 * Specifically, defaults are provided for all methods marked as "optional" by {@link List}.
 */
public interface ReadonlyList<T> extends List<T> {
     /** Helper method for default implementations of {@link ReadonlyList}. Not meant to be used otherwise. */
     static void throwIfRequested(@NonNull ReadonlyList<?> self) {
        if (self.throwOnModification()) {
            throw new UnsupportedOperationException("List cannot be modified");
        }
    }

    /**
     * Indicates default behaviour for default methods of {@link ReadonlyList}.
     * @return True if the defaults should throw an exception,
     *         False if the defaults should do nothing and return dummy values.
     */
    default boolean throwOnModification() {
        return true;
    }

    @Override
    default boolean add(T t) {
        throwIfRequested(this);
        return false;
    }

    @Override
    default boolean remove(@Nullable Object o) {
        throwIfRequested(this);
        return false;
    }

    @Override
    default boolean addAll(@NonNull Collection<? extends T> collection) {
        throwIfRequested(this);
        return false;
    }

    @Override
    default boolean addAll(int i, @NonNull Collection<? extends T> collection) {
        throwIfRequested(this);
        return false;
    }

    @Override
    default boolean removeAll(@NonNull Collection<?> collection) {
        throwIfRequested(this);
        return false;
    }

    @Override
    default boolean retainAll(@NonNull Collection<?> collection) {
        throwIfRequested(this);
        return false;
    }

    @Override
    default void clear() {
        throwIfRequested(this);
    }

    @Override
    default T set(int i, T t) {
        throwIfRequested(this);
        return null;
    }

    @Override
    default void add(int i, T t) {
        throwIfRequested(this);
    }

    @Override
    default T remove(int i) {
        throwIfRequested(this);
        return null;
    }
}
