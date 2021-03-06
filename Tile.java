import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import javax.swing.*;
public class Tile extends Blob{
    public Tile(String fileName, int w, int h){
        super(fileName, w, h);
    }
    
    public Tile(String fileName, int x, int y, int w, int h){
        super(fileName, x, y, w, h);
    }
    
    public Tile(String fileName, int x, int y, int w, int h, int xPos, int yPos){
        super(fileName, w, h);
        try{
            BufferedImage sheet = ImageIO.read(new File(fileName));
            setImage(sheet.getSubimage(xPos, yPos, w, h));
            setArea(x, y, w, h);
            setVisible(false);
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error in Tile constructor\nError Message: " + e.getMessage(), "Tile Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}