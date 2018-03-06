package airplane;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class NormalEnemy extends Enemy {
    private int life = 1;
    public int reward = 1;
    private int xDirection = (int)(Math.random()*10);
    private int Direction = 0;

    public NormalEnemy() throws Exception{
        imgItself = ImageIO.read(new File("EnermyNor.png"));
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

    public boolean crash(){
        return this.life == 0;
    }

    public int getReward(){
        return reward;
    }

    public boolean getDown(){
        return indexY == 700 + height;
    }

    public void setIndexX(int a){
        indexX = a;
    }

    @Override
    public void reachBorder(){
        if(indexX + width > 500||indexY + height > 700){
            Direction = 1;
        }else if(indexX < 0){
            Direction = 0;
        }
    }

    public int getxDirection(){
        return this.xDirection;
    }

    public void setxDirection(int a){
        xDirection = a;
    }

    public boolean shot(Bullet a) {
        int x = a.indexX;
        int y = a.indexY;
        if (this.indexX < x + a.width + 32  && x < this.indexX + this.width  - 32&& this.indexY < y  && y < this.indexY + this.height - 20 ) {
            this.life--;
            return true;
        }else{
            return false;
        }
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
