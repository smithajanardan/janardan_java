package ClassExamples;


//HelloUniverse.java

//2006 Sun Microsystems, Inc.
//Simplified by Andrew Davison, ad@fivedots.coe.psu.ac.th, June 2007


import java.awt.*;
import javax.swing.*;

import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.ColorCube;
import javax.media.j3d.*;
import javax.vecmath.*;


public class HelloUniverse extends JFrame {


    public HelloUniverse() {
        // create a Swing panel inside the JFrame
        JPanel p = new JPanel();
        p.setLayout( new BorderLayout() );
        p.setPreferredSize( new Dimension(400, 400) );
        getContentPane().add(p, BorderLayout.CENTER);

        // add the 3D canvas to panel
        Canvas3D c3d = createCanvas3D();
        p.add(c3d, BorderLayout.CENTER);

        // configure the window (JFrame)
        setTitle("HelloUniverse");
        setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        pack();
        setVisible(true);
    }  // end of HelloUniverse()



    private Canvas3D createCanvas3D() {
        /* Build a 3D canvas holding a SimpleUniverse which contains
          the 3D scene (a rotating colored cube) */

        // get the preferred graphics configuration for the default screen
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

        // create a Canvas3D using the preferred configuration
        Canvas3D c3d = new Canvas3D(config);

        // create a simple universe
        SimpleUniverse univ = new SimpleUniverse(c3d);

        // move the camera back a bit so the cube can be seen
        univ.getViewingPlatform().setNominalViewingTransform();

        // ensure at least one redraw every 5 ms
        univ.getViewer().getView().setMinimumFrameCycleTime(5);

        // add the scene to the universe
        BranchGroup scene = createSceneGraph();
        univ.addBranchGraph(scene);

        return c3d;
    }  // end of createCanvas3D()



        /* The scene graph is:
      scene ---> tg ---> colored cube
            |
            ---> rotator
         */
        

        /* Create a TransformGroup node. Enable its TRANSFORM_WRITE 
    capability so it can be affected at run time */
//        Transform3D rotate1 = new Transform3D();
//        Transform3D rotate2 = new Transform3D();
//        rotate1.rotX(Math.PI / 4.0d);
//        rotate2.rotY(Math.PI / 4.0d);
//        rotate1.mul(rotate2);
//        
//        TransformGroup tg = new TransformGroup(rotate1);
    
    public BranchGroup createSceneGraph() { 
        BranchGroup scene = new BranchGroup();
        
        Transform3D pos = new Transform3D();
        Vector3f p = new Vector3f(2, 4, -10);
        pos.setTranslation(p);
        TransformGroup tg = new TransformGroup();
        
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        scene.addChild(tg);   // add to the scene 

        // connect a coloured cube to the TransformGroup
        tg.addChild( new ColorCube(.4) );
        
        /* Create a rotation behaviour (a rotation interpolator) 
         * which will   make the cube spin around its y-axis, 
         * taking 4 secs to do one rotation. 
         */
        
        Transform3D yAxis = new Transform3D();
        
        // experiment
        yAxis.rotZ(Math.PI / 4);
        
        Alpha rotationAlpha = new Alpha(-1, 4000);   // 4 secs
        
        RotationInterpolator rotator = 
            new RotationInterpolator(rotationAlpha, tg, 
                    yAxis, 0.0f, (float) Math.PI*2.0f);
        
        rotator.setSchedulingBounds(
                new BoundingSphere( new Point3d(0,0,0), 100.0) );
        
        scene.addChild(rotator);    // add to the scene
        
//      // exp, try adding another cube
      Transform3D otherT = new Transform3D();
      otherT.setTranslation(new Vector3f(.5f, .5f, 0));
      TransformGroup otherTG = new TransformGroup(otherT);
      otherTG.addChild(new ColorCube(0.33));
      scene.addChild(otherTG);
        
        // optimize the scene graph
        scene.compile();
        return scene;


        

        

    }  // end of createSceneGraph()



    // ------------------------------------------------------------

    public static void main(String args[]) {
        new HelloUniverse();  }


} // end of HelloUniverse class

