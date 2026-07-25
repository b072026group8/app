package com.cscb07.taamapp.util;

 /**
 * Callbacks to respond to when a db request results in success or failure.
 * @param <T> The type received on success.
 */
public interface OperationListener<T> {
    void onSuccess(T value);
    void onFailure(Exception value);

}
