package airplane;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

public class GameTabServer extends JFrame {
    private JPanel panelContent;
    private JMenuBar jmenubarMenuBar;
    private JMenu jmenuOpration, jMenuFile;
    private JMenuItem jmenuiStatrt, jmenuiPause, jmenuiGoOn, jmenuiSave;
    private ImageIcon imgBackgroundImg;
    private BufferedImage pause = ImageIO.read(new File("暂停.png"));
    private BufferedImage opening = ImageIO.read(new File("飞机大战.png"));
    private BufferedImage ending = ImageIO.read(new File("游戏结束.png"));
    private BufferedImage imgWin = ImageIO.read(new File("Win.png"));
    private boolean up = false, down = false, left = false, right = false;
    public int crashedEnermy = 0, p1score = 0, p2score = 0;
    private File normal1 = new File("Plane1_Normal_1.png");
    private File leftMove1 = new File("Plane1_Left_1.png");
    private File rightMove1 = new File("Plane1_right_1.png");
    private File leftMove2 = new File("Plane2_Left.png");
    private File rightMove2 = new File("Plane2_right.png");
    private File normal2 = new File("Plane2_Normal_1.png");
    private boolean win = false;
    public MyPlane plane1 = new MyPlane(normal1, leftMove1, rightMove1, 75, 500);
    public MyPlane plane2 = new MyPlane(normal2, leftMove2, rightMove2, 300, 500);
    private Graphics g;
    private int enermyWait = 1;
    private int reload = 1;
    private onJPanel paintOnJPanel = null;
    public ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    public int enermyType;
    private ArrayList<EnermyBullet> ebullets = new ArrayList<EnermyBullet>();
    private ArrayList<Enemy> enemy = new ArrayList<Enemy>();
    private static int stateJudge = 0;
    private Server myServer;
    private AudioClip audio;

    public GameTabServer() throws Exception {
        super("Air Force - P2");
        imgBackgroundImg = new ImageIcon("background.jpg");
        JLabel label = new JLabel(imgBackgroundImg);
        label.setBounds(-100, -50, imgBackgroundImg.getIconWidth(), imgBackgroundImg.getIconHeight());
        getLayeredPane().add(label, new Integer(-30001));

        panelContent = (JPanel) getContentPane();
        panelContent.setOpaque(false);
        paintOnJPanel = new onJPanel();
        paintOnJPanel.setOpaque(false);
        panelContent.add(paintOnJPanel);

        MyPlaneMoveListener plane2Move = new MyPlaneMoveListener(plane2, bullets);
        SpaceListener plane2Fire = new SpaceListener(plane2, bullets);
        this.addKeyListener(plane2Fire);
        this.addKeyListener(plane2Move);

        jmenubarMenuBar = new JMenuBar();
        jmenuOpration = new JMenu("Opration");
        jMenuFile = new JMenu("File");
        jmenuiStatrt = new JMenuItem("Start");
        jmenuiPause = new JMenuItem("Pause");
        jmenuiGoOn = new JMenuItem("Continue");
        jmenuiSave = new JMenuItem("Save");
        jmenuiStatrt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    stateJudge = 1;
                    ebullets = new ArrayList<EnermyBullet>();
                    enemy = new ArrayList<Enemy>();
                    plane1 = new MyPlane(normal1, leftMove1, rightMove1, 75, 500);
                    plane2 = new MyPlane(normal2, leftMove2, rightMove2, 300, 500);
                    enermyWait = 1;
                    crashedEnermy = 0;
                    p1score = 0;
                    p2score = 0;
                    win = false;
                    myServer.sendMessage("start" + "\n");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }
        });
        jmenuiPause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    stateJudge = 2;
                    myServer.sendMessage("pause" + "\n");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        jmenuiGoOn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    stateJudge = 1;
                    myServer.sendMessage("continue" + "\n");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        jmenuOpration.add(jmenuiStatrt);
        jmenuOpration.add(jmenuiPause);
        jmenuOpration.add(jmenuiGoOn);
        jMenuFile.add(jmenuiSave);
        jmenubarMenuBar.add(jmenuOpration);
        jmenubarMenuBar.add(jMenuFile);
        setJMenuBar(jmenubarMenuBar);

        setSize(500, 700);
        setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        Server s = new Server();
        GameTabServer a = new GameTabServer();
        a.myServer = s;
        s.setMyGameTab(a);
        a.loop();
        a.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private class MusicThread extends Thread{
        @Override
        public void run() {
            try {
                playMusic();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    public void loop() throws MalformedURLException {
        Timer timer = new Timer(); // 流程控制
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (stateJudge == 1) {
                        myPlaneFly(up, down, left, right);
                        myPlaneBulletFly();
                        createEnermy();
                        enermyFly(enemy);
                        enermyBulletFly(ebullets);
                        gameOver();
                        judgeWin();
                        repaint();
                    } else if (stateJudge == 2 || stateJudge == 3) {
                        repaint();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 100, 20);
    }

    private class onJPanel extends JPanel {
        public void paint(Graphics g) {
            super.paint(g);
            try {
                if (stateJudge == 0) {
                    g.drawImage(opening, 50, 200, 400, 150, null);
                } else if (stateJudge == 1) {
                    paintMyPlane(g);
                    paintBullets(g);
                    paintNormalEnermy(g);
                    paintEnermyBullets(g);
                    paintInfo(g);
                } else if (stateJudge == 2) {
                    paintMyPlane(g);
                    paintBullets(g);
                    paintNormalEnermy(g);
                    paintEnermyBullets(g);
                    paintPause(g);
                    paintInfo(g);
                } else if (stateJudge == 3) {
                    g.drawImage(ending, 50, 200, 400, 150, null);
                    paintInfo(g);
                } else if (stateJudge == 4) {
                    paintWin(g);
                    paintInfo(g);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void paintPause(Graphics g) {
        g.drawImage(pause, 50, 200, 400, 150, null);
    }

    public void paintWin(Graphics g) {
        g.drawImage(imgWin, 50, 200, 400, 150, null);
    }

    public void paintInfo(Graphics g) {
        Font font = new Font("TimesRoman", Font.BOLD, 20);
        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString("消灭敌机", 200, 30);
        g.drawString("" + crashedEnermy, 235, 50);
        g.drawString("P1\n", 10, 30);
        g.drawString("" + p1score, 15, 50);
        g.drawString("P2\n", 450, 30);
        g.drawString("" + p2score, 455, 50);
    }

    public void paintMyPlane(Graphics g) {
        try {
            if (!plane1.crash()) {
                g.drawImage(plane1.getImgItself(), plane1.getIndexX(), plane1.getIndexY(), plane1.getwdith(), plane1.getHeight(), null);
            }
            if (!plane2.crash()) {
                g.drawImage(plane2.getImgItself(), plane2.getIndexX(), plane2.getIndexY(), plane2.getwdith(), plane2.getHeight(), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void paintBullets(Graphics g) {
        if (bullets.size() != 0) {
            for (int i = 0; i < bullets.size(); i++) {
                if (bullets.get(i).belongsto == 1) {
                    g.drawImage(bullets.get(i).imgItself, bullets.get(i).indexX - bullets.get(i).width / 2 + plane1.width / 2, bullets.get(i).indexY + bullets.get(i).height / 2 - plane1.height / 2, bullets.get(i).width, bullets.get(i).height, null);
                } else if (bullets.get(i).belongsto == 2) {
                    g.drawImage(bullets.get(i).imgItself, bullets.get(i).indexX - bullets.get(i).width / 2 + plane2.width / 2, bullets.get(i).indexY + bullets.get(i).height / 2 - plane2.height / 2, bullets.get(i).width, bullets.get(i).height, null);
                }
            }
        }
    }

    public void paintNormalEnermy(Graphics g) {
        for (Enemy a : enemy) {
            if (!a.crash()) {
                g.drawImage(a.imgItself, a.indexX, a.indexY, a.width, a.height, null);
            }
        }
    }

    public void paintEnermyBullets(Graphics g) {
        if (ebullets.size() != 0) {
            for (int i = 0; i < ebullets.size(); i++) {
                g.drawImage(ebullets.get(i).imgItself, ebullets.get(i).indexX - ebullets.get(i).width / 2 + ebullets.get(i).differDistanceX, ebullets.get(i).indexY - ebullets.get(i).height / 2 + ebullets.get(i).differDistanceY, ebullets.get(i).width, ebullets.get(i).height, null);
            }
        }
    }

    private class MyPlaneMoveListener implements KeyListener {
        MyPlane myplane;
        ArrayList<Bullet> myBullet;

        public MyPlaneMoveListener(MyPlane a, ArrayList<Bullet> b) {
            this.myplane = a;
            this.myBullet = b;
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                if (plane2.indexY <= 550) {
                    down = true;
                }
            }

            if (e.getKeyCode() == KeyEvent.VK_UP) {
                if (plane2.indexY >= 0) {
                    up = true;
                }
            }

            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                if (plane2.indexX >= 0) {
                    left = true;
                }
            }

            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                if (plane2.indexX <= 402) {
                    right = true;
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                try {
                    plane2.goNor();
                    myServer.sendMessage("goNor" + "\n");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private class SpaceListener implements KeyListener {
        MyPlane myplane;
        ArrayList<Bullet> myBullet;

        public SpaceListener(MyPlane m, ArrayList<Bullet> b) {
            this.myplane = m;
            this.myBullet = b;
        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                try {
                    if (reload == 1) {
                        Bullet a = plane2.fireOneBullet(2);
                        myServer.sendMessage("2fire" + "\n");
                        myBullet.add(a);
                        reload = 0;
                        reloading();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    public void myPlaneFly(boolean a, boolean b, boolean c, boolean d) throws IOException {
        if (a) {
            up = plane2.goUp();
            myServer.sendMessage("2up" + "\n");
        }
        if (b) {
            down = plane2.goDown();
            myServer.sendMessage("2down" + "\n");
        }
        if (c) {
            left = plane2.goLeft();
            myServer.sendMessage("2left" + "\n");
        }
        if (d) {
            right = plane2.goRight();
            myServer.sendMessage("2right" + "\n");
        }
    }

    public void myPlaneBulletFly() throws IOException {
        Iterator<Bullet> itb = bullets.iterator();
        while (itb.hasNext()) {
            Bullet a = itb.next();
            if (a.indexY >= -40) {
                a.myPlaneBulletMove();
                Iterator<Enemy> normalIter = enemy.iterator();
                while (normalIter.hasNext()) {
                    Enemy b = normalIter.next();
                    if (b.shot(a)) {
                        itb.remove();
                        if (b.crash()) {
                            if (a.belongsto == 1) {
                                normalIter.remove();
                            } else if (a.belongsto == 2) {
                                p2score += b.getReward();
                                normalIter.remove();
                                myServer.sendMessage("p2" + p2score + "\n");
                            }
                            crashedEnermy++;
                        }
                    }
                }
            } else {
                itb.remove();
            }
            paintOnJPanel.repaint();
        }
    }

    public void enermyBulletFly(ArrayList<EnermyBullet> a) throws IOException {
        Iterator<EnermyBullet> ebIter = a.iterator();
        while (ebIter.hasNext()) {
            EnermyBullet e = ebIter.next();
            if (e.indexY <= 750) {
                e.enermyBulletMove();
                if (plane1.shot(e)) {
                    ebIter.remove();
                    plane1.dead();
                    myServer.sendMessage("1dead"+"\n");
                }else if(plane2.shot(e)){
                    ebIter.remove();
                    plane2.dead();
                    myServer.sendMessage("2dead"+"\n");
                }
            } else {
                ebIter.remove();
            }
        }
    }

    public void createEnermy() throws Exception {
        enermyWait++;
        if (enermyWait % 125 == 0) {
            enermyType = (int) (Math.random() * 10);
            if (enermyType <= 6) {
                NormalEnemy a = new NormalEnemy();
                myServer.sendMessage("ne" + a.indexX + "," + a.getxDirection() + "\n");
                enemy.add(a);
            } else if (enermyType <= 8) {
                int ranNum = (int) (Math.random() * 10);
                BoomEnemy a = new BoomEnemy(plane1, plane2, ranNum);
                myServer.sendMessage("nb" + a.indexX + "," + ranNum + "\n");
                enemy.add(a);
            } else {
                SuperEnemy a = new SuperEnemy();
                myServer.sendMessage("ns" + a.indexX + "," + a.getxDirection() + "\n");
                enemy.add(a);
            }
        }
    }

    public void reloading() {
        Timer reloadingTimer = new Timer();
        reloadingTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                reload = 1;
            }
        }, 1000);
    }

    public void enermyFly(ArrayList<Enemy> a) throws IOException {
        Iterator<Enemy> eIter = a.iterator();
        while (eIter.hasNext()) {
            Enemy b = eIter.next();
            if (b.getDown()) {
                eIter.remove();
            }
            if (enermyWait % 60 == 0) {
                if (b instanceof NormalEnemy || b instanceof SuperEnemy) {
                    EnermyBullet t = b.fireOneEnermyBullet();
                    ebullets.add(t);
                }
            }
            if (plane1.crashInto(b)) {
                plane1.dead();
                eIter.remove();
                myServer.sendMessage("1dead"+"\n");
            } else if(plane2.crashInto(b)){
                plane2.dead();
                eIter.remove();
                myServer.sendMessage("2dead"+"\n");
            }
            b.enermyMove();
        }
    }

    public void setStateJudge(int a) {
        stateJudge = a;
    }

    public void gameOver() throws IOException {
        if (plane1.crash() && plane2.crash()) {
            stateJudge = 3;
            myServer.sendMessage("over"+"\n");
        }
    }

    public void judgeWin() throws IOException {
        if (crashedEnermy == 20) {
            win = true;
            stateJudge = 4;
            myServer.sendMessage("win" + "\n");
        }
    }

    static void playMusic() throws MalformedURLException {
        URI cd;
        File f = new File("BackgroundMusic.WAV");
        cd = f.toURI();
        URL music = cd.toURL();
        AudioClip auc;
        auc = Applet.newAudioClip(music);
        auc.loop();
    }
}
