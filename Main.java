public class Main {
    public static void main (String args[]){
        GameFrame f = new GameFrame("Maze of Wit", 640, 480);
        f.setVisible(true);
        f.setResizable(false);
        f.setLocationRelativeTo(null); // centre the frame when it comes up
    }
}