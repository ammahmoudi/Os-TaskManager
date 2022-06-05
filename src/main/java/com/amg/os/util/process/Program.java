package com.amg.os.util.process;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

public class Program {

    private final LinkedList<String> command ;

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
    public Program addClassName (Class<?> clazz){
        command.add(clazz.getName());
        return this;
    }

    public Process run(Class<?> clazz) throws IOException {
        String[] args=new String[command.size()];
        command.toArray(args);
        return Runtime.getRuntime().exec(args);
    }
}
