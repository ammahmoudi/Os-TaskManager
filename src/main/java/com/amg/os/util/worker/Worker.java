package com.amg.os.util.worker;


import com.amg.os.task.Task;
import com.amg.os.task.TaskContext;
import com.amg.os.task.TaskRunner;
import com.amg.os.util.storage.StorageApi;

public class Worker {
    int id;
    int port;

    int storagePort;
    StorageApi storageApi;
TaskContext currentContext;
TaskRunner taskRunner;

    public StorageApi getStorageApi() {
        return storageApi;
    }

    public void setStorageApi(StorageApi storageApi) {
        this.storageApi = storageApi;
    }

    public Worker(int id, int port) {
        this.id = id;
        this.port = port;
    }

    public Worker() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getStoragePort() {
        return storagePort;
    }

    public void setStoragePort(int storagePort) {
        this.storagePort = storagePort;
    }

    public TaskContext getCurrentContext() {
        return currentContext;
    }

    public void setCurrentContext(TaskContext currentContext) {
        this.currentContext = currentContext;
    }
}
