import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;

public class bullet extends PlayerTemplate{
    private double dx;
    private double dy;
    private boolean active;
    private boolean stopped;
    private int endX;
    private int endY;
    private boolean bulletType;//true =  blue portal bullet; false = orange portal bullet
    private Image bulletImage;
    public double realX, realY;
    private CollisionDetection colD;
    private Game g;
    private boolean wallP1 = false;
    private boolean wallP2 = false;

    public bullet(int x, int y, boolean visibility, int speed, Game g) {
        super(x, y, visibility,null, speed,10,10);
        active = false;
        stopped = false;
        endX = 0;
        endY = 0;
        bulletType = true;
        this.g = g;
        colD = new CollisionDetection(g);
        try {
            bulletImage = ImageIO.read(getClass().getResourceAsStream("/Images/Blue Portal launcher.png"));
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void trigger(int startX, int startY, int endX, int endY, boolean type){
        bulletType = type;
        setCoords(startX, startY);
        realX = startX;
        realY = startY;
        setVisibility(true);
        active = true;
        stopped = false;
        this.endX = endX;
        this.endY = endY;

        double angle = Math.atan2(endY - startY, endX - startX);
        dx = Math.cos(angle) * getSpeed();
        dy = Math.sin(angle) * getSpeed();
        bulletType = !bulletType;
    }
     
    public void update(int level) {
        if (active) {
            double nextX =  realX + dx;
            double nextY = realY + dy;
           
            int centerX = (int) (nextX + getBounds().width / 2);
            int centerY = (int) (nextY + getBounds().height / 2);
            if(colD.checkTileAt(this,centerX, centerY, level)){
                active = false;
                stopped = true;
                setVisibility(false);
                setCoords((int)realX, (int)realY);
                if(bulletType){
                    wallP1 = true;
                }else{
                    wallP2 = true;
                }
            } else{
                realX = nextX;
                realY = nextY;
                setCoords((int)realX, (int)realY);

                int deltaX = endX - (int)realX;
                int deltaY = endY - (int)realY;
                if ((dx * deltaX + dy * deltaY) <= 0) {
                    active = false;
                    stopped = true;
                    setVisibility(false);
                }
            }

            if(bulletType){
                try {
                    bulletImage = ImageIO.read(getClass().getResourceAsStream("/Images/bulletBlue.png"));
                } catch (IOException | IllegalArgumentException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    bulletImage = ImageIO.read(getClass().getResourceAsStream("/Images/bulletOrange.png"));
                } catch (IOException | IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
    }
      
    public boolean isActive() {
        return active;
    }

    public boolean isStopped(){
      return stopped;
    }
    public void changeStopped(){
        stopped = !stopped;
    }

    public boolean getBulletType(){
        return bulletType;
    }

    public boolean wallPortal(boolean portalType){
        if(portalType){
            return wallP1;
        } else{
            return wallP2;
        }
    }

    public void setWallPortal(boolean portalType, boolean value){
        if(portalType){
            wallP1 = value;
        } else{
            wallP2 = value;
        }
    }

    @Override
    public void render(Graphics g, int cameraX, int cameraY) {
        if (active) {
            if (bulletImage != null) {
                g.drawImage(bulletImage, getX() - cameraX, getY() - cameraY, 50, 50, null);
            } else {
                g.setColor(Color.YELLOW);
                g.fillOval(getX() - cameraX, getY() - cameraY, 5, 5);
            }
        }
    }

    void fillRect(int x, int y, int i, int i0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}