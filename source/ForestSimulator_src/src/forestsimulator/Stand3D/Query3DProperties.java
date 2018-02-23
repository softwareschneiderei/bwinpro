/*
 * Query3DProperties.java
 *
 * Created on 8. März 2006, 15:06
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package forestsimulator.Stand3D;
import java.util.Map;
import javax.media.j3d.VirtualUniverse;


/**
 *
 * @author jhansen
 * this class tests if the java 3d api is installed and working correctly
 */
public class Query3DProperties {
    
    /**
     * Creates a new instance of Query3DProperties 
     */
    public Query3DProperties() {        
        VirtualUniverse vu = new VirtualUniverse();   
	Map vuMap = vu.getProperties();
	System.out.println("version = " +
			   vuMap.get("j3d.version"));
	System.out.println("vendor = " +
			   vuMap.get("j3d.vendor"));
	System.out.println("specification.version = " +
			   vuMap.get("j3d.specification.version"));
	System.out.println("specification.vendor = " +
			   vuMap.get("j3d.specification.vendor"));
	System.out.println("renderer = " +
			   vuMap.get("j3d.renderer") + "\n");
    }
}
