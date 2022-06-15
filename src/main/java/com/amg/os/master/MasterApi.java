package com.amg.os.master;

import com.amg.os.request.Packet;
import com.amg.os.request.PacketType;
import com.amg.os.util.network.connection.Connection;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;


public class MasterApi {
    private final Connection connection;

    public MasterApi(int masterPort) throws IOException {
        connection = new Connection(masterPort);
    }

    public Packet introduceWorker(int port, int id) throws InterruptedException {
        Packet packet = new Packet(id, PacketType.INTRODUCE_WORKER,false, String.valueOf(port));
        connection.sendObject(packet);
        try {
            return awaitMasterResponse();
        } catch (InterruptedException e) {
            connection.sendObject(new Packet(id, PacketType.CANCEL,false, ""));
            throw e;
        } catch (ExecutionException e) {
            return new Packet(-2,null,true,"");
        }
    }

    public Packet introduceStorage(int port) throws InterruptedException {
        Packet packet = new Packet(-2, PacketType.INTRODUCE_STORAGE,false, String.valueOf(port));
        connection.sendObject(packet);
        try {
            return awaitMasterResponse();
        } catch (InterruptedException e) {
            connection.sendObject(new Packet(-2, PacketType.CANCEL,false, ""));
            throw e;
        } catch (ExecutionException e) {
            return new Packet(-2,null,true,"");
        }
    }

    private Packet awaitMasterResponse() throws InterruptedException, ExecutionException {
        FutureTask<Packet> getMasterResponseTask = new FutureTask<>(
                connection::readObject
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
