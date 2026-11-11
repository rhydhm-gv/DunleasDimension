import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;


public class Game extends JPanel implements ActionListener, MouseMotionListener, MouseListener {
    private int cursorX = 0;
    private int cursorY = 0;
    private int PLAYER_SIZE = 30;
    private final Timer timer;
    private boolean isRunning = false;
    //Images
    private Image playerImage;
    private Image portalImageBlue;
    private Image portalImageOrange;
    private Image doorImage;
    //entities/objects
    public Player p;//change to private later
    public bullet b;
    public Portal por1;
    public Portal por2;
    public Door d;
    //components
    public MapManager tileM;
    public LevelManager levelManager;
    public CollisionDetection collisionD = new CollisionDetection(this);
    private Keyhandler keyH;
    public int level;
    //arraylists

    ArrayList<PlayerTemplate> teleportable = new ArrayList<PlayerTemplate>();
    ArrayList<PlayerTemplate> entities = new ArrayList<PlayerTemplate>();
    public UI ui;
    //camera variables
    private int cameraX = 0;
    private int cameraY = 0;

    //gamestates
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int deathState = 3;
    public final int winState = 4;
    //timer

    private long startTime = 0;
    private long elapsedTime = 0;
    private long lastHealTime = 0;
    private int minutes = 0;

    private boolean doortouched = false;


    public Game() {
        setPreferredSize(new Dimension(960, 480));
        setBackground(Color.BLACK);
        setFocusable(true);
        requestFocusInWindow();
        keyH = new Keyhandler(this);
        addKeyListener(keyH);
        addMouseMotionListener(this);
        addMouseListener(this);
        
        // Make cursor visible
        setCursor(Cursor.getDefaultCursor());
        
        timer = new Timer(16, this); // roughly 60 FPS

        try {
            playerImage = ImageIO.read(getClass().getResourceAsStream("/Images/Minion Design 24x24 1.01.png"));
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        try {
            doorImage = ImageIO.read(getClass().getResourceAsStream("/Images/Door.png"));
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        // If you have portal images, load them the same way:
        try {
            portalImageBlue = ImageIO.read(getClass().getResourceAsStream("/Images/portalBlue.png"));
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        try {
            portalImageOrange = ImageIO.read(getClass().getResourceAsStream("/Images/portalOrange.png"));
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        levelManager = new LevelManager();
        ui = new UI(this);
        p = new Player(levelManager.levels.get(level).getStartX(), levelManager.levels.get(level).getStartY(), true, playerImage,5, this);
        por1 = new Portal(2000,2050,false, portalImageBlue, true, this);
        por2 = new Portal(2050,2000,false, portalImageOrange, false, this);
        d = new Door(levelManager.levels.get(level).getEndX(), levelManager.levels.get(level).getEndY(), true, doorImage,0,this);
        b = new bullet(5,5,false, 30,this);
        tileM = new MapManager(this);
        teleportable.add(p);
        level = 0;
    }
    public void setUpGame(){
        gameState = titleState;
    }
    public void startGame() {
        gameState = 0; // reset to title state
        isRunning = true;
        timer.start();
        level = 0;
        startTime = System.currentTimeMillis();
        lastHealTime = startTime; // reset heal timer
    }

    public void paintComponent(Graphics g) {

        // super.paintComponent(g);
        // Graphics2D g2 = (Graphics2D) g;

        // double scale = Math.min(getWidth() / 960.0, getHeight() / 480.0);
        // int xOffset = (int)((getWidth() - 960 * scale) / 2);
        // int yOffset = (int)((getHeight() - 480 * scale) / 2);

        // g2.translate(xOffset, yOffset);
        // g2.scale(scale, scale);

        Graphics2D g2 = (Graphics2D) g;
        if(gameState == titleState || gameState == pauseState || gameState == winState){
            ui.draw(g2);
        }else if(gameState == deathState){
            ui.drawDeathScreen(g2);
        }
        else{
        
            super.paintComponent(g);

            // int tileSize = 32; // or whatever your tile size is
            // int mapWidth = levelManager.levels.get(level).getMap()[0].length * tileSize;
            // int mapHeight = levelManager.levels.get(level).getMap().length * tileSize;
            // int screenWidth = getWidth();
            // int screenHeight = getHeight();

            cameraX = p.getX() - getWidth() / 2 + 25;
            cameraY = p.getY() - getHeight() / 2 + 25;

            // cameraX = p.getX() - screenWidth / 2 + 25; //assuming player is 50 x 50 pixels
            // cameraY = p.getY() - screenHeight / 2 + 25; //assuming player is 50 x 50 pixels

            // cameraX = Math.max(0, Math.min(0, mapWidth - screenWidth));
            // cameraY = Math.max(0, Math.min(0, mapHeight - screenHeight));

            // Draw game status
            tileM.draw(g2, level,cameraX, cameraY);
            
            g.setColor(Color.WHITE);
            g.drawString("Use arrow keys to move", 10, 20);
            g.drawString("Cursor: X=" + cursorX + ", Y=" + cursorY, 10, 40);
            g.drawString("Level: " + level, 10, 60);
            g.drawString("Health: " + p.health,10,80);
            g.drawString("w,a,d to move left and right for portals", 10,120);
            g.drawString("GameState: "+gameState, 10,100);
            g.drawRect(p.getX() - cameraX +10, p.getY() - 15 - cameraY,30,10);
            g.setColor(Color.GREEN);
            g.fillRect(p.getX() - cameraX +10, p.getY() - 15 - cameraY,(int)((p.health/100.0)*30.0),10);
            p.render(g,cameraX, cameraY);
            por1.render(g, cameraX, cameraY);
            por2.render(g, cameraX, cameraY);
            b.render(g, cameraX, cameraY);
            d.render(g, cameraX, cameraY);

            long totalSeconds = elapsedTime / 1000;
            int minutes = (int)(totalSeconds / 60);
            int seconds = (int)(totalSeconds % 60);
            String timeString = String.format("Time: %02d:%02d", minutes, seconds);
            g2.drawString(timeString, getWidth()/2 -60, 40);
        }
    
}
    public void actionPerformed(ActionEvent e) {
        if (gameState == playState && gameState != pauseState) {
            elapsedTime = accumulatedTime + (System.currentTimeMillis() - startTime);

            long now  = System.currentTimeMillis();
            if(now - lastHealTime >= 5000){
                p.heal();
                lastHealTime = now; // reset heal timer
            }

            por1.update(); // << animate portal 1
            por2.update(); // << animate portal 2

            p.update(keyH);
            keyPressed();

            // --- Only check player for door collision and handle level transition ---
            if (d.getBounds().intersects(p.getBounds())) {
                if (!doortouched) {
                    doortouched = true;
                    if (level == levelManager.levels.size() - 1) {
                        gameState = winState;
                    } else {
                        level++;
                        if (level >= levelManager.levels.size()) {
                            level = levelManager.levels.size() - 1;
                        }
                        // Move player to new level's start position
                        p.setCoords(levelManager.levels.get(level).getStartX(), levelManager.levels.get(level).getStartY());
                        d.setCoords(levelManager.levels.get(level).getEndX(), levelManager.levels.get(level).getEndY());
                    }
                    return; // Exit immediately after transition
                }
            } else {
                doortouched = false;
            }

            // --- Keep portal logic for all teleportable entities ---
            for(PlayerTemplate entity: teleportable){
                if (por1.getBounds().intersects(entity.getBounds())) {
                    por1.teleport(entity, por2, p.getDirection());
                }
                if(por2.getBounds().intersects(entity.getBounds())){
                    por2.teleport(entity, por1, p.getDirection());
                }
            }

            if (b != null && b.isActive() && !b.isStopped()) {
                b.update(level);
            } else if(b.isStopped()){
                int bx = (int) b.realX;
                int by = (int) b.realY;
                if(b.getBulletType()){
                    if(por2.getX() != b.getX() || por2.getY() != b.getY()){
                        por1.setCoords(bx-15, by-25);
                    }
                } else {
                    if(por2.getX() != b.getX() || por2.getY() != b.getY()){
                        por2.setCoords(bx-15, by-25);
                    }
                }
            }

            if(p.health == 0){
                gameState = deathState;
            }
            repaint();
        } else if (gameState == pauseState || gameState == titleState || gameState == deathState || gameState == winState) {
            // Still repaint so UI screens are shown
            repaint();
        }
    }


    // Key controls
    public void keyPressed() {
        if (keyH.isLevel()){
            level++;
             if (level >= levelManager.levels.size()) {
                level = 0; // or keep at max, or handle as you wish
            }
            p.setCoords(levelManager.levels.get(level).getStartX(), levelManager.levels.get(level).getStartY());
            d.setCoords(levelManager.levels.get(level).getEndX(), levelManager.levels.get(level).getEndY());
        }
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
    
    public void mouseMoved(MouseEvent e) {
        cursorX = e.getX();
        cursorY = e.getY();
        repaint();
    }
    
    public void mouseDragged(MouseEvent e) {
        cursorX = e.getX();
        cursorY = e.getY();
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        cursorX = e.getX();
        cursorY = e.getY();
        repaint();
    }

    private int screenToWorldX(int screenX) {
        return screenX + cameraX;
    }
    private int screenToWorldY(int screenY) {
        return screenY + cameraY;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(gameState == playState) {
            cursorX = e.getX();
            cursorY = e.getY();

            // double scale = Math.min(getWidth() / 960.0, getHeight() / 480.0);
            // int xOffset = (int)((getWidth() - 960 * scale) / 2);
            // int yOffset = (int)((getHeight() - 480 * scale) / 2);

            // // Convert screen (mouse) coordinates to game coordinates
            // int scaledX = (int)((e.getX() - xOffset) / scale);
            // int scaledY = (int)((e.getY() - yOffset) / scale);

            // cursorX = scaledX;
            // cursorY = scaledY;
      
            // Shoot a bullet toward the cursor position
            int playerCenterX = p.getX() + 15; // if player is 50x50
            int playerCenterY = p.getY() + 15;
            int worldCursorX = screenToWorldX(cursorX);
            int worldCursorY = screenToWorldY(cursorY);
            if(SwingUtilities.isRightMouseButton(e)){
                b.trigger(playerCenterX, playerCenterY, worldCursorX, worldCursorY, true);
                por2.setVisibility(true);
            } else if(SwingUtilities.isLeftMouseButton(e)){
                b.trigger(playerCenterX, playerCenterY, worldCursorX, worldCursorY, false);
                por1.setVisibility(true);
            }
            repaint();
            
        }
    }

    public void reset(){
        p.setCoords(levelManager.levels.get(0).getStartX(), levelManager.levels.get(0).getStartY());
        p.health = 100;
        por1.reset();
        por2.reset();
        level = 0;
        d.setCoords(levelManager.levels.get(0).getEndX(), levelManager.levels.get(0).getEndY());
        // Reset timer
        startTime = System.currentTimeMillis();
        elapsedTime = 0;
        accumulatedTime = 0;
    }
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    private long accumulatedTime = 0; // Add this field

    public void resumeGame() {
        previousGameState = gameState;
    gameState = playState;
    startTime = System.currentTimeMillis();

    }
    // When resuming from pause:

    private int previousGameState = titleState;

}
