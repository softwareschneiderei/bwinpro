/*
 * WalkBehavior.java
 *
 * Created on 8. März 2006, 16:24
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

import java.awt.event.*;
import java.awt.AWTEvent;
import javax.media.j3d.*;
import java.util.Enumeration;
import javax.vecmath.*;

// Mover behavior class - used to allow viewer to move using arrow keys
class WalkBehavior extends Behavior{
    CollisionDetector coll=null;
    Point3d lookfrom=new Point3d();
    Point3d lookat=new Point3d();
    private Vector3d vr;
    private Point3d oldfrom= new Point3d();
    private Point3d oldat= new Point3d();
    private static double speedfac=0.35;
    WakeupOnAWTEvent w1 = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
    WakeupCriterion[] w2 = {w1};
    WakeupCondition w = new WakeupOr(w2);
    TransformGroup viewTransformGroup;
    TransformGroup walker=null;
    StandBase3D terain=null;
    double rotation = (Math.PI/180.0)*1.5;		// holds current rotation radians
    double beta=0;
    
    public void initialize() {
	// Establish initial wakeup criteria
	wakeupOn(w);
    }


    /**
     *  Override Behavior's stimulus method to handle the event.
     */
    public void processStimulus(Enumeration criteria) {
	WakeupOnAWTEvent ev;
	WakeupCriterion genericEvt;
	AWTEvent[] events;
   
	while (criteria.hasMoreElements()) {
	    genericEvt = (WakeupCriterion) criteria.nextElement();
	    if (genericEvt instanceof WakeupOnAWTEvent) {
		ev = (WakeupOnAWTEvent) genericEvt;
		events = ev.getAWTEvent();
		processManualEvent(events);
	    }
	}
	// Set wakeup criteria for next time
	wakeupOn(w);
    }

    
    /**
     *  Process a keyboard event to move or rotate the viewer.
     */
    void processManualEvent(AWTEvent[] events) {

	for (int i = 0; i < events.length; ++i) {
	    if (events[i] instanceof KeyEvent) {
		KeyEvent event = (KeyEvent)events[i];
		if (event.getKeyCode() == KeyEvent.VK_EQUALS) {
		    continue;
		}
                // berechne richtungsvector               
                double x=lookat.x-lookfrom.x;
                double y=lookat.y-lookfrom.y;
                double z=lookat.z-lookfrom.z;
                vr= new Vector3d(x,y,z);
                Vector3d vrot=new Vector3d(x,y, z);
                vr.normalize();                
                if (event.getKeyCode() == KeyEvent.VK_UP) {
		    lookfrom.x=lookfrom.x+(vr.x*speedfac);
                    lookfrom.z=lookfrom.z+(vr.z*speedfac);
                    lookat.x=lookat.x+(vr.x*speedfac);
                    lookat.y=lookat.y;
                    lookat.z=lookat.z+(vr.z*speedfac);
                    beta=beta+0.5;
                    if (beta>Math.PI*2) beta=0; 
                    if(terain!=null)lookfrom.y=terain.getHeightAtPoint(lookfrom.x,lookfrom.z, false)+1.5+(Math.cos(beta)/10);
                    else lookfrom.y=1.5+(Math.cos(beta)/10);                      
		}
		else if (event.getKeyCode() == KeyEvent.VK_DOWN) {
		    lookfrom.x=lookfrom.x-(vr.x*speedfac);                                        
                    lookfrom.z=lookfrom.z-(vr.z*speedfac);
                    beta=beta+0.5;
                    if (beta>Math.PI*2) beta=0; 
                    if(terain!=null)lookfrom.y=terain.getHeightAtPoint(lookfrom.x,lookfrom.z, false)+1.5+(Math.cos(beta)/10);
                    else lookfrom.y=1.5+(Math.cos(beta)/10);
                    lookat.x=lookat.x-(vr.x*speedfac);                    
                    lookat.y=lookat.y;
                    lookat.z=lookat.z-(vr.z*speedfac);
		}
		else if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
                    double xrot=(vrot.x*Math.cos(rotation)) -(vrot.z*Math.sin(rotation));
                    double yrot=vrot.y;
                    double zrot=(vrot.x*Math.sin(rotation)) +(vrot.z*Math.cos(rotation));
                    lookat.x=lookfrom.x+xrot;
                    lookat.y=lookfrom.y+yrot;
                    lookat.z=lookfrom.z+zrot;
		}
		else if (event.getKeyCode() == KeyEvent.VK_LEFT) {
		    double xrot=(vrot.x*Math.cos(-rotation)) -(vrot.z*Math.sin(-rotation));
                    double yrot=vrot.y;
                    double zrot=(vrot.x*Math.sin(-rotation)) +(vrot.z*Math.cos(-rotation));
                    lookat.x=lookfrom.x+xrot;
                    lookat.y=lookfrom.y+yrot;
                    lookat.z=lookfrom.z+zrot;
		}
                else if (event.getKeyCode() == KeyEvent.VK_PAGE_UP) {
		    lookat.y=lookat.y+1.0;
		}
                else if (event.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
		    lookat.y=lookat.y-1.0;
		}
                
		setViewAndPosition();
	    }
	}
    }
    
    private void setViewAndPosition(){
        Transform3D translate = new Transform3D();
        if(walker!=null){
            Vector3d v= new Vector3d(lookfrom.x, lookfrom.y,  lookfrom.z);
            translate.setTranslation(v);
            walker.setTransform(translate);
            if(coll.isInCollison()){               
                /*System.out.println(lookfrom.toString());
                System.out.println(oldfrom.toString());*/
                lookfrom=new Point3d(oldfrom.x-vr.x, oldfrom.y, oldfrom.z-vr.z);
                lookat= new Point3d(oldat.x-vr.x, oldat.y, oldat.z-vr.z);
                v= new Vector3d(oldfrom.x-vr.x, oldfrom.y,  oldfrom.z-vr.z);
                translate.set(v);
                walker.setTransform(translate);
            }
        }
        oldfrom=new Point3d(lookfrom.x, lookfrom.y, lookfrom.z);
        oldat=  new Point3d(lookat.x, lookat.y, lookat.z);
        Vector3d upvector = new Vector3d(0,1, 0);
        translate.lookAt(lookfrom, lookat,  upvector);
        translate.invert();        
        viewTransformGroup.setTransform(translate);
    }  
  
    /**
     *  Constructor 
     */
    public WalkBehavior(TransformGroup trans, Point3d _lookfrom, Point3d _lookat, TransformGroup walkingobj, CollisionDetector cd, StandBase3D base) {
	viewTransformGroup = trans; 
        terain=base;
        if(walkingobj!=null)walker=walkingobj;
        if(cd!=null)coll=cd;
        lookfrom= new Point3d(_lookfrom.x, _lookfrom.y, _lookfrom.z);
        lookat= new Point3d(_lookat.x, _lookat.y, _lookat.z);
	Bounds bound = new BoundingSphere(new Point3d(0.0,0.0,0.0),10000.0);
	this.setSchedulingBounds(bound);
    }
}



