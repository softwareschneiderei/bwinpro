/*
 * FieldSquares.java
 *
 * Created on 3. April 2006, 16:14
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package forestsimulator.Stand3D;

import javax.media.j3d.*;
import javax.vecmath.*;
import treegross.base.Corners;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.geometry.Stripifier;

/**
 *
 * @author jhansen
 */
public class FieldSquares {
    private double meshwidth;
      
     public FieldSquares(double mw){
        meshwidth=mw;
     }
    
     public void makeMesh(StandBase3D base, Shape3D s){         
        makeShape(base, s);
    } 
        
    private LineStripArray makeLines(StandBase3D base){         
        double xmax=base.getMaxX();
        double ymax=base.getMaxY();
        double ymin=base.getMinY();
        double xmin=base.getMinX();
        double width=xmax-xmin;
        double length= ymax-ymin;        
         
         int npw=((int)(width/meshwidth)+1);
         int npl=((int)(length/meshwidth)+1);
         int np=(npw+npl)*2;
         int[] nlines= new int[npw+npl];
         for(int i=0; i<(npw+npl); i++){
             nlines[i]=2;
         }
         Vector3f n = new Vector3f(0.0f, 1.0f, 0.0f);      
	 n.normalize();
         LineStripArray lsa= new LineStripArray(np, LineStripArray.COORDINATES | LineStripArray.NORMALS, nlines);
         for(int i=0; i<npw; i++){            
                double x=(meshwidth*i)-base.getCorrX();                
                 lsa.setCoordinate(2*i,   new Point3f(-(float)x, 0.01f, (float)(ymin-base.getCorrY())));
                 lsa.setCoordinate(2*i+1, new Point3f(-(float)x, 0.01f, (float)(length-base.getCorrY())));  
                 lsa.setNormal(2*i, n);
                 lsa.setNormal(2*i+1, n);
          }
         int add=(npw-1)*2;
         for(int i=0; i<npl; i++){            
                double z=(meshwidth*i)-base.getCorrY();                
               lsa.setCoordinate(2*i+add,  new Point3f(-(float)(xmin-base.getCorrX()),0.01f,(float)z));
               lsa.setCoordinate(2*i+1+add,new Point3f(-(float)(width-base.getCorrX()),0.01f,(float)z)); 
               lsa.setNormal(2*i+add, n);
               lsa.setNormal(2*i+1+add, n);
        }        
        return lsa;
    }
     
     private void makeShape(StandBase3D base, Shape3D s){        
        s.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
        s.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
        Appearance a= new Appearance();
        a.setCapability(a.ALLOW_RENDERING_ATTRIBUTES_READ);
        a.setCapability(a.ALLOW_RENDERING_ATTRIBUTES_WRITE);
        LineAttributes la=new LineAttributes();
        RenderingAttributes ra= new RenderingAttributes();
        ra.setCapability(ra.ALLOW_VISIBLE_READ);
        ra.setCapability(ra.ALLOW_VISIBLE_WRITE);
        la.setLineWidth(1f);
        a.setLineAttributes(la);
        a.setRenderingAttributes(ra);
        Material material = new Material();
        material.setDiffuseColor (new Color3f(1.0f, 0.0f, 0.0f));
        material.setSpecularColor(new Color3f(1.0f, 0.0f, 0.0f));
        material.setEmissiveColor(new Color3f(1.0f, 0.0f, 0.0f));  
        a.setMaterial(material);      
        s.setGeometry(makeLines(base));
        s.setCapability(Node.ALLOW_BOUNDS_READ);
        int n =s.numGeometries();
        for(int i=0; i<n; i++){
            s.getGeometry(i).setCapability(Geometry.ALLOW_INTERSECT);
        }        
        s.setAppearance(a);
    }   
}
