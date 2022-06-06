package com.amg.os.controllers;

import com.amg.os.util.CustomOutputStream;
import com.amg.os.util.storage.Storage;
import com.amg.os.util.storage.StorageServer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;

public class StorageController  {

    @FXML
    private TextArea console;

    @FXML
    private Label welcomeText;
    public static   StorageServer storageServer;
    public static Storage storage;
    public  static TextArea storageConsole;


    public void initializeValues(int port,int[] memoryValues){
        storage=new Storage(memoryValues);
        storageServer=new StorageServer(storage);
        storageServer.listen(port);
        storageConsole=console;
        PrintStream printStream = new PrintStream(new CustomOutputStream(storageConsole)) ;
        System.setOut(printStream);
        System.setErr(printStream);
       System.out.println("hi");
    }
}