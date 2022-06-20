package com.amg.os.util.storage;
import com.amg.os.App;
import com.amg.os.controllers.StorageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Arrays;


public class StorageProcess extends Application {

    public static int port;
    public  static int[] memoryValues;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Storage-view.fxml"));
        Parent root=fxmlLoader.load();
        StorageController storageController=fxmlLoader.getController();
        Scene scene = new Scene(root);
        stage.setTitle("Storage!");
        stage.setScene(scene);
        stage.show();

        System.out.println(port+" "+Arrays.toString(memoryValues));
        try {
            storageController.initializeValues(port, memoryValues);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
      //  System.out.println(port);


    }


    public static void main(String[] args) {
        port=8090;
        memoryValues=new int[]{1,2,3,4,5};
        if(args.length>0) {
            port = Integer.parseInt(args[0]);

            memoryValues = Arrays.stream(args[1].split(" ")).mapToInt(Integer::parseInt).toArray();
        }

        launch();
    }
    @Override
    public void stop() {
        System.exit(0);
    }
}
