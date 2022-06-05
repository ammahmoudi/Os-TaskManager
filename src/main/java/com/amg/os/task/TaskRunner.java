package com.amg.os.task;

import java.io.IOException;

import com.amg.os.util.storage.StorageApi;


public class TaskRunner {

    private final int storagePort;
    private Task task;
    
    public TaskRunner(int storagePort) {
        this.storagePort = storagePort;
        task = null;
    }

    public void runTask(TaskContext context) {
        try {
            task = new Task(newStorageApi(), context);
            task.start();
        } catch (Exception e) {};
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
