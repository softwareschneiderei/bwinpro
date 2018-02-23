/*
 * Disc.java
 *
 * Created on 23. Februar 2006, 16:27
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
public class Disc {
    public Shape3D shape;
    
    /** Creates a new instance of Disc */
    public Disc(double x, double y, double z, double radius, int quality, Appearance a, Vector3f normal, boolean texture, Color3f color, StandBase3D base) {
        int[] psia= new int[1];        
        if (quality<3) quality=3;
        int nvertices=(quality+1)*3;
        int npoints=(quality+1);
        psia[0]=nvertices;
        IndexedTriangleFanArray tfa;
        if(texture) tfa= new IndexedTriangleFanArray(npoints, IndexedTriangleFanArray.COORDINATES | 
                                                     IndexedTriangleFanArray.NORMALS | IndexedTriangleFanArray.TEXTURE_COORDINATE_2 | IndexedTriangleFanArray.COLOR_4, 
                                                     nvertices, psia); 
        
        else tfa= new IndexedTriangleFanArray(npoints, IndexedTriangleFanArray.COORDINATES | 
                                              IndexedTriangleFanArray.NORMALS | IndexedTriangleFanArray.TEXTURE_COORDINATE_2 | IndexedTriangleFanArray.COLOR_4, 
                                              nvertices, psia);
        //middelpoint
        Point3d middelpoint;
        if(base==null)middelpoint = new Point3d(x,y,z);
        else middelpoint = new Point3d(x,base.getHeightAtPoint(x,z, false)+0.02,z);             
        Vector3f n=normal;        
        boolean top=true;
        if(n.y<0) top=false;
	n.normalize();
        // vertices: 1. middelpoint
        tfa.setCoordinate(0, middelpoint);
        tfa.setColor(0, new Color4f(color.x, color.y, color.z, 1f));
        tfa.setNormal( 0, n);
        TexCoord2f texcoord= new TexCoord2f(0.5f, 0.5f);
        if(texture) tfa.setTextureCoordinate(0,0,texcoord);
        
        // vertices: 2. outer points:
         int j=0;
         int k=0;
         double inc = 2.0*Math.PI/quality;
         for(int i=1; i< npoints; i++){ 
             if(top)k=(quality-i);
             else k=i;
             double z1 =radius* Math.sin(inc*k) + z;
	     double x1 =radius* Math.cos(inc*k) + x;
             
             Point3d point=new Point3d(radius* Math.cos(inc*k) , 0, radius* Math.sin(inc*k));
             point.x=point.x+x;                
             point.z=point.z+z;
             if(base!=null)point.y=base.getHeightAtPoint(point.x, point.z, false)+0.02;
             else point.y=y;
             tfa.setCoordinate(i,point);
             
             double tu,tv;
             tu=(Math.sin(inc*k)+1)*0.5;
             tv=(Math.cos(inc*k)+1)*0.5;
             if(texture){
                 texcoord= new TexCoord2f( (float)tu,(float)tv);
                 tfa.setTextureCoordinate(0,i,texcoord);
             }
             tfa.setNormal( i, n);
             tfa.setColor(i, new Color4f(0f, 0f, 0f, 0f));
            
             tfa.setCoordinateIndex( j*3  , 0);
             tfa.setCoordinateIndex( j*3+1, i);            
             if(i<(quality)) tfa.setCoordinateIndex( j*3+2, i+1);
             else tfa.setCoordinateIndex( j*3+2, 1);
             
             tfa.setColorIndex( j*3  , 0);
             tfa.setColorIndex( j*3+1, i);            
             if(i<(quality)) tfa.setColorIndex( j*3+2, i+1);             
             else tfa.setColorIndex( j*3+2, 1);
             
             if(texture) {
                tfa.setTextureCoordinateIndex(0, j*3  , 0);
                tfa.setTextureCoordinateIndex( 0,j*3+1, i);            
                if(i<(quality)) tfa.setTextureCoordinateIndex( 0,j*3+2, i+1);
                else tfa.setTextureCoordinateIndex( 0,j*3+2, 1);
             }
             j++;
         } 
         shape = new Shape3D(tfa, a);  
         shape.setAppearanceOverrideEnable(false);
         shape.getGeometry().setCapability(Geometry.ALLOW_INTERSECT);      
         shape.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
         shape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
    }
    
    public Shape3D getShape(){        
        return shape;        
    }
    
}
