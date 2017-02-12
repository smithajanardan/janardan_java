package ClassExamples;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.Alpha;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Material;
import javax.media.j3d.PointLight;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.ScaleInterpolator;
import javax.media.j3d.SpotLight;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

public class InterpolatorSample extends JFrame {
    public InterpolatorSample() {
        super("Interpolator Sample");
        Container c = getContentPane();
        c.setLayout( new BorderLayout() );
        InterpolatorPanel w3d = new InterpolatorPanel();     
        c.add(w3d, BorderLayout.CENTER);
        setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        setResizable(false); 
        pack();
        setVisible(true);
    } 


    public static void main(String[] args)
    { new InterpolatorSample(); }

}

class InterpolatorPanel extends JPanel {
    private static final int PWIDTH = 600;   
    private static final int PHEIGHT = 600; 

    private static final int BOUNDSIZE = 150;  

    private static final Point3d USERPOSN = new Point3d(0, 10 ,30); 

    // instance vars
    private SimpleUniverse su;
    private BranchGroup sceneBG;
    private BoundingSphere bounds;

    public InterpolatorPanel() {

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
        su.getViewer().getView().setMinimumFrameCycleTime(20);
        System.out.println(canvas3D.getDoubleBufferAvailable());
        System.out.println(canvas3D.getDoubleBufferEnable());
        createSceneGraph();
        initUserPosition();        
        orbitControls(canvas3D);   
        su.addBranchGraph( sceneBG );
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
        sceneBG.addChild( new CheckerFloor().getBG() );  
        addObjects();
        sceneBG.compile();   
    } 

    
    private void addObjects() {
        // addSphere();
        addPillar();
    }
    
    private void addPillar() {
        Appearance ap = getApp();
        Box b = new Box(.25f, 7.5f, .25f, Box.GENERATE_NORMALS, ap);
        
        Transform3D t3d = new Transform3D();
        t3d.setTranslation(new Vector3f(5, 5, 10));
        TransformGroup positionTG = new TransformGroup(t3d);
        
        Alpha a = new Alpha(-1,
                Alpha.DECREASING_ENABLE | Alpha.INCREASING_ENABLE,
                0,
                0,
                8000,
                0,
                1000,
                2000, 
                0,
                4000);
        
        TransformGroup rotateTG = new TransformGroup();
        rotateTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        rotateTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        Transform3D rotT3d = new Transform3D();
        
        // to do propeller motion uncommen the following line
        // otherwise drill motion occurs
        rotT3d.rotX(Math.PI / 2);
        RotationInterpolator ri = new RotationInterpolator(
                a, rotateTG, rotT3d, 0, (float) (8 * Math.PI));
        ri.setSchedulingBounds(bounds);
        
        
        // assemble the scene graph
        rotateTG.addChild(ri);
        rotateTG.addChild(b);
        positionTG.addChild(rotateTG);
        sceneBG.addChild(positionTG);
    }
    
    private Appearance getApp() {
        Color3f card = new Color3f(.77f, .12f, .24f);
        Color3f black = new Color3f(0, 0, 0);
        Color3f whiteish = new Color3f(.8f, .8f, .8f);
        
        Material mat = new Material(card, black, card, whiteish, 64);
        Appearance result = new Appearance();
        result.setMaterial(mat);
        return result;
    }

    private void addSphere() {
        ColoringAttributes ca = new ColoringAttributes();
        ca.setColor(.77f, .12f, .24f);
        Appearance ap = new Appearance();
        ap.setColoringAttributes(ca);
        Sphere sp = new Sphere(3, Sphere.GENERATE_NORMALS, 50, ap);
        
        // position
        Transform3D t3d = new Transform3D();
        t3d.setTranslation(new Vector3f(5, 10, -20)); // x, y, z
        TransformGroup positionTG = new TransformGroup(t3d);
        
        // scale
        TransformGroup scaleTG = new TransformGroup();
        scaleTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        scaleTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        Alpha alpha = new Alpha(-1, // loopCount, -1 = repeating
                Alpha.DECREASING_ENABLE | Alpha.INCREASING_ENABLE,
                0, // triggerTime
                0, // phaseDelayDuration
                4000, // increasing Alpha duration
                0, // increasing ramp duration
                1000, // time at one
                2000, // decreasing Alpha duration
                0, // decreasing ramp duration
                3000); // time at zero
        ScaleInterpolator si = new ScaleInterpolator(
                alpha, // the alpha used by interpolator
                scaleTG, // target transform group
                new Transform3D(), // local coordinate system, 
                // scale done about origin
                0.2f, // min scale
                5); // max scale
        si.setSchedulingBounds(bounds);
        
        // assemble scene graph
        scaleTG.addChild(si);
        scaleTG.addChild(sp);
        positionTG.addChild(scaleTG);
        sceneBG.addChild(positionTG);
    }

    private void addBackground() {
        Background back = new Background();
        back.setApplicationBounds( bounds );
        back.setColor(0.30f, 0.75f, 0.95f);
        sceneBG.addChild( back );
    }  

    private void lightScene() {
        // ambient light
        Color3f white = new Color3f(0.9f, 0.9f, 0.9f);
        AmbientLight ambientLightNode = new AmbientLight(white);
        ambientLightNode.setInfluencingBounds(bounds);
        sceneBG.addChild(ambientLightNode);

        // Set up the directional lights
        // pointing left, down, backwards 
        Vector3f light1Direction  = new Vector3f(-1.0f, -1.0f, -1.0f);
        // point right, down, forwards   
        Vector3f light2Direction  = new Vector3f(1.0f, -1.0f, 1.0f);


        DirectionalLight light1 = 
            new DirectionalLight(white, light1Direction);
        light1.setInfluencingBounds(bounds);
        sceneBG.addChild(light1);

        DirectionalLight light2 = 
            new DirectionalLight(white, light2Direction);
        light2.setInfluencingBounds(bounds);
        sceneBG.addChild(light2);

        // example of a point light
        Color3f brightWhite = new Color3f(1, 1, 1);
        Point3f lower = new Point3f(1, -5, -1);
        Point3f attenuation = new Point3f(1, 0.01f, 0.01f);
        PointLight highLight = new PointLight(brightWhite, lower, attenuation);
        highLight.setInfluencingBounds(bounds);
        sceneBG.addChild(highLight);

        // example of a spot light
        Color3f brightRed = new Color3f(1, 0.75f, 0.75f);
        Point3f pos = new Point3f(0, 4, 4);
        Vector3f direction = new Vector3f(0, 0, -1);
        SpotLight spot = new SpotLight(brightRed, pos, attenuation, direction, (float)(Math.PI / 2), 100); 
        // last two parameters are spread angle and concentration of light. 
        // 0 is uniform concentration across spread, 128 is max concentration
        // in center
        spot.setInfluencingBounds(bounds);
        sceneBG.addChild(spot);

    } 

    private void initUserPosition() {
        // necessary to get the Transform group for the viewing platform
        // in order to position it.
        ViewingPlatform vp = su.getViewingPlatform();
        TransformGroup steerTG = vp.getViewPlatformTransform();

        Transform3D t3d = new Transform3D();
        //  Copies the transform component of the TransformGroup 
        // into the passed transform object. 
        // (So we can move it.)
        steerTG.getTransform(t3d);

        // args are: viewer posn, where looking, up direction
        // recall USERPOSN is (0, 5, 20) // x, y, z
        t3d.lookAt( USERPOSN, new Point3d(0,0,0), new Vector3d(0,1,0));
        t3d.invert();

        steerTG.setTransform(t3d);

        // change the clip distance
        View v = su.getCanvas().getView();
        // System.out.println(v.getBackClipDistance());
        // System.out.println(v.getFrontClipDistance());
        v.setBackClipDistance(40);
    }  
}
