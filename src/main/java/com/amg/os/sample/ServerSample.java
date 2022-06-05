package com.amg.os.sample;

import java.io.IOException;

import com.amg.os.util.network.connection.Connection;
import com.amg.os.util.network.server.Server;


public class ServerSample {
    public static void main(String[] args) throws IOException {
        int port = 9090;

        new Server(
            connection -> {
                connection.send("HI");
                connection.close();
            }
        ).listen(port);
            
        var con = new Connection(9090);
        System.out.println(con.receive());
    }
}
