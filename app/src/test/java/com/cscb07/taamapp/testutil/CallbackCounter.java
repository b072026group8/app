package com.cscb07.taamapp.testutil;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for tests to allow callbacks to signal they have been called.
 * @param <T> The return type to use.
 */
public class CallbackCounter<T> {
    private int count = 0;
    private List<T> returnValues = new ArrayList<>();

    /** Gets amount of times the {@link CallbackCounter#signal} was called. */
    public int getCallbackCount() {
        return count;
    }

    /** Signals that a callback was called, and passback a value. */
    public void signal(T returnValue) {
        count++;
        returnValues.add(returnValue);
    }

    /** Gets the list of return values given by the callback. */
    public List<T> getReturnValues() {
        return returnValues;
    }

    /** Resets the number of times signalled and returned values to 0. */
    public void reset() {
        count = 0;
        getReturnValues().clear();
    }
}
