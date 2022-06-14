package com.amg.os.util.network.connection;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Scanner;
import java.util.function.Consumer;

import util.serialize.Deserializer;
import util.serialize.Serializer;

public class Connection {

    private final PrintStream out;
    private final Scanner in;

    private final Serializer serializer;
    private final Deserializer deserializer;

    private Thread listenThread;

    /** 
     * constructor used the server 
     * 
     * @param socket port of the socket server accepting new connections
     * @throws IOException
     */
    public Connection(Socket socket) throws IOException {
        in = new Scanner(socket.getInputStream());
        out = new PrintStream(socket.getOutputStream());
        
        serializer = new Serializer();
        deserializer = new Deserializer();
    }
    
    /** 
     * constructor used by the clients
     * 
     * @param port port of the tcp server to connect to
     * @throws IOException
     */
    public Connection(int port) throws IOException {
        this(
            new Socket("localhost", port)
        );
    }

    public void send(String message) {

        out.println(message);

        System.out.println("sending:[ "+message+" ]");
    }

    public void send(Object obj) {
        this.send(obj.toString());
    }

    /**
     *  returns a new message from server (if available),
     *  otherwise blocks
     */
    public String receive() {
        if (in.hasNextLine()){
            String string=in.nextLine();
          //  if(string.length()<=20){
            System.out.println("receiving:[ "+string+" ]");
            return string;
        }
        return null;
    }

    /**
     *  @return true if there is new message
     *  @apiNote This method may block for input
     */
    public boolean hasNextLine() {
        return in.hasNextLine();
    }

    public void sendObject(Serializable object) {
        send(serializer.serialize(object));
    }

    public <T> T readObject() {
        return (T) deserializer.deserialize(receive());
    }

    /** 
     * closes connection.
     * notice this may be the only way to cancel the wait for nextLine or hasNextLine
     */
    public void close() {
        in.close();
        out.close();
    }

    /**
     *  an alternative way to receive message.
     * 
     *  catches the next message received by the connection and passes it to a callback
     * 
     *  @param onReceive called everytime connection recevies a message
     *  @apiNote warning: either use nextLine or listen
     */
    public void listen(Consumer<String> onReceive) {
        listenThread = new Thread(() -> {
            onReceive.accept(this.receive());
            listenThread = null;
        });
        listenThread.start();
    }

    public void interrupt() {
        if (listenThread != null)
            listenThread.interrupt();
    }
}
