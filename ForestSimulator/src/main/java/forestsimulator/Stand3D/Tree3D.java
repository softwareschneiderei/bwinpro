/*
 * Tree3D.java
 *
 * Created on 6. April 2006, 12:20
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package forestsimulator.Stand3D;

import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Sphere;
import javax.media.j3d.*;
import javax.vecmath.*;
import treegross.base.Tree;

/**
 *
 * @author jhansen
 */
public class Tree3D {

    private final BranchGroup bgtree;
    private RenderingAttributes ra;
    private RenderingAttributes ramarker;
    private RenderingAttributes ramarkercrown;
    private Material macrown;
    private Material matrunk;
    private Material mamarker;
    private TextureUnitState[] tustrunk;
    private TextureUnitState[] tuscrown;
    private final Appearance apcrown;
    private final Appearance aptrunk;
    private Appearance apmarker;
    private Color3f speciescolor;
    private Color3f naturalcolor;
    private Color3f trunkcolor;
    private Color3f grey;    
    private boolean wascolored;
    private boolean showstatus;
    public UserData userdata;
    private final double x;
    private final double y; //coordinates of tree in virtual universe
    private final double z; //coordinates of tree in virtual universe
    
    public Tree3D(Tree tgtree, Texture2D[] bltexture, boolean speciescolor, boolean textured, Group parent, StandBase3D base) {       
         //init the tree  
         x=-1*(tgtree.x-base.corrx);
         z= tgtree.y-base.getCorrY();
         if (base == null) {
            y = tgtree.z;
        } else {
            y = base.getHeightAtPoint(x, z, false);
        }

         userdata=new UserData(tgtree,1, x, z);//stores treedata
         initColors(tgtree);
         initAppearObj(bltexture, tgtree);
         apcrown=makeAppearanceCrown();
         aptrunk=makeAppearanceTrunk();
         setSpeciesColorOn(speciescolor);
         setTextured(textured);      
         // init tree j3dgroups and geometrys
         bgtree= new BranchGroup();
         TransformGroup roottg= new TransformGroup();
         roottg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
         roottg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE); 
         roottg.setCapability(TransformGroup.ALLOW_CHILDREN_READ);         
         // compute/rescale treedata:       
         double dbh=tgtree.d/200;
         double h=tgtree.h;
         double cw=tgtree.cw;
         double cb=tgtree.cb; 
         // add all elements of the tree to roottg:        
         if((tgtree.outtype==0 && tgtree.out==-1) || (tgtree.outtype!=0 && tgtree.out!=-1)){
            roottg.addChild(new SimpleCrownSchadow3D(tgtree,18, ra, base).getShadow()); 
            if(tgtree.sp.spDef.crownType==1) roottg.addChild(this.makeCrownConi(x, y, z, h, cw, cb));
            else roottg.addChild(this.makeCrownBL(x, y, z, cb, cw, h));
         }
         roottg.addChild(makeTrunk( x, y, z, h, dbh));
         roottg.addChild(makeMarker());
         roottg.addChild(makeMarkerCrown());
         bgtree.addChild(roottg); 
         bgtree.setBounds(computeBoundingBox(tgtree, x, z));
         bgtree.setUserData(userdata);
         bgtree.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
         bgtree.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
         bgtree.setCapability(BranchGroup.ALLOW_COLLIDABLE_READ);
         bgtree.setCapability(BranchGroup.ALLOW_COLLIDABLE_WRITE);
         bgtree.setCapability(BranchGroup.ALLOW_PICKABLE_READ);
         bgtree.setCapability(BranchGroup.ALLOW_PICKABLE_WRITE);
         bgtree.setCapability(BranchGroup.ALLOW_DETACH);
         bgtree.compile();  
         parent.addChild(bgtree);
    }
    
    private void initColors(Tree tr){
        speciescolor=makeSpeciesColor(tr);
        naturalcolor=RealisticColors3D.getCrownColor(tr.code, tr.sp.spDef.crownType);
        if(tr.out!=-1 && tr.outtype==0) trunkcolor=new Color3f(0.15f, 0.15f, 0.15f);
        else trunkcolor=RealisticColors3D.getTrunkColor(tr.code, tr.sp.spDef.crownType);        
        grey=new Color3f(0.8f,0.8f,0.8f);        
    }
    
    private void initAppearObj(Texture2D[] bltexture, Tree tr){
        // ra is used in all appearances
        ra= new RenderingAttributes();
        ra.setCapability(RenderingAttributes.ALLOW_VISIBLE_READ);
        ra.setCapability(RenderingAttributes.ALLOW_VISIBLE_WRITE);    
        // material crown
        macrown= new Material();
        macrown.setCapability(Material.ALLOW_COMPONENT_READ);
        macrown.setCapability(Material.ALLOW_COMPONENT_WRITE);
        macrown.setShininess(50.0f); 
        useNaturalColor();
        // material trunk
        matrunk= new Material();
        matrunk.setDiffuseColor(trunkcolor);
        matrunk.setSpecularColor(trunkcolor);
        matrunk.setAmbientColor(trunkcolor);
        matrunk.setShininess(50.0f);     
        // textures trunk:
        tustrunk = new TextureUnitState[2];       
        TextureAttributes texAttr1 = new TextureAttributes();
        texAttr1.setTextureMode(TextureAttributes.MODULATE);                    
        TextureAttributes texAttr2 = new TextureAttributes();
        texAttr2.setTextureMode(TextureAttributes.COMBINE_SRC_ALPHA);                    
        tustrunk[0]= new TextureUnitState(bltexture[5], texAttr1, null);
        tustrunk[1]= new TextureUnitState(bltexture[1], texAttr2, null);        
        // textures crown:
        int tex1 = 0;
        if (tr.sp.spDef.crownType == 1) {
            tex1 = 2;//conifer
        }
        tuscrown = new TextureUnitState[2];       
        TextureAttributes texAttr1c = new TextureAttributes();
        texAttr1c.setTextureMode(TextureAttributes.MODULATE);                    
        TextureAttributes texAttr2c = new TextureAttributes();
        texAttr2c.setTextureMode(TextureAttributes.COMBINE_SRC_ALPHA);                    
        tuscrown[0]= new TextureUnitState(bltexture[tex1], texAttr1c, null);
        tuscrown[1]= new TextureUnitState(bltexture[1], texAttr2c, null);
        //maker
        mamarker= new Material();
        mamarker.setCapability(Material.ALLOW_COMPONENT_READ);
        mamarker.setCapability(Material.ALLOW_COMPONENT_WRITE);
        mamarker.setShininess(50.0f);
        ramarker= new RenderingAttributes();
        ramarker.setCapability(RenderingAttributes.ALLOW_VISIBLE_READ);
        ramarker.setCapability(RenderingAttributes.ALLOW_VISIBLE_WRITE);  
        ramarkercrown= new RenderingAttributes();
        ramarkercrown.setCapability(RenderingAttributes.ALLOW_VISIBLE_READ);
        ramarkercrown.setCapability(RenderingAttributes.ALLOW_VISIBLE_WRITE);      
        updateMarker();
        setShowTreeStatus(false);
    }
    
    private Color3f makeSpeciesColor(Tree tr){
        int r=tr.sp.spDef.colorRed;
        int g=tr.sp.spDef.colorGreen;
        int b=tr.sp.spDef.colorBlue;
        double newr=(double)r/255.0;
        double newg=(double)g/255.0;
        double newb=(double)b/255.0;
        return new Color3f((float)newr,(float)newg, (float)newb);        
    }
    
    private Appearance makeAppearanceTrunk(){
        Appearance ap= new Appearance();
        ap.setCapability(Appearance.ALLOW_MATERIAL_READ);
        ap.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
        ap.setCapability(Appearance.ALLOW_TEXTURE_UNIT_STATE_WRITE);
        ap.setCapability(Appearance.ALLOW_TEXTURE_UNIT_STATE_READ);
        ap.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
        ap.setMaterial(matrunk);
        ap.setRenderingAttributes(ra);
        ap.setTextureUnitState(tustrunk);
        return ap;
    }
    
    private Appearance makeAppearanceCrown(){
        Appearance ap= new Appearance();
        ap.setCapability(Appearance.ALLOW_MATERIAL_READ);
        ap.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
        ap.setCapability(Appearance.ALLOW_TEXTURE_UNIT_STATE_WRITE);
        ap.setCapability(Appearance.ALLOW_TEXTURE_UNIT_STATE_READ);
        ap.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
        ap.setMaterial(macrown);
        ap.setRenderingAttributes(ra);
        ap.setTextureUnitState(tuscrown);
        return ap;
    }  
    
    private Appearance makeAppearanceMarker(){
        Appearance ap= new Appearance();
        ap.setCapability(Appearance.ALLOW_MATERIAL_READ);
        ap.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
        ap.setMaterial(mamarker);
        ap.setRenderingAttributes(ramarker);
        return ap;
    }   
    
    private Appearance makeAppearanceMarkerCrown(){
        Appearance ap= new Appearance();
        ap.setCapability(Appearance.ALLOW_MATERIAL_READ);
        ap.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
        ap.setMaterial(mamarker);
        ap.setRenderingAttributes(ramarkercrown);
        return ap;
    }  
    
    
    public BranchGroup getBG(){
        return bgtree;
    }
    
    public void setVisible(boolean visible){
        ra.setVisible(visible);
        bgtree.setCollidable(visible);
        bgtree.setPickable(visible);
        updateMarker();
        updateMarkerCrown();
    }
    
    public void setShowTreeStatus(boolean visible){
        showstatus=visible;
        updateMarkerCrown();
    }
    
    public void setTextured(boolean textured){
        if(textured){
            /*macrown.setDiffuseColor(grey);
            macrown.setSpecularColor(grey);
            macrown.setAmbientColor(grey);*/
            macrown.setDiffuseColor(naturalcolor);
            macrown.setSpecularColor(naturalcolor);
            macrown.setAmbientColor(naturalcolor);
            //apcrown.setTextureUnitState(tuscrown);
            apcrown.setTextureUnitState(tuscrown);
            aptrunk.setTextureUnitState(tustrunk);            
        }
        else{
            if(!wascolored)useNaturalColor();
            else useSpeciesColor();
            apcrown.setTextureUnitState(null);
            aptrunk.setTextureUnitState(null);
        }
    }
    
    private void useNaturalColor(){ 
        wascolored=false;
        macrown.setDiffuseColor(naturalcolor);
        macrown.setSpecularColor(naturalcolor);
        macrown.setAmbientColor(naturalcolor);
    }
    
    private void useSpeciesColor(){ 
        wascolored=true;
        macrown.setDiffuseColor(speciescolor);
        macrown.setSpecularColor(speciescolor);
        macrown.setAmbientColor(speciescolor);
    }
    
    public void setSpeciesColorOn(boolean on){
        setTextured(false);
        if(on){
            useSpeciesColor();            
        }
        else useNaturalColor();
    }
    
    public void updateMarker(){
        if(userdata.marker==UserData.THINNING){
            mamarker.setDiffuseColor (new Color3f(1.0f, 0.0f, 0.0f));
            mamarker.setSpecularColor(new Color3f(1.0f, 0.0f, 0.0f));
            mamarker.setEmissiveColor(new Color3f(0.6f, 0.0f, 0.0f));
        }
        if(userdata.marker==UserData.CROP){
            mamarker.setDiffuseColor (new Color3f(0.0f, 1.0f, 0.0f));
            mamarker.setSpecularColor(new Color3f(0.0f, 1.0f, 0.0f));
            mamarker.setEmissiveColor(new Color3f(0.0f, 0.6f, 0.0f));
        }
        if(userdata.marker==UserData.TEMP_CROP){
            mamarker.setDiffuseColor (new Color3f(1.0f, 1.0f, 0.0f));
            mamarker.setSpecularColor(new Color3f(1.0f, 1.0f, 0.0f));
            mamarker.setEmissiveColor(new Color3f(0.3f, 3.0f, 0.0f));
        }
        if(userdata.marker==UserData.HABITAT){
            mamarker.setDiffuseColor (new Color3f(0.0f, 0.0f, 1.0f));
            mamarker.setSpecularColor(new Color3f(0.0f, 0.0f, 1.0f));
            mamarker.setEmissiveColor(new Color3f(0.0f, 0.0f, 0.6f));
        }
        if(ra.getVisible() && userdata.marker!= UserData.NOT) ramarker.setVisible(true);
        else ramarker.setVisible(false);
        updateMarkerCrown();
    }
    
    private void updateMarkerCrown(){
        if(ra.getVisible() && userdata.marker!= UserData.NOT && showstatus) ramarkercrown.setVisible(true);
        else ramarkercrown.setVisible(false);
    }
    
    private Shape3D makeMarker(){
        apmarker=makeAppearanceMarker();	
        Frustrum f=new Frustrum((float)x, (float)y+1.3f, (float)z, (float)(userdata.dbh/200.0)+0.01f, (float)(userdata.dbh/200.0), 0.1f, 12, apmarker);
        Shape3D s=f.getShape();      
        return s;        
    }    
    
   private Shape3D makeMarkerCrown(){
        Appearance a=makeAppearanceMarkerCrown();	
        Frustrum f=new Frustrum((float)x, (float)y+(float)userdata.h+0.4f, (float)z, 1f, 0f, 1f, 3, a);
        Shape3D s=f.getShape();      
        return s;        
    }  
    
     public void harvestTree(StandBase3D base){
        if(bgtree!=null){
            double randangle=Math.random()*0.25*Math.PI;
            UserData u=(UserData)bgtree.getUserData();
            u.marker= UserData.NOT;
            u.living=false;
            u.standing=false;
            Transform3D transrot= new Transform3D();
            Transform3D transrand= new Transform3D();
            Transform3D transu= new Transform3D();
            Transform3D transxz= new Transform3D();
            transu.setTranslation(new Vector3d(-x,-y, -z));
            
            transxz.setTranslation(new Vector3d(x, y+u.dbh/200, z));
            transrand.rotY(randangle);
            // Fallwinkel bestimmen
            double fallangle=0.5*Math.PI;
            if(base!=null){
                Transform3D transrottest= new Transform3D();
                Transform3D transrandtest= new Transform3D();
                transrottest.rotZ(0.5*Math.PI);
                transrandtest.rotY(randangle);
                transrandtest.mul(transrottest);
                Vector3d target= new Vector3d(0,u.cb,0);
                Vector3d position= new Vector3d(x,y,z);
                Vector3d upvec= new Vector3d(0,1,0);
                //turn
                transrandtest.transform(target);
                //translate
                target.x=target.x+x;
                target.z=target.z+z;
                target.y=base.getHeightAtPoint(target.x,target.z, false);
                target.sub(position);
                fallangle=target.angle(upvec);                
            }
            //
            transrot.rotZ(fallangle);       
            transrot.mul(transu);
            transrand.mul(transrot);
            transxz.mul(transrand);
            TransformGroup t=(TransformGroup)bgtree.getChild(0);
            t.setTransform(transxz);
            // remove shadow and root
            Shape3D shadow=(Shape3D)t.getChild(0);
            shadow.removeAllGeometries();
            shadow.removeAllGeometries();
            TransformGroup trunk=(TransformGroup)t.getChild(2);
            Shape3D root= (Shape3D)trunk.getChild(1);
            root.removeAllGeometries();
        }        
    }    

    
    //geometrys:
     private BoundingBox computeBoundingBox(Tree tr, double x3d, double y3d){
        Point3d upper=new Point3d(x3d-(tr.cw/2),tr.z, y3d-(tr.cw/2));
        Point3d lower=new Point3d(x3d+(tr.cw/2),tr.z+tr.h, y3d+(tr.cw/2));
        BoundingBox bb=new BoundingBox(lower, upper);
        return bb;
    }
    private BoundingBox computeBoundingBox(double x, double y, double z, double r, double h){
        Point3d upper=new Point3d(x-r,y, z-r);
        Point3d lower=new Point3d(x+r,y+h, z+r);
        BoundingBox bb=new BoundingBox(lower, upper);
        return bb;
    }
    
     private TransformGroup makeTrunk(double x, double y, double z, double h, double dbh){
        TransformGroup transform = new TransformGroup();
        Transform3D translate = new Transform3D();
        //move by vector:
        translate.set(new Vector3d(x, y+(h/2), z));
        transform.setTransform(translate);      
        Cone trunk=new Cone((float)(dbh*1.01), (float)h, (Cone.GENERATE_NORMALS | Cone.GEOMETRY_NOT_SHARED | Cone.GENERATE_TEXTURE_COORDS), aptrunk);
        trunk.getShape(Cone.BODY).setUserData(userdata);
        trunk.getShape(Cone.BODY).getGeometry().setCapability(Geometry.ALLOW_INTERSECT);
        trunk.getShape(Cone.CAP).setUserData(userdata);
        trunk.getShape(Cone.CAP).getGeometry().setCapability(Geometry.ALLOW_INTERSECT);
        trunk.getShape(Cone.BODY).setBounds(computeBoundingBox(0,0,0, dbh*1.01, h)); 
        //roots
        Frustrum root=new Frustrum(0f, (float)(y-(h/2)-0.5), 0f, (float)(dbh*2.5), 0.0f, 1.5f, 6, aptrunk);
        root.getShape().setUserData(userdata);
        root.getShape().getGeometry().setCapability(Geometry.ALLOW_INTERSECT);
        root.getShape().setUserData(userdata);
        root.getShape().getGeometry().setCapability(Geometry.ALLOW_INTERSECT);
        root.getShape().setBounds(computeBoundingBox(0,0,0, dbh*1.01, h));
        root.getShape().setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
        transform.addChild(trunk); 
        transform.addChild(root.getShape());
        transform.setCapability(TransformGroup.ALLOW_CHILDREN_READ); 
        return transform;
    } 
     
      private TransformGroup makeCrownConi(double x, double y, double z, double h, double cw, double cb){
        double cl=h-cb; //crownlength;
        double cllight=cl*0.66; //length of lightcrown;
        double clshadow=cl-cllight; // length of shadowcrown
        TransformGroup transform = new TransformGroup();
        TransformGroup lightcTG = new TransformGroup();
        Transform3D translate = new Transform3D();
        //move by vector:
        translate.set(new Vector3d(x, cb+clshadow+(cllight/2)+y, z));
        lightcTG.setTransform(translate);                
        Cone crownlight=new Cone((float)(cw/2), (float)(cllight), Cone.GENERATE_NORMALS | Cone.GEOMETRY_NOT_SHARED | Cone.GENERATE_TEXTURE_COORDS,12,1, apcrown);
        //FrustrumNew crownlight2= new FrustrumNew((float)x, (float)cb+(float)y+(float)(clshadow), (float)z,(float)(cw/2),(float)(0),(float)(cllight),12,apcrown);
        FrustrumNew crownshadow= new FrustrumNew((float)x, (float)cb+(float)y, (float)z,(float)(cw/3),(float)(cw/2),(float)(clshadow),12,apcrown);
        Disc crownbottom;
        crownbottom= new Disc(x, cb+y, z, (cw/3), 12, apcrown, new Vector3f(0f, -1f,0f), true, naturalcolor, null); 
        crownlight.getShape(Cone.BODY).setUserData(userdata);
        crownlight.getShape(Cone.BODY).getGeometry().setCapability(Geometry.ALLOW_INTERSECT);
        crownlight.getShape(Cone.CAP).setUserData(userdata);
        crownlight.getShape(Cone.CAP).getGeometry().setCapability(Geometry.ALLOW_INTERSECT);
        crownshadow.getShape().setUserData(userdata); 
        crownshadow.getShape().getGeometry().setCapability(Geometry.ALLOW_INTERSECT);
        crownbottom.getShape().setUserData(userdata);
        crownbottom.getShape().getGeometry().setCapability(Geometry.ALLOW_INTERSECT);
        
        /*crownlight2.getShape().setUserData(userdata); 
        crownlight2.getShape().getGeometry().setCapability(Geometry.ALLOW_INTERSECT);
        crownlight2.getShape().setUserData(userdata);
        crownlight2.getShape().getGeometry().setCapability(Geometry.ALLOW_INTERSECT);*/
        
        lightcTG.addChild(crownlight);        
        transform.addChild(lightcTG);
        //transform.addChild(crownlight2.getShape());
        transform.addChild(crownshadow.getShape());
        transform.addChild(crownbottom.getShape());
        transform.setUserData(userdata);
        return transform;
    }
      
    private TransformGroup makeCrownBL(double x, double y, double z, double cb, double cw, double h){
        TransformGroup transform = new TransformGroup();
        Transform3D translate = new Transform3D();
        Transform3D scale = new Transform3D();
        Transform3D both = new Transform3D();
        //move by vector:
        translate.set(new Vector3d(x, y+cb+((h-cb)/2), z));
        //scale by vector;
        scale.setScale(new Vector3d(1,(h-cb)/cw,1));
        both.mul(translate,scale);
        transform.setTransform(both);   
        
        Sphere crown =new Sphere((float)(cw/2), Sphere.GENERATE_NORMALS | Sphere.GEOMETRY_NOT_SHARED | Sphere.GENERATE_TEXTURE_COORDS, apcrown);
      
        crown.getShape().setUserData(userdata);
        crown.getShape().getGeometry().setCapability(Geometry.ALLOW_INTERSECT);
        crown.getShape().setBoundsAutoCompute(true);
        
        transform.addChild(crown);
        return transform;
    }
    
    public void deleteTree(){         
         bgtree.detach();
    }
}
