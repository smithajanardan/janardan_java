package ClassExamples;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Scratch {
    public static void main(String[] args) {    
        //        GraphicsEnvironment ge 
        //                = GraphicsEnvironment.getLocalGraphicsEnvironment();
        //        String[] fontFamilies = ge.getAvailableFontFamilyNames();
        //        System.out.println(fontFamilies.length);
        //        for(String fontName : fontFamilies)
        //            System.out.println(fontName);

        ScratchFrame f = new ScratchFrame();
        f.start();
    }

} // end of HelloGUI

class ScratchFrame extends JFrame {

    private ScratchPanel thePanel;

    public ScratchFrame() {
        setTitle("Graphics Examples");
        setSize(600, 600); // width, height in pixels
        setLocation(20, 60); // x, y
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

        thePanel = new ScratchPanel();
        add(thePanel);

    }

    public void start() {
        setVisible(true);

    }
}

class ScratchPanel extends JPanel {

    private int timesPCcalled;
    private int SIZE = 600;

    private int cx;
    private int cy;

    public ScratchPanel() {
        // setBackground(Color.ORANGE);   
        createMouseListner();
        cx = SIZE - 20;
        cy = 50;
    }

    private void createMouseListner() {
        MouseAdapter ma = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.out.println(e);
                cx = e.getX();
                cy = e.getY();
                repaint();
            }
        };
        this.addMouseListener(ma);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g); // call parent version of paintComponent
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                RenderingHints.VALUE_ANTIALIAS_ON);

        demoArea(g2);
        // drawYY(g2);
        // simpleWind(g2);
        // demoWinding(g2);
        //        showGeneralPath(g2);
        //        showCubicCurve(g2);
        //        showQuadCurve(g2);
        //        System.out.println(g2.getFont());
        //        drawArc(g2);      
        //        showClip(g2);

        //        timesPCcalled++;
        //        g.setFont(new Font("Serif", Font.PLAIN, 40));
        //        g.drawString("paintComponentCalled: " + timesPCcalled, 
        //                20, 40); // string, x, y of baseline
        //        

        //        // showing different translates
        //        Rectangle2D.Double rect = new Rectangle2D.Double(20, 20, 75, 75);
        //        drawOrigin(g2);
        //        g2.setColor(Color.PINK);
        //        // drawRect(g2, rect, Color.PINK);
        //        g2.fill(rect);    
        //        
        //        g2.setFont(new Font("Future", 
        //                Font.BOLD | Font.ITALIC, 50));
        //        g2.setColor(Color.BLACK);
        //        g2.drawString("You win!!!", 0, 50);
        //        g2.setColor(Color.PINK);
        ////        
        //        g2.translate(200, 100); // x, y or new origin of graphics object
        //        drawOrigin(g2);
        //        g2.fill(rect);
        //        g2.fillOval(30, 45, 50, 175);
        //        
        //        g2.rotate(Math.toRadians(60));
        //        drawOrigin(g2);
        //        g2.fill(rect);
        //        g2.fillOval(30, 45, 50, 175);
        //        
        //
        //        
        //        g2.translate(200, 100); // x, y or new origin of graphics object
        //        g2.scale(3, 0.5);
        //        drawOrigin(g2);
        //        g2.fill(rect);
        //
        //        // showing different fonts
        //        g2.setColor(Color.BLACK);
        //        g2.setFont(new Font(Font.SERIF, 
        //                Font.BOLD | Font.ITALIC, 50));
        //        g2.drawString("You win!!!", 0, 20);

    }

    private static final Stroke originStroke 
    = new BasicStroke(3, BasicStroke.CAP_ROUND,
            BasicStroke.JOIN_ROUND, 1.0f,
            new float[]{10f, 5f, 20f, 20f}, 0);

    private void drawOrigin(Graphics2D g2) {
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(originStroke);
        Color oldColor = g2.getColor();
        g2.setColor(Color.BLACK);
        g2.fillOval(0, 0, 10, 10);
        g2.drawLine(0,0, 300, 0);
        g2.drawLine(0, 0, 0, 300);
        g2.setColor(oldColor);
        g2.setStroke(oldStroke);

    }

    public void transformExamples(Graphics2D g2) {
        Rectangle2D.Double rect = new Rectangle2D.Double(20, 20, 75, 75);

        g2.translate(50, 100);
        drawRect(g2, rect, Color.PINK);

        g2.translate(150, 50);
        g2.rotate(Math.toRadians(30));
        drawRect(g2, rect, Color.PINK);

        g2.translate(200, 0);
        drawRect(g2, rect, Color.GREEN);

        g2.translate(-200, 0);
        g2.rotate(-Math.toRadians(30));
        g2.translate(150, -100);
        drawRect(g2, rect, Color.WHITE);
        // g2.translate(-100, 100);
        g2.scale(0.75, 2.0);
        drawRect(g2, rect, Color.MAGENTA);
    }

    private void drawArc(Graphics2D g2) {

        g2.rotate(Math.PI / 6);

        Rectangle2D.Double rect = new Rectangle2D.Double(20, 20, 75, 75);
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.GREEN);
        Arc2D.Double arc = new Arc2D.Double(rect, 50, 80, Arc2D.CHORD);
        g2.draw(arc);
        g2.translate(100, 0);
        g2.fill(arc);

        g2.setColor(Color.RED);
        g2.translate(100, 0);
        arc.setArcType(Arc2D.PIE);
        g2.draw(arc);
        g2.translate(100, 0);
        g2.fill(arc);

        g2.setColor(Color.BLUE);
        g2.translate(100, 0);
        arc.setArcType(Arc2D.OPEN);
        g2.draw(arc);
        g2.translate(100, 0);
        g2.fill(arc);
    }

    private void drawRect(Graphics2D g2, Rectangle2D rect, Color c) {
        g2.setColor(c);
        g2.fill(rect); // x, y, width, height
        g2.setColor(Color.BLACK);
        g2.draw(rect);       
    }

    private void showClip(Graphics2D g2) {
        Random r = new Random();
        Arc2D[] arcs = new Arc2D[50];
        int width = getWidth();
        int height = getHeight();
        for(int i = 0; i < arcs.length; i++) {
            int x = r.nextInt(width / 2);
            int y = r.nextInt(height / 2);
            int w = r.nextInt(width);
            int h = r.nextInt(height);
            Rectangle2D.Double frame = new Rectangle2D.Double(x, y, w, h);
            int start = r.nextInt(360);
            int extent = r.nextInt(40) + r.nextInt(180);
            arcs[i] = new Arc2D.Double(frame, start, extent, Arc2D.PIE);
        }

        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(4));
        g2.setClip(new Rectangle2D.Double(100, 100, 300, 300));
        g2.setColor(Color.BLACK);
        double colorInc = 255.0 / arcs.length;
        double intensity = 0;
        for(Arc2D a : arcs) {
            g2.draw(a);
            intensity += colorInc;
            g2.setColor(new Color((int) intensity, 0, (int) intensity ));
        }
        g2.setStroke(oldStroke);
    }

    private static final Stroke lineStroke 
    = new BasicStroke(3, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_ROUND, 1.0f,
            new float[]{5f, 15f}, 0);

    private void showQuadCurve(Graphics2D g2) {
        double x1 = 20;
        double y1 = 20;
        double x2 = getWidth() / 2.0;
        double y2 = getHeight() - 20;

        int pointSize = 5;
        drawPoint(g2, x1, y1, pointSize);
        drawPoint(g2, x2, y2, pointSize);
        drawPoint(g2, cx, cy, pointSize);

        g2.setColor(Color.RED);
        g2.setStroke(new BasicStroke(3));
        QuadCurve2D qc 
        = new QuadCurve2D.Double(x1, y1, cx, cy, x2, y2);
        g2.fill(qc);

        Stroke currentStroke = g2.getStroke();
        g2.setStroke(lineStroke);
        g2.setColor(Color.BLACK);
        g2.draw(new Line2D.Double(x1, y1, cx, cy));
        g2.draw(new Line2D.Double(x2, y2, cx, cy));
        g2.setStroke(currentStroke);
    }

    private void drawPoint(Graphics2D g2, double x, double y, double size) {
        Color oldColor = g2.getColor();
        g2.setColor(Color.BLACK);
        g2.fill(new Ellipse2D.Double(x - size / 2, y - size / 2, size, size));
        g2.setColor(oldColor);
    }

    private void showCubicCurve(Graphics2D g2) {
        double x1 = 20;
        double y1 = 30;
        double x2 = getWidth() - 20;
        double y2 = getHeight() - 30;

        double cx1 = getWidth() * 2;
        double cy1 = 60;
        double cx2 = 40;
        double cy2 = getHeight() - 80;

        int pointSize = 10;
        drawPoint(g2, x1, y1, pointSize);
        drawPoint(g2, x2, y2, pointSize);
        drawPoint(g2, cx1, cy1, pointSize);
        drawPoint(g2, cx2, cy2, pointSize);

        CubicCurve2D cc = new CubicCurve2D.Double(x1, y1, cx1, cy1,
                cx2, cy2, x2, y2);
        g2.setColor(Color.RED);
        g2.draw(cc);

        // draw lines
        Stroke currentStroke = g2.getStroke();
        g2.setStroke(lineStroke);
        g2.setColor(Color.BLACK);
        g2.draw(new Line2D.Double(x1, y1, cx1, cy1));
        g2.draw(new Line2D.Double(x2, y2, cx2, cy2));
        g2.draw(new Line2D.Double(cx1, cy1, cx2, cy2));
        g2.setStroke(currentStroke);
    }

    private void showGeneralPath(Graphics2D g2) {
        g2.setStroke(new BasicStroke(5));
        g2.setColor(Color.BLUE);

        GeneralPath gp1, gp2, gp3, gp4;
        ArrayList<GeneralPath> paths = new ArrayList<GeneralPath>();

        gp1 = new GeneralPath();
        gp1.moveTo(120, 180);
        gp1.quadTo(150, 120, 180, 180);
        gp1.closePath();
        paths.add(gp1);

        gp2 = new GeneralPath();
        gp2.moveTo(220, 150);
        gp2.curveTo(240, 130, 280, 160, 300, 140);
        gp2.lineTo(300, 180);
        gp2.quadTo(260, 160, 220, 180);
        gp2.closePath();
        paths.add(gp2);

        gp3 = new GeneralPath();
        gp3.moveTo(360, 100);
        gp3.lineTo(360, 200);
        gp3.lineTo(400, 140);
        gp3.lineTo(320, 120);
        gp3.lineTo(400, 180);
        gp3.lineTo(320, 180);
        gp3.closePath();
        paths.add(gp3);

        gp4 = new GeneralPath(new Ellipse2D.Double(400, 200, 100, 200));
        gp4.lineTo(100, 300);
        gp4.lineTo(300, 350);
        gp4.lineTo(250, 500);
        gp4.quadTo(-100, 500, 100, 300);
        paths.add(gp4);

        for(GeneralPath gp : paths)
            g2.fill(gp);
    }

    private void demoWinding(Graphics2D g2) {
        final int SIZE = 200;
        GeneralPath p = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        p.moveTo(0,0);
        p.lineTo(0, SIZE);
        p.lineTo(SIZE, 0);
        p.lineTo(SIZE, SIZE);
        p.lineTo(0,0);
        p.lineTo(SIZE / 2.0, SIZE * 2);
        p.lineTo(SIZE, 0);
        p.lineTo(0, 0);

        g2.translate(SIZE / 2.0, SIZE);
        g2.setColor(Color.RED);
        g2.draw(p);
        g2.translate(SIZE * 2.0, 0);
        g2.fill(p);
    }

    private void simpleWind(Graphics2D g2) {

        g2.setStroke(new BasicStroke(4));
        g2.setColor(Color.ORANGE);

        double width = 300;
        double height = 100;
        //        GeneralPath gp = new GeneralPath(
        //                new Rectangle2D.Double(20, 20, getWidth() - 100, getHeight() - 100));

        GeneralPath gp = new GeneralPath(GeneralPath.WIND_NON_ZERO);
        int x = 100;
        int y = 50;
        gp.moveTo(x, y);
        gp.lineTo(x += width, y);
        gp.lineTo(x, y += height);
        gp.lineTo(x -= width, y);
        gp.lineTo(x, y -= height);
        gp.moveTo(x += width / 2, y += height / 2);
        gp.lineTo(x, y += height);
        gp.lineTo(x += width, y);
        gp.lineTo(x, y -= height);
        gp.lineTo(x -= width, height);

        g2.fill(gp);
    }

    private void drawYY(Graphics2D g2) {
        double oneSixthWidth = getWidth() / 6.0;
        double oneSixthHeight = getHeight() / 6.0;
        Rectangle2D arcBound = new Rectangle2D.Double(oneSixthWidth, oneSixthHeight, oneSixthWidth * 4, oneSixthHeight * 4);
        Arc2D leftArc = new Arc2D.Double(arcBound,  90, 180, Arc2D.CHORD);
        Arc2D rightArc = new Arc2D.Double(arcBound,270, 180, Arc2D.CHORD);

        Ellipse2D topCircle = new Ellipse2D.Double(oneSixthWidth * 2, 
                oneSixthHeight, oneSixthWidth * 2, oneSixthHeight * 2);
        Ellipse2D bottomCircle = new Ellipse2D.Double(oneSixthWidth * 2, 
                oneSixthHeight * 3, oneSixthWidth * 2, oneSixthHeight * 2);

        double smallCircleWidthOffset = getWidth() / 40.0;
        double smallCircleHeightOffset = getHeight() / 40.0;
        Ellipse2D topSmallCircle = 
            new Ellipse2D.Double(oneSixthWidth * 3 - smallCircleWidthOffset, oneSixthHeight * 2 - smallCircleHeightOffset, smallCircleWidthOffset * 2, smallCircleHeightOffset * 2);

        Ellipse2D bottomSmallCircle = 
            new Ellipse2D.Double(oneSixthWidth * 3 - smallCircleWidthOffset, oneSixthHeight * 4 - smallCircleHeightOffset, smallCircleWidthOffset * 2, smallCircleHeightOffset * 2);

        // testing code
        //            Shape[] test = {leftArc, rightArc, topCircle, bottomCircle, //                topSmallCircle, bottomSmallCircle};
        //            for(Shape s : test)
        //                g2.draw(s);

        Area ying = createHalf(new Shape[] {leftArc, bottomCircle, 
                bottomSmallCircle, topCircle, topSmallCircle});
        Area yang = createHalf(new Shape[] {rightArc, topCircle, 
                topSmallCircle, bottomCircle, bottomSmallCircle});

        g2.setColor(Color.BLACK);
        g2.fill(yang);
        g2.setColor(Color.WHITE);
        g2.fill(ying);
    }

    private Area createHalf(Shape[] parts){
        Area result = new Area(parts[0]);
        result.add(new Area(parts[1]));
        result.subtract(new Area(parts[2]));
        result.subtract(new Area(parts[3]));
        result.add(new Area(parts[4]));
        return result;
    }
    
    private void demoArea(Graphics2D g2) {
        
        g2.setStroke(new BasicStroke(4));
        g2.setColor(Color.BLUE.darker());
        
        int rw = 250;
        int rh = 125;
        
        int cw = 250;
        int ch = 200;
        
        int x = 200;
        int y = 200;
        
        Rectangle2D r1 = new Rectangle2D.Double(x, y, rw, rh);
        Rectangle2D r2 = new Rectangle2D.Double(x + rw / 4, y + rh / 4, rw / 2, rh /2);
        Ellipse2D c1 = new Ellipse2D.Double(x - rw / 2, y + rh / 2, cw, ch);
        Ellipse2D c2 = new Ellipse2D.Double(x + rw * 3 / 5, y - rh, cw, ch);
        Ellipse2D c3 = new Ellipse2D.Double(x + rw * 3 / 5, y + rh * 4 / 5, cw, ch);
        
//        g2.draw(r1);
//        g2.draw(r2);
//        g2.draw(c1);
//        g2.draw(c2);
//        g2.draw(c3);
        Area a1 = new Area(r1);
        Area a2 = new Area(r2);
        Area a3 = new Area(c1);
        Area a4 = new Area(c2);
        Area a5 = new Area(c3);
        a1.subtract(a2);
        a1.add(a3);
        a1.exclusiveOr(a4);
        a1.subtract(a5);
        g2.fill(a1);
        
    }
}