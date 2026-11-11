import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Portal extends PlayerTemplate {
    
    private Image[] animationFrames = new Image[8];
    private int currentFrame = 0;
    private int frameCounter = 0;
    private int frameDelay = 5; // controls speed of animation
    private boolean type; // true for blue and false for orange
    private Game gp;

    public Portal(int x, int y, boolean visibility, Image portalImage, boolean type, Game gp){
        super(x,y,visibility,portalImage, 0,30,50);
        this.gp = gp;
        this.type = type;
        loadAnimationFrames();
    }
    
    public void teleport(PlayerTemplate entity, Portal p, String direction){
        int x, y;
        if(getVisibility() && p.getVisibility()){
            boolean isWallPortal = gp.b.wallPortal(type);
            switch(direction){
                case "left":
                    x = p.getX() + (isWallPortal ? 30 : -30);
                    y = p.getY();
                    break;
                case "right":
                    x = p.getX() + (isWallPortal ? -30 : 30);
                    y = p.getY();
                    break;
                default:
                    x = p.getX() + (isWallPortal ? -30 : 30);
                    y = p.getY();
                    break;
            }
            // Only teleport if not colliding with a wall
            if(!gp.collisionD.checkTileAt(entity, x, y, gp.level)){
                entity.setCoords(x, y);
                gp.p.resetFall(y);
            }
            // Reset wall portal flag after teleport
            gp.b.setWallPortal(type, false);
        }
    }

    private void loadAnimationFrames() {
        try {
            for (int i = 0; i < 8; i++) {
                if(type){
                    animationFrames[i] = ImageIO.read(getClass().getResourceAsStream("/Images/Blue_Portal_animation" + i + ".png"));
                } else{
                    animationFrames[i] = ImageIO.read(getClass().getResourceAsStream("/Images/Orange Portal Complete" + i + ".png"));
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (!getVisibility()) return;

        frameCounter++;
        if (frameCounter >= frameDelay) {
            currentFrame = (currentFrame + 1) % animationFrames.length;
            frameCounter = 0;
        }
    }

    @Override
    public Image getImage() {
        if (!getVisibility()) return null;
        return animationFrames[currentFrame];
    }

    @Override
    public void render(Graphics g, int cameraX, int cameraY) {
        if (getVisibility() && getImage() != null) {
            g.drawImage(getImage(), getX() - cameraX, getY() - cameraY, 50, 50, null);
        }
    }

    public void reset(){
        setVisibility(false);
        setCoords(2000,2050);
    }
}


