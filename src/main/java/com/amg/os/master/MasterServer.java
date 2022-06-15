package com.amg.os.master;

import com.amg.os.request.Packet;
import com.amg.os.request.PacketType;
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
        Packet packet = connection.readObject();
        if(packet==null)return;

        switch (packet.getType()) {
            case INTRODUCE_WORKER:
                handleWorkerIntroduction(connection,packet);
                break;
            case INTRODUCE_STORAGE:
                handleStorageIntroduction(connection,packet);
                break;
            case CANCEL:
                introductionThreads.get(connection).interrupt();
                break;
        }
    }

    private void handleWorkerIntroduction(Connection connection, Packet packet) {
        int port = Integer.parseInt(packet.getData());
        int id = packet.getSenderId();
        Thread thread = new Thread(() -> {
            System.out.println("worker " + id + " started on port: " + port);
            try {
                Master.workerApis[id] = new WorkerApi(port);
                Master.workerApis[id].setId(id);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            connection.sendObject(new Packet(-1, PacketType.INTRODUCE_WORKER,true,String.valueOf(Master.storagePort)));
            if(id== master.workers_n-1)master.start();
        });
        introductionThreads.put(connection, thread);
        thread.start();
    }

    private void handleStorageIntroduction(Connection connection,Packet packet) {
        int port = Integer.parseInt(packet.getData());
        Thread thread = new Thread(() -> {
            System.out.println("storage " + " on port: " + port);
            Master.storagePort=port;
            connection.sendObject(new Packet(-1, PacketType.INTRODUCE_STORAGE,true,"Introduced"));

        });
        introductionThreads.put(connection, thread);
        thread.start();
    }
}
