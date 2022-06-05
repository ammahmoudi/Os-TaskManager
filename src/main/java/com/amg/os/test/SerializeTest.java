package com.amg.os.test;

import java.io.IOException;
import java.io.Serializable;

import com.amg.os.util.network.connection.Connection;
import com.amg.os.util.network.server.Server;


public class SerializeTest {
    
    static class Data implements Serializable {
        private final int x;

        public Data(int x) {
            this.x = x;
        }

        public void print() {
            System.out.println("I am " + x);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 8080;
        new Server(con -> {
            Data data = con.readObject();
            data.print();
        }).listen(port);

        new Connection(port).sendObject(new Data(2));

        Thread.sleep(2000);
    }
}
