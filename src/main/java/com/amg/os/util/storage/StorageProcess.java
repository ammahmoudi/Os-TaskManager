package com.amg.os.util.storage;

import com.amg.os.util.worker.WorkerServer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StorageProcess {
    public  static int id;
    public static int port;
    public  static int[] memoryValues;
    public static  Storage storage;

    public static StorageServer storageServer;

    public static void main(String[] args) {
        id= Integer.parseInt(args[0]);
        port=Integer.parseInt(args[1]);
        memoryValues= Arrays.stream(args[2].split(" ")).mapToInt(Integer::parseInt).toArray();
        storage=new Storage(memoryValues);
        storageServer=new StorageServer(storage);
        storageServer.listen(port);

    }
}
