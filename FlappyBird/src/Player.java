import java.awt.*;

public class Player {
    private int posX;
    private int posY;
    private int width;
    private int height;
    private Image image;
    private double velocityY = 0;

    // constructor
    public Player(int posX, int posY, int width, int height, Image image) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.image = image;
        this.velocityY = 0;
    }

    // getter
    public int getPosX() {return posX;};
    public int getPosY() {return posY;};
    public int getWidth() {return width;};
    public int getHeight() {return height;};
    public Image getImage() {return image;};
    public double getVelocityY() {return velocityY;}

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
    void setHeight(int height) {this.height = height;}
    void setImage(Image image) {this.image = image;}
    void setVelocityY(double velocityY) {this.velocityY = velocityY;}
}
