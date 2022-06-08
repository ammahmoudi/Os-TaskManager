package com.amg.os.controllers;

import com.amg.os.DeadLockMode;
import com.amg.os.SchedulingMode;
import com.amg.os.master.Master;
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


    public void initializeValues() throws IOException {

        MasterConsole = console;
        PrintStream printStream = new PrintStream(new CustomOutputStream(MasterConsole));
        System.setOut(printStream);
        System.setErr(printStream);
        master = new Master(4, 4, SchedulingMode.FCFS, DeadLockMode.NONE, 9089, new int[]{1, 2, 3, 4, 5});
        master.initialize();
        System.out.println("initialized");


    }
}