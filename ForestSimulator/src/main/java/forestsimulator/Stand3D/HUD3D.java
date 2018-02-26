package forestsimulator.Stand3D;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.PlatformGeometry;
import com.sun.j3d.utils.universe.SimpleUniverse;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.Text2D;
import java.awt.Font;


/**
 *
 * @author jhansen
 */
public class HUD3D {
    private Shape3D hud;
    private TransformGroup hudtext;
    private Shape3D text2d;
    private static float hudwidth =0.036f;
    private static float hudheight=0.02f;

    
    /** Creates a new instance of HUD3D */
    public HUD3D(SimpleUniverse u) {        
        PlatformGeometry pg=u.getViewingPlatform().getPlatformGeometry();       
        TransformGroup tg = new TransformGroup();     
        hud=makeHUD();
        text2d=getText2D("");
        setNoText();
        hudtext=makeTxt();
        //tg.addChild(hud);
        tg.addChild(hudtext);
        if(pg!=null)pg.addChild(tg);
        else{
            pg=new PlatformGeometry();
            pg.addChild(tg);
            u.getViewingPlatform().setPlatformGeometry(pg);
        }
    }
    

    
    private Shape3D makeHUD(){
        Shape3D plane = new Shape3D(createGeometry(hudwidth, hudheight/2, -0.1f, new Color3f(0f, 0.3f,0f)), createAppear());
        return plane;
    }
    
    private Shape3D getText2D(String text){        
        Shape3D s=new Text2D(text,new Color3f(1f, 0f, 0f),
                              "Serif",40,Font.BOLD);     
        
        Appearance a = s.getAppearance();
       
        TextureAttributes ta = new TextureAttributes();
        ta.setTextureMode(TextureAttributes.MODULATE);
        //ta.setTextureMode(TextureAttributes.REPLACE);
        //ta.setTextureMode(TextureAttributes.DECAL);
        //ta.setTextureMode(TextureAttributes.BLEND);
        ta.setPerspectiveCorrectionMode(TextureAttributes.FASTEST);
        ta.setTextureBlendColor(new Color4f(0, 0, 0, 0));
        a.setTextureAttributes(ta);
        s.setAppearance(a);
        s.setCollidable(false);
        s.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
        s.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
        s.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
        s.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
        return s;
    }    
   
    
    public void setText2D(String text){
        text2d.removeAllGeometries();
        Shape3D dummy=getText2D(text.trim());
        text2d.addGeometry(dummy.getGeometry());        
        text2d.setAppearance(dummy.getAppearance());
    }
    
    public void setNoText(){
        text2d.removeAllGeometries();
    }
    
    private TransformGroup makeTxt(){
        TransformGroup tg= new TransformGroup();        
        Transform3D trans= new Transform3D();
        trans.setScale(0.02);
        trans.setTranslation(new Vector3d(-(hudwidth/2), -0.002, -0.1));        
        tg.setTransform(trans);
        if(text2d!=null) tg.addChild(text2d);
        return tg;
    }  
    
    private Geometry createGeometry(float x, float y, float z, Color3f color){
        QuadArray plane =new QuadArray(4,GeometryArray.COORDINATES | GeometryArray.COLOR_4);
        Point3f p=new Point3f();
        p.set(-x,y-0.04f,z);
        plane.setCoordinate(0,p);
        plane.setColor(0,new Color4f(color.x,color.y,color.z,1f));       
        p.set(-x,-y-0.04f,z);
        plane.setCoordinate(1,p);
        plane.setColor(1,new Color4f(color.x,color.y,color.z,0.2f));   
        p.set(x,-y-0.04f,z);
        plane.setCoordinate(2,p);
        plane.setColor(2,new Color4f(color.x,color.y,color.z,0.2f));   
        p.set(x,y-0.04f,z);
        plane.setCoordinate(3,p);
        plane.setColor(3,new Color4f(color.x,color.y,color.z,1f));   
        plane.setCapability(plane.ALLOW_INTERSECT);
        return plane;
    }
    
    private Appearance createAppear(){
        Appearance a= new Appearance();
        TransparencyAttributes ta = new TransparencyAttributes(TransparencyAttributes.NICEST,0.3f);
        a.setTransparencyAttributes(ta);  
        RenderingAttributes ra= new RenderingAttributes();
        ra.setDepthBufferEnable(false);
        ra.setDepthBufferWriteEnable(false);
        a.setRenderingAttributes(ra);
        return a;
    }  

    
}
