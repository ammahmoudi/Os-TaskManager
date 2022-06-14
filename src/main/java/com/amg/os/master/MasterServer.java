package com.amg.os.master;

import com.amg.os.util.network.connection.Connection;
import com.amg.os.util.network.server.AbstractServer;
import com.amg.os.util.worker.WorkerApi;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MasterServer extends AbstractServer {
    private final Master master;
    private final Map<Connection, Thread> introductionThreads;

    public MasterServer() {
        master = new Master();
        introductionThreads = new HashMap<>();
    }

    public MasterServer(Master master) {
        this.master = master;
        introductionThreads = new HashMap<>();
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
        String string = connection.receive();
        if(string==null)return;
        MasterRequest request = MasterRequest.valueOf(string);
        switch (request) {
            case INTRODUCE:
                handleIntroduce(connection);
                break;
            case STORAGE:
                handleStorageIntroduction(connection);
                break;
            case CANCEL:
                introductionThreads.get(connection).interrupt();
                break;
        }
    }

    private void handleIntroduce(Connection connection) {
        int port = Integer.parseInt(connection.receive());
        int id = Integer.parseInt(connection.receive());
        Thread thread = new Thread(() -> {
            System.out.println("worker " + id + " on port: " + port);
            try {
                Master.workerApis[id] = new WorkerApi(port);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            connection.send(Master.storagePort);
            if(id== master.jobs_n-1)master.start();
        });
        introductionThreads.put(connection, thread);
        thread.start();
    }

    private void handleStorageIntroduction(Connection connection) {
        int port = Integer.parseInt(connection.receive());
        Thread thread = new Thread(() -> {
            System.out.println("storage " + " on port: " + port);
            Master.storagePort=port;
            connection.send("ok");
        });
        introductionThreads.put(connection, thread);
        thread.start();
    }
}
