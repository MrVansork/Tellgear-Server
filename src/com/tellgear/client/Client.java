package com.tellgear.client;

import com.tellgear.Server;
import com.tellgear.net.Message;
import com.tellgear.util.UniqueIdentifier;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client extends Thread{

    private Socket socket = null;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;

    private boolean running = false;
    private Server server;

    private int ID = -1;
    private String username = "";

    public Client(Socket socket, Server server){
        this.socket = socket;
        this.server = server;
    }

    public void init() throws IOException {
        ID = UniqueIdentifier.getIdentifier();

        System.out.println("-New client accepted-");
        System.out.println("IP: "+socket.getInetAddress().toString());
        System.out.println("ID: "+ID);
        System.out.println("-   -   -   -   -   -");

        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        in = new ObjectInputStream(socket.getInputStream());

        running = true;
        start();
    }

    @SuppressWarnings("deprecation")
    public void run() {
        while(running){
            try{
                Message msg = (Message) in.readObject();
                server.handle(ID, msg);
            }
            catch(Exception ioe){
                System.out.println(ID + " ERROR reading: " + ioe.getMessage());
                server.remove(ID);
                stop();
            }
        }
    }

    public void close() throws IOException {
        if(socket != null) socket.close();
        if(out != null) out.close();
        if(in != null) in.close();
    }

    public void send(Message msg){
        try {
            out.writeObject(msg);
            out.flush();
        }
        catch (IOException ex) {
            System.out.println("Exception [SocketClient : send(...)]");
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public int getID() {
        return ID;
    }
}
