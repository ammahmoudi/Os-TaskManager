package com.amg.os.util.network.server;


import com.amg.os.util.network.connection.Connection;

public abstract class AbstractServer {
    private final Server server;

    protected AbstractServer() {
        this.server = new Server(this::acceptConnection);
    }
    
    public void listen(int port) {
        server.listen(port);
    }

    public abstract void acceptConnection(Connection connection);
}
