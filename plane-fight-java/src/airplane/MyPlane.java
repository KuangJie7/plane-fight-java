package airplane;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MyPlane extends FlyingThings {
    private int life = 1;
    public int movement = 10;
    public boolean crashed = false;
    private BufferedImage imgNor,imgLef,imgRig;

    public MyPlane(File a,File d,File e, int b, int c) throws Exception {
        imgItself = ImageIO.read(a);
        imgNor = ImageIO.read(a);
        imgLef = ImageIO.read(d);
        imgRig = ImageIO.read(e);
        width = imgItself.getWidth();
        height = imgItself.getHeight();
        indexX = b;
        indexY = c;
    }

    public void setIimgItself(File a) throws IOException {
        this.imgItself = ImageIO.read(a);
    }


    public Bullet fireOneBullet(int a) throws IOException {
        Bullet bullet = new Bullet(a);
        bullet.setIndexX(this.indexX);
        bullet.setIndexY(this.indexY);
        return bullet;
    }

    public boolean shot(EnermyBullet a) {
        int x = a.indexX;
        int y = a.indexY;
        if (this.indexX - 45 < x + a.width && x < this.indexX + this.width - 50 && this.indexY - 10 < y + a.height && y < this.indexY + this.height - 10) {
            this.crashed = true;
            return true;
        }else{
            return false;
        }
    }

    public void goNor(){
        this.imgItself = imgNor;
    }

    public boolean goUp() {
        this.setIndexY(this.indexY - this.movement);
        return false;
    }

    public boolean goLeft() {
        this.setIndexX(this.indexX - this.movement);
        this.imgItself = this.imgLef;
        return false;
    }

    public boolean goDown() {
        this.setIndexY(this.indexY + this.movement);
        return false;
    }

    public boolean goRight() {
        this.setIndexX(this.indexX + this.movement);
        this.imgItself = imgRig;
        return false;
    }

    public boolean crash() {
        return crashed;
    }

    public void dead(){
        crashed = true;
        indexX = -100;
        indexY = -100;
        width = 0;
        height = 0;

    }

    public boolean crashInto(Enemy e) {
        if (e.indexX + e.width > indexX && e.indexX < indexX + width && e.indexY > indexY && e.indexY + e.height < indexY + height) {
            this.crashed = true;
            return true;
        }else {return false;}
    }
}
