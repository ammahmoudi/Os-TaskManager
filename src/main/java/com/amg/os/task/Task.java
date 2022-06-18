package com.amg.os.task;

import java.util.concurrent.Semaphore;

import com.amg.os.util.storage.StorageApi;
import com.amg.os.util.time.Time;


import static java.lang.Math.abs;

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
         //   System.out.println("interrupted in task");
            context.setInUse(false);
         //   System.out.println(context.totalTimeSlept);
        //    System.out.println("lock released in catch");
            lock.release();
            interrupt();
        }
       // System.out.println("lock released after while");
        lock.release();
        this.interrupt();
    }

    private void doSleep() throws InterruptedException {
        int startTime = Time.getNowMillis();
      //  System.out.println("start: "+startTime);
        Integer timeToSleep = context.getLastSleepDuration();
        System.out.println("sleep for " + timeToSleep);

        try {
            Thread.sleep(timeToSleep);
            int stopTime = Time.getNowMillis();
            context.setLastSleepIndex(context.getLastSleepIndex()+1);
            context.setLastSleepDuration(context.sleeps[context.getLastSleepIndex()]);
            context.setTotalTimeSlept(context.getTotalTimeSlept()+(stopTime-startTime));
        } catch (InterruptedException e) {
            int stopTime = Time.getNowMillis();
          //  System.out.println("stop: "+stopTime);
            context.setLastSleepDuration(context.getLastSleepDuration()-(stopTime-startTime));
            context.setTotalTimeSlept(context.getTotalTimeSlept()+(stopTime-startTime));
         //   System.out.println("intrupt in sleep");
            throw e;
        }
    }

    private void doRead() throws InterruptedException {
        Integer readIndex = context.getNextReadIndex();

        System.out.println("reading index " + context.indices[readIndex]);
        try {
            context.setCurrentSum(context.getCurrentSum() + Integer.parseInt(storage.obtain(context.indices[readIndex], context.getId()).getData()));
            context.setLastReadAttemptIndex(readIndex);

        } catch (InterruptedException e) {
            context.setLastReadAttemptIndex(readIndex-1);
            throw e;
        }
        storage.release(context.indices[readIndex],context.getId());

    }

    private boolean isFinished() {
        if (context.lastIndicesIndex == context.indices.length - 1) {
            context.setDone(true);
            System.out.println("job is finished");
            return true;
        }
        return false;
    }

    public synchronized TaskContext interruptTask() {
       interrupt();
        try {
            lock.acquire();
       //     System.out.println("lock acquired in intrupttask");
            context.setInUse(true);
        } catch (InterruptedException e) {
        }
        return context;
    }
}
