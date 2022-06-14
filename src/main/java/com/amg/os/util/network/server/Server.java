package com.amg.os.util.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

import com.amg.os.util.network.connection.Connection;


/** 
 *  TCP connection listener
 * 
 *  usage example:
 *  <pre>
 *  {@code
 *      new Server(
 *          connection -> connection.send("HI from server!")
 *      ).listen(port);
 *  }
 *  </pre>
 */
public class Server {
   public int port;

    private final Consumer<Connection> onAccept;

    /**
     * @param onAccept called whenever a new tcp connection arrives
     */
    public Server(Consumer<Connection> onAccept) {
        this.onAccept = onAccept;
    }

    public void listen(int port) {

        new Thread(() -> {
            try (ServerSocket server = new ServerSocket(port)){
                this.port=server.getLocalPort();
                listen(server);


            } catch (Exception e) {
            }
        }).start();
    }

    private void listen(ServerSocket server) throws IOException {
        System.out.println("listening on port:"+server.getLocalPort());
        this.port=server.getLocalPort();


        while (!server.isClosed()) {
            Socket socket = server.accept();
            System.out.println("socket accepted");
            onAccept.accept(new Connection(socket));
        }
    }
}
