package com.amg.os.master;

import com.amg.os.DeadLockMode;
import com.amg.os.SchedulingMode;
import com.amg.os.task.TaskBuilder;
import com.amg.os.task.TaskContext;
import com.amg.os.util.process.Program;
import com.amg.os.util.storage.StorageApi;
import com.amg.os.util.storage.StorageProcess;

import com.amg.os.util.worker.WorkerApi;
import com.amg.os.util.worker.WorkerProcess;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.StringJoiner;

import static java.lang.Thread.sleep;

public class Master {
    int workers_n;
    int jobs_n;
    SchedulingMode schedulingMode;
    DeadLockMode deadLockMode;
    public static int storagePort;
    public static int masterPort;
    int[] storageData;
    StorageApi storageApi;
    public static LinkedList<TaskContext> taskContexts;

    public static WorkerApi[] workerApis;
    public boolean jobsDone = false;

    public Master() {
    }

    public Master(int workers_n, int jobs_n, SchedulingMode schedulingMode, DeadLockMode deadLockMode, int[] storageData) {
        this.workers_n = workers_n;
        this.jobs_n = jobs_n;
        this.schedulingMode = schedulingMode;
        this.deadLockMode = deadLockMode;
        this.storageData = storageData;
        workerApis = new WorkerApi[workers_n];
        taskContexts = new LinkedList<>();
    }

    public TaskContext addJob(String jobString) {
        TaskContext taskContext = new TaskBuilder(jobString).getContext();
        taskContexts.add(taskContext);
        return taskContext;

    }

    public void initialize() throws IOException {

        StringJoiner dataString = new StringJoiner(" ");
        for (int i : storageData
        ) {
            dataString.add(String.valueOf(i));
        }

        Program storageProgram = new Program(StorageProcess.class, true).addArgument(String.valueOf(masterPort)).addArgument(dataString.toString());
        storageProgram.run();
        try {
            sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < workers_n; i++) {
            Program program = new Program(WorkerProcess.class, true).addArgument(String.valueOf(masterPort)).addArgument(String.valueOf(i));
            try {
                program.run();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void start() {
        Runnable runnable = () -> {
            while (!jobsDone) {
                switch (schedulingMode) {
                    case FCFS -> {
                     //   System.out.println("start handling jobs in FCFS");
                        for (TaskContext taskContext : taskContexts) {
                            if (taskContext.isInUse() || taskContext.isDone()) continue;
                            WorkerApi worker = findFreeWorker();

                            if (worker == null) continue;
                            System.out.println("worker " + worker.getId() + " gets job " + taskContext.getId());
                            worker.setWorking(true);
                            taskContext.setInUse(true);
                            new Thread(() -> {
                                try {
                                    taskContext.setCurrentSum(Integer.parseInt(worker.run(taskContext)));
                                    taskContext.setDone(true);
                                    worker.setWorking(false);
                                    taskContext.setInUse(false);
                                    System.out.println("job "+taskContext.getId()+" has been done by worker "+worker.getId()+" resulting "+taskContext.getCurrentSum());
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }).start();

                        }

                    }
                    case SJF -> {
                        taskContexts.sort(Comparator.comparing(TaskContext::getTotalEstimatedTime));
                        for (TaskContext taskContext : taskContexts) {
                            if (taskContext.isInUse() || taskContext.isDone()) continue;
                            WorkerApi worker = findFreeWorker();

                            if (worker == null) continue;
                            System.out.println("worker " + worker.getId() + " gets job " + taskContext.getId());
                            worker.setWorking(true);
                            taskContext.setInUse(true);
                            new Thread(() -> {
                                try {
                                    taskContext.setCurrentSum(Integer.parseInt(worker.run(taskContext)));
                                    taskContext.setDone(true);
                                    worker.setWorking(false);
                                    taskContext.setInUse(false);
                                    System.out.println("job "+taskContext.getId()+" has been done by worker "+worker.getId()+" resulting "+taskContext.getCurrentSum());
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }).start();

                        }

                    }
                }
            }


        };
        new Thread(runnable).start();

    }

    public WorkerApi findFreeWorker() {
        for (WorkerApi worker : workerApis) {
            if (worker != null && !worker.isWorking()) return worker;
        }
        return null;
    }
}

