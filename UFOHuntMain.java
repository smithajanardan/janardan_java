package ClassExamples.UFOHunt;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

public class UFOHuntMain  {

    public static void main(String[] args) {
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e){
            System.out.println("Unable to change look and feel");
        }
        UFOFrame f = new UFOFrame();
        f.start();
    }
    
} // end of HelloGUI

class UFOFrame extends JFrame {
    
    private UFOPanel thePanel;
    private Animator timer;
    
    public UFOFrame() {
        setTitle("UFO Hunt");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setLocation(100, 50);
        thePanel = new UFOPanel();
        add(thePanel);
        createTimer();
        setResizable(false);
        pack();
    }
    
    private void createTimer() {
        timer = new Animator(Animator.INFINITE, Animator.INFINITE, null,
             new TimingTargetAdapter() {
                public void timingEvent(float fraction) {
                    thePanel.tick();
                    thePanel.repaint();
                }
        });
        timer.setResolution(10);
    }
    
    public void start() {
        timer.start();
        setVisible(true);

    }
}

class UFOPanel extends JPanel {
    
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private BufferedImage background;
    private Set<UFO> ufos;
    
    public UFOPanel() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        createBackground();
        this.setBackground(Color.WHITE);
        ufos = new HashSet<UFO>();
    }
    
    public void tick() {
        if(Math.random() < 0.01) {
            UFO newUFO = new UFO();
            newUFO.start();
            ufos.add(newUFO);
            // System.out.println(ufos);
        }
        Iterator<UFO> it = ufos.iterator();
        while(it.hasNext()) {
            if(!it.next().isRunning())
                it.remove();
        }
    }
    
    private void createBackground () {
        BufferedImage temp = ImageLoader.loadImage("images\\sky.jpg");
        RescaleOp brighten = new RescaleOp(1.3f, 20, null);
        temp = brighten.filter(temp, null);
        background = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2  = (Graphics2D) background.getGraphics();
        
        g2.drawImage(temp, 0, 0, WIDTH, HEIGHT, Color.WHITE, null);
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // call parent version of paintComponent
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.drawImage(background, 0, 0, null);
        for(UFO ufo : ufos)
            ufo.paintUFO(g2);
    }
}



class ImageLoader {
    
    public static final int DEFAULT_SIZE = 50;
    
    public static BufferedImage loadImage(String name) {
        BufferedImage result = new BufferedImage(DEFAULT_SIZE, DEFAULT_SIZE, BufferedImage.TYPE_INT_RGB);
        try {
            result = ImageIO.read(new File(name));
        }
        catch(IOException e){
            System.out.println("Error reading image file: " + e + ", " + name);
        }
        return result;
    }
}

