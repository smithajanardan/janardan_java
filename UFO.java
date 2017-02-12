package ClassExamples.UFOHunt;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

public class UFO {
    
    private static final BufferedImage[] pictures;
    private static final int NUM_UFO_IMAGES = 4;
    
    static {
        System.out.println("Reading images");
        pictures = new BufferedImage[NUM_UFO_IMAGES];
        for(int i = 0; i < pictures.length; i++) {
            String name = "images\\ufo" + (i + 1) + ".png";
            System.out.println("Reading images: " + name);
            pictures[i] = ImageLoader.loadImage(name);
        }
    }
    
    private static final Random rand = new Random();
    private static final int BASE_WIDTH = 35;
    private static final int WIDTH_VARIABLE = 20;
    private static final int BASE_HEIGHT = 20;
    private static final int HEIGHT_VARIABLE = 20;
    private static final int BUFFER = 10;
    private static final int BASE_DURATION = 2000;
    private static final int DURATION_VARIABLE = 4000;
    
    private Animator animator;
    private BufferedImage image;
    private int width;
    private int height;
    private float x;
    private float y;
    
    
    public UFO() {
        image = pictures[rand.nextInt(pictures.length)];
        width = BASE_WIDTH + rand.nextInt(WIDTH_VARIABLE);
        height = BASE_HEIGHT + rand.nextInt(HEIGHT_VARIABLE);
        y = rand.nextInt(UFOPanel.HEIGHT - height - BUFFER * 2) + BUFFER;
        float startX = 0 - width;
        float endX = UFOPanel.WIDTH;
        if(rand.nextDouble() < 0.5) {
            float temp = startX;
            startX = endX;
            endX = temp;
        }
        x = startX;
        int duration = BASE_DURATION + rand.nextInt(DURATION_VARIABLE);
        animator = PropertySetter.createAnimator(duration, this,
                "x", startX, endX);
        animator.setResolution(10);
    }
    
    public void paintUFO(Graphics2D g2) {
        g2.drawImage(image, (int) x, (int) y, width, height, null);
//        g2.setColor(Color.RED);
//        g2.drawRect((int) x, (int) y, 30, 20);
    }
    
    public void setX(float xVal) {
        x = xVal;
    }
    
    public void setY(float yVal) {
        y = yVal;
    }
    
    public void start() {
        animator.start();
    }
    
    public boolean isRunning() {
        return animator.isRunning();
    }
    
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
