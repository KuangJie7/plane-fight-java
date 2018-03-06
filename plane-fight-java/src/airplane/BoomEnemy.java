package airplane;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class BoomEnemy extends Enemy {
    private int life = 1;
    public int reward = 1;
    private MyPlane aimPlane;
    private int movement = 6;
    public int ran;

    public BoomEnemy(MyPlane a, MyPlane b, int ranNum) throws Exception{
        imgItself = ImageIO.read(new File("EnermyBoom.png"));
        width = imgItself.getWidth();
        height = imgItself.getHeight();
        indexX = (int)(Math.random() * 500);
        indexY = -height;
        ran = ranNum;
        if(ran <5){
            aimPlane = a;
        }else{
            aimPlane = b;
        }
    }

    public EnermyBullet fireOneEnermyBullet() throws IOException {
        EnermyBullet e = new EnermyBullet();
        return e;
    }

    public boolean crash(){
        return this.life == 0;
    }

    public int getReward(){
        return reward;
    }

    public int getRan(){
        return ran;
    }

    public void setRan(int a){
        this.ran = a;
    }

    public boolean getDown(){
        return indexY == 700 + height;
    }

    @Override
    public void reachBorder(){
    }

    public void setIndexX(int a){
        this.indexX = a;
    }



    public boolean shot(Bullet a) {
        int x = a.indexX;
        int y = a.indexY;
        if (this.indexX - 10 < x + a.width  && x < this.indexX + this.width && this.indexY < y  && y < this.indexY + this.height - 20 ) {
            this.life--;
            return true;
        }else{
            return false;
        }
    }

    public void enermyMove(){
        indexY += movement;
        if(indexX < aimPlane.indexX) {
            indexX += movement;
        }else {
            indexX -= movement;
        }
    }
}
