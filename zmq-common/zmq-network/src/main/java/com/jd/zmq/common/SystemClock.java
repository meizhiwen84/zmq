package com.jd.zmq.common;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by meizhiwen on 2017/1/16.
 */
public class SystemClock {
    private static final SystemClock instance = new SystemClock();
    private final long precision;
    private final AtomicLong now;
    private ScheduledExecutorService scheduler;

    public static SystemClock getInstance() {
        return instance;
    }

    public SystemClock() {
        this(1L);
    }

    public SystemClock(long precision) {
        this.precision = precision;
        this.now = new AtomicLong(System.currentTimeMillis());
        this.scheduler = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable, "System_Clock");
                thread.setDaemon(true);
                return thread;
            }
        });
        this.scheduler.scheduleAtFixedRate(new SystemClock.Timer(this.now), precision, precision, TimeUnit.MILLISECONDS);
    }

    public long now() {
        return this.now.get();
    }

    public long precision() {
        return this.precision;
    }

    protected class Timer implements Runnable {
        private final AtomicLong now;

        private Timer(AtomicLong now) {
            this.now = now;
        }

        public void run() {
            this.now.set(System.currentTimeMillis());
        }
    }
}
