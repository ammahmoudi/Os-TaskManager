package com.amg.os.controllers;

import com.amg.os.DeadLockMode;
import com.amg.os.SchedulingMode;
import com.amg.os.master.Master;
import com.amg.os.master.MasterServer;
import com.amg.os.task.TaskContext;
import com.amg.os.util.CustomOutputStream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class MasterController {

    @FXML
    private Button addJob_button;

    @FXML
    private TextArea console;

    @FXML
    private TextField jobString_field;

    @FXML
    private TextField masterPort_field;

    @FXML
    private TextArea result_console;
    public static TextArea result;

    @FXML
    private ComboBox<SchedulingMode> scheduling_combo;

    @FXML
    private Button start_button;

    @FXML
    private Label storagePort_label;
    public static Label storagePort;
    @FXML
    private Label welcomeText;
    @FXML
    private TextField timeQuantum_field;
    @FXML
    private TextField workersN_field;
    @FXML
    private TextField storageData_field;
    @FXML
    private ComboBox<DeadLockMode> deadLockMode_combo;
    @FXML
    private ListView<Label> job_listview;

    @FXML
    void onAddJobButtonAction(ActionEvent event) {
        TaskContext taskContext = master.addJob(jobString_field.getText());
        Label label = new Label("Job " + taskContext.getId() + " : " + jobString_field.getText());
        label.setPadding(new Insets(10));
        job_listview.getItems().add(label);
        jobString_field.clear();
    }

    @FXML
    void onStartButtonAction(ActionEvent event) throws IOException, ExecutionException, InterruptedException {
        if (start_button.getText().equals("New Master")) {
            int workers_n = Integer.parseInt(workersN_field.getText());
            int[] storageData = Arrays.stream(storageData_field.getText().split(" ")).mapToInt(Integer::parseInt).toArray();
            master = new Master(workers_n, 0, scheduling_combo.getValue(), deadLockMode_combo.getValue(), storageData);
            master.setTimeQuantum(Integer.parseInt(timeQuantum_field.getText()));
            masterServer = new MasterServer(master);
            masterServer.listen(Integer.parseInt(masterPort_field.getText()));
            //    Thread.sleep(200);
           // Master.masterPort = masterServer.getServer().port;
            Master.masterPort= Integer.parseInt(masterPort_field.getText());
            start_button.setText("start");
            System.out.println("now Add your jobs and press start button");

        } else {
//        master.addJob("1000 1 2000 2 1000 4");
//        master.addJob("1000 2 1000 0 1000 4");
//        master.addJob("1000 0 500 1 2000 2");

            master.initialize();
            System.out.println("Master initialized");
            start_button.setText("New Master");
            start_button.setDisable(true);
        }
    }

    public static TextArea masterConsole;
    public Master master;
    public MasterServer masterServer;


    public void initializeValues() throws IOException, InterruptedException, ExecutionException {
        scheduling_combo.setItems(FXCollections.observableArrayList(SchedulingMode.FCFS, SchedulingMode.SJF, SchedulingMode.RR));
        deadLockMode_combo.setItems(FXCollections.observableArrayList(DeadLockMode.NONE, DeadLockMode.DETECT, DeadLockMode.PREVENT));
        masterConsole = console;
        result = result_console;
        storagePort = storagePort_label;

        PrintStream printStream = new PrintStream(new CustomOutputStream(masterConsole));
        System.setOut(printStream);
        System.setErr(printStream);
test();

    }
    public void test(){
        masterPort_field.setText("8090");
        workersN_field.setText("2");
        storageData_field.setText("1 2 3 4 5");
        scheduling_combo.setValue(SchedulingMode.FCFS);
        deadLockMode_combo.setValue(DeadLockMode.NONE);
        timeQuantum_field.setText("0");

    }

}


