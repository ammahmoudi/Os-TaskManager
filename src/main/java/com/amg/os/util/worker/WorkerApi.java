package com.amg.os.util.worker;

import com.amg.os.task.TaskContext;
import com.amg.os.util.network.connection.Connection;
import com.amg.os.util.storage.StorageRequest;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class WorkerApi {
    private final Connection connection;
    private int id=-1;
    private boolean isWorking;

    public WorkerApi(int WorkerPort) throws IOException {
        connection = new Connection(WorkerPort);
    }

    public TaskContext run(TaskContext taskContext) throws InterruptedException {
        connection.send(WorkerRequest.RUN);
        connection.sendObject(taskContext);
        try {
            return (TaskContext) awaitWorkerResponseObject();
        } catch (InterruptedException e) {
            connection.send(WorkerRequest.CANCEL);
            throw e;
        } catch (ExecutionException e) {
            return null;
        }
    }

    private String awaitWorkerResponseString() throws InterruptedException, ExecutionException {
        FutureTask<String> getWorkerResponseTask = new FutureTask<>(
                connection::receive
        );
        Executors.newFixedThreadPool(1).execute(getWorkerResponseTask);

        return getWorkerResponseTask.get();
    }
    private Object awaitWorkerResponseObject() throws InterruptedException, ExecutionException {
        FutureTask<Object> getWorkerResponseTask = new FutureTask<>(
                connection::readObject
        );
        Executors.newFixedThreadPool(1).execute(getWorkerResponseTask);

        return getWorkerResponseTask.get();
    }
    public void release() {
        /* TODO */
    }

    public void interrupt() {
        connection.close();
    }

    public boolean isWorking() {
        return isWorking;
    }

    public void setWorking(boolean working) {
        isWorking = working;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
