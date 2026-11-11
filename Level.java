public class Level {
    private int[][] map;
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public Level(int[][] map, int startX, int startY, int endX, int endY) {
        this.map = map;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        
    }

    public int[][] getMap() {
        return map;
    }
    public int getStartX() {
        return startX;
    }   
    public int getStartY() {
        return startY;
    }
    public int getEndX(){
        return endX;
    }
    public int getEndY(){
        return endY;
    }
}
