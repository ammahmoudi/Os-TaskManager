package com.amg.os.task;

import java.io.IOException;
import java.util.concurrent.*;

import com.amg.os.util.storage.StorageApi;


public class TaskRunner {

    private final int storagePort;
    private Task task;
    
    public TaskRunner(int storagePort) {
        this.storagePort = storagePort;
        task = null;
    }

    public TaskContext runTask(TaskContext context) {
        try {
            task = new Task(newStorageApi(), context);
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

    public TaskContext interrupt() {
        TaskContext context =  task.interruptTask();
        task = null;
        return context;
    }
}
