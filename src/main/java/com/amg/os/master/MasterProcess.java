package com.amg.os.master;

import com.amg.os.App;
import com.amg.os.controllers.MasterController;
import com.amg.os.controllers.StorageController;
import com.amg.os.util.process.Program;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import static javafx.application.Application.launch;

public class MasterProcess extends Application {
    @Override
    public void start(Stage stage) throws IOException, InterruptedException, ExecutionException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("master-view.fxml"));
        Parent root=fxmlLoader.load();
        MasterController masterController =fxmlLoader.getController();
        Scene scene = new Scene(root);
        stage.setTitle("Master");
        stage.setScene(scene);
        stage.show();
        masterController.initializeValues();

    }


    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
         //   ProcessHandle.current().descendants().forEach(ProcessHandle::destroyForcibly);
            Program.processes.forEach(Process::destroy);
        }));

        launch(args);
    }
    @Override
    public void stop() {
        System.exit(0);
    }
}
