package com.cscb07.taamapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import com.cscb07.taamapp.util.ListStrategy;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.Strict.class)
public class ListStrategyTest {
    @Test
    public void mostMethods_sampleList_callsListItems() {
        //noinspection unchecked
        List<Object> instance = mock(List.class);
        ListStrategy<Object> sut = new ListStrategy<>(instance);

        sut.size();
        sut.isEmpty();
        sut.contains(null);
        sut.iterator();
        sut.toArray();
        sut.toArray(new Object[10]);
        sut.add(null);
        sut.remove(null);
        sut.containsAll(instance);
        sut.addAll(instance);
        sut.addAll(instance);
        sut.removeAll(instance);
        sut.retainAll(instance);
        sut.clear();
        sut.get(0);
        sut.set(0, null);
        sut.add(0, null);
        sut.remove(0);
        sut.indexOf(null);
        sut.lastIndexOf(null);
        sut.listIterator();
        sut.listIterator(0);
        sut.subList(0, 1);

        verify(instance, atLeastOnce()).size();
        verify(instance, atLeastOnce()).isEmpty();
        verify(instance, atLeastOnce()).contains(null);
        verify(instance, atLeastOnce()).iterator();
        verify(instance, atLeastOnce()).toArray();
        verify(instance, atLeastOnce()).toArray(any(Object[].class));
        verify(instance, atLeastOnce()).add(null);
        verify(instance, atLeastOnce()).remove(null);
        verify(instance, atLeastOnce()).containsAll(any());
        verify(instance, atLeastOnce()).addAll(any());
        verify(instance, atLeastOnce()).addAll(any());
        verify(instance, atLeastOnce()).removeAll(any());
        verify(instance, atLeastOnce()).retainAll(any());
        verify(instance, atLeastOnce()).clear();
        verify(instance, atLeastOnce()).get(0);
        verify(instance, atLeastOnce()).set(0, null);
        verify(instance, atLeastOnce()).add(0, null);
        verify(instance, atLeastOnce()).remove(0);
        verify(instance, atLeastOnce()).indexOf(null);
        verify(instance, atLeastOnce()).lastIndexOf(null);
        verify(instance, atLeastOnce()).listIterator();
        verify(instance, atLeastOnce()).listIterator(0);
        verify(instance, atLeastOnce()).subList(0, 1);
    }

    @Test
    public void strategyGetSet_switchInstance_callsSameInstance() {
        //noinspection unchecked
        List<Object> oldInstance = mock(List.class);
        //noinspection unchecked
        List<Object> newInstance = mock(List.class);
        ListStrategy<Object> sut = new ListStrategy<>(oldInstance);

        sut.setListStrategy(newInstance);
        sut.size();
        List<Object> result = sut.getListStrategy();

        verifyNoInteractions(oldInstance);
        verify(newInstance, only()).size();
        assertSame(result, newInstance);
    }
}
