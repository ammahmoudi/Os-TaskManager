package com.amg.os.master;

import com.amg.os.DeadLockMode;
import com.amg.os.SchedulingMode;
import com.amg.os.util.storage.StorageApi;
import com.amg.os.util.worker.WorkerApi;

public class Master {
    int workers_n;
    int jobs_n;
    SchedulingMode schedulingMode;
    DeadLockMode deadLockMode;
    int storagePort;
    int[] StorageData;
    StorageApi storageApi;
    WorkerApi[] workerApis;

}
