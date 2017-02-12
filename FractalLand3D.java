package Fractal;
// FractalLand3D.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* A fractal landscape is generated, made out of a mesh
   of textured squares. Squares at different heights are
   textured in different ways. 

   The bumpiness of the landscape is controlled by a flatness
   value input from the command line (or a default value can
   be used).

   The landscape is surrounded by dark blue walls.

   The user can 'walk' over the landscape using the
   similar left/right/front/back/turn/up/down moves
   as in the FPShooter3D example.
 */

import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;

import com.sun.j3d.utils.behaviors.vp.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.*;
import com.sun.j3d.utils.picking.*;
import com.sun.j3d.utils.universe.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;

public class FractalLand3D extends JFrame {
    private static final double DEF_FLAT = 3.0;   // makes a smooth-ish landscape
    private static final double MIN_FLAT = 1.0;   // rough
    private static final double MAX_FLAT = 3.0;   // very flat


    public FractalLand3D(String[] args) 
    {
        super("3D Fractal Landscape");

        double flatness = processArgs(args);
        System.out.println("Flatness: " + flatness);

        WrapFractalLand3D w3d = new WrapFractalLand3D(flatness);

        Container c = getContentPane();
        c.setLayout( new BorderLayout() );
        c.add(w3d, BorderLayout.CENTER);

        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        pack();
        setResizable(false);    // fixed size display
        setVisible(true);
    } // end of FractalLand3D()


    private double processArgs(String[] args)
    // 0 or 1 argument is acceptable
    {
        double flatness = DEF_FLAT;
        if (args.length == 1)
            flatness = getFlatness(args[0]);
        else if (args.length > 1) {
            System.out.println("Usage: java FractalLand3D [<Flatness>]");
            System.exit(0);
        }
        return flatness;
    }  // end of processArgs()


    private double getFlatness(String arg)
    // flatness must be a double within the range MIN_FLAT to MAX_FLAT
    {
        double flatness;
        try {
            flatness = Double.parseDouble(arg);
            if ((flatness < MIN_FLAT) || (flatness > MAX_FLAT)) {
                System.out.println("Flatness must be between " + 
                        MIN_FLAT + " and " + MAX_FLAT);
                flatness = DEF_FLAT;
            }
        }
        catch (NumberFormatException ex)
        { System.out.println("Incorrect format for Flatness double");  
        flatness = DEF_FLAT;
        }
        return flatness;
    }  // end of getFlatness()


    // -----------------------------------------

    public static void main(String[] args)
    { new FractalLand3D( args);  }

} // end of FractalLand3D class

class WrapFractalLand3D extends JPanel {
    /* Holds the 3D fractal landscape in a Swing container. */
    // private static final Point3d USERPOSN = new Point3d(40, 350, 200); // to view world
    private static final Point3d USERPOSN = new Point3d(0, 300, -350);

    private final static int PWIDTH = 1000;   // size of panel
    private final static int PHEIGHT = 700; 

    private static final int BOUNDSIZE = 400;  // larger than world

    private Color3f skyColor = new Color3f(0.17f, 0.07f, 0.45f);
    // used for the Background and LinearFog nodes

    private SimpleUniverse su;
    private BranchGroup sceneBG;
    private BoundingSphere bounds;   // for environment nodes

    private Landscape land;   // creates the floor and walls


    public WrapFractalLand3D(double flatness, boolean keyboard) {
        setLayout( new BorderLayout() );
        setOpaque( false );
        setPreferredSize( new Dimension(PWIDTH, PHEIGHT));

        GraphicsConfiguration config =
            SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas3D = new Canvas3D(config);
        add("Center", canvas3D);
        canvas3D.setFocusable(true);   
        canvas3D.requestFocus();
        su = new SimpleUniverse(canvas3D);

        createSceneGraph(flatness);
        createUserControls();

        su.addBranchGraph( sceneBG );
    } // end of WrapFractalLand3D()

    public WrapFractalLand3D(double flatness) {
        setLayout( new BorderLayout() );
        setOpaque( false );
        setPreferredSize( new Dimension(PWIDTH, PHEIGHT));

        GraphicsConfiguration config =
            SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas3D = new Canvas3D(config);
        add("Center", canvas3D);
        canvas3D.setFocusable(true);   
        canvas3D.requestFocus();
        su = new SimpleUniverse(canvas3D);

        createSceneGraph(flatness);
        
        // alter controls
        createUserControls();
        // orbitControls(canvas3D);
        // initUserPosition();

        su.addBranchGraph( sceneBG );
    } // end of WrapFractalLand3D()

    private void orbitControls(Canvas3D canvas3d) {

        // to move the view point in the same direction as mouse
        OrbitBehavior orbit = new OrbitBehavior(canvas3d, 
                OrbitBehavior.REVERSE_ALL);

        orbit.setSchedulingBounds(bounds);
        ViewingPlatform vp = su.getViewingPlatform();
        vp.setViewPlatformBehavior(orbit);
    }

    private void initUserPosition() {
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
        t3d.lookAt(USERPOSN, new Point3d(0,0,0), new Vector3d(0,1,0));
        t3d.invert();

        steerTG.setTransform(t3d);

        // change clip distances and field of view
        View view = su.getViewer().getView();
        view.setBackClipDistance(150);
    }  


    void createSceneGraph(double flatness) {
        // initilise the scene
        sceneBG = new BranchGroup();
        bounds = new BoundingSphere(new Point3d(0,0,0), BOUNDSIZE);

        lightScene();     // add the lights
        addBackground();  // add the sky
        // Comment this line out to switch off fog
        addFog();         // add the fog.

        // create the landscape: the floor and walls
        land = new Landscape(flatness);
        sceneBG.addChild( land.getLandBG() );   

        sceneBG.compile();   // fix the scene
    } // end of createScene()


    private void lightScene() {
        // one directional light
        Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

        Vector3f lightDir = new Vector3f(1.0f, -1.0f, -1.0f); // upper left
        DirectionalLight light1 = 
            new DirectionalLight(white, lightDir);
        light1.setInfluencingBounds(bounds);
        sceneBG.addChild(light1);
    }  // end of lightScene()


    private void addBackground() {
        // An early evening sky
        Background back = new Background();
        back.setApplicationBounds( bounds );
        back.setColor( skyColor );   // darkish blue
        sceneBG.addChild( back );
    }  // end of addBackground()


    private void addFog() {
        // linear fog
        // skyColor = new Color3f(0.17f, 0.07f, 0.45f);
        Color3f fogColor = new Color3f(.45f, .1f, .1f);
        LinearFog fogLinear = new LinearFog(skyColor, 5.0f, 40.0f);
        fogLinear.setInfluencingBounds(bounds);  // same as background
        sceneBG.addChild(fogLinear);
    }  // end of addFog()


    // ---------------------- user controls --------------------------


    private void createUserControls()
    /* Adjust the clip distances and set up the KeyBehaviour.
     The behaviour initialises the viewpoint at the origin on
     the XZ plane.
     */
    {
        // original clips are 10 and 0.1; keep ratio between 100-1000
        View view = su.getViewer().getView();
        view.setBackClipDistance(150);      // can see a long way
        view.setFrontClipDistance(0.05);   // can see close things

        ViewingPlatform vp = su.getViewingPlatform();
        TransformGroup steerTG = vp.getViewPlatformTransform();

        // set up keyboard controls (and position viewpoint)
        KeyBehavior keybeh = new KeyBehavior(land, steerTG);
        keybeh.setSchedulingBounds(bounds);
        vp.setViewPlatformBehavior(keybeh);
    } // end of createUserControls()


} // end of WrapFractalLand3D class

class Landscape {

    public static final int WORLD_LEN = 256;   //side length of floorfflate

    private static final int NUM_TEXTURES = 5;
    private static final String textureFns[] =
    {"water-shallow.jpg", "sand.jpg", "grass.gif", 
        "dryEarth.jpg", "stone.gif"};

    public  final static double MIN_HEIGHT = -20;
    public final static double MAX_HEIGHT = 100; 

    /* The number of textures are used to calculate the height boundaries 
     between MIN_HEIGHT and MAX_HEIGHT. The first named texture in textureFns[]
     is used for quads in the first height range (the lowest), the second
     texture for the next height range, and so on.
     */

    private final static Vector3d DOWN_VEC = new Vector3d(0.0,-1.0, 0.0);
    // direction for picking -- down below the viewpoint


    private BranchGroup landBG, floorBG;
    private Point3d vertices[];               // landscape coordinates
    private double textureBoundaries[];   
    // heights that mark the boundaries for which mesh gets which texture

    private Vector3d originVec = new Vector3d();   // stating point for viewpoint
    private boolean foundOrigin = false;
    private PickTool picker;


    public Landscape(double flatness)
    {
        landBG = new BranchGroup();
        floorBG = new BranchGroup();
        landBG.addChild(floorBG);  // so landBG-->floorBG

        setTexBoundaries();

        picker = new PickTool(floorBG);   // only check the floor
        picker.setMode(PickTool.GEOMETRY_INTERSECT_INFO);

        FractalMesh fm = new FractalMesh(flatness);
        // fm.printMesh(1);   // for debugging: x=0; y=1; z=2
        vertices = fm.getVertices();    // get the vertices generated by FractalMesh

        platifyFloor();
        addWalls();
    } // end of Landscape()


    private void setTexBoundaries()
    // Store the height boundaries for the textures
    {
        textureBoundaries = new double[NUM_TEXTURES];
        double boundStep = (MAX_HEIGHT - MIN_HEIGHT) / NUM_TEXTURES;
        double boundary = MIN_HEIGHT + boundStep;
        for(int j=0; j < NUM_TEXTURES; j++) {
            textureBoundaries[j] = boundary;     // place in increasing order
            boundary += boundStep;
        }
    } // end of setTexBoundaries()



    private void platifyFloor()
    /* Examine the quads stored in vertices[]. Check the
     average height and assign it to the ArrayList for
     coords in that height range.

     Pass each ArrayList and their texture filenames to
     a TexturedPlanes object to build the 3D mesh for
     those coords.
     */
    {
        ArrayList[] coordsList = new ArrayList[NUM_TEXTURES];
        for (int i=0; i < NUM_TEXTURES; i++)
            coordsList[i] = new ArrayList();

        int heightIdx;
        for (int j=0; j < vertices.length; j=j+4) {   // test each quad
            heightIdx = findHeightIdx(j);   // which height index applies to the quad
            addCoords( coordsList[heightIdx], j);   // add quad to the list for that height
            checkForOrigin(j);       // check if (0,0) is a point in the quad
        }

        // use each coordsList and texture to make a TexturedPlanes object
        for (int i=0; i < NUM_TEXTURES; i++)
            if (coordsList[i].size() > 0)    // if used
                floorBG.addChild( new TexturedPlanes(coordsList[i],  // then add to the floor
                        "images\\" + textureFns[i]) );
    } // end of platifyFloor()



    private int findHeightIdx(int vertIndex)
    /* Find the height index for the quad starting at vertices[vertIndex].
     Get the average height for the 4 points in the quad.
     If it is less than the boundary value, then the quad belongs 
     to that height range. */
    {
        double ah = avgHeight(vertIndex);
        for(int i=0; i < textureBoundaries.length; i++)
            if (ah < textureBoundaries[i])
                return i;
        return NUM_TEXTURES-1;   // last ArrayList is default
    } // end of findHeightIdx()


    private double avgHeight(int vi)
    // Calculate the average height for the 4 points in the quad.
    { 
        return (vertices[vi].y + vertices[vi+1].y +
                vertices[vi+2].y + vertices[vi+3].y)/4.0;
    }


    private void addCoords(ArrayList coords, int vi)
    // add the 4 coords (the quad) beginning at vertices[vi] to the ArrayList
    {
        coords.add( vertices[vi] ); coords.add( vertices[vi+1] ); 
        coords.add( vertices[vi+2] ); coords.add( vertices[vi+3] );
    }  // end of addCoords()


    private void checkForOrigin(int vi)
    // If vertices[vi] is at the origin, store its position in originVec
    {
        if (!foundOrigin) {
            if ((vertices[vi].x == 0.0) && (vertices[vi].z == 0.0)) {
                // System.out.println("Found Origin: (" + vertices[vi].x + ", " + 
                //         vertices[vi].y + ", " + vertices[vi].z + ")");
                originVec.y = vertices[vi].y;
                foundOrigin = true;
            }
        }
    }  // end of checkForOrigin()



    private void addWalls()
    // Add 4 walls around the landscape
    {
        Color3f eveningBlue = new Color3f(0.17f, 0.07f, 0.45f);  // wall colour

        // the eight corner points
        // back, left
        Point3d p1 = new Point3d(-WORLD_LEN/2.0f, MIN_HEIGHT, -WORLD_LEN/2.0f);
        Point3d p2 = new Point3d(-WORLD_LEN/2.0f, MAX_HEIGHT, -WORLD_LEN/2.0f);

        // front, left
        Point3d p3 = new Point3d(-WORLD_LEN/2.0f, MIN_HEIGHT, WORLD_LEN/2.0f);
        Point3d p4 = new Point3d(-WORLD_LEN/2.0f, MAX_HEIGHT, WORLD_LEN/2.0f);

        // front, right
        Point3d p5 = new Point3d(WORLD_LEN/2.0f, MIN_HEIGHT, WORLD_LEN/2.0f);
        Point3d p6 = new Point3d(WORLD_LEN/2.0f, MAX_HEIGHT, WORLD_LEN/2.0f);

        // back, right
        Point3d p7 = new Point3d(WORLD_LEN/2.0f, MIN_HEIGHT, -WORLD_LEN/2.0f);
        Point3d p8 = new Point3d(WORLD_LEN/2.0f, MAX_HEIGHT, -WORLD_LEN/2.0f);

        // left wall; counter-clockwise
        landBG.addChild( new ColouredPlane(p3, p1, p2, p4, 
                new Vector3f(-1,0,0), eveningBlue) );
        // front wall; counter-clockwise from back
        landBG.addChild( new ColouredPlane(p5, p3, p4, p6,
                new Vector3f(0,0,-1), eveningBlue) );
        // right wall
        landBG.addChild( new ColouredPlane(p7, p5, p6, p8, 
                new Vector3f(-1,0,0), eveningBlue) );
        // back wall
        landBG.addChild( new ColouredPlane(p7, p8, p2, p1, 
                new Vector3f(0,0,1), eveningBlue) );
    } // end of addWalls()



    // ------------- public methods ------------------


    public BranchGroup getLandBG()
    {  return landBG;  }


    public boolean inLandscape(double xPosn, double zPosn)
    // is (xPosn,zPosn) on the floo?
    {
        int x = (int) Math.round(xPosn);   // to deal with dp errors
        int z = (int) Math.round(zPosn);

        if ((x <= -WORLD_LEN/2) || (x >= WORLD_LEN/2) ||
                (z <= -WORLD_LEN/2) || (z >= WORLD_LEN/2))
            return false;
        return true;
    }  // end of inLandscape()


    public Vector3d getOriginVec()
    {  return originVec;  }


    public double getLandHeight(double x, double z, double currHeight)
    /* Throw a pick ray downwards below the (x,z) point to intersect
     with the floor. Extract the y-value (the height of the floor
     and return it.

     Picking is a bit flakey, especially near quad edges and corners,
     so if no PickResult is found, no intersections found, or the
     extraction of intersection coords fails, then the
     height for the last viewpoint position is returned.
     */
    {
        Point3d pickStart = new Point3d(x, MAX_HEIGHT*2, z);    // start from high up
        picker.setShapeRay(pickStart, DOWN_VEC);   // shoot a ray downwards

        PickResult picked = picker.pickClosest();
        if (picked != null) {    // pick sometimes misses at an edge/corner
            if (picked.numIntersections() != 0) {    // sometimes no intersects are found
                PickIntersection pi = picked.getIntersection(0);
                Point3d nextPt;
                try {   // handles 'Interp point outside quad' error
                    nextPt = pi.getPointCoordinates();
                }
                catch (Exception e) {
                    // System.out.println(e);
                    return currHeight;
                }
                return nextPt.y;
            }
        }
        return currHeight;    // error if we reach here; return existing height
    }  // end of getLandHeight()


} // end of Landscape class

class KeyBehavior extends ViewPlatformBehavior {

    private static final double ROT_AMT = Math.PI / 36.0;   // 5 degrees
    private static final double MOVE_STEP = 0.2;

    private static final double USER_HEIGHT = 1.0;  // of head above the floor

    // hardwired movement vectors
    private static final Vector3d FWD = new Vector3d(0,0,-MOVE_STEP);
    private static final Vector3d BACK = new Vector3d(0,0,MOVE_STEP);
    private static final Vector3d LEFT = new Vector3d(-MOVE_STEP,0,0);
    private static final Vector3d RIGHT = new Vector3d(MOVE_STEP,0,0);
    private static final Vector3d UP = new Vector3d(0,MOVE_STEP,0);
    private static final Vector3d DOWN = new Vector3d(0,-MOVE_STEP,0);

    // key names
    private int forwardKey = KeyEvent.VK_UP;
    private int backKey = KeyEvent.VK_DOWN;
    private int leftKey = KeyEvent.VK_LEFT;
    private int rightKey = KeyEvent.VK_RIGHT;


    private WakeupCondition keyPress;

    private Landscape land;         // used for checking/calculating moves
    private double currLandHeight;  // floor height at current position
    private int zOffset;            // used when moving up/down

    // for repeated calcs
    private Transform3D t3d = new Transform3D();
    private Transform3D toMove = new Transform3D();
    private Transform3D toRot = new Transform3D();
    private Vector3d trans = new Vector3d();


    public KeyBehavior(Landscape ld, TransformGroup steerTG)
    {
        land = ld;
        zOffset = 0;   // user is standing on the floor at the start
        initViewPosition(steerTG);

        keyPress = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
    } // end of KeyBehavior()


    private void initViewPosition(TransformGroup steerTG)
    // place viewpoint at (0,?,0), facing into scene
    {
        Vector3d startPosn = land.getOriginVec();
        // startPosn is (0, <height of floor>, 0)

        currLandHeight = startPosn.y;   // store current floor height
        startPosn.y += USER_HEIGHT;     // add user's height

        steerTG.getTransform(t3d);      // targetTG not yet available
        t3d.setTranslation(startPosn);  // so use steerTG
        steerTG.setTransform(t3d); 
    }  // end of initViewPosition()


    public void initialize()
    {  wakeupOn( keyPress );  }


    public void processStimulus(Enumeration criteria)
    // respond to a keypress
    {
        WakeupCriterion wakeup;
        AWTEvent[] event;

        while( criteria.hasMoreElements() ) {
            wakeup = (WakeupCriterion) criteria.nextElement();
            if( wakeup instanceof WakeupOnAWTEvent ) {
                event = ((WakeupOnAWTEvent)wakeup).getAWTEvent();
                for( int i = 0; i < event.length; i++ ) {
                    if( event[i].getID() == KeyEvent.KEY_PRESSED )
                        processKeyEvent((KeyEvent)event[i]);
                }
            }
        }
        wakeupOn( keyPress );
    } // end of processStimulus()



    private void processKeyEvent(KeyEvent eventKey)
    {
        int keyCode = eventKey.getKeyCode();
        // System.out.println(keyCode);

        if( eventKey.isAltDown() )    // key + <alt>
            altMove(keyCode);
        else
            standardMove(keyCode);
    } // end of processKeyEvent()


    private void standardMove(int keycode)
    /* viewer moves forward or backward; 
     rotate left or right */
    { if(keycode == forwardKey)
        moveBy(FWD);
    else if(keycode == backKey)
        moveBy(BACK);
    else if(keycode == leftKey)
        rotateY(ROT_AMT);
    else if(keycode == rightKey)
        rotateY(-ROT_AMT);
    } // end of standardMove()


    private void altMove(int keycode)
    // moves viewer up or down, left or right
    { if(keycode == forwardKey) {
        doMove(UP);   // no testing using moveBy()
        zOffset++;
    }
    else if(keycode == backKey) {
        if (zOffset > 0) {
            doMove(DOWN);  // no testing using moveBy()
            zOffset--;
        }
    }
    else if(keycode == leftKey)
        moveBy(LEFT);
    else if(keycode == rightKey)
        moveBy(RIGHT);
    }  // end of altMove()



    // ----------------------- moves ----------------------------


    private void moveBy(Vector3d theMove)
    /* Calculate the next position on the floor (x,?,z). Test if it
     is within the floor boundaries. 

     If it is then ask Landscape to get the floor height for that (x,z). 

     Then set the y value to a change so that the viewpoint will rest
     on the floor at (x,z)
     */
    { 
        Vector3d nextLoc = tryMove(theMove);   // next (x,?,z) user position
        if (!land.inLandscape(nextLoc.x, nextLoc.z))   // if not on landscape
            return;

        // Landscape returns floor height at (x,z)
        double floorHeight = land.getLandHeight(nextLoc.x, nextLoc.z, 
                currLandHeight);
        // Calculate the change from the current y-position.
        // Reset any offset upwards back to 0.
        double heightChg = floorHeight - currLandHeight -
        (MOVE_STEP*zOffset);

        currLandHeight = floorHeight;    // update current height
        zOffset = 0;                     // back on floor, so no offset
        Vector3d actualMove = new Vector3d(theMove.x, heightChg, theMove.z);
        doMove(actualMove);
    }  // end of moveBy()



    private Vector3d tryMove(Vector3d theMove)
    /* Calculate the effect of the given translation to get the
     new (x,?, z) coord. Do not update the viewpoint's TG yet
     */
    { targetTG.getTransform( t3d );
    toMove.setTranslation(theMove);
    t3d.mul(toMove);
    t3d.get( trans );
    return trans;
    }  // end of tryMove()


    private void doMove(Vector3d theMove)
    // move the viewpoint by theMove offset
    {
        targetTG.getTransform(t3d);
        toMove.setTranslation(theMove);
        t3d.mul(toMove);
        targetTG.setTransform(t3d);  // update viewpoint's TG
    } // end of doMove()


    // -------------- rotation --------------------

    private void rotateY(double radians)
    // rotate about y-axis by radians
    {
        targetTG.getTransform(t3d);
        toRot.rotY(radians);
        t3d.mul(toRot);
        targetTG.setTransform(t3d);
    } // end of rotateY()


}  // end of KeyBehavior class

class ColouredPlane extends Shape3D {

    private static final int NUM_VERTS = 4;

    public ColouredPlane(Point3d p1, Point3d p2, Point3d p3, Point3d p4,
            Vector3f normVec, Color3f col) 
    { createGeometry(p1, p2, p3, p4, normVec);
    createAppearance(col);
    } // end of ColouredPlane()


    private void createGeometry(Point3d p1, Point3d p2, Point3d p3, Point3d p4,
            Vector3f normVec)
    {
        QuadArray plane = new QuadArray(NUM_VERTS, 
                GeometryArray.COORDINATES | GeometryArray.NORMALS );

        // counter-clockwise point specification
        plane.setCoordinate(0, p1);
        plane.setCoordinate(1, p2);
        plane.setCoordinate(2, p3);
        plane.setCoordinate(3, p4);

        Vector3f[] norms = new Vector3f[NUM_VERTS];
        for (int i=0; i < NUM_VERTS; i++)
            norms[i] = normVec;   // same normal for all points
        plane.setNormals(0, norms);

        setGeometry(plane);
    }  // end of createGeometry()


    private void createAppearance(Color3f col)
    {
        Appearance app = new Appearance();

        // mateial with lighting effects
        Material mat = new Material();
        mat.setDiffuseColor(col); 
        mat.setLightingEnable(true);

        app.setMaterial(mat);
        setAppearance(app);
    }  // end of createAppearance()


} // end of ColouredPlane class

class TexturedPlanes extends Shape3D {

    public TexturedPlanes(ArrayList coords, String fnm) 
    {
        System.out.println(fnm + "; numPoints: " + coords.size());
        createGeometry(coords);
        createAppearance(fnm);

        // set the picking capabilities so that intersection
        // coords can be extracted after the shape is picked
        PickTool.setCapabilities(this, PickTool.INTERSECT_COORD);
    } // end of TexturedPlanes()


    private void createGeometry(ArrayList coords) {

        int numPoints = coords.size();
        QuadArray plane = new QuadArray(numPoints, 
                GeometryArray.COORDINATES | 
                GeometryArray.TEXTURE_COORDINATE_2 |
                GeometryArray.NORMALS );

        // set coordinates
        Point3d[] points = new Point3d[numPoints];
        coords.toArray(points);

        // assign texture coords to each quad
        // counter-clockwise, from bottom left
        TexCoord2f[] tcoords = new TexCoord2f[numPoints];
        for(int i=0; i < numPoints; i=i+4) {
            tcoords[i] = new TexCoord2f(0.0f, 0.0f);   // for 1 point
            tcoords[i+1] = new TexCoord2f(1.0f, 0.0f);
            tcoords[i+2] = new TexCoord2f(1.0f, 1.0f);
            tcoords[i+3] = new TexCoord2f(0.0f, 1.0f);
        }

        // create geometryInfo
        GeometryInfo gi = new GeometryInfo(GeometryInfo.QUAD_ARRAY);
        gi.setCoordinates(points);
        gi.setTextureCoordinateParams(1, 2); // one set of 2D texels
        gi.setTextureCoordinates(0, tcoords);

        // calculate normals with very smooth edges
        NormalGenerator ng = new NormalGenerator();
        ng.setCreaseAngle( (float) Math.toRadians(150));   // default is 44
        ng.generateNormals(gi);

        // stripifier to use triangle strips
        Stripifier st = new Stripifier();
        st.stripify(gi);

        // extract and use GeometryArray
        setGeometry(gi.getGeometryArray());
    }  // end of createGeometry()




    private void createAppearance(String fnm) {
        // combine the texture with a lit white surface
        Appearance app = new Appearance();

        // mix the texture and the material colour
        TextureAttributes ta = new TextureAttributes();
        ta.setTextureMode(TextureAttributes.MODULATE);
        app.setTextureAttributes(ta);

        // load and set the texture; generate mipmaps for it
        TextureLoader loader = new TextureLoader(fnm, 
                TextureLoader.GENERATE_MIPMAP, null);

        Texture2D texture = (Texture2D) loader.getTexture();
        texture.setMinFilter(Texture2D.MULTI_LEVEL_LINEAR);  // NICEST

        app.setTexture(texture);      // set the texture

        // set a default white material
        Material mat = new Material();
        mat.setLightingEnable(true);    // lighting switched on
        app.setMaterial(mat);

        setAppearance(app);
    }  // end of createAppearance()


} // end of TexturedPlanes class

class FractalMesh {

    private static final int WORLD_LEN = Landscape.WORLD_LEN;
    /* WORLD_LEN should be a power of 2 since it will be
     halved repeatedly until a single mesh cell as a
     width of 1 unit. */

    // a height range of 10 units
    private final static double MIN_HEIGHT = Landscape.MIN_HEIGHT;
    private final static double MAX_HEIGHT = Landscape.MAX_HEIGHT;


    private Point3d mesh[][];    // stores the mesh's (x,y,z) points
    private DecimalFormat df;    // for output of mesh

    private double flatness;
    /* Amount used to reduce dHeight on each recursive
     call to divideMesh(). A larger value makes
     landscape smoother; smaller is more chaotic
     */

    private Random rnd;   // for generating random numbers 


    public FractalMesh(double flat)
    {
        flatness = flat;
        //flatness = 1; // to see complete randomness
        mesh = new Point3d[WORLD_LEN+1][WORLD_LEN+1];
        df = new DecimalFormat("0.##");  // 2 dp
        rnd = new Random(1L);       // a fixed seed
        makeMesh();
    } // end of FractalMesh()


    private void makeMesh()
    /* Initialise the 4 corners of the mesh with random heights
     within the MIN and MAX range. Then start recursively
     generating midoints by calling divideMesh(). */
    {
        System.out.println("Building the landscape...please wait");
        //    mesh[0][0] =    // back left
        //      new Point3d( -WORLD_LEN/2, randomHeight(), -WORLD_LEN/2 );
        //
        //    mesh[0][WORLD_LEN] =    // back right
        //      new Point3d( WORLD_LEN/2, randomHeight(), -WORLD_LEN/2 );
        //
        //    mesh[WORLD_LEN][0] =    // front left
        //      new Point3d( -WORLD_LEN/2, randomHeight(), WORLD_LEN/2 );
        //
        //    mesh[WORLD_LEN][WORLD_LEN] =    // front right
        //      new Point3d( WORLD_LEN/2, randomHeight(), WORLD_LEN/2 );

        // try fixed heights
        mesh[0][0] =    // back left
            new Point3d( -WORLD_LEN/2, MAX_HEIGHT, -WORLD_LEN/2 );

        mesh[0][WORLD_LEN] =    // back right
            new Point3d( WORLD_LEN/2, MAX_HEIGHT, -WORLD_LEN/2 );

        mesh[WORLD_LEN][0] =    // front left
            new Point3d( -WORLD_LEN/2, MAX_HEIGHT, WORLD_LEN/2 );

        mesh[WORLD_LEN][WORLD_LEN] =    // front right
            new Point3d( WORLD_LEN/2, MIN_HEIGHT, WORLD_LEN/2 );

        divideMesh( (MAX_HEIGHT-MIN_HEIGHT)/flatness, WORLD_LEN/2);

    } // end of makeMesh()


    private double randomHeight()
    // between MIN_HEIGHT and MAX_HEIGHT
    {  return (Math.random()*(MAX_HEIGHT-MIN_HEIGHT) + MIN_HEIGHT);  
    // return (rnd.nextDouble()*(MAX_HEIGHT-MIN_HEIGHT) + MIN_HEIGHT);
    // to fix the randomness
    }


    private void divideMesh(double dHeight, int stepSize)
    /* At each step, we must perform the diamond step for
     the entire mesh before doing the square step. */
    {
        int xPt, zPt;
        if (stepSize >= 1) {   // stop recursing once stepSize is < 1

            // diamond step for all mid points at this level
            zPt = stepSize;
            while (zPt < WORLD_LEN+1) {
                xPt = stepSize;
                while (xPt < WORLD_LEN+1) {
                    mesh[zPt][xPt] = getDiamond(zPt, xPt, dHeight, stepSize);
                    xPt += (stepSize*2);
                }
                zPt += (stepSize*2);
            }

            // square step for all points surrounding diamonds
            zPt = stepSize;
            while (zPt < WORLD_LEN+1) {
                mesh[zPt][0] = getSquare(zPt, 0, dHeight, stepSize);  // left column
                xPt = stepSize;
                while (xPt < WORLD_LEN+1) {
                    getSquares(zPt, xPt, dHeight, stepSize);  // back & right cells
                    xPt += (stepSize*2);
                }
                zPt += (stepSize*2);
            }

            xPt = stepSize;
            while (xPt < WORLD_LEN+1) {
                mesh[WORLD_LEN][xPt] = 
                    getSquare(WORLD_LEN, xPt, dHeight, stepSize);  // front row
                xPt += (stepSize*2);
            }

            divideMesh(dHeight/flatness, stepSize/2);
        }
    }  // end of divideMesh()


    private void getSquares(int z, int x, double dHeight, int stepSize)
    {
        mesh[cCoord(z-stepSize)][x] =    // back
            getSquare(cCoord(z-stepSize), x, dHeight, stepSize);

        mesh[z][cCoord(x+stepSize)] =    // right
            getSquare(z, cCoord(x+stepSize), dHeight, stepSize);
    } // end of getSquares()



    private Point3d getDiamond(int z, int x, double dHeight, int stepSize)
    {
        // System.out.println("getDiamond: " + z + ", " + x + " /" + stepSize);
        Point3d leftBack = mesh[cCoord(z-stepSize)][cCoord(x-stepSize)];
        Point3d rightBack = mesh[cCoord(z-stepSize)][cCoord(x+stepSize)];
        Point3d leftFront = mesh[cCoord(z+stepSize)][cCoord(x-stepSize)];
        Point3d rightFront = mesh[cCoord(z+stepSize)][cCoord(x+stepSize)];
        double height =
            calcHeight(leftBack, rightBack, leftFront, rightFront, dHeight);
        double xWorld = x - (WORLD_LEN/2);
        double zWorld = z - (WORLD_LEN/2);

        return new Point3d(xWorld, height, zWorld);
    } // end of getDiamond()


    private Point3d getSquare(int z, int x, double dHeight, int stepSize)
    {
        // System.out.println("getSquare: " + z + ", " + x + " /" + stepSize);
        Point3d back = mesh[cCoord(z-stepSize)][x];
        Point3d front = mesh[cCoord(z+stepSize)][x];
        Point3d left = mesh[z][cCoord(x-stepSize)];
        Point3d right = mesh[z][cCoord(x+stepSize)];
        double height = calcHeight(back, front, left, right, dHeight);
        double xWorld = x - (WORLD_LEN / 2);
        double zWorld = z - (WORLD_LEN / 2);

        return new Point3d(xWorld, height, zWorld);
    } // end of getSquare()


    private int cCoord(int coordIdx)
    /* If the coord index is less then o, greater then WORLD_LEN
     then use the coord on the opposite edge of the mesh.
     */
    {
        if (coordIdx < 0)
            return WORLD_LEN + coordIdx;
        else if (coordIdx > WORLD_LEN)
            return coordIdx - WORLD_LEN;
        else
            return coordIdx;
    } // end of cCoord()


    private double calcHeight(Point3d back, Point3d front,
            Point3d left, Point3d right, double dHeight)
    /* If the calculated height is < MIN_HEIGHT, set it to MIN_HEIGHT.
     If the calculated height is > MAX_HEIGHT, then take modulo
     MAX_HEIGHT.
     */
    {
        double height = (back.y + front.y + left.y + right.y)/4.0f +
        randomRange(dHeight);
        if (height < MIN_HEIGHT)
            height = MIN_HEIGHT;
        else if (height > MAX_HEIGHT)
            height = height%MAX_HEIGHT;
        return height;
    }  // end of calcHeight()


    private double randomRange(double h)
    // between -h and h
    {  return ((Math.random() * 2 * h) - h);  
    // return ((rnd.nextDouble() * 2 * h) - h);   // to fix the randomness
    }



    public Point3d[] getVertices()
    /* Return the mesh as an array of vertices. Each group of 4 points
     will be used to create a TexturedPlane object. 

     We order the points so a plane is specified in anti-clockwise order,
     which will be used when a texture is placed on it.
     */
    { int numVerts = WORLD_LEN*WORLD_LEN*4;
    Point3d vertices[] = new Point3d[numVerts];

    int vPos = 0;
    for(int z=0; z<WORLD_LEN; z++) {
        for(int x=0; x<WORLD_LEN; x++) {
            vertices[vPos++] = mesh[z+1][x];     // anti-clockwise creation
            vertices[vPos++] = mesh[z+1][x+1];   // from bottom-left
            vertices[vPos++] = mesh[z][x+1];
            vertices[vPos++] = mesh[z][x];
        }
    }
    return vertices;
    }  // end of getVertices



    // ---------------------- debugging ------------------


    public void printMesh(int axis)
    // axis values: x=0, y=1, z=2
    {
        File f = new File("mesh.txt");
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(f),true);

            if (axis == 0)
                pw.println("---------- World X Coords ------------");
            else if (axis == 1)
                pw.println("---------- World Y Coords ------------");
            else 
                pw.println("---------- World Z Coords ------------");

            for(int z=0; z<WORLD_LEN+1; z++) {
                for(int x=0; x<WORLD_LEN+1; x++)
                    if (axis == 0)
                        pw.print( df.format(mesh[z][x].x) + " ");
                    else if (axis == 1)
                        pw.print( df.format(mesh[z][x].y) + " ");
                    else
                        pw.print( df.format(mesh[z][x].z) + " ");
                pw.println();
            }
            pw.println("--------------------------------------");

            System.out.println("Mesh written to mesh.txt");
        }
        catch(Exception e)
        {  System.out.println("Could not write mesh to mesh.txt");  }
    }  // end of printMesh()


}  // end of FractalMesh class
