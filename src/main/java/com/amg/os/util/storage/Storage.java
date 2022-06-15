package com.amg.os.util.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class Storage {
    private final Map<Integer, Integer> data;   // index, value
    private Map<Integer, Integer> lockOwner; // index, owner
private int port;
    private final Map<Integer, Semaphore> locks;

    public Storage() {
        data = new HashMap<>();
        lockOwner = new HashMap<>();
        locks = new HashMap<>();
    }

    public Storage(int[] data) {
        this();
        
        for (int i=0; i<data.length; ++i) {
            write(i, data[i]);
        }
    }

    public int obtain(int index, int id) throws InterruptedException {
        if (lockOwner.getOrDefault(index, -1) != id) {
            locks.get(index).acquire();
            System.out.println(id+ " acquired lock for index "+index);
            lockOwner.put(index, id);
        }
        return data.get(index);
    }

    public void write(int index, int value) {
        data.put(index, value);
        locks.computeIfAbsent(index, i -> new Semaphore(1));
    }

    public synchronized void release(int index, int id) {
        if (lockOwner.get(index) == id) {
            locks.get(index).release();
            System.out.println(id+ " released lock for index "+index);
        }

    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
