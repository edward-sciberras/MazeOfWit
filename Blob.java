import java.awt.image.*;
import javax.imageio.*;
import java.awt.*;
import java.io.*;
import javax.swing.*;
public class Blob{
    protected BufferedImage image;
    protected Rectangle area;
    protected boolean visible;
    protected int ax, ay, width, height;

    // Contructor with parameters of fileName, width and height
    public Blob(String fileName, int w, int h){
        try{    
            image = ImageIO.read(new File(fileName));
            area = new Rectangle(0, 0, w, h);
            //visible = false;
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error in Blob constructor\nError Message: " + e.getMessage(), "Blob Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Contructor with parameters of fileName, x value, y value, width and height
    public Blob(String fileName, int x, int y, int w, int h){
        try{    
            image = ImageIO.read(new File(fileName));
            area = new Rectangle(x, y, w, h);
            //visible = false;
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error in Blob constructor\nError Message: " + e.getMessage(), "Blob Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Getters
    public int getX(){
        return (int)area.getX();
    }

    public int getY(){
        return (int)area.getY();
    }

     public Rectangle getArea(){
         return area;
    }

    public boolean getVisible(){
        return visible;
    }

    // Setters
    public void setImage(BufferedImage i){
        image = i;
    }

    public void setImage(String fileName, int w, int h){
        try{
            image = ImageIO.read(new File(fileName));
            area = new Rectangle(0, 0, w, h);
        } catch (Exception e){
            System.out.print("Error in Setting Image - " + e.getMessage());
        }
    }

    public void setLocation(int x, int y){
        area.setLocation(x, y);
    }

    public void setVisible(boolean v){
        visible = v;
    }

    public void setArea(int x, int y, int w, int h){
        ax = x;
        ay = y;
        width = w;
        height = h;
    }

    // Draws the image
    public void paint(Graphics g){
        if(visible && image != null){
            g.drawImage(image, (int)area.getX(), (int)area.getY(), null);
        }
    }
}