package com.cscb07.taamapp.util;

import android.util.Log;

/**
 * A logger for a class to log.
 * Exists for dependency inversion purposes, as {@link Log}
 * requires Context to run, so cant be unit tested.
 */
public interface Logger {
    void v(String tag, String msg);
    void d(String tag, String msg);
    void i(String tag, String msg);
    void w(String tag, String msg);
    void e(String tag, String msg);
    void wtf(String tag, String msg);
    void v(String tag, String msg, Throwable tr);
    void d(String tag, String msg, Throwable tr);
    void i(String tag, String msg, Throwable tr);
    void w(String tag, String msg, Throwable tr);
    void e(String tag, String msg, Throwable tr);
    void wtf(String tag, String msg, Throwable tr);
}
