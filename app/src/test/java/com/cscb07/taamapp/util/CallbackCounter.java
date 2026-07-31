package com.cscb07.taamapp.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for tests to allow callbacks to signal they have been called.
 * @param <T> The return type to use.
 */
public class CallbackCounter<T> {
    private int count = 0;
    private List<T> returnValues = new ArrayList<>();

    public int getCallbackCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void signal(T returnValue) {
        count++;
        returnValues.add(returnValue);
    }

    public List<T> getReturnValues() {
        return returnValues;
    }
}
