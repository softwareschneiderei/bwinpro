/*
 * FrustrumNew.java
 *
 * Created on 5. April 2006, 16:57
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package forestsimulator.Stand3D;

import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.Triangulator;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.geometry.Stripifier;

/**
 *
 * @author jhansen
 */
public class FrustrumNew {
   
   Shape3D shape;
    
    /** Creates a new instance of Frustrum */
    public FrustrumNew(float x, float y, float z, float rbottom, float rtop, float length, int quality, Appearance a) {
        if(quality<3) quality=3;
        shape= makeShapeBody(x, y, z, rbottom, rtop, length, quality, a);
    }
    
    private Point3f[] makePointsBody(float x, float y, float z, float rbottom, float rtop, float length, int quality){
        Point3f[] points= new Point3f[quality*4];
        double inc = 2.0*Math.PI/quality;
        for(int i=0; i< quality; i++){      
             float zb = rbottom * (float)Math.sin((double)i*inc) + z;
             float xb = rbottom * (float)Math.cos((double)i*inc) + x;
             float zb2 = rbottom * (float)Math.sin((double)(i+1)*inc) + z;
             float xb2 = rbottom * (float)Math.cos((double)(i+1)*inc) + x;         
             float zt = rtop * (float)Math.sin((double)i*inc) + z;
             float xt = rtop * (float)Math.cos((double)i*inc) + x;
             float zt2 = rtop * (float)Math.sin((double)(i+1)*inc) + z;
             float xt2 = rtop * (float)Math.cos((double)(i+1)*inc) + x;             
             points[4*i]=  new Point3f(xt,  y+length, zt);
             points[4*i+3]=new Point3f(xb,  y,        zb);
             points[4*i+2]=new Point3f(xb2, y,        zb2);
             points[4*i+1]=new Point3f(xt2, y+length, zt2);
         } 
        return points;
    }
    
    
     private Point3f[] makePointsBody(float x, float y, float z, float xo, float zo, float rbottom, float rtop, float length, int quality){
        Point3f[] points= new Point3f[quality*4];
        double inc = 2.0*Math.PI/quality;
        for(int i=0; i< quality; i++){      
             float zb = rbottom * (float)Math.sin((double)i*inc) + z;
             float xb = rbottom * (float)Math.cos((double)i*inc) + x;
             float zb2 = rbottom * (float)Math.sin((double)(i+1)*inc) + z;
             float xb2 = rbottom * (float)Math.cos((double)(i+1)*inc) + x;         
             float zt = rtop * (float)Math.sin((double)i*inc) + zo;
             float xt = rtop * (float)Math.cos((double)i*inc) + xo;
             float zt2 = rtop * (float)Math.sin((double)(i+1)*inc) + zo;
             float xt2 = rtop * (float)Math.cos((double)(i+1)*inc) + xo;             
             points[4*i]=  new Point3f(xt,  y+length, zt);
             points[4*i+3]=new Point3f(xb,  y,        zb);
             points[4*i+2]=new Point3f(xb2, y,        zb2);
             points[4*i+1]=new Point3f(xt2, y+length, zt2);
         } 
        return points;
    } 
    
    
     private TexCoord2f[] makeTexCoordinates(int quality, double tr){
        TexCoord2f[] tc = new TexCoord2f[quality*4];
        double inc = 2.0*Math.PI/quality;
        for(int i=0; i<quality; i++){            
            tc[4*i]  = new TexCoord2f((float)Math.cos((double)i*inc*tr), 0);
            tc[4*i+3]= new TexCoord2f((float)Math.cos((double)i*inc*tr), (float)(1*tr));
            tc[4*i+2]= new TexCoord2f((float)Math.cos((double)(i+1)*inc*tr), (float)(1*tr));
            tc[4*i+1]= new TexCoord2f((float)Math.cos((double)(i+1)*inc*tr), 0);
        }
        return tc;
    }
    
    private Shape3D makeShapeBody(float x, float y, float z, float rbottom, float rtop, float length, int quality, Appearance a){
        int[] stripcounts= new int[quality];
        for(int i=0; i<quality; i++) stripcounts[i]=4;       
        GeometryInfo gi= new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
        gi.setCoordinates( makePointsBody(x,y,z, rbottom, rtop,length, quality));
        gi.setTextureCoordinateParams(1, 2);
        gi.setTextureCoordinates(0,makeTexCoordinates(quality, 0.3));   
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
        s.setGeometry(gi.getGeometryArray());
        s.setCapability(Node.ALLOW_BOUNDS_READ);
        int n =s.numGeometries();
        for(int i=0; i<n; i++){
            s.getGeometry(i).setCapability(Geometry.ALLOW_INTERSECT);
        }        
        s.setAppearance(a);
        return s;
    }    
    public Shape3D getShape(){
        return shape;
    }
    
}
