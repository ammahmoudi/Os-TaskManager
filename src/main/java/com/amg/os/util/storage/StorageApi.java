package com.amg.os.util.storage;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import com.amg.os.util.network.connection.Connection;


public class StorageApi {
    private final Connection connection;

    public StorageApi(int storagePort) throws IOException {
        connection = new Connection(storagePort);
    }

    public int obtain(int index, int id) throws InterruptedException {
        connection.send(StorageRequest.OBTAIN);
        connection.send(index);
        connection.send(id);
        try {
            return awaitStorageResponse();
        } catch (InterruptedException e) {
            connection.send(StorageRequest.CANCEL);
            throw e;
        } catch (ExecutionException e) {
            return -1;
        }
    }

    private Integer awaitStorageResponse() throws InterruptedException, ExecutionException {
        FutureTask<Integer> getStorageResponseTask = new FutureTask<>(
            () -> Integer.parseInt(connection.receive())
        );
        Executors.newFixedThreadPool(1).execute(getStorageResponseTask);
        
        return getStorageResponseTask.get();
    }

    public void release() {
        /* TODO */
    }

    public void interrupt() {
        connection.close();
    }
}
