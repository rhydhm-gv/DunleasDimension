import java.awt.*;

public class Door extends PlayerTemplate {

    private Game g;
    private LevelManager levelManager = new LevelManager();

    public Door(int x, int y, boolean visibility, Image image, int speed, Game g){
        super(x,y,visibility,image,0,10,10);
        this.g = g;
    }
    
    public void DoorTouched(){
        if(g.level == 0){
            g.level = 1;
        } else if(g.level == 1){
            g.level = 2;
        } else if(g.level == 2){
            g.level = 3;
        } else if(g.level == 3){
            g.level = 4;
        } else if(g.level == 4){
            g.gameState = g.winState;
        }
        g.d.setCoords(levelManager.levels.get(0).getEndX(), levelManager.levels.get(0).getEndY());
    }
    
}
