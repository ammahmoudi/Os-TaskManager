package com.amg.os.controllers;

import com.amg.os.master.MasterApi;
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
import java.util.Arrays;
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
        storageConsole=console;
        PrintStream printStream = new PrintStream(new CustomOutputStream(storageConsole)) ;
        System.setOut(printStream);
        System.setErr(printStream);
        System.out.println("initializing memory with:"+ Arrays.toString(memoryValues));
        storage=new Storage(memoryValues);
        storageServer=new StorageServer(storage);
        storageServer.listen(0);
        storage.setPort(storageServer.getServer().port);

        new Thread(() -> {
            //   Thread.sleep(1000);
            try {
                MasterApi masterApi = new MasterApi(port);
                try {
                    masterApi.introduceStorage(storageServer.getServer().port);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }
}