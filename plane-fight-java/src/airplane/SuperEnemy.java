package airplane;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class SuperEnemy extends Enemy {
    private int life = 2;
    public int reward = 3;
    private int xDirection = (int)(Math.random()*10);
    private int Direction = 0;

    public SuperEnemy() throws Exception{
        imgItself = ImageIO.read(new File("EnermySur.png"));
        width = imgItself.getWidth();
        height = imgItself.getHeight();
        indexX = (int)(Math.random() * 500);
        indexY = -height;
    }

    public EnermyBullet fireOneEnermyBullet() throws IOException {
        EnermyBullet e = new EnermyBullet();
        e.setIndexX(this.indexX);
        e.setIndexY(this.indexY);
        e.setDifferDistanceX(this.width);
        e.setDifferDistanceY(this.height);
        return e;
    }

    public void reachBorder(){
        if(indexX + width > 500||indexY + height > 700){
            Direction = 1;
        }else if(indexX < 0){
            Direction = 0;
        }
    }

    public void setIndexX(int a){
        this.indexX = a;
    }

    public boolean crash(){
        return this.life == 0;
    }

    public boolean getDown(){
        return indexY == 700 + height;
    }

    public boolean shot(Bullet a) {
        int x = a.indexX;
        int y = a.indexY;
        if (this.indexX - 40 < x + a.width  && x + a.width  < this.indexX + this.width -35 && this.indexY < y && y < this.indexY + this.height) {
            this.life--;
            return true;
        }else{
            return false;
        }
    }

    public int getxDirection(){
        return this.xDirection;
    }

    public void setxDirection(int a){
        this.xDirection = a;
    }

    public int getReward(){
        return reward;
    }
    public void enermyMove(){
        indexY += movement;
        reachBorder();
        if (Direction == 0){
            indexX += movement*xDirection/5;
        }else if(Direction == 1){
            indexX -= movement*xDirection/5;
        }
    }
}
