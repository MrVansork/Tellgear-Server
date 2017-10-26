package com.tellgear;

import com.tellgear.client.Client;
import com.tellgear.util.Constants;
import com.tellgear.util.DataBase;
import com.tellgear.net.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private boolean running = false;
    private List<Client> clients = new ArrayList<>();
    private ServerSocket ss;
    private int port;
    private DataBase db;

    public Server(int port){
        this.port = port;
        db = new DataBase(Constants.ProgramData+Constants.DataBaseName);
    }

    public void init(){
        try {
            ss = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        running = true;

        try {
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void run() throws IOException {
        System.out.println("Server started at: "+Constants.PORT);
        while(running){

            clients.add(new Client(ss.accept(), this));
            clients.get(clients.size()-1).init();
        }
    }

    public synchronized void handle(int ID, Message msg){
        if(msg.content.equals("!!##quit")){
            Announce("signout", "SERVER", msg.sender);
            remove(ID);
        }else{
            if(msg.type.equals("login")){
                if(findClient(msg.sender) == null){
                    if(db.checkLogin(msg.sender, msg.content)){
                        findClient(ID).setUsername(msg.sender);
                        findClient(ID).send(new Message("login", "SERVER", "TRUE", msg.sender));
                        Announce("newuser", "SERVER", msg.sender);
                        SendUserList(msg.sender);
                        System.out.println("TRUE "+ID+" - ");
                    }
                    else{
                        findClient(ID).send(new Message("login", "SERVER", "FALSE:BAD_LOGIN", msg.sender));
                    }
                }
                else{
                    findClient(ID).send(new Message("login", "SERVER", "FALSE:ON_LINE", msg.sender));
                }
            }

            else if(msg.type.equals("message")){
                if(msg.recipient.equals("!!##ALL")){
                    Announce("message", msg.sender, msg.content);
                }
                else{
                    findClient(msg.recipient).send(new Message(msg.type, msg.sender, msg.content, msg.recipient));
                    findClient(ID).send(new Message(msg.type, msg.sender, msg.content, msg.recipient));
                }
            }

            else if(msg.type.equals("signup")){
                if(findClient(msg.sender) == null){
                    if(!db.userExists(msg.sender)){
                        db.addUser(msg.sender, msg.content);
                        findClient(ID).setUsername(msg.sender);
                        findClient(ID).send(new Message("signup", "SERVER", "TRUE", msg.sender));
                        findClient(ID).send(new Message("login", "SERVER", "TRUE", msg.sender));
                        Announce("newuser", "SERVER", msg.sender);
                        SendUserList(msg.sender);
                    }
                    else{
                        findClient(ID).send(new Message("signup", "SERVER", "FALSE:EXISTS", msg.sender));
                    }
                }
                else{
                    findClient(ID).send(new Message("signup", "SERVER", "FALSE:ON_LINE", msg.sender));
                }
            }
        }
    }

    public void SendUserList(String toWhom){
        for(int i = 0; i < clients.size(); i++){
            findClient(toWhom).send(new Message("newuser", "SERVER", clients.get(i).getUsername(), toWhom));
        }
    }

    public void Announce(String type, String sender, String content){
        Message msg = new Message(type, sender, content, "All");
        for(int i = 0; i < clients.size(); i++){
            clients.get(i).send(msg);
        }
    }

    private Client findClient(int ID){
        for(Client c:clients)
            if(c.getID() == ID)
                return c;
        return null;
    }

    public Client findClient(String usr){
        for(Client c:clients){
            if(c.getUsername().equals(usr)){
                return c;
            }
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    public synchronized void remove(int ID){
        Client client = findClient(ID);
        System.out.println("Removing "+ID + " -> "+ client.getUsername());
        clients.remove(client);
        try{
            client.close();
        }
        catch(IOException ioe){
            System.out.println("\nError closing thread: " + ioe);
        }
        client.stop();
    }

}
