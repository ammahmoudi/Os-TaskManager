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
                listen(server);
            } catch (Exception e) {
            }
        }).start();
    }

    private void listen(ServerSocket server) throws IOException {
        while (!server.isClosed()) {
            Socket socket = server.accept();
            onAccept.accept(new Connection(socket));
        }
    }
}
