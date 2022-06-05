package com.amg.os.util.worker;

public class WorkerProcess {
    public  static int id;
    public static int port;
    public static WorkerServer workerServer;

    public static void main(String[] args) {
        id= Integer.parseInt(args[0]);
        port=Integer.parseInt(args[0]);
        workerServer =new WorkerServer();
        workerServer.listen(port);
    }
    public WorkerProcess() {

    }



}
