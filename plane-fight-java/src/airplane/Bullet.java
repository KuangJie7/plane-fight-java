package airplane;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Bullet extends FlyingThings {
    public int movement = 15;
    public int belongsto;

    public Bullet(int a) throws IOException {
        this.indexX = 0;
        this.indexY = 0;
        this.belongsto = a;
        if(a == 1) {
            imgItself = ImageIO.read(new File("Bullet1.png"));
        }else if (a == 2){
            imgItself = ImageIO.read(new File("Bullet2.png"));
        }if (a == 3){
            imgItself = ImageIO.read(new File("BulletE.png"));
        }
        width = imgItself.getWidth();
        height = imgItself.getHeight();
    }

    public void myPlaneBulletMove(){
        this.indexY -= movement;
    }

}
