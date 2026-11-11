
import java.awt.*;

public class PlayerTemplate {

    private int x;
    private int y;
    private boolean visibility;
    private Image image;
    private int speed;
    private int width;
    private int height;
    public String direction;
    public boolean collisionOn;

    public PlayerTemplate(int x, int y, boolean visibility, Image image, int speed, int width, int height){
        this.x = x;
        this.y = y;
        this.visibility = visibility;
        this.image = image;
        this.speed = speed;
        this.width = width;
        this.height = height;
        width = 50;
        height = 50;
        direction = "";
        collisionOn = false;
    }
    
    public void render(Graphics g, int cameraX, int cameraY) {
        if (image != null) {
            g.drawImage(image, getX() - cameraX, getY() - cameraY, 50, 50, null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(getX() - cameraX, getY() - cameraY, 50, 50);
        }
    }

    public void setVisibility(boolean visible){
        visibility = visible;
    }

    public boolean getVisibility(){
        return visibility;
    }

    public void setImage(Image stage){
        stage = image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void changeX(int increment){
        x += increment;
    }

    public void changeY(int increment){
        y += increment;
    }

    public void changeXY(int increment){
        x += increment;
        y += increment;
    }

    public void setCoords(int newX, int newY){
        x = newX;
        y = newY;
    }

    public Image getImage() {
        return image;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, getWidth(), getHeight());
    }
    
    public int getSpeed(){
        return speed;
    }

    public void DirectionUp(){
        direction = "up";
    }

    public void DirectionDown(){
        direction = "down";
    }

    public void DirectionLeft(){
        direction = "left";
    }

    public void DirectionRight(){
        direction = "right";
    }

    public String getDirection(){
        return direction;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
