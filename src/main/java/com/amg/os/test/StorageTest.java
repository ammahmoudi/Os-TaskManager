package com.amg.os.test;

import com.amg.os.util.storage.StorageApi;
import com.amg.os.util.storage.StorageServer;

import java.io.IOException;



public class StorageTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        new StorageServer().listen(9087);

        StorageApi storage = new StorageApi(9087);

        Thread.sleep(100);
        System.out.println("the result was: " + storage.obtain(1, 1));
    }
}
