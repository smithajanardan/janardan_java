package ClassExamples;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GradientTest {
    public static void main(String[] args) {
        GradientFrame f = new GradientFrame();
        f.start();
    }
}

class GradientFrame extends JFrame {

    private GradientPanel thePanel;

    public GradientFrame() {
        setTitle("Gradient Examples");
        setSize(600, 400); // width, height in pixels
        setLocation(20, 60); // x, y
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

        thePanel = new GradientPanel();
        add(thePanel);

    }

    public void start() {
        setVisible(true);

    }
}

class GradientPanel extends JPanel {

    public GradientPanel() {
        this.setPreferredSize(new Dimension(600, 600));

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g); // call parent version of paintComponent
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                RenderingHints.VALUE_ANTIALIAS_ON);
        
        // drawGradientSimple(g2);
        
        // drawBiggerSimpleGradient(g2);
        
         // drawBiggerSimpleGradientCyclical(g2);
        
        // linearGradientPaint(g2);
        
        // linearGradientPaintFractions(g2);
        
        linearGradientPaintCyclic(g2);
        
        // radialGradientPaint(g2);
        
        // radialGradientPaintNotCentered(g2);
        
        // radialGradientPaintAlterFocal(g2);
        
    }
    
    private void radialGradientPaintNotCentered(Graphics2D g2) {
        
        int rad = 300;
        int xul = 100;
        int yul = 100;
        
        Ellipse2D temp = new Ellipse2D.Double(xul, yul + 50, 
                rad, rad);
       
        float[] fractions = {0, 1};
        Color[] colors = {Color.WHITE, 
                Color.RED};
        
        RadialGradientPaint rgp = new RadialGradientPaint(
                xul + rad / 2, yul + rad / 2, 150, 
                fractions, colors);
        
        g2.setPaint(rgp);
        g2.fill(temp);

        g2.setColor(Color.BLACK);
        // g2.fillOval(cx + rad / 2, cy + rad / 2, 5, 5);          
    }
    
    
    private void radialGradientPaintAlterFocal(Graphics2D g2) {
        
        int rad = 400;
        int cx = 50;
        int cy = 50;
        
        Ellipse2D temp = new Ellipse2D.Double(cx, cy, 
                rad, rad);
       
        float[] fractions = {0, 1};
        Color[] colors = {Color.WHITE, 
                Color.RED};
        
        RadialGradientPaint rgp = new RadialGradientPaint(
                cx + rad / 2, cy + rad / 2, 150,
                cx + rad / 2 - 25, cy + rad / 2 - 75, 
                fractions, colors,
                MultipleGradientPaint.CycleMethod.NO_CYCLE);
        
        g2.setPaint(rgp);
        g2.fill(temp);

        g2.setColor(Color.BLACK);
        g2.fillOval(cx + rad / 2, cy + rad / 2, 5, 5);  
        g2.fillOval(cx + rad / 2 - 25, cy + rad / 2 - 75, 5, 5);
        g2.drawOval(cx + rad / 2 - 150, cy + rad / 2 -150, 300, 300);
    }
    
    private void radialGradientPaint(Graphics2D g2) {
        
        int rad = 300;
        int cx = 100;
        int cy = 100;
        
        Ellipse2D temp = new Ellipse2D.Double(cx, cy, 
                rad, rad);
       
        float[] fractions = {0, 1};
        Color[] colors = {Color.WHITE, 
                Color.RED};
        
        RadialGradientPaint rgp = new RadialGradientPaint(
                cx + rad / 2, cy + rad / 2, 50, 
                fractions, colors);
        
        g2.setPaint(rgp);
        g2.fill(temp);

        g2.setColor(Color.BLACK);
        // g2.fillOval(cx + rad / 2, cy + rad / 2, 5, 5);          
    }
    
    
    private void linearGradientPaintCyclic(Graphics2D g2) {
        Rectangle2D temp = new Rectangle2D.Double(0, 0, 
                getWidth(), getHeight());
       
        float[] fractions = {0, 0.5f, 1};
        Color[] colors = {Color.RED, 
                Color.GREEN, Color.BLUE};
        
        LinearGradientPaint lgp = new LinearGradientPaint(150, 150, 
                200, 200,
                fractions, colors, 
                MultipleGradientPaint.CycleMethod.REFLECT);
        
        g2.setPaint(lgp);
        g2.fill(temp);

        g2.setColor(Color.BLACK);
        g2.fillOval(150, 150, 5, 5);
        g2.fillOval(200, 200, 5, 5);          
    }
    
    
    private void linearGradientPaintFractions(Graphics2D g2) {
        Rectangle2D temp = new Rectangle2D.Double(0, 0, 
                getWidth(), getHeight());
       
        float[] fractions = {0, 0.1f, 0.9f, 1};
        Color[] colors = {Color.RED, Color.YELLOW, 
                Color.GREEN, Color.MAGENTA};
        
        LinearGradientPaint lgp = new LinearGradientPaint(100, 100, 
                getWidth() - 100, getHeight() - 50,
                fractions, colors);
        
        g2.setPaint(lgp);
        g2.fill(temp);

        g2.setColor(Color.BLACK);
        g2.fillOval(100, 100, 5, 5);
        g2.fillOval(getWidth() - 100, getHeight() - 50, 5, 5);          
    }
    
    private void linearGradientPaint(Graphics2D g2) {
        Rectangle2D temp = new Rectangle2D.Double(0, 0, 
                getWidth(), getHeight());
       
        float[] fractions = {0, 0.33f, 0.67f, 1};
        Color[] colors = {Color.RED, Color.YELLOW, 
                Color.GREEN, Color.MAGENTA};
        
        LinearGradientPaint lgp = new LinearGradientPaint(100, 100, 
                getWidth() - 100, getHeight() - 50,
                fractions, colors);
        
        g2.setPaint(lgp);
        g2.fill(temp);

        g2.setColor(Color.BLACK);
        g2.fillOval(100, 100, 5, 5);
        g2.fillOval(getWidth() - 100, getHeight() - 50, 5, 5);          
    }
    
    private void drawBiggerSimpleGradientCyclical(Graphics2D g2) {
        
        Rectangle2D temp = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
        
        GradientPaint gp = new GradientPaint(100, 50, Color.BLUE, 
                200, 150, Color.RED, true);
        g2.setPaint(gp);
        g2.fill(temp);

        g2.setColor(Color.BLACK);
        g2.fillOval(100, 50, 5, 5);
        g2.fillOval(350, 300, 5, 5);        
    }
    
    private void drawBiggerSimpleGradient(Graphics2D g2) {
        Rectangle2D temp = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
        GradientPaint gp = new GradientPaint(100, 50, Color.BLUE, 
                350, 300, Color.RED);
        g2.setPaint(gp);
        g2.fill(temp);

        g2.setColor(Color.BLACK);
        g2.fillOval(100, 50, 5, 5);
        g2.fillOval(350, 300, 5, 5);        
    }
    
    private void drawGradientSimple(Graphics2D g2) {
        Shape temp 
            = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
        
        GradientPaint gp = new GradientPaint(100, 50, new Color(255, 255, 125), 
                350, 300, new Color(255, 0, 125));
        g2.setPaint(gp);
        g2.fill(temp);

        g2.setColor(Color.BLACK);
        g2.fillOval(100, 50, 5, 5);
        g2.fillOval(350, 300, 5, 5); 
        
        // what is the paint here????
        
        
        
    }
}
