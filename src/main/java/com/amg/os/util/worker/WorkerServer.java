package com.amg.os.util.worker;

import com.amg.os.request.Packet;
import com.amg.os.request.PacketType;
import com.amg.os.task.Task;
import com.amg.os.task.TaskContext;
import com.amg.os.task.TaskRunner;
import com.amg.os.util.network.connection.Connection;
import com.amg.os.util.network.server.AbstractServer;
import com.amg.os.util.storage.StorageApi;


import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class WorkerServer extends AbstractServer {
    private final Worker worker;
    private final Map<Connection, Thread> obtainThreads;

    public WorkerServer() {
        worker = new Worker();
        obtainThreads = new HashMap<>();
    }

    public WorkerServer(Worker worker) {
        this.worker = worker;
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
        Packet packet = connection.readObject();
        if (packet != null)
            switch (packet.getType()) {
                case RUN_WORKER:
                    new Thread(() -> {
                        handleRun(connection, packet);
                    }).start();


                    break;

                case INTERRUPT_WORKER:
                    if (worker.taskRunner != null) worker.taskRunner.interrupt();
                    //   connection.sendObject(new Packet(worker.getId(), PacketType.INTERRUPT_WORKER, true, "int"));
                    break;
                case CANCEL:

                    break;


            }
    }

    private void handleRun(Connection connection, Packet packet) {
        TaskContext taskContext = (TaskContext) packet.getObject();
        System.out.println("Running job with indices  " + Arrays.toString(taskContext.indices) + " and sleeps " + Arrays.toString(taskContext.sleeps));


            worker.setCurrentContext(taskContext);
            worker.taskRunner = new TaskRunner(worker.getStoragePort());
            worker.taskRunner.runTask(worker.getCurrentContext());
            System.out.println(worker.getCurrentContext());
            connection.sendObject(new Packet(worker.getId(), PacketType.RUN_WORKER, true, worker.getCurrentContext()));



    }

}
