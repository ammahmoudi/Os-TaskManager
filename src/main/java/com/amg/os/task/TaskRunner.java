package com.amg.os.task;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import com.amg.os.util.storage.StorageApi;
import com.amg.os.util.worker.Worker;


public class TaskRunner {

    private final int storagePort;
    private Task task;
    private Worker worker;
    private Semaphore lock;
    public TaskRunner(int storagePort, Worker worker) {
        this.storagePort = storagePort;
        this.worker=worker;
        task = null;
        lock=new Semaphore(0);
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public TaskContext runTask(TaskContext context) {
        try {
            task = new Task(newStorageApi(), context);
            lock.release();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        task.start();
        try {
            //System.out.println("a:"+context.getResult());
            task.join();
           // System.out.println("b:"+context.getResult());

        } catch (InterruptedException e) {
           // System.out.println("finnished in task runner");
            //System.out.println("c:"+context.getResult());

        }

        return context;
    }

    private StorageApi newStorageApi() throws IOException {
        return new StorageApi(storagePort);
    }

    public TaskContext interrupt() throws InterruptedException {
        lock.acquire();
        TaskContext context =  task.interruptTask();
        task = null;
        return context;
    }
}
