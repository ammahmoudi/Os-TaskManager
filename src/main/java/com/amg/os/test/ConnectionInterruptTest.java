package com.amg.os.test;

import com.amg.os.util.network.connection.Connection;
import com.amg.os.util.network.server.Server;

import java.io.IOException;



public class ConnectionInterruptTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        new Server(con -> {
            try {
                Thread.sleep(3000);
                con.send("hello there");
                con.send("still listening?");
            } catch (InterruptedException e) {}

        }).listen(9090);

        var con = new Connection(9090);
        con.listen(System.out::println);
        con.interrupt();
        
        System.out.println("cancelled last listen");
        con.listen(System.out::println);
    }
}
