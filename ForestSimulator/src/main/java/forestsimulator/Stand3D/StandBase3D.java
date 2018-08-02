package forestsimulator.Stand3D;

import javax.media.j3d.*;
import javax.vecmath.*;
import treegross.base.Corners;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.geometry.Stripifier;
import com.sun.j3d.utils.picking.PickTool;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickIntersection;

/**
 *
 * @author jhansen
 */
public class StandBase3D {
    private BranchGroup bg;
    private TextureUnitState[] ta;
    private Appearance applate;
    private Point3f[] points;
    private PickTool picktool;
    private static Vector3d raydir= new Vector3d(0,-1,0);
    private double xmax=0;
    private double ymax=0;
    private double ymin=0;
    private double xmin=0;
    double corry =0;
    double corrx = 0;
    private Corners[] cp;

    public StandBase3D(Corners[] c, int np, Texture2D[] textures, boolean textured, Group parent) { 
        System.out.println("StandBase3Dnew: n corner points: "+np);
        calcMinMax( c, np);
        cp=transform(c, np);        
        if(np>2){
            bg=new BranchGroup();
            bg.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
            bg.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE );            
            bg.setCapability(BranchGroup.ALLOW_DETACH);
            bg.addChild(makeShapePlate(cp, np, textures, textured)); 
            bg.addChild(makeShapeBody(cp, np));                         
            bg.setUserData("StandBase");  
            picktool= new PickTool(bg);
            picktool.setMode(PickTool.GEOMETRY_INTERSECT_INFO );            
            bg.compile();
            parent.addChild(bg);
        }       
    }
    
    private Point3f[] makePointsPlate(Corners[] cp, int np){
        Point3f[] points= new Point3f[np];
        for(int i=0; i<np; i++){
            points[(np-1)-i]= new Point3f((float)-cp[i].x, (float)cp[i].z, (float)cp[i].y);
        }
        return points;
    }
    
     private Point3f[] makePointsBody(Corners[] cp, int np){
        Point3f[] points= new Point3f[np*4];
        int index=1;
        for(int i=0; i< np; i++){  
             if(i==(np-1)) index=-(np-1);
             points[4*i]=new Point3f((float)-cp[i].x, (float)cp[i].z, (float)cp[i].y);
             points[4*i+3]=new Point3f((float)-cp[i].x, -2f, (float)cp[i].y);
             points[4*i+2]=new Point3f((float)-cp[i+index].x, -2f, (float)cp[i+index].y);
             points[4*i+1]=new Point3f((float)-cp[i+index].x, (float)cp[i+index].z, (float)cp[i+index].y);
         } 
        return points;
    }
     
    private void calcMinMax(Corners[] cp, int np){ 
        for( int i=0; i< np; i++){
             if(cp[i].x > xmax) xmax=cp[i].x;
             if(cp[i].y > ymax) ymax=cp[i].y;
             if(cp[i].x < xmin) xmin=cp[i].x;
             if(cp[i].y < ymin) xmax=cp[i].y;
        }      
    }
    
    private Corners[] transform(Corners[] cp, int np){
        Corners[] result= new Corners[np];
        corry= ymin+((ymax-ymin)/2);
        corrx= xmin+((xmax-xmin)/2);
        for(int i=0; i<np; i++){
            result[i]=new Corners();
            result[i].x=cp[i].x-corrx;
            result[i].y=cp[i].y-corry;
            result[i].z=cp[i].z;
            result[i].no=cp[i].no;
        }
        return result;
    }
    
    private TexCoord2f[] makeTexCoordinatesPlane(Corners[] cp, int np){
        TexCoord2f[] tc = new TexCoord2f[np];        
        for(int i=0; i<np; i++){            
            tc[(np-1)-i]= new TexCoord2f((float)(cp[i].x/(xmax-xmin)),(float)(cp[i].y/(ymax-ymin)));
        }
        return tc;
    }
    
    public Shape3D makeShapePlate(Corners[] cp, int np, Texture2D[] textures, boolean textured){
        int[] stripcounts={np};
        GeometryInfo gi= new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
        gi.setCoordinates( makePointsPlate(cp, np));
        gi.setTextureCoordinateParams(1, 2);
        gi.setTextureCoordinates(0,makeTexCoordinatesPlane(cp, np));
        gi.setStripCounts(stripcounts);
//        Triangulator tr= new Triangulator();
//        tr.triangulate(gi);
        gi.recomputeIndices();
        NormalGenerator ng= new NormalGenerator();
        ng.generateNormals(gi);
        gi.recomputeIndices();
        Stripifier sf= new Stripifier();
        sf.stripify(gi);
        gi.recomputeIndices();
        Shape3D s= new Shape3D();
        applate = createMatAppearPlate(new Color3f(0.5f,0.5f,0.5f), new Color3f(0.5f,0.5f,0.5f), 5.0f, textures, textured, 4, 1);       
        s.setGeometry(gi.getGeometryArray());
        s.setCapability(Node.ALLOW_BOUNDS_READ);
        
        int n =s.numGeometries();
        for(int i=0; i<n; i++){
            s.getGeometry(i).setCapability(Geometry.ALLOW_INTERSECT);     
        }  
        picktool.setCapabilities(s, PickTool.INTERSECT_FULL );
        s.setAppearance(applate);
        return s;
    }
    
    public Shape3D makeShapeBody(Corners[] cp, int np){
        int[] stripcounts= new int[np];
        for(int i=0; i<np; i++) stripcounts[i]=4;       
        GeometryInfo gi= new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
        gi.setCoordinates( makePointsBody(cp, np));
        gi.setStripCounts(stripcounts);
//        Triangulator tr= new Triangulator();
//        tr.triangulate(gi);
        gi.recomputeIndices();
        NormalGenerator ng= new NormalGenerator();
        ng.generateNormals(gi);
        gi.recomputeIndices();
        Stripifier sf= new Stripifier();
        sf.stripify(gi);
        gi.recomputeIndices();      
        Shape3D s= new Shape3D();
        Appearance appear1 = createMatAppearBody(new Color3f(0.5f,0.5f,0.5f), new Color3f(0.5f,0.5f,0.5f), 5.0f);
        s.setGeometry(gi.getGeometryArray());      
        s.setCapability(Node.ALLOW_BOUNDS_READ);
        int n =s.numGeometries();
        for(int i=0; i<n; i++){
            s.getGeometry(i).setCapability(Geometry.ALLOW_INTERSECT);
        }        
        s.setAppearance(appear1);
        s.setPickable(false);
        return s;
    }
    
    private Appearance createMatAppearPlate(Color3f dColor, Color3f sColor, float shine, Texture2D[] bltexture, boolean textured, int tex1, int tex2) {
		Appearance appear = new Appearance();              
		Material material = new Material();
		material.setDiffuseColor(dColor);
		material.setSpecularColor(sColor);
                material.setAmbientColor(dColor);
                material.setShininess(shine);               
		appear.setMaterial(material);
                appear.setCapability(appear.ALLOW_TEXTURE_UNIT_STATE_READ);
                appear.setCapability(appear.ALLOW_TEXTURE_UNIT_STATE_WRITE);
                ta = new TextureUnitState[2];
                if(bltexture!=null){
                    TextureAttributes texAttr1 = new TextureAttributes();
                    texAttr1.setTextureMode(TextureAttributes.COMBINE);
                    TextureAttributes texAttr2 = new TextureAttributes();
                    texAttr2.setTextureMode(TextureAttributes.COMBINE_SRC_ALPHA);                    
                    ta[0]= new TextureUnitState(bltexture[tex1], texAttr1, null);
                    ta[1]= new TextureUnitState(bltexture[tex2], texAttr2, null); 
                    if(textured) appear.setTextureUnitState(ta);
                }
		return appear;
	}    
    
    private Appearance createMatAppearBody(Color3f dColor, Color3f sColor, float shine) {
		Appearance appear = new Appearance();              
		Material material = new Material();
		material.setDiffuseColor(dColor);
		material.setSpecularColor(sColor);
                material.setAmbientColor(dColor);
                material.setShininess(shine);               
		appear.setMaterial(material);                          
                return appear;
	}
    
    public void setTexttured(boolean textured){
        if(textured) applate.setTextureUnitState(ta);
        else applate.setTextureUnitState(null);
    }   
    
    public double getHeightAtPoint(double x, double z, boolean invinitive) {
        double result;
        if (invinitive) {
            result = Double.NEGATIVE_INFINITY;
        } else {
            result = 0.0;
        }
        Point3d pos = new Point3d(x, 10000, z);
        PickRay pr = new PickRay(pos, raydir);
        PickResult picked = new PickResult(bg.getChild(0), new Transform3D(), pr);
        if (picked != null) {
            if (picked.numIntersections() > 0) {
                PickIntersection intersect = picked.getIntersection(0);
                result = intersect.getPointCoordinates().y;
            }
        }
        return result;
    }
    
    public Vector3f getNormalAtPoint(double x, double z){
        Vector3f result=null;
        Point3d pos= new Point3d(x, 10000, z);
        PickRay pr= new PickRay(pos, raydir);       
        PickResult picked=new PickResult(bg.getChild(0), new Transform3D(), pr);
        if(picked!=null)if(picked.numIntersections()>0){
            PickIntersection intersect = picked.getIntersection(0);        
            result=intersect.getPointNormal();
        }        
        return result;    
    }
    
    public Corners[] getCornerPoints(){return cp;}
    public double getMinX(){return xmin;}
    public double getMinY(){return ymin;}
    public double getMaxX(){return xmax;}
    public double getMaxY(){return ymax;}
    public double getCorrX(){return corrx;}
    public double getCorrY(){return corry;}
    
    public void destroyBase(){
        picktool=null;
        ta=null;
        applate=null;
        points=null;
        bg.detach();
    }
    
}
