package com.jd.zmq.common;

/**
 * Created by meizhiwen on 2017/1/16.
 */
public class EventException extends RuntimeException {
    public EventException(Exception cause) {
        super(cause);
    }
}
