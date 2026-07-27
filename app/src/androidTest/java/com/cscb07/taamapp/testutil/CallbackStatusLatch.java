package com.cscb07.taamapp.testutil;

import androidx.annotation.Nullable;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * A latch for asynchronous callbacks to signal to tests that they were called
 * and provide a value to perform further asserts.
 * @param <T> The value the callback passes back.
 */
public final class CallbackStatusLatch<T> {
    T result = null;

    /**
     * @return The value the callback passes back, or null if not signaled yet.
     */
    @Nullable
    public T getResult() { return result; }

    CountDownLatch latch;
    public CallbackStatusLatch() {
        latch = new CountDownLatch(1);
    }

    /**
     * Signal that a callback was called to awaiting threads.
     * @param result The value to pass back.
     */
    public void countDown(T result) {
        this.result = result;
        latch.countDown();
    }

    /**
     * Pauses the current thread until a callback signals it was called, or until
     * the specified waiting time (approximately) elapses.
     * @param durationMilliseconds The duration in milliseconds to wait.
     * @return <c>true</c> if a callback has signalled within the duration,
     *         <c>false</c> if the duration expires otherwise.
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    public boolean awaitCallback(int durationMilliseconds) throws InterruptedException {
        final int awaitDuration = 100;
        int awaitCount = (int)(durationMilliseconds /(double)awaitDuration + 0.5);
        for (int i = 0; i < awaitCount; i++) {
            if (latch.await(awaitDuration, TimeUnit.MILLISECONDS)) {
                return true;
            }
        }
        return false;
    }


}
