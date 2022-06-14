package com.amg.os.task;

import java.util.concurrent.Semaphore;

import com.amg.os.util.storage.StorageApi;
import util.time.Time;

public class Task extends  Thread
{
    private final StorageApi storage;
    private TaskContext context;
    private final Semaphore lock;

    public Task(StorageApi storage, TaskContext context) {
        lock = new Semaphore(0);
        this.storage = storage;
        this.context = context;
    }
    public void run()  {
        try {
            while (!isInterrupted() && !isFinished()) {
                doSleep();
                if (isInterrupted()) break;
                doRead();
            }
        } catch (InterruptedException e) {
            System.out.println("interrupted in task");
            context.setInUse(false);
            lock.release();
            interrupt();
        }
        this.interrupt();
    }

    private void doSleep() throws InterruptedException {
        int startTime = Time.getNowMillis();
        int timeToSleep = context.getNextSleep();
        System.out.println("sleep for " + timeToSleep);

        try {
            Thread.sleep(timeToSleep);
            context.setLastSleepIndex(context.getLastSleepIndex()+1);
        } catch (InterruptedException e) {
            int stopTime = Time.getNowMillis();
            context.setLastSleepDuration(stopTime - startTime);
            throw e;
        }
    }

    private void doRead() throws InterruptedException {
        Integer readIndex = context.getNextReadIndex();

        System.out.println("reading index " + readIndex);
        try {
            context.setCurrentSum(context.getCurrentSum() + storage.obtain(context.indices[readIndex], context.getId()));
            context.setLastReadAttempt(context.getLastIndicesIndex()+1);

        } catch (InterruptedException e) {
            context.setLastReadAttempt(readIndex);
            throw e;
        }

    }

    private boolean isFinished() {
        if (context.lastIndicesIndex == context.indices.length - 1) {
            context.setDone(true);
            return true;
        }
        return false;
    }

    public synchronized TaskContext interruptTask() {
       interrupt();
        try {
            lock.acquire();
            context.setInUse(true);
        } catch (InterruptedException e) {
        }
        return context;
    }
}
