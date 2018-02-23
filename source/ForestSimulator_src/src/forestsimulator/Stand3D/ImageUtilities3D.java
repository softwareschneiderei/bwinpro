/*
 * ImageUtilities3D.java
 *
 * Created on 13. Februar 2007, 09:57
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package forestsimulator.Stand3D;

import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.media.j3d.GraphicsContext3D;
import javax.media.j3d.ImageComponent;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Raster;
import javax.vecmath.Point3f;
/**
 *
 * @author jhansen
 */
public class ImageUtilities3D {
    
    public static BufferedImage getScreenShot(SimpleUniverse universe){
       int width=universe.getCanvas().getWidth();
       int height=universe.getCanvas().getHeight();      
       GraphicsContext3D  ctx = universe.getCanvas().getGraphicsContext3D();
       // the raster components need all be set
       BufferedImage img = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);


       Raster ras = new Raster(
                   new Point3f(-1.0f,-1.0f,-1.0f),
		   Raster.RASTER_COLOR,
		   0,0,
		   width,height,
		   new ImageComponent2D(
                             ImageComponent.FORMAT_RGB,
			     new BufferedImage(width,height,
					       BufferedImage.TYPE_INT_RGB)),
		   null);
                   

       ctx.readRaster(ras);
       // now strip out the image info
       img = ras.getImage().getImage();
       return img;
    }
    
    public static BufferedImage scale(BufferedImage bsrc, int newwidth, int newheight){
        BufferedImage bdest = new BufferedImage(newwidth, newheight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bdest.createGraphics();
        AffineTransform at =
            AffineTransform.getScaleInstance((double)newwidth/bsrc.getWidth(),
            (double)newheight/bsrc.getHeight());
        g.drawRenderedImage(bsrc,at);
        return bdest;
    }
    
    
    
}
