package com.amg.os.util.process;
import com.amg.os.util.storage.StorageProcess;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Program {
    private final LinkedList<String> command ;
    final String javaHome = System.getProperty("java.home");
    final String javaBin = javaHome + File.separator + "bin" + File.separator + "java.exe";
    final String classpath = "build\\classes\\java\\main";
    final String modulePath="C:\\Users\\amma\\IdeaProjects\\Os\\jfx-sdk\\lib\\";
    final String fxModules="javafx.controls,javafx.fxml";
    public static LinkedList<Process> processes =new LinkedList<>();

    public Program(List<String> commonArgs) {
    command= new LinkedList<>();
        command.addAll(commonArgs);

    }
    public Program(String[] commonArgs) {
        command =new LinkedList<>();
        command.addAll(Arrays.stream(commonArgs).toList());

    }

    public Program addArgument(String arg) {
        command.add(arg);
        return this;
    }
    public Program (Class<?> clazz,boolean isFX){
        command= new LinkedList<>();
        command.add(javaBin);
        command.add("-cp");
        command.add(classpath);
        if(isFX){
            command.add("--module-path");
            command.add(modulePath);
            command.add("--add-modules");
            command.add(fxModules);
        }
        command.add(clazz.getCanonicalName());
    }
    public Program addClassName (Class<?> clazz){
        command.add(clazz.getName());
        return this;
    }
    public Process run() throws IOException {
        String[] args=new String[command.size()];
        command.toArray(args);
        Process process=new ProcessBuilder(args).start();
        processes.add(process);
        return process ;
    }
    public Process runWithIO() throws IOException {
        String[] args=new String[command.size()];
        command.toArray(args);
        return new ProcessBuilder(args).inheritIO().start();
    }
}
