package airplane;

import java.awt.image.BufferedImage;

public abstract class FlyingThings {
    protected int indexX,indexY;
    protected BufferedImage imgItself;
    protected int width,height;
    protected int movement;
    public int getIndexX(){return indexX;}
    public void setIndexX(int x){this.indexX = x;}
    public int getIndexY(){return indexY;}
    public void setIndexY(int y){this.indexY = y;}
    public int getwdith(){return width;}
    public void setWidth(int w){this.width = w;}
    public int getHeight(){return height;}
    public void setHeight(int h){this.height = h;}
    public BufferedImage getImgItself(){return imgItself;}
}
