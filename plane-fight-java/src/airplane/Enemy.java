package airplane;

import java.io.IOException;

public abstract class Enemy extends FlyingThings{
    protected int life = 1;
    public int movement = 2;
    private int xDirection = (int)(Math.random()*10);
    private int Direction = 0;
    private int reward;

    public boolean shot(Bullet a) {
        int x = a.indexX;
        int y = a.indexY;
        if (this.indexX < x + a.width  && x + a.width  < this.indexX + this.width && this.indexY < y && y < this.indexY + this.height) {
            this.life--;
            return true;
        }else{
            return false;
        }
    }

    public abstract EnermyBullet fireOneEnermyBullet() throws IOException;

    public abstract boolean crash();

    public abstract void setIndexX(int a);

    public abstract int getReward();

    public abstract boolean getDown();

    public void reachBorder(){
        if(indexX + width > 500||indexY + height > 700){
            Direction = 1;
        }else if(indexX < 0){
            Direction = 0;
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
