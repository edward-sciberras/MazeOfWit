import java.awt.*;
import java.awt.event.*;

public abstract class ClosingFrame extends Frame{
    public ClosingFrame(String title, int width, int height){
        super(title);
        setSize(width, height);

        addWindowListener(new MyWindowAdapter(this)); //calling instance

    }

    public abstract void onClosing();
}

class MyWindowAdapter extends WindowAdapter {

    ClosingFrame frame; // creating pointer to above class
    
    MyWindowAdapter (ClosingFrame f) {
        frame = f;
    }
    
    @Override
    public void windowClosing(WindowEvent e){
        frame.onClosing();
    }

}