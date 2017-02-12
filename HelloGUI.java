package ClassExamples;

import java.awt.*;
import javax.swing.*;

public class HelloGUI {

    public static void main(String[] args) {
        Baz b = new Baz();
        System.out.println(b.toString());
        
        HelloFrame f = new HelloFrame();
        f.start();
    }
    
} // end of HelloGUI

class HelloFrame extends JFrame {
    
    private HelloPanel thePanel;
    
    public HelloFrame() {
        setTitle("Our First Frame");
        setSize(400, 400); // width, height in pixels
        setLocation(20, 60); // x, y
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        
        thePanel = new HelloPanel();
        add(thePanel);
        
    }
    
    public void start() {
        setVisible(true);

    }
}

class HelloPanel extends JPanel {
    
    private int timesPCcalled;
    
    public HelloPanel() {
        setBackground(Color.ORANGE);        
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // call parent version of paintComponent
        timesPCcalled++;
        g.setFont(new Font("Serif", Font.PLAIN, 40));
        g.drawString("paintComponentCalled: " + timesPCcalled, 
                20, 40); // string, x, y of baseline
        
        int width = getWidth() - 40;
        int height = getHeight() - 40;
        
    
        g.drawRect(20, 20, width, height);
        g.setColor(Color.PINK);
        g.fillRect(20, 20, width, height); // x, y, width, height
        g.setColor(Color.BLACK);
 
    }
}

 class Baz {
    public String toString() {
        return "I am a Baz.";
    }
}
