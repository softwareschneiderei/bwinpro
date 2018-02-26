package forestsimulator.Stand3D;

/**
 *
 * @author jhansen
 */
public class PackageInfo {
    public boolean j3dinstalled=false;
    public PackageInfo() {
	ClassLoader classLoader = getClass().getClassLoader();
        int counter=0;
	if(pkgInfo(classLoader, "javax.vecmath", "Point3d")) counter++;
	if(pkgInfo(classLoader, "javax.media.j3d", "SceneGraphObject")) counter++;
	if(pkgInfo(classLoader, "com.sun.j3d.utils.universe", "SimpleUniverse")) counter++;
        if(counter==3) j3dinstalled=true;
    }

    private static boolean pkgInfo(ClassLoader classLoader, String pkgName, String className) {
        boolean result=false;
        try {
	    classLoader.loadClass(pkgName + "." + className);
	    Package p = Package.getPackage(pkgName);
	    if (p == null) {
		System.out.println("WARNING: Package.getPackage(" +
				   pkgName +
				   ") is null");
	    }
	    else {
		System.out.println(p);
		System.out.println("Specification Title = " +
				   p.getSpecificationTitle());
		System.out.println("Specification Vendor = " +
				   p.getSpecificationVendor());
		System.out.println("Specification Version = " +
				   p.getSpecificationVersion());

		System.out.println("Implementation Vendor = " +
				   p.getImplementationVendor());
		System.out.println("Implementation Version = " +
				   p.getImplementationVersion());
                result=true;
	    }
	}
	catch (ClassNotFoundException e) {
	    System.out.println("Unable to load " + pkgName);
	}
	System.out.println();
        return result;
    } 
    public boolean isJ3DInstalled(){
        return j3dinstalled;
    }
}

