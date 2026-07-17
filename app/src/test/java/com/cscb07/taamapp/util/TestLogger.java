package com.cscb07.taamapp.util;

import java.util.ArrayList;

/**
 * A simple logger using {@link System#out}, to avoid android dependency.
 */
public class TestLogger implements Logger{
    public ArrayList<String> messages = new ArrayList<>();

    private void log(Character level, String tag, String msg, Throwable tr) {
        log(level.toString(), tag, msg, tr);
    }
    private void log(String level, String tag, String msg, Throwable tr)
    {
        String message = level + " - " + tag + ": " + msg + " | " + tr;
        messages.add(message);
        System.out.println(message);
    }
    @Override
    public void v(String tag, String msg) {
        v(tag, msg, null);
    }

    @Override
    public void d(String tag, String msg) {
        d(tag, msg, null);
    }

    @Override
    public void i(String tag, String msg) {
        i(tag, msg, null);
    }

    @Override
    public void w(String tag, String msg) {
        w(tag, msg, null);
    }
    @Override
    public void wtf(String tag, String msg) {
        wtf(tag, msg, null);
    }

    @Override
    public void e(String tag, String msg) {
        e(tag, msg, null);
    }

    @Override
    public void v(String tag, String msg, Throwable tr) {
        log('V', tag, msg, tr);
    }

    @Override
    public void d(String tag, String msg, Throwable tr) {
        log('D', tag, msg, tr);
    }

    @Override
    public void i(String tag, String msg, Throwable tr) {
        log('I', tag, msg, tr);
    }

    @Override
    public void w(String tag, String msg, Throwable tr) {
        log('W', tag, msg, tr);
    }

    @Override
    public void e(String tag, String msg, Throwable tr) {
        log('E', tag, msg, tr);
    }
    @Override
    public void wtf(String tag, String msg, Throwable tr) {
        log("WTF", tag, msg, tr);
    }
}
