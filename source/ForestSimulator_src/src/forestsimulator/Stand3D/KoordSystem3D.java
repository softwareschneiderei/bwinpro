/*
 * KoordSystem3D.java
 *
 * Created on 28. Februar 2006, 10:25
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package forestsimulator.Stand3D;

import javax.media.j3d.*;
import javax.vecmath.*;

/**
 *
 * @author jhansen
 */
public class KoordSystem3D{
    private BranchGroup bg=new BranchGroup();
    /** Creates a new instance of KoordSystem3D */
    public KoordSystem3D(StandBase3D base) {
        //TransformGroup tgx= makeAxis(base);
        TransformGroup tgy= makeAxis(base);
        //TransformGroup tgz= makeAxis(base);
        //Transform3D transform = new Transform3D();
        //transform.rotZ(0.5*Math.PI);
        //tgx.setTransform(transform);
        //transform= new Transform3D();
        //transform.rotX(0.5*Math.PI);
        //tgz.setTransform(transform);
        //bg.addChild(tgx);
        bg.addChild(tgy);
        //bg.addChild(tgz);
        bg.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
        bg.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE );
        bg.setCapability(BranchGroup.ALLOW_DETACH);
        bg.setUserData(new Integer(3));
    }
    
    private TransformGroup makeAxis(StandBase3D base){
        TransformGroup result= new TransformGroup();
        float x0=-(float)base.getCorrX();
        float y0=-(float)base.getCorrY();
        boolean dummy=true;
        for(int i=0; i<10; i++){
            double y=i*1;
            Appearance a;
            if(dummy) a =createMatAppear(new Color3f(1.0f, 0.0f, 0.0f),new Color3f(1.0f, 0.0f, 0.0f), 100.0f);
            else a =createMatAppear(new Color3f(1.0f, 1.0f, 1.0f),new Color3f(1.0f, 1.0f, 1.0f), 100.0f);
            FrustrumNew seg= new FrustrumNew(x0,(float)y,y0, 0.1f,0.1f, 1.0f, 9, a); 
            seg.getShape().getGeometry().setCapability(Geometry.ALLOW_INTERSECT);
            dummy=(!dummy);
            result.addChild(seg.getShape());
        }
        return result;        
    }
    
     private Appearance createMatAppear(Color3f dColor, Color3f sColor, float shine) {
		Appearance appear = new Appearance();
		Material material = new Material();
		material.setDiffuseColor(dColor);
		material.setSpecularColor(sColor);
                material.setAmbientColor(sColor);
                material.setShininess(shine);               
		appear.setMaterial(material);		
		return appear;
	}
     public BranchGroup getAxis(){
         return bg;
     }
     
     public void clean(){
         bg.detach();
         bg=null;
     }
    
}
