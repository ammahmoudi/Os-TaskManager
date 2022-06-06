package com.amg.os.util.worker;

import com.amg.os.task.Task;

public class Worker {
    int id;
    int port;
    Task currentTask;

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

    public Task getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }
}
