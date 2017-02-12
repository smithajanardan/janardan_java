import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

// This series of classes demonstrates how to create
// different color objects. The various colors are
// displayed in a panel.
public class ColorExampleMain {
    public static void main(String[] args) { 
        ColorFrame f = new ColorFrame(); 
        f.start(); 
             
    } 
}

class ColorFrame{
    private JFrame frame;
    private JPanel panel;
    
    public ColorFrame() {
        //  set up frame 
        frame = new JFrame();
        frame.setTitle("Color Samples");
        frame.setSize(400, 400);
        frame.setLocation(200, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        panel = new AlphaColorPanel();
        // panel = new ColorPanel();
        frame.add(panel);
    }
    
    
    public void start(){
        frame.setVisible(true);
    }
}


// this class shows the effects of changint the
// alpha value or transparency on Color objects.
class AlphaColorPanel extends JPanel{
    
    // draw three circles that overlap, fill with colors
    // with non opaque alpha
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        
        Rectangle2D rect = new Rectangle2D.Double(0, 0, getWidth() / 2, getHeight());
        g2.setColor(Color.GRAY);
        g2.fill(rect);
        
        int width = getWidth() / 3 * 2;
        int height = getHeight() / 3 * 2;
        Ellipse2D circ1 = new Ellipse2D.Double(getWidth() / 6, 0, width, height);
        Ellipse2D circ2 = new Ellipse2D.Double(0, getHeight() / 3, width, height);
        Ellipse2D circ3 = new Ellipse2D.Double(getWidth() / 3, getHeight() / 3, width, height);
        int intensity = 255;
        int transparency = 100;
        g2.setColor(new Color(intensity, 0, 0, transparency));
        g2.fill(circ1);
        g2.setColor(new Color(0, intensity, 0, transparency));
        g2.fill(circ2);
        g2.setColor(new Color(0, 0, intensity, transparency));
        g2.fill(circ3);
    }
}
