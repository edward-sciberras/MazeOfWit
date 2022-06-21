public class Sprite extends Blob{
    private int dir;
    
    // Contructor with parameters of fileName, width and height
    public Sprite(String fileName, int w, int h){
        super(fileName, w, h);
    }
    
    // Contructor with parameters of fileName, x value, y value, width and height
    public Sprite(String fileName, int x, int y, int w, int h){
        super(fileName, x, y, w, h);
    }
    
    // Move Controls
    public void moveLeft(int px){
        int newX = (int)getX() - px;
        setLocation(newX, (int)getY());
    }
    
    public void moveRight(int px){
        int newX = (int)getX() + px;
        setLocation(newX, (int)getY());
    }
    
    public void moveUp(int px){
        int newY = (int)getY() - px;
        setLocation((int)getX(), newY);
    }
    
    public void moveDown(int px){
        int newY = (int)getY() + px;
        setLocation((int)getX(), newY);
    }
    
    // Getters
    public int getDir(){
        return dir;
    }
    
    // Setters
    public void setDir(int d){
        dir = d;
    }
}