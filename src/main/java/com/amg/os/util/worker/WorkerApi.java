package com.amg.os.util.worker;

import com.amg.os.request.Packet;
import com.amg.os.request.PacketType;
import com.amg.os.task.Task;
import com.amg.os.task.TaskContext;
import com.amg.os.util.network.connection.Connection;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import static java.lang.Thread.sleep;

public class WorkerApi {
    private final Connection connection;
    private int id=-1;
    private boolean isWorking;

    public WorkerApi(int WorkerPort) throws IOException {
        connection = new Connection(WorkerPort);
    }

    public Packet run(TaskContext taskContext) throws InterruptedException {
        Packet packet=new Packet(-1, PacketType.RUN_WORKER,false,taskContext);
       connection.sendObject(packet);
        try {
            return (Packet) awaitWorkerResponseObject();
        } catch (InterruptedException e) {
            connection.sendObject(new Packet(-1, PacketType.CANCEL,false, ""));
            throw e;
        } catch (ExecutionException e) {
            return null;
        }
    }
    public Packet run(TaskContext taskContext, int timeOut) throws InterruptedException {
        Packet packet=new Packet(-1, PacketType.RUN_WORKER,false,taskContext);
        connection.sendObject(packet);
        new Thread(() -> {
            try {
                sleep(timeOut);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                interruptTask(taskContext);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        try {
            return (Packet) awaitWorkerResponseObject();
        } catch (InterruptedException e) {

            connection.sendObject(new Packet(-1, PacketType.CANCEL,false, ""));
            throw e;
        } catch (ExecutionException e) {
            return null;
        }
    }
public Packet interruptTask(TaskContext taskContext) throws InterruptedException {
        Packet packet=new Packet(-1,PacketType.INTERRUPT_WORKER,false,String.valueOf(taskContext.getId()));
        connection.sendObject(packet);
        //wait for answer
//    try {
//        return (Packet) awaitWorkerResponseObject();
//    } catch (InterruptedException e) {
//        connection.sendObject(new Packet(-1, PacketType.CANCEL,false, ""));
//        throw e;
//    } catch (ExecutionException e) {
//        return null;
//    }
    return null;
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
    private Object awaitWorkerResponseObject(int timeOut) throws InterruptedException, ExecutionException {
        FutureTask<Object> getWorkerResponseTask = new FutureTask<>(
                connection::readObject
        );
        Executors.newFixedThreadPool(1).execute(getWorkerResponseTask);

        return getWorkerResponseTask.get();
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
