package com.amg.os.controllers;

import com.amg.os.DeadLockMode;
import com.amg.os.SchedulingMode;
import com.amg.os.master.Master;
import com.amg.os.master.MasterApi;
import com.amg.os.master.MasterServer;
import com.amg.os.util.CustomOutputStream;
import com.amg.os.util.storage.Storage;
import com.amg.os.util.storage.StorageServer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;

public class MasterController {

    @FXML
    private TextArea console;

    @FXML
    private Label welcomeText;

    public static TextArea MasterConsole;
    public Master master;
    public MasterServer masterServer;


    public void initializeValues() throws IOException, InterruptedException {
        MasterConsole = console;
        PrintStream printStream = new PrintStream(new CustomOutputStream(MasterConsole));
        System.setOut(printStream);
        System.setErr(printStream);
        master = new Master(2, 2, SchedulingMode.FCFS, DeadLockMode.NONE, new int[]{1, 2, 3, 4, 5});
        masterServer = new MasterServer(master);
        masterServer.listen(0);
        Thread.sleep(200);
        Master.masterPort = masterServer.getServer().port;
        master.addJob("100 1 100 0 200 3");
        master.addJob("1000 2 100 0 200 4");
        master.initialize();
        System.out.println("Master initialized");


    }
}