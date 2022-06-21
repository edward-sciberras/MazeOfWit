import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;
public class Player extends Sprite{
    private BufferedImage up, down, left, right;

    private final String FACING_UP = "Sprites/Facing-Up.png";
    private final String FACING_DOWN = "Sprites/Facing-Down.png";
    private final String FACING_LEFT = "Sprites/Facing-Left.png";
    private final String FACING_RIGHT = "Sprites/Facing-Right.png";

    public Player(String fileName, int x, int y, int w, int h){
        super(fileName, x, y, w, h);

        //loading in sprites for player
        try {
            up = ImageIO.read(new File(FACING_UP));
            down = ImageIO.read(new File(FACING_DOWN));
            left = ImageIO.read(new File(FACING_LEFT));
            right = ImageIO.read(new File(FACING_RIGHT));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading sprites of character\nError Message: " + e.getMessage(), "Character Sprites Loading Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void moveUp(){
        super.moveUp(32);

        image = up;
    }

    public void moveRight(){
        super.moveRight(32);

        image = right;
    }

    public void moveDown(){
        super.moveDown(32);

        image = down;
    }

    public void moveLeft(){
        super.moveLeft(32);

        image = left;
    }

    @Override
    public void paint(Graphics g){
        if(visible && image != null){
            g.drawImage(image, (int)area.getX(), (int)area.getY(), null);
        }
    }
}