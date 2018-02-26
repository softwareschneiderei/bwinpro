/*
 * SimpleCrownSchadow3D.java
 *
 * Created on 27. Februar 2006, 10:45
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package forestsimulator.Stand3D;
import javax.media.j3d.*;
import javax.vecmath.*;
import treegross.base.Tree;

/**
 *
 * @author jhansen
 */
public class SimpleCrownSchadow3D {
    private Disc shadow;
    private Color3f grey= new Color3f(0.0f, 0.0f,0.0f); //for normal shadow
    private Color3f red=  new Color3f(1.0f, 0.0f, 0.0f);  //for dead tree shadow
    private double x=0;
    private double z=0;
    private double y=0;
    /** Creates a new instance of SimpleCrownSchadow3D */
    public SimpleCrownSchadow3D(Tree tree, int quality, RenderingAttributes ra, StandBase3D base) {       
        Appearance a=new Appearance();
        Color3f color;
        if(tree.out==-1) color=grey;
        else color=red;
        a.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.NICEST,0.3f));
        if(ra!=null){
            a.setRenderingAttributes(ra);
            a.setCapability(a.ALLOW_RENDERING_ATTRIBUTES_READ);
            a.setCapability(a.ALLOW_RENDERING_ATTRIBUTES_WRITE);
        }
        y=tree.z+0.02;
        double r=(tree.cw/2)*1.5;
        double cb=tree.cb; 
        /*x=-tree.x;
        z=tree.y;*/        
        x=-1*(tree.x-base.corrx);
        z= tree.y-base.getCorrY();
        
        Vector3f normal;
        if(base==null) normal= new Vector3f(0f, 1f, 0f);
        else{
            normal= base.getNormalAtPoint(x, z);
            if(normal==null)normal= new Vector3f(0f, 1f, 0f);
        }        
        shadow = new Disc(x,y,z, r, quality,  a, normal, true, color, base); 
        shadow.getShape().getGeometry().setCapability(Geometry.ALLOW_INTERSECT);
    }
    
   private Appearance createMatAppear(Color3f Color, float shine) {
		Appearance appear = new Appearance();
                Material material = new Material();
		material.setDiffuseColor(Color);
		material.setSpecularColor(Color);
                material.setAmbientColor(Color);
                material.setShininess(shine);               
		appear.setMaterial(material);             
                /*if(bltexture!=null){
                    TextureUnitState[] ta = new TextureUnitState[2];
                    TextureAttributes texAttr1 = new TextureAttributes(TextureAttributes.REPLACE, new Transform3D(), new Color4f(1f,1f, 1f, 1.0f), TextureAttributes.NICEST);
                    TextureAttributes texAttr2 = new TextureAttributes(TextureAttributes.COMBINE_SRC_ALPHA, new Transform3D(), new Color4f(1f,1f, 1f, 1.0f), TextureAttributes.NICEST);
                    ta[0]= new TextureUnitState(bltexture[tex1], texAttr1, null);
                    ta[1]= new TextureUnitState(bltexture[tex2], texAttr2, null); 
                    appear.setTextureUnitState(ta);
                }*/
            return appear;
	}
   public Shape3D getShadow(){
       return shadow.getShape();
   }
    
}
