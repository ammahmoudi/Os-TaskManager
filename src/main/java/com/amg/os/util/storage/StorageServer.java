package com.amg.os.util.storage;

import java.util.HashMap;
import java.util.Map;

import com.amg.os.util.network.connection.Connection;
import com.amg.os.util.network.server.AbstractServer;


public class StorageServer extends AbstractServer {
    private final Storage storage;
    private final Map<Connection, Thread> obtainThreads;

    public StorageServer() {
        storage = new Storage();
        obtainThreads = new HashMap<>();
    }

    public StorageServer(Storage storage) {
        this.storage = storage;
        obtainThreads = new HashMap<>();
    }

    @Override
    public void acceptConnection(Connection connection) {
        new Thread(() -> {
            while (true) {
                serverConnection(connection);
            }
        }).start();
    }

    private void serverConnection(Connection connection) {
        StorageRequest request = StorageRequest.valueOf(connection.receive());

        switch (request) {
            case OBTAIN:
                handleObtain(connection);
                break;

            case RELEASE:
                // TODO
                handleRelease(connection);
                // storage.release(index, id);
                break;

            case CANCEL:
                obtainThreads.get(connection).interrupt();
                break;

            case WRITE:
                // TODO
                break;
        }
    }

    private void handleObtain(Connection connection) {
        int index = Integer.parseInt(connection.receive());
        int id = Integer.parseInt(connection.receive());
        Thread thread = new Thread(() -> {
            try {
                int value = storage.obtain(index, id);
                connection.send(value);
            } catch (InterruptedException e) {
            }
        });
        obtainThreads.put(connection, thread);
        thread.start();
    }

    private void handleRelease(Connection connection) {
        int index = Integer.parseInt(connection.receive());
        int id = Integer.parseInt(connection.receive());
        Thread thread = new Thread(() -> {
            storage.release(index, id);
            connection.send(1);
        });
        //obtainThreads.remove(connection)
        thread.start();
    }
}
