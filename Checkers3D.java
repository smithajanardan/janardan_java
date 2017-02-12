import javax.imageio.ImageIO;
import javax.media.j3d.*;
import javax.vecmath.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import com.sun.j3d.utils.behaviors.vp.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;

//Based largely on example from Killer
//Game Programming in java by Andrew Davison
public class Checkers3D extends JFrame {
    
    
    public Checkers3D() {
        super("Checkers3D");
        add(new WrapCheckers3D());
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        createMenu();
        setResizable(false);
        pack();     
        setVisible(true);
    } 


    private void createMenu() {
        // TODO Auto-generated method stub 
    }
    
    public static void main(String[] args)
    { new Checkers3D(); }
}

class WrapCheckers3D extends JPanel {
    private static final int PWIDTH = 800;   
    private static final int PHEIGHT = 600; 
    private static final int BOUNDSIZE = 100;  
    private static final Point3d USERPOSN = new Point3d(0,5,20);  

    // instance vars
    private SimpleUniverse su;
    private BranchGroup sceneBG;
    private BoundingSphere bounds;

    public WrapCheckers3D() {

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(PWIDTH, PHEIGHT));

        GraphicsConfiguration config =
            SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas3D = new Canvas3D(config);
        add(canvas3D);
        canvas3D.setFocusable(true);     

        su = new SimpleUniverse(canvas3D);

        createSceneGraph();
        initUserPosition();        
        orbitControls(canvas3D);   

        su.addBranchGraph(sceneBG);
    }

    private void orbitControls(Canvas3D canvas3d) {
        
        // to move the view point in the same direction as mouse
        OrbitBehavior orbit = new OrbitBehavior(canvas3d, 
            OrbitBehavior.REVERSE_ALL);
            
        orbit.setSchedulingBounds(bounds);
        ViewingPlatform vp = su.getViewingPlatform();
        vp.setViewPlatformBehavior(orbit);
    }

    private void createSceneGraph() {

        sceneBG = new BranchGroup();
        bounds = new BoundingSphere(new Point3d(0,0,0), BOUNDSIZE);   

        lightScene();         
        addBackground();      
        // sceneBG.addChild( new CheckerFloor().getBG() );  
        floatingSpheres();     

        sceneBG.compile();   
    } 

    private void addBackground() {
        Background back = new Background();
        back.setApplicationBounds(bounds);
        back.setColor(0.17f, 0.65f, 0.92f); 

        // sample code to load image as background
//        try{
//            BufferedImage bi 
//                  = ImageIO.read(new File("mountain.jpg"));
//            ImageComponent2D ic 
//                     = new ImageComponent2D(bi.getType(), bi);
//            back.setImage(ic);
//        }
//        catch(Exception e){
//            System.out.println("Failed to load image" + e);
//        }
        sceneBG.addChild(back);
    }  

    private void lightScene() {
        // BoundingSphere lightbounds = new BoundingSphere(new Point3d(0,0,0), 50); 
        
        Color3f white = new Color3f(1, 1, 1);
        AmbientLight ambientLightNode = new AmbientLight(white);
        ambientLightNode.setInfluencingBounds(bounds);
        sceneBG.addChild(ambientLightNode);

        // Set up the directional lights

        // pointing left, down, into scene 
        Vector3f light1Direction  
            = new Vector3f(-1, -1, -1);     
        Color3f yellow = new Color3f(1, 1, 0);
        DirectionalLight light1 = 
            new DirectionalLight(yellow, light1Direction);
        light1.setInfluencingBounds(bounds);
        sceneBG.addChild(light1);
    
        // point right, down, out of scene   
        Vector3f light2Direction  
            = new Vector3f(1.0f, -1.0f, 1.0f);
        Color3f magenta = new Color3f(1, 0, 1);
        DirectionalLight light2 = 
            new DirectionalLight(magenta, light2Direction);
        light2.setInfluencingBounds(bounds);
        sceneBG.addChild(light2);

//        // sample code for point lights and spot lights
        Color3f cyan = new Color3f(1, 1, 1);
        Point3f higher = new Point3f(1, 20, -1);
        Point3f attenuation = new Point3f(1, .05f, .001f);
        PointLight highLight = new PointLight(cyan, higher, attenuation);
        highLight.setInfluencingBounds(bounds);
        sceneBG.addChild(highLight);
      
//        
        // Point3f attenuation = new Point3f(1, .05f, .001f);
        Point3f pos = new Point3f(0, 10, 5);
        Vector3f direction = new Vector3f(0, -.5f, -1);
        SpotLight spot = new SpotLight(white, pos, 
                attenuation, direction, (float)(Math.PI * .25), 100); 
        
        // Last two parameters are spread angle and 
        // concentration of light. 
        // Spread angle between 0 and PI / 2 (90 degrees).
        // Any spread angle over PI / 2 set to PI / 2.
        // Light concentration varies between 0 and 128.
        // 0 is uniform concentration 
        // across spread, 128 is max concentration
        // in center
        
        spot.setInfluencingBounds(bounds);
        sceneBG.addChild(spot);


    } 

    private void floatingSpheres() {
        
//        Appearance app = new Appearance();
//        ColoringAttributes ca = new ColoringAttributes();
//        ca.setColor(new Color3f(.9f, .2f, .2f));
//        app.setColoringAttributes(ca);
        
        // Set up the polygon attributes
        PolygonAttributes pa = new PolygonAttributes();
        pa.setPolygonMode(PolygonAttributes.POLYGON_LINE);
        // Appearance blueApp = new Appearance();
        // app.setPolygonAttributes(pa);
        // pa.setPolygonMode(PolygonAttributes.POLYGON_POINT);
        pa.setPolygonMode(PolygonAttributes.POLYGON_FILL);
        // pa.setCullFace(PolygonAttributes.CULL_NONE);
        // app.setPolygonAttributes(pa);
        // The previous section is to see the wireframe
        
        Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
        Color3f blue = new Color3f(0.3f, 0.3f, 0.8f);
        Color3f specular = new Color3f(0.9f, 0.9f, 0.9f);
        Color3f red = new Color3f(1, 0, 0);
        Color3f white = new Color3f(1, 1, 1);

        // Material blueMat= new Material(red, black, red, white, 25);
        Material blueMat= new Material(blue, black, blue, specular, 25);
        // blueMat.setLightingEnable(true);

        Appearance app = new Appearance();
        // to see the color
        app.setMaterial(blueMat);
              

        // position the spheres
        int y = 7;
        int divisions = 4;
        for(int z = -30; z <= 0; z += 10){
            y -= 2;
            for(int x = -18; x <= 18; x += 5){
                Transform3D t3d = new Transform3D();
                t3d.set( new Vector3f(x, y, z)); 
                TransformGroup tg = new TransformGroup(t3d);
                tg.addChild(new Sphere(2f, Sphere.GENERATE_NORMALS, 
                        divisions, app));
                sceneBG.addChild(tg);
                divisions++;
                System.out.println(divisions);
            }
        }
    }

    private void initUserPosition() {
        
        
        // change back clip distance
        View view = su.getViewer().getView();
        System.out.println("Clip distance: " + view.getBackClipDistance());
        view.setBackClipDistance(view.getBackClipDistance() * 3);
        view.setFrontClipDistance(0.5);
        
        // necessary to get the Transform group for the viewing paltform
        // in order to position it.
        ViewingPlatform vp = su.getViewingPlatform();
        TransformGroup steerTG = vp.getViewPlatformTransform();

        Transform3D t3d = new Transform3D();
        //  Copies the transform component of the TransformGroup 
        // into the passed transform object. (So we can
        // move it.)
        steerTG.getTransform(t3d);

        // args are: viewer posn, where looking, up direction
        // recall USERPOSN is (0, 5, 20) // x, y, z
        t3d.lookAt( USERPOSN, new Point3d(0,0,0), new Vector3d(0,1,0));
        t3d.invert();

        steerTG.setTransform(t3d);
    }  
}

class CheckerFloor {
    private final static int FLOOR_SIZE = 40;

    // tile colors
    private final static Color3f blue = new Color3f(0.0f, 0.1f, 0.4f);
    private final static Color3f green = new Color3f(0.0f, 0.5f, 0.1f);

    // colors for axis and text
    private final static Color3f medRed = new Color3f(0.8f, 0.4f, 0.3f);
    private final static Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

    // single instance variable, to be able to add to scene graph
    private BranchGroup floorBG;
    
 // constructor for the CheckFloor class. 
    public CheckerFloor() {
        ArrayList<Point3f> blueCoords = new ArrayList<Point3f>();
        ArrayList<Point3f> greenCoords = new ArrayList<Point3f>();
        floorBG = new BranchGroup();

        boolean isBlue = false;
        // create coordinates of tiles. Each tile is 1 unit by 1 unit
        final int LIMIT = (FLOOR_SIZE / 2) - 1;
        for(int z = -FLOOR_SIZE / 2; z <= LIMIT; z++) {
            isBlue = !isBlue;
            for(int x = -FLOOR_SIZE / 2; x <= LIMIT; x++) {
                Point3f[] points = createCoords(x, z);
                ArrayList<Point3f> addTo = isBlue ? blueCoords : greenCoords;
                for(Point3f p : points)
                    addTo.add(p);
                isBlue = !isBlue;
            }
        }
        floorBG.addChild( new ColoredTiles(blueCoords, blue) );
        floorBG.addChild( new ColoredTiles(greenCoords, green) );

        addOriginMarker();
        labelAxes();
    }
    
    private void labelAxes(){
        final int LIMIT = FLOOR_SIZE / 2;
        Vector3d pt = new Vector3d();
        for(int i = -LIMIT; i <= LIMIT; i++){
            pt.z = 0;
            pt.x = i;
            floorBG.addChild( makeText(pt, "" + i) );
            pt.x = 0;
            pt.z = i;
            floorBG.addChild( makeText(pt, "" + i) );
        }
    }
    
    private TransformGroup makeText(Vector3d pos, String text){
        Text2D label = new Text2D(text, white, "SansSerif", 36, Font.BOLD);
        
        // to turn off culling of back of text
        Appearance app = label.getAppearance();       
        PolygonAttributes pa = app.getPolygonAttributes();
        if (pa == null)
            pa = new PolygonAttributes();
        pa.setCullFace(PolygonAttributes.CULL_NONE);
        if (app.getPolygonAttributes() == null)
            app.setPolygonAttributes(pa);
        
        // to position text
        TransformGroup tg = new TransformGroup();
        Transform3D transform = new Transform3D();
        transform.setTranslation(pos);
        tg.setTransform(transform);
        tg.addChild(label);
        
        return tg;
    }
    
    // create coords. x, z is upper left corner of tile
    // coords added in counter cloxkwise fashion in order
    // to be in proper order for quad array
    private Point3f[] createCoords(int x, int z) {
        Point3f[] result = new Point3f[4];
        result[0] = new Point3f(x, 0, z + 1);
        result[1] = new Point3f(x + 1, 0, z + 1);
        result[2] = new Point3f(x + 1, 0, z);
        result[3] = new Point3f(x, 0, z);   
        return result;
    }
    
    // as in example from book, KGPJ add a red square in center
    private void addOriginMarker(){
        float q = 0.25f;
        float y = 0.01f;
        ArrayList<Point3f> pts = new ArrayList<Point3f>();
        pts.add(new Point3f(-q, y, q));
        pts.add(new Point3f(-q, y, -q));
        pts.add(new Point3f(q, y, -q));
        pts.add(new Point3f(q, y, q));
        floorBG.addChild(new ColoredTiles(pts, medRed)); 
    }
    
    public BranchGroup getBG(){
        return floorBG;
    }
    
}

class ColoredTiles extends Shape3D {

    private QuadArray plane;


    public ColoredTiles(ArrayList<Point3f> pts, Color3f col){
        plane = new QuadArray(pts.size(), 
            GeometryArray.COORDINATES | GeometryArray.COLOR_3);
        
        Point3f[] quadPoints = new Point3f[pts.size()];
        pts.toArray(quadPoints); // copy elements into array
        
        // 0 is the starting vertex in the QuadArray
        plane.setCoordinates(0, quadPoints); 
        
        // set the color of all vertices. Same for all vertices
        Color3f[] colors = new Color3f[pts.size()];
        Arrays.fill(colors, col);
        plane.setColors(0, colors);
        
        // inherited method from Shape3D
        setGeometry(plane);
        
        setAppearance();
    } 

    private void setAppearance(){
        Appearance ap = new Appearance();
        PolygonAttributes attr = new PolygonAttributes();
        attr.setCullFace(PolygonAttributes.CULL_NONE);
        ap.setPolygonAttributes(attr);

        // inherited methods from Shape3D
        setAppearance(ap);
    }     

  }