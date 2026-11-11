
public class CollisionDetection {
    
    Game gp;

    public CollisionDetection(Game gp){
        this.gp = gp;
    }


    public boolean checkTileAt(PlayerTemplate entity, int testX, int testY, int level) {
        int tileSize = 32; // Adjust if your tiles are a different size
        int width = entity.getBounds().width;
        int height = entity.getBounds().height;

        // Check all four corners of the entity's bounding box at the test position
        int leftCol   = testX / tileSize;
        int rightCol  = (testX + width - 1) / tileSize;
        int topRow    = testY / tileSize;
        int bottomRow = (testY + height - 1) / tileSize;

        int[][] map = gp.levelManager.getLevel(level).getMap();

        // Optionally: Check each corner
        int[] cols = {leftCol, rightCol};
        int[] rows = {topRow, bottomRow};
        for (int col : cols) {
            for (int row : rows) {
                if (row >= 0 && row < map.length && col >= 0 && col < map[0].length) {
                    int tileNum = map[row][col];
                    if (gp.tileM.tile[tileNum].collision) {
                        return true; // Collision detected
                    }
                }
            }
        }
        return false; // No collision
    }

}
