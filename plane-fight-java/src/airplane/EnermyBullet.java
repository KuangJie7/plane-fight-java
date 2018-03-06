package airplane;

import java.io.IOException;

public class EnermyBullet extends Bullet {
    public int differDistanceX,differDistanceY;

    public EnermyBullet() throws IOException {
        super(3);
    }

    public void enermyBulletMove(){
        this.movement = 5;
        this.indexY += this.movement;
    }

    public void setDifferDistanceX(int x){this.differDistanceX = (int)(Math.floor(x/2));}
    public void setDifferDistanceY(int y){this.differDistanceY = y;}

}
