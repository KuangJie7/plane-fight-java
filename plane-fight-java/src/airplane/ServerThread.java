package airplane;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.Socket;
import java.util.Objects;

public class ServerThread extends Thread {
    private Socket mySocket;
    private Server myServer;

    public ServerThread(Socket s, Server a) throws MalformedURLException {
        this.mySocket = s;
        this.myServer = a;
    }

    public void run() {
        try {
        while (true) {
                BufferedReader br = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
                String s = br.readLine();
                if(s.length() >= 2) {
                    if (Objects.equals(s.substring(0, 2), "p1")) {
                        myServer.myGameTab.p1score = Integer.parseInt(s.substring(2));
                    } else if (Objects.equals(s.substring(0, 2), "p2")) {
                        myServer.myGameTab.p2score = Integer.parseInt(s.substring(2));
                    }
                }

            switch (s){
                case  "1up":
                    myServer.myGameTab.plane1.goUp();
                    break;
                case "1down":
                    myServer.myGameTab.plane1.goDown();
                    break;
                case  "1left":
                    myServer.myGameTab.plane1.goLeft();
                    break;
                case "1right":
                    myServer.myGameTab.plane1.goRight();
                    break;
                case "1fire":
                    myServer.myGameTab.bullets.add(myServer.myGameTab.plane1.fireOneBullet(1));
                    break;
                case "pause":
                    myServer.myGameTab.setStateJudge(2);
                    break;
                case "continue":
                    myServer.myGameTab.setStateJudge(1);
                    break;
                case "1crash":
                    myServer.myGameTab.plane1.crashed = true;
                    break;
                case "goNor":
                    myServer.myGameTab.plane1.goNor();
                    break;
                case "1dead":
                    myServer.myGameTab.plane1.dead();
                    break;
                case "2dead":
                    myServer.myGameTab.plane2.dead();
                    break;
            }
        }
        } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
