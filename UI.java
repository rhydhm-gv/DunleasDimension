import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class UI {
    public final int titleState = 0;
    public int gameState;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int winState = 4; // Add this line
    public boolean messageOn = false;
    public String message = "";
    private final int x = 480; // Center X for message
    private final int y = 60;  // Y for message, a bit below top
    public int commandNum = 0;
    private Image TitleImage;
    private Image PauseImage;
   int messageCounter = 0;
   Game gp;
   Font arial_40;
   public List<UIButton> titleButtons;
   public List<UIButton> pauseButtons;
   public List<UIButton> deathButtons;
   public List<UIButton> winButtons;
   private List<Image> titleFrames = new ArrayList<>();
   private int titleFrameIndex = 0;
   private int titleFrameCounter = 0;
   private final int titleFrameDelay = 6; // Adjust for animation speed (frames per image)

   public UI(Game gp){
    this.gp = gp;
    arial_40 = new Font("Arial",Font.PLAIN, 40);

    // Load all 34 title frames using resource stream for JAR compatibility
    for (int i = 0; i < 34; i++) {
        try {
            String path = String.format("/Images/Title Screen/pixil-frame-%d.png", i);
            titleFrames.add(ImageIO.read(getClass().getResourceAsStream(path)));
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            titleFrames.add(null); // Placeholder for missing images
        }
    }

    // Initialize buttons for each screen (set x, y to 0)
    titleButtons = new ArrayList<>();
    titleButtons.add(new UIButton("NEW GAME", 0, 0));
    // titleButtons.add(new UIButton("LOAD GAME", 0, 0)); // Remove or comment out this line
    titleButtons.add(new UIButton("QUIT", 0, 0));

    pauseButtons = new ArrayList<>();
    pauseButtons.add(new UIButton("RESUME", 0, 0));
    pauseButtons.add(new UIButton("QUIT TO TITLE", 0, 0));

    deathButtons = new ArrayList<>();
    deathButtons.add(new UIButton("LOCK IN", 0, 0));
    deathButtons.add(new UIButton("QUIT TO TITLE", 0, 0));

    winButtons = new ArrayList<>();
    winButtons.add(new UIButton("RETURN TO TITLE", 0, 0));
   }
    public void draw(Graphics2D g2){
        g2.setFont(arial_40);
        g2.setColor(Color.white);
        // Draw level at top-left, inside visible area
        g2.drawString("Level " + gp.level, 20, 40);
        // Center message text
        if(messageOn == true){
            int msgWidth = g2.getFontMetrics().stringWidth(message);
            g2.drawString(message, (960 - msgWidth) / 2, y);
            messageCounter++;
            if(messageCounter > 120){
                messageCounter = 0;
                messageOn = false;
            }
        }
        if(gp.gameState == gp.pauseState){
            drawPauseScreen(g2);
        }
        if(gp.gameState == gp.titleState){
            drawTitleScreen(g2);
        }
        if(gp.gameState == gp.deathState){
            drawDeathScreen(g2);
        }
        if(gp.gameState == gp.winState){
            drawWinScreen(g2);
        }
    }
    public void showMessage(String text){
        message = text;
        messageOn = true;
    }
    // Draws a rounded, semi-transparent sub window with a white border
    public void drawSubWindow(Graphics2D g2, int x, int y, int width, int height) {
        Color backgroundColor = new Color(0, 0, 0, 200); // semi-transparent black
        Color borderColor = Color.WHITE;
        int arc = 35;
        int borderThickness = 4;

        // Draw background
        g2.setColor(backgroundColor);
        g2.fillRoundRect(x, y, width, height, arc, arc);

        // Draw border
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(borderThickness));
        g2.drawRoundRect(x, y, width, height, arc, arc);
    }

    public void drawPauseScreen(Graphics2D g2){
        // Draw the sub window in the center of the screen
        int swX = 230;
        int swY = 90;
        int swWidth = 500;
        int swHeight = 300;
        drawSubWindow(g2, swX, swY, swWidth, swHeight);

        // Center "PAUSED" text
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
        g2.setColor(Color.WHITE);
        String pauseText = "PAUSED";
        int pauseTextWidth = g2.getFontMetrics().stringWidth(pauseText);
        g2.drawString(pauseText, (960 - pauseTextWidth) / 2, swY + 100);

        // Draw buttons centered below, wider for long text
        int buttonWidth = 320; // Increased width for "QUIT TO TITLE"
        int buttonStartY = swY + 150;
        for (int i = 0; i < pauseButtons.size(); i++) {
            pauseButtons.get(i).setSelected(i == commandNum);
            pauseButtons.get(i).x = (960 - buttonWidth) / 2;
            pauseButtons.get(i).y = buttonStartY + i * 60;
            pauseButtons.get(i).width = buttonWidth; // Make sure UIButton supports this
            pauseButtons.get(i).draw(g2);
        }
    }
    public void drawTitleScreen(Graphics2D g2){
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,96F));
        // Draw the current frame
        Image frame = titleFrames.get(titleFrameIndex);
        if (frame != null) {
            g2.drawImage(frame, 0, 0, 960, 480, null);
        }


        // Animate: advance frame every titleFrameDelay calls
        titleFrameCounter++;
        if (titleFrameCounter >= titleFrameDelay) {
            titleFrameCounter = 0;
            titleFrameIndex = (titleFrameIndex + 1) % titleFrames.size();
        }

        // Draw buttons centered vertically
        int buttonStartY = 250;
        for (int i = 0; i < titleButtons.size(); i++) {
            titleButtons.get(i).setSelected(i == commandNum);
            titleButtons.get(i).x = (960 - 200) / 2;
            titleButtons.get(i).y = buttonStartY + i * 60;
            titleButtons.get(i).draw(g2);
        }
    }

    public void drawDeathScreen(Graphics2D g2){
        // Draw a semi-transparent black background
        Color prevColor = g2.getColor();
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, 960, 480);

        // Draw "YOU DIED" in bold, large font, centered
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
        g2.setColor(Color.RED);
        String text = "YOU DIED";
        int textWidth = g2.getFontMetrics().stringWidth(text);
        int x = (960 - textWidth) / 2;
        int y = 200;
        g2.drawString(text, x, y);

        // Draw buttons centered below, wider for long text
        int buttonWidth = 320; // Increased width
        int buttonStartY = 300;
        for (int i = 0; i < deathButtons.size(); i++) {
            deathButtons.get(i).setSelected(i == commandNum);
            deathButtons.get(i).x = (960 - buttonWidth) / 2;
            deathButtons.get(i).y = buttonStartY + i * 60;
            deathButtons.get(i).width = buttonWidth; // Make sure UIButton supports this
            deathButtons.get(i).draw(g2);
        }

        g2.setColor(prevColor); // Restore previous color
    }
    public void drawWinScreen(Graphics2D g2){
        // Draw a semi-transparent background
        Color prevColor = g2.getColor();
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, 960, 480);

        // Draw "YOU WIN!" in bold, large font, centered
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
        g2.setColor(Color.GREEN);
        String text = "YOU WIN!";
        int textWidth = g2.getFontMetrics().stringWidth(text);
        int x = (960 - textWidth) / 2;
        int y = 200;
        g2.drawString(text, x, y);

        // Draw button centered below
        int buttonStartY = 300;
        for (int i = 0; i < winButtons.size(); i++) {
            winButtons.get(i).setSelected(i == commandNum);
            winButtons.get(i).x = (960 - 200) / 2;
            winButtons.get(i).y = buttonStartY + i * 60;
            winButtons.get(i).draw(g2);
        }

        g2.setColor(prevColor); // Restore previous color
    }
}

