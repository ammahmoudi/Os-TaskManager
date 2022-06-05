package com.amg.os.util.worker;

import com.amg.os.util.network.connection.Connection;
import com.amg.os.util.network.server.AbstractServer;

import java.util.HashMap;
import java.util.Map;

public class WorkerServer extends AbstractServer {
    private final WorkerProcess workerProcess;
    private final Map<Connection, Thread> obtainThreads;

    public WorkerServer() {
        workerProcess = new WorkerProcess();
        obtainThreads = new HashMap<>();
    }

    public WorkerServer(WorkerProcess workerProcess) {
        this.workerProcess = workerProcess;
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
        WorkerRequest request = WorkerRequest.valueOf(connection.receive());

        switch (request) {
            case RUN:

                break;


            case CANCEL:

                break;


        }
    }

    private void handleObtain(Connection connection) {
//        int index = Integer.parseInt(connection.receive());
//        int id = Integer.parseInt(connection.receive());
//        Thread thread = new Thread(() -> {
//            try {
//                int value = storage.obtain(index, id);
//                connection.send(value);
//            } catch (InterruptedException e) { }
//        });
//        obtainThreads.put(connection, thread);
//        thread.start();
    }
}
