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
    private boolean isWorking;

    public WorkerApi(int WorkerPort) throws IOException {
        connection = new Connection(WorkerPort);
    }

//    public int run(int index, int id) throws InterruptedException {
//        connection.send(WorkerRequest.RUN);
//        connection.send(index);
//        connection.send(id);
//        try {
//            return awaitWorkerResponse();
//        } catch (InterruptedException e) {
//            connection.send(WorkerRequest.CANCEL);
//            throw e;
//        } catch (ExecutionException e) {
//            return -1;
//        }
//    }
    public String run(TaskContext taskContext) throws InterruptedException {
        connection.send(WorkerRequest.RUN);
        connection.sendObject(taskContext);
       // connection.send();
        try {
            return awaitWorkerResponse();
        } catch (InterruptedException e) {
            connection.send(WorkerRequest.CANCEL);
            throw e;
        } catch (ExecutionException e) {
            return null;
        }
    }

    private String awaitWorkerResponse() throws InterruptedException, ExecutionException {
        FutureTask<String> getWorkerResponseTask = new FutureTask<>(
                connection::receive
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
}
