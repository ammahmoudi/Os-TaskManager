package com.amg.os.util.storage;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import com.amg.os.master.Master;
import com.amg.os.request.Packet;
import com.amg.os.request.PacketType;
import com.amg.os.util.network.connection.Connection;


public class StorageApi {
    private final Connection connection;

    public StorageApi(int storagePort) throws IOException {
        connection = new Connection(storagePort);
    }

    public Packet obtain(int index, int id) throws InterruptedException {
        Packet packet=new Packet(id, PacketType.OBTAIN_MEMORY,false,String.valueOf(index));
     connection.sendObject(packet);
        try {
            return awaitStorageResponse();
        } catch (InterruptedException e) {
            connection.sendObject(new Packet(id, PacketType.CANCEL,false, ""));
            throw e;
        } catch (ExecutionException e) {
            return new Packet(id,null,true,"");
        }
    }

    private Packet awaitStorageResponse() throws InterruptedException, ExecutionException {
        FutureTask<Packet> getStorageResponseTask = new FutureTask<>(
                connection::readObject
        );
        Executors.newFixedThreadPool(1).execute(getStorageResponseTask);
        
        return getStorageResponseTask.get();
    }

    public Packet release(int index, int id) throws InterruptedException {
        Packet packet=new Packet(id, PacketType.RELEASE_MEMORY,false,String.valueOf(index));
        connection.sendObject(packet);
        try {
            return awaitStorageResponse();
        } catch (InterruptedException e) {
            connection.send(StorageRequest.CANCEL);
            throw e;
        } catch (ExecutionException e) {
            return new Packet(id,null,true,"");
        }
    }

    public void interrupt() {
        connection.close();
    }
}
