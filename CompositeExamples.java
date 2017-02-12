package ClassExamples;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class CompositeExamples {
    public static void main(String[] args) {        
        CompFrame f = new CompFrame();
        f.start();
    }
}

class CompFrame extends JFrame {

    private CompPanel thePanel;

    public CompFrame() {
        setTitle("Sample Composites");
        setSize(400, 400); // width, height in pixels
        setLocation(20, 60); // x, y
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

        thePanel = new CompPanel();
        add(thePanel);
        setResizable(false);
        pack();

    }

    public void start() {
        setVisible(true);

    }
}

class CompPanel extends JPanel {

    private BufferedImage image;

    public CompPanel() { 
        setBackground(Color.WHITE);
        try {
            image = ImageIO.read(new File("images\\3Cheers.png"));
        } catch (IOException e) {
        }
        if(image != null)
            this.setPreferredSize( 
                    new Dimension(image.getWidth() * 3 / 2, image.getHeight() * 3 / 2));
        else
            this.setPreferredSize(new Dimension(500, 300));
            
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g); // call parent version of paintComponent
        Graphics2D g2 = (Graphics2D)g;

        // showClear(g2);
        
        showSourceIn(g2);


    }
    
    private void showSourceIn(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());
        
        BufferedImage buffImg = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D gbi = buffImg.createGraphics();
        gbi.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                RenderingHints.VALUE_ANTIALIAS_ON); 
        
        
        
        FontRenderContext frc = g2.getFontRenderContext();
        Font f = new Font("Times",Font.BOLD, 300);
        String s = new String("UTCS");
        TextLayout tl = new TextLayout(s, f, frc);
        float sw = (float) tl.getBounds().getWidth();
        AffineTransform transform = new AffineTransform();
        transform.setToTranslation(getWidth() / 2 - sw / 2, getHeight() / 2);
        Shape shape = tl.getOutline(transform);
        
        
        gbi.fill(shape);
        gbi.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, 0.5f));
        gbi.drawImage(image, 0, 0, getWidth(), getHeight(), null);
     
        g2.drawImage(buffImg, 0, 0, null);        
    }
    
    
    private void showClear(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());
        
        BufferedImage buffImg = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D gbi = buffImg.createGraphics();
        gbi.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                RenderingHints.VALUE_ANTIALIAS_ON); 
        
        gbi.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        
        FontRenderContext frc = g2.getFontRenderContext();
        Font f = new Font("Times",Font.BOLD, 300);
        String s = new String("UTCS");
        TextLayout tl = new TextLayout(s, f, frc);
        float sw = (float) tl.getBounds().getWidth();
        AffineTransform transform = new AffineTransform();
        transform.setToTranslation(getWidth() / 2 - sw / 2, getHeight() / 2);
        Shape shape = tl.getOutline(transform);
        
        gbi.setComposite(AlphaComposite.Clear);
        gbi.fill(shape);
     
        g2.drawImage(buffImg, 0, 0, null);        
    }
}