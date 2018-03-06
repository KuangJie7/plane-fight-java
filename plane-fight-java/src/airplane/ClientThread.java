package airplane;//不断监听接收的信息

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread extends Thread{
    private Socket mySocket;
    private Client myClient;

    public ClientThread(Socket s,Client a){
        this.mySocket = s;
        this.myClient = a;
    }

    public void run(){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
            while (true){
                String s = br.readLine();
                if(s.length() >= 2) {
                    switch (s.substring(0, 2)) {
                        case "p1":
                            myClient.clientGameTab.p1score = Integer.parseInt(s.substring(2));
                            break;
                        case "p2":
                            myClient.clientGameTab.p2score = Integer.parseInt(s.substring(2));
                            break;
                        case "ne":
                            NormalEnemy a = new NormalEnemy();
                            a.setIndexX(Integer.parseInt(s.substring(2, s.indexOf(","))));
                            a.setxDirection(Integer.parseInt(s.substring(s.indexOf(",") + 1, s.length())));
                            myClient.clientGameTab.enemy.add(a);
                            break;
                        case "nb":
                            BoomEnemy b = new BoomEnemy(myClient.clientGameTab.plane1, myClient.clientGameTab.plane2, Integer.parseInt(s.substring(s.indexOf(",") + 1, s.length())));
                            b.setIndexX(Integer.parseInt(s.substring(2, s.indexOf(","))));
                            myClient.clientGameTab.enemy.add(b);
                            break;
                        case "ns":
                            SuperEnemy c = new SuperEnemy();
                            c.setIndexX(Integer.parseInt(s.substring(2, s.indexOf(","))));
                            c.setxDirection(Integer.parseInt(s.substring(s.indexOf(",") + 1, s.length())));
                            myClient.clientGameTab.enemy.add(c);
                            break;
                    }
                }

                switch (s){
                    case  "2up":
                        myClient.clientGameTab.plane2.goUp();
                        break;
                    case "2down":
                        myClient.clientGameTab.plane2.goDown();
                        break;
                    case  "2left":
                        myClient.clientGameTab.plane2.goLeft();
                        break;
                    case "2right":
                        myClient.clientGameTab.plane2.goRight();
                        break;
                    case "2fire":
                        myClient.clientGameTab.bullets.add(myClient.clientGameTab.plane2.fireOneBullet(2));
                        break;
                    case "start":
                        myClient.clientGameTab.start();
                        break;
                    case "pause":
                        myClient.clientGameTab.setStateJudge(2);
                        break;
                    case "continue":
                        myClient.clientGameTab.setStateJudge(1);
                        break;
                    case  "2crash":
                        myClient.clientGameTab.plane2.crashed = true;
                        break;
                    case "win":
                        myClient.clientGameTab.setStateJudge(4);
                        break;
                    case "goNor":
                        myClient.clientGameTab.plane2.goNor();
                        break;
                    case "1dead":
                        myClient.clientGameTab.plane1.dead();
                        break;
                    case "2dead":
                        myClient.clientGameTab.plane2.dead();
                        break;
                    case "over":
                        myClient.clientGameTab.setStateJudge(3);
                        break;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
        }
