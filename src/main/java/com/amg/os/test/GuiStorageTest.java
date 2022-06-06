package com.amg.os.test;
import com.amg.os.util.process.Program;
import com.amg.os.util.storage.StorageProcess;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class GuiStorageTest {
    public static void main(String[] args) {
        Program storageProgram=new Program(StorageProcess.class,true).addArgument("9090").addArgument("1 2 3 4 5");
        try {
            storageProgram.run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
