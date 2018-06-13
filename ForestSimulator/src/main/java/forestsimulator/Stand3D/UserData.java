package forestsimulator.Stand3D;

import treegross.base.Tree;

/**
 *
 * @author jhansen
 */
public class UserData {
    public int type = 1; //1=tree, 2=text
    public String name = "";
    public String specname = "";
    public double dbh = 0.0;
    public double cb = 0.0;
    public double h = 0.0;
    public int age = 0;
    public int spec = 0;
    public double x = 0;
    public double y = 0;
    public double z = 0;
    public boolean living = false;
    public boolean standing = false;
    public TreeMarker marker = TreeMarker.NOT; //0=not marked, 1= marked for thinning, 2=marked for harvesting, 3= z-tree, 4= temp-z, 5=habitat

    public UserData(Tree tr, int t, double x3d, double y3d) {
        type = t;
        name = tr.no;
        dbh = tr.d;
        cb = tr.cb;
        h = tr.h;
        age = tr.age;
        spec = tr.code;
        specname = tr.sp.spDef.shortName;
        x = -x3d;
        y = y3d;
        z = tr.z;
        if (tr.crop) {
            marker = TreeMarker.CROP;
        }
        if (tr.tempcrop) {
            marker = TreeMarker.TEMP_CROP;
        }
        if (tr.habitat) {
            marker = TreeMarker.HABITAT;
        }
        living = tr.out == -1;
        standing = tr.outtype == 0;
    }
}
