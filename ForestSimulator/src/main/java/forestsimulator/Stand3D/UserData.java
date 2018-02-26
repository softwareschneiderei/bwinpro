package forestsimulator.Stand3D;
import treegross.base.Tree;

/**
 *
 * @author jhansen
 */
public class UserData {
        public static int NOT=0;
        public static int THINNING=1;
        public static int HARVESTING=2;
        public static int CROP=3;
        public static int TEMP_CROP=4;
        public static int HABITAT=5;
        public int type=1; //1=tree, 2=text
        public String name="";
        public String specname="";
        public double dbh=0.0;
        public double cb=0.0;
        public double h=0.0;
        public int    age=0;
        public int    spec=0;
        public double x=0;
        public double y=0;
        public double z=0;
        public boolean living=false;  
        public boolean standing=false;
        public int marker=0; //0=not marked, 1= marked for thinning, 2=marked for harvesting, 3= z-tree, 4= temp-z, 5=habitat
    
    /**
     * Creates a new instance of UserData 
     */
    public UserData(Tree tr, int t, double x3d, double y3d) {
        type=t;
        name=tr.no;
        dbh=tr.d;
        cb=tr.cb;
        h=tr.h;
        age=tr.age;
        spec=tr.code;
        specname=tr.sp.spDef.shortName;
        x=-x3d;
        y=y3d;
        z=tr.z;
        if(tr.crop) marker=CROP;
        if(tr.tempcrop) marker=TEMP_CROP;
        if(tr.habitat) marker=HABITAT;
        if(tr.out==-1) living= true;
        else living=false;
        if(tr.outtype==0) standing=true;
        else standing=false;
    }
    
}
