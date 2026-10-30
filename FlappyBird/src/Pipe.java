import java.awt.*;

public class Pipe {
    private int posX;
    private int posY;
    private int width;
    private int height;
    private Image image;
    private int velocityX;
    boolean passed;

    // constructor
    public Pipe(int posX, int posY, int width, int height, Image image){
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.image = image;
        this.velocityX = 0;
        this.passed = false;
    }

    // getter
    public int getPosX(){return posX;}
    public int getPosY(){return posY;}
    public int getWidth(){return width;}
    public int getHeight(){return height;}
    public Image getImage(){return image;}
    public int getVelocityX(){return velocityX;}
    public boolean getPassed(){return passed;}

    // setter
    void setPosX(int posX){
        this.posX = posX;
    }
    void setPosY(int posY){
        this.posY = posY;
    }
    void setWidth(int width){
        this.width = width;
    }
    void setHeight(int height){
        this.height = height;
    }
    void setImage(Image image){
        this.image = image;
    }
    void setVelocityX(int velocityX){
        this.velocityX = velocityX;
    }
    void setPassed(boolean passed){
        this.passed = passed;
    }



}
