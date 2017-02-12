package ClassExamples;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Smiley {
    public static void main(String[] args) {   
        
        SmileyFrame f = new SmileyFrame();
        f.start();
    }

} // end of HelloGUI

class SmileyFrame extends JFrame {

    private SmileyPanel thePanel;

    public SmileyFrame() {
        setTitle("Smiley Faces");
        setLocation(20, 60); // x, y
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

        thePanel = new SmileyPanel();
        add(thePanel);
        setResizable(false);
        pack();
    }

    public void start() {
        setVisible(true);

    }
}

class SmileyPanel extends JPanel {

    private static final int SIZE = 600;
    private static final double FACE_SIZE = SIZE / 4;
    private static final double EYE_WIDTH = FACE_SIZE / 10.0;
    private static final double SMILE_SIZE = FACE_SIZE * 2 / 3;
    private static final double EYE_DISTANCE =FACE_SIZE / 4 * Math.sin(Math.PI/4);
    private static final double DISTANCE_TO_FACE = SIZE / 3;
    
    Color[] manyColors = {Color.RED.darker(), Color.RED, Color.RED.brighter(),
            Color.ORANGE.darker(), Color.ORANGE, Color.ORANGE.brighter(),
            Color.YELLOW.darker(), Color.YELLOW, Color.YELLOW.brighter(),
            Color.GREEN.darker(), Color.GREEN, Color.GREEN.brighter(),
            Color.BLUE.darker(), Color.BLUE, Color.BLUE.brighter(),
            Color.CYAN.darker(), Color.CYAN, Color.CYAN.brighter(),
            Color.MAGENTA.darker(), Color.MAGENTA, Color.MAGENTA.brighter()};

    Color[] fewColors = {Color.RED, Color.ORANGE, Color.YELLOW,
            Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA};
    
    public SmileyPanel() {
        setPreferredSize(new Dimension(SIZE, SIZE));       
        this.setBackground(Color.WHITE);
        System.out.println(EYE_DISTANCE);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g); // call parent version of paintComponent
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2));
        
        drawFaces(g2);
    }
    
    
    
    private void drawFaces(Graphics2D g2) {
        Color[] colors = fewColors;
        double angleInc = Math.PI * 2 / colors.length;
        double angle = 0;
        g2.translate(SIZE / 2, SIZE / 2);
        for(int i = 0; i < colors.length; i++) {
            double x = DISTANCE_TO_FACE * Math.cos(angle);
            double y = DISTANCE_TO_FACE * Math.sin(angle);
            drawFace(g2, x, y, angle, colors[i]);
            angle += angleInc;
        }
        
    }

    private void drawFace(Graphics2D g2, double x, 
            double y, double angle, Color c) {
        
        g2.setColor(c);
        g2.translate(x, y);
        g2.rotate(angle);
        
        Ellipse2D.Double faceOutline = new Ellipse2D.Double(-FACE_SIZE/2, 
                -FACE_SIZE/ 2, FACE_SIZE, FACE_SIZE);
        
        g2.draw(faceOutline);
        
        drawEyes(g2);
        drawSmile(g2);

        g2.rotate(-angle);
        g2.translate(-x, -y);
    }
    
    private void drawSmile(Graphics2D g2) {
        Rectangle2D smileFrame = new Rectangle2D.Double(-SMILE_SIZE / 2, 
                -SMILE_SIZE / 2, SMILE_SIZE, SMILE_SIZE);
        Arc2D.Double smile = new Arc2D.Double(smileFrame, 200, 140, Arc2D.OPEN);
        g2.draw(smile);       
    }
    
    private void drawEyes(Graphics2D g2) {
        Ellipse2D.Double eye = new Ellipse2D.Double(-EYE_WIDTH/2, 
                -EYE_WIDTH/ 2, EYE_WIDTH, EYE_WIDTH * 2);
        g2.translate(EYE_DISTANCE, -EYE_DISTANCE);
        g2.fill(eye);
        g2.translate(-EYE_DISTANCE * 2, 0);
        g2.fill(eye);
        g2.translate(EYE_DISTANCE, EYE_DISTANCE);
    }
}
    