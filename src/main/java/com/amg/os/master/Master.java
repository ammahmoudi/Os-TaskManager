package com.amg.os.master;

import com.amg.os.DeadLockMode;
import com.amg.os.SchedulingMode;
import com.amg.os.util.process.Program;
import com.amg.os.util.storage.StorageApi;
import com.amg.os.util.storage.StorageProcess;

import com.amg.os.util.worker.WorkerApi;
import com.amg.os.util.worker.WorkerProcess;

import java.io.IOException;

import static java.lang.Thread.sleep;

public class Master {
    int workers_n;
    int jobs_n;
    SchedulingMode schedulingMode;
    DeadLockMode deadLockMode;
    int storagePort;
    int[] StorageData;
    StorageApi storageApi;
    WorkerApi[] workerApis;

    public Master(int workers_n, int jobs_n, SchedulingMode schedulingMode, DeadLockMode deadLockMode, int storagePort, int[] storageData) {
        this.workers_n = workers_n;
        this.jobs_n = jobs_n;
        this.schedulingMode = schedulingMode;
        this.deadLockMode = deadLockMode;
        this.storagePort = storagePort;
        StorageData = storageData;
    workerApis=new WorkerApi[workers_n];
    }
    public void initialize() throws IOException {
        Program storageProgram=new Program(StorageProcess.class,true).addArgument(String.valueOf(9089)).addArgument("1 2 3 4 5");
        storageProgram.run();
        System.out.println("storage started on port: "+storagePort);
        try {
            sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for(int i=0;i<workers_n;i++){
            Program program=new Program(WorkerProcess.class,true).addArgument(String.valueOf(9040+i)).addArgument(String.valueOf(i));
            try {
                program.run();
                System.out.println("worker "+i+" started on port: "+(9040+i));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
//            try {
//                workerApis[i]=new WorkerApi(9090+i);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
        }







    }
}
