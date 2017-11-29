package com.jd.zmq.common;

/**
 * Created by meizhiwen on 2017/1/16.
 */
public interface EventListener<E> {
    void onEvent(E var1);
}