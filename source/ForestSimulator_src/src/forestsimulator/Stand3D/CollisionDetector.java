/*
 * CollisionDetector.java
 *
 * Created on 9. März 2006, 10:37
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package forestsimulator.Stand3D;

/**
 *
 * @author jhansen
 */

import java.util.Enumeration;
import javax.media.j3d.*;
import javax.vecmath.*;

public class CollisionDetector extends Behavior {
    private static final Color3f highlightColor =
	new Color3f(0.0f, 1.0f, 0.0f);
    private static final ColoringAttributes highlight =
	new ColoringAttributes(highlightColor,
			       ColoringAttributes.SHADE_GOURAUD);

    private boolean inCollision = false;
    private Shape3D shape;
    private ColoringAttributes shapeColoring;
    private Appearance shapeAppearance;

    private WakeupOnCollisionEntry wEnter;
    private WakeupOnCollisionExit wExit;


    public CollisionDetector(Shape3D s) {
	shape = s;
	shapeAppearance = shape.getAppearance();
	shapeColoring = shapeAppearance.getColoringAttributes();
	inCollision = false;
    }

    public void initialize() {
	wEnter = new WakeupOnCollisionEntry(shape, wEnter.USE_GEOMETRY);
	wExit = new WakeupOnCollisionExit(shape, wExit.USE_GEOMETRY);
	wakeupOn(wEnter);
    }
    
    public boolean isInCollison(){
        if(inCollision) return true;
        else return false;
    }
    

    public void processStimulus(Enumeration criteria) {
	inCollision = !inCollision;

	if (inCollision) {
	    shapeAppearance.setColoringAttributes(highlight);
	    wakeupOn(wExit);
	}
	else {
	    shapeAppearance.setColoringAttributes(shapeColoring);
	    wakeupOn(wEnter);
	}
    }
}
