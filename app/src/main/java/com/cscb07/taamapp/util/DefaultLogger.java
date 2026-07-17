package com.cscb07.taamapp.util;

import android.util.Log;

/**
 * A logger that delegates to {@link Log} methods.
 */
public class DefaultLogger implements Logger {
    static DefaultLogger log = null;
    public static DefaultLogger Instance() {
        if (log == null)
            log = new DefaultLogger();
        return log;
    }

    private DefaultLogger() {
    }

    @Override
    public void v(String tag, String msg) {
        Log.v(tag, msg);
    }

    @Override
    public void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    @Override
    public void i(String tag, String msg) {
        Log.i(tag, msg);
    }

    @Override
    public void w(String tag, String msg) {
        Log.w(tag, msg);
    }

    @Override
    public void e(String tag, String msg) {
        Log.e(tag, msg);
    }
    @Override
    public void wtf(String tag, String msg) {
        Log.wtf(tag, msg);
    }

    @Override
    public void v(String tag, String msg, Throwable tr) {
        Log.v(tag, msg, tr);
    }

    @Override
    public void d(String tag, String msg, Throwable tr) {
        Log.d(tag, msg, tr);
    }

    @Override
    public void i(String tag, String msg, Throwable tr) {
        Log.i(tag, msg, tr);
    }

    @Override
    public void w(String tag, String msg, Throwable tr) {
        Log.w(tag, msg, tr);
    }

    @Override
    public void e(String tag, String msg, Throwable tr) {
        Log.e(tag, msg, tr);
    }
    @Override
    public void wtf(String tag, String msg, Throwable tr) {
        Log.wtf(tag, msg, tr);
    }
}
