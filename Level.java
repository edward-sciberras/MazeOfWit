import java.io.*;
import javax.swing.*;
public class Level{
    private String fileName;

    private char[][] tile; // screen is 480 by 640 so 15 and 20 (tile is 32x32)

    public void loadLevel(String fileName){
        String line = "";
        try{
            tile = new char[15][20]; 
            BufferedReader f = new BufferedReader(new FileReader(fileName));
            for(int i = 0; i < 15; i++){ // loop for y
                line = f.readLine();
                for(int j = 0; j < 20; j++){ // loop for x
                    tile[i][j] = line.charAt(j); // loading char into array
                    //System.out.print(tile[i][j]);
                }
                //System.out.println();
            }
            //System.out.println("\n\n");
            f.close();
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error loading in level\nError Message: " + e.getMessage(), "Level Loading Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Getter
    public char getTile(int x, int y){
        return tile[y][x];
    }

    // Setter
    public void setTile(int x, int y, char c){
        tile[y][x] = c;
    }
}