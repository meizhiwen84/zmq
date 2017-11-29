package com.jd.zmq.common;

/**
 * Created by meizhiwen on 2017/1/16.
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class EventManager<E> {
    protected CopyOnWriteArrayList<EventListener<E>> listeners;
    protected BlockingQueue<Ownership> events;
    protected String name;
    protected Thread dispatcher;
    protected EventManager<E>.EventDispatcher eventDispatcher;
    protected AtomicBoolean started;
    protected boolean triggerNoEvent;
    protected long interval;
    protected long idleTime;
    protected long timeout;

    public EventManager() {
        this((String)null, 0);
    }

    public EventManager(EventListener<E> listener) {
        this((String)null, listener, 0);
    }

    public EventManager(String name) {
        this(name, 0);
    }

    public EventManager(String name, int capacity) {
        this.listeners = new CopyOnWriteArrayList();
        this.eventDispatcher = new EventManager.EventDispatcher();
        this.started = new AtomicBoolean(false);
        this.timeout = 1000L;
        this.name = name;
        if(capacity > 0) {
            this.events = new ArrayBlockingQueue(capacity);
        } else {
            this.events = new LinkedBlockingDeque();
        }

    }

    public EventManager(String name, EventListener<E> listener) {
        this(name, 0);
        this.addListener(listener);
    }

    public EventManager(String name, EventListener<E> listener, int capacity) {
        this(name, capacity);
        this.addListener(listener);
    }

    public EventManager(String name, List<? extends EventListener<E>> listeners) {
        this(name, 0);
        if(listeners != null) {
            Iterator var3 = listeners.iterator();

            while(var3.hasNext()) {
                EventListener listener = (EventListener)var3.next();
                this.addListener(listener);
            }
        }

    }

    public long getInterval() {
        return this.interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public long getIdleTime() {
        return this.idleTime;
    }

    public void setIdleTime(long idleTime) {
        this.idleTime = idleTime;
    }

    public long getTimeout() {
        return this.timeout;
    }

    public void setTimeout(long timeout) {
        if(timeout > 0L) {
            this.timeout = timeout;
        }

    }

    public boolean isTriggerNoEvent() {
        return this.triggerNoEvent;
    }

    public void setTriggerNoEvent(boolean triggerNoEvent) {
        this.triggerNoEvent = triggerNoEvent;
    }

    public void start() {
        if(this.started.compareAndSet(false, true)) {
            this.events.clear();
            this.eventDispatcher.start();
            if(this.name != null) {
                this.dispatcher = new Thread(this.eventDispatcher, this.name);
            } else {
                this.dispatcher = new Thread(this.eventDispatcher);
            }

            this.dispatcher.setDaemon(true);
            this.dispatcher.start();
        }

    }

    public void stop() {
        this.stop(false);
    }

    public void stop(boolean gracefully) {
        if(this.started.compareAndSet(true, false) && this.dispatcher != null) {
            this.dispatcher.interrupt();
            this.dispatcher = null;
            this.eventDispatcher.stop(gracefully);
            this.events.clear();
        }

    }

    public boolean isStarted() {
        return this.started.get();
    }

    public boolean addListener(EventListener<E> listener) {
        return listener != null?this.listeners.addIfAbsent(listener):false;
    }

    public boolean removeListener(EventListener<E> listener) {
        return listener != null?this.listeners.remove(listener):false;
    }

    public List<EventListener<E>> getListeners() {
        return this.listeners;
    }

    public boolean add(E event) {
        return this.add(event, (EventListener)null);
    }

    public boolean add(E event, long timeout, TimeUnit unit) {
        return this.add(event, (EventListener)null, timeout, unit);
    }

    public boolean add(E event, EventListener<E> owner) {
        if(event == null) {
            return false;
        } else {
            try {
                this.events.put(new EventManager.Ownership(event, owner));
                return true;
            } catch (InterruptedException var4) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
    }

    public boolean add(E event, EventListener<E> owner, long timeout, TimeUnit unit) {
        if(event == null) {
            return false;
        } else {
            try {
                return this.events.offer(new EventManager.Ownership(event, owner), timeout, unit);
            } catch (InterruptedException var7) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
    }

    public void inform(E event) throws Exception {
        if(event != null) {
            Iterator var2 = this.listeners.iterator();

            while(var2.hasNext()) {
                EventListener listener = (EventListener)var2.next();

                try {
                    listener.onEvent(event);
                } catch (EventException var5) {
                    throw (Exception)var5.getCause();
                }
            }

        }
    }

    protected void onIdle() {
    }

    protected void publish(List<EventManager<E>.Ownership> events) {
        if(events != null && !events.isEmpty()) {
            this.publish((EventManager.Ownership)events.get(events.size() - 1));
        }
    }

    protected void publish(EventManager<E>.Ownership event) {
        if(event != null && event.owner != null) {
            try {
                event.owner.onEvent(event.event);
            } catch (Throwable var7) {
                ;
            }
        } else {
            Object e = event == null?null:event.event;
            Iterator var3 = this.listeners.iterator();

            while(var3.hasNext()) {
                EventListener listener = (EventListener)var3.next();

                try {
                    listener.onEvent(e);
                } catch (Throwable var6) {
                    ;
                }
            }
        }

    }

    protected class EventDispatcher implements Runnable {
        private AtomicBoolean started = new AtomicBoolean();
        private AtomicBoolean gracefully = new AtomicBoolean(false);
        private CountDownLatch latch;

        protected EventDispatcher() {
        }

        public void start() {
            if(this.started.compareAndSet(false, true)) {
                this.latch = new CountDownLatch(1);
                this.gracefully.set(false);
            }

        }

        public void stop(boolean gracefully) {
            if(this.started.compareAndSet(true, false)) {
                this.gracefully.set(gracefully);
                if(gracefully) {
                    try {
                        this.latch.await();
                    } catch (InterruptedException var3) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

        }

        protected boolean isAlive() {
            return this.started.get() && !Thread.currentThread().isInterrupted();
        }

        public void run() {
            long lastTime = SystemClock.getInstance().now();

            while(true) {
                try {
                    EventManager.Ownership event = null;
                    if(this.isAlive()) {
                        event = (EventManager.Ownership)EventManager.this.events.poll(EventManager.this.timeout, TimeUnit.MILLISECONDS);
                    }

                    if(!this.isAlive()) {
                        if(!this.gracefully.get()) {
                            break;
                        }

                        if(event == null) {
                            if(!Thread.currentThread().isInterrupted()) {
                                event = (EventManager.Ownership)EventManager.this.events.poll(50L, TimeUnit.MILLISECONDS);
                            } else {
                                event = (EventManager.Ownership)EventManager.this.events.poll();
                            }
                        }

                        if(event == null) {
                            break;
                        }
                    }

                    if(event == null) {
                        if(EventManager.this.triggerNoEvent) {
                            EventManager.this.publish((EventManager.Ownership)null);
                        }

                        if(EventManager.this.idleTime > 0L) {
                            long now = SystemClock.getInstance().now();
                            if(now - lastTime > EventManager.this.idleTime) {
                                lastTime = now;
                                EventManager.this.onIdle();
                            }
                        }
                    } else {
                        if(EventManager.this.idleTime > 0L) {
                            lastTime = SystemClock.getInstance().now();
                        }

                        if(EventManager.this.interval <= 0L) {
                            EventManager.this.publish(event);
                        } else {
                            ArrayList e = new ArrayList();
                            e.add(event);

                            while(true) {
                                if(EventManager.this.events.isEmpty()) {
                                    EventManager.this.publish((List)e);
                                    break;
                                }

                                event = (EventManager.Ownership)EventManager.this.events.poll();
                                if(event != null) {
                                    e.add(event);
                                }
                            }
                        }
                    }

                    if(EventManager.this.interval > 0L && this.isAlive()) {
                        Thread.sleep(EventManager.this.interval);
                    }
                } catch (InterruptedException var7) {
                    Thread.currentThread().interrupt();
                }
            }

            if(this.latch != null) {
                this.latch.countDown();
            }

        }
    }

    protected class Ownership {
        private E event;
        private EventListener<E> owner;

        public Ownership(E event) {
            this.event = event;
        }

        public Ownership(E event, EventListener<E> owner) {
            this.event = event;
            this.owner = owner;
        }

        public E getEvent() {
            return this.event;
        }

        public EventListener<E> getOwner() {
            return this.owner;
        }
    }
}
