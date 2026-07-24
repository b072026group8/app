package com.cscb07.taamapp.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * An implementation of {@link List<T>} that allows swapping out the exact implementation
 * without having to re-assign this instance.
 * @param <T> The item type to store.
 */
public class ListStrategy<T> implements List<T> {
    @NonNull
    private List<T> list;
    public ListStrategy(@NonNull List<T> list) {
        this.list = list;
    }

    @NonNull
    public List<T> getListStrategy() {
        return list;
    }

    public void setListStrategy(@NonNull List<T> list) {
        this.list = list;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(@Nullable Object o) {
        return list.contains(o);
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @NonNull
    @Override
    public <T1> T1[] toArray(@NonNull T1[] t1s) {
        return list.toArray(t1s);
    }

    @Override
    public boolean add(T t) {
        return list.add(t);
    }

    @Override
    public boolean remove(@Nullable Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> collection) {
        //noinspection SlowListContainsAll
        return list.containsAll(collection);
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends T> collection) {
        return list.addAll(collection);
    }

    @Override
    public boolean addAll(int i, @NonNull Collection<? extends T> collection) {
        return list.addAll(i, collection);
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> collection) {
        //noinspection SuspiciousMethodCalls
        return list.remove(collection);
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> collection) {
        return list.retainAll(collection);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public T get(int i) {
        return list.get(i);
    }

    @Override
    public T set(int i, T t) {
        return list.set(i, t);
    }

    @Override
    public void add(int i, T t) {
        list.add(i, t);
    }

    @Override
    public T remove(int i) {
        return list.remove(i);
    }

    @Override
    public int indexOf(@Nullable Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(@Nullable Object o) {
        return list.lastIndexOf(o);
    }

    @NonNull
    @Override
    public ListIterator<T> listIterator() {
        return list.listIterator();
    }

    @NonNull
    @Override
    public ListIterator<T> listIterator(int i) {
        return list.listIterator(i);
    }

    @NonNull
    @Override
    public List<T> subList(int i, int i1) {
        return list.subList(i, i1);
    }

}
