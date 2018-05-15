package forestsimulator.Stand3D;
import java.util.Map;
import javax.media.j3d.VirtualUniverse;


/**
 *
 * @author jhansen
 * this class tests if the java 3d api is installed and working correctly
 */
public class Query3DProperties {
    
    public void print() {
	Map vuMap = VirtualUniverse.getProperties();
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
