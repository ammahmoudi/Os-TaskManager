package com.amg.os.master;

import com.amg.os.util.network.connection.Connection;
import com.amg.os.util.storage.StorageRequest;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;


public class MasterApi {
    private final Connection connection;

    public MasterApi(int masterPort) throws IOException {
        connection = new Connection(masterPort);
    }

    public int introduce(int port, int id) throws InterruptedException {
        connection.send(MasterRequest.INTRODUCE);
        connection.send(port);
        connection.send(id);
        try {
            return awaitMasterResponse();
        } catch (InterruptedException e) {
            connection.send(MasterRequest.CANCEL);
            throw e;
        } catch (ExecutionException e) {
            return -1;
        }
    }
    public int introduceStorage(int port) throws InterruptedException {
        connection.send(MasterRequest.STORAGE);
        connection.send(port);
        try {
            return awaitMasterResponse();
        } catch (InterruptedException e) {
            connection.send(MasterRequest.CANCEL);
            throw e;
        } catch (ExecutionException e) {
            return -1;
        }
    }

    private Integer awaitMasterResponse() throws InterruptedException, ExecutionException {
        FutureTask<Integer> getMasterResponseTask = new FutureTask<>(
            () -> Integer.parseInt(connection.receive())
        );
        Executors.newFixedThreadPool(1).execute(getMasterResponseTask);
        
        return getMasterResponseTask.get();
    }

    public void release() {
        /* TODO */
    }

    public void interrupt() {
        connection.close();
    }
}
