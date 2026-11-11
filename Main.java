
import javax.swing.*;
 
public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Dunleas Dimensions");
        Game game = new Game();
        
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        // frame.setUndecorated(true); // Optional: removes window borders

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        game.requestFocusInWindow();

        game.startGame();
    }
}