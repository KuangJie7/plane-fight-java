package airplane;

import java.io.*;
import java.net.*;

public class Server {
    private DataOutputStream os;
    private Socket mySocket;
    GameTabServer myGameTab;

    public Server()throws IOException{
        ServerSocket serverA = new ServerSocket(8888);
         mySocket = serverA.accept();
        this.os = new DataOutputStream(mySocket.getOutputStream());

        ServerThread s = new ServerThread(mySocket,this);
        s.start();
    }

    public void sendMessage(String message) throws IOException{
        this.os.writeBytes(message);
    }

    public void setMyGameTab(GameTabServer a){
        this.myGameTab = a;
    }
}
