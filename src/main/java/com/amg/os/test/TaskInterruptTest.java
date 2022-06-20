package com.amg.os.test;

import java.io.IOException;

import com.amg.os.task.TaskContext;
import com.amg.os.task.TaskRunner;
import com.amg.os.util.storage.Storage;
import com.amg.os.util.storage.StorageApi;
import com.amg.os.util.storage.StorageServer;



public class TaskInterruptTest {
    public static void main(String[] args) throws InterruptedException, IOException {
//        int storagePort = 9095;
//        new StorageServer(
//            new Storage(
//                    new int[]{1, 2, 3, 4}
//            )
//        ).listen(storagePort);
//
//        new StorageApi(storagePort).obtain(0, 1);
//
//        TaskRunner taskRunner = new TaskRunner(storagePort,w);
//        taskRunner.runTask(new TaskContext());
//        Thread.sleep(200);
//        var task = taskRunner.interrupt();
//        System.out.println("interrupt done");
//        System.out.println("task result was: " + task.getResult());
//    }
}
}
