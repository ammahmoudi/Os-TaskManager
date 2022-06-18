package com.amg.os.util.storage;

import java.util.HashMap;
import java.util.Map;

import com.amg.os.request.Packet;
import com.amg.os.request.PacketType;
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
        Packet packet=connection.readObject();
        if(packet!=null)

        switch (packet.getType()) {
            case OBTAIN_MEMORY:
                handleObtain(connection,packet);
                break;

            case RELEASE_MEMORY:

                handleRelease(connection,packet);

                break;

            case CANCEL:
                obtainThreads.get(connection).interrupt();
                break;

            case WRITE_MEMORY:
                // TODO
                break;
        }
    }

    private void handleObtain(Connection connection,Packet packet) {
        int index = Integer.parseInt(packet.getData());
        int id = packet.getSenderId();
        Thread thread = new Thread(() -> {
            try {
                int value = storage.obtain(index, id);
                connection.sendObject(new Packet(-2, PacketType.OBTAIN_MEMORY,true,String.valueOf(value)));
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }
        });
        obtainThreads.put(connection, thread);
        thread.start();
    }

    private void handleRelease(Connection connection,Packet packet) {
        int index = Integer.parseInt(packet.getData());
        int id = packet.getSenderId();
        Thread thread = new Thread(() -> {
            storage.release(index, id);
            connection.sendObject(new Packet(-2, PacketType.RELEASE_MEMORY,true,String.valueOf(id)));
        });
        thread.start();
    }
}
