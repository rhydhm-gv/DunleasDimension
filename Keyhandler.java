import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyhandler implements KeyListener {
    private Game game;
    // Key states for in-game controls
    private boolean up, down, left, right, jump, level;
    private boolean pause = false;

    public Keyhandler(Game game) {
        this.game = game;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    @Override
public void keyPressed(KeyEvent e) {
    int code = e.getKeyCode();

    // Menu navigation (title, pause, death screens)
    if (game.gameState == game.titleState || game.gameState == game.pauseState || game.gameState == game.deathState) {
        // Move selection up
        if (code == KeyEvent.VK_UP) {
            game.ui.commandNum--;
            if (game.gameState == game.titleState && (game.ui.titleButtons != null && !game.ui.titleButtons.isEmpty()) && game.ui.commandNum < 0) {
                game.ui.commandNum = game.ui.titleButtons.size() - 1;
            } else if (game.gameState == game.pauseState && (game.ui.pauseButtons != null && !game.ui.pauseButtons.isEmpty()) && game.ui.commandNum < 0) {
                game.ui.commandNum = game.ui.pauseButtons.size() - 1;
            } else if (game.gameState == game.deathState && (game.ui.deathButtons != null && !game.ui.deathButtons.isEmpty()) && game.ui.commandNum < 0) {
                game.ui.commandNum = game.ui.deathButtons.size() - 1;
            }
        }
        // Move selection down
        if (code == KeyEvent.VK_DOWN) {
            if (game.gameState == game.titleState && game.ui.titleButtons != null && !game.ui.titleButtons.isEmpty()) {
                game.ui.commandNum = (game.ui.commandNum + 1) % game.ui.titleButtons.size();
            } else if (game.gameState == game.pauseState && game.ui.pauseButtons != null && !game.ui.pauseButtons.isEmpty()) {
                game.ui.commandNum = (game.ui.commandNum + 1) % game.ui.pauseButtons.size();
            } else if (game.gameState == game.deathState && game.ui.deathButtons != null && !game.ui.deathButtons.isEmpty()) {
                game.ui.commandNum = (game.ui.commandNum + 1) % game.ui.deathButtons.size();
            }
        }

    if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
        if (game.gameState == game.titleState && game.ui.titleButtons != null && !game.ui.titleButtons.isEmpty()) {
            handleTitleButton(game.ui.commandNum);
        } else if (game.gameState == game.pauseState && game.ui.pauseButtons != null && !game.ui.pauseButtons.isEmpty()) {
            handlePauseButton(game.ui.commandNum);
        } else if (game.gameState == game.deathState && game.ui.deathButtons != null && !game.ui.deathButtons.isEmpty()) {
            handleDeathButton(game.ui.commandNum);
        }
        return;
}


        // Confirm selection (optional: handle actions here or in UI)
        // if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) { ... }
        return;
    }

        // In-game controls (play state) - just set key states
        if (game.gameState == game.playState) {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                up = true;
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                down = true;
            }
            if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
                left = true;
            }
            if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
                right = true;
            }
            if (code == KeyEvent.VK_SPACE) {
                jump = true;
            }
            if (code == KeyEvent.VK_R) {
                level = true;
                game.por1.reset();
                game.por2.reset();
            }
            if (code == KeyEvent.VK_ESCAPE) {
                if(pause){
                    pause = false;
                    game.gameState = game.playState; // Resume game
                } else{
                    pause = true;
                    game.gameState = game.pauseState;
                }
            }
            // Add other in-game controls as needed
        } else if (game.gameState == game.winState) {
            // Move selection up/down (only one button, but for consistency)
            if (code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN) {
                game.ui.commandNum = 0;
            }
            if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                handleWinButton(game.ui.commandNum);
                return;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        // In-game controls (play state) - unset key states
        if (game.gameState == game.playState) {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                up = false;
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                down = false;
            }
            if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
                left = false;
            }
            if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
                right = false;
            }
            if (code == KeyEvent.VK_SPACE) {
                jump = false;
            }
            if (code == KeyEvent.VK_R) {
                level = false;
            }
            if(code == KeyEvent.VK_H){
                game.p.health = 100;
            }
        }
    }
    private void handleTitleButton(int index) {
    switch (index) {
        case 0: // NEW GAME
            game.reset();
            game.gameState = game.playState; // Start new game
            break;
        case 1: // LOAD GAME
            // Implement load logic if needed
            break;
        case 2: // QUIT
            System.exit(0);
            break;
    }
}

private void handlePauseButton(int index) {
    switch (index) {
        case 0: // RESUME
            game.gameState = game.playState;
            break;
        case 1: // QUIT TO TITLE
            game.gameState = game.titleState;
            break;
    }
}

private void handleDeathButton(int index) {
    switch (index) {
        case 0: // RESTART
            game.startGame();
            game.gameState = game.playState;
            game.level = 0;
            break;
        case 1: // QUIT TO TITLE
            game.gameState = game.titleState;
            break;
    }
}

private void handleWinButton(){

}



private void handleWinButton(int index) {
    // Only one button: RETURN TO TITLE
    game.gameState = game.titleState;
    game.startGame(); // Reset the game (or implement your reset logic)
}

    // Getters for key states
    public boolean isUp() { return up; }
    public boolean isDown() { return down; }
    public boolean isLeft() { return left; }
    public boolean isRight() { return right; }
    public boolean isJump() { return jump; }
    public boolean isLevel() { return level; }
}