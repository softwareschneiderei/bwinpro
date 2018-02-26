/*
 * TreeText3D.java
 *
 * Created on 1. März 2006, 10:45
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package forestsimulator.Stand3D;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.Font;
import treegross.base.Tree;
import com.sun.j3d.utils.geometry.ColorCube;

/**
 *
 * @author jhansen
 */
public class TreeText3D{
    
    /** Creates a new instance of TreeText3D */
    public TreeText3D() {      
    }
    
    public void makeText(Tree tr, BranchGroup bg){
        bg.setCapability(BranchGroup.ALLOW_DETACH);
        TransformGroup tg=new TransformGroup();
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        make3DTxt(tr, tg, bg);
        addRotation(tr,tg);     
    }
    
    private void make3DTxt(Tree tr, TransformGroup tg, BranchGroup bg){
        UserData userdata= new UserData(tr,2, 0,0);
        TransformGroup tg2= new TransformGroup();     
        if(tr.habitat || tr.tempcrop || tr.crop || tr.out>-1){
            //ColorCube c = new ColorCube(0.4f);
            String status="";
            if(tr.habitat) status="H";
            if(tr.crop) status="Z";
            if(tr.tempcrop) status="tZ";
            if(tr.out>-1) status="+"+tr.outtype;
            tg2.addChild(make3DTextShape(status));
            Transform3D trans = new Transform3D();
            trans.setTranslation(new Vector3d(-tr.x,tr.h,tr.y));
            tg2.setTransform(trans);
            tg.addChild(tg2);
            bg.addChild(tg);
            bg.setUserData(userdata);          
            bg.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
            bg.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
            bg.setCapability(BranchGroup.ALLOW_DETACH);
        }        
    }
    
    private Shape3D make3DTextShape(String text){
        float sl = text.length();
        Font3D f3d;
	f3d = new Font3D(new Font("font01", Font.PLAIN, 2),new FontExtrusion());
	Text3D txt = new Text3D(f3d, text, new Point3f(0-(sl/2f), 0, 0));
        txt.setCapability(Geometry.ALLOW_INTERSECT);
	Shape3D sh = new Shape3D();
	Appearance app = new Appearance();
	Material mm = new Material();
        mm.setEmissiveColor(1.0f, 0.0f, 0.0f);
        mm.setDiffuseColor(1.0f, 0.0f, 0.0f);
        mm.setShininess(100.0f);
	mm.setLightingEnable(true);
	app.setMaterial(mm);
	sh.setGeometry(txt);
	sh.setAppearance(app);
        sh.setBoundsAutoCompute(true);
        return sh;
    }
    
    private void addRotation(Tree tr, TransformGroup tg){
        BoundingSphere bounds =new BoundingSphere(new Point3d(0,0,0), 200.0);
        Transform3D yAxis = new Transform3D();
        yAxis.setTranslation(new Vector3d(-tr.x,0f,tr.y));
	Alpha rotationAlpha = new Alpha(-1, 6000);
        RotationInterpolator rotator = new RotationInterpolator(rotationAlpha, tg, yAxis, 0.0f, (float)Math.PI*2.0f);
	rotator.setSchedulingBounds(bounds);
	tg.addChild(rotator);     
        Transform3D trans = new Transform3D();
        trans.setTranslation(new Vector3d(-tr.x,tr.h,tr.y));
        tg.setTransform(trans);
    }   
    
}
