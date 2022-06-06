package com.amg.os.util.storage;

import com.amg.os.App;
import com.amg.os.controllers.StorageController;
import com.amg.os.controllers.WorkerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

import static javafx.application.Application.launch;

public class WorkerProcess extends Application {

    public static int port;
    public  static int id;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("worker-view.fxml"));
        Parent root=fxmlLoader.load();
        WorkerController workerController =fxmlLoader.getController();
        Scene scene = new Scene(root);
        stage.setTitle("Worker");
        stage.setScene(scene);
        stage.show();
        try {
            workerController.initializeValues(port,id);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        //  System.out.println(port);
    }


    public static void main(String[] args) {
        port=8091;
        id=0;

        if(args.length>0) {
            port = Integer.parseInt(args[0]);
          id=Integer.parseInt(args[1]);
        }

        launch();
    }
    @Override
    public void stop() {
        System.exit(0);
    }
}
