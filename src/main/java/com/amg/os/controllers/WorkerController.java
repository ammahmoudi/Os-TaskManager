package com.amg.os.controllers;

import com.amg.os.util.CustomOutputStream;
import com.amg.os.util.storage.Storage;
import com.amg.os.util.storage.StorageServer;
import com.amg.os.util.worker.Worker;
import com.amg.os.util.worker.WorkerServer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.PrintStream;

public class WorkerController  {

    @FXML
    private TextArea console;

    @FXML
    private Label welcomeText;
    public static WorkerServer workerServer;
    public static Worker worker;
    public  static TextArea workerConsole;


    public void initializeValues(int port,int id){
        workerConsole =console;
        PrintStream printStream = new PrintStream(new CustomOutputStream(workerConsole)) ;
        System.setOut(printStream);
        System.setErr(printStream);
        worker=new Worker(id,port);

        workerServer=new WorkerServer(worker);
        workerServer.listen(port);


    }
}