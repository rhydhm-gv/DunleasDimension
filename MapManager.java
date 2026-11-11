import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MapManager {
    Game gp;
    Tile[] tile;
    int[][] map = new int[10][20]; // Assuming a 10x20 map, adjust as needed
    // Constructor to initialize the MapManager with a reference to the Game instance
    LevelManager levelManager;

    public MapManager(Game gp){
        this.gp = gp;
        tile = new Tile[20];
        getTileImage();
        levelManager = new LevelManager();
    }
    public void getTileImage(){
        try {
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/Images/Tiles/Ruined_Brick_Tile_3.png.png"));
            tile[0].collision = true;

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/Images/Tiles/Brick Tile.png"));
            tile[1].collision = true; 

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/Images/Tiles/Wall (1).png"));

            tile[3] = new Tile();
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/Images/Tiles/Wall.png"));

            tile[4] = new Tile();
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/Images/Tiles/grass block.png"));
            tile[4].collision = true;

            tile[5] = new Tile();
            tile[5].image = ImageIO.read(getClass().getResourceAsStream("/Images/Tiles/Ruined Cave Tile (1).png"));
            tile[5].collision = true;

            tile[6] = new Tile();
            tile[6].image = ImageIO.read(getClass().getResourceAsStream("/Images/Tiles/Ruined Cave Tile (2).png"));
            tile[6].collision = true;

            tile[7] = new Tile();
            tile[7].image = ImageIO.read(getClass().getResourceAsStream("/Images/Tiles/Ruined Cave Tile (3).png"));
            tile[7].collision = true;

            tile[8] = new Tile();
            tile[8].image = ImageIO.read(getClass().getResourceAsStream("/Images/Tiles/Ruined Cave Tile (1) flip.png"));
            tile[8].collision = true;

            tile[9] = new Tile();
            tile[9].image = ImageIO.read(getClass().getResourceAsStream("/Images/Tiles/Ruined Cave Tile (2) flip.png"));
            tile[9].collision = true;

            
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void draw(Graphics2D g2, int level, int cameraX, int cameraY) {
        map = levelManager.getLevel(level).getMap();
        for(int i=0; i<map.length; i++){
            for(int j =0; j<map[i].length; j++){
                int tileIndex = map[i][j];
                if(tileIndex >= 0 && tileIndex < tile.length){
                    int drawX = j * 32 - cameraX; // Assuming each tile is 32x32 pixels
                    int drawY = i * 32 - cameraY; 
                    g2.drawImage(tile[tileIndex].image, drawX, drawY, 32, 32, null);
                }
            }
        }
        
    }

}
