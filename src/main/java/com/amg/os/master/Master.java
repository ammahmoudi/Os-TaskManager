package com.amg.os.master;

import com.amg.os.DeadLockMode;
import com.amg.os.SchedulingMode;
import com.amg.os.request.Packet;
import com.amg.os.task.TaskBuilder;
import com.amg.os.task.TaskContext;
import com.amg.os.util.process.Program;
import com.amg.os.util.serialize.Deserializer;
import com.amg.os.util.storage.StorageApi;
import com.amg.os.util.storage.StorageProcess;

import com.amg.os.util.worker.WorkerApi;
import com.amg.os.util.worker.WorkerProcess;
import javafx.application.Platform;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.StringJoiner;
import java.util.concurrent.*;

import static java.lang.Thread.sleep;

public class Master {
    int workers_n;
    int jobs_n;
    SchedulingMode schedulingMode;
    DeadLockMode deadLockMode;
    public static int storagePort = -1;
    public static int masterPort;
    int[] storageData;
    StorageApi storageApi;
    public static LinkedList<TaskContext> taskContexts;

    public static WorkerApi[] workerApis;
    public boolean jobsDone = false;
    public int timeQuantum;
    public static final Semaphore lock = new Semaphore(0);

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

    public void initialize() throws IOException, InterruptedException, ExecutionException {
        startStorage();
        lock.acquire();
        startWorkers();

    }

    public void startWorkers() {
        while (true) {
            if (getStoragePort() != -1) break;
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

    public void startStorage() {
        StringJoiner dataString = new StringJoiner(" ");
        for (int i : storageData
        ) {
            dataString.add(String.valueOf(i));
        }

        Program storageProgram = new Program(StorageProcess.class, true).addArgument(String.valueOf(masterPort)).addArgument(dataString.toString());
        try {
            storageProgram.run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void start() {
        Runnable runnable = () -> {
            while (!isFinished()) {
                switch (schedulingMode) {
                    case FCFS -> {
                        //   System.out.println("start handling jobs in FCFS");
                        for (int i = 0; i < taskContexts.size(); i++) {
                            final TaskContext[] taskContext = {taskContexts.get(i)};
                            if (taskContext[0].isInUse() || taskContext[0].isDone()) continue;
                            WorkerApi worker = findFreeWorker();

                            if (worker == null) continue;
                            System.out.println("Worker " + worker.getId() + " gets job " + taskContext[0].getId());
                            worker.setWorking(true);
                            taskContext[0].setInUse(true);
                            new Thread(() -> {
                                try {
                                    taskContext[0] = (TaskContext) worker.run(taskContext[0]).getObject();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                worker.setWorking(false);
                                System.out.println("job " + taskContext[0].getId() + " has been done by worker " + worker.getId() + " resulting " + taskContext[0].getCurrentSum());

                            }).start();


                        }

                    }
                    case SJF -> {
                        taskContexts.sort(Comparator.comparing(TaskContext::getTotalEstimatedTime));
                        for (int i = 0; i < taskContexts.size(); i++) {
                            final TaskContext[] taskContext = {taskContexts.get(i)};
                            if (taskContext[0].isInUse() || taskContext[0].isDone()) continue;
                            WorkerApi worker = findFreeWorker();

                            if (worker == null) continue;
                            System.out.println("Worker " + worker.getId() + " gets job " + taskContext[0].getId());
                            worker.setWorking(true);
                            taskContext[0].setInUse(true);
                            new Thread(() -> {
                                try {
                                    taskContext[0] = (TaskContext) worker.run(taskContext[0]).getObject();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                worker.setWorking(false);
                                System.out.println("Job " + taskContext[0].getId() + " has been done by worker " + worker.getId() + " resulting " + taskContext[0].getCurrentSum());

                            }).start();

                        }

                    }
                    case RR -> {

                        for (int i = 0; i < taskContexts.size(); i++) {
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }


                            final TaskContext[] taskContext = {taskContexts.get(i)};
                            //  System.out.println(taskContext[0].isInUse()+" "+taskContext[0].isDone());
                            if (taskContext[0].isInUse() || taskContext[0].isDone()) continue;

                            WorkerApi worker = findFreeWorker();

                            if (worker == null) continue;
                            System.out.println("Worker " + worker.getId() + " gets job " + taskContext[0].getId());
                            worker.setWorking(true);
                            taskContext[0].setInUse(true);
                            int finalI = i;
                            new Thread(() -> {
                                try {

                                    TaskContext taskContext1 = (TaskContext) worker.run(taskContext[0], timeQuantum).getObject();

                                    taskContext[0] = taskContext1;
                                    taskContext[0].setInUse(false);
                                    taskContexts.set(finalI, taskContext[0]);
                                    System.out.println(taskContext1);
                                    if (taskContext1.isDone())
                                        System.out.println("job " + taskContext[0].getId() + " has been done by worker " + worker.getId() + " resulting " + taskContext[0].getCurrentSum());


                                } catch (InterruptedException e) {
                                    System.out.println("exp");
                                    throw new RuntimeException(e);
                                }
                                worker.setWorking(false);

                            }).start();

                        }

                    }
                }
            }
            System.out.println("Program is finished!");


        };
        new Thread(runnable).start();

    }

    public WorkerApi findFreeWorker() {
        for (WorkerApi worker : workerApis) {
            if (worker != null && !worker.isWorking()) return worker;
        }
        return null;
    }
public boolean isFinished(){
        for(TaskContext taskContext:taskContexts){
            if(!taskContext.isDone())return false;
        }
        return true;
}
    public int getTimeQuantum() {
        return timeQuantum;
    }

    public void setTimeQuantum(int timeQuantum) {
        this.timeQuantum = timeQuantum;
    }

    public static int getStoragePort() {
        return storagePort;
    }

    public static void setStoragePort(int storagePort) {
        Master.storagePort = storagePort;
    }

}

