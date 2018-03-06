package airplane;

import java.io.*;
import java.net.*;

public class Client {
    private DataOutputStream os;
    public GameTabClient clientGameTab;

    public Client() throws IOException{
        Socket socketClient = new Socket("localhost",8888);
        this.os = new DataOutputStream(socketClient.getOutputStream());

        ClientThread c = new ClientThread(socketClient,this);
        c.start();
    }

    public void sendMessage(String message) throws IOException {
        os.writeBytes(message);
    }
    public void setClientGameTab(GameTabClient a){
        this.clientGameTab = a;
    }
}
