import java.awt.*;

public class UIButton {
    private String text;
    public int x;
    public int y;
    public int width;
    private int height;
    private boolean selected = false;

    public UIButton(String text, int x, int y) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = 200;  // Default width
        this.height = 50;  // Default height
    }

    // Setters for dynamic positioning
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    // Getters if needed
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public String getText() { return text; }

    public void draw(Graphics2D g2) {
        // Draw button background
        if (selected) {
            g2.setColor(new Color(100, 149, 237)); // Cornflower blue for selected
        } else {
            g2.setColor(new Color(60, 60, 60, 200)); // Semi-transparent dark
        }
        g2.fillRoundRect(x, y, width, height, 20, 20);

        // Draw border
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(x, y, width, height, 20, 20);

        // Draw text centered in button
        g2.setFont(new Font("Arial", Font.BOLD, 32));
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();
        int textX = x + (width - textWidth) / 2;
        int textY = y + (height + textHeight) / 2 - 6;
        g2.setColor(Color.WHITE);
        g2.drawString(text, textX, textY);
    }
}