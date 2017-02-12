package ClassExamples;

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
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Animation {

    public static void main(String[] args) {
        AnimationFrame f = new AnimationFrame();
        f.start();
    }

} // end of HelloGUI

class AnimationFrame extends JFrame {

    private AnimationPanel thePanel;

    public AnimationFrame() {
        setTitle("Animation Samples");
        setSize(400, 400); // width, height in pixels
        setLocation(50, 50); // x, y
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

        thePanel = new AnimationPanel();
        add(thePanel);
        pack();

    }

    //Animation Frame class
    public void start() {
        setVisible(true);
        //                int x = 0;
        //                for(int i = 0; i < 1000000000; i++)
        //                    for(int j = 0; j < 10000000; j++)
        //                        x = i * j;
        //                System.out.println(x);

        // thePanel.moveShip();
        thePanel.start();
        // repaint();
    }

}

class AnimationPanel extends JPanel {

    private BufferedImage ufo;
    private int startX;
    private int endX;
    private double xImg;
    private int yImg;
    private long startTime;
    private int numFrames;
    private float fps;
    private static final long BILLION = 1000000000;
    private static final long FPS_WINDOW = BILLION * 5;
    private static int DELAY = 30; // milliseconds
    private Timer timer;
    private long previousTime;
    private int speed;
    private double angle;

    private static final int WIDTH = 800;
    private static final int HEIGHT = 500;

    public AnimationPanel() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        loadImage();
        setBackground(Color.WHITE);
        xImg = startX = 20;
        yImg = 20;
        endX = 800 - ufo.getWidth() - 20;
        addTimer();
    }

    private void addTimer() {
        timer = new Timer(30, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // System.out.println(e);
                update();
            }
        });
    }

    public void start() {
        previousTime = System.nanoTime();
        speed = 200;
        timer.start();
    }

    //    private void moveShipCircle() {
    //        long currentTime = System.nanoTime();
    //        long elapsedTime = currentTime - previousTime;
    //        
    //        xImg = xImg +  1.0 * elapsedTime / BILLION * speed;
    ////        System.out.println(xImg + " " + previousTime 
    ////                + " " + currentTime + " " + elapsedTime);
    //        previousTime = currentTime;
    //        if(xImg > endX || xImg < startX)
    //            speed = speed * -1;
    //    }

        // responding to a timer going off
        private void moveShip() {
            long currentTime = System.nanoTime();
            long elapsedTime = currentTime - previousTime;
            xImg = xImg +  1.0 * elapsedTime / BILLION * speed;
    //        System.out.println(xImg + " " + previousTime 
    //                + " " + currentTime + " " + elapsedTime);
            previousTime = currentTime;
            if(xImg > endX || xImg < startX)
                speed = speed * -1;
        }

    private void update() {
        moveShip();
        repaint();
    }

//    // back and forth using Thread.sleep
//    public void moveShip() {
//        xImg = yImg = 20;
//        double startX = xImg;
//        int endX = getWidth() - ufo.getWidth() - 20;
//        long previousTime = System.nanoTime();
//        int speed = 100; // pixels per second
//        while(true) {
//            long currentTime = System.nanoTime();
//            long elapsedTime = currentTime - previousTime;
//            xImg = xImg +  1.0 * elapsedTime / BILLION * speed;
//            //          System.out.println(xImg + " " + previousTime 
//            //                  + " " + currentTime + " " + elapsedTime);
//            previousTime = currentTime;
//            if(xImg > endX || xImg < startX)
//                speed = speed * -1;
//            repaint();
//
//            pause();
//        }
//    }

    private void pause() {
        try {
            Thread.sleep(DELAY);
        }
        catch(InterruptedException e) {
            System.out.println(e);
        }
    }



    //    // back and forth
    //    public void moveShip() {
    //        xImg = yImg = 20;
    //        double startX = xImg;
    //        int endX = getWidth() - ufo.getWidth() - 20;
    //        long previousTime = System.nanoTime();
    //        int speed = 100; // pixels per second
    //        while(true) {
    //            long currentTime = System.nanoTime();
    //            long elapsedTime = currentTime - previousTime;
    //            xImg = xImg +  1.0 * elapsedTime / BILLION * speed;
    //            //            System.out.println(xImg + " " + previousTime 
    //            //                    + " " + currentTime + " " + elapsedTime);
    //            previousTime = currentTime;
    //            if(xImg > endX || xImg < startX)
    //                speed = speed * -1;
    //            repaint();
    //            int x = 0;
    //            for(int i = 0; i < 10000; i++)
    //                for(int j = 0; j < 10000; j++)
    //                    x = i * j;  
    //        }
    //    }

    //    //    // Animation Panel class
    //        public void moveShip() {
    //            xImg = yImg = 20;
    //            double startX = xImg;
    //            int endX = getWidth() - ufo.getWidth() - 20;
    //            long animationDuration = (long) (4.5 * BILLION); // milliseconds
    //            long startTime = System.nanoTime();
    //            long currentTime = startTime;
    //            long endTime = startTime + animationDuration;
    //            while(currentTime < endTime) {
    //                long elapsedTime = currentTime - startTime;
    //                float t = ((float) elapsedTime) / animationDuration;
    //                xImg = (int) (startX + t * (endX - startX));
    //                repaint();
    //                
    //                        int x = 0;
    //                        for(int i = 0; i < 10000000; i++)
    //                            for(int j = 0; j < 100000; j++)
    //                                x = i * j;
    //                
    //                currentTime = System.nanoTime();
    //            }
    //        }

    //    public void moveShip() {
    //        xImg = getWidth() - ufo.getWidth() - 20;
    //    }

    //    public void moveShip() {
    //        xImg = yImg = prevXImg = prevYImg = 20;
    //        int endX = getWidth() - ufo.getWidth() - 20;
    //        for(int x = xImg; x < endX; x++) {
    //            repaint();
    //            
    //            int temp = 0; 
    //            for(int i = 0; i < 10000000; i++)
    //                for(int j = 0; j < 1000000; j++)
    //                    temp = i * j;
    //            
    //            xImg += 1;
    //        }
    //    }

    //    public void moveShip() {
    //        xImg = yImg = prevXImg = prevYImg = 20;
    //        int endX = getWidth() - ufo.getWidth() - 20;
    //        int startX = 20;
    //        int xInc = 1;
    //        while(true) {
    //            repaint();
    //            
    //            int temp = 0; 
    //            for(int i = 0; i < 10000; i++)
    //                for(int j = 0; j < 1000; j++)
    //                    temp = i * j;
    //            
    //            if(xImg == endX)
    //                xInc = -1;
    //            else if(xImg == startX)
    //                xInc = 1;
    //            xImg += xInc;
    //        }
    //    }
    //    

    private float getFPS() {
        numFrames++;
        if(startTime == 0) {
            startTime = System.nanoTime();
        }
        else {
            long currentTime = System.nanoTime();
            long delta = (currentTime - startTime);
            // Average the fps every five seconds
            if(delta > FPS_WINDOW) {
                fps = ((float) numFrames) / delta * BILLION;
                numFrames = 0;
                startTime = currentTime;
                System.out.println(fps);
            }
        }
        return fps;
    }


    private void loadImage() {

        try {
            ufo = ImageIO.read(new File("images\\ufo.gif"));
            //            BufferedImage temp = ImageIO.read(new File("images\\ufo.gif"));
            //            ufo = new BufferedImage(temp.getWidth() / 2, temp.getHeight() / 2, BufferedImage.TYPE_INT_ARGB);
            //            Graphics g2 = ufo.getGraphics();
            //            g2.drawImage(temp, 0, 0, temp.getWidth() / 2, temp.getHeight() / 2, null);
            //            g2.dispose();
        } catch (IOException e) {
            ufo = new BufferedImage(40, 40, BufferedImage.TYPE_3BYTE_BGR);
        }     
    }

    //        // draw image
    //        public void paintComponent(Graphics g) {
    //            super.paintComponent(g); 
    //            Graphics2D g2 = (Graphics2D)g;
    //               
    //            g2.drawImage(ufo, 20, 20, null);
    //        }

    //        // attempt at animation 1
    //        public void paintComponent(Graphics g) {
    //            super.paintComponent(g); 
    //            Graphics2D g2 = (Graphics2D)g;
    //               
    //            g2.drawImage(ufo, 20, 20, null);
    //            
    //            g2.setColor(getBackground());
    //            g2.fillRect(20, 20, ufo.getWidth(), ufo.getHeight());
    //            
    //            g2.drawImage(ufo, getWidth() - ufo.getWidth() - 20, 20, null);  
    //        }

    //    // attempt at animation 2  - slow it down?
    //      public void paintComponent(Graphics g) {
    //          super.paintComponent(g); 
    //          Graphics2D g2 = (Graphics2D)g;
    //             
    //          g2.drawImage(ufo, 20, 20, null);
    //          
    //          int x = 0;
    //          for(int i = 0; i < 1000000000; i++)
    //              for(int j = 0; j < 10000000; j++)
    //                  x = i * j;
    //          System.out.println(x);
    //          
    //          g2.setColor(getBackground());
    //          g2.fillRect(20, 20, ufo.getWidth(), ufo.getHeight());
    //          
    //          g2.drawImage(ufo, getWidth() - ufo.getWidth() - 20, 20, null);  
    //      }
    //
    // attempt 3 - pauses elsewhere
    public void paintComponent(Graphics g) {
        getFPS();
        super.paintComponent(g); 
        Graphics2D g2 = (Graphics2D)g;
        // System.out.println("in paint");
        g2.drawImage(ufo, (int) xImg, yImg, null);

    }

    //    // attempt 4 - pauses elsewhere
    //    public void paintComponent(Graphics g) {
    //        super.paintComponent(g); 
    //        System.out.println(getFPS());
    //        Graphics2D g2 = (Graphics2D)g;
    //
    //        g2.drawImage(ufo, xImg, yImg, null);
    //
    //    }
}
