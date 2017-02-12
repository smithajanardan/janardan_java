import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.Animator.Direction;
import org.jdesktop.animation.timing.Animator.RepeatBehavior;

/**
 *
 * @author Chet
 */
public class FadingButtonTF extends JButton 
        implements ActionListener, TimingTarget {

    // current opacity of button
    float alpha = 1.0f;   
    // for start/stop actions
    Animator animator;      
    // each cycle will take 2 seconds
    int animationDuration = 4000;   
    BufferedImage buttonImage = null;
    Checkerboard cb;

    /** Creates a new instance of FadingButtonTF */
    public FadingButtonTF(String label) {
        super(label);
        setOpaque(false);
        setPreferredSize(new Dimension(150, 50));
        animator = new Animator(
                animationDuration/2,
                Animator.INFINITE, 
                RepeatBehavior.REVERSE, 
                this);
        animator.setStartFraction(1.0f);
        // animator.setStartDirection(Direction.BACKWARD);
        
//        // sample to show ease in and out
//        animator.setAcceleration(.2f);
//        animator.setDeceleration(.4f);
        
        addActionListener(this);
    }

    public void paintComponent(Graphics g) {
        // Create an image for the button graphics if necessary
        if (buttonImage == null || buttonImage.getWidth() != getWidth() ||
                buttonImage.getHeight() != getHeight()) {
            buttonImage = getGraphicsConfiguration().
                createCompatibleImage(getWidth(), getHeight());
        }
        Graphics gButton = buttonImage.getGraphics();
        gButton.setClip(g.getClip());

        //  Have the superclass render the button for us
        super.paintComponent(gButton);

        // Make the graphics object sent to this paint() method translucent
        Graphics2D g2  = (Graphics2D)g;
        AlphaComposite newComposite = 
            AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        g2.setComposite(newComposite);

        // Copy the button's image to the destination graphics
        g2.drawImage(buttonImage, 0, 0, null);
    }

    /**
     * This method receives click events, 
     * which start and stop the animation
     */
    public void actionPerformed(ActionEvent ae) {
        if (!animator.isRunning()) {
            this.setText("Stop Animation");
            animator.start();
        } else {
            animator.stop();
            this.setText("Start Animation");
            // reset alpha to opaque
            alpha = 1.0f;
        }
    }
    // Ununsed TimingTarget methods
    public void begin() {}
    public void end() {}
    public void repeat() {}

    /**
     * TimingTarget implementation: this method 
     * sets the alpha of our button equal to 
     * the current elapsed fraction of the animation.
     */
    public void timingEvent(float fraction) {
        alpha = fraction;
        cb.update(fraction);
        // redisplay our the button
        cb.repaint();
    }
    
    private void addPanel(Checkerboard panel) {
        cb = panel;
    }

    private static void createAndShowGUI() {
        JFrame f = new JFrame("Fading Button TF");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(300, 300);
        Checkerboard checkerboard = new Checkerboard();
        FadingButtonTF button = new FadingButtonTF("Start Animation");
        checkerboard.add(button);
        button.addPanel(checkerboard);
        f.add(checkerboard);
        f.setResizable(false);
        f.pack();
        f.setVisible(true);
    }

    public static void main(String args[]) {
        Runnable doCreateAndShowGUI = new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        };
        SwingUtilities.invokeLater(doCreateAndShowGUI);
    }

    private static class Checkerboard extends JPanel {
        private static final int DIVISIONS = 6;
        private static final int CHECKER_SIZE = 100;
        private static final int SIZE = 600;
        
        private Ship s;
        
        public Checkerboard() {
            setPreferredSize(new Dimension(SIZE, SIZE));
            s = new Ship(SIZE);
        }
        
        public void update(float fraction) {
            s.update(fraction);
        }
        
        public void paintComponent(Graphics g) {
            g.setColor(Color.white);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.BLACK);
            for (int stripeX = 0; stripeX < getWidth(); stripeX += CHECKER_SIZE) {
                for (int y = 0, row = 0; y < getHeight(); y += CHECKER_SIZE/2, ++row) {
                    int x = (row % 2 == 0) ? stripeX : (stripeX + CHECKER_SIZE/2);
                    g.fillRect(x, y, CHECKER_SIZE/2, CHECKER_SIZE/2);
                }
            }
            
            s.draw(g);
        }
    }
    
    private static class Ship {
        private float xPos;
        private float yPos;
        private float startX;
        private BufferedImage img;
        private float length;
        
        private Ship(int width) {
            loadImage();
            xPos = startX = 20;
            yPos = 400;
            length = width - img.getWidth() - 20;
        }
        
        public void update(float fraction) {
            xPos = startX + (1.0f - fraction) * length;
        }
        
        public void draw(Graphics g) {
            g.drawImage(img, (int) xPos, (int) yPos, null);
        }
        
        private void loadImage() {
            try {
                img = ImageIO.read(new File("images\\ufo.gif"));
                //            BufferedImage temp = ImageIO.read(new File("images\\ufo.gif"));
                //            ufo = new BufferedImage(temp.getWidth() / 2, temp.getHeight() / 2, BufferedImage.TYPE_INT_ARGB);
                //            Graphics g2 = ufo.getGraphics();
                //            g2.drawImage(temp, 0, 0, temp.getWidth() / 2, temp.getHeight() / 2, null);
                //            g2.dispose();
            } catch (IOException e) {
                img = new BufferedImage(40, 40, BufferedImage.TYPE_3BYTE_BGR);
            }     
        }
    }
}
