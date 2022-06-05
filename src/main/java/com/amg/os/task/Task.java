package com.amg.os.task;

import java.util.concurrent.Semaphore;

import com.amg.os.util.storage.StorageApi;
import util.time.Time;

public class Task extends Thread {
    
    private final StorageApi storage;
    private TaskContext context;

    private final Semaphore lock;

    public Task(StorageApi storage, TaskContext context) {
        lock = new Semaphore(0);
        this.storage = storage;
        this.context = context;
    }

    @Override
    public void run() {
        try {
            while (!interrupted() && !isFinished()) {
                doSleep();
                if (interrupted()) break;
                doRead();
            }
        } catch (InterruptedException e) {
            lock.release();
            
            Thread.currentThread().interrupt();
        }
    }

    private void doSleep() throws InterruptedException {
        int startTime = Time.getNowMillis();
        int timeToSleep = context.getNextSleep();

        try {
            Thread.sleep(timeToSleep);
        } catch (InterruptedException e) {
            int stopTime = Time.getNowMillis();
            context.setLastSleepDuration(stopTime - startTime);
            throw e;
        }
    }

    private void doRead() throws InterruptedException {
        int readIndex = context.getNextReadIndex();
        try {
            storage.obtain(readIndex, context.getId());
        } catch (InterruptedException e) {
            context.setLastReadAttempt(readIndex);
            throw e;
        }
    }

    private boolean isFinished() {
        /* TODO */
        return false;
    }

    public synchronized TaskContext interruptTask() {
        this.interrupt();
        try {
            lock.acquire();
        } catch (InterruptedException e) {}
        
        return context;
    }
}
