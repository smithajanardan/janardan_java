package ClassExamples;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.Alpha;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.PointLight;
import javax.media.j3d.PositionPathInterpolator;
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
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

public class ThreeDMotion extends JFrame {
	public ThreeDMotion() {
		super("ThreeDMotion");
		Container c = getContentPane();
		c.setLayout( new BorderLayout() );
		MotionPanel w3d = new MotionPanel();     
		// panel holding the 3D canvas

		c.add(w3d, BorderLayout.CENTER);

		setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
		setResizable(false); 
		pack();
		setVisible(true);
	} 


	public static void main(String[] args)
	{ new ThreeDMotion(); }

} // end of CheckboardWorld

class MotionPanel extends JPanel {
	private static final int PWIDTH = 600;   
	private static final int PHEIGHT = 600; 

	private static final int BOUNDSIZE = 150;  

	private static final Point3d USERPOSN = new Point3d(0, 10 ,30); 
	// x, y, z

	// instance vars
	private SimpleUniverse su;
	private BranchGroup sceneBG;
	private BoundingSphere bounds;

	public MotionPanel() {

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

		addMovingBoxes();
		// addScaledBox();
		sceneBG.compile();   
	} 

	
	private void addScaledBox() {
	    Transform3D position = new Transform3D();
	    position.set(new Vector3f(-4, 3, 1));
	    TransformGroup pos = new TransformGroup(position);
	    sceneBG.addChild(pos);
	    TransformGroup objTrans = new TransformGroup();
	    pos.addChild(objTrans);
	    objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	    objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	    

	    Alpha alpha = new Alpha(
	           -1,  
	           Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE,
	           0, 
	           0, 
	           3000, 
	           400, 
	           0, 
	           3000, 
	           400, 
	           0
	   );

	    Transform3D axis = new Transform3D();

	    ScaleInterpolator scale = 
	      new ScaleInterpolator(alpha, objTrans, axis, 0.5f, 1.5f);
	    BoundingSphere bounds = new BoundingSphere(new Point3d(), 100.0);
	    scale.setSchedulingBounds(bounds);
	    objTrans.addChild(scale);
	    
	    objTrans.addChild(new ColorCube(2));
	    
	}
	private void addMovingBoxes() {
		Transform3D boxPosition = new Transform3D();
		boxPosition.set(new Vector3f(-5, 1, 0));
		TransformGroup tg = new TransformGroup(boxPosition);

		// allow the transform group to be written to by behaviors
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		sceneBG.addChild(tg);

		// create the alpha
		Alpha a = new Alpha(-1, 2000); // continuous, 2 seconds
		// make alpha go up and down, a triangle wave
		a.setMode(Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE);
		// same time to come down to 0 as it is to go up to 1
		a.setDecreasingAlphaDuration(5000);
		a.setAlphaAtOneDuration(2000);
		a.setIncreasingAlphaRampDuration(1000);
		a.setDecreasingAlphaRampDuration(1000);
		a.setTriggerTime(2000);
		
		// create a translation interpolator to change position
		float[] knots = {0, 0.75f, 1};
		Point3f[] points = {new Point3f(-5, 1, 0), new Point3f(5, 1, 0), 
				new Point3f(5, 6, -10)};
		PositionPathInterpolator movement 
		= new PositionPathInterpolator(a, tg, 
		        new Transform3D(), knots, points);
		movement.setSchedulingBounds(bounds);
		tg.addChild(movement);


		// Create a transform group node to rotate the cube about
		// its y axis and enable the TRANSFORM_WRITE capability so
		// that the rotation behavior can modify it at runtime.
		TransformGroup cubeRotate = new TransformGroup();
		cubeRotate.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg.addChild(cubeRotate);

		Alpha rotationAlpha = new Alpha(-1, 2000);
		RotationInterpolator rotator = 
			new RotationInterpolator(rotationAlpha, cubeRotate, 
					new Transform3D(), 0.0f, (float) Math.PI * 2.0f);
		rotator.setSchedulingBounds(bounds);
		cubeRotate.addChild(rotator);   

		cubeRotate.addChild(new ColorCube());
  
		// try adding another cube to the system
		Transform3D boxPosition2 = new Transform3D();
		boxPosition2.set(new Vector3f(-5, 1, 0));
		TransformGroup box2TG = new TransformGroup(boxPosition2);
		cubeRotate.addChild(box2TG);
		
		TransformGroup scaleTG = new TransformGroup();
		scaleTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		scaleTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		Alpha scaleAlpha = new Alpha(-1, 2000);
		scaleAlpha.setMode(Alpha.DECREASING_ENABLE | Alpha.INCREASING_ENABLE);
		scaleAlpha.setDecreasingAlphaDuration(2000);
		ScaleInterpolator scaleInt = new ScaleInterpolator(scaleAlpha, 
		        scaleTG, new Transform3D(), 0.25f, 10);
		scaleInt.setSchedulingBounds(bounds);
		scaleTG.addChild(scaleInt);
		
		scaleTG.addChild(new ColorCube(0.5));
		box2TG.addChild(scaleTG);
		//        
		//        // allow the transform group to be written to by behaviors
		//        box2TG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		//        cubeRotate.addChild(box2TG);
		//        
		//        // rotate the other box on its own
		//        TransformGroup box2Rotate = new TransformGroup();
		//        box2Rotate.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		//        box2TG.addChild(box2Rotate);
		//        
		//        Alpha rotationAlpha2 = new Alpha(-1, 1000);
		//        RotationInterpolator rotator2 = 
		//            new RotationInterpolator(rotationAlpha2, box2Rotate, 
		//                    new Transform3D(), 0.0f, (float) Math.PI * 2.0f);
		//        rotator2.setSchedulingBounds(bounds);
		//        box2Rotate.addChild(rotator2);   
		//
		//        box2Rotate.addChild(new ColorCube(0.5));
	}

	private void addBackground() {
		Background back = new Background();
		back.setApplicationBounds( bounds );
		back.setColor(0.30f, 0.75f, 0.95f);

		//        try{
		//            BufferedImage bi = ImageIO.read(new File("mountains.jpg"));
		//            ImageComponent2D ic = new ImageComponent2D(bi.getType(), bi);
		//            back.setImage(ic);
		//        }
		//        catch(Exception e){
		//            System.out.println("Failed to load image");
		//        }

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

