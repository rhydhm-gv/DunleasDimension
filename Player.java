import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player extends PlayerTemplate{

    public final int screenX;
    public final int screenY;
    private Game g;

    private double velocityY = 0;
    private boolean onGround = false;
    private final double GRAVITY = 0.8;
    private final double JUMP_STRENGTH = -9;
    private double dx = 0;

    public double health = 100.0;
    private int fallStartY = 0;
    private boolean wasOnGround = false;

    // Animation
    private Image[] walkRightFrames;
    private Image[] walkLeftFrames;
    private int frameIndex = 0;
    private int frameCounter = 0;
    private final int frameDelay = 6;
    private boolean facingRight = true; // true = right, false = left

    // GAME STATE
    
    // normal x and y are world coordinates
    // screenX and screenY are screen coordinates for rendering camera type view
    public Player(int x, int y, boolean visibility, Image image, int speed, Game g){
        super(x,y,visibility,image,speed,50,50);
        this.g = g;
        screenX = 480/2 -16;
        screenY = 960/2 -16;

        // Load right walking frames
        walkRightFrames = new Image[4]; // adjust if you have more/less frames
        for (int i = 0; i < walkRightFrames.length; i++) {
            try {
                walkRightFrames[i] = ImageIO.read(getClass().getResourceAsStream("/Images/Player/MinionWalking" + i + ".png"));
            } catch (IOException | IllegalArgumentException e) {
                walkRightFrames[i] = null;
            }
        }
        // Load left walking frames
        walkLeftFrames = new Image[4];
        for (int i = 0; i < walkLeftFrames.length; i++) {
            try {
                walkLeftFrames[i] = ImageIO.read(getClass().getResourceAsStream("/Images/Player/MinionWalkingLeft" + i + ".png"));
            } catch (IOException | IllegalArgumentException e) {
                walkLeftFrames[i] = null;
            }
        }
    }


    public void update(Keyhandler keyH){
        boolean moving = false;

        if (keyH.isLeft()) {
            dx = -5;
            DirectionLeft();
            facingRight = false;
            moving = true;
        }
        if (keyH.isRight()) {
            dx = 5;
            DirectionRight();
            facingRight = true;
            moving = true;
        }

        

        double deceleration = .95;

        if(!keyH.isLeft() && !keyH.isRight())
        {
            dx*=deceleration;

            if (Math.abs(dx) < .01)
            {
                dx = 0;
            }
        }

        // Animation frame update
        if (moving) {
            frameCounter++;
            if (frameCounter >= frameDelay) {
                frameIndex = (frameIndex + 1) % walkRightFrames.length;
                frameCounter = 0;
            }
        } else {
            frameIndex = 0; // idle frame is first right frame
        }

        if (keyH.isJump() && onGround) { // You need to implement isJump() in Keyhandler (e.g. space bar)
            velocityY = JUMP_STRENGTH;
            onGround = false;
        }

        if (!onGround && wasOnGround) {
            fallStartY = getY();
        }

            // Apply gravity
        velocityY += GRAVITY;

        // Try to move vertically
        int newY = (int)(getY() + velocityY);

        // --- Horizontal movement (X) ---
        if (!g.collisionD.checkTileAt(this, getX() + (int)dx, getY(), g.level)) {
            changeX((int)dx);
        }


        //vertical movement
        if (!g.collisionD.checkTileAt(this, getX(), newY, g.level)) {
            setCoords(getX(), newY);
            onGround = false;
        } else {
            if (velocityY > 0) { // Only set onGround if falling down
                onGround = true;

                // --- FALL DAMAGE LOGIC ---
                // Only apply if you were falling fast enough (not just stepping down)
                if (!wasOnGround && velocityY > 4) { // 4 is a reasonable threshold, tweak as needed
                    int fallDistance = Math.abs(getY() - fallStartY);
                    if (fallDistance > 50) {
                        double damage = ((fallDistance / 32) * 1.1);
                        health -= damage;
                        if (health < 0) health = 0;
                    }
                }
                // --- END FALL DAMAGE LOGIC ---


                // Snap player's feet to the top of the tile
                //int tileSize = 32;
                int playerHeight = 30; // matches getBounds()
                int playerBottom = getY() + playerHeight;
                // int tileRow = playerBottom / tileSize;
                int snappedY = playerBottom - playerHeight; 
                // int snappedY = tileRow * tileSize - playerHeight;
                setCoords(getX(), snappedY);
            }
            velocityY = 0;

            
        }
        wasOnGround = onGround;

    }

    @Override
    public void render(Graphics g, int cameraX, int cameraY) {
        Image currentFrame;
        if (dx == 0) {
            currentFrame = walkRightFrames[0]; // idle is first right frame
        } else if (facingRight) {
            currentFrame = walkRightFrames[frameIndex];
        } else {
            currentFrame = walkLeftFrames[frameIndex];
        }
        if (currentFrame != null) {
            g.drawImage(currentFrame, getX() - cameraX, getY() - cameraY, 50, 50, null);
        } else {
            g.setColor(Color.YELLOW);
            g.fillRect(getX() - cameraX, getY() - cameraY, 30, 30);
        }
    }

    public void heal() {
        if (health <= 95) {
            health += 10;
        }
    }

    public void resetFall(int newy){
        fallStartY = newy;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), 30, 30); // Adjusted for player size
    }

    
}